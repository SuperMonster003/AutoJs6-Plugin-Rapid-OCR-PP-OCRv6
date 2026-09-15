#include "OcrResultUtils.h"

using ocr::jni::LocalRef;

OcrResultUtils::OcrResultUtils(JNIEnv *env, const OcrResult &ocrResult, jobject boxImg)
        : jniEnv(env), listClass(env), pointClass(env), textBlockClass(env) {
    if (env->ExceptionCheck()) return;
    LocalRef<jclass> resultClass(env, env->FindClass("com/benjaminwan/ocrlibrary/OcrResult"));
    if (resultClass.get() == nullptr) return;
    jmethodID resultConstructor = env->GetMethodID(resultClass.get(), "<init>",
            "(DLjava/util/ArrayList;Landroid/graphics/Bitmap;DLjava/lang/String;)V");
    if (resultConstructor == nullptr || !initJavaTypes()) return;

    LocalRef<jobject> textBlocks(env, getTextBlocks(ocrResult.textBlocks));
    if (env->ExceptionCheck() || textBlocks.get() == nullptr) return;
    LocalRef<jstring> text(env, env->NewStringUTF(ocrResult.strRes.c_str()));
    if (text.get() == nullptr) return;
    LocalRef<jobject> result(env, env->NewObject(resultClass.get(), resultConstructor,
            ocrResult.dbNetTime, textBlocks.get(), boxImg, ocrResult.detectTime, text.get()));
    if (env->ExceptionCheck()) return;
    jOcrResult = result.release();
}

jobject OcrResultUtils::getJObject() const {
    return jOcrResult;
}

bool OcrResultUtils::initJavaTypes() {
    listClass.reset(jniEnv->FindClass("java/util/ArrayList"));
    if (listClass.get() == nullptr) return false;
    listConstructor = jniEnv->GetMethodID(listClass.get(), "<init>", "()V");
    if (listConstructor == nullptr) return false;
    listAdd = jniEnv->GetMethodID(listClass.get(), "add", "(Ljava/lang/Object;)Z");
    if (listAdd == nullptr) return false;

    pointClass.reset(jniEnv->FindClass("com/benjaminwan/ocrlibrary/Point"));
    if (pointClass.get() == nullptr) return false;
    pointConstructor = jniEnv->GetMethodID(pointClass.get(), "<init>", "(II)V");
    if (pointConstructor == nullptr) return false;

    textBlockClass.reset(jniEnv->FindClass("com/benjaminwan/ocrlibrary/TextBlock"));
    if (textBlockClass.get() == nullptr) return false;
    textBlockConstructor = jniEnv->GetMethodID(textBlockClass.get(), "<init>",
            "(Ljava/util/ArrayList;FIFDLjava/lang/String;[FDD)V");
    return textBlockConstructor != nullptr;
}

jobject OcrResultUtils::newJBoxPoint(const std::vector<cv::Point> &boxPoint) {
    LocalRef<jobject> list(jniEnv, jniEnv->NewObject(listClass.get(), listConstructor));
    if (jniEnv->ExceptionCheck() || list.get() == nullptr) return nullptr;
    for (const auto &point : boxPoint) {
        LocalRef<jobject> jPoint(jniEnv, jniEnv->NewObject(
                pointClass.get(), pointConstructor, point.x, point.y));
        if (jniEnv->ExceptionCheck() || jPoint.get() == nullptr) return nullptr;
        jniEnv->CallBooleanMethod(list.get(), listAdd, jPoint.get());
        if (jniEnv->ExceptionCheck()) return nullptr;
    }
    return list.release();
}

jobject OcrResultUtils::getTextBlock(const TextBlock &textBlock) {
    LocalRef<jobject> points(jniEnv, newJBoxPoint(textBlock.boxPoint));
    if (jniEnv->ExceptionCheck() || points.get() == nullptr) return nullptr;
    LocalRef<jstring> text(jniEnv, jniEnv->NewStringUTF(textBlock.text.c_str()));
    if (text.get() == nullptr) return nullptr;
    LocalRef<jfloatArray> scores(jniEnv, newJScoreArray(textBlock.charScores));
    if (jniEnv->ExceptionCheck() || scores.get() == nullptr) return nullptr;
    LocalRef<jobject> block(jniEnv, jniEnv->NewObject(textBlockClass.get(), textBlockConstructor,
            points.get(), textBlock.boxScore, textBlock.angleIndex, textBlock.angleScore,
            textBlock.angleTime, text.get(), scores.get(), textBlock.crnnTime, textBlock.blockTime));
    if (jniEnv->ExceptionCheck()) return nullptr;
    return block.release();
}

jobject OcrResultUtils::getTextBlocks(const std::vector<TextBlock> &textBlocks) {
    LocalRef<jobject> list(jniEnv, jniEnv->NewObject(listClass.get(), listConstructor));
    if (jniEnv->ExceptionCheck() || list.get() == nullptr) return nullptr;
    for (const auto &textBlock : textBlocks) {
        LocalRef<jobject> block(jniEnv, getTextBlock(textBlock));
        if (jniEnv->ExceptionCheck() || block.get() == nullptr) return nullptr;
        jniEnv->CallBooleanMethod(list.get(), listAdd, block.get());
        if (jniEnv->ExceptionCheck()) return nullptr;
    }
    return list.release();
}

jfloatArray OcrResultUtils::newJScoreArray(const std::vector<float> &scores) {
    LocalRef<jfloatArray> result(jniEnv, jniEnv->NewFloatArray(static_cast<jsize>(scores.size())));
    if (result.get() == nullptr) return nullptr;
    if (!scores.empty()) {
        jniEnv->SetFloatArrayRegion(result.get(), 0, static_cast<jsize>(scores.size()), scores.data());
        if (jniEnv->ExceptionCheck()) return nullptr;
    }
    return result.release();
}
