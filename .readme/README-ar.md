<!--suppress HtmlDeprecatedAttribute, HttpUrlsUsage -->

<div align="center">
  <p>
    <picture>
      <source srcset="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap-night/ic_launcher.png?raw=true" media="(prefers-color-scheme: dark)" />
      <img src="https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/res/mipmap/ic_launcher.png?raw=true" alt="autojs6-plugin-rapid-ocr-pp-ocrv6-ic-launcher" border="0" width="128" />
    </picture>
  </p>

  <p>مكون Rapid OCR الإضافي للتعرف على النص عبر PP-OCRv6 Small في AutoJs6</p>

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

### اللغات (Languages)

******

يدعم README.md الحالي اللغات التالية:

- [简体中文 [zh-Hans]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hans.md)
- [繁體中文 (香港) [zh-Hant-HK]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-HK.md)
- [繁體中文 (台灣) [zh-Hant-TW]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-zh-Hant-TW.md)
- [English [en]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-en.md)
- [Français [fr]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-fr.md)
- [Español [es]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-es.md)
- [日本語 [ja]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ja.md)
- [한국어 [ko]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ko.md)
- [Русский [ru]](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.readme/README-ru.md)
- العربية [ar] # الحالية

******

### مقدمة

******

يوفر مكون AutoJs6 Rapid OCR PP-OCRv6 Small الإضافي اكتشاف النص والتعرف عليه بالاعتماد على RapidOCR و ONNX Runtime و OpenCV Mobile. يمكنه إرجاع النص المتعرف عليه وقيم الثقة وحدود النص.

******

### الميزات

******

- يوفر خدمة OCR المشتركة بمعرف المكون `rapid-ocr-pp-ocrv6` والمحرك `rapid-ocr` والمتغير `pp-ocrv6`.
- يعرض واجهة OCR AIDL عبر `org.autojs.plugin.PADDLE_OCR` ويعمل مع آلية اكتشاف مكونات OCR في مضيف AutoJs6.
- يدعم `ocr.rapid.recognizeText` لقوائم السلاسل و `ocr.rapid.detect` للنص وقيم الثقة والحدود.
- يدعم إدخال الصور المشفرة وإدخال مخزن صور `ARGB_8888` الخام.
- يوفر حزم APK للأنواع `arm64-v8a` و `armeabi-v7a` و `x86_64` و `universal`.
- تمت ترجمة بيانات المكون الإضافي وتعليمات الاستخدام و README و CHANGELOG إلى الإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية في هونغ كونغ والصينية التقليدية في تايوان.
- مبني على RapidOCR و ONNX Runtime و OpenCV Mobile.

******

### أمثلة الاستخدام

******

بعد قراءة صورة استخدم وحدة Rapid OCR للتعرف على النص أو للحصول على نتائج مفصلة:

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

يعيد `recognizeText` قائمة سلاسل بينما يعيد `detect` قائمة بنتائج التعرف المفصلة.

******

### واجهة المكون الإضافي

******

يمكن للمضيف اكتشاف المكون واستدعاؤه باستخدام الهوية التالية:

```text
action: org.autojs.plugin.PADDLE_OCR
engine: rapid-ocr
variant: pp-ocrv6
plugin id: rapid-ocr-pp-ocrv6
supported ABIs: arm64-v8a, armeabi-v7a, x86_64, universal
```

تحتوي كل نتيجة من `detect` على `text` و `confidence` و `bounds`; لا توفر الواجهة الحالية حقل quad.

******

### خيارات التعرف

******

يقرأ التنفيذ الحالي للمكون خيارات التعرف التالية:

- `maxSideLen`: أقصى طول للجانب المستخدم أثناء الاكتشاف. القيمة الافتراضية هي `1024`.
- `scoreThreshold`: عتبة تقييم مربع النص. القيمة الافتراضية هي `0.5`.

******

### أصول النماذج

******

تنزل عملية البناء الأصول الأربعة التالية من ModelScope وتتحقق منها:

