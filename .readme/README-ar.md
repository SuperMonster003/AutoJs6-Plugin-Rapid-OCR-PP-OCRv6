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
- يوفر حزم APK للأنواع `arm64-v8a` و `armeabi-v7a` و `x86` و `x86_64` و `universal`.
- تمت ترجمة بيانات المكون الإضافي وتعليمات الاستخدام و README و CHANGELOG إلى الإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية في هونغ كونغ والصينية التقليدية في تايوان.
- مبني على RapidOCR و ONNX Runtime و OpenCV Mobile.
- تقبل الصور حتى 16777216 بكسل, ويقتصر حجم مخزن الصورة الخام على 64 MiB
- يقتصر حجم الصورة المشفرة على 64 MiB مع دعم واصفات الملفات والأنابيب

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
supported ABIs: arm64-v8a, armeabi-v7a, x86, x86_64, universal
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

# v1.0.4

###### 2026/09/13

* `إصلاح` تراكم مراجع JNI المحلية عند تحويل عدد كبير من الكتل النصية, مما قد يؤدي إلى تجاوز سعة جدول المراجع على Android 7.x _[`issue #575`](http://issues.autojs6.com/575)_
* `إصلاح` بقاء المخازن المؤقتة الأصلية ومراجع JNI المؤقتة عند تحويل السلاسل أثناء التهيئة, ومعالجة غير صحيحة للسلاسل الفارغة _[`issue #575`](http://issues.autojs6.com/575)_
* `إصلاح` تطابق معلومات الإصدار وواجهات ABI في مركز الإضافات مع حزمة الإضافة المثبتة
* `إصلاح` يقتصر حجم الصورة المشفرة على 64 MiB مع دعم واصفات الملفات والأنابيب
* `إصلاح` تستخدم تواريخ الإصدارات تنسيقا إنجليزيا موحدا
* `تحسين` التحقق من إصدار حزم النشر وتوقيعها واكتمال متغيراتها قبل إنشاء ملفات التنزيل
* `تحسين` تقبل الصور حتى 16777216 بكسل, ويقتصر حجم مخزن الصورة الخام على 64 MiB
* `تحسين` توسيع حزم ABI الأصلية وبيانات الإضافة الوصفية لتشمل arm64-v8a وarmeabi-v7a وx86 وx86_64, مع ملفات APK عامة ومنفصلة متطابقة لكل ABI

# v1.0.3

###### 2026/09/12

* `إصلاح` نص تعرف غير صحيح بعد إعادة إنشاء خدمة الإضافة أو تكرار تهيئة المحرك بسبب إلحاق قاموس OCR أكثر من مرة
* `إصلاح` بقاء جلسات ONNX القديمة عند إعادة التهيئة وحالات التسابق بين التهيئة والتعرف داخل العملية نفسها

# v1.0.2

###### 2026/09/12

* `إصلاح` إصلاح غياب `libc++_shared.so` من المكتبات الأصلية المضمنة: يستخدم CMake الآن مكتبة STL `c++_shared` صراحةً بحيث يضمّن AGP مكتبة libc++ من NDK r28.2 لجميع ABI الأربعة, ويعود محرك OCR للتحميل على الأجهزة ذات صفحات 16 كيلوبايت
* `إصلاح` فشل مهمة `clean` عندما لم تتم تهيئة خيار تنظيف التبعيات الأصلية لـ Rapid OCR
* `تحسين` مزامنة مكتبة OpenCV 4.8.0 الأصلية مع إعادة بناء NDK r28c (Clang 19.0.1) (المصدر: AutoJs6-Plugin-OpenCV); تحافظ `libopencv_java4.so` لجميع ABI الأربعة على محاذاة `PT_LOAD` بحجم 16 كيلوبايت وتأتي مع بيان provenance

##### لمزيد من سجل الإصدارات

* [CHANGELOG-ar.md](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/doc/CHANGELOG-ar.md)

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


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
