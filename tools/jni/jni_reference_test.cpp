#include "CheckedJniEnv.h"
#include <string>
#include <vector>

#ifdef TEST_PADDLE
#include "native.h"
#else
#include "OcrResultUtils.h"
#include "OcrUtils.h"
#endif

namespace {
jlong lastStats[6]{};

void saveStats(const CheckedJniEnv &probe) {
    lastStats[0] = probe.peak;
    lastStats[1] = probe.refs.size();
    lastStats[2] = probe.finds;
    lastStats[3] = probe.operations;
    lastStats[4] = probe.violations;
    lastStats[5] = probe.pinnedArrays;
}
}

extern "C" JNIEXPORT jlongArray JNICALL
Java_org_autojs_ocr_jni_JniStringTest_stats(JNIEnv *env, jclass) {
    jlongArray result = env->NewLongArray(6);
    if (result != nullptr) env->SetLongArrayRegion(result, 0, 6, lastStats);
    return result;
}

extern "C" JNIEXPORT jbyteArray JNICALL
Java_org_autojs_ocr_jni_JniStringTest_utf8(JNIEnv *env, jclass, jstring text, jint rounds, jint failAt) {
    CheckedJniEnv probe(env, failAt);
    std::string value;
    for (int i = 0; i < rounds; ++i) {
#ifdef TEST_PADDLE
        value = jstring_to_cpp_string(&probe, text);
#else
        value = jstringTostring(&probe, text);
#endif
        if (env->ExceptionCheck()) break;
    }
    saveStats(probe);
    if (env->ExceptionCheck()) return nullptr;
    jbyteArray bytes = env->NewByteArray(static_cast<jsize>(value.size()));
    if (bytes != nullptr && !value.empty()) {
        env->SetByteArrayRegion(bytes, 0, static_cast<jsize>(value.size()),
                reinterpret_cast<const jbyte *>(value.data()));
    }
    return bytes;
}

#ifndef TEST_PADDLE
extern "C" JNIEXPORT jobject JNICALL
Java_com_benjaminwan_ocrlibrary_JniResultTest_convert(JNIEnv *env, jclass, jint count, jint pointCount, jint failAt) {
    OcrResult data{};
    data.dbNetTime = 1.25;
    data.detectTime = 3.5;
    data.strRes = "summary";
    for (int i = 0; i < count; ++i) {
        TextBlock block{};
        for (int point = 0; point < pointCount; ++point) {
            block.boxPoint.emplace_back(i * 10 + point, i * 20 + point);
        }
        block.boxScore = 0.875f;
        block.angleIndex = 1;
        block.angleScore = 0.625f;
        block.angleTime = 2.25;
        block.text = u8"OCR 中文 " + std::to_string(i);
        if (i % 2 != 0) block.charScores = {0.25f, 0.5f, 0.75f};
        block.crnnTime = 4.5;
        block.blockTime = 6.75;
        data.textBlocks.push_back(std::move(block));
    }
    CheckedJniEnv probe(env, failAt);
    jobject result = OcrResultUtils(&probe, data, nullptr).getJObject();
    saveStats(probe);
    return result;
}
#endif
