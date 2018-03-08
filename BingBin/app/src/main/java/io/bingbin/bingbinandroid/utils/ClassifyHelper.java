package io.bingbin.bingbinandroid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.bingbin.bingbinandroid.tensorflow.Classifier;
import io.bingbin.bingbinandroid.tensorflow.TensorFlowImageClassifier;
import io.bingbin.bingbinandroid.tensorflow.env.ImageUtils;

/**
 * Helper class for classify an image
 *
 * @author Junyang HE
 */

public abstract class ClassifyHelper {
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";
    private static final boolean MAINTAIN_ASPECT = true;

    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";

    public static String classify(Activity activity, Bitmap rgbFrameBitmap) {

        int previewHeight, previewWidth, sensorOrientation;

        // 读取图
        //rgbFrameBitmap = rgbFrameBitmap.copy(Bitmap.Config.ARGB_8888, true);
        rgbFrameBitmap = rgbFrameBitmap.copy(Bitmap.Config.RGB_565, true);

        previewHeight = rgbFrameBitmap.getHeight();
        previewWidth = rgbFrameBitmap.getWidth();
        sensorOrientation = previewHeight > previewWidth ? 0 : 1;

        // 创建classifier
        Classifier classifier = TensorFlowImageClassifier.create(
                activity.getAssets(),
                MODEL_FILE,
                LABEL_FILE,
                INPUT_SIZE,
                IMAGE_MEAN,
                IMAGE_STD,
                INPUT_NAME,
                OUTPUT_NAME);

        // 缩小图像矩阵
        Matrix frameToCropTransform;
        frameToCropTransform =
                ImageUtils.getTransformationMatrix(
                        previewWidth, previewHeight,
                        INPUT_SIZE, INPUT_SIZE,
                        sensorOrientation, MAINTAIN_ASPECT);

        // 空的小图
        //Bitmap croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);
        Bitmap croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.RGB_565);

        // 缩小图片
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        // 识别
        List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
        Collections.sort(results, new RecognitionResultComparator());

        Log.d("Recognition result", results.get(0).getTitle() + ": " + results.get(0).getConfidence());
        return results.get(0).getTitle();
    }
}

class RecognitionResultComparator implements Comparator<Classifier.Recognition> {
    @Override
    public int compare(Classifier.Recognition o1, Classifier.Recognition o2) {
        return o1.getConfidence().compareTo(o2.getConfidence());
    }
}