- `PP-OCRv6_det_small.onnx`: نموذج اكتشاف النص PP-OCRv6 Small.
- `PP-OCRv6_rec_small.onnx`: نموذج التعرف على النص PP-OCRv6 Small.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: أصل نموذج مساعد مطلوب أثناء تهيئة المحرك.
- `ppocrv6_dict.txt`: قاموس التعرف PP-OCRv6.

******

### سجل الإصدارات

******

# v1.0.0

###### 2026/07/17

* `ميزة` تمت إضافة هوية المكون الإضافي Rapid OCR (PP-OCRv6 Small) مع معرف المكون `rapid-ocr-pp-ocrv6`, والمحرك `rapid-ocr`, والمتغير `pp-ocrv6`.
* `ميزة` تمت إضافة خدمة `PADDLE_OCR` عبر AIDL وواجهتي `recognizeText` و `detect`. تعيد `recognizeText` سلاسل النص, بينما تعيد `detect` قيم `text` و `confidence` و `bounds` لكل نتيجة.
* `ميزة` تم دعم الصور المشفرة وصور `ARGB_8888` الخام. يستخدم Android 13 ذاكرة `SharedMemory` عند توفرها, مع الرجوع إلى القراءة المتدفقة عندما لا تتوفر `SharedMemory` أو تفشل.
* `ميزة` تمت إضافة اكتشاف النص والتعرف عليه محليا باستخدام نماذج الاكتشاف والتعرف `PP-OCRv6 Small`, و `ONNX Runtime`, و `OpenCV Mobile`.
* `ميزة` تم دعم خياري `maxSideLen` و `scoreThreshold` للتعرف.
* `ميزة` تم ربط `preBuild` بتنزيل موارد النماذج, والتحقق من ملخصات `SHA-256`, ومزامنة مكتبات `ONNX Runtime` الأصلية.
* `ميزة` تمت إضافة بيانات المكون الإضافي وتعليمات الاستخدام بالإسبانية, والفرنسية, والروسية, والعربية, واليابانية, والكورية, والإنجليزية, والصينية المبسطة, والصينية التقليدية في هونغ كونغ, والصينية التقليدية في تايوان.
* `ميزة` تمت إضافة مصادر `JSON` ومولد `Python` لمستندات `README` و `CHANGELOG` متعددة اللغات.
* `ميزة` تمت إضافة متغيرات APK لأنظمة `arm64-v8a`, و `armeabi-v7a`, و `x86_64`, و `universal`. تتضمن أسماء ملفات الإصدار النسخة ومتغير `ABI`, وتتضمن النسخ المؤرشفة ملخص `CRC32`.

##### لمزيد من سجل الإصدارات

* [CHANGELOG-ar.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/.changelog/CHANGELOG-ar.md)

******

### البناء

******

```powershell
.\gradlew.bat :app:assembleDebug
```

بناء Release:

```powershell
.\gradlew.bat :app:assembleRelease
```

تأتي معلمات البناء من `version.properties`; الحد الأدنى الحالي لحزمة SDK هو 24 وحزمة SDK المستهدفة هي 36. تقوم `preBuild` بتنزيل أصول النماذج والتحقق منها ثم مزامنة مكتبات ONNX Runtime الأصلية.

******

### بنية الموارد

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

يحتوي `strings.xml` على أوصاف المكون الإضافي المترجمة ويحتوي `plugin_instruction.md` على تعليمات الاستخدام التي يعرضها المضيف. يتم إنشاء README و CHANGELOG من مصادر JSON بواسطة `.python/generate_markdown.py`. يتم إنشاء `README.md` فقط في جذر المستودع ولا يتم إنشاء `CHANGELOG.md` في الجذر.

******

### الروابط

******

- وثائق AutoJs6 OCR: https://docs.autojs6.com/#/ocr
- مشروع RapidOCR: https://github.com/RapidAI/RapidOCR
- أصول نماذج ModelScope: https://www.modelscope.cn/models/RapidAI/RapidOCR
- ONNX Runtime: https://onnxruntime.ai/
- OpenCV Mobile: https://github.com/nihui/opencv-mobile
