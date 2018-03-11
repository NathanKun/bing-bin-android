package io.bingbin.bingbinandroid.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * @author Junyang HE
 * Created by Junyang HE on 2018/3/11.
 */

public class BingBinButton extends android.support.v7.widget.AppCompatButton {
    public BingBinButton(Context context) {
        super(context);
    }

    public BingBinButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BingBinButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "futura_medium.ttf");
        setTypeface(tf, Typeface.NORMAL);
    }
}
