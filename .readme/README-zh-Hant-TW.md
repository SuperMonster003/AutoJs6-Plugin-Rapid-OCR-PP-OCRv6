<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用於 AutoJs6 PP-OCRv6 Small 文字辨識的 Rapid OCR 外掛</p>

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
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- 繁體中文 (台灣) [zh-Hant-TW] # 目前
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

AutoJs6 Rapid OCR PP-OCRv6 Small 外掛為 AutoJs6 提供基於 RapidOCR, ONNX Runtime 和 OpenCV Mobile 的文字偵測與辨識能力. 外掛可傳回辨識文字, 信賴度和文字邊界框.

******

### 功能

******

- 提供共用 OCR 外掛服務, 外掛 ID 為 `rapid-ocr-pp-ocrv6`, 引擎為 `rapid-ocr`, 變體為 `pp-ocrv6`.
- 透過 `org.autojs.plugin.PADDLE_OCR` 公開 OCR AIDL 介面, 相容 AutoJs6 宿主端 OCR 外掛探索機制.
- 支援 `ocr.rapid.recognizeText` 傳回字串清單, 支援 `ocr.rapid.detect` 傳回文字, 信賴度和邊界框.
- 支援編碼圖片輸入和原始 `ARGB_8888` 圖片緩衝區輸入.
- 提供 `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK.
- 外掛資訊, 使用說明, README 和 CHANGELOG 支援西班牙文/法文/俄文/阿拉伯文/日文/韓文/英文/簡體中文/香港繁體/台灣繁體.
- 基於 RapidOCR, ONNX Runtime 和 OpenCV Mobile 建置.

******

### 使用範例

******

讀取圖片後可透過 Rapid OCR 模組辨識文字或取得詳細結果:

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

`recognizeText` 傳回字串清單, `detect` 傳回包含辨識詳細資料的結果清單.

******

### 外掛介面

******

宿主可透過以下識別資訊探索並呼叫外掛:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86_64, universal
```

`detect` 的每個結果包含 `text`, `confidence` 和 `bounds`, 目前介面不提供 quad 欄位.

******

### 辨識選項

******

目前外掛實作讀取以下辨識選項:

- `maxSideLen`: 偵測階段的最大邊長, 預設值為 `1024`.
- `scoreThreshold`: 文字框分數臨界值, 預設值為 `0.5`.

******

### 模型資產

******

建置過程從 ModelScope 下載並驗證以下 4 個資產:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 文字偵測模型.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 文字辨識模型.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: 引擎初始化所需的輔助模型資產.
- `ppocrv6_dict.txt`: PP-OCRv6 辨識字典.

******

### 發行歷史

******

# v1.0.3

###### 2026/09/12

* `修正` 外掛服務重建或重複初始化引擎時, 字典重複附加導致辨識文字偏移的問題
* `修正` 重新初始化時遺留舊 ONNX Session 的問題, 並循序處理同一處理程序內的初始化與辨識以避免狀態競爭

# v1.0.2

###### 2026/09/12

* `修正` 修復原生程式庫未打包 `libc++_shared.so` 的問題: CMake 現在明確使用 `c++_shared` STL, 由 AGP 隨 4 個 ABI 一併打包 NDK r28.2 的 libc++, 16 KB 分頁大小裝置上不再因缺少相依而無法載入 OCR 引擎
* `修正` Rapid OCR 原生依賴清理開關未初始化時 `clean` 任務執行失敗
* `改善` 同步 OpenCV 4.8.0 原生程式庫至 NDK r28c (Clang 19.0.1) 重新建置版本 (donor: AutoJs6-Plugin-OpenCV), 4 個 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 對齊並附帶 provenance 清單

# v1.0.1

###### 2026/09/11

* `修正` 在同一個打包應用中依序使用 Paddle OCR 和 Rapid OCR 時可能崩潰的問題
* `改善` 建置階段校驗 64 位原生函式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告
* `相依性` 升級 ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) 版本 1.18.0 -> 1.21.1
* `相依性` 升級 OpenCV 版本 4.5.3 -> 4.8.0

##### 更多發行歷史可參閱

* [CHANGELOG-zh-Hant-TW.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hant-TW.md)

******

### 建置

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 建置:

```powershell
.\gradlew.bat :app:assembleRelease
```

建置參數來自 `version.properties`, 目前最低 SDK 為 24, 目標 SDK 為 36. `preBuild` 會下載並驗證模型資產, 同時同步 ONNX Runtime 原生程式庫.

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

`strings.xml` 提供外掛描述本地化, `plugin_instruction.md` 提供宿主端顯示的使用說明. README 和 CHANGELOG 由 `.python/generate_markdown.py` 根據 JSON 來源檔產生. 根目錄只產生 `README.md`, 不產生根目錄 `CHANGELOG.md`.

******

### 相關連結

******

- AutoJs6 OCR 文件: https://docs.autojs6.com/#/ocr
- RapidOCR 專案: https://github.com/RapidAI/RapidOCR
- ModelScope 模型資產: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
