#include "CrnnNet.h"
#include "OcrUtils.h"
#include <numeric>
#include <cstdlib>
#include <sstream>
#include <stdexcept>
#include <string>

CrnnNet::CrnnNet() {}

CrnnNet::~CrnnNet() = default;

void CrnnNet::setNumThread(int numOfThread) {
    numThread = numOfThread;
    //===session options===
    // Sets the number of threads used to parallelize the execution within nodes
    // A value of 0 means ORT will pick a default
    sessionOptions.SetIntraOpNumThreads(numThread);
    //set OMP_NUM_THREADS=16

    // Sets the number of threads used to parallelize the execution of the graph (across nodes)
    // If sequential execution is enabled this value is ignored
    // A value of 0 means ORT will pick a default
    sessionOptions.SetInterOpNumThreads(numThread);

    // Sets graph optimization level
    // ORT_DISABLE_ALL -> To disable all optimizations
    // ORT_ENABLE_BASIC -> To enable basic optimizations (Such as redundant node removals)
    // ORT_ENABLE_EXTENDED -> To enable extended optimizations (Includes level 1 + more complex optimizations like node fusions)
    // ORT_ENABLE_ALL -> To Enable All possible opitmizations
    sessionOptions.SetGraphOptimizationLevel(GraphOptimizationLevel::ORT_ENABLE_EXTENDED);
}

char *readKeysFromAssets(AAssetManager *mgr, const std::string &keysName) {
    //LOGI("readKeysFromAssets start...");
    if (mgr == NULL) {
        LOGE(" %s", "AAssetManager==NULL");
        return NULL;
    }
    char *buffer;
    /*获取文件名并打开*/
    AAsset *asset = AAssetManager_open(mgr, keysName.c_str(), AASSET_MODE_UNKNOWN);
    if (asset == NULL) {
        LOGE(" %s", "asset==NULL");
        return NULL;
    }
    /*获取文件大小*/
    off_t bufferSize = AAsset_getLength(asset);
    //LOGI("file size : %d", bufferSize);
    buffer = (char *) malloc(bufferSize + 1);
    if (buffer == NULL) {
        AAsset_close(asset);
        LOGE(" %s", "cannot allocate the dictionary buffer");
        return NULL;
    }
    int numBytesRead = AAsset_read(asset, buffer, bufferSize);
    /*关闭文件*/
    AAsset_close(asset);
    if (numBytesRead != bufferSize) {
        LOGE("dictionary read %d of %ld bytes", numBytesRead, (long) bufferSize);
        free(buffer);
        return NULL;
    }
    buffer[bufferSize] = 0;
    return buffer;
}

void CrnnNet::initModel(AAssetManager *mgr, const std::string &name, const std::string &keysName) {
    int dbModelDataLength = 0;
    std::unique_ptr<void, decltype(&std::free)> modelData(
            getModelDataFromAssets(mgr, name.c_str(), dbModelDataLength), &std::free);
    if (modelData == nullptr || dbModelDataLength <= 0) {
        throw std::runtime_error("cannot read model asset: " + name);
    }
    auto newSession = std::make_unique<Ort::Session>(ortEnv, modelData.get(), dbModelDataLength,
                                                     sessionOptions);
    auto newInputNames = getInputNames(newSession.get());
    auto newOutputNames = getOutputNames(newSession.get());

    std::unique_ptr<char, decltype(&std::free)> buffer(readKeysFromAssets(mgr, keysName), &std::free);
    if (buffer == nullptr) {
        throw std::runtime_error("cannot read dictionary asset: " + keysName);
    }
    // The CTC blank comes first and the space class last.
    std::vector<std::string> newKeys{"#"};
    std::istringstream inStr(buffer.get());
    std::string line;
    while (getline(inStr, line)) {
        newKeys.emplace_back(line);
    }
    newKeys.emplace_back(" ");
    if (newKeys.size() <= 2) {
        throw std::runtime_error("dictionary asset is empty: " + keysName);
    }
    // A mismatched dictionary silently shifts every decoded character.
    std::vector<int64_t> outputShape = newSession->GetOutputTypeInfo(0).GetTensorTypeAndShapeInfo().GetShape();
    if (!outputShape.empty() && outputShape.back() > 0 && outputShape.back() != (int64_t) newKeys.size()) {
        throw std::runtime_error("dictionary " + keysName + " defines " + std::to_string(newKeys.size()) +
                                 " classes, but model " + name + " outputs " + std::to_string(outputShape.back()));
    }

    // Keep the previous model and dictionary usable until the replacement is complete.
    inputNamesPtr = std::move(newInputNames);
    outputNamesPtr = std::move(newOutputNames);
    keys = std::move(newKeys);
    session = std::move(newSession);
    LOGI("keys size(%zu)", keys.size());
}

