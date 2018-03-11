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

public class BingBinBoldTextView extends android.support.v7.widget.AppCompatTextView {
    public BingBinBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BingBinBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BingBinBoldTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "futura_bold.otf");
        setTypeface(tf, Typeface.NORMAL);
    }
}
