package com.zepp.www.moreteapots.helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.opengl.GLUtils;
import android.os.Build;
import java.io.File;
import java.io.FileInputStream;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by xubinggui on 1/26/16.
 */
public class NDKHelper {
    private static Context sContext;

    public static void setContext(Context c) {
        sContext = c;
    }

    private int nextPOT(int i) {
        int pot = 1;
        while(pot < i) {
            pot <<= 1;
        }
        return pot;
    }

    private Bitmap scaleBitmap(Bitmap bitmapToScale, float newWidth, float newHeight) {
        if(bitmapToScale == null) {
            return null;
        }

        int width = bitmapToScale.getWidth();
        int height = bitmapToScale.getHeight();

        Matrix matrix = new Matrix();

        matrix.postScale(newWidth / width, newHeight / height);

        return Bitmap.createBitmap(bitmapToScale, 0, 0, bitmapToScale.getWidth(), bitmapToScale.getHeight(), matrix, true);
    }

    public boolean loadTexture(String path) {
        Bitmap bitmap = null;
        try {
            String str = path;
            if(!path.startsWith("/")) {
                str = "/" + path;
            }

            File file = new File(sContext.getExternalFilesDir(null), str);
            if(file.canRead()) {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            }else {
                bitmap = BitmapFactory.decodeStream(sContext.getResources().getAssets().open(path));
            }
        }catch (Exception e) {
            return false;
        }

        if(bitmap != null) {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        }
        return true;
    }

    public Bitmap openBitmap(String path, boolean iScalePOT) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(sContext.getResources().getAssets().open(path));
            if(iScalePOT) {
                int originWidth = getBitmapWidth(bitmap);
                int originHeight = getBitmapHeight(bitmap);
                int width = nextPOT(originWidth);
                int height = nextPOT(originHeight);
                if(originWidth != width || originHeight != height) {
                    bitmap = scaleBitmap(bitmap, width, height);
                }
            }
        }catch (Exception e) {

        }
        return bitmap;
    }

    public int getBitmapHeight(Bitmap bitmap) {
        return bitmap.getHeight();
    }

    public int getBitmapWidth(Bitmap bitmap) {
        return bitmap.getWidth();
    }

    public void closeBitmap(Bitmap bitmap) {
        bitmap.recycle();
    }

    public void getBitmapPixels(Bitmap bmp, int[] pixels) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
    }

    public static String getNativeLibraryDirectory(Context context) {
        ApplicationInfo info = context.getApplicationInfo();

        if((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0 || (info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
            return info.nativeLibraryDir;
        }

        return "/system/lib";
    }

    @TargetApi(17)
    public int getNativeAudioBufferSize() {
        int SDK_INT = Build.VERSION.SDK_INT;
        if(SDK_INT >= 17) {
            AudioManager am = (AudioManager) sContext.getSystemService(Context.AUDIO_SERVICE);
            String framesPerBuffer = am.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER);
            return Integer.parseInt(framesPerBuffer);
        }
        return 0;
    }

    public int getNativeAudioSampleRate() {
        return AudioTrack.getNativeOutputSampleRate(AudioManager.STREAM_SYSTEM);
    }
}
