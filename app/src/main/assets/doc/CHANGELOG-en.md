******

### Release History

******

# v1.0.3

###### 2026/09/12

* `Fix` Incorrect recognized text after plugin service recreation or repeated engine initialization caused by appending the OCR dictionary again
* `Fix` Old ONNX sessions retained during reinitialization and races between initialization and recognition in the same process

# v1.0.2

###### 2026/09/12

* `Fix` Fixed the missing `libc++_shared.so` in the packaged native libraries: CMake now uses the `c++_shared` STL explicitly so AGP bundles the NDK r28.2 libc++ for all 4 ABIs, and the OCR engine loads again on 16 KB page size devices
* `Fix` The `clean` task failed when the Rapid OCR native dependency cleanup flag was not initialized
* `Improvement` Synced the OpenCV 4.8.0 native library to the NDK r28c (Clang 19.0.1) rebuild (donor: AutoJs6-Plugin-OpenCV); `libopencv_java4.so` for all 4 ABIs keeps 16 KB `PT_LOAD` alignment and ships with a provenance manifest

# v1.0.1

###### 2026/09/11

* `Fix` A crash when Paddle OCR and Rapid OCR are used sequentially in the same packaged app
* `Improvement` Build verification of 16 KB page alignment for 64-bit native libraries, including manifest contract checks and JSON reports
* `Dependency` Upgraded ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) version 1.18.0 -> 1.21.1
* `Dependency` Upgraded OpenCV version from 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `Feature` Added the Rapid OCR (PP-OCRv6 Small) plugin identity with plugin ID `rapid-ocr-pp-ocrv6`, engine `rapid-ocr`, and variant `pp-ocrv6`.
* `Feature` Added the `PADDLE_OCR` AIDL service with `recognizeText` and `detect`. `recognizeText` returns `text` strings, while `detect` returns `text`, `confidence`, and `bounds` for each result.
* `Feature` Supported encoded images and raw `ARGB_8888` images. Android 13 uses `SharedMemory` when available, with a stream fallback when `SharedMemory` is unavailable or fails.
* `Feature` Added local text detection and recognition with `PP-OCRv6 Small` detection and recognition models, `ONNX Runtime`, and `OpenCV Mobile`.
* `Feature` Supported `maxSideLen` and `scoreThreshold` options for recognition.
* `Feature` Connected `preBuild` to download model assets, verify their `SHA-256` digests, and synchronize native `ONNX Runtime` libraries.
* `Feature` Added localized plugin metadata and usage instructions for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese.
* `Feature` Added `JSON` sources and a `Python` generator for multilingual `README` and `CHANGELOG` documents.
* `Feature` Added APK variants for `arm64-v8a`, `armeabi-v7a`, `x86_64`, and `universal`. Release filenames include the version and `ABI`, and archived copies include a `CRC32` digest.
* `Improvement` Standardize the README layout and Gradle platform version management
