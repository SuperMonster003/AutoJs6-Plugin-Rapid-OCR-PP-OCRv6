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

يوفر مكون AutoJs6 Rapid OCR PP-OCRv6 Small الإضافي اكتشاف النص والتعرف عليه بالاعتماد على RapidOCR و ONNX Runtime و OpenCV. يمكنه إرجاع النص المتعرف عليه وقيم الثقة وحدود النص.

******

### الميزات

******

- يوفر خدمة OCR المشتركة بمعرف المكون `rapid-ocr-pp-ocrv6` والمحرك `rapid-ocr` والمتغير `pp-ocrv6`.
- يعرض واجهة OCR AIDL عبر `org.autojs.plugin.PADDLE_OCR` ويعمل مع آلية اكتشاف مكونات OCR في مضيف AutoJs6.
- يدعم `ocr.rapid.recognizeText` لقوائم السلاسل و `ocr.rapid.detect` للنص وقيم الثقة والحدود.
- يدعم إدخال الصور المشفرة وإدخال مخزن صور `ARGB_8888` الخام.
- يوفر حزم APK للأنواع `arm64-v8a` و `armeabi-v7a` و `x86` و `x86_64` و `universal`.
- تمت ترجمة بيانات المكون الإضافي وتعليمات الاستخدام و README و CHANGELOG إلى الإسبانية والفرنسية والروسية والعربية واليابانية والكورية والإنجليزية والصينية المبسطة والصينية التقليدية في هونغ كونغ والصينية التقليدية في تايوان.
- مبني على RapidOCR و ONNX Runtime و OpenCV.
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

تقوم عملية البناء بتنزيل الموارد الأربعة التالية من RapidOCR v3.10.0 على ModelScope والتحقق منها:

- `PP-OCRv6_det_small.onnx`: نموذج اكتشاف النص PP-OCRv6 Small.
- `PP-OCRv6_rec_small.onnx`: نموذج التعرف على النص PP-OCRv6 Small.
- `ch_ppocr_mobile_v2.0_cls_mobile.onnx`: أصل نموذج مساعد مطلوب أثناء تهيئة المحرك.
- `ppocrv6_dict.txt`: قاموس التعرف PP-OCRv6.

PP-OCRv6 Small نموذج متعدد اللغات يتعرف على الصينية المبسطة والصينية التقليدية والإنجليزية واليابانية والعديد من اللغات المكتوبة بالحروف اللاتينية (يسرد جدول التوجيه في المشروع الأصلي 52 رمز لغة). لا يوفر المكون الإضافي خيارا للغة ولا حاجة إلى تحديدها.

******

### الترخيص والإسناد

******

تخضع الشيفرة المصدرية للمكون الإضافي لترخيص MPL-2.0. نماذج PP-OCRv6 والقاموس المضمنة هي تحويلات RapidOCR لنماذج PaddleOCR الرسمية, وحقوق النشر مملوكة لشركة Baidu وأصحاب الحقوق المعنيين في PaddleOCR, ويعاد توزيعها دون تعديل بموجب ترخيص Apache-2.0. توجد النصوص الكاملة للتراخيص وبيانات الإسناد الخاصة بـ ONNX Runtime (MIT) و OpenCV (Apache-2.0) والمكونات الأخرى داخل APK في `assets/licenses/`, راجع [`THIRD_PARTY_NOTICES.md`](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/app/src/main/assets/licenses/THIRD_PARTY_NOTICES.md). تُذكر الأسماء لبيان المصدر فقط ولا تعني أي تأييد.

******

### سجل الإصدارات

******

# v1.0.6

###### 2026/10/10

* `تلميح` بصمات SHA-256 لنماذج الكشف والتعرف PP-OCRv6 Small ونموذج التصنيف المساعد والقاموس متطابقة في الإصدارين; يتم تحديث معلومات المصدر دون تغيير سلوك التعرف
* `إصلاح` تعطل عملية المكون الإضافي عند التعرف على الصور ذات عمق البت العالي مثل ملفات PNG بعمق 16 بت التي يفك Android ترميزها بتنسيق RGBA_F16
* `إصلاح` كانت الأخطاء الأصلية أثناء تحميل النموذج أو الاستدلال تنهي عملية المكون الإضافي, وأصبحت تُعاد الآن إلى المضيف كأخطاء
* `تحسين` تستخدم أيقونات مركز الملحقات الأحجام والمواضع والصور الفاتحة والداكنة والخلفيات الدائرية المعدلة في Icon Studio مع الاحتفاظ بالمصادر والمعلمات لإعادة إنتاجها
* `تحسين` تستخدم أيقونات معلومات التطبيق في Android صور Icon Studio وخلفياته الفاتحة والداكنة مع الحفاظ على الصور الشفافة في مركز الملحقات وخيارات المشغل الحالية
* `تحسين` تتحقق تهيئة النموذج من قراءة الأصول بالكامل ومن تطابق قاموس التعرف مع عدد فئات مخرجات النموذج
* `تحسين` يتضمن APK النصوص الكاملة للتراخيص وبيانات الإسناد لنماذج PP-OCRv6 و ONNX Runtime و OpenCV والمكونات الخارجية الأخرى (`assets/licenses/`)
* `تحسين` توضح تعليمات المكون الإضافي اللغات التي يدعمها PP-OCRv6 Small ومعلومات الترخيص
* `تحسين` أصبح OpenCV مربوطًا بشكل ثابت وخاص مع تصدير دوال JNI فقط, ولم يعد ملف APK يتضمن libopencv_java4.so فأصبح أصغر حجمًا, ويمكن للتطبيقات المحزّمة استخدامه مع إصدار آخر من OpenCV
* `تبعية` ترقية مصدر نماذج RapidOCR من v3.9.1 إلى v3.10.0 وتثبيت إصدار النماذج في `version.properties`
* `تبعية` استبدال بيئة تشغيل OpenCV 4.8.0 المشتركة بالمكتبات الثابتة من OpenCV Android SDK 4.8.0

# v1.0.5

###### 2026/09/19

* `إصلاح` تحذيرات قراءة SDK XML v4 مع AGP 9.1 وتشغيل فحص محاذاة مكتبات APK الأصلية خطأ عند تجميع اختبارات JVM, باستخدام إضافات البناء المشتركة 1.8.3
* `تحسين` رفع compileSdk و targetSdk إلى 37 (Android 17), لا يعتمد سلوك المكون الإضافي على الهدف الجديد

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

تأتي معلمات البناء من `version.properties`; الحد الأدنى الحالي لحزمة SDK هو 24 وحزمة SDK المستهدفة هي 37. تقوم `preBuild` بتنزيل أصول النماذج والتحقق منها ثم مزامنة مكتبات ONNX Runtime الأصلية.

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
- OpenCV: https://github.com/opencv/opencv


[16 KB page alignment and build verification](https://github.com/SuperMonster003/AutoJs6-Plugin-Rapid-OCR-PP-OCRv6/blob/master/docs/16kb.md)
