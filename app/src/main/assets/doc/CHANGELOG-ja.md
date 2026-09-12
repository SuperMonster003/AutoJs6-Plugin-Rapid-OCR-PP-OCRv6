******

### リリース履歴

******

# v1.0.2

###### 2026/09/12

* `修正` パッケージ済みネイティブライブラリに `libc++_shared.so` が含まれていなかった問題を修正: CMake が `c++_shared` STL を明示的に使用し, AGP が 4 つの ABI すべてに NDK r28.2 の libc++ を同梱するため, 16 KB ページサイズ端末でも OCR エンジンを読み込めるように
* `修正` Rapid OCR のネイティブ依存関係のクリーンアップ設定が初期化されていない場合に `clean` タスクが失敗する問題
* `改善` OpenCV 4.8.0 ネイティブライブラリを NDK r28c (Clang 19.0.1) 再ビルド版に同期 (donor: AutoJs6-Plugin-OpenCV); 4 つの ABI の `libopencv_java4.so` は 16 KB `PT_LOAD` アラインメントを維持し provenance マニフェストを同梱

# v1.0.1

###### 2026/09/11

* `修正` 同じパッケージ化アプリで Paddle OCR と Rapid OCR を順番に使用するとクラッシュする問題
* `改善` 64 ビットのネイティブライブラリの 16 KB ページアラインメントをビルド時に検証, manifest 契約の検査と JSON レポートに対応
* `依存関係` ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) バージョン 1.18.0 -> 1.21.1 に更新
* `依存関係` OpenCV バージョンのアップグレード 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `機能` Rapid OCR (PP-OCRv6 Small) プラグインの識別情報を追加しました. プラグイン ID は `rapid-ocr-pp-ocrv6`, エンジンは `rapid-ocr`, バリアントは `pp-ocrv6` です.
* `機能` `PADDLE_OCR` AIDL サービスと `recognizeText` および `detect` インターフェースを追加しました. `recognizeText` はテキスト文字列を返し, `detect` は各結果の `text`, `confidence`, `bounds` を返します.
* `機能` エンコード画像と raw `ARGB_8888` 画像に対応しました. Android 13 では利用可能な場合に `SharedMemory` を使用し, 利用できない場合または失敗した場合はストリーム読み取りへフォールバックします.
* `機能` `PP-OCRv6 Small` の検出モデルと認識モデル, `ONNX Runtime`, `OpenCV Mobile` を使用するローカルテキスト検出と認識を追加しました.
* `機能` 認識用の `maxSideLen` と `scoreThreshold` オプションに対応しました.
* `機能` `preBuild` をモデル資産のダウンロード, `SHA-256` ダイジェストの検証, `ONNX Runtime` ネイティブライブラリの同期に接続しました.
* `機能` スペイン語, フランス語, ロシア語, アラビア語, 日本語, 韓国語, 英語, 簡体字中国語, 香港繁体字中国語, 台湾繁体字中国語のプラグインメタデータと使用説明を追加しました.
* `機能` 多言語 `README` と `CHANGELOG` 文書用の `JSON` ソースと `Python` ジェネレータを追加しました.
* `機能` `arm64-v8a`, `armeabi-v7a`, `x86_64`, `universal` の APK バリアントを追加しました. 公開ファイル名にはバージョンと `ABI` が含まれ, アーカイブコピーには `CRC32` ダイジェストが含まれます.
* `改善` README のレイアウトと Gradle プラットフォームのバージョン管理方式を統一
