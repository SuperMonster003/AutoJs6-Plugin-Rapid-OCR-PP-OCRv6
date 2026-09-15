"""Compile production JNI sources and run isolated ART tests without installing an APK.

First build the native module (to prepare OpenCV/ONNX headers). Supply --serial,
--sdk and --jdk. Tests use app_process with CheckJNI, without changing VM properties.
--native-source and --baseline can probe a saved pre-fix Rapid source directory;
run that baseline only on Android 8+ because it intentionally retains >512 refs.
"""

import argparse
import os
from pathlib import Path
import shlex
import subprocess
import sys
import uuid
import zipfile


ROOT = Path(__file__).resolve().parents[2]
HERE = Path(__file__).resolve().parent


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--serial', required=True)
    parser.add_argument('--sdk', type=Path, default=os.environ.get('ANDROID_HOME'))
    parser.add_argument('--jdk', type=Path, default=os.environ.get('JAVA_HOME'))
    parser.add_argument('--build-tools', default='36.0.0')
    parser.add_argument('--native-source', type=Path)
    parser.add_argument('--baseline', action='store_true')
    args = parser.parse_args()
    if args.sdk is None or args.jdk is None:
        parser.error('Supply --sdk/ANDROID_HOME and --jdk/JAVA_HOME')
    sdk, jdk = args.sdk.resolve(), args.jdk.resolve()
    suffix = '.exe' if os.name == 'nt' else ''
    adb = sdk / 'platform-tools' / ('adb' + suffix)
    rapid = (ROOT / 'libs/rapidocr').is_dir()
    properties = dict(line.split('=', 1) for line in
                      (ROOT / 'version.properties').read_text().splitlines()
                      if '=' in line and not line.startswith('#'))
    ndk = sdk / 'ndk' / properties['RAPID_OCR_NDK_VERSION' if rapid else 'PADDLE_OCR_NDK_VERSION']
    host = 'windows-x86_64' if os.name == 'nt' else ('darwin-x86_64' if sys.platform == 'darwin' else 'linux-x86_64')
    toolchain = ndk / 'toolchains/llvm/prebuilt' / host

    def device(*command):
        return subprocess.check_output([str(adb), '-s', args.serial, *command], text=True).strip()

    abi = device('shell', 'getprop', 'ro.product.cpu.abi')
    api = int(device('shell', 'getprop', 'ro.build.version.sdk'))
    if args.baseline and (not rapid or api < 26):
        parser.error('The baseline requires Rapid OCR and Android 8+ (API 26+)')
    triples = {'x86': 'i686-linux-android', 'x86_64': 'x86_64-linux-android',
               'arm64-v8a': 'aarch64-linux-android', 'armeabi-v7a': 'arm-linux-androideabi'}
    triple = triples[abi]
    target = 'armv7a-linux-androideabi24' if abi == 'armeabi-v7a' else triple + '24'
    out = ROOT / 'build/reports/jni' / (abi + ('-baseline' if args.baseline else ''))
    out.mkdir(parents=True, exist_ok=True)
    log = out / ('api-' + str(api) + '.log')
    log.write_text(f'{ROOT.name} API={api} ABI={abi}\n', encoding='utf-8')

    def run(command):
        completed = subprocess.run([str(part) for part in command], stdout=subprocess.PIPE,
                                   stderr=subprocess.STDOUT, text=True, encoding='utf-8', errors='replace')
        with log.open('a', encoding='utf-8') as stream:
            stream.write(subprocess.list2cmdline([str(part) for part in command]) + '\n')
            stream.write(completed.stdout + '\n')
        if completed.returncode:
            print(completed.stdout, flush=True)
            raise subprocess.CalledProcessError(completed.returncode, command)
        return completed.stdout

    java_files = [HERE / 'JniStringTest.java']
    native = args.native_source or ROOT / ('libs/rapidocr/src/main/cpp' if rapid else 'libs/paddleocr/src/main/cpp')
    if not native.is_dir():
        parser.error('Native source directory does not exist: ' + str(native))
    sources = [HERE / 'jni_reference_test.cpp']
    flags = ['-DORT_API_MANUAL_INIT']
    if rapid:
        module = ROOT / 'libs/rapidocr'
        cv = module / 'build/opencv-native'
        includes = [native / 'include', cv / 'headers', module / 'build/onnxruntime-native/headers']
        cv_library = cv / 'jni' / abi / 'libopencv_java4.so'
        sources += [native / 'src/OcrResultUtils.cpp', native / 'src/OcrUtils.cpp']
        dto = module / 'src/main/java/com/benjaminwan/ocrlibrary'
        java_files += [HERE / 'JniResultTest.java', *[dto / (name + '.java') for name in ['Point', 'TextBlock', 'OcrResult']]]
        entrypoint = 'com.benjaminwan.ocrlibrary.JniResultTest'
    else:
        cv = ROOT / 'libs/paddleocr/src/sdk/native'
        includes = [native, cv / 'jni/include']
        cv_library = cv / 'libs' / abi / 'libopencv_java4.so'
        flags += ['-DTEST_PADDLE']
        entrypoint = 'org.autojs.ocr.jni.JniStringTest'
    runtime = toolchain / 'sysroot/usr/lib' / triple / 'libc++_shared.so'
    library = out / 'libjni_reference_test.so'
    compile_command = [toolchain / 'bin' / ('clang++' + suffix), '--target=' + target,
                       '-std=c++17' if rapid else '-std=c++11', '-shared', '-fPIC', '-O1',
                       '-ffunction-sections', '-fdata-sections', '-fvisibility=hidden',
                       '-fvisibility-inlines-hidden', '-Wl,--gc-sections', '-Wl,--no-undefined',
                       '-Wl,-z,max-page-size=16384', '-nostdlib++', *flags]
    compile_command += ['-I' + str(path) for path in includes]
    compile_command += [*sources, cv_library, runtime, '-llog', '-landroid', '-ljnigraphics', '-o', library]
    run(compile_command)

    android_jar = sdk / 'platforms/android-36/android.jar'
    classes = out / 'classes'
    dex = out / 'dex'
    classes.mkdir(exist_ok=True)
    dex.mkdir(exist_ok=True)
    run([jdk / 'bin' / ('javac' + suffix), '-encoding', 'UTF-8', '-source', '8', '-target', '8',
         '-cp', android_jar, '-d', classes, *java_files])
    run([jdk / 'bin' / ('java' + suffix), '-cp', sdk / 'build-tools' / args.build_tools / 'lib/d8.jar',
         'com.android.tools.r8.D8', '--lib', android_jar, '--min-api', '24', '--output', dex,
         *sorted(classes.rglob('*.class'))])
    jar = out / 'tests.jar'
    with zipfile.ZipFile(jar, 'w') as archive:
        archive.write(dex / 'classes.dex', 'classes.dex')

    remote = '/data/local/tmp/autojs-jni-' + uuid.uuid4().hex
    try:
        run([adb, '-s', args.serial, 'shell', 'mkdir', '-p', remote])
        for file in [library, jar, cv_library, runtime]:
            run([adb, '-s', args.serial, 'push', file, remote + '/' + file.name])
        command = ['env', 'CLASSPATH=' + remote + '/tests.jar', 'LD_LIBRARY_PATH=' + remote,
                   'app_process', '-Xcheck:jni', '/system/bin', entrypoint, remote + '/' + library.name]
        if args.baseline:
            command.append('baseline')
        print(run([adb, '-s', args.serial, 'shell', shlex.join(command)]), flush=True)
    finally:
        run([adb, '-s', args.serial, 'shell', 'rm', '-rf', remote])
    print('Report: ' + str(log), flush=True)


if __name__ == '__main__':
    main()
