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
- 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 和 `universal` APK.
- 插件資訊, 使用說明, README 和 CHANGELOG 支援西班牙語/法語/俄語/阿拉伯語/日語/韓語/英語/簡體中文/香港繁體/台灣繁體.
- 基於 RapidOCR, ONNX Runtime 和 OpenCV Mobile 構建.
- 影像最多包含 16777216 個像素, 原始影像緩衝區上限為 64 MiB
- 編碼圖像最大為 64 MiB, 支援檔案描述符和管道傳輸

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
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
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

構建過程從 ModelScope 的 RapidOCR v3.9.2 下載並校驗以下 4 個資產:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 文本檢測模型.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 文本識別模型.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: 引擎初始化所需的輔助模型資產.
- `ppocrv6_dict.txt`: PP-OCRv6 識別字典.

******

### 發行歷史

******

# v1.0.6

###### 2026/09/20

* `提示` PP-OCRv6 Small 檢測模型, 識別模型, 輔助分類模型和字典在兩個版本中的 SHA-256 均一致; 本次更新模型來源記錄, 識別行為保持不變
* `依賴` RapidOCR 模型來源版本從 v3.9.1 升級至 v3.9.2, 並在 `version.properties` 中統一鎖定模型版本

# v1.0.5

###### 2026/09/19

* `修復` AGP 9.1 構建時的 SDK XML v4 解析警告及 JVM 單元測試組裝任務誤觸發 APK 原生程式庫對齊檢查的問題 (共用構建外掛 1.8.3)
* `優化` 將 compileSdk 與 targetSdk 提升到 37 (Android 17), 插件行為不受新目標版本影響

# v1.0.4

###### 2026/09/13

* `修復` 識別結果包含大量文本塊時, JNI 局部引用持續累積, 可能導致 Android 7.x 引用表溢出的問題 _[`issue #575`](http://issues.autojs6.com/575)_
* `修復` 初始化字串轉換遺留原生緩衝區和 JNI 臨時引用, 以及空字串處理異常的問題 _[`issue #575`](http://issues.autojs6.com/575)_
* `修復` 外掛中心顯示的版本與 ABI 資訊符合實際安裝的 APK
* `修復` 編碼圖像最大為 64 MiB, 支援檔案描述符和管道傳輸
* `修復` 版本日期保持統一的英文格式
* `優化` 發佈下載檔案產生前校驗 APK 版本, 簽署與完整變體集合
* `優化` 影像最多包含 16777216 個像素, 原始影像緩衝區上限為 64 MiB
* `優化` 擴展原生 ABI 打包與插件中繼資料至 arm64-v8a, armeabi-v7a, x86 和 x86_64, 同步通用 APK 與各 ABI 獨立 APK

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
