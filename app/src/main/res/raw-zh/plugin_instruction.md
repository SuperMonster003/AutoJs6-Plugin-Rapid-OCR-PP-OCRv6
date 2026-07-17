# 插件说明

## 身份信息

- Action: `org.autojs.plugin.PADDLE_OCR`
- Engine: `rapid-ocr`
- Variant: `pp-ocrv6`
- Plugin ID: `rapid-ocr-pp-ocrv6`

## 识别

- `recognizeText` 返回识别文本字符串列表.
- `detect` 返回包含文本, 置信度和边界矩形的检测块列表.

## 输入

- 编码图像从提供的文件描述符解码.
- raw 输入使用 ARGB_8888 像素以及宽度, 高度和行跨度元数据.

## 选项

- `maxSideLen` 对应 `OcrOptions.detLongSize`, 控制检测使用的图像最大边长. 默认值为 1024.
- `scoreThreshold` 对应 `OcrOptions.scoreThreshold`, 控制可接受的最低检测分数. 默认值为 0.5.
