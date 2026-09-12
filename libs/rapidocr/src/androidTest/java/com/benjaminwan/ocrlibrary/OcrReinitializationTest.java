package com.benjaminwan.ocrlibrary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class OcrReinitializationTest {

    private Context context;
    private Bitmap image;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        System.loadLibrary("onnxruntime");
        image = Bitmap.createBitmap(640, 160, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        canvas.drawText("Hello AutoJs6", 30, 90, paint);
    }

    @After
    public void tearDown() {
        image.recycle();
    }

    private OcrEngine newEngine() {
        OcrEngine engine = new OcrEngine(context);
        engine.setDoAngle(false);
        engine.setMostAngle(false);
        return engine;
    }

    private String recognize(OcrEngine engine) {
        Bitmap output = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        try {
            List<TextBlock> blocks = engine.detect(image, output, 1024).getTextBlocks();
            assertFalse("OCR should detect the test text", blocks.isEmpty());
            StringBuilder text = new StringBuilder();
            for (TextBlock block : blocks) {
                assertEquals(4, block.getBoxPoint().size());
                if (text.length() > 0) text.append(' ');
                text.append(block.getText());
            }
            return text.toString();
        } finally {
            output.recycle();
        }
    }

    @Test
    public void repeatedInitializationKeepsTextAndBounds() {
        OcrEngine first = newEngine();
        String expected = recognize(first);
        assertTrue("Initial recognition: " + expected, expected.contains("Hello"));
        for (int round = 1; round <= 8; round++) {
            OcrEngine replacement = newEngine();
            assertEquals("New wrapper after initialization " + round, expected, recognize(replacement));
            assertEquals("Existing wrapper after initialization " + round, expected, recognize(first));
            Log.i("RapidOcrReinit", "round=" + round + ", nativeHeap=" + Debug.getNativeHeapAllocatedSize());
        }
    }

    @Test
    public void initializationAndRecognitionCanOverlapAcrossWrappers() throws Exception {
        OcrEngine first = newEngine();
        String expected = recognize(first);
        assertTrue(expected.contains("Hello"));
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService workers = Executors.newFixedThreadPool(2);
        try {
            Future<?> initialization = workers.submit(() -> {
                await(start);
                for (int i = 0; i < 4; i++) assertEquals(expected, recognize(newEngine()));
            });
            Future<?> recognition = workers.submit(() -> {
                await(start);
                for (int i = 0; i < 12; i++) assertEquals(expected, recognize(first));
            });
            start.countDown();
            initialization.get(120, TimeUnit.SECONDS);
            recognition.get(120, TimeUnit.SECONDS);
        } finally {
            workers.shutdownNow();
            assertTrue(workers.awaitTermination(120, TimeUnit.SECONDS));
        }
    }

    private static void await(CountDownLatch latch) {
        try {
            assertTrue(latch.await(30, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AssertionError(e);
        }
    }
}
