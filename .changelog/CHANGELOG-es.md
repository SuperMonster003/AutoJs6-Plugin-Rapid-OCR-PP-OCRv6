******

### Historial de versiones

******

# v1.0.0

###### 2026/07/17

* `Función` Se añadió la identidad del complemento Rapid OCR (PP-OCRv6 Small) con ID `rapid-ocr-pp-ocrv6`, motor `rapid-ocr` y variante `pp-ocrv6`.
* `Función` Se añadieron el servicio AIDL `PADDLE_OCR` y las interfaces `recognizeText` y `detect`. `recognizeText` devuelve cadenas de texto, mientras `detect` devuelve `text`, `confidence` y `bounds` para cada resultado.
* `Función` Se admiten imágenes codificadas e imágenes `ARGB_8888` sin procesar. Android 13 usa `SharedMemory` cuando está disponible, con lectura por flujo como alternativa cuando `SharedMemory` no está disponible o falla.
* `Función` Se añadieron detección y reconocimiento local de texto con los modelos de detección y reconocimiento `PP-OCRv6 Small`, `ONNX Runtime` y `OpenCV Mobile`.
* `Función` Se admiten las opciones `maxSideLen` y `scoreThreshold` para el reconocimiento.
* `Función` Se conectó `preBuild` con la descarga de recursos de modelos, la verificación de sus resúmenes `SHA-256` y la sincronización de bibliotecas nativas `ONNX Runtime`.
* `Función` Se añadieron metadatos e instrucciones de uso para español, francés, ruso, árabe, japonés, coreano, inglés, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwán.
* `Función` Se añadieron fuentes `JSON` y un generador `Python` para documentos `README` y `CHANGELOG` multilingües.
* `Función` Se añadieron variantes APK para `arm64-v8a`, `armeabi-v7a`, `x86_64` y `universal`. Los nombres de archivos publicados incluyen la versión y la variante `ABI`, y las copias archivadas incluyen un resumen `CRC32`.
