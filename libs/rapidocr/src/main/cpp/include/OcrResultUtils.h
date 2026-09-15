#ifndef __OCR_RESULT_UTILS_H__
#define __OCR_RESULT_UTILS_H__

#include "JniUtils.h"
#include "OcrStruct.h"

class OcrResultUtils {
public:
    OcrResultUtils(JNIEnv *env, const OcrResult &ocrResult, jobject boxImg);

    jobject getJObject() const;

private:
    JNIEnv *jniEnv;
    // Ownership is transferred to the JNI caller, which returns this reference to Java.
    jobject jOcrResult = nullptr;

    // Cache for this conversion only, without extending the class loader's lifetime.
    ocr::jni::LocalRef<jclass> listClass;
    ocr::jni::LocalRef<jclass> pointClass;
    ocr::jni::LocalRef<jclass> textBlockClass;
    jmethodID listConstructor = nullptr;
    jmethodID listAdd = nullptr;
    jmethodID pointConstructor = nullptr;
    jmethodID textBlockConstructor = nullptr;

    bool initJavaTypes();
    jobject getTextBlock(const TextBlock &textBlock);
    jobject getTextBlocks(const std::vector<TextBlock> &textBlocks);
    jobject newJBoxPoint(const std::vector<cv::Point> &boxPoint);
    jfloatArray newJScoreArray(const std::vector<float> &scores);
};

#endif // __OCR_RESULT_UTILS_H__
