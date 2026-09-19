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
- Proporciona APK para `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` y `universal`.
- Los metadatos del plugin, las instrucciones de uso, el README y el CHANGELOG están localizados en español, francés, ruso, árabe, japonés, coreano, inglés, chino simplificado, chino tradicional de Hong Kong y chino tradicional de Taiwán.
- Construido sobre RapidOCR, ONNX Runtime y OpenCV Mobile.
- Las imágenes admiten hasta 16777216 píxeles; los búferes de imagen sin procesar se limitan a 64 MiB
- La imagen codificada admite hasta 64 MiB mediante descriptores de archivo y tuberías

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
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
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

# v1.0.5

###### 2026/09/19

* `Corrección` Advertencias de lectura de SDK XML v4 con AGP 9.1 y comprobaciones de alineación nativa de APK activadas por error al ensamblar pruebas unitarias JVM, mediante los plugins de compilación compartidos 1.8.3
* `Mejora` compileSdk y targetSdk suben a 37 (Android 17); el comportamiento del plugin no depende del nuevo objetivo

# v1.0.4

###### 2026/09/13

* `Corrección` Acumulación de referencias locales JNI al convertir muchos bloques de texto, que podía desbordar la tabla de referencias en Android 7.x _[`issue #575`](http://issues.autojs6.com/575)_
* `Corrección` Búferes nativos y referencias JNI temporales retenidos al convertir cadenas durante la inicialización, y manejo incorrecto de cadenas vacías _[`issue #575`](http://issues.autojs6.com/575)_
* `Corrección` La versión y las ABI del centro de complementos coinciden con el APK instalado
* `Corrección` La imagen codificada admite hasta 64 MiB mediante descriptores de archivo y tuberías
* `Corrección` Las fechas de versión mantienen un formato uniforme en inglés
* `Mejora` Validación de las versiones, firmas y variantes completas de los APK antes de crear los archivos de descarga
* `Mejora` Las imágenes admiten hasta 16777216 píxeles; los búferes de imagen sin procesar se limitan a 64 MiB
* `Mejora` Ampliar el empaquetado de ABI nativas y los metadatos del complemento a arm64-v8a, armeabi-v7a, x86 y x86_64, con APK universales e individuales coherentes

# v1.0.3

###### 2026/09/12

* `Corrección` Texto reconocido incorrecto tras recrear el servicio o inicializar el motor repetidamente debido a la adición repetida del diccionario OCR
* `Corrección` Sesiones ONNX anteriores retenidas durante la reinicialización y condiciones de carrera entre la inicialización y el reconocimiento en el mismo proceso

##### Para consultar más historial de versiones

* [CHANGELOG-es.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-es.md)

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
