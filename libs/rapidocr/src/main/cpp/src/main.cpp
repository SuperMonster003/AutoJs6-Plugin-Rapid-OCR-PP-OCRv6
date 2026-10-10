#include "OcrResultUtils.h"
#include "BitmapUtils.h"
#include "OcrLite.h"
#include "OcrUtils.h"
#include "JniUtils.h"
#include <exception>
#include <mutex>
#include <string>

// All Java wrappers share one native engine, even across service instances.
static std::mutex ocrMutex;
static OcrLite *ocrLite;

// An uncaught C++ exception aborts the whole plugin process; report it to the caller instead.
static void throwJavaException(JNIEnv *env, const char *className, const std::string &message) {
    if (env->ExceptionCheck()) return;
    ocr::jni::LocalRef<jclass> type(env, env->FindClass(className));
    if (type.get() != nullptr) env->ThrowNew(type.get(), message.c_str());
}

static void throwNativeFailure(JNIEnv *env, const char *stage, const char *detail) {
    LOGE("%s failed: %s", stage, detail);
    throwJavaException(env, "java/lang/IllegalStateException",
                       std::string("Rapid OCR ") + stage + " failed: " + detail);
}

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    ocrLite = new OcrLite();
    return JNI_VERSION_1_4;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    std::lock_guard<std::mutex> lock(ocrMutex);
    LOGI("Goodbye OcrLite!");
    delete ocrLite;
    ocrLite = nullptr;
}

extern "C" JNIEXPORT jboolean JNICALL
Java_com_benjaminwan_ocrlibrary_OcrEngine_init(JNIEnv *env, jobject thiz, jobject assetManager,
                                               jint numThread, jstring detName, jstring clsName,
                                               jstring recName, jstring keysName) {
    std::lock_guard<std::mutex> lock(ocrMutex);
    std::string modelDetName = jstringTostring(env, detName);
    if (env->ExceptionCheck()) return JNI_FALSE;
    std::string modelClsName = jstringTostring(env, clsName);
    if (env->ExceptionCheck()) return JNI_FALSE;
    std::string modelRecName = jstringTostring(env, recName);
    if (env->ExceptionCheck()) return JNI_FALSE;
    std::string modelKeysName = jstringTostring(env, keysName);
    if (env->ExceptionCheck()) return JNI_FALSE;
    try {
        ocrLite->init(env, assetManager, numThread, modelDetName, modelClsName, modelRecName, modelKeysName);
    } catch (const std::exception &e) {
        throwNativeFailure(env, "model initialization", e.what());
        return JNI_FALSE;
    } catch (...) {
        throwNativeFailure(env, "model initialization", "unknown native exception");
        return JNI_FALSE;
    }
    //ocrLite->initLogger(false);
    return JNI_TRUE;
}

cv::Mat makePadding(cv::Mat &src, const int padding) {
    if (padding <= 0) return src;
    cv::Scalar paddingScalar = {255, 255, 255};
    cv::Mat paddingSrc;
    cv::copyMakeBorder(src, paddingSrc, padding, padding, padding, padding, cv::BORDER_ISOLATED,
                       paddingScalar);
    return paddingSrc;
}

// Reads the input bitmap; returns false with a pending Java exception when it cannot be read.
static bool readInputBitmap(JNIEnv *env, jobject input, cv::Mat &imgBGR) {
    cv::Mat imgRGBA;
    bitmapToMat(env, input, imgRGBA);
    if (env->ExceptionCheck() || imgRGBA.empty()) {
        // bitmapToMat reports every failure as a generic java.lang.Exception.
        env->ExceptionClear();
        throwJavaException(env, "java/lang/IllegalArgumentException",
                           "Rapid OCR input bitmap must be readable RGBA_8888 or RGB_565");
        return false;
    }
    cv::cvtColor(imgRGBA, imgBGR, cv::COLOR_RGBA2BGR);
    return true;
}

