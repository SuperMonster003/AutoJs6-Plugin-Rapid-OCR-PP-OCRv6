# Third-party notices

AutoJs6 Rapid OCR PP-OCRv6 Small plugin. The plugin's own source code is licensed under the Mozilla Public License 2.0 (repository `LICENSE`). This APK also redistributes the third-party works below, each under its own license. The license texts are packaged next to this file in `assets/licenses/`.

## OCR models (Apache License 2.0)

The model files are packaged from RapidOCR v3.10.0 (commit `bd941dae09da793e76c6ba15c7aaf40f8424232a`, ModelScope revision `v3.10.0`) without modification. Each file is identified by its SHA-256 hash.

| File | Upstream model | SHA-256 |
|---|---|---|
| `PP-OCRv6_det_small.onnx` | `PP-OCRv6_small_det` | `090f04abcd9d9a7498bc4ebf677e4cb9bdce1fe4197ddb7e529f1ef44e1ff94f` |
| `PP-OCRv6_rec_small.onnx` | `PP-OCRv6_small_rec` | `6f327246b50388f3c176ae304bd95767ea6dc0c9ae92153ef8cbe210b3c14884` |
| `ch_ppocr_mobile_v2.0_cls_mobile.onnx` | `ch_ppocr_mobile_v2.0_cls` | `e47acedf663230f8863ff1ab0e64dd2d82b838fceb5957146dab185a89d6215c` |
| `ppocrv6_dict.txt` | `PP-OCRv6_small_rec` character dictionary | `b5f2bfe2bdd9448429e3e82b51c789775d9b42f2403d082b00662eb77e401c5d` |

Copyright in the upstream model weights is held by Baidu and/or the applicable PaddleOCR rights holders. The models are licensed under the Apache License, Version 2.0 (`LICENSE-Apache-2.0.txt`). The ONNX files are RapidOCR conversions; RapidOCR states for these converted files:

> Converted from an official PaddleOCR model to the indicated inference format and packaged for use with RapidOCR. The upstream model weights are copyright Baidu and/or the applicable PaddleOCR rights holders and are licensed under the Apache License, Version 2.0.

- RapidOCR model license notice: <https://github.com/RapidAI/RapidOCR/blob/v3.10.0/python/MODEL_LICENSES.md>
- RapidOCR: <https://github.com/RapidAI/RapidOCR>, Copyright (c) 2021 RapidOCR Authors, Apache-2.0
- PaddleOCR: <https://github.com/PaddlePaddle/PaddleOCR>, Apache-2.0
- PP-OCRv6 model cards: <https://huggingface.co/PaddlePaddle/PP-OCRv6_small_det_onnx>, <https://huggingface.co/PaddlePaddle/PP-OCRv6_small_rec_onnx>
- Legacy angle classifier: <https://github.com/PaddlePaddle/PaddleOCR/blob/main/docs/version2.x/ppocr/model_list.en.md>

No upstream `NOTICE` file was identified for these artifacts. The names Baidu, PaddlePaddle, PaddleOCR and RapidOCR are used only to describe the origin of the files and do not imply endorsement, sponsorship or affiliation.

## OCR engine source (Apache License 2.0)

The C++ detection, angle classification and recognition pipeline (`libRapidOcr.so`) and the `com.benjaminwan.ocrlibrary` Java wrapper are derived from RapidOcrAndroidOnnx by benjaminwan and RapidAI: <https://github.com/RapidAI/RapidOcrAndroidOnnx>. The AutoJs6 plugin maintainers modified these files, including the ONNX Runtime 1.21 API, 16 KB page alignment, JNI reference handling, input validation and native exception reporting.

## ONNX Runtime 1.21.1 (MIT License)

`libonnxruntime.so` comes from the Maven artifact `com.microsoft.onnxruntime:onnxruntime-android:1.21.1`. Copyright (c) Microsoft Corporation. License: `onnxruntime-1.21.1/LICENSE`. ONNX Runtime's own third-party notices: <https://github.com/microsoft/onnxruntime/blob/v1.21.1/ThirdPartyNotices.txt>

## OpenCV 4.8.0 (Apache License 2.0) and statically linked components

`libopencv_java4.so` is byte-identical to the AutoJs6 OpenCV plugin payload recorded in `libs/opencv-native-4.8.0.provenance.json`. Its license and attribution texts are in `opencv-4.8.0/`. They cover OpenCV and the statically linked ADE, Android cpufeatures, Carotene (ARM ABIs), FlatBuffers, Intel IPP and IPP IW (x86 ABIs), Intel ITT, libjpeg-turbo, OpenEXR, OpenJPEG, libpng, libtiff, libwebp, Protocol Buffers, quirc, SoftFloat, Intel TBB 2020.2 and the Torch importer.

## Clipper 6.4.2 (Boost Software License 1.0)

Polygon offsetting in `libRapidOcr.so` uses Clipper 6.4.2, Copyright Angus Johnson 2010-2017. License: `clipper-6.4.2/LICENSE_1_0.txt`.

## LLVM libc++ (Apache License 2.0 with LLVM Exceptions)

`libc++_shared.so` is the C++ runtime from Android NDK 28.2.13676358. License: <https://github.com/llvm/llvm-project/blob/main/libcxx/LICENSE.TXT>

## Android and Kotlin libraries (Apache License 2.0)

AndroidX libraries (Copyright The Android Open Source Project), the Kotlin standard library and JetBrains annotations (Copyright JetBrains s.r.o.) are licensed under the Apache License, Version 2.0 (`LICENSE-Apache-2.0.txt`). The AutoJs6 plugin API libraries belong to the AutoJs6 project and are licensed under the Mozilla Public License 2.0.
