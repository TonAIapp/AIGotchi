package com.digwex.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.nio.ByteBuffer;

import javax.inject.Inject;
import javax.inject.Singleton;


@Singleton
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenshotManager implements ImageReader.OnImageAvailableListener {

    private Intent mIntent;
    private Handler mHandler;

    public interface Callback {
        void onScreenshot(Bitmap bmp);
    }

    private final Context mContext;
    private Callback mCallback;

    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;
    private MediaProjection mMediaProjection;

    @Inject
    ScreenshotManager(Context context) {
        mContext = context;
        startBackgroundThread();
    }

    public void requestScreenshotPermission(@NonNull Activity activity, int requestId) {
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager != null) {
            activity.startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(),
                    requestId);
        }
    }

    public void onActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null)
            mIntent = data;
        else mIntent = null;
    }

    private void startBackgroundThread() {
        HandlerThread thread = new HandlerThread(ScreenshotManager.class.getName() + "Thread",
                Process.THREAD_PRIORITY_FOREGROUND);
        thread.start();
        mHandler = new Handler(thread.getLooper());
    }

    /**
     * Takes the screenshot of whatever currently is on the default display.
     */
    public void takeScreenshot(final Callback callback) {
        mCallback = callback;

        if (mIntent == null) {
            mCallback.onScreenshot(null);
            return;
        }
        MediaProjectionManager mediaProjectionManager =
                (MediaProjectionManager) mContext.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (mediaProjectionManager == null) {
            mCallback.onScreenshot(null);
            return;
        }

        mMediaProjection = mediaProjectionManager.getMediaProjection(Activity.RESULT_OK, mIntent);

        if (mMediaProjection == null) {
            mCallback.onScreenshot(null);
            return;
        }

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        if (wm == null) {
            mCallback.onScreenshot(null);
            return;
        }
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getRealSize(size);

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int mWidth = size.x;
        int mHeight = size.y;
        int density = metrics.densityDpi;

        mImageReader = ImageReader.newInstance(mWidth, mHeight, PixelFormat.RGBA_8888, 2);
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("ScreenshotDisplay",
                mWidth, mHeight, density,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);

        mMediaProjection.registerCallback(new MediaProjection.Callback() {
            @Override
            public void onStop() {
                super.onStop();
                if (mVirtualDisplay != null)
                    mVirtualDisplay.release();
                mImageReader.setOnImageAvailableListener(null, null);
                mMediaProjection.unregisterCallback(this);
            }
        }, null);

        mImageReader.setOnImageAvailableListener(ScreenshotManager.this, mHandler);
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        mMediaProjection.stop();
        Image image = null;
        Bitmap bitmap = null;

        try {
            image = reader.acquireLatestImage();
            if (image != null) {
                int width = image.getWidth();
                int height = image.getHeight();
                Image.Plane[] planes = image.getPlanes();
                ByteBuffer buffer = planes[0].getBuffer();
                int pixelStride = planes[0].getPixelStride();
                int rowStride = planes[0].getRowStride();
                int rowPadding = rowStride - pixelStride * width;

                bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height,
                        Bitmap.Config.ARGB_8888);
                bitmap.copyPixelsFromBuffer(buffer);

                Matrix matrix = new Matrix();
                if (height > width)
                    matrix.postRotate(-90);

                if (bitmap.getWidth() != width || bitmap.getHeight() != height || height > width) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
                }
            }
        } catch (Exception ignored) {
            if (bitmap != null)
                bitmap.recycle();
        } finally {
            if (image != null)
                image.close();
            reader.close();
            mCallback.onScreenshot(bitmap);
        }
    }
}
