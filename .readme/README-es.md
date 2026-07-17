<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Plugin Rapid OCR para el reconocimiento de texto con PP-OCRv6 Small en AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/commit/96410a029a429919da9eaaa21b5c900875f32bad"><img alt="Created" src="https://img.shields.io/date/1783059864?color=2e7d32&label=Created"/></a>
    <br>
    <a href="https://developer.android.com/studio/archive"><img alt="Android Studio" src="https://img.shields.io/badge/Android%20Studio-2023.3+-B64FC8"/></a>
    <a href="https://www.jetbrains.com/idea/download/other.html"><img alt="IntelliJ IDEA" src="https://img.shields.io/badge/IntelliJ%20IDEA-2023.3+-EE4677"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Idiomas (Languages)

******

El README.md actual admite los siguientes idiomas:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- Español [es] # actual
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### Introducción

******

El plugin AutoJs6 Rapid OCR PP-OCRv6 Small proporciona detección y reconocimiento de texto basados en RapidOCR, ONNX Runtime y OpenCV Mobile. Puede devolver texto reconocido, valores de confianza y límites de texto.

******

### Funciones

******

- Proporciona el servicio OCR compartido con el ID de plugin `rapid-ocr-pp-ocrv6`, el motor `rapid-ocr` y la variante `pp-ocrv6`.
- Expone la interfaz AIDL de OCR mediante `org.autojs.plugin.PADDLE_OCR` y funciona con el mecanismo de descubrimiento de plugins OCR del host AutoJs6.
- Admite `ocr.rapid.recognizeText` para listas de cadenas y `ocr.rapid.detect` para texto, valores de confianza y límites.
- Admite imágenes codificadas y búferes de imagen `ARGB_8888` sin codificar.
- Proporciona APK para `arm64-v8a`, `armeabi-v7a`, `x86_64` y `universal`.
- Los metadatos del plugin, las instrucciones de uso, el README y el CHANGELOG están localizados en español, francés, ruso, árabe, japonés, coreano, inglés, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwán.
- Construido sobre RapidOCR, ONNX Runtime y OpenCV Mobile.

******

### Ejemplos de uso

******

Después de leer una imagen, use el módulo Rapid OCR para reconocer texto u obtener resultados detallados:

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

`recognizeText` devuelve una lista de cadenas, mientras que `detect` devuelve una lista de resultados detallados.

******

### Interfaz del plugin

******

El host puede descubrir e invocar el plugin con la siguiente identidad:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86_64, universal
```

Cada resultado de `detect` contiene `text`, `confidence` y `bounds`; la interfaz actual no proporciona un campo quad.

******

### Opciones de reconocimiento

******

La implementación actual del plugin lee las siguientes opciones de reconocimiento:

- `maxSideLen`: Longitud lateral máxima utilizada durante la detección. El valor predeterminado es `1024`.
- `scoreThreshold`: Umbral de puntuación de los cuadros de texto. El valor predeterminado es `0.5`.

******

### Recursos de modelos

******

La compilación descarga y verifica los siguientes 4 recursos desde ModelScope:

- `PP-OCRv6_det_small.onnx`: Modelo de detección de texto PP-OCRv6 Small.
- `PP-OCRv6_rec_small.onnx`: Modelo de reconocimiento de texto PP-OCRv6 Small.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: Recurso de modelo auxiliar necesario durante la inicialización del motor.
- `ppocrv6_dict.txt`: Diccionario de reconocimiento PP-OCRv6.

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

##### Para consultar más historial de versiones

* [CHANGELOG-es.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.changelog/CHANGELOG-es.md)

******

### Compilación

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Compilación Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Los parámetros de compilación provienen de `version.properties`; el SDK mínimo actual es 24 y el SDK de destino es 36. `preBuild` descarga y verifica los recursos de modelos, y luego sincroniza las bibliotecas nativas de ONNX Runtime.

******

### Estructura de recursos

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

`strings.xml` contiene descripciones localizadas del plugin y `plugin_instruction.md` contiene instrucciones de uso mostradas por el host. Los archivos README y CHANGELOG se generan desde fuentes JSON mediante `.python/generate_markdown.py`. Solo se genera `README.md` en la raíz del repositorio y no se genera un `CHANGELOG.md` raíz.

******

### Enlaces

******

- Documentación OCR de AutoJs6: https://docs.autojs6.com/#/ocr
- Proyecto RapidOCR: https://github.com/RapidAI/RapidOCR
- Recursos de modelos de ModelScope: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile
