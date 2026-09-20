<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>Плагин Rapid OCR для распознавания текста PP-OCRv6 Small в AutoJs6</p>

  <p>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/releases"><img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?label=Release"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/issues"><img alt="GitHub closed issues" src="https://img.shields.io/github/issues/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=A24232&label=Issues"/></a>
    <a href="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/LICENSE"><img alt="GitHub License" src="https://img.shields.io/github/license/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6?color=534BAE&label=License"/></a>
  </p>
</div>

******

### Языки (Languages)

******

Текущий README.md поддерживает следующие языки:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- Русский [ru] # текущий
- [العربية [ar]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ar.md)

******

### Введение

******

Плагин AutoJs6 Rapid OCR PP-OCRv6 Small предоставляет обнаружение и распознавание текста на основе RapidOCR, ONNX Runtime и OpenCV Mobile. Он может возвращать распознанный текст, значения достоверности и границы текста.

******

### Возможности

******

- Предоставляет общий сервис OCR с ID плагина `rapid-ocr-pp-ocrv6`, движком `rapid-ocr` и вариантом `pp-ocrv6`.
- Открывает интерфейс OCR AIDL через `org.autojs.plugin.PADDLE_OCR` и работает с механизмом обнаружения плагинов OCR в AutoJs6.
- Поддерживает `ocr.rapid.recognizeText` для списков строк и `ocr.rapid.detect` для текста, значений достоверности и границ.
- Поддерживает кодированные изображения и необработанные буферы изображений `ARGB_8888`.
- Предоставляет APK для `arm64-v8a`, `armeabi-v7a`, `x86`, `x86_64` и `universal`.
- Метаданные плагина, инструкции, README и CHANGELOG локализованы на испанский, французский, русский, арабский, японский, корейский, английский, упрощенный китайский, гонконгский традиционный китайский и тайваньский традиционный китайский.
- Создан на основе RapidOCR, ONNX Runtime и OpenCV Mobile.
- Изображения могут содержать до 16777216 пикселей; размер буферов необработанных изображений ограничен 64 MiB
- Размер закодированного изображения ограничен 64 MiB с поддержкой файловых дескрипторов и каналов

******

### Примеры использования

******

После чтения изображения используйте модуль Rapid OCR для распознавания текста или получения подробных результатов:

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

`recognizeText` возвращает список строк, а `detect` возвращает список подробных результатов распознавания.

******

### Интерфейс плагина

******

Хост может обнаружить и вызвать плагин со следующими идентификаторами:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
```

Каждый результат `detect` содержит `text`, `confidence` и `bounds`; текущий интерфейс не предоставляет поле quad.

******

### Параметры распознавания

******

Текущая реализация плагина считывает следующие параметры распознавания:

- `maxSideLen`: Максимальная длина стороны при обнаружении. Значение по умолчанию равно `1024`.
- `scoreThreshold`: Порог оценки текстового блока. Значение по умолчанию равно `0.5`.

******

### Ресурсы моделей

******

При сборке загружаются и проверяются следующие 4 ресурса RapidOCR v3.9.2 из ModelScope:

- `PP-OCRv6_det_small.onnx`: Модель обнаружения текста PP-OCRv6 Small.
- `PP-OCRv6_rec_small.onnx`: Модель распознавания текста PP-OCRv6 Small.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: Вспомогательный ресурс модели для инициализации движка.
- `ppocrv6_dict.txt`: Словарь распознавания PP-OCRv6.

******

### История выпусков

******

# v1.0.6

###### 2026/09/20

* `Подсказка` Модели обнаружения и распознавания PP-OCRv6 Small, вспомогательная модель классификации и словарь имеют одинаковые SHA-256 в обеих версиях; обновляются сведения о происхождении без изменения поведения распознавания
* `Зависимость` Источник моделей RapidOCR обновлен с v3.9.1 до v3.9.2, версия моделей закреплена в `version.properties`

# v1.0.5

###### 2026/09/19

* `Исправление` Предупреждения чтения SDK XML v4 с AGP 9.1 и ошибочный запуск проверки выравнивания нативных библиотек APK при сборке модульных тестов JVM, устраненные общими плагинами сборки 1.8.3
* `Улучшение` Подняты compileSdk и targetSdk до 37 (Android 17); поведение плагина не зависит от нового целевого уровня

# v1.0.4

###### 2026/09/13

* `Исправление` Накопление локальных ссылок JNI при преобразовании большого числа текстовых блоков, которое могло переполнить таблицу ссылок в Android 7.x _[`issue #575`](http://issues.autojs6.com/575)_
* `Исправление` Сохранение нативных буферов и временных ссылок JNI при преобразовании строк во время инициализации, а также некорректная обработка пустых строк _[`issue #575`](http://issues.autojs6.com/575)_
* `Исправление` Информация о версии и ABI в центре плагинов соответствует установленному APK
* `Исправление` Размер закодированного изображения ограничен 64 MiB с поддержкой файловых дескрипторов и каналов
* `Исправление` Даты версий используют единый английский формат
* `Улучшение` Проверка версий, подписей и полного набора вариантов APK перед подготовкой файлов для загрузки
* `Улучшение` Изображения могут содержать до 16777216 пикселей; размер буферов необработанных изображений ограничен 64 MiB
* `Улучшение` Расширение набора нативных ABI и метаданных плагина до arm64-v8a, armeabi-v7a, x86 и x86_64 с согласованными универсальными и отдельными APK для каждой ABI

##### Дополнительная история выпусков

* [CHANGELOG-ru.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-ru.md)

******

### Сборка

******

```powershell
.\gradlew.bat :app:assembleDebug
```

Сборка Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

Параметры сборки берутся из `version.properties`; текущий минимальный SDK равен 24, а целевой SDK равен 36. `preBuild` загружает и проверяет ресурсы моделей, затем синхронизирует нативные библиотеки ONNX Runtime.

******

### Структура ресурсов

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

`strings.xml` содержит локализованные описания плагина, а `plugin_instruction.md` содержит инструкции для отображения в хосте. README и CHANGELOG генерируются из исходных файлов JSON скриптом `.python/generate_markdown.py`. В корне репозитория создается только `README.md`, а корневой `CHANGELOG.md` не создается.

******

### Ссылки

******

- Документация AutoJs6 OCR: https://docs.autojs6.com/#/ocr
- Проект RapidOCR: https://github.com/RapidAI/RapidOCR
- Ресурсы моделей ModelScope: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
