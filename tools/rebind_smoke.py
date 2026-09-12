"""Verify Rapid OCR after idle unbinding while its process stays alive.

Requires AutoJs6 and the enabled, authorized Rapid plugin on the chosen device.
Does not stop processes or change Plugin Center preferences. Run for each variant:
  python tools/rebind_smoke.py emulator-5580 --variant v6 --adb /path/to/adb
"""

import argparse
import json
import re
import shlex
import subprocess
import time
from pathlib import Path


SCRIPT = r'''
var receipt = {rounds: [], complete: false, ok: false};
var output = new java.io.File(context.getExternalFilesDir(null), '__RECEIPT__').getAbsolutePath();
var image;
function save() { files.write(output, JSON.stringify(receipt)); }
try {
    var bitmap = android.graphics.Bitmap.createBitmap(640, 160, android.graphics.Bitmap.Config.ARGB_8888);
    var canvas = new android.graphics.Canvas(bitmap);
    canvas['drawColor(int)'](android.graphics.Color.WHITE);
    var paint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
    paint['setColor(int)'](android.graphics.Color.BLACK);
    paint.setTextSize(48);
    canvas.drawText('Hello AutoJs6', 30, 90, paint);
    image = Packages.org.autojs.autojs.core.image.ImageWrapper.ofBitmap(runtime, bitmap);
    var options = {variant: '__VARIANT__'};
    for (var i = 0; i < __CYCLES__; i++) {
        var text = ocr.rapid(image, options).join(' ');
        var blocks = ocr.rapid.detect(image, options);
        var bounded = blocks.length > 0 && blocks.every(function (block) {
            return block.bounds.width() > 0 && block.bounds.height() > 0;
        });
        receipt.rounds.push({round: i, text: text, detectText: blocks.map(function (b) {return b.text;}).join(' '), bounded: bounded});
        save();
        if (i + 1 < __CYCLES__) sleep(__IDLE_MS__);
    }
    var first = receipt.rounds[0].text;
    receipt.ok = first.indexOf('Hello') >= 0 && receipt.rounds.every(function (row) {
        return row.text === first && row.detectText === first && row.bounded;
    });
} catch (e) {
    receipt.error = String(e) + '\n' + e.stack;
} finally {
    if (image) image.recycle();
    receipt.complete = true;
    save();
}
'''


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument('serial')
    parser.add_argument('--variant', choices=['v3', 'v6'], required=True)
    parser.add_argument('--adb', default='adb')
    parser.add_argument('--cycles', type=int, default=3)
    parser.add_argument('--idle-seconds', type=int, default=35)
    parser.add_argument('--out', type=Path, default=Path('build/rebind-smoke'))
    args = parser.parse_args()
    if args.cycles < 2 or args.idle_seconds < 33:
        parser.error('Use at least 2 cycles and idle for at least 33 seconds (host idle timeout is 30 seconds)')
    package = f'io.github.supermonster003.autojs6.plugin.rapidocr.ppocr{args.variant}'
    name = f'rapid-{args.variant}-rebind-{time.time_ns()}.json'
    remote = f'/sdcard/Android/data/org.autojs.autojs6/files/{name}'
    script = (SCRIPT.replace('__RECEIPT__', name).replace('__VARIANT__', args.variant)
              .replace('__CYCLES__', str(args.cycles)).replace('__IDLE_MS__', str(args.idle_seconds * 1000)))

    def adb(*command, check=True):
        result = subprocess.run([args.adb, '-s', args.serial, *command], capture_output=True,
                                text=True, encoding='utf-8', errors='replace', timeout=30)
        if check and result.returncode:
            raise RuntimeError(result.stdout + result.stderr)
        return result.stdout.strip()

    print(adb('shell', shlex.join(['am', 'start', '-n',
          'org.autojs.autojs6/org.autojs.autojs.external.open.RunIntentActivity', '--es', 'script', script])), flush=True)
    observations = []
    receipt = {}
    deadline = time.monotonic() + args.cycles * (args.idle_seconds + 60)
    while time.monotonic() < deadline:
        raw = adb('shell', 'cat', remote, check=False)
        try:
            receipt = json.loads(raw)
        except json.JSONDecodeError:
            time.sleep(1)
            continue
        if len(receipt['rounds']) > len(observations):
            service = adb('shell', 'dumpsys', 'activity', 'services', package)
            token = re.search(r'ServiceRecord\{([0-9a-f]+)\s+u\d+\s+' + re.escape(package) + r'/', service)
            observations.append({'round': len(receipt['rounds']) - 1,
                                 'pid': adb('shell', 'pidof', package),
                                 'service': token.group(1) if token else None})
            print(json.dumps({**receipt['rounds'][-1], **observations[-1]}), flush=True)
        if receipt.get('complete'):
            break
        time.sleep(1)
    receipt.update(variant=args.variant, lifecycle=observations)
    receipt['sameProcess'] = bool(observations) and bool(observations[0]['pid']) and len({r['pid'] for r in observations}) == 1
    receipt['serviceRecreated'] = (len(observations) == args.cycles
                                   and all(r['service'] for r in observations)
                                   and len({r['service'] for r in observations}) == args.cycles)
    receipt['ok'] = bool(receipt.get('ok') and receipt['sameProcess'] and receipt['serviceRecreated'])
    args.out.mkdir(parents=True, exist_ok=True)
    (args.out / name).write_text(json.dumps(receipt, ensure_ascii=False, indent=2) + '\n', encoding='utf-8')
    if not receipt['ok']:
        raise AssertionError(json.dumps(receipt, ensure_ascii=True))
    print(f'PASS: {args.variant}, {args.cycles} service instances, one process, stable recognition and bounds', flush=True)


if __name__ == '__main__':
    main()