template<class ForwardIterator>
inline static size_t argmax(ForwardIterator first, ForwardIterator last) {
    return std::distance(first, std::max_element(first, last));
}

TextLine CrnnNet::scoreToTextLine(const std::vector<float> &outputData, int h, int w) {
    auto keySize = keys.size();
    auto dataSize = outputData.size();
    std::string strRes;
    std::vector<float> scores;
    int lastIndex = 0;
    int maxIndex;
    float maxValue;

    for (int i = 0; i < h; i++) {
        int start = i * w;
        int stop = (i + 1) * w;
        if (stop > dataSize - 1) {
            stop = (i + 1) * w - 1;
        }
        maxIndex = int(argmax(&outputData[start], &outputData[stop]));
        maxValue = float(*std::max_element(&outputData[start], &outputData[stop]));

        if (maxIndex > 0 && maxIndex < keySize && (!(i > 0 && maxIndex == lastIndex))) {
            scores.emplace_back(maxValue);
            strRes.append(keys[maxIndex]);
        }
        lastIndex = maxIndex;
    }
    return {strRes, scores};
}

TextLine CrnnNet::getTextLine(cv::Mat &src) {
    float scale = (float) dstHeight / (float) src.rows;
    int dstWidth = int((float) src.cols * scale);

    cv::Mat srcResize;
    resize(src, srcResize, cv::Size(dstWidth, dstHeight));

    std::vector<float> inputTensorValues = substractMeanNormalize(srcResize, meanValues,
                                                                  normValues);

    std::array<int64_t, 4> inputShape{1, srcResize.channels(), srcResize.rows, srcResize.cols};

    auto memoryInfo = Ort::MemoryInfo::CreateCpu(OrtDeviceAllocator, OrtMemTypeCPU);

    Ort::Value inputTensor = Ort::Value::CreateTensor<float>(memoryInfo, inputTensorValues.data(),
                                                             inputTensorValues.size(),
                                                             inputShape.data(),
                                                             inputShape.size());
    assert(inputTensor.IsTensor());
    std::vector<const char *> inputNames = {inputNamesPtr.data()->get()};
    std::vector<const char *> outputNames = {outputNamesPtr.data()->get()};
    auto outputTensor = session->Run(Ort::RunOptions{nullptr}, inputNames.data(), &inputTensor,
                                     inputNames.size(), outputNames.data(), outputNames.size());

    assert(outputTensor.size() == 1 && outputTensor.front().IsTensor());

    std::vector<int64_t> outputShape = outputTensor[0].GetTensorTypeAndShapeInfo().GetShape();

    int64_t outputCount = std::accumulate(outputShape.begin(), outputShape.end(), 1,
                                          std::multiplies<int64_t>());

    float *floatArray = outputTensor.front().GetTensorMutableData<float>();
    std::vector<float> outputData(floatArray, floatArray + outputCount);
    return scoreToTextLine(outputData, outputShape[1], outputShape[2]);
}

std::vector<TextLine> CrnnNet::getTextLines(std::vector<cv::Mat> &partImg) {
    int size = partImg.size();
    std::vector<TextLine> textLines(size);
    for (int i = 0; i < size; ++i) {
        //getTextLine
        double startCrnnTime = getCurrentTime();
        TextLine textLine = getTextLine(partImg[i]);
        double endCrnnTime = getCurrentTime();
        textLine.time = endCrnnTime - startCrnnTime;
        textLines[i] = textLine;
    }
    return textLines;
}