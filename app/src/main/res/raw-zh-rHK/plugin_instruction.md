# 插件說明

## 身份資料

- Action: `org.autojs.plugin.PADDLE_OCR`
- Engine: `rapid-ocr`
- Variant: `pp-ocrv6`
- Plugin ID: `rapid-ocr-pp-ocrv6`

## 識別

- `recognizeText` 傳回已識別文字字串清單.
- `detect` 傳回包含文字, 信心度和邊界矩形的偵測區塊清單.

## 輸入

- 編碼影像從提供的文件描述符解碼.
- raw 輸入使用 ARGB_8888 像素以及寬度, 高度和行跨度元資料.

## 選項

- `maxSideLen` 對應 `OcrOptions.detLongSize`, 控制偵測使用的影像最大邊長. 預設值為 1024.
- `scoreThreshold` 對應 `OcrOptions.scoreThreshold`, 控制可接受的最低偵測分數. 預設值為 0.5.

## 語言

- PP-OCRv6 Small 是單一的多語言模型, 無需語言選項即可識別簡體中文, 繁體中文, 英文, 日文以及多種拉丁字母語言.

## 許可

- OCR 模型為 RapidOCR 轉換的 PaddleOCR 模型, 採用 Apache-2.0 許可. 第三方許可全文打包在 APK 的 `assets/licenses/` 目錄中.
