These tests compile the production JNI converter and string helper, then run them on ART through `app_process -Xcheck:jni`. They do not install an APK, change device-wide VM settings, or run OCR models. Java result objects come from the production DTO sources. Temporary device files are removed after the run.

Prepare the native dependencies with `gradlew.bat :libs:rapidocr:externalNativeBuildRelease` (or `:libs:paddleocr:externalNativeBuildRelease` in the Paddle plugin), then run:

```powershell
python tools/jni/run_jni_tests.py --serial DEVICE_SERIAL --sdk E:/.android/sdk --jdk E:/.java/jdk-21.0.12.1
```

Adjust the SDK/JDK paths for your machine. The runner uses the NDK version from `version.properties`, Android SDK platform 36, and build-tools 36.0.0 (`--build-tools` can override the last value). It selects the connected device's ABI. Rapid supports all four ABIs; Paddle's native dependency package supports the two ARM ABIs.

The JNI proxy delegates every operation to the real VM. It counts references owned by the converter/helper and injects Java exceptions at each failable call in small representative conversions. Its counters are printed in this order:

```text
[peak local references, retained references, FindClass calls, operations, invalid JNI calls/deletions, pinned byte arrays]
```

The string cases cover null, empty strings, model paths, Chinese text, supplementary Unicode characters, and embedded NUL bytes. Each input is converted 10,000 times within one native call. Successful and failed conversions must leave zero temporary references and no pinned arrays.

Rapid additionally checks empty results, 1/100/5,000 text blocks, empty score arrays, zero-point and 1,024-point boxes, every returned field after GC, and all injected failure paths. A successful conversion retains only the final Java result reference. Class lookups remain constant, and the converter's peak must stay within 16 references. Run on Android 7.x as well as a modern Android version.

To compare a saved Rapid native source tree before the fix, add `--native-source PATH_TO_SAVED_CPP_DIRECTORY --baseline`. Use Android 8+ for this mode: the old converter intentionally retains more than 512 references, which may abort Android 7.x. Baseline mode skips the string tests and confirms reference growth for 100 text blocks.

Reports are written to `build/reports/jni/ABI/api-N.log`, with a separate `ABI-baseline` directory for the baseline. The standalone JNI library is confined to this test directory and is not part of release APKs.
