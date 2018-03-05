package com.catprogrammer.android.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Convert android unit DP SP PX
 *
 * @author Junyang HE
 * Created by jhe on 01/03/2018.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class AndroidUnitConverter {

    /**
     * DP to PX
     * @param dp DP
     * @param context context
     * @return SP
     */
    public static int convertDpToPixels(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    /**
     * SP to PX
     * @param sp SP
     * @param context context
     * @return Pixel value
     */
    public static int convertSpToPixels(float sp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * DP to SP
     * @param dp DP
     * @param context context
     * @return SP value
     */
    public static int convertDpToSp(float dp, Context context) {
        return (int) (convertDpToPixels(dp, context) / (float) convertSpToPixels(dp, context));
    }

    public static float convertPxToSp(int px, Context context) {
        return px / context.getResources().getDisplayMetrics().scaledDensity;
    }
}
