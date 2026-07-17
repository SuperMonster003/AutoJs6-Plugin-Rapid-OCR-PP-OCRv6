# v1.0.0

###### 2026/07/17

* `新增` Rapid OCR (PP-OCRv6 Small) 插件标识, 插件 ID 为 `rapid-ocr-pp-ocrv6`, 引擎为 `rapid-ocr`, 变体为 `pp-ocrv6`.
* `新增` `PADDLE_OCR` AIDL 服务以及 `recognizeText` 和 `detect` 接口. `recognizeText` 返回文本字符串, `detect` 为每个结果返回 `text`, `confidence` 和 `bounds`.
* `新增` 支持编码图像和原始 `ARGB_8888` 图像. Android 13 在可用时使用 `SharedMemory`, 在 `SharedMemory` 不可用或失败时回退到流式读取.
* `新增` 基于 `PP-OCRv6 Small` 检测和识别模型, `ONNX Runtime` 与 `OpenCV Mobile` 实现本地文本检测和识别.
* `新增` 支持用于识别的 `maxSideLen` 和 `scoreThreshold` 选项.
* `新增` 将 `preBuild` 接入模型资源下载和 `SHA-256` 摘要校验, 并同步 `ONNX Runtime` 原生库.
* `新增` 西班牙语, 法语, 俄语, 阿拉伯语, 日语, 韩语, 英语, 简体中文, 香港繁体中文和台湾繁体中文的插件信息与使用说明.
* `新增` `JSON` 源文件和 `Python` 生成器, 用于生成多语言 `README` 与 `CHANGELOG` 文档.
* `新增` `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK 变体. 发布文件名包含版本和 `ABI`, 归档副本包含 `CRC32` 摘要.
