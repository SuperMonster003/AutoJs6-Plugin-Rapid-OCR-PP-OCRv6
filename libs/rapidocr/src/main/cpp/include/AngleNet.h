#ifndef __OCR_ANGLENET_H__
#define __OCR_ANGLENET_H__

#include "OcrStruct.h"
#include <memory>
#include "onnxruntime_cxx_api.h"
#include <opencv2/core.hpp>
#include <android/asset_manager.h>
#include <android/asset_manager_jni.h>

class AngleNet {
public:
    AngleNet();

    ~AngleNet();

    void setNumThread(int numOfThread);

    void initModel(AAssetManager *mgr, const std::string &name);

    std::vector<Angle> getAngles(std::vector<cv::Mat> &partImgs, bool doAngle, bool mostAngle);

private:
    Ort::Env ortEnv = Ort::Env(ORT_LOGGING_LEVEL_ERROR, "AngleNet");
    Ort::SessionOptions sessionOptions = Ort::SessionOptions();
    // Destroy the session before its environment and session options.
    std::unique_ptr<Ort::Session> session;
    int numThread = 0;

    std::vector<Ort::AllocatedStringPtr> inputNamesPtr;
    std::vector<Ort::AllocatedStringPtr> outputNamesPtr;

    const float meanValues[3] = {127.5, 127.5, 127.5};
    const float normValues[3] = {1.0 / 127.5, 1.0 / 127.5, 1.0 / 127.5};
    const int dstWidth = 192;
    const int dstHeight = 48;

    Angle getAngle(cv::Mat &src);
};


#endif //__OCR_ANGLENET_H__
