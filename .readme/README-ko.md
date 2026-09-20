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
- `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64`, `universal` APK를 제공합니다.
- 플러그인 정보, 사용 설명, README, CHANGELOG는 스페인어/프랑스어/러시아어/아랍어/일본어/한국어/영어/중국어 간체/홍콩 중국어 번체/대만 중국어 번체로 제공됩니다.
- RapidOCR, ONNX Runtime, OpenCV Mobile을 기반으로 빌드됩니다.
- 이미지는 최대 16777216픽셀, 원시 이미지 버퍼는 최대 64 MiB까지 지원
- 인코딩된 이미지는 최대 64 MiB이며 파일 디스크립터와 파이프를 지원합니다

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
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
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

빌드 과정에서 ModelScope의 RapidOCR v3.9.2에서 다음 4개 자산을 다운로드하고 검증:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 문자 감지 모델.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 문자 인식 모델.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: 엔진 초기화에 필요한 보조 모델 자산.
- `ppocrv6_dict.txt`: PP-OCRv6 인식 사전.

******

### 릴리스 기록

******

# v1.0.6

###### 2026/09/20

* `힌트` PP-OCRv6 Small 감지 모델, 인식 모델, 보조 분류 모델 및 사전의 SHA-256은 두 버전에서 동일함. 이번 변경은 모델 출처 기록을 갱신하며 인식 동작은 유지됨
* `의존성` RapidOCR 모델 소스를 v3.9.1에서 v3.9.2로 업그레이드하고 `version.properties`에서 모델 버전을 고정

# v1.0.5

###### 2026/09/19

* `수정` 공유 빌드 플러그인 1.8.3을 통해 AGP 9.1의 SDK XML v4 파싱 경고 및 JVM 단위 테스트 조립 작업에서 APK 네이티브 라이브러리 정렬 검사가 잘못 실행되는 문제 해결
* `개선` compileSdk 와 targetSdk 를 37 (Android 17) 로 올리며, 플러그인 동작은 새 대상 버전의 영향을 받지 않음

# v1.0.4

###### 2026/09/13

* `수정` 많은 텍스트 블록을 변환할 때 JNI 로컬 참조가 누적되어 Android 7.x에서 참조 테이블이 넘칠 수 있는 문제 _[`issue #575`](http://issues.autojs6.com/575)_
* `수정` 초기화 중 문자열 변환 시 네이티브 버퍼와 JNI 임시 참조가 남는 문제 및 빈 문자열 처리 오류 _[`issue #575`](http://issues.autojs6.com/575)_
* `수정` 플러그인 센터의 버전과 ABI 정보가 설치된 APK와 일치
* `수정` 인코딩된 이미지는 최대 64 MiB이며 파일 디스크립터와 파이프를 지원합니다
* `수정` 버전 날짜를 일관된 영어 형식으로 표시
* `개선` 다운로드 파일 생성 전에 릴리스 APK의 버전, 서명 및 전체 변형 구성을 검증
* `개선` 이미지는 최대 16777216픽셀, 원시 이미지 버퍼는 최대 64 MiB까지 지원
* `개선` 네이티브 ABI 패키징과 플러그인 메타데이터를 arm64-v8a, armeabi-v7a, x86, x86_64로 확장하고 범용 APK와 ABI별 APK를 일치시킴

##### 더 많은 릴리스 기록

* [CHANGELOG-ko.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-ko.md)

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
