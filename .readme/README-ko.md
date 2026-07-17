<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>AutoJs6에서 PP-OCRv6 Small 문자 인식을 제공하는 Rapid OCR 플러그인</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/commit/96410a029a429919da9eaaa21b5c900875f32bad"><img alt="Created" src="https://img.shields.io/date/1783059864?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 언어 (Languages)

******

현재 README.md는 다음 언어를 지원합니다:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- 한국어 [ko] # 현재
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### 소개

******

AutoJs6 Rapid OCR PP-OCRv6 Small 플러그인은 RapidOCR, ONNX Runtime, OpenCV Mobile 기반의 문자 감지와 인식을 AutoJs6에 제공합니다. 인식된 문자, 신뢰도 값, 문자 경계를 반환할 수 있습니다.

******

### 기능

******

- 플러그인 ID `rapid-ocr-pp-ocrv6`, 엔진 `rapid-ocr`, 변형 `pp-ocrv6`인 공유 OCR 플러그인 서비스를 제공합니다.
- `org.autojs.plugin.PADDLE_OCR`을 통해 OCR AIDL 인터페이스를 제공하며 AutoJs6 호스트의 OCR 플러그인 검색 흐름과 함께 작동합니다.
- `ocr.rapid.recognizeText`는 문자열 목록을 반환하고 `ocr.rapid.detect`는 문자, 신뢰도 값, 경계를 반환합니다.
- 인코딩된 이미지 입력과 원시 `ARGB_8888` 이미지 버퍼 입력을 지원합니다.
- `arm64-v8a`, `armeabi-v7a`, `x86_64`, `universal` APK를 제공합니다.
- 플러그인 정보, 사용 설명, README, CHANGELOG는 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/중국어 간체/홍콩 중국어 번체/대만 중국어 번체로 제공됩니다.
- RapidOCR, ONNX Runtime, OpenCV Mobile을 기반으로 빌드됩니다.

******

### 사용 예제

******

이미지를 읽은 후 Rapid OCR 모듈로 문자를 인식하거나 상세 결과를 가져옵니다:

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

`recognizeText`는 문자열 목록을 반환하고 `detect`는 상세 인식 결과 목록을 반환합니다.

******

### 플러그인 인터페이스

******

호스트는 다음 식별 정보로 플러그인을 검색하고 호출할 수 있습니다:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86_64, universal
```

각 `detect` 결과에는 `text`, `confidence`, `bounds`가 포함됩니다. 현재 인터페이스는 quad 필드를 제공하지 않습니다.

******

### 인식 옵션

******

현재 플러그인 구현은 다음 인식 옵션을 읽습니다:

- `maxSideLen`: 감지 중에 사용하는 최대 변 길이입니다. 기본값은 `1024`입니다.
- `scoreThreshold`: 문자 상자 점수 임계값입니다. 기본값은 `0.5`입니다.

******

### 모델 자산

******

빌드는 ModelScope에서 다음 4개 자산을 다운로드하고 검증합니다:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 문자 감지 모델.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 문자 인식 모델.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: 엔진 초기화에 필요한 보조 모델 자산.
- `ppocrv6_dict.txt`: PP-OCRv6 인식 사전.

******

### 릴리스 기록

******

# v1.0.0

###### 2026/07/17

* `기능` Rapid OCR (PP-OCRv6 Small) 플러그인 식별 정보를 추가했습니다. 플러그인 ID는 `rapid-ocr-pp-ocrv6`, 엔진은 `rapid-ocr`, 변형은 `pp-ocrv6`입니다.
* `기능` `PADDLE_OCR` AIDL 서비스와 `recognizeText` 및 `detect` 인터페이스를 추가했습니다. `recognizeText`는 텍스트 문자열을 반환하고, `detect`는 각 결과의 `text`, `confidence`, `bounds`를 반환합니다.
* `기능` 인코딩 이미지와 raw `ARGB_8888` 이미지를 지원합니다. Android 13에서는 사용 가능한 경우 `SharedMemory`를 사용하고, 사용할 수 없거나 실패하면 스트림 읽기로 대체합니다.
* `기능` `PP-OCRv6 Small` 감지 및 인식 모델, `ONNX Runtime`, `OpenCV Mobile`을 사용하는 로컬 텍스트 감지와 인식을 추가했습니다.
* `기능` 인식용 `maxSideLen` 및 `scoreThreshold` 옵션을 지원합니다.
* `기능` `preBuild`가 모델 자산 다운로드, `SHA-256` 다이제스트 검증, `ONNX Runtime` 네이티브 라이브러리 동기화를 수행하도록 연결했습니다.
* `기능` 스페인어, 프랑스어, 러시아어, 아랍어, 일본어, 한국어, 영어, 중국어 간체, 홍콩 중국어 번체, 대만 중국어 번체 플러그인 메타데이터와 사용 설명을 추가했습니다.
* `기능` 다국어 `README` 및 `CHANGELOG` 문서용 `JSON` 소스와 `Python` 생성기를 추가했습니다.
* `기능` `arm64-v8a`, `armeabi-v7a`, `x86_64`, `universal` APK 변형을 추가했습니다. 릴리스 파일 이름에는 버전과 `ABI`가 포함되고, 보관 사본에는 `CRC32` 다이제스트가 포함됩니다.

##### 더 많은 릴리스 기록

* [CHANGELOG-ko.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.changelog/CHANGELOG-ko.md)

******

### 빌드

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 빌드:

```powershell
.\gradlew.bat :app:assembleRelease
```

빌드 매개변수는 `version.properties`에서 가져옵니다. 현재 최소 SDK는 24이고 대상 SDK는 36입니다. `preBuild`는 모델 자산을 다운로드하고 검증한 다음 ONNX Runtime 네이티브 라이브러리를 동기화합니다.

******

### 리소스 구조

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

`strings.xml`에는 현지화된 플러그인 설명이 있고 `plugin_instruction.md`에는 호스트에 표시되는 사용 설명이 있습니다. README와 CHANGELOG는 `.python/generate_markdown.py`가 JSON 소스에서 생성합니다. 저장소 루트에는 `README.md`만 생성하며 루트 `CHANGELOG.md`는 생성하지 않습니다.

******

### 링크

******

- AutoJs6 OCR 문서: https://docs.autojs6.com/#/ocr
- RapidOCR 프로젝트: https://github.com/RapidAI/RapidOCR
- ModelScope 모델 자산: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile
