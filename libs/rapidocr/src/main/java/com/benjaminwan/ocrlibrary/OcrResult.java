package com.benjaminwan.ocrlibrary;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class OcrResult {

    private final double dbNetTime;
    private final ArrayList<TextBlock> textBlocks;
    private Bitmap boxImg;
    private double detectTime;
    private String strRes;

    public OcrResult(
            double dbNetTime,
            ArrayList<TextBlock> textBlocks,
            Bitmap boxImg,
            double detectTime,
            String strRes
    ) {
        this.dbNetTime = dbNetTime;
        this.textBlocks = textBlocks;
        this.boxImg = boxImg;
        this.detectTime = detectTime;
        this.strRes = strRes;
    }

    public double getDbNetTime() {
        return dbNetTime;
    }

    public ArrayList<TextBlock> getTextBlocks() {
        return textBlocks;
    }

    public Bitmap getBoxImg() {
        return boxImg;
    }

    public void setBoxImg(Bitmap boxImg) {
        this.boxImg = boxImg;
    }

    public double getDetectTime() {
        return detectTime;
    }

    public void setDetectTime(double detectTime) {
        this.detectTime = detectTime;
    }

    public String getStrRes() {
        return strRes;
    }

    public void setStrRes(String strRes) {
        this.strRes = strRes;
    }
}
