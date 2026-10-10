"""Write or check the provenance record of the private OpenCV static libraries.

The native engine links the static OpenCV libraries of the official OpenCV Android SDK release.
This script reads that release archive, resolves the static link closure of the requested
modules for every ABI from the SDK's own CMake export files, and records the archive hash,
the per-ABI archive member hashes and the upstream compiler identification.

The Gradle build verifies the downloaded archive and the extracted libraries against the record,
so an OpenCV upgrade is: download the new SDK, run this script with the new tag and commit,
update RAPID_OCR_OPENCV_VERSION, rebuild and re-verify on devices.

Example:
    python -I tools/opencv/opencv_sdk_provenance.py --archive opencv-4.8.0-android-sdk.zip \
        --version 4.8.0 --commit f9a59f2592993d3dcc080e495f4f5e02dd8ec7ef \
        --modules core imgproc --abis arm64-v8a armeabi-v7a x86 x86_64 \
        --out libs/opencv-android-sdk-4.8.0.provenance.json
Use --check instead of writing to compare an existing record with the archive.
"""
import argparse
import hashlib
import json
from pathlib import Path
import re
import sys
import zipfile

PREFIX = "OpenCV-android-sdk/"
COMPILER = re.compile(rb"Android \([^)]*\) clang version [0-9.]+(?: \([^)]*\))*")


def read_member(archive, name):
    return archive.read(PREFIX + name)


def link_closure(archive, abi, modules):
    """Returns sdk/native relative paths of the static archives linked for the given modules."""
    config = f"sdk/native/jni/abi-{abi}/"
    exports = read_member(archive, config + "OpenCVModules.cmake").decode("utf-8")
    locations = read_member(archive, config + "OpenCVModules-release.cmake").decode("utf-8")
    interfaces = {name: value for name, value in re.findall(
        r'set_target_properties\(([\w-]+) PROPERTIES\s+INTERFACE_LINK_LIBRARIES "([^"]*)"', exports)}
    files = {name: path for name, path in re.findall(
        r'set_target_properties\(([\w-]+) PROPERTIES[^)]*?IMPORTED_LOCATION_RELEASE "\$\{_IMPORT_PREFIX\}/([^"]+)"',
        locations, re.S)}
    # OpenCVConfig.cmake declares a few prebuilt dependencies itself, such as ippicv on x86.
    config_text = read_member(archive, config + "OpenCVConfig.cmake").decode("utf-8")
    files.update({name: path for name, path in re.findall(
        r'set_target_properties\(([\w-]+) PROPERTIES[^)]*?IMPORTED_LOCATION "\$\{OpenCV_INSTALL_PATH\}/([^"]+)"',
        config_text, re.S)})
    pending = [f"opencv_{module}" for module in modules]
    seen = []
    while pending:
        target = pending.pop(0)
        if target in seen:
            continue
        if target not in files:
            if target.startswith("opencv_"):
                raise SystemExit(f"{abi}: OpenCV module {target} is not a static library in this SDK")
            continue  # System libraries such as log, m, dl and z.
        seen.append(target)
        for item in interfaces.get(target, "").split(";"):
            item = re.sub(r"^\\?\$<LINK_ONLY:(.+)>$", r"\1", item.strip())
            if item:
                pending.append(item)
    return sorted(files[target] for target in seen)


def build_record(archive_path, version, commit, modules, abis):
    data = archive_path.read_bytes()
    record = {
        "schemaVersion": 1,
        "component": "OpenCV",
        "opencvVersion": version,
        "upstream": {
            "repository": "https://github.com/opencv/opencv",
            "tag": version,
            "commit": commit,
            "archive": f"https://github.com/opencv/opencv/releases/download/{version}/opencv-{version}-android-sdk.zip",
            "archiveSize": len(data),
            "archiveSha256": hashlib.sha256(data).hexdigest(),
        },
        "linkage": "static",
        "modules": list(modules),
        "parallelBackend": "TBB from the SDK; no OpenMP runtime",
        "symbolVisibility": "hidden; only JNI entry points are exported",
        "compilers": [],
        "staticLibraries": {},
    }
    compilers = set()
    with zipfile.ZipFile(archive_path) as archive:
        for abi in abis:
            entries = {}
            for path in link_closure(archive, abi, modules):
                member = read_member(archive, path)
                entries[path.removeprefix("sdk/native/")] = hashlib.sha256(member).hexdigest()
                compilers.update(match.decode("utf-8", "replace") for match in COMPILER.findall(member))
            record["staticLibraries"][abi] = entries
    record["compilers"] = sorted(compilers)
    return record


def main():
    parser = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawDescriptionHelpFormatter)
    parser.add_argument("--archive", type=Path, required=True)
    parser.add_argument("--version", required=True)
    parser.add_argument("--commit", required=True, help="Upstream commit of the release tag")
    parser.add_argument("--modules", nargs="+", required=True)
    parser.add_argument("--abis", nargs="+", required=True)
    parser.add_argument("--out", type=Path, required=True)
    parser.add_argument("--check", action="store_true")
    args = parser.parse_args()
    record = build_record(args.archive, args.version, args.commit, args.modules, args.abis)
    text = json.dumps(record, indent=2) + "\n"
    if args.check:
        current = args.out.read_text(encoding="utf-8")
        if current.replace("\r\n", "\n") != text:
            print(f"{args.out} does not match {args.archive}", file=sys.stderr)
            return 1
        print(f"{args.out} matches {args.archive}")
        return 0
    args.out.write_text(text, encoding="utf-8", newline="\n")
    print(f"Wrote {args.out}")
    return 0


if __name__ == "__main__":
    sys.exit(main())
