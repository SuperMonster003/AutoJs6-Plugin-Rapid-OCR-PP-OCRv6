<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>AutoJs6 で PP-OCRv6 Small 文字認識を提供する Rapid OCR プラグイン</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 言語 (Languages)

******

現在の README.md は次の言語に対応しています:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- 日本語 [ja] # 現在
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### 概要

******

AutoJs6 Rapid OCR PP-OCRv6 Small プラグインは RapidOCR, ONNX Runtime, OpenCV Mobile を使用した文字検出と文字認識を AutoJs6 に提供します. 認識した文字列, 信頼度, 文字領域を返すことができます.

******

### 機能

******

- プラグイン ID `rapid-ocr-pp-ocrv6`, エンジン `rapid-ocr`, バリアント `pp-ocrv6` の共有 OCR プラグインサービスを提供します.
- `org.autojs.plugin.PADDLE_OCR` を介して OCR AIDL インターフェースを公開し, AutoJs6 ホストの OCR プラグイン検出機構に対応します.
- `ocr.rapid.recognizeText` は文字列リストを返し, `ocr.rapid.detect` は文字列, 信頼度, 領域を返します.
- エンコード画像入力と未エンコードの `ARGB_8888` 画像バッファ入力に対応します.
- `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`, `universal` APK を提供します.
- プラグイン情報, 使用説明, README, CHANGELOG はスペイン語/フランス語/ロシア語/アラビア語/日本語/韓国語/英語/簡体字中国語/香港繁体字中国語/台湾繁体字中国語に対応します.
- RapidOCR, ONNX Runtime, OpenCV Mobile を基盤として構築されています.
- 画像は最大 16777216 ピクセルまで, 生画像バッファーは 64 MiB まで対応
- エンコード済み画像は 64 MiB まで対応し ファイル記述子とパイプを使用できます

******

### 使用例

******

画像を読み込んだ後に Rapid OCR モジュールで文字を認識するか詳細結果を取得します:

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

`recognizeText` は文字列リストを返し, `detect` は詳細な認識結果のリストを返します.

******

### プラグインインターフェース

******

ホストは次の識別情報でプラグインを検出して呼び出せます:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
```

`detect` の各結果には `text`, `confidence`, `bounds` が含まれます. 現在のインターフェースは quad フィールドを提供しません.

******

### 認識オプション

******

現在のプラグイン実装は次の認識オプションを読み取ります:

- `maxSideLen`: 検出時に使用する最大辺長です. デフォルトは `1024` です.
- `scoreThreshold`: テキストボックスのスコアしきい値です. デフォルトは `0.5` です.

******

### モデルアセット

******

ビルドは ModelScope から次の 4 個のアセットをダウンロードして検証します:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 文字検出モデル.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 文字認識モデル.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: エンジン初期化に必要な補助モデルアセット.
- `ppocrv6_dict.txt`: PP-OCRv6 認識辞書.

******

### リリース履歴

******

# v1.0.4

###### 2026/09/13

* `修正` 多数のテキストブロックの変換時に JNI ローカル参照が蓄積し, Android 7.x で参照テーブルがオーバーフローする可能性がある問題 _[`issue #575`](http://issues.autojs6.com/575)_
* `修正` 初期化時の文字列変換でネイティブバッファと JNI 一時参照が残留する問題, および空文字列の処理が不正になる問題 _[`issue #575`](http://issues.autojs6.com/575)_
* `修正` プラグインセンターのバージョンと ABI 情報がインストール済み APK と一致
* `修正` エンコード済み画像は 64 MiB まで対応し ファイル記述子とパイプを使用できます
* `修正` バージョン日付は英語の統一形式で表示されます
* `改善` ダウンロード用ファイルの作成前に, リリース APK のバージョン, 署名, バリアントの完全性を検証
* `改善` 画像は最大 16777216 ピクセルまで, 生画像バッファーは 64 MiB まで対応
* `改善` ネイティブ ABI のパッケージ化とプラグインメタデータを arm64-v8a, armeabi-v7a, x86, x86_64 に拡張し, ユニバーサル APK と ABI 別 APK を同期

# v1.0.3

###### 2026/09/12

* `修正` プラグインサービスの再作成やエンジンの繰り返し初期化時に OCR 辞書が重複追加され, 認識文字がずれる問題
* `修正` 再初期化時に古い ONNX セッションが残る問題と, 同一プロセス内の初期化と認識の競合

# v1.0.2

###### 2026/09/12

* `修正` パッケージ済みネイティブライブラリに `libc++_shared.so` が含まれていなかった問題を修正: CMake が `c++_shared` STL を明示的に使用し, AGP が 4 つの ABI すべてに NDK r28.2 の libc++ を同梱するため, 16 KB ページサイズ端末でも OCR エンジンを読み込めるように
* `修正` Rapid OCR のネイティブ依存関係のクリーンアップ設定が初期化されていない場合に `clean` タスクが失敗する問題
* `改善` OpenCV 4.8.0 ネイティブライブラリを NDK r28c (Clang 19.0.1) 再ビルド版に同期 (donor: AutoJs6-Plugin-OpenCV); 4 つの ABI の `libopencv_java4.so` は 16 KB `PT_LOAD` アラインメントを維持し provenance マニフェストを同梱

##### その他のリリース履歴

* [CHANGELOG-ja.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-ja.md)

******

### ビルド

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release ビルド:

```powershell
.\gradlew.bat :app:assembleRelease
```

ビルドパラメータは `version.properties` から取得します. 現在の最小 SDK は 24, ターゲット SDK は 36 です. `preBuild` はモデルアセットをダウンロードして検証し, ONNX Runtime ネイティブライブラリを同期します.

******

### リソース構成

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

`strings.xml` にはローカライズされたプラグイン説明が含まれ, `plugin_instruction.md` にはホストが表示する使用説明が含まれます. README と CHANGELOG は `.python/generate_markdown.py` が JSON ソースから生成します. リポジトリのルートには `README.md` だけを生成し, ルートの `CHANGELOG.md` は生成しません.

******

### リンク

******

- AutoJs6 OCR ドキュメント: https://docs.autojs6.com/#/ocr
- RapidOCR プロジェクト: https://github.com/RapidAI/RapidOCR
- ModelScope モデルアセット: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
