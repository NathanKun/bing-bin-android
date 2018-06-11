package io.bingbin.bingbinandroid.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Common utils
 *
 * @author Junyang HE
 */
@SuppressWarnings({"WeakerAccess", "unused"})
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
    @SuppressWarnings("SameParameterValue")
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

    //毛玻璃
    public static Bitmap maoboliBlur(Bitmap bitmap){

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int color;

        Random rnd = new Random();
        int iModel = 5;
        int i = width - iModel;
        while (i > 1)
        {
            int j = height - iModel;
            while (j > 1)
            {
                int iPos = rnd.nextInt(100000) % iModel;
                color = bitmap.getPixel(i + iPos, j + iPos);
                result.setPixel(i, j, color);
                j = j - 1;
            }
            i = i - 1;
        }
        return result;
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

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        bmpOriginal.recycle();
        return bmpGrayscale;
    }

    /**
     * Write bitmap associated with a url to disk cache
     */
    public static String saveBitmap(Context context, Bitmap bitmap) {
        final String dirpath = Environment.getExternalStorageDirectory() + File.separator + "BingBin";
        final String filename = (new SimpleDateFormat("yyyyMMdd-HHmmssSSS", Locale.FRANCE).format(new Date())) + ".JPEG";
        final String filepath = dirpath + File.separator + filename;
        File dir = new File(dirpath);
        boolean doSave = true;
        if (!dir.exists()) {
            doSave = dir.mkdirs();
        }
        if (doSave) {
            saveBitmapToFile(dir,filename, bitmap, 100);
            return filepath;
        }
        else {
            Log.e("saveBitmap","Couldn't create target directory.");
        }
        return null;
    }

    public static Bitmap loadBitmap(Context context, String filepath) {
        return loadBitmap(context, filepath, 1);
    }

    public static Bitmap loadBitmap(Context context, String filepath, int sample) {
        Bitmap bmp = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sample;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        try {
            bmp = BitmapFactory.decodeFile(filepath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * @param dir you can get from many places like Environment.getExternalStorageDirectory() or mContext.getFilesDir() depending on where you want to save the image.
     * @param fileName The file name.
     * @param bm The Bitmap you want to save.
     * @param quality quality goes from 1 to 100. (Percentage).
     * @return true if the Bitmap was saved successfully, false otherwise.
     */
    @SuppressWarnings({"UnusedReturnValue", "SameParameterValue"})
    public static boolean saveBitmapToFile(File dir, String fileName, Bitmap bm, int quality) {

        File imageFile = new File(dir, fileName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);

            bm.compress(Bitmap.CompressFormat.PNG, quality, fos);

            fos.close();

            return true;
        }
        catch (IOException e) {
            Log.e("app",e.getMessage());
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return false;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
            /*while ((height / inSampleSize) >= reqHeight
                    && (width / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }*/
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight, Bitmap.Config config) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = config;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStream is,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }
}
