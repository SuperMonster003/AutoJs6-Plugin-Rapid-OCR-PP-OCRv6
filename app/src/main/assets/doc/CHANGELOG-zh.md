******

### 发行历史

******

# v1.0.4

###### 2026/09/13

* `修复` 插件中心显示的版本与 ABI 信息匹配实际安装的 APK
* `修复` 编码图像最大为 64 MiB, 支持文件描述符和管道传输
* `修复` 版本日期保持统一的英文格式
* `优化` 发布下载文件生成前校验 APK 版本, 签名与完整变体集合
* `优化` 图像最多包含 16777216 个像素, 原始图像缓冲区上限为 64 MiB

# v1.0.3

###### 2026/09/12

* `修复` 插件服务重建或重复初始化引擎时, 字典重复追加导致识别文字偏移的问题
* `修复` 重新初始化时遗留旧 ONNX Session 的问题, 并串行处理同一进程内的初始化与识别以避免状态竞争

# v1.0.2

###### 2026/09/12

* `修复` 修复原生库未打包 `libc++_shared.so` 的问题: CMake 现在显式使用 `c++_shared` STL, 由 AGP 随 4 个 ABI 一并打包 NDK r28.2 的 libc++, 16 KB 页大小设备上不再因缺少依赖而无法加载 OCR 引擎
* `修复` Rapid OCR 原生依赖清理开关未初始化时 `clean` 任务执行失败
* `优化` 同步 OpenCV 4.8.0 原生库至 NDK r28c (Clang 19.0.1) 重编版本 (donor: AutoJs6-Plugin-OpenCV), 4 个 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 对齐并附带 provenance 清单

# v1.0.1

###### 2026/09/11

* `修复` 在同一个打包应用中依次使用 Paddle OCR 和 Rapid OCR 时可能崩溃的问题
* `优化` 构建阶段校验 64 位原生库的 16 KB 页大小对齐, 检查 manifest 契约并输出 JSON 报告
* `依赖` 升级 ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) 版本 1.18.0 -> 1.21.1
* `依赖` 升级 OpenCV 版本 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `新增` Rapid OCR (PP-OCRv6 Small) 插件标识, 插件 ID 为 `rapid-ocr-pp-ocrv6`, 引擎为 `rapid-ocr`, 变体为 `pp-ocrv6`.
* `新增` `PADDLE_OCR` AIDL 服务以及 `recognizeText` 和 `detect` 接口. `recognizeText` 返回文本字符串, `detect` 为每个结果返回 `text`, `confidence` 和 `bounds`.
* `新增` 支持编码图像和原始 `ARGB_8888` 图像. Android 13 在可用时使用 `SharedMemory`, 在 `SharedMemory` 不可用或失败时回退到流式读取.
* `新增` 基于 `PP-OCRv6 Small` 检测和识别模型, `ONNX Runtime` 与 `OpenCV Mobile` 实现本地文本检测和识别.
* `新增` 支持用于识别的 `maxSideLen` 和 `scoreThreshold` 选项.
* `新增` 将 `preBuild` 接入模型资源下载和 `SHA-256` 摘要校验, 并同步 `ONNX Runtime` 原生库.
* `新增` 西班牙语, 法语, 俄语, 阿拉伯语, 日语, 韩语, 英语, 简体中文, 香港繁体中文和台湾繁体中文的插件信息与使用说明.
* `新增` `JSON` 源文件和 `Python` 生成器, 用于生成多语言 `README` 与 `CHANGELOG` 文档.
* `新增` `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK 变体. 发布文件名包含版本和 `ABI`, 归档副本包含 `CRC32` 摘要.
* `优化` 统一 README 版式与 Gradle 平台版本管理方式
