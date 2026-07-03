#include <OcrUtils.h>
#include "OcrResultUtils.h"

namespace {

void throwIllegalState(JNIEnv *env, const char *message) {
    if (env->ExceptionCheck()) {
        return;
    }
    jclass clazz = env->FindClass("java/lang/IllegalStateException");
    if (clazz != NULL) {
        env->ThrowNew(clazz, message);
    }
}

}

OcrResultUtils::OcrResultUtils(JNIEnv *env, OcrResult &ocrResult, jobject boxImg) {
    jniEnv = env;
    jOcrResult = NULL;

    jclass jOcrResultClass = env->FindClass("com/benjaminwan/ocrlibrary/OcrResult");
    if (jOcrResultClass == NULL) {
        LOGE("OcrResult class is null");
        throwIllegalState(env, "OcrResult class is null");
        return;
    }

    jmethodID jOcrResultConstructor = env->GetMethodID(jOcrResultClass, "<init>",
                                                       "(DLjava/util/ArrayList;Landroid/graphics/Bitmap;DLjava/lang/String;)V");
    if (jOcrResultConstructor == NULL) {
        LOGE("OcrResult constructor is null");
        throwIllegalState(env, "OcrResult constructor is null");
        return;
    }

    jobject textBlocks = getTextBlocks(ocrResult.textBlocks);
    if (env->ExceptionCheck()) {
        return;
    }
    jdouble dbNetTime = (jdouble) ocrResult.dbNetTime;
    jdouble detectTime = (jdouble) ocrResult.detectTime;
    jstring jStrRest = jniEnv->NewStringUTF(ocrResult.strRes.c_str());
    if (env->ExceptionCheck()) {
        return;
    }

    jOcrResult = env->NewObject(jOcrResultClass, jOcrResultConstructor, dbNetTime,
                                textBlocks, boxImg, detectTime, jStrRest);
}

OcrResultUtils::~OcrResultUtils() {
    jniEnv = NULL;
}

jobject OcrResultUtils::getJObject() {
    return jOcrResult;
}

jclass OcrResultUtils::newJListClass() {
    jclass clazz = jniEnv->FindClass("java/util/ArrayList");
    if (clazz == NULL) {
        LOGE("ArrayList class is null");
        return NULL;
    }
    return clazz;
}

jmethodID OcrResultUtils::getListConstructor(jclass clazz) {
    if (clazz == NULL) {
        throwIllegalState(jniEnv, "ArrayList class is null");
        return NULL;
    }
    jmethodID constructor = jniEnv->GetMethodID(clazz, "<init>", "()V");
    if (constructor == NULL) {
        LOGE("ArrayList constructor is null");
        throwIllegalState(jniEnv, "ArrayList constructor is null");
    }
    return constructor;
}

jobject OcrResultUtils::newJPoint(cv::Point &point) {
    jclass clazz = jniEnv->FindClass("com/benjaminwan/ocrlibrary/Point");
    if (clazz == NULL) {
        LOGE("Point class is null");
        throwIllegalState(jniEnv, "Point class is null");
        return NULL;
    }
    jmethodID constructor = jniEnv->GetMethodID(clazz, "<init>", "(II)V");
    if (constructor == NULL) {
        LOGE("Point constructor is null");
        throwIllegalState(jniEnv, "Point constructor is null");
        return NULL;
    }
    jobject obj = jniEnv->NewObject(clazz, constructor, point.x, point.y);
    return obj;
}

jobject OcrResultUtils::newJBoxPoint(std::vector<cv::Point> &boxPoint) {
    jclass jListClass = newJListClass();
    jmethodID jListConstructor = getListConstructor(jListClass);
    if (jListConstructor == NULL) {
        return NULL;
    }
    jobject jList = jniEnv->NewObject(jListClass, jListConstructor);
    if (jniEnv->ExceptionCheck()) {
        return NULL;
    }
    jmethodID jListAdd = jniEnv->GetMethodID(jListClass, "add", "(Ljava/lang/Object;)Z");
    if (jListAdd == NULL) {
        LOGE("ArrayList.add method is null");
        throwIllegalState(jniEnv, "ArrayList.add method is null");
        return NULL;
    }

    for (auto point : boxPoint) {
        jobject jPoint = newJPoint(point);
        if (jPoint == NULL || jniEnv->ExceptionCheck()) {
            return NULL;
        }
        jniEnv->CallBooleanMethod(jList, jListAdd, jPoint);
        if (jniEnv->ExceptionCheck()) {
            return NULL;
        }
    }
    return jList;
}

