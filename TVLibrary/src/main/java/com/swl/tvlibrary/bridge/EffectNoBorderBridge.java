package com.swl.tvlibrary.bridge;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * 无边框，仅有放缩特效
 * 无需设置边框resource，无需调用隐藏边框方法
 * <p>
 *
 * @author zhangTianSheng 956122936@qq.com
 */

public class EffectNoBorderBridge extends OpenEffectBridge {

    protected AnimatorSet mCurrentAnimatorSet;

    @Override
    public void clearAnimator() {

    }

    /**
     * 设置背景，边框不使用绘制.
     */
    @Override
    public void setUpRectResource(int resId) {

    }

    @Override
    public void setUpRectDrawable(Drawable upRectDrawable) {

    }

    @Override
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (!isAnimEnabled()) {
            return;
        }
        if (oldFocusView != null) {
            recoverScaleAnimation(oldFocusView, scaleX, scaleY);
        }
    }

    @Override
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        if (!isAnimEnabled())
            return;
        if (focusView != null) {
            /**
             * 我这里重写了onFocusView. <br>
             * 并且交换了位置. <br>
             * 你可以写自己的动画效果. <br>
             */
            runScaleAnimation(focusView, scaleX, scaleY);
        }
    }

    /**
     * 弹动效果，放大再缩小
     */
    public void runScaleAnimation(View focusView, float scaleX, float scaleY) {
        if (mCurrentAnimatorSet != null) {
            mCurrentAnimatorSet.cancel();
        }
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(focusView, "scaleX", 1.0f, scaleX);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(focusView, "scaleY", 1.0f, scaleY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(300);
        animatorSet.start();
        mCurrentAnimatorSet = animatorSet;
    }

    public void recoverScaleAnimation(View focusView, float backScaleX, float backScaleY) {
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(focusView, "scaleX", backScaleX);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(focusView, "scaleY", backScaleY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(300);
        animatorSet.start();
    }


    /**
     * 重写该函数，<br>
     * 不进行绘制 边框和阴影.
     */
    @Override
    public boolean onDrawMainUpView(Canvas canvas) {
        return false;
    }

}
