<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>用于 AutoJs6 PP-OCRv6 Small 文字识别的 Rapid OCR 插件</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### 语言 (Languages)

******

当前 README.md 支持以下语言:

- 简体中文 [zh-Hans] # 当前
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### 简介

******

AutoJs6 Rapid OCR PP-OCRv6 Small 插件为 AutoJs6 提供基于 RapidOCR, ONNX Runtime 和 OpenCV Mobile 的文字检测与识别能力. 插件可返回识别文本, 置信度和文本边界框.

******

### 功能

******

- 提供共享 OCR 插件服务, 插件 ID 为 `rapid-ocr-pp-ocrv6`, 引擎为 `rapid-ocr`, 变体为 `pp-ocrv6`.
- 通过 `org.autojs.plugin.PADDLE_OCR` 暴露 OCR AIDL 接口, 兼容 AutoJs6 宿主侧 OCR 插件发现机制.
- 支持 `ocr.rapid.recognizeText` 返回字符串列表, 支持 `ocr.rapid.detect` 返回文本, 置信度和边界框.
- 支持编码图片输入和原始 `ARGB_8888` 图片缓冲区输入.
- 提供 `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` 和 `universal` APK.
- 插件信息, 使用说明, README 和 CHANGELOG 支持西班牙语/法语/俄语/阿拉伯语/日语/韩语/英语/简体中文/香港繁体/台湾繁体.
- 基于 RapidOCR, ONNX Runtime 和 OpenCV Mobile 构建.
- 图像最多包含 16777216 个像素, 原始图像缓冲区上限为 64 MiB
- 编码图像最大为 64 MiB, 支持文件描述符和管道传输

******

### 使用示例

******

读取图片后可通过 Rapid OCR 模块识别文本或获取详细结果:

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

`recognizeText` 返回字符串列表, `detect` 返回包含识别详情的结果列表.

******

### 插件接口

******

宿主可通过以下身份发现并调用插件:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
```

`detect` 的每个结果包含 `text`, `confidence` 和 `bounds`, 当前接口不提供 quad 字段.

******

### 识别选项

******

当前插件实现读取以下识别选项:

- `maxSideLen`: 检测阶段的最大边长, 默认值为 `1024`.
- `scoreThreshold`: 文本框分数阈值, 默认值为 `0.5`.

******

### 模型资产

******

构建过程从 ModelScope 的 RapidOCR v3.9.2 下载并校验以下 4 个资产:

- `PP-OCRv6_det_small.onnx`: PP-OCRv6 Small 文本检测模型.
- `PP-OCRv6_rec_small.onnx`: PP-OCRv6 Small 文本识别模型.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: 引擎初始化所需的辅助模型资产.
- `ppocrv6_dict.txt`: PP-OCRv6 识别字典.

******

### 发行历史

******

# v1.0.6

###### 2026/09/20

* `提示` PP-OCRv6 Small 检测模型, 识别模型, 辅助分类模型和字典在两个版本中的 SHA-256 均一致; 本次更新模型来源记录, 识别行为保持不变
* `依赖` RapidOCR 模型来源版本从 v3.9.1 升级至 v3.9.2, 并在 `version.properties` 中统一锁定模型版本

# v1.0.5

###### 2026/09/19

* `修复` AGP 9.1 构建时的 SDK XML v4 解析警告及 JVM 单元测试组装任务误触发 APK 原生库对齐检查的问题 (共享构建插件 1.8.3)
* `优化` 将 compileSdk 与 targetSdk 提升到 37 (Android 17), 插件行为不受新目标版本影响

# v1.0.4

###### 2026/09/13

* `修复` 识别结果包含大量文本块时, JNI 局部引用持续累积, 可能导致 Android 7.x 引用表溢出的问题 _[`issue #575`](http://issues.autojs6.com/575)_
* `修复` 初始化字符串转换遗留原生缓冲区和 JNI 临时引用, 以及空字符串处理异常的问题 _[`issue #575`](http://issues.autojs6.com/575)_
* `修复` 插件中心显示的版本与 ABI 信息匹配实际安装的 APK
* `修复` 编码图像最大为 64 MiB, 支持文件描述符和管道传输
* `修复` 版本日期保持统一的英文格式
* `优化` 发布下载文件生成前校验 APK 版本, 签名与完整变体集合
* `优化` 图像最多包含 16777216 个像素, 原始图像缓冲区上限为 64 MiB
* `优化` 扩展原生 ABI 打包与插件元数据至 arm64-v8a, armeabi-v7a, x86 和 x86_64, 同步通用 APK 与各 ABI 独立 APK

##### 更多发行历史可参阅

* [CHANGELOG-zh-Hans.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-zh-Hans.md)

******

### 构建

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Release 构建:

```powershell
.\gradlew.bat :app:assembleRelease
```

构建参数来自 `version.properties`, 当前最低 SDK 为 24, 目标 SDK 为 36. `preBuild` 会下载并校验模型资产, 同时同步 ONNX Runtime 原生库.

******

### 资源结构

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

`strings.xml` 提供插件描述本地化, `plugin_instruction.md` 提供宿主侧展示的使用说明. README 和 CHANGELOG 由 `.python/generate_markdown.py` 根据 JSON 源文件生成. 根目录只生成 `README.md`, 不生成根目录 `CHANGELOG.md`.

******

### 相关链接

******

- AutoJs6 OCR 文档: https://docs.autojs6.com/#/ocr
- RapidOCR 项目: https://github.com/RapidAI/RapidOCR
- ModelScope 模型资产: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
