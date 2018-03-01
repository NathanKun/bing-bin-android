package com.catprogrammer.android.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;

/**
 *
 * Animation util
 *
 * @author Junyang HE
 * Created by Junyang HE on 22/02/2018.
 */
public abstract class AnimationUtil {


    public static void revealView(View view, boolean show) {
        revealView(view, show, 1000, null);
    }

    public static void revealView(View view, boolean show, Runnable onAnimationEnd) {
        revealView(view, show, 1000, onAnimationEnd);
    }

    public static void revealView(View view, boolean show, int duration) {
        revealView(view, show, duration, null);
    }

    // show or hide a view
    public static void revealView(View view, boolean show, int duration, Runnable onAnimationEnd) {
        if(show) {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);
        } else {
            view.setAlpha(1f);
            view.setVisibility(View.VISIBLE);
        }

        ViewPropertyAnimator animator = view.animate()
                .alpha(show ? 1f : 0f)
                .setDuration(duration);

        if(null != onAnimationEnd) {
            animator.setListener(new Animator.AnimatorListener() {

                @Override
                public void onAnimationEnd(Animator animator) {
                    onAnimationEnd.run();
                }

                @Override
                public void onAnimationStart(Animator animator) {}
                @Override
                public void onAnimationCancel(Animator animator) {}
                @Override
                public void onAnimationRepeat(Animator animator) {}
            });
        }

        animator.start();

        if(!show) (new Handler()).postDelayed(() -> view.setVisibility(View.GONE), duration);
    }

}