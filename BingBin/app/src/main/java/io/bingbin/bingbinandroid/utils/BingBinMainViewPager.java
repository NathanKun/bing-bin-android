package io.bingbin.bingbinandroid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import io.bingbin.bingbinandroid.models.SwipeDirection;

/**
 * @author Junyang HE
 * Created by NathanKun on 2018/2/16.
 */

public class BingBinMainViewPager extends ViewPager {
    private float x1;
    private SwipeDirection direction;

    public BingBinMainViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.direction = SwipeDirection.ALL;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isSwipeAllowed(event) && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return isSwipeAllowed(event) && super.onInterceptTouchEvent(event);
    }

    private boolean isSwipeAllowed(MotionEvent event) {
        if(direction == SwipeDirection.ALL) return true;

        if(direction == SwipeDirection.NONE )//disable any swipe
            return false;

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            x1 = event.getX();
            return true;
        }

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                float diffX = event.getX() - x1;
                if (diffX > -100 && direction == SwipeDirection.RIGHT ) {
                    // swipe from left to right detected
                    return false;
                }else if (diffX < 100 && direction == SwipeDirection.LEFT ) {
                    // swipe from right to left detected
                    return false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return true;
    }

    public void setAllowedSwipeDirection(SwipeDirection direction) {
        this.direction = direction;
    }
}