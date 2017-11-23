package io.bingbin.bingbinandroid.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.io.File;
import java.util.List;

import io.bingbin.bingbinandroid.tensorflow.Classifier;
import io.bingbin.bingbinandroid.tensorflow.TensorFlowImageClassifier;
import io.bingbin.bingbinandroid.tensorflow.env.ImageUtils;

/**
 * Created by Junyang HE on 22/11/2017.
 * Helper class for classify an image
 */

public class ClassifyHelper {
    private static final int INPUT_SIZE = 224;
    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;
    private static final String INPUT_NAME = "input";
    private static final String OUTPUT_NAME = "final_result";
    private static final boolean MAINTAIN_ASPECT = true;

    private static final String MODEL_FILE = "file:///android_asset/graph.pb";
    private static final String LABEL_FILE = "file:///android_asset/labels.txt";

    public static List<Classifier.Recognition> Classify(Activity activity, File imgFile) {

        int previewHeight, previewWidth, sensorOrientation;

        // 读取图片
        Bitmap rgbFrameBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        rgbFrameBitmap = rgbFrameBitmap.copy(Bitmap.Config.ARGB_8888, true); // mutable
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
        Bitmap croppedBitmap = Bitmap.createBitmap(INPUT_SIZE, INPUT_SIZE, Bitmap.Config.ARGB_8888);

        // 缩小图片
        final Canvas canvas = new Canvas(croppedBitmap);
        canvas.drawBitmap(rgbFrameBitmap, frameToCropTransform, null);

        // 识别
        final List<Classifier.Recognition> results = classifier.recognizeImage(croppedBitmap);
        Log.d("result", results.toString());
        return results;
    }
}