static jobject detectLocked(JNIEnv *env, jobject input, jobject output,
                            jint padding, jint maxSideLen, jfloat boxScoreThresh, jfloat boxThresh,
                            jfloat unClipRatio, jboolean doAngle, jboolean mostAngle) {
    Logger("padding(%d),maxSideLen(%d),boxScoreThresh(%f),boxThresh(%f),unClipRatio(%f),doAngle(%d),mostAngle(%d)",
           padding, maxSideLen, boxScoreThresh, boxThresh, unClipRatio, doAngle, mostAngle);
    cv::Mat imgBGR, imgOut;
    if (!readInputBitmap(env, input, imgBGR)) return nullptr;
    int originMaxSide = (std::max)(imgBGR.cols, imgBGR.rows);
    int resize;
    if (maxSideLen <= 0 || maxSideLen > originMaxSide) {
        resize = originMaxSide;
    } else {
        resize = maxSideLen;
    }
    resize += 2*padding;
    cv::Rect paddingRect(padding, padding, imgBGR.cols, imgBGR.rows);
    cv::Mat paddingSrc = makePadding(imgBGR, padding);
    // 按比例缩小图像, 减少文字分割时间
    ScaleParam s = getScaleParam(paddingSrc, resize); // 例: 按长或宽缩放 src.cols=不缩放, src.cols/2=长度缩小一半
    OcrResult ocrResult = ocrLite->detect(paddingSrc, paddingRect, s, boxScoreThresh, boxThresh,
                                          unClipRatio, doAngle, mostAngle);

    cv::cvtColor(ocrResult.boxImg, imgOut, cv::COLOR_BGR2RGBA);
    matToBitmap(env, imgOut, output);
    if (env->ExceptionCheck()) return nullptr;

    return OcrResultUtils(env, ocrResult, output).getJObject();
}

extern "C"
JNIEXPORT jobject JNICALL
Java_com_benjaminwan_ocrlibrary_OcrEngine_detect(JNIEnv *env, jobject thiz, jobject input, jobject output,
                                                 jint padding, jint maxSideLen, jfloat boxScoreThresh, jfloat boxThresh,
                                                 jfloat unClipRatio, jboolean doAngle, jboolean mostAngle) {
    std::lock_guard<std::mutex> lock(ocrMutex);
    try {
        return detectLocked(env, input, output, padding, maxSideLen, boxScoreThresh, boxThresh,
                            unClipRatio, doAngle, mostAngle);
    } catch (const std::exception &e) {
        throwNativeFailure(env, "inference", e.what());
    } catch (...) {
        throwNativeFailure(env, "inference", "unknown native exception");
    }
    return nullptr;
}

static jdouble benchmarkLocked(JNIEnv *env, jobject input, jint loop) {
    int padding = 50;
    int paddingRect= 0;
    float boxScoreThresh = 0.6;
    float boxThresh = 0.3;
    float unClipRatio = 2.0;
    bool doAngle = true;
    bool mostAngle = true;
    LOGI("padding(%d),paddingRect(%d),boxScoreThresh(%f),boxThresh(%f),unClipRatio(%f),doAngle(%d),mostAngle(%d)",
         padding, paddingRect, boxScoreThresh, boxThresh, unClipRatio, doAngle, mostAngle);
    cv::Mat imgBGR;
    if (!readInputBitmap(env, input, imgBGR)) return -1;
    cv::Rect originRect(padding, padding, imgBGR.cols, imgBGR.rows);
    cv::Mat src = makePadding(imgBGR, padding);
    // 按比例缩小图像, 减少文字分割时间
    ScaleParam s = getScaleParam(src, src.cols); // 例: 按长或宽缩放 src.cols=不缩放, src.cols/2=长度缩小一半

    LOGI("=====warmup=====");
    OcrResult result = ocrLite->detect(src, originRect, s, boxScoreThresh, boxThresh,
                                       unClipRatio, doAngle, mostAngle);
    LOGI("dbNetTime(%f) detectTime(%f)\n", result.dbNetTime, result.detectTime);
    double dbTime = 0.0f;
    double detectTime = 0.0f;
    int loopCount = loop;
    for (int i = 0; i < loopCount; ++i) {
        LOGI("=====loop:%d=====", i + 1);
        OcrResult ocrResult = ocrLite->detect(src, originRect, s, boxScoreThresh, boxThresh,
                                              unClipRatio, doAngle, mostAngle);
        LOGI("dbNetTime(%f) detectTime(%f)\n", ocrResult.dbNetTime, ocrResult.detectTime);
        dbTime += ocrResult.dbNetTime;
        detectTime += ocrResult.detectTime;
    }
    LOGI("=====result=====\n");
    double averageTime = detectTime / loopCount;
    LOGI("average dbNetTime=%fms, average detectTime=%fms\n", dbTime / loopCount,
         averageTime);
    return (jdouble) averageTime;
}

extern "C" JNIEXPORT jdouble JNICALL
Java_com_benjaminwan_ocrlibrary_OcrEngine_benchmark(JNIEnv *env, jobject thiz, jobject input,
                                                    jint loop) {
    std::lock_guard<std::mutex> lock(ocrMutex);
    try {
        return benchmarkLocked(env, input, loop);
    } catch (const std::exception &e) {
        throwNativeFailure(env, "benchmark", e.what());
    } catch (...) {
        throwNativeFailure(env, "benchmark", "unknown native exception");
    }
    return -1;
}
