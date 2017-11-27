package io.bingbin.bingbinandroid.utils.GaussianBlur;

/**
 * @author Pramod J George
 *
 * @see io.bingbin.bingbinandroid.utils.GaussianBlur.AndroidImage
 * @see <a href="https://stackoverflow.com/questions/14555937/android-programmatically-blur-imageview-drawable">android programmatically blur imageview drawable</a>
 *
 */
public interface IAndroidFilter {

    public AndroidImage process(AndroidImage imageIn);
}

