package org.autojs.plugin.runtime;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import androidx.test.platform.app.InstrumentationRegistry;
import org.autojs.plugin.paddle.ocr.api.IOcrPlugin;
import org.autojs.plugin.paddle.ocr.api.OcrOptions;
import org.autojs.plugin.paddle.ocr.api.OcrResult;
import org.junit.Test;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 * BitmapFactory decodes 16-bit PNG files as RGBA_F16 even when ARGB_8888 is preferred.
 * The native engine accepts RGBA_8888 only, so the service must convert such images.
 */
public class HighBitDepthImageTest {
    @Test(timeout = 180000) public void sixteenBitPngIsRecognizedThroughFilesAndPipes() throws Exception {
        assumeTrue("RGBA_F16 requires Android 8.0", Build.VERSION.SDK_INT >= Build.VERSION_CODES.O);
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        File image = File.createTempFile("binder-f16-", ".png", context.getCacheDir());
        try {
            Bitmap source = Bitmap.createBitmap(640, 160, Bitmap.Config.RGBA_F16);
            Canvas canvas = new Canvas(source);
            canvas.drawColor(Color.WHITE);
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(Color.BLACK);
            paint.setTextSize(48);
            canvas.drawText("Hello AutoJs6", 30, 90, paint);
            try (FileOutputStream stream = new FileOutputStream(image)) {
                assertTrue(source.compress(Bitmap.CompressFormat.PNG, 100, stream));
            }
            source.recycle();
            Bitmap decoded = BitmapFactory.decodeFile(image.getPath());
            assertNotNull(decoded);
            Bitmap.Config decodedConfig = decoded.getConfig();
            decoded.recycle();
            assumeTrue("The platform must decode the fixture as RGBA_F16, got " + decodedConfig, decodedConfig == Bitmap.Config.RGBA_F16);

            ArrayBlockingQueue<IBinder> connectionResult = new ArrayBlockingQueue<>(1);
            ServiceConnection connection = new ServiceConnection() {
                @Override public void onServiceConnected(ComponentName name, IBinder binder) { connectionResult.add(binder); }
                @Override public void onServiceDisconnected(ComponentName name) {}
            };
            Intent discovery = new Intent("org.autojs.plugin.PADDLE_OCR").addCategory("rapid-ocr").setPackage(context.getPackageName());
            List<ResolveInfo> services = context.getPackageManager().queryIntentServices(discovery, 0);
            assertEquals(1, services.size());
            ComponentName component = new ComponentName(services.get(0).serviceInfo.packageName, services.get(0).serviceInfo.name);
            assertTrue(context.bindService(new Intent().setComponent(component), connection, Context.BIND_AUTO_CREATE));
            try {
                IBinder binder = connectionResult.poll(30, TimeUnit.SECONDS);
                assertNotNull(binder);
                // Force real parcels even when instrumentation shares the service process.
                Binder transport = new Binder() {
                    @Override protected boolean onTransact(int code, Parcel input, Parcel output, int flags) throws RemoteException {
                        return binder.transact(code, input, output, flags);
                    }
                };
                transport.attachInterface(null, binder.getInterfaceDescriptor());
                IOcrPlugin plugin = IOcrPlugin.Stub.asInterface(transport);
                try (ParcelFileDescriptor fd = ParcelFileDescriptor.open(image, ParcelFileDescriptor.MODE_READ_ONLY)) {
                    assertRecognized(plugin.detect(fd, new OcrOptions()));
                }
                ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                AtomicReference<Throwable> pipeFailure = new AtomicReference<>();
                Thread writer = new Thread(() -> {
                    try (InputStream input = new FileInputStream(image);
                         OutputStream target = new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])) {
                        byte[] bytes = new byte[8192];
                        int count;
                        while ((count = input.read(bytes)) != -1) target.write(bytes, 0, count);
                    } catch (Throwable error) { pipeFailure.set(error); }
                }, "binder-f16-pipe");
                writer.setDaemon(true);
                writer.start();
                try (ParcelFileDescriptor fd = pipe[0]) {
                    assertRecognized(plugin.detect(fd, new OcrOptions()));
                }
                writer.join(10000);
                assertFalse("Image pipe writer leaked", writer.isAlive());
                assertNull(pipeFailure.get());
            } finally {
                context.unbindService(connection);
            }
        } finally {
            assertTrue(image.delete() || !image.exists());
        }
    }

    private static void assertRecognized(List<OcrResult> results) {
        assertNotNull(results);
        StringBuilder text = new StringBuilder();
        for (OcrResult result : results) text.append(result.text).append(' ');
        assertTrue("Recognized text: " + text, text.toString().contains("Hello"));
    }
}
