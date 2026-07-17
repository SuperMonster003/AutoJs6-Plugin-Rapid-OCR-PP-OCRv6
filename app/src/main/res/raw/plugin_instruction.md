# Plugin instructions

## Identity

- Action: `org.autojs.plugin.PADDLE_OCR`
- Engine: `rapid-ocr`
- Variant: `pp-ocrv6`
- Plugin ID: `rapid-ocr-pp-ocrv6`

## Recognition

- `recognizeText` returns a list of recognized text strings.
- `detect` returns detected blocks with text, confidence, and bounds.

## Input

- Encoded images are decoded from the supplied file descriptor.
- Raw input uses ARGB_8888 pixels with width, height, and row stride metadata.

## Options

- `maxSideLen` maps to `OcrOptions.detLongSize` and controls the maximum image side length used for detection. The default is 1024.
- `scoreThreshold` maps to `OcrOptions.scoreThreshold` and controls the minimum accepted detection score. The default is 0.5.
