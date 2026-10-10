# RapidOCR v3.10.0 升级路线图

适用仓库: AutoJs6-Plugin-Rapid-OCR-PP-OCRv6 (插件 ID `rapid-ocr-pp-ocrv6`, 引擎 `rapid-ocr`, 变体 `pp-ocrv6`).

起草日期: 2026-10-10. 上游目标: [RapidAI/RapidOCR v3.10.0](https://github.com/RapidAI/RapidOCR/releases/tag/v3.10.0) (2026-10-08 发布, 标签指向提交 `bd941dae09da793e76c6ba15c7aaf40f8424232a`).

证据标签沿用宿主 `docs/dev/page-size-16kb-roadmap.md`: SOURCE (源码 / 上游文件核对), ANDROID_BUILD (Gradle 构建与单元测试), ELF (APK 原生库对齐门禁), DEVICE (4 KB 真机), DEVICE-16K (原生 16 KB 真机或 ps16k 模拟器), DOCS (README / changelog / 来源记录).

---

## 0. 基线

| 项目 | 当前值 | 来源 |
|---|---|---|
| 插件版本 | 1.0.6 (未发布; GitHub 最新发布为 1.0.5), `VERSION_BUILD=32` | `version.properties`, `gh release list` |
| RapidOCR 模型来源 | v3.9.2 (提交 `095232a4`) | `libs/rapidocr/models.provenance.json` |
| 打包模型 | `PP-OCRv6_det_small.onnx`, `PP-OCRv6_rec_small.onnx`, `ch_ppocr_mobile_v2.0_cls_mobile.onnx`, `ppocrv6_dict.txt` | `libs/rapidocr/build.gradle.kts` |
| ONNX Runtime | Maven `onnxruntime-android` 1.21.1 (Paddle v4 / v5 / v6 与 Rapid v3 同版本) | `version.properties`, 兄弟仓库 |
| OpenCV | 4.8.0 共享运行库 (TBB, NDK r28c donor) | `libs/opencv-native-4.8.0.provenance.json` |
| NDK / CMake | 28.2.13676358 / 3.22.1 | `version.properties` |
| 共享 Gradle 插件 | platform-versions / native-alignment 1.9.0 | `settings.gradle.kts` |
| ABI | arm64-v8a, armeabi-v7a, x86, x86_64, universal | `app/build.gradle.kts` |
| C++ 引擎来源 | RapidAI/RapidOcrAndroidOnnx 派生 (`com.benjaminwan.ocrlibrary`), 已本地化修改 | `libs/rapidocr/src/main/cpp` |

本插件只打包 ONNX 模型与自有 C++ 引擎, 不打包 RapidOCR Python 包. 因此上游发布中 Python 专属的改动只需评估, 不需移植.

---

## 1. 上游 v3.9.2 -> v3.10.0 变化与影响

核对方式 (2026-10-10, SOURCE): `gh api repos/RapidAI/RapidOCR/compare/v3.9.2...v3.10.0` (23 个提交, 36 个文件, 全部位于 `python/`, README, CI 与 pre-commit), 逐项阅读 `default_models.yaml`, `config.yaml`, `ch_ppocr_det/main.py`, `ch_ppocr_rec/main.py`, `MODEL_LICENSES.md`; 从 ModelScope `resolve/v3.10.0` 实际下载四个打包资产并计算 SHA-256.

### 1.1 模型资产: 字节一致

| 资产 | 字节数 | SHA-256 | 与 v3.9.2 |
|---|---|---|---|
| `PP-OCRv6_det_small.onnx` | 9929594 | `090f04abcd9d9a7498bc4ebf677e4cb9bdce1fe4197ddb7e529f1ef44e1ff94f` | 一致 |
| `PP-OCRv6_rec_small.onnx` | 21234383 | `6f327246b50388f3c176ae304bd95767ea6dc0c9ae92153ef8cbe210b3c14884` | 一致 |
| `ch_ppocr_mobile_v2.0_cls_mobile.onnx` | 585532 | `e47acedf663230f8863ff1ab0e64dd2d82b838fceb5957146dab185a89d6215c` | 一致 |
| `ppocrv6_dict.txt` | 74947 | `b5f2bfe2bdd9448429e3e82b51c789775d9b42f2403d082b00662eb77e401c5d` | 一致 |

补充核对: 识别模型输出为 `[N, T, 18710]`, 等于字典 18708 行加 CTC 空白与空格; 模型 `metadata_props.character` 与 `ppocrv6_dict.txt` 逐行相同. 检测模型输入 `[N, 3, H, W]` 全动态, 识别模型输入高度固定 48. 上游提交 `bd941da` ("correct model SHA256") 除把全部地址的版本号改为 v3.10.0 外, 只把 `multi_PP-OCRv3_det_mobile` 的 SHA-256 从 `5475c6c7...` 更正为 `55829336...`, 并把一个 torch 分类模型地址从 `resolve/master` 固定到版本号; 不涉及本插件资产.

结论: 本次升级不改变推理输入, 识别结果应与 1.0.6 完全一致. 工作内容是来源记录重新固定, 而非模型替换.

### 1.2 逐项影响

| 上游变化 | 上游提交 | 对本插件 | 处理 |
|---|---|---|---|
| 模型路由集中到 YAML, 公开 PP-OCRv6 支持语言 (#741, #739) | `75946b1`, `4a3070f` | Small 档支持 52 个语言代码 (含 `japan`; Tiny 档 51 个, 不含 `japan`). 插件未声明支持语言 | P4 (可选): 文档与插件说明补充语言范围 |
| 新增 `python/MODEL_LICENSES.md` 与 `python/LICENSE` | `4a3070f` | 第 6 节恰好列出本插件打包的三个 ONNX 资产, 并给出再分发义务 (附 Apache-2.0 文本, 转换修改说明, 保留署名) | P2 (必须): 补齐 APK 与 README 内的许可和署名 |
| `limit_type: max` 时尊重 `limit_side_len` (#740, #742) | `f65c7da` | Python 旧逻辑会把上限改写成 960 / 1500 / 2000 档位. C++ 引擎直接使用 `maxSideLen`, 不存在该缺陷 | 无需移植; P3 中记录差异 |
| 原始内存缓冲区支持格式与 stride 元数据 (#738) | `deea2a0`, `64b1c68` | 插件的 AIDL 原始输入已校验 stride, 仅接受 ARGB_8888 | 无需移植; 若宿主将来发送灰度 / BGR, 另立协议条目 |
| 阿拉伯语前校验 python-bidi (#719) | `0e629c8` | PP-OCRv6 不含阿拉伯语模型 | 不适用 |
| 模型懒加载并加锁 (#725) | `0ad7c04`, `486ec53` | 插件已懒加载 (`OcrService.getEngine`), 并由 `ocrLock` 与原生互斥锁串行化 | 已具备 |
| torch / MNN / NRTR / TensorRT 修复, OpenCV headless | 多个 | 插件只用 ONNX Runtime 与自带 OpenCV | 不适用 |
| `config.yaml` | 无变化 | 检测阈值 0.3, 框阈值 0.5, unclip 1.6, mean / std 0.5, 识别高度 48 均与插件一致 | 无 |

C++ 侧上游 [RapidAI/RapidOcrOnnx](https://github.com/RapidAI/RapidOcrOnnx) 在 2026-10-02 的提交 `675e73f` 将裸 `Ort::Session *` 初始化为 `nullptr`. 本插件三个网络已使用 `std::unique_ptr<Ort::Session>`, 无需移植. Android 示例仓库 RapidOcrAndroidOnnx 自 2025-04 起无代码更新.

### 1.3 与上游默认行为的已知差异 (升级前即存在)

这些差异不是 v3.10.0 引入的, 但升级时应记录, 由 P3 决定是否处理:

- 尺寸策略: 上游默认 `limit_type: min, limit_side_len: 736`, 会把短边小于 736 的图像放大; 插件按长边上限 `maxSideLen` (默认 1024) 只缩小不放大.
- 32 对齐: 上游四舍五入到 32 的倍数; 插件在非整除时向下取整后再减 32 (例如 1074 -> 1024).
- 识别批处理: 上游按宽高比分批并补零到统一宽度; 插件逐行推理, 不补零.
- 方向分类: 插件加载分类模型, 但服务默认 `doAngle=false`, 分类模型只占用初始化时间与内存.
- 置信度: 插件 `confidence` 为检测框分数 (`boxScore`), 宿主 `scoreThreshold` 也映射到检测框分数阈值; 上游 Python 返回识别分数.
- 多余开销: 每次识别都会复制整张补边图像, 绘制文本框, 再转换为 RGBA, 而服务传入的输出位图只有 1 x 1, 结果被丢弃. 对接近 16777216 像素上限的图像, 这会额外占用数百 MiB 的临时内存.

---

## 2. 决策

- D1 模型来源固定到 v3.10.0: ModelScope 修订 `v3.10.0`, 上游提交 `bd941dae`. 只有四个资产重新下载后 SHA-256 与第 1.1 节一致才可推进; 任一不一致即停止, 转为模型替换流程 (需要全量识别回归).
- D2 版本号: 若执行时 1.0.6 仍未发布, 合并进 1.0.6 的 changelog (依赖条目改为 `3.9.1 -> 3.10.0`); 若 1.0.6 已发布, 使用 1.0.7. 不改变公开 AIDL, 选项与结果语义, 因此只做补丁版本.
- D3 ONNX Runtime 升级不随本次进行: 用户 APK 内 Rapid v3 / v6 与 Paddle v4 / v5 / v6 共用一份 `libonnxruntime.so`, 版本必须全体一致. ORT 升级作为独立轨道 P5, 需舰队协调.
- D4 许可合规属于本次必做: 上游已经书面列出本插件资产的再分发义务, 插件目前只在 `models.provenance.json` 记录 `Apache-2.0`, APK 与 README 均未附许可文本和转换说明.
- D5 行为差异 (第 1.3 节) 默认不改: 任何改动都会改变识别结果, 需要样本集对比证据和 changelog 说明, 并按用户可见变化评估 SemVer.

---

## 3. 阶段

### P0 基线与前置

- [x] 读取 `git status`, 分支, 最近提交; 确认 2026-10-10 审查修复 (第 5 节) 已提交. (SOURCE) 2026-10-10: 审查修复为提交 `ae3d4af`, 插件版本规则为 `3766e25`, 工作区干净.
- [x] 在 1.0.6 当前产物上运行 `PluginBinderContractTest`, `HighBitDepthImageTest`, `OcrReinitializationTest`, 记录设备, ABI, 页大小与识别文本, 作为升级前对照. (DEVICE) 2026-10-10: 审查修复后的同一源码已在 Sony XQ-DQ72 上通过 (见第 5 节), 识别文本含 "Hello".
- [x] 记录当前 Release APK 的 `assets/models/*` SHA-256 (应等于第 1.1 节). (ANDROID_BUILD) 2026-10-10: 以 v3.9.2 资产构建的 1.0.6 Debug universal APK (Release 打包同一组资产) 中四个文件的字节数与 SHA-256 均等于第 1.1 节.

### P1 模型来源重新固定

- [x] `version.properties`: `RAPID_OCR_MODEL_VERSION=3.10.0`. `build.gradle.kts` 的下载地址由该键派生, 无需改动. (SOURCE)
- [x] `libs/rapidocr/models.provenance.json`: 更新 `version`, `upstream_commit` (`bd941dae09da793e76c6ba15c7aaf40f8424232a`), `release_url`, `upstream_model_config`, `license_url` (改指 `python/LICENSE`), `model_card_url`, `verified_date`, `upgrade_notes` 与四个资产 URL; 新增 `model_license_notice_url` 指向 `python/MODEL_LICENSES.md`. (SOURCE)
- [x] 删除本地 `libs/rapidocr/src/main/assets/models/` 后执行 `:libs:rapidocr:downloadRapidOcrV6Models`, 确认四个资产经 SHA-256 门禁重新下载. (ANDROID_BUILD) 2026-10-10: 四个资产从 `resolve/v3.10.0` 重新下载, 校验通过, 字节数与 SHA-256 不变.
- [x] `.readme/common.json` 的 `rapidocr_model_version` 改为 3.10.0; 十语 changelog 写入依赖条目与 "模型 SHA-256 不变, 识别行为不变" 的提示. 运行 `python .python/generate_markdown.py` 及 `--check`. (DOCS)

### P2 模型许可与署名

- [x] 在 APK 内附带第三方声明: RapidOCR (Apache-2.0), PaddleOCR 模型权重 (Apache-2.0, 版权归 Baidu / PaddleOCR 权利人), 上游给出的转换修改说明原文, ONNX Runtime (MIT), OpenCV 4.8.0 (Apache-2.0), 静态链接进 OpenCV 的 Intel TBB 2020.2 (Apache-2.0, 见 `libopencv_java4.so` 的构建信息 `Parallel framework: TBB (ver 2020.2 interface 11102)`), NDK 的 `libc++_shared.so` (Apache-2.0 WITH LLVM-exception), Clipper (Boost). 附 Apache-2.0 全文或其固定链接, 以满足 MODEL_LICENSES 第 7 节第 1 项. (SOURCE; DOCS) 2026-10-10: 落在 `app/src/main/assets/licenses/` (与 changelog 目录分开): `THIRD_PARTY_NOTICES.md`, 官方 Apache-2.0 全文, ONNX Runtime 1.21.1 MIT 许可, Clipper 6.4.2 Boost 许可, 以及从 AutoJs6-Plugin-OpenCV 原样导出的 33 个 OpenCV 4.8.0 许可文件 (本插件的 `libopencv_java4.so` 四个 ABI 与其来源记录逐字节一致). 契约测试 `test_third_party_notices_follow_packaged_versions` 校验声明中的版本, 上游提交与模型哈希.
- [x] `models.provenance.json` 每个资产补 `upstream_model` (`PP-OCRv6_small_det`, `PP-OCRv6_small_rec`, `ch_ppocr_mobile_v2.0_cls`), 官方模型卡链接与 `copyright` 字段; 字典标注来源为 PaddleOCR `ppocrv6_dict.txt` (MODEL_LICENSES 第 6 节未列出字典, 需单独记录). (SOURCE) 2026-10-10: 另增 `redistribution` 段 (APK 内声明位置, 许可全文位置, 上游转换说明原文).
- [x] README 十语新增 "许可与署名" 段落, 说明插件代码 (MPL-2.0) 与模型 (Apache-2.0) 分属不同许可, 并链接上述声明; 不使用 Baidu / PaddlePaddle / RapidOCR 商标暗示背书 (MODEL_LICENSES 第 8 节). (DOCS)
- [x] 若设置或说明界面链接了发行历史, 同时链接第三方声明. (SOURCE) 2026-10-10: 插件无设置界面; 宿主展示的 `plugin_instruction.md` 十一个语言版本新增 "许可" 段, 指向 `assets/licenses/`.

### P3 行为一致性评估 (可选, 不阻塞发布)

- [ ] 建立固定样本集: 现有 "Hello AutoJs6" 合成图, 中英混排截图, 竖排或旋转文本, 小尺寸 (短边 < 200) 与大尺寸 (长边 > 2000) 图片, 至少 20 张. 样本与期望输出纳入 `libs/rapidocr/src/androidTest` 资源. (SOURCE)
- [ ] 在桌面用 RapidOCR 3.10.0 (onnxruntime, 同一组模型, 关闭 `use_cls` 以对齐插件默认) 生成参考输出; 在设备上运行插件得到对照输出; 比较文本编辑距离与框 IoU. (DEVICE)
- [ ] 针对第 1.3 节逐项给出结论: 保持现状, 或以新选项 (默认值不变) 暴露, 或修改默认值. 修改默认值需要 changelog `优化` 条目并说明识别结果可能变化. (DOCS)
- [ ] 若决定不再使用方向分类, 评估停止加载分类模型 (减少初始化与内存), 但须保持 `init` 的 JNI 签名兼容. (SOURCE)
- [ ] 输出位图为 1 x 1 时跳过文本框绘制与 RGBA 转换, 并用大图 (约 4000 x 4000) 对比修改前后的原生堆峰值. 识别结果必须逐字一致. (SOURCE; DEVICE)

### P4 语言范围说明 (可选)

- [x] README 与 `plugin_instruction.md` 十语补充: PP-OCRv6 Small 为多语言模型, 覆盖简体中文, 繁体中文, 英文, 日文及多种拉丁字母语言 (以 `default_models.yaml` 的 `ppocrv6_langs` 为准, 共 52 个代码, 其中 `french` / `german` 是 `fr` / `de` 的别名). 不新增选项. (DOCS) 2026-10-10: README 模型资产段与插件说明 "语言" 段均已补充.

### P5 ONNX Runtime 升级 (独立轨道, 舰队协调)

Maven 最新为 1.31.0 (2026-10-09). 不在本次升级范围内执行, 只在满足以下前提时启动:

- [ ] 宿主与 Paddle v4 / v5 / v6, Rapid v3 维护者确认同一目标版本, 同批发布. (SOURCE)
- [ ] 同步 `assets/licenses/onnxruntime-<版本>/LICENSE` 与 `THIRD_PARTY_NOTICES.md` 中的 ORT 版本 (契约测试会在不同步时失败). (SOURCE)
- [ ] 核对 ORT C++ API 变化 (头文件, `Ort::Session` 构造, `GetInputNameAllocated`) 与 Android AAR 的最低 SDK (须仍兼容 minSdk 24). (ANDROID_BUILD)
- [ ] 新 `libonnxruntime.so` 四个 ABI 的 64 位 ELF 对齐通过共享门禁; armeabi-v7a / x86 按现有策略记录. (ELF)
- [ ] 识别结果与 P0 对照逐字一致; 用户 APK 内 Paddle + Rapid 同进程识别通过. (DEVICE; DEVICE-16K)

### P6 验证与发布

- [ ] 源码检查: `python .python/generate_markdown.py --check`, `python -m unittest discover -s .python -p "test_*.py"`, `python .python/generate_icon_studio.py --check`. (SOURCE)
- [ ] 构建: `:app:assembleDebug :app:testDebugUnitTest`, `:app:assembleDebugAndroidTest :app:lintDebug`. (ANDROID_BUILD)
- [ ] 设备: 4 KB arm64 真机运行全部 instrumentation (含管道输入与高位深 PNG); x86_64 ps16k 模拟器 (CI 矩阵); 原生 16 KB arm64 若可用则复验. 缺失项写入发布评估. (DEVICE; DEVICE-16K)
- [ ] 发布: 提交前 `VERSION_BUILD = git rev-list --count HEAD + 1`; `:app:appendDigestToReleasedFiles` 归档全部 ABI 与 universal APK; 核对 `getInfo()` 的 ABI 清单与实际安装包一致. (ELF; ANDROID_BUILD)

---

## 4. 风险与回滚

- ModelScope 修订 `v3.10.0` 下的文件被替换: 下载任务的 SHA-256 门禁会使构建失败. 处理方式是停止升级, 保留 v3.9.2 固定.
- 许可声明遗漏: 以 MODEL_LICENSES 第 7 节四项为检查表逐项勾选.
- ORT 版本分裂: P5 未完成前, 任何仓库不得单独升级 ORT.
- 回滚: P1 / P2 只涉及来源记录与文档, 回退相关提交即可; 模型字节不变, 无数据迁移.

---

## 5. 2026-10-10 审查记录 (升级前修复)

本仓库此前没有独立 roadmap. 与本插件相关的已勾选条目位于宿主 `docs/dev/page-size-16kb-roadmap.md` (D3, D7, P2.4, X1, P2.6) 与 `docs/dev/android-sdk-37-roadmap.md` (附录 E 的 37 / 37 行). 逐项核对结果:

| 条目 | 核对结果 |
|---|---|
| P2.4 Maven ORT 1.21.1 替换预编译库 | 已完成: `extractOnnxRuntimeNative` 解包 Maven AAR |
| P2.4 / D3 NDK 28.2 与显式 16 KB 链接参数 | 已完成: `RAPID_OCR_NDK_VERSION=28.2.13676358`, CMake `-Wl,-z,max-page-size=16384` |
| X1 `c++_shared` 与 OpenCV r28c donor | 已完成: `-DANDROID_STL=c++_shared`, `libs/opencv-native-4.8.0.provenance.json` |
| P2.4 识别一致性 | 已有 `OcrReinitializationTest` 覆盖 "Hello AutoJs6" 识别与重复初始化 |
| P2.4 README 更新 | 未完全完成: README 仍写 "OpenCV Mobile", 已修正为 OpenCV |
| SDK 37 升级的 README 刷新 | 未完成: README 仍写目标 SDK 36, 已修正为 37 |
| 共享 Gradle 插件 1.9.0 升级 | 未完成: 契约测试仍断言 1.8.3 (测试失败), AGENTS.md 与 `docs/16kb.md` 版本过期. 按维护者要求改为不固定版本: 规则与文档只写插件名并推荐最新发布版本, 契约测试只要求两个插件来自已发布版本且版本相同 |

审查中另外发现并修复的运行时问题 (写入 1.0.6 changelog 十语 `修复` / `优化`):

- [x] 16 位 PNG 使插件进程崩溃: `BitmapFactory` 在默认 ARGB_8888 偏好下仍把高位深 PNG 解码为 RGBA_F16 (10 位图像为 RGBA_1010102). 原生 `bitmapToMat` 拒绝该格式后, `detect` 未检查挂起的 Java 异常, 继续以空矩阵调用 `cv::cvtColor`, 未捕获的 `cv::Exception` 触发 SIGABRT. 修复前在 Sony XQ-DQ72 (API 33, arm64, 4 KB) 复现: `Abort message: 'terminating due to uncaught exception of type cv::Exception: ... (-215:Assertion failed) !_src.empty() in function 'cvtColor''`, 栈顶为 `Java_com_benjaminwan_ocrlibrary_OcrEngine_detect+176`. 修复: `OcrService.toArgb8888` 在解码后统一转换为 ARGB_8888; 原生层读取位图失败时改抛 `IllegalArgumentException`. (DEVICE)
- [x] 原生异常终止进程: 模型加载 (`Ort::Session` 构造) 与推理 (`session->Run`, OpenCV 调用) 的 C++ 异常此前没有任何 JNI 边界捕获. 现在 `init` / `detect` / `benchmark` 统一转换为 `IllegalStateException`, 经 Binder 返回宿主. (SOURCE)
- [x] 初始化校验: 模型与字典资产必须完整读取; 识别字典类别数 (含 CTC 空白与空格) 必须等于模型输出维度 (当前 18710); 新会话与字典全部就绪后才替换旧状态, 失败时保留可用的旧引擎. 该校验也是后续模型升级的安全网. (SOURCE)
- [x] 构建日志中 `OPENCV_MOBILE: null` 两行改为输出实际 OpenCV 版本. (ANDROID_BUILD)

验证 (2026-10-10, Sony XQ-DQ72, Android 13 / API 33, arm64-v8a, 页大小 4096):

| 套件 | 结果 |
|---|---|
| `HighBitDepthImageTest` (新增, 文件与管道两种传输, RGBA_F16 夹具) | 修复前进程崩溃; 修复后通过, 未跳过 |
| `PluginBinderContractTest` | 通过 |
| `OcrReinitializationTest` (含新增的 ALPHA_8 拒绝用例, 拒绝后引擎仍可识别) | 4 / 4 通过 (连同 `ExampleInstrumentedTest`) |
| JVM 单元测试 | 16 / 16 通过 |
| Python 检查 (`generate_markdown --check`, 21 个 unittest, `generate_icon_studio --check`) | 通过 |
| 原生对齐门禁 (Debug, 五个 APK) | 通过, `libRapidOcr.so` 四个 ABI 均为 16384 |

未覆盖: x86 / x86_64 / armeabi-v7a 运行时, 原生 16 KB 设备与 ps16k 模拟器, Release 签名构建与 `:app:appendDigestToReleasedFiles` 归档. 这些项目留给 P6 与 CI 矩阵.
