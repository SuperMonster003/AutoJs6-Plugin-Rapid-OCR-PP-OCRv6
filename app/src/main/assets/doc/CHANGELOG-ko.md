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
