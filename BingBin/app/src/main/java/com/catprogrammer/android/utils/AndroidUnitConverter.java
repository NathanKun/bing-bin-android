package com.catprogrammer.android.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Convert android unit DP SP PX
 *
 * @author Junyang HE
 * Created by jhe on 01/03/2018.
 */

public abstract class AndroidUnitConverter {

    /**
     * DP to PX
     * @param dp DP
     * @param context context
     * @return SP
     */
    public static int convertDpToPixels(float dp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * SP to PX
     * @param sp SP
     * @param context context
     * @return Pixel value
     */
    public static int convertSpToPixels(float sp, Context context) {
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
        return px;
    }

    /**
     * DP to SP
     * @param dp DP
     * @param context context
     * @return SP value
     */
    public static int convertDpToSp(float dp, Context context) {
        int sp = (int) (convertDpToPixels(dp, context) / (float) convertSpToPixels(dp, context));
        return sp;
    }

    public static float convertPxToSp(int px, Context context) {
        float sp = px / context.getResources().getDisplayMetrics().scaledDensity;
        return sp;
    }
}
