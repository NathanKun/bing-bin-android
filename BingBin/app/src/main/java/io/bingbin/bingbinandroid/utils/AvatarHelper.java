package io.bingbin.bingbinandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import io.bingbin.bingbinandroid.R;

/**
 * Healper class for avatar
 *
 * @author Junyang HE
 * Created by Junyang HE on 22/02/2018.
 */

public abstract class AvatarHelper {
    final static int[] thresholdRabbit = {3, 10, 25, 75, 150, 233, 666, 1024}; // 8
    final static int[] thresholdLeaf = {1, 3, 6, 10, 15, 25, 50, 75, 100, 125, 150, 175, 200, 233, 666, 1024}; // 16

    final static int[] rabbitIds = {R.drawable.rabbit_1, R.drawable.rabbit_2, R.drawable.rabbit_3,
            R.drawable.rabbit_4, R.drawable.rabbit_5, R.drawable.rabbit_6,
            R.drawable.rabbit_7, R.drawable.rabbit_8, R.drawable.rabbit_9};
    final static int[] leafIds = {R.drawable.leaf_1, R.drawable.leaf_2, R.drawable.leaf_3,
            R.drawable.leaf_4, R.drawable.leaf_5, R.drawable.leaf_6, R.drawable.leaf_17,
            R.drawable.leaf_8, R.drawable.leaf_9, R.drawable.leaf_10, R.drawable.leaf_11,
            R.drawable.leaf_12, R.drawable.leaf_13, R.drawable.leaf_14, R.drawable.leaf_15,
            R.drawable.leaf_16, R.drawable.leaf_17, R.drawable.leaf_18, R.drawable.leaf_19};

    /**
     * get max rabbitId allow
     * @param ecoPoint  ecopoint
     * @return  max rabbitId allow
     */
    public static int getAllowMaxRabbitId(int ecoPoint) {
        for(int i = 0; i < thresholdRabbit.length; i++) {
            if(thresholdRabbit[i] > ecoPoint) {
                return i + 1; // id start with 1 and there is a default rabbit
            }
        }
        return thresholdRabbit.length + 1;
    }

    /**
     * get max leafId allowed
     * @param sunPoint  sunpoint of the user
     * @return  max leafId allowed
     */
    public static int getAllowMaxLeafId(int sunPoint) {
        for(int i = 0; i < thresholdLeaf.length; i++) {
            if(thresholdLeaf[i] > sunPoint) {
                return i + 1; // id start with 1 and there is a default leaf
            }
        }
        return thresholdLeaf.length + 1;
    }

    /**
     * generate avatar bitmap from rabbitId and leafId
     * @param context   Context object
     * @param rabbitId  rabbit id
     * @param leafId    leaf id
     * @return  bitmap compose by rabbit and leaf
     */
    public static Bitmap generateAvarat(Context context, int rabbitId, int leafId) {
        Bitmap rabbit = BitmapFactory.decodeResource(context.getResources(), rabbitIds[rabbitId - 1]);
        Bitmap leaf = BitmapFactory.decodeResource(context.getResources(), leafIds[leafId - 1]);

        Bitmap bmOverlay = Bitmap.createBitmap(rabbit.getWidth(), rabbit.getHeight(), rabbit.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(rabbit, new Matrix(), null);
        canvas.drawBitmap(leaf, (rabbit.getWidth() - leaf.getWidth()) / 2, rabbit.getHeight()/3, null);

        return bmOverlay;
    }
}
