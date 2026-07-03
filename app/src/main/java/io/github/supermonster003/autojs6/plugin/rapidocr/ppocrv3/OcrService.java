package io.github.supermonster003.autojs6.plugin.rapidocr.ppocrv3;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SharedMemory;
import android.util.Log;

import com.benjaminwan.ocrlibrary.RapidOcrEngine;

import org.autojs.plugin.common.api.PluginCapabilityKeys;
import org.autojs.plugin.common.api.PluginInfo;
import org.autojs.plugin.paddle.ocr.api.IOcrPlugin;
import org.autojs.plugin.paddle.ocr.api.OcrOptions;
import org.autojs.plugin.paddle.ocr.api.OcrResult;
import org.autojs.plugin.paddle.ocr.api.PaddleOcrOptionExtraKeys;
import org.autojs.plugin.paddle.ocr.api.PaddleOcrPluginCapabilityKeys;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OcrService extends Service {

    private static final String TAG = "RapidOcrV3Service";

    private static final int DEFAULT_MAX_SIDE_LEN = 1024;
    private static final int DEFAULT_PADDING = 50;
    private static final float DEFAULT_BOX_SCORE_THRESH = 0.5f;
    private static final float DEFAULT_BOX_THRESH = 0.3f;
    private static final float DEFAULT_UNCLIP_RATIO = 2.0f;
    private static final boolean DEFAULT_DO_ANGLE = false;
    private static final boolean DEFAULT_MOST_ANGLE = false;
    private static final String[] SUPPORTED_ABIS = {"arm64-v8a", "armeabi-v7a", "x86_64"};

    private static boolean nativeDependencyLoaded;

    private final Object ocrLock = new Object();
    private RapidOcrEngine engine;

    private final IOcrPlugin.Stub binder = new IOcrPlugin.Stub() {
        @Override
        public PluginInfo getInfo() throws RemoteException {
            PluginInfo info = new PluginInfo();
            info.setName("Rapid OCR (PP-OCRv3)");
            info.setAuthor("RapidAI / BenjaminWan");
            info.setId("rapid-ocr-pp-ocrv3");
            info.setEngine("rapid-ocr");
            info.setVariant("pp-ocrv3");
            info.setVersionName(BuildConfig.VERSION_NAME);
            info.setVersionCode(BuildConfig.VERSION_CODE);
            info.setVersionDate(BuildConfig.VERSION_DATE);
            info.setSupportedAbis(SUPPORTED_ABIS);

            Bundle capabilities = new Bundle();
            capabilities.putInt(PluginCapabilityKeys.REQUIRES_HOST_VERSION, 3923);
            capabilities.putBoolean(PaddleOcrPluginCapabilityKeys.SUPPORTS_RAW_IMAGE, true);
            info.setCapabilities(capabilities);
            return info;
        }

        @Override
        public List<String> recognizeText(ParcelFileDescriptor image, OcrOptions options) throws RemoteException {
            synchronized (ocrLock) {
                Bitmap bitmap = decodeImage(image, options);
                try {
                    List<String> texts = new ArrayList<>();
                    for (OcrResult result : detectInternal(bitmap, options)) {
                        texts.add(result.text);
                    }
                    return texts;
                } catch (RuntimeException e) {
                    Log.e(TAG, "recognizeText failed", e);
                    throw e;
                } finally {
                    recycleDecodedBitmap(bitmap);
                }
            }
        }

        @Override
        public List<OcrResult> detect(ParcelFileDescriptor image, OcrOptions options) throws RemoteException {
            synchronized (ocrLock) {
                Bitmap bitmap = decodeImage(image, options);
                try {
                    return detectInternal(bitmap, options);
                } catch (RuntimeException e) {
                    Log.e(TAG, "detect failed", e);
                    throw e;
                } finally {
                    recycleDecodedBitmap(bitmap);
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private RapidOcrEngine getEngine() {
        if (engine == null) {
            try {
                ensureNativeDependencyLoaded();
                engine = new RapidOcrEngine(this);
            } catch (Throwable e) {
                throw wrapFailure(e);
            }
        }
        return engine;
    }

    private static synchronized void ensureNativeDependencyLoaded() {
        if (nativeDependencyLoaded) {
            return;
        }
        System.loadLibrary("onnxruntime");
        nativeDependencyLoaded = true;
    }

    private List<OcrResult> detectInternal(Bitmap bitmap, OcrOptions options) {
        Bitmap output = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        try {
            RapidOcrEngine.Result raw = invokeDetect(bitmap, output, options);
            List<OcrResult> results = new ArrayList<>();
            for (RapidOcrEngine.Block block : textBlocksOf(raw)) {
                OcrResult result = new OcrResult();
                result.text = block.getText();
                result.confidence = block.getConfidence();
                result.bounds = block.getBounds();
                results.add(result);
            }
            return results;
        } finally {
            output.recycle();
        }
    }

    private RapidOcrEngine.Result invokeDetect(Bitmap bitmap, Bitmap output, OcrOptions options) {
        RapidOcrEngine currentEngine = getEngine();
        try {
            return currentEngine.detect(
                    bitmap,
                    output,
                    DEFAULT_PADDING,
                    resolveMaxSideLen(options),
                    resolveBoxScoreThresh(options),
                    DEFAULT_BOX_THRESH,
                    DEFAULT_UNCLIP_RATIO,
                    DEFAULT_DO_ANGLE,
                    DEFAULT_MOST_ANGLE
            );
        } catch (Throwable e) {
            throw wrapFailure(e);
        }
    }

    private int resolveMaxSideLen(OcrOptions options) {
        if (options != null && options.detLongSize > 0) {
            return options.detLongSize;
        }
        return DEFAULT_MAX_SIDE_LEN;
    }

    private float resolveBoxScoreThresh(OcrOptions options) {
        if (options != null && options.scoreThreshold >= 0f) {
            return options.scoreThreshold;
        }
        return DEFAULT_BOX_SCORE_THRESH;
    }

    private List<RapidOcrEngine.Block> textBlocksOf(RapidOcrEngine.Result raw) {
        if (raw == null || raw.getTextBlocks() == null) {
            return Collections.emptyList();
        }
        return raw.getTextBlocks();
    }

    private RuntimeException wrapFailure(Throwable cause) {
        if (cause instanceof Error) {
            Log.e(TAG, "Rapid OCR (PP-OCRv3) engine failed", cause);
            throw (Error) cause;
        }
        String message = "Rapid OCR (PP-OCRv3) engine failed: " + cause.getClass().getName();
        if (cause.getMessage() != null && !cause.getMessage().isEmpty()) {
            message += ": " + cause.getMessage();
        }
        Log.e(TAG, message, cause);
        return new IllegalStateException(message, cause);
    }

    private Bitmap decodeImage(ParcelFileDescriptor descriptor, OcrOptions options) {
        Bundle extras = options == null ? null : options.extras;
        boolean useRaw = extras != null && extras.getBoolean(PaddleOcrOptionExtraKeys.RAW_IMAGE, false);
        if (useRaw) {
            int width = extras.getInt(PaddleOcrOptionExtraKeys.RAW_WIDTH, -1);
            int height = extras.getInt(PaddleOcrOptionExtraKeys.RAW_HEIGHT, -1);
            int stride = extras.getInt(PaddleOcrOptionExtraKeys.RAW_STRIDE, width * 4);
            if (width > 0 && height > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                    Bitmap decoded;
                    try {
                        decoded = tryDecodeSharedMemory(descriptor, width, height, stride);
                    } catch (Throwable e) {
                        decoded = null;
                    }
                    if (decoded != null) {
                        closeQuietly(descriptor);
                        return decoded;
                    }
                }
                return decodeRawStream(descriptor, width, height, stride);
            }
        }

        try (ParcelFileDescriptor closeable = descriptor) {
            Bitmap bitmap = BitmapFactory.decodeFileDescriptor(closeable.getFileDescriptor());
            if (bitmap == null) {
                throw new IllegalStateException("decode image failed");
            }
            return bitmap;
        } catch (IOException e) {
            throw new IllegalStateException("decode image failed", e);
        }
    }

    private void recycleDecodedBitmap(Bitmap bitmap) {
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    private Bitmap decodeRawStream(ParcelFileDescriptor descriptor, int width, int height, int stride) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        int rowBytes = width * 4;
        int total = stride * height;
        byte[] raw = new byte[total];
        try (ParcelFileDescriptor.AutoCloseInputStream input = new ParcelFileDescriptor.AutoCloseInputStream(descriptor)) {
            int offset = 0;
            while (offset < total) {
                int read = input.read(raw, offset, total - offset);
                if (read < 0) {
                    break;
                }
                offset += read;
            }
            if (offset < total) {
                throw new IllegalStateException("read raw image failed: " + offset + "/" + total);
            }
        } catch (IOException e) {
            throw new IllegalStateException("read raw image failed", e);
        }

        if (stride == rowBytes && bitmap.getRowBytes() == rowBytes) {
            bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(raw, 0, rowBytes * height));
        } else {
            ByteBuffer tmp = ByteBuffer.allocate(rowBytes * height);
            int pos = 0;
            for (int row = 0; row < height; row++) {
                tmp.put(raw, pos, rowBytes);
                pos += stride;
            }
            tmp.rewind();
            bitmap.copyPixelsFromBuffer(tmp);
        }
        return bitmap;
    }

    private Bitmap tryDecodeSharedMemory(ParcelFileDescriptor descriptor, int width, int height, int stride) throws IOException {
        ParcelFileDescriptor dup = ParcelFileDescriptor.dup(descriptor.getFileDescriptor());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            closeQuietly(dup);
            return null;
        }

        SharedMemory sharedMemory;
        try {
            sharedMemory = SharedMemory.fromFileDescriptor(dup);
        } catch (Throwable e) {
            closeQuietly(dup);
            return null;
        }

        ByteBuffer buffer;
        try {
            buffer = sharedMemory.mapReadOnly();
        } catch (Throwable e) {
            sharedMemory.close();
            throw new IllegalStateException(e);
        }

        buffer.order(ByteOrder.nativeOrder());
        try {
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            int rowBytes = width * 4;
            if (stride == rowBytes && bitmap.getRowBytes() == rowBytes) {
                buffer.limit(rowBytes * height);
                bitmap.copyPixelsFromBuffer(buffer);
            } else {
                byte[] packed = new byte[rowBytes * height];
                int srcPos = 0;
                int dstPos = 0;
                while (dstPos < packed.length) {
                    buffer.position(srcPos);
                    buffer.get(packed, dstPos, rowBytes);
                    srcPos += stride;
                    dstPos += rowBytes;
                }
                bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(packed));
            }
            return bitmap;
        } finally {
            SharedMemory.unmap(buffer);
            sharedMemory.close();
        }
    }

    private static void closeQuietly(ParcelFileDescriptor descriptor) {
        try {
            descriptor.close();
        } catch (Throwable ignored) {
            // Ignore close failures.
        }
    }
}
