<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用於 AutoJs6 PP-OCRv6 Small 文字識別的 Rapid OCR 插件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 語言 (Languages)

******

目前 README.md 支援以下語言:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- 繁體中文 (香港) [zh-Hant-HK] # 目前
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### 簡介

******

AutoJs6 Rapid OCR PP-OCRv6 Small 插件為 AutoJs6 提供基於 RapidOCR, ONNX Runtime 和 OpenCV Mobile 的文字檢測與識別能力. 插件可返回識別文本, 置信度和文本邊界框.

******

### 功能

******

- 提供共享 OCR 插件服務, 插件 ID 為 `rapid-ocr-pp-ocrv6`, 引擎為 `rapid-ocr`, 變體為 `pp-ocrv6`.
- 通過 `org.autojs.plugin.PADDLE_OCR` 暴露 OCR AIDL 接口, 兼容 AutoJs6 宿主側 OCR 插件發現機制.
- 支援 `ocr.rapid.recognizeText` 返回字符串列表, 支援 `ocr.rapid.detect` 返回文本, 置信度和邊界框.
- 支援編碼圖片輸入和原始 `ARGB_8888` 圖片緩衝區輸入.
- 提供 `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK.
- 插件資訊, 使用說明, README 和 CHANGELOG 支援西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體.
- 基於 RapidOCR, ONNX Runtime 和 OpenCV Mobile 構建.

******

### 使用示例

******

讀取圖片後可通過 Rapid OCR 模組識別文本或取得詳細結果:

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

`recognizeText` 返回字符串列表, `detect` 返回包含識別詳情的結果列表.

******

### 插件接口

******

宿主可通過以下身份發現並調用插件:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86_64, universal
```

`detect` 的每個結果包含 `text`, `confidence` 和 `bounds`, 目前接口不提供 quad 字段.

******

### 識別選項

******

目前插件實現讀取以下識別選項:

- `maxSideLen`: 檢測階段的最大邊長, 默認值為 `1024`.
- `scoreThreshold`: 文本框分數閾值, 默認值為 `0.5`.

******

### 模型資產

******

構建過程從 ModelScope 下載並校驗以下 4 個資產:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 文本檢測模型.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 文本識別模型.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: 引擎初始化所需的輔助模型資產.
- `ppocrv6_dict.txt`: PP-OCRv6 識別字典.

******

### 發行歷史

******

# v1.0.2

###### 2026/09/12

* `修復` 修復原生庫未打包 `libc++_shared.so` 的問題: CMake 現在顯式使用 `c++_shared` STL, 由 AGP 隨 4 個 ABI 一併打包 NDK r28.2 的 libc++, 16 KB 頁大小設備上不再因缺少依賴而無法載入 OCR 引擎
* `修復` Rapid OCR 原生依賴清理開關未初始化時 `clean` 任務執行失敗
* `優化` 同步 OpenCV 4.8.0 原生庫至 NDK r28c (Clang 19.0.1) 重編版本 (donor: AutoJs6-Plugin-OpenCV), 4 個 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 對齊並附帶 provenance 清單

# v1.0.1

###### 2026/09/11

* `修復` 在同一個打包應用中依次使用 Paddle OCR 和 Rapid OCR 時可能崩潰的問題
* `優化` 建置階段校驗 64 位原生程式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告
* `依賴` 升級 ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) 版本 1.18.0 -> 1.21.1
* `依賴` 升級 OpenCV 版本 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `新增` Rapid OCR (PP-OCRv6 Small) 插件標識, 插件 ID 為 `rapid-ocr-pp-ocrv6`, 引擎為 `rapid-ocr`, 變體為 `pp-ocrv6`.
* `新增` `PADDLE_OCR` AIDL 服務以及 `recognizeText` 和 `detect` 接口. `recognizeText` 返回文本字串, `detect` 為每個結果返回 `text`, `confidence` 和 `bounds`.
* `新增` 支援編碼圖像和原始 `ARGB_8888` 圖像. Android 13 在可用時使用 `SharedMemory`, 在 `SharedMemory` 不可用或失敗時回退到串流讀取.
* `新增` 基於 `PP-OCRv6 Small` 檢測和識別模型, `ONNX Runtime` 與 `OpenCV Mobile` 實現本地文本檢測和識別.
* `新增` 支援用於識別的 `maxSideLen` 和 `scoreThreshold` 選項.
* `新增` 將 `preBuild` 接入模型資源下載和 `SHA-256` 摘要驗證, 並同步 `ONNX Runtime` 原生庫.
* `新增` 西班牙語, 法語, 俄語, 阿拉伯語, 日語, 韓語, 英語, 簡體中文, 香港繁體中文和台灣繁體中文的插件資訊及使用說明.
* `新增` `JSON` 源文件和 `Python` 生成器, 用於生成多語言 `README` 與 `CHANGELOG` 文件.
* `新增` `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK 變體. 發佈文件名包含版本和 `ABI`, 歸檔副本包含 `CRC32` 摘要.
* `優化` 統一 README 版式與 Gradle 平台版本管理方式

##### 更多發行歷史可參閱

* [CHANGELOG-zh-Hant-HK.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-HK.md)

******

### 構建

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 構建:

```powershell
.\gradlew.bat :app:assembleRelease
```

構建參數來自 `version.properties`, 目前最低 SDK 為 24, 目標 SDK 為 36. `preBuild` 會下載並校驗模型資產, 同時同步 ONNX Runtime 原生庫.

******

### 資源結構

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

`strings.xml` 提供插件描述本地化, `plugin_instruction.md` 提供宿主側展示的使用說明. README 和 CHANGELOG 由 `.python/generate_markdown.py` 根據 JSON 源文件生成. 根目錄只生成 `README.md`, 不生成根目錄 `CHANGELOG.md`.

******

### 相關連結

******

- AutoJs6 OCR 文檔: https://docs.autojs6.com/#/ocr
- RapidOCR 項目: https://github.com/RapidAI/RapidOCR
- ModelScope 模型資產: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
