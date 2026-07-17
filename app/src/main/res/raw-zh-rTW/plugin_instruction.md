# 外掛程式說明

## 識別資訊

- Action: `org.autojs.plugin.PADDLE_OCR`
- Engine: `rapid-ocr`
- Variant: `pp-ocrv6`
- Plugin ID: `rapid-ocr-pp-ocrv6`

## 辨識

- `recognizeText` 傳回辨識文字字串清單.
- `detect` 傳回包含文字, 信賴度和邊界矩形的偵測區塊清單.

## 輸入

- 編碼影像從提供的檔案描述元解碼.
- raw 輸入使用 ARGB_8888 像素以及寬度, 高度和列距中繼資料.

## 選項

- `maxSideLen` 對應 `OcrOptions.detLongSize`, 控制偵測使用的影像最大邊長. 預設值為 1024.
- `scoreThreshold` 對應 `OcrOptions.scoreThreshold`, 控制可接受的最低偵測分數. 預設值為 0.5.
