package io.bingbin.bingbinandroid.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Custom text view with futura medium font
 *
 * @author Junyang HE
 * Created by Junyang HE on 2018/3/4.
 */

public class BingBinTextView extends android.support.v7.widget.AppCompatTextView {
    public BingBinTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BingBinTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BingBinTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "futura_medium.ttf");
        setTypeface(tf, Typeface.NORMAL);

    }
}
