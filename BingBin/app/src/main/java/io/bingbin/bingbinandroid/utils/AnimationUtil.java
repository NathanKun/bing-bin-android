package io.bingbin.bingbinandroid.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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

    // show or hide a view
    public static void revealView(View view, boolean show, Runnable onAnimationEnd) {
        if(show) {
            view.setAlpha(0f);
            view.setVisibility(View.VISIBLE);
        }

        ViewPropertyAnimator animator = view.animate()
                .alpha(show ? 1f : 0f)
                .setDuration(1000);

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

        if(!show) view.setVisibility(View.GONE);
    }

}