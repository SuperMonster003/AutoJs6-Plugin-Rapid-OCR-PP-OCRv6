package com.benjaminwan.ocrlibrary;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

public class OcrEngine {

    public static final int numThread = 4;

    private int padding = 50;
    private float boxScoreThresh = 0.5f;
    private float boxThresh = 0.3f;
    private float unClipRatio = 1.6f;
    private boolean doAngle = true;
    private boolean mostAngle = true;

    public OcrEngine(Context context) {
        System.loadLibrary("RapidOcr");
        boolean ret = init(
                context.getAssets(),
                numThread,
                "models/PP-OCRv6_det_small.onnx",
                "models/ch_ppocr_mobile_v2.0_cls_mobile.onnx",
                "models/PP-OCRv6_rec_small.onnx",
                "models/ppocrv6_dict.txt"
        );
        if (!ret) {
            throw new IllegalArgumentException();
        }
    }

    public OcrResult detect(Bitmap input, Bitmap output, int maxSideLen) {
        return detect(input, output, padding, maxSideLen, boxScoreThresh, boxThresh, unClipRatio, doAngle, mostAngle);
    }

    public native boolean init(
            AssetManager assetManager,
            int numThread,
            String detName,
            String clsName,
            String recName,
            String keysName
    );

    public native OcrResult detect(
            Bitmap input,
            Bitmap output,
            int padding,
            int maxSideLen,
            float boxScoreThresh,
            float boxThresh,
            float unClipRatio,
            boolean doAngle,
            boolean mostAngle
    );

    public native double benchmark(Bitmap input, int loop);

    public int getPadding() {
        return padding;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public float getBoxScoreThresh() {
        return boxScoreThresh;
    }

    public void setBoxScoreThresh(float boxScoreThresh) {
        this.boxScoreThresh = boxScoreThresh;
    }

    public float getBoxThresh() {
        return boxThresh;
    }

    public void setBoxThresh(float boxThresh) {
        this.boxThresh = boxThresh;
    }

    public float getUnClipRatio() {
        return unClipRatio;
    }

    public void setUnClipRatio(float unClipRatio) {
        this.unClipRatio = unClipRatio;
    }

    public boolean getDoAngle() {
        return doAngle;
    }

    public void setDoAngle(boolean doAngle) {
        this.doAngle = doAngle;
    }

    public boolean getMostAngle() {
        return mostAngle;
    }

    public void setMostAngle(boolean mostAngle) {
        this.mostAngle = mostAngle;
    }
}
