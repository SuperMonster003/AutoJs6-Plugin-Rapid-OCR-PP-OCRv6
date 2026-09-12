******

### Historial de versiones

******

# v1.0.2

###### 2026/09/12

* `Corrección` Corregida la ausencia de `libc++_shared.so` en las bibliotecas nativas empaquetadas: CMake usa ahora explícitamente la STL `c++_shared`, AGP incluye la libc++ del NDK r28.2 para las 4 ABI y el motor OCR vuelve a cargarse en dispositivos con páginas de 16 KB
* `Corrección` El comando `clean` fallaba cuando no se inicializaba la opción de limpieza de las dependencias nativas de Rapid OCR
* `Mejora` Sincronizada la biblioteca nativa OpenCV 4.8.0 con la reconstrucción NDK r28c (Clang 19.0.1) (donante: AutoJs6-Plugin-OpenCV); `libopencv_java4.so` de las 4 ABI mantiene la alineación `PT_LOAD` de 16 KB e incluye un manifiesto de provenance

# v1.0.1

###### 2026/09/11

* `Corrección` Un cierre inesperado al usar Paddle OCR y Rapid OCR consecutivamente en la misma aplicación empaquetada
* `Mejora` Verificación de compilación de la alineación de páginas de 16 KB en bibliotecas nativas de 64 bits, con controles del contrato manifest e informes JSON
* `Dependencia` Actualizacion de ONNX Runtime (com.microsoft.onnxruntime:onnxruntime-android) version 1.18.0 -> 1.21.1
* `Dependencia` Actualización de versión de OpenCV de 4.5.3 a 4.8.0

# v1.0.0

###### 2026/09/01

* `Función` Se añadió la identidad del complemento Rapid OCR (PP-OCRv6 Small) con ID `rapid-ocr-pp-ocrv6`, motor `rapid-ocr` y variante `pp-ocrv6`.
* `Función` Se añadieron el servicio AIDL `PADDLE_OCR` y las interfaces `recognizeText` y `detect`. `recognizeText` devuelve cadenas de texto, mientras `detect` devuelve `text`, `confidence` y `bounds` para cada resultado.
* `Función` Se admiten imágenes codificadas e imágenes `ARGB_8888` sin procesar. Android 13 usa `SharedMemory` cuando está disponible, con lectura por flujo como alternativa cuando `SharedMemory` no está disponible o falla.
* `Función` Se añadieron detección y reconocimiento local de texto con los modelos de detección y reconocimiento `PP-OCRv6 Small`, `ONNX Runtime` y `OpenCV Mobile`.
* `Función` Se admiten las opciones `maxSideLen` y `scoreThreshold` para el reconocimiento.
* `Función` Se conectó `preBuild` con la descarga de recursos de modelos, la verificación de sus resúmenes `SHA-256` y la sincronización de bibliotecas nativas `ONNX Runtime`.
* `Función` Se añadieron metadatos e instrucciones de uso para español, francés, ruso, árabe, japonés, coreano, inglés, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwán.
* `Función` Se añadieron fuentes `JSON` y un generador `Python` para documentos `README` y `CHANGELOG` multilingües.
* `Función` Se añadieron variantes APK para `arm64-v8a`, `armeabi-v7a`, `x86_64` y `universal`. Los nombres de archivos publicados incluyen la versión y la variante `ABI`, y las copias archivadas incluyen un resumen `CRC32`.
* `Mejora` Unificar el diseño del README y la gestión de versiones de la plataforma Gradle
