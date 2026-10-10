# 플러그인 안내

## 식별 정보

- 액션: `org.autojs.plugin.PADDLE_OCR`
- 엔진: `rapid-ocr`
- 변형: `pp-ocrv6`
- 플러그인 ID: `rapid-ocr-pp-ocrv6`

## 인식

- `recognizeText` 는 인식된 텍스트 문자열 목록을 반환합니다.
- `detect` 는 텍스트, 신뢰도, 경계 사각형이 포함된 감지 블록 목록을 반환합니다.

## 입력

- 인코딩된 이미지는 제공된 파일 디스크립터에서 디코딩됩니다.
- raw 입력은 너비, 높이, 행 스트라이드 메타데이터와 함께 ARGB_8888 픽셀을 사용합니다.

## 옵션

- `maxSideLen` 은 `OcrOptions.detLongSize` 에 대응하며 감지에 사용할 이미지의 최대 변 길이를 제어합니다. 기본값은 1024 입니다.
- `scoreThreshold` 는 `OcrOptions.scoreThreshold` 에 대응하며 허용할 최소 감지 점수를 제어합니다. 기본값은 0.5 입니다.

## 언어

- PP-OCRv6 Small은 단일 다국어 모델이며 언어 옵션 없이 중국어 간체, 중국어 번체, 영어, 일본어 및 여러 라틴 문자 언어를 인식합니다.

## 라이선스

- OCR 모델은 RapidOCR이 변환한 PaddleOCR 모델이며 Apache-2.0 라이선스를 따릅니다. 서드파티 라이선스 전문은 APK의 `assets/licenses/`에 포함되어 있습니다.
