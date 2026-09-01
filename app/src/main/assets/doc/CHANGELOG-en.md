******

### Release History

******

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
