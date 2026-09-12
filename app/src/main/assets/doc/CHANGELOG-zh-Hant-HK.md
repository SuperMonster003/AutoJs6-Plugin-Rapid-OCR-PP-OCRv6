******

### 發行歷史

******

# v1.0.2

###### 2026/09/12

* `修復` 修復原生庫未打包 `libc++_shared.so` 的問題: CMake 現在顯式使用 `c++_shared` STL, 由 AGP 隨 4 個 ABI 一併打包 NDK r28.2 的 libc++, 16 KB 頁大小設備上不再因缺少依賴而無法載入 OCR 引擎
* `修復` Rapid OCR 原生依賴清理開關未初始化時 `clean` 任務執行失敗
* `優化` 同步 OpenCV 4.8.0 原生庫至 NDK r28c (Clang 19.0.1) 重編版本 (donor: AutoJs6-Plugin-OpenCV), 4 個 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 對齊並附帶 provenance 清單

# v1.0.1

###### 2026/09/11

* `修復` 在同一個打包應用中依次使用 Paddle OCR 和 Rapid OCR 時可能崩潰的問題
* `優化` 建置階段校驗 64 位原生程式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告
* `依賴` 升級 ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) 版本 1.18.0 -> 1.21.1
* `依賴` 升級 OpenCV 版本 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `新增` Rapid OCR (PP-OCRv6 Small) 插件標識, 插件 ID 為 `rapid-ocr-pp-ocrv6`, 引擎為 `rapid-ocr`, 變體為 `pp-ocrv6`.
* `新增` `PADDLE_OCR` AIDL 服務以及 `recognizeText` 和 `detect` 接口. `recognizeText` 返回文本字串, `detect` 為每個結果返回 `text`, `confidence` 和 `bounds`.
* `新增` 支援編碼圖像和原始 `ARGB_8888` 圖像. Android 13 在可用時使用 `SharedMemory`, 在 `SharedMemory` 不可用或失敗時回退到串流讀取.
* `新增` 基於 `PP-OCRv6 Small` 檢測和識別模型, `ONNX Runtime` 與 `OpenCV Mobile` 實現本地文本檢測和識別.
* `新增` 支援用於識別的 `maxSideLen` 和 `scoreThreshold` 選項.
* `新增` 將 `preBuild` 接入模型資源下載和 `SHA-256` 摘要驗證, 並同步 `ONNX Runtime` 原生庫.
* `新增` 西班牙語, 法語, 俄語, 阿拉伯語, 日語, 韓語, 英語, 簡體中文, 香港繁體中文和台灣繁體中文的插件資訊及使用說明.
* `新增` `JSON` 源文件和 `Python` 生成器, 用於生成多語言 `README` 與 `CHANGELOG` 文件.
* `新增` `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK 變體. 發佈文件名包含版本和 `ABI`, 歸檔副本包含 `CRC32` 摘要.
* `優化` 統一 README 版式與 Gradle 平台版本管理方式
