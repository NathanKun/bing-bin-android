package io.bingbin.bingbinandroid.utils.GaussianBlur;

import android.graphics.Bitmap;

/**
 * @author Pramod J George
 *
 * @see io.bingbin.bingbinandroid.utils.GaussianBlur.AndroidImage
 * @see <a href="https://stackoverflow.com/questions/14555937/android-programmatically-blur-imageview-drawable">android programmatically blur imageview drawable</a>
 *
 */
public class GaussianBlur implements IAndroidFilter{

    @Override
    public AndroidImage process(AndroidImage imageIn) {
        // TODO Auto-generated method stub
        Bitmap src=imageIn.getImage();
        double[][] GaussianBlurConfig = new double[][] {
                { 1, 2, 1 },
                { 2, 4, 2 },
                { 1, 2, 1 }
        };
        ConvolutionMatrix convMatrix = new ConvolutionMatrix(3);
        convMatrix.applyConfig(GaussianBlurConfig);
        convMatrix.Factor = 200;
        convMatrix.Offset = 0;
        return new AndroidImage(ConvolutionMatrix.computeConvolution3x3(src, convMatrix));
    }

}