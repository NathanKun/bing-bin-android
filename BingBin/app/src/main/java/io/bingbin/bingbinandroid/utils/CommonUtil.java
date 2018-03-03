package io.bingbin.bingbinandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Common utils
 *
 * @author Junyang HE
 */
public abstract class CommonUtil {

    /**
     * Get file from a content uri
     *
     * @param uri      input uri
     * @param activity activity
     * @return file stored in cache
     */
    public static File uriToFile(Uri uri, Activity activity) {
        try {
            InputStream input = activity.getContentResolver().openInputStream(uri);
            File file = new File(activity.getCacheDir(), "cacheFileAppeal.srl");
            OutputStream output = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            assert input != null;
            while ((read = input.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
            input.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Bitmap scaleBitmapKeepRatio(Bitmap TargetBmp, int reqHeightInPixels, int reqWidthInPixels) {
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, TargetBmp.getWidth(), TargetBmp.getHeight()), new RectF(0, 0, reqWidthInPixels, reqHeightInPixels), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(TargetBmp, 0, 0, TargetBmp.getWidth(), TargetBmp.getHeight(), m, true);
    }

    public static Bitmap scaleBitmapToCropFill(Bitmap bitmap, int reqHeightInPixels, int reqWidthInPixels) {
        bitmap = scaleBitmapKeepRatio(bitmap, reqHeightInPixels, 100000);
        if (bitmap.getWidth() < reqWidthInPixels) {
            bitmap = scaleBitmapKeepRatio(bitmap, 100000, reqWidthInPixels);
        }
        return bitmap;
    }

    /**
     * Blur a bitmap
     * @param context   context
     * @param source    original bitmap
     * @param radius    radius of blur
     * @return  blur bitmap
     */
    public static Bitmap rsBlur(Context context, Bitmap source, int radius) {

        //初始化一个RenderScript Context
        RenderScript renderScript = RenderScript.create(context);
        //通过Script至少创建一个Allocation
        final Allocation input = Allocation.createFromBitmap(renderScript, source);
        final Allocation output = Allocation.createTyped(renderScript, input.getType());
        //创建ScriptIntrinsic
        ScriptIntrinsicBlur scriptIntrinsicBlur = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        //填充数据到Allocations
        scriptIntrinsicBlur.setInput(input);
        //设置模糊半径, 0-25
        scriptIntrinsicBlur.setRadius(radius);
        //启动内核，调用方法处理
        scriptIntrinsicBlur.forEach(output);
        //从Allocation 中拷贝数据
        output.copyTo(source);
        //销毁RenderScript对象
        renderScript.destroy();

        return source;
    }

    /**
     * Process a bitmap to gray scale
     * @param bmpOriginal   original bitmap
     * Ref: https://stackoverflow.com/questions/3373860/convert-a-bitmap-to-grayscale-in-android
     * @return  gray scale bitmap
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
