package com.benjaminwan.ocrlibrary;

import java.util.Arrays;
import org.autojs.ocr.jni.JniStringTest;
import static org.autojs.ocr.jni.JniStringTest.require;

public final class JniResultTest {
    private static native OcrResult convert(int blocks, int points, int failAt);

    private static void verify(OcrResult result, int blocks, int points) {
        require(result != null, "Missing result");
        System.gc();
        require(result.getTextBlocks().size() == blocks, "Block count changed");
        require(result.getDbNetTime() == 1.25 && result.getDetectTime() == 3.5, "Timing changed");
        require("summary".equals(result.getStrRes()) && result.getBoxImg() == null, "Result metadata changed");
        for (int i = 0; i < blocks; ++i) {
            TextBlock block = result.getTextBlocks().get(i);
            require(("OCR 中文 " + i).equals(block.getText()), "Text changed at " + i);
            require(block.getBoxPoint().size() == points, "Point count changed");
            for (int p = 0; p < points; ++p) {
                Point point = block.getBoxPoint().get(p);
                require(point.getX() == i * 10 + p && point.getY() == i * 20 + p, "Coordinates changed");
            }
            float[] expectedScores = i % 2 == 0 ? new float[0] : new float[]{0.25f, 0.5f, 0.75f};
            require(Arrays.equals(expectedScores, block.getCharScores()), "Character scores changed");
            require(block.getBoxScore() == 0.875f && block.getAngleIndex() == 1
                    && block.getAngleScore() == 0.625f && block.getAngleTime() == 2.25
                    && block.getCrnnTime() == 4.5 && block.getBlockTime() == 6.75, "Block metadata changed");
        }
    }

    public static void main(String[] args) {
        if (args.length > 1 && "baseline".equals(args[1])) {
            System.load(args[0]);
            verify(convert(100, 4, 0), 100, 4);
            System.out.println("BASELINE blocks=100 stats=" + Arrays.toString(JniStringTest.stats()));
            require(JniStringTest.stats()[0] > 512, "Old implementation did not reproduce reference growth");
            return;
        }
        JniStringTest.main(args);
        for (int blocks : new int[]{0, 1, 100, 5000}) {
            verify(convert(blocks, 4, 0), blocks, 4);
            JniStringTest.clean(JniStringTest.stats(), 1, 16);
            require(JniStringTest.stats()[2] == 4, "Class lookups were not cached per conversion");
            System.out.println("RESULT blocks=" + blocks + " stats=" + Arrays.toString(JniStringTest.stats()));
        }
        verify(convert(1, 1024, 0), 1, 1024);
        JniStringTest.clean(JniStringTest.stats(), 1, 16);
        verify(convert(1, 0, 0), 1, 0);
        JniStringTest.clean(JniStringTest.stats(), 1, 16);
        convert(2, 4, 0);
        int operations = (int) JniStringTest.stats()[3];
        for (int failAt = 1; failAt <= operations; ++failAt) {
            try {
                convert(2, 4, failAt);
                throw new AssertionError("Expected failure at operation " + failAt);
            } catch (IllegalStateException expected) {
                require("Injected JNI failure".equals(expected.getMessage()), "Unexpected failure: " + expected);
                JniStringTest.clean(JniStringTest.stats(), 0, 16);
            }
        }
        System.out.println("PASS result conversion; injected failure paths=" + operations);
    }
}
