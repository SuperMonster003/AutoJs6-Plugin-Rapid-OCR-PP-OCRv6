******

### リリース履歴

******

# v1.0.0

###### 2026/07/17

* `機能` Rapid OCR (PP-OCRv6 Small) プラグインの識別情報を追加しました. プラグイン ID は `rapid-ocr-pp-ocrv6`, エンジンは `rapid-ocr`, バリアントは `pp-ocrv6` です.
* `機能` `PADDLE_OCR` AIDL サービスと `recognizeText` および `detect` インターフェースを追加しました. `recognizeText` はテキスト文字列を返し, `detect` は各結果の `text`, `confidence`, `bounds` を返します.
* `機能` エンコード画像と raw `ARGB_8888` 画像に対応しました. Android 13 では利用可能な場合に `SharedMemory` を使用し, 利用できない場合または失敗した場合はストリーム読み取りへフォールバックします.
* `機能` `PP-OCRv6 Small` の検出モデルと認識モデル, `ONNX Runtime`, `OpenCV Mobile` を使用するローカルテキスト検出と認識を追加しました.
* `機能` 認識用の `maxSideLen` と `scoreThreshold` オプションに対応しました.
* `機能` `preBuild` をモデル資産のダウンロード, `SHA-256` ダイジェストの検証, `ONNX Runtime` ネイティブライブラリの同期に接続しました.
* `機能` スペイン語, フランス語, ロシア語, アラビア語, 日本語, 韓国語, 英語, 簡体字中国語, 香港繁体字中国語, 台湾繁体字中国語のプラグインメタデータと使用説明を追加しました.
* `機能` 多言語 `README` と `CHANGELOG` 文書用の `JSON` ソースと `Python` ジェネレータを追加しました.
* `機能` `arm64-v8a`, `armeabi-v7a`, `x86_64`, `universal` の APK バリアントを追加しました. 公開ファイル名にはバージョンと `ABI` が含まれ, アーカイブコピーには `CRC32` ダイジェストが含まれます.
