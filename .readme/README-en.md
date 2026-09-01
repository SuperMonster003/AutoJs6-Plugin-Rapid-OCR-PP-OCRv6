<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Rapid OCR Plugin for AutoJs6 PP-OCRv6 Small text recognition</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Languages

******

The current README.md supports the following languages:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- English [en] # current
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### Introduction

******

The AutoJs6 Rapid OCR PP-OCRv6 Small Plugin provides text detection and recognition based on RapidOCR, ONNX Runtime, and OpenCV Mobile. It can return recognized text, confidence values, and text bounds.

******

### Features

******

- Provides the shared OCR plugin service with plugin ID `rapid-ocr-pp-ocrv6`, engine `rapid-ocr`, and variant `pp-ocrv6`.
- Exposes the OCR AIDL interface through `org.autojs.plugin.PADDLE_OCR` and works with the AutoJs6 host OCR plugin discovery flow.
- Supports `ocr.rapid.recognizeText` for string lists and `ocr.rapid.detect` for text, confidence values, and bounds.
- Supports encoded image input and raw `ARGB_8888` image buffer input.
- Provides `arm64-v8a`, `armeabi-v7a`, `x86_64`, and `universal` APKs.
- Plugin metadata, usage instructions, README, and CHANGELOG are localized for Spanish, French, Russian, Arabic, Japanese, Korean, English, Simplified Chinese, Hong Kong Traditional Chinese, and Taiwan Traditional Chinese.
- Built on RapidOCR, ONNX Runtime, and OpenCV Mobile.

******

### Usage Examples

******

After reading an image, use the Rapid OCR module to recognize text or obtain detailed results:

```js
let image = images.read("./sample.png");

let texts = ocr.rapid.recognizeText(image, {
    variant: "pp-ocrv6",
});
console.log(texts);

let results = ocr.rapid.detect(image, {
    variant: "pp-ocrv6",
    maxSideLen: 1024,
    scoreThreshold: 0.5,
});
console.log(results);

image.recycle();
```

`recognizeText` returns a string list, while `detect` returns a list of detailed recognition results.

******

### Plugin Interface

******

The host can discover and invoke the plugin with the following identity:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86_64, universal
```

Each `detect` result contains `text`, `confidence`, and `bounds`; the current interface does not provide a quad field.

******

### Recognition Options

******

The current plugin implementation reads the following recognition options:

- `maxSideLen`: Maximum side length used during detection. The default is `1024`.
- `scoreThreshold`: Text box score threshold. The default is `0.5`.

******

### Model Assets

******

The build downloads and verifies the following 4 assets from ModelScope:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small text detection model.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small text recognition model.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: Auxiliary model asset required during engine initialization.
- `ppocrv6_dict.txt`: PP-OCRv6 recognition dictionary.

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

##### For more release history

* [CHANGELOG-en.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-en.md)

******

### Build

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release build:

```powershell
.\gradlew.bat :app:assembleRelease
```

Build parameters come from `version.properties`; the current minimum SDK is 24 and target SDK is 36. `preBuild` downloads and verifies model assets, then synchronizes ONNX Runtime native libraries.

******

### Resource Layout

******

```text
.readme/lang_*.json
.changelog/lang_*.json
.python/generate_markdown.py
app/src/main/assets/doc/CHANGELOG-*.md
app/src/main/res/values-*/strings.xml
app/src/main/res/raw-*/plugin_instruction.md
libs/rapidocr/src/main/assets/models/
```

`strings.xml` contains localized plugin descriptions, and `plugin_instruction.md` contains usage instructions displayed by the host. README and CHANGELOG files are generated from JSON sources by `.python/generate_markdown.py`. Only `README.md` is generated in the repository root, and no root `CHANGELOG.md` is generated.

******

### Links

******

- AutoJs6 OCR documentation: https://docs.autojs6.com/#/ocr
- RapidOCR project: https://github.com/RapidAI/RapidOCR
- ModelScope model assets: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile
