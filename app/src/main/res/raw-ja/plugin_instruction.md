# プラグインの説明

## 識別情報

- アクション: `org.autojs.plugin.PADDLE_OCR`
- エンジン: `rapid-ocr`
- バリアント: `pp-ocrv6`
- プラグイン ID: `rapid-ocr-pp-ocrv6`

## 認識

- `recognizeText` は認識された文字列のリストを返します.
- `detect` はテキスト, 信頼度, 境界矩形を含む検出ブロックのリストを返します.

## 入力

- エンコード済み画像は指定されたファイルディスクリプタからデコードされます.
- raw 入力は幅, 高さ, 行ストライドのメタデータを持つ ARGB_8888 ピクセルを使用します.

## オプション

- `maxSideLen` は `OcrOptions.detLongSize` に対応し, 検出に使用する画像の最大辺長を指定します. 既定値は 1024 です.
- `scoreThreshold` は `OcrOptions.scoreThreshold` に対応し, 許容する最小検出スコアを指定します. 既定値は 0.5 です.
