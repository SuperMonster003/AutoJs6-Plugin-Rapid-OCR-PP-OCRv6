package org.autojs.ocr.jni;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class JniStringTest {
    private static native byte[] utf8(String text, int rounds, int failAt);
    public static native long[] stats();

    public static void require(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }

    public static void clean(long[] values, int retained, int maximum) {
        require(values[0] <= maximum, "Local reference peak: " + Arrays.toString(values));
        require(values[1] == retained, "Unexpected retained references: " + Arrays.toString(values));
        require(values[4] == 0, "Invalid JNI use: " + Arrays.toString(values));
        require(values[5] == 0, "Pinned byte arrays: " + Arrays.toString(values));
    }

    public static void main(String[] args) {
        System.load(args[0]);
        String[] inputs = {null, "", "model/path.onnx", "中文/模型\uD83D\uDE00\u0000tail", "a\u0000b"};
        for (String input : inputs) {
            byte[] expected = input == null ? new byte[0] : input.getBytes(StandardCharsets.UTF_8);
            require(Arrays.equals(expected, utf8(input, 10000, 0)), "UTF-8 bytes changed");
            clean(stats(), 0, 3);
            System.out.println("UTF8 bytes=" + expected.length + " rounds=10000 stats=" + Arrays.toString(stats()));
        }
        utf8("failure-path", 1, 0);
        int operations = (int) stats()[3];
        for (int failAt = 1; failAt <= operations; ++failAt) {
            try {
                utf8("failure-path", 1, failAt);
                throw new AssertionError("Expected failure at operation " + failAt);
            } catch (IllegalStateException expected) {
                require("Injected JNI failure".equals(expected.getMessage()), "Unexpected failure: " + expected);
                clean(stats(), 0, 3);
            }
        }
        System.out.println("PASS UTF-8 conversion; injected failure paths=" + operations);
    }
}
