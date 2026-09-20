******

### 發行歷史

******

# v1.0.6

###### 2026/09/20

* `提示` PP-OCRv6 Small 檢測模型, 識別模型, 輔助分類模型和字典在兩個版本中的 SHA-256 均一致; 本次更新模型來源記錄, 識別行為保持不變
* `依賴` RapidOCR 模型來源版本從 v3.9.1 升級至 v3.9.2, 並在 `version.properties` 中統一鎖定模型版本

# v1.0.5

###### 2026/09/19

* `修復` AGP 9.1 構建時的 SDK XML v4 解析警告及 JVM 單元測試組裝任務誤觸發 APK 原生程式庫對齊檢查的問題 (共用構建外掛 1.8.3)
* `優化` 將 compileSdk 與 targetSdk 提升到 37 (Android 17), 插件行為不受新目標版本影響

# v1.0.4

###### 2026/09/13

* `修復` 識別結果包含大量文本塊時, JNI 局部引用持續累積, 可能導致 Android 7.x 引用表溢出的問題 _[`issue #575`](http://issues.autojs6.com/575)_
* `修復` 初始化字串轉換遺留原生緩衝區和 JNI 臨時引用, 以及空字串處理異常的問題 _[`issue #575`](http://issues.autojs6.com/575)_
* `修復` 外掛中心顯示的版本與 ABI 資訊符合實際安裝的 APK
* `修復` 編碼圖像最大為 64 MiB, 支援檔案描述符和管道傳輸
* `修復` 版本日期保持統一的英文格式
* `優化` 發佈下載檔案產生前校驗 APK 版本, 簽署與完整變體集合
* `優化` 影像最多包含 16777216 個像素, 原始影像緩衝區上限為 64 MiB
* `優化` 擴展原生 ABI 打包與插件中繼資料至 arm64-v8a, armeabi-v7a, x86 和 x86_64, 同步通用 APK 與各 ABI 獨立 APK

# v1.0.3

###### 2026/09/12

* `修復` 插件服務重建或重複初始化引擎時, 字典重複附加導致識別文字偏移的問題
* `修復` 重新初始化時遺留舊 ONNX Session 的問題, 並循序處理同一程序內的初始化與識別以避免狀態競爭

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
