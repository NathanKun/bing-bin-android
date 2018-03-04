package io.bingbin.bingbinandroid.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.bingbin.bingbinandroid.R;

/**
 * Healper class for avatar
 *
 * @author Junyang HE
 * Created by Junyang HE on 22/02/2018.
 */

public abstract class AvatarHelper {

    // threshold of ecopoint/sunpoint to unlock the next rabbit/leaf
    final static int[] thresholdRabbit = {3, 10, 25, 75, 150, 233, 666, 1024}; // 8
    final static int[] thresholdLeaf = {1, 3, 6, 10, 15, 25, 50, 75, 100, 125, 150, 175, 200, 233, 666, 1024}; // 16

    final static int TYPE_RABBIT = 666;
    final static int TYPE_LEAF = 6666;

    final static int[] rabbitIds = {R.drawable.rabbit_1, R.drawable.rabbit_2, R.drawable.rabbit_3,
            R.drawable.rabbit_4, R.drawable.rabbit_5, R.drawable.rabbit_6,
            R.drawable.rabbit_7, R.drawable.rabbit_8, R.drawable.rabbit_9};
    final static int[] leafIds = {R.drawable.leaf_1, R.drawable.leaf_2, R.drawable.leaf_3,
            R.drawable.leaf_4, R.drawable.leaf_5, R.drawable.leaf_6, R.drawable.leaf_17,
            R.drawable.leaf_8, R.drawable.leaf_9, R.drawable.leaf_10, R.drawable.leaf_11,
            R.drawable.leaf_12, R.drawable.leaf_13, R.drawable.leaf_14, R.drawable.leaf_15,
            R.drawable.leaf_16, R.drawable.leaf_17, R.drawable.leaf_18, R.drawable.leaf_19};

    public static int[] getThresholdRabbit() { return thresholdRabbit; }

    public static int[] getThresholdLeaf() { return thresholdLeaf; }

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
     *
     * @param context   Context object
     * @param rabbitId  rabbit id
     * @param leafId    leaf id
     * @return  bitmap compose by rabbit and leaf
     */
    public static Bitmap generateAvatar(Context context, int rabbitId, int leafId) {
        Bitmap rabbit;
        Bitmap leaf;
        if(rabbitId > 0 && rabbitId < rabbitIds.length && leafId > 0 && leafId < leafIds.length) {
            rabbit = BitmapFactory.decodeResource(context.getResources(), rabbitIds[rabbitId - 1]);
            leaf = BitmapFactory.decodeResource(context.getResources(), leafIds[leafId - 1]);
        } else {
            Log.d("id index out of bounds", "rabbitId = " + rabbitId + " leafId = " + leafId);
            rabbit = BitmapFactory.decodeResource(context.getResources(), rabbitIds[0]);
            leaf = BitmapFactory.decodeResource(context.getResources(), leafIds[0]);
        }

        Bitmap bmOverlay = Bitmap.createBitmap(rabbit.getWidth(), rabbit.getHeight(), rabbit.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(rabbit, new Matrix(), null);
        canvas.drawBitmap(leaf, (rabbit.getWidth() - leaf.getWidth()) / 2, rabbit.getHeight()/5, null);

        return bmOverlay;
    }

    /**
     * get list of rabbit bitmap, for AvatarActivity
     *
     * @param ctx   context
     * @param ecoPoint  eco point of user
     * @return  list of rabbit bitmap
     */
    public static List<Bitmap> getRabbitBitmapsForChangingAvatar(Context ctx, int ecoPoint) {
        return getBitmapsForChangingAvatar(ctx, ecoPoint, TYPE_RABBIT);
    }

    /**
     * get list of leaf bitmap, for AvatarActivity
     *
     * @param ctx   context
     * @param sunPoint  sun point of user
     * @return  list of leaf bitmap
     */
    public static List<Bitmap> getLeafBitmapsForChangingAvatar(Context ctx, int sunPoint) {
        return getBitmapsForChangingAvatar(ctx, sunPoint, TYPE_LEAF);
    }

    /**
     * get list of bitmap for showing, for AvatarActivity
     *
     * @param context   context
     * @param points    ecopoint or sunpoint, depends on type
     * @param type      TYPE_RABBIT or TYPE_LEAF
     * @return  list of bitmap
     */
    private static List<Bitmap> getBitmapsForChangingAvatar(Context context, int points, int type) {
        int maxAllow;
        int[] ids;

        if(type == TYPE_RABBIT) {
            maxAllow = getAllowMaxRabbitId(points);
            ids = rabbitIds;
        } else {
            maxAllow = getAllowMaxLeafId(points);
            ids = leafIds;
        }

        List<Bitmap> bitmaps = new ArrayList<>();
        for(int i = 0; i < ids.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), ids[i]);
            if(i <= maxAllow) {
                bitmaps.add(bitmap);
            } else {
                bitmaps.add(CommonUtil.toGrayscale(bitmap));
            }
        }
        return bitmaps;
    }
}
