#pragma once

#include <jni.h>
#include <algorithm>
#include <unordered_set>

// Forward to a real ART JNIEnv, counting only references owned by the code under test.
// A proxy avoids modifying the VM's function table or changing device-wide CheckJNI settings.
struct CheckedJniEnv : JNIEnv {
    JNIEnv *base;
    JNINativeInterface table{};
    std::unordered_set<jobject> refs;
    jclass errorClass;
    int peak = 0;
    int finds = 0;
    int operations = 0;
    int violations = 0;
    int pinnedArrays = 0;
    int failAt;

    bool step() {
        if (base->ExceptionCheck()) {
            ++violations;
            return false;
        }
        if (++operations == failAt) {
            base->ThrowNew(errorClass, "Injected JNI failure");
            return false;
        }
        return true;
    }

    template<typename T> T keep(T reference) {
        if (reference != nullptr) {
            refs.insert(reference);
            peak = std::max(peak, static_cast<int>(refs.size()));
        }
        return reference;
    }

    explicit CheckedJniEnv(JNIEnv *real, int failure = 0) : base(real), failAt(failure) {
        functions = &table;
        errorClass = base->FindClass("java/lang/IllegalStateException");
#define PROBE() auto &p = *static_cast<CheckedJniEnv *>(env)
        table.FindClass = [](JNIEnv *env, const char *name) -> jclass {
            PROBE();
            if (!p.step()) return nullptr;
            ++p.finds;
            return p.keep(p.base->FindClass(name));
        };
        table.GetObjectClass = [](JNIEnv *env, jobject object) -> jclass {
            PROBE();
            return p.step() ? p.keep(p.base->GetObjectClass(object)) : nullptr;
        };
        table.GetMethodID = [](JNIEnv *env, jclass clazz, const char *name, const char *sig) -> jmethodID {
            PROBE();
            return p.step() ? p.base->GetMethodID(clazz, name, sig) : nullptr;
        };
        table.NewObjectV = [](JNIEnv *env, jclass clazz, jmethodID method, va_list args) -> jobject {
            PROBE();
            return p.step() ? p.keep(p.base->NewObjectV(clazz, method, args)) : nullptr;
        };
        table.CallObjectMethodV = [](JNIEnv *env, jobject object, jmethodID method, va_list args) -> jobject {
            PROBE();
            return p.step() ? p.keep(p.base->CallObjectMethodV(object, method, args)) : nullptr;
        };
        table.CallBooleanMethodV = [](JNIEnv *env, jobject object, jmethodID method, va_list args) -> jboolean {
            PROBE();
            return p.step() ? p.base->CallBooleanMethodV(object, method, args) : JNI_FALSE;
        };
        table.NewStringUTF = [](JNIEnv *env, const char *value) -> jstring {
            PROBE();
            return p.step() ? p.keep(p.base->NewStringUTF(value)) : nullptr;
        };
        table.NewFloatArray = [](JNIEnv *env, jsize length) -> jfloatArray {
            PROBE();
            return p.step() ? p.keep(p.base->NewFloatArray(length)) : nullptr;
        };
        table.SetFloatArrayRegion = [](JNIEnv *env, jfloatArray array, jsize start, jsize length, const jfloat *data) {
            PROBE();
            if (p.step()) p.base->SetFloatArrayRegion(array, start, length, data);
        };
        table.GetArrayLength = [](JNIEnv *env, jarray array) -> jsize {
            PROBE();
            return p.step() ? p.base->GetArrayLength(array) : 0;
        };
        table.GetByteArrayRegion = [](JNIEnv *env, jbyteArray array, jsize start, jsize length, jbyte *data) {
            PROBE();
            if (p.step()) p.base->GetByteArrayRegion(array, start, length, data);
        };
        // Also support the old helper so the same harness can establish a baseline.
        table.GetByteArrayElements = [](JNIEnv *env, jbyteArray array, jboolean *copy) -> jbyte * {
            PROBE();
            if (!p.step()) return nullptr;
            jbyte *data = p.base->GetByteArrayElements(array, copy);
            if (data != nullptr) ++p.pinnedArrays;
            return data;
        };
        table.ReleaseByteArrayElements = [](JNIEnv *env, jbyteArray array, jbyte *data, jint mode) {
            PROBE();
            --p.pinnedArrays;
            p.base->ReleaseByteArrayElements(array, data, mode);
        };
        table.ExceptionCheck = [](JNIEnv *env) -> jboolean {
            PROBE();
            return p.base->ExceptionCheck();
        };
        table.ThrowNew = [](JNIEnv *env, jclass clazz, const char *message) -> jint {
            PROBE();
            return p.base->ThrowNew(clazz, message);
        };
        table.DeleteLocalRef = [](JNIEnv *env, jobject reference) {
            PROBE();
            if (reference != nullptr && p.refs.erase(reference) != 1) ++p.violations;
            p.base->DeleteLocalRef(reference);
        };
#undef PROBE
    }

    ~CheckedJniEnv() { base->DeleteLocalRef(errorClass); }
};
