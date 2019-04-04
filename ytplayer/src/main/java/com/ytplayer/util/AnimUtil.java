package com.ytplayer.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;

public class AnimUtil {

    public interface Listener {
        void onAnimationStart();

        void onAnimationEnd();
    }

    public static void resizeView(final View view, int fromWidth, int fromHeight, int toWidth, int toHeight) {
        resizeView(view, fromWidth, fromHeight, toWidth, toHeight, null);
    }

    public static void resizeView(final View view, int fromWidth, int fromHeight, int toWidth, int toHeight, final Listener listener) {
        if (view != null) {
            ValueAnimator heightAnimation = ValueAnimator.ofInt(fromHeight, toHeight);
            heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int val = (Integer) valueAnimator.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.height = val;
                    view.setLayoutParams(layoutParams);
                }
            });

            ValueAnimator widthAnimation = ValueAnimator.ofInt(fromWidth, toWidth);
            widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {
                    int val = (Integer) updatedAnimation.getAnimatedValue();
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    layoutParams.width = val;
                    view.setLayoutParams(layoutParams);
                }
            });
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setDuration(1000);
            animatorSet.playTogether(
                    heightAnimation, widthAnimation);
            widthAnimation.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationStart();
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (listener != null) {
                        listener.onAnimationEnd();
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            widthAnimation.start();
        }
    }

}
