******

### 发行历史

******

# v1.0.6

###### 2026/10/05

* `提示` PP-OCRv6 Small 检测模型, 识别模型, 辅助分类模型和字典在两个版本中的 SHA-256 均一致; 本次更新模型来源记录, 识别行为保持不变
* `修复` 识别 16 位 PNG 等高位深图像时插件进程崩溃的问题 (Android 会将其解码为 RGBA_F16)
* `修复` 模型加载或推理出现原生异常时插件进程直接崩溃的问题, 现改为向宿主返回错误
* `优化` 插件中心图标采用统一工作台调整后的尺寸, 位置, 亮暗图稿与圆形底色, 保留可重建原稿和参数
* `优化` Android 系统应用信息图标与图标工作台共用图稿和亮暗底色, 保留插件中心透明图稿及现有启动器选项
* `优化` 模型初始化时校验资产是否完整读取, 以及识别字典与模型输出类别数是否一致
* `优化` APK 内附带 PP-OCRv6 模型, ONNX Runtime, OpenCV 等第三方组件的许可全文与署名 (`assets/licenses/`)
* `优化` 插件说明补充 PP-OCRv6 Small 的多语言识别范围及许可信息
* `依赖` 升级 RapidOCR 模型来源版本 3.9.1 -> 3.10.0

# v1.0.5

###### 2026/09/19

* `修复` AGP 9.1 构建时的 SDK XML v4 解析警告, 以及 JVM 单元测试误触发 APK 原生库对齐检查的问题 (共享构建插件 1.8.3)
* `优化` compileSdk/targetSdk 升级至 37 (Android 17)

# v1.0.4

###### 2026/09/13

* `修复` 识别结果包含大量文本块时, JNI 局部引用持续累积, 可能导致 Android 7.x 引用表溢出的问题 _[`issue #575`](http://issues.autojs6.com/575)_
* `修复` 初始化字符串转换遗留原生缓冲区和 JNI 临时引用, 以及空字符串处理异常的问题 _[`issue #575`](http://issues.autojs6.com/575)_
* `修复` 插件中心显示的版本及 ABI 信息与实际安装包不一致的问题
* `修复` 编码图像的文件描述符及管道输入兼容性问题 (文件大小上限为 64 MiB)
* `修复` 版本日期受构建环境语言影响, 未统一使用英文格式的问题
* `优化` 发布前校验 APK 版本, 签名及变体完整性
* `优化` 图像最多包含 16777216 个像素, 原始图像缓冲区上限为 64 MiB
* `优化` 原生库支持 arm64-v8a/armeabi-v7a/x86/x86_64, 并提供通用及各 ABI 独立安装包

# v1.0.3

###### 2026/09/12

* `修复` 插件服务重建或重复初始化引擎时, 字典重复追加导致识别文字偏移的问题
* `修复` 重新初始化时遗留旧 ONNX Session 的问题, 并串行处理同一进程内的初始化与识别以避免状态竞争

# v1.0.2

###### 2026/09/12

* `修复` 原生库未打包 `libc++_shared.so` 的问题: CMake 现在显式使用 `c++_shared` STL, 由 AGP 随 4 个 ABI 一并打包 NDK r28.2 的 libc++, 16 KB 页大小设备上不再因缺少依赖而无法加载 OCR 引擎
* `修复` Rapid OCR 原生依赖清理开关未初始化时 clean 任务执行失败
* `优化` OpenCV 4.8.0 原生库改用 NDK r28c (Clang 19.0.1) 构建, 4 个 ABI 均支持 16 KB 内存页并附带构建来源清单 (来源: AutoJs6-Plugin-OpenCV)

# v1.0.1

###### 2026/09/11

* `修复` 在同一个打包应用中依次使用 Paddle OCR 和 Rapid OCR 时可能崩溃的问题
* `优化` 构建阶段校验 64 位原生库的 16 KB 内存页对齐及 Manifest 配置, 并输出校验报告
* `依赖` 升级 ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) 版本 1.18.0 -> 1.21.1
* `依赖` 升级 OpenCV 版本 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `新增` Rapid OCR (PP-OCRv6 Small) 插件标识, 插件 ID 为 `rapid-ocr-pp-ocrv6`, 引擎为 `rapid-ocr`, 变体为 `pp-ocrv6`
* `新增` PADDLE_OCR AIDL 服务以及 recognizeText 和 detect 接口. recognizeText 返回文本字符串, detect 为每个结果返回 text, confidence 和 bounds
* `新增` 支持编码图像和原始 ARGB_8888 图像. Android 13 在可用时使用 SharedMemory, 在 SharedMemory 不可用或失败时回退到流式读取
* `新增` 基于 `PP-OCRv6 Small` 检测和识别模型, `ONNX Runtime` 与 `OpenCV Mobile` 实现本地文本检测和识别
* `新增` 支持用于识别的 maxSideLen 和 scoreThreshold 选项
* `新增` 将 preBuild 接入模型资源下载和 `SHA-256` 摘要校验, 并同步 `ONNX Runtime` 原生库
* `新增` 西班牙语, 法语, 俄语, 阿拉伯语, 日语, 韩语, 英语, 简体中文, 香港繁体中文和台湾繁体中文的插件信息与使用说明
* `新增` JSON 源文件和 Python 生成器, 用于生成多语言 README 与 CHANGELOG 文档
* `新增` `arm64-v8a`, `armeabi-v7a`, x86_64 和 universal APK 变体. 发布文件名包含版本和 ABI, 归档副本包含 CRC32 摘要
* `优化` 统一 README 版式与 Gradle 平台版本管理方式
