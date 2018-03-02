package io.bingbin.bingbinandroid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * ListView that can listen click event beside items
 *
 * Ref: https://stackoverflow.com/questions/18523241/how-can-you-listen-to-click-events-on-a-listview-that-occur-outside-of-the-list
 * @author Torsten Ojaperv
 */

public class WholeListClickListenableListView extends ListView {

    private OnNoItemClickListener mOnNoItemClickListener;

    public interface OnNoItemClickListener {
        void onNoItemClicked();
    }

    public WholeListClickListenableListView(Context context) {
        super(context);
    }

    public WholeListClickListenableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WholeListClickListenableListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //check whether the touch hit any elements INCLUDING ListView footer
        if (pointToPosition((int) (ev.getX() * ev.getXPrecision()),
                (int) (ev.getY() * ev.getYPrecision())) == -1 && ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mOnNoItemClickListener != null) {
                mOnNoItemClickListener.onNoItemClicked();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void setOnNoItemClickListener(OnNoItemClickListener listener) {
        mOnNoItemClickListener = listener;
    }

}