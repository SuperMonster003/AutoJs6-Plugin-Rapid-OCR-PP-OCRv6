#pragma once

#include <jni.h>
#include <string>

namespace ocr {
namespace jni {

// JNI references outlive C++ scopes unless explicitly released, even on exceptions.
template<typename T>
class LocalRef {
public:
    explicit LocalRef(JNIEnv *env, T reference = nullptr) : env_(env), reference_(reference) {}

    ~LocalRef() { reset(); }

    LocalRef(const LocalRef &) = delete;
    LocalRef &operator=(const LocalRef &) = delete;

    T get() const { return reference_; }

    T release() {
        T reference = reference_;
        reference_ = nullptr;
        return reference;
    }

    void reset(T reference = nullptr) {
        if (reference_ != nullptr) {
            env_->DeleteLocalRef(reference_);
        }
        reference_ = reference;
    }

private:
    JNIEnv *env_;
    T reference_;
};

inline std::string toUtf8(JNIEnv *env, jstring value) {
    if (value == nullptr || env->ExceptionCheck()) {
        return {};
    }
    LocalRef<jclass> stringClass(env, env->GetObjectClass(value));
    if (stringClass.get() == nullptr) return {};
    jmethodID getBytes = env->GetMethodID(stringClass.get(), "getBytes", "(Ljava/lang/String;)[B");
    if (getBytes == nullptr) return {};
    // Keep standard UTF-8, including supplementary characters and embedded NULs.
    // GetStringUTFChars would change this to JNI's modified UTF-8 encoding.
    LocalRef<jstring> encoding(env, env->NewStringUTF("UTF-8"));
    if (encoding.get() == nullptr) return {};
    LocalRef<jbyteArray> bytes(env, static_cast<jbyteArray>(
            env->CallObjectMethod(value, getBytes, encoding.get())));
    if (env->ExceptionCheck() || bytes.get() == nullptr) return {};

    const jsize length = env->GetArrayLength(bytes.get());
    std::string result(static_cast<size_t>(length), '\0');
    if (length > 0) {
        // Copy straight into owned storage: no malloc buffer or pinned Java array.
        env->GetByteArrayRegion(bytes.get(), 0, length, reinterpret_cast<jbyte *>(&result[0]));
        if (env->ExceptionCheck()) return {};
    }
    return result;
}

} // namespace jni
} // namespace ocr