jobject OcrResultUtils::getTextBlock(TextBlock &textBlock) {
    jobject jBoxPint = newJBoxPoint(textBlock.boxPoint);
    if (jBoxPint == NULL || jniEnv->ExceptionCheck()) {
        return NULL;
    }
    jfloat jBoxScore = (jfloat) textBlock.boxScore;
    jfloat jAngleScore = (jfloat) textBlock.angleScore;
    jdouble jAngleTime = (jdouble) textBlock.angleTime;
    jstring jText = jniEnv->NewStringUTF(textBlock.text.c_str());
    if (jniEnv->ExceptionCheck()) {
        return NULL;
    }
    jobject jCharScores = newJScoreArray(textBlock.charScores);
    if (jCharScores == NULL || jniEnv->ExceptionCheck()) {
        return NULL;
    }
    jdouble jCrnnTime = (jdouble) textBlock.crnnTime;
    jdouble jBlockTime = (jdouble) textBlock.blockTime;
    jclass clazz = jniEnv->FindClass("com/benjaminwan/ocrlibrary/TextBlock");
    if (clazz == NULL) {
        LOGE("TextBlock class is null");
        throwIllegalState(jniEnv, "TextBlock class is null");
        return NULL;
    }
    jmethodID constructor = jniEnv->GetMethodID(clazz, "<init>",
                                                "(Ljava/util/ArrayList;FIFDLjava/lang/String;[FDD)V");
    if (constructor == NULL) {
        LOGE("TextBlock constructor is null");
        throwIllegalState(jniEnv, "TextBlock constructor is null");
        return NULL;
    }
    jobject obj = jniEnv->NewObject(clazz, constructor, jBoxPint, jBoxScore, textBlock.angleIndex,
                                    jAngleScore, jAngleTime, jText, jCharScores, jCrnnTime,
                                    jBlockTime);
    return obj;
}

jobject OcrResultUtils::getTextBlocks(std::vector<TextBlock> &textBlocks) {
    jclass jListClass = newJListClass();
    jmethodID jListConstructor = getListConstructor(jListClass);
    if (jListConstructor == NULL) {
        return NULL;
    }
    jobject jList = jniEnv->NewObject(jListClass, jListConstructor);
    if (jniEnv->ExceptionCheck()) {
        return NULL;
    }
    jmethodID jListAdd = jniEnv->GetMethodID(jListClass, "add", "(Ljava/lang/Object;)Z");
    if (jListAdd == NULL) {
        LOGE("ArrayList.add method is null");
        throwIllegalState(jniEnv, "ArrayList.add method is null");
        return NULL;
    }

    for (int i = 0; i < textBlocks.size(); ++i) {
        auto textBlock = textBlocks[i];
        jobject jTextBlock = getTextBlock(textBlock);
        if (jTextBlock == NULL || jniEnv->ExceptionCheck()) {
            return NULL;
        }
        jniEnv->CallBooleanMethod(jList, jListAdd, jTextBlock);
        if (jniEnv->ExceptionCheck()) {
            return NULL;
        }
    }
    return jList;
}

jfloatArray OcrResultUtils::newJScoreArray(std::vector<float> &scores) {
    jfloatArray jScores = jniEnv->NewFloatArray(scores.size());
    if (jScores == NULL || jniEnv->ExceptionCheck()) {
        return NULL;
    }
    jniEnv->SetFloatArrayRegion(jScores, 0, scores.size(), (jfloat *) scores.data());
    return jScores;
}
