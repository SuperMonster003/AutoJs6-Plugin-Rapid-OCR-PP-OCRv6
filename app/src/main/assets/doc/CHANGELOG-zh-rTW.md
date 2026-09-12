******

### 發行歷史

******

# v1.0.3

###### 2026/09/12

* `修正` 外掛服務重建或重複初始化引擎時, 字典重複附加導致辨識文字偏移的問題
* `修正` 重新初始化時遺留舊 ONNX Session 的問題, 並循序處理同一處理程序內的初始化與辨識以避免狀態競爭

# v1.0.2

###### 2026/09/12

* `修正` 修復原生程式庫未打包 `libc++_shared.so` 的問題: CMake 現在明確使用 `c++_shared` STL, 由 AGP 隨 4 個 ABI 一併打包 NDK r28.2 的 libc++, 16 KB 分頁大小裝置上不再因缺少相依而無法載入 OCR 引擎
* `修正` Rapid OCR 原生依賴清理開關未初始化時 `clean` 任務執行失敗
* `改善` 同步 OpenCV 4.8.0 原生程式庫至 NDK r28c (Clang 19.0.1) 重新建置版本 (donor: AutoJs6-Plugin-OpenCV), 4 個 ABI 的 `libopencv_java4.so` 保持 16 KB `PT_LOAD` 對齊並附帶 provenance 清單

# v1.0.1

###### 2026/09/11

* `修正` 在同一個打包應用中依序使用 Paddle OCR 和 Rapid OCR 時可能崩潰的問題
* `改善` 建置階段校驗 64 位原生函式庫的 16 KB 頁面大小對齊, 檢查 manifest 契約並輸出 JSON 報告
* `相依性` 升級 ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) 版本 1.18.0 -> 1.21.1
* `相依性` 升級 OpenCV 版本 4.5.3 -> 4.8.0

# v1.0.0

###### 2026/09/01

* `新增` Rapid OCR (PP-OCRv6 Small) 外掛識別資訊, 外掛 ID 為 `rapid-ocr-pp-ocrv6`, 引擎為 `rapid-ocr`, 變體為 `pp-ocrv6`.
* `新增` `PADDLE_OCR` AIDL 服務以及 `recognizeText` 和 `detect` 介面. `recognizeText` 回傳文字字串, `detect` 為每個結果回傳 `text`, `confidence` 和 `bounds`.
* `新增` 支援編碼影像和原始 `ARGB_8888` 影像. Android 13 在可用時使用 `SharedMemory`, 在 `SharedMemory` 不可用或失敗時回退到串流讀取.
* `新增` 基於 `PP-OCRv6 Small` 偵測和辨識模型, `ONNX Runtime` 與 `OpenCV Mobile` 實作本機文字偵測和辨識.
* `新增` 支援用於辨識的 `maxSideLen` 和 `scoreThreshold` 選項.
* `新增` 將 `preBuild` 接入模型資源下載及 `SHA-256` 摘要驗證, 並同步 `ONNX Runtime` 原生程式庫.
* `新增` 西班牙文, 法文, 俄文, 阿拉伯文, 日文, 韓文, 英文, 簡體中文, 香港繁體中文和台灣繁體中文的外掛中繼資料與使用說明.
* `新增` `JSON` 來源檔和 `Python` 產生器, 用於產生多語言 `README` 與 `CHANGELOG` 文件.
* `新增` `arm64-v8a`, `armeabi-v7a`, `x86_64` 和 `universal` APK 變體. 發佈檔名包含版本和 `ABI`, 封存副本包含 `CRC32` 摘要.
* `改善` 統一 README 版式與 Gradle 平台版本管理方式
