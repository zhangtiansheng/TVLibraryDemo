package com.swl.tvlibrary.customBridge;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import com.swl.tvlibrary.bridge.EffectNoDrawBridge;
import com.swl.tvlibrary.custom.SmoothScrollLinearLayout;

/**
 * 用于一个页面的垂直布局，解决当页面垂直滚动时，边框位置不对的问题
 *
 *@author zhangTianSheng 956122936@qq.com
 */
public class VerticalScrollBridge extends EffectNoDrawBridge {

    private boolean mIsHideBorder = false;
    private boolean mIsHideAnimationProcess = false;

    private AnimatorSet mAnimatorSet;
    private int duration = 300;

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
            runTranslateAnimation(focusView, scaleX, scaleY);
            runScaleAnimation(focusView, scaleX, scaleY, 1.05f, 1.05f);
        }
    }

    /**
     * 弹动效果，放大再缩小
     */
    public void runScaleAnimation(View focusView, float scaleX, float scaleY, float endScaleX, float endScaleY) {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(focusView, "scaleX", 1f, scaleX, endScaleX);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(focusView, "scaleY", 1f, scaleY, endScaleY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(duration);
        animatorSet.start();
        mAnimatorSet = animatorSet;
    }

    public void recoverScaleAnimation(View focusView, float backScaleX, float backScaleY) {
        ObjectAnimator scaleAnimatorX = ObjectAnimator.ofFloat(focusView, "scaleX", backScaleX);
        ObjectAnimator scaleAnimatorY = ObjectAnimator.ofFloat(focusView, "scaleY", backScaleY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleAnimatorX, scaleAnimatorY);
        animatorSet.setDuration(duration);
        animatorSet.start();
    }

    /**
     * 重写边框移动函数.
     */
    @Override
    public void flyWhiteBorder(final View focusView, final View moveView, float scaleX, float scaleY) {
        final RectF paddingRect = getDrawUpRect();
        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;

        int newX = 0;
        int newY = 0;

        // 取消之前的动画.
        if (mCurrentAnimatorSet != null) {
            mCurrentAnimatorSet.cancel();
        }

        if (focusView != null) {
            newWidth = (int) (focusView.getMeasuredWidth() * 1.05f);
            newHeight = (int) (focusView.getMeasuredHeight() * 1.05f);
            oldWidth = moveView.getMeasuredWidth();
            oldHeight = moveView.getMeasuredHeight();
            Rect fromRect = findLocationWithView(moveView);
            Rect toRect = findLocationWithView(focusView);
            int x = toRect.left - fromRect.left - ((int) Math.rint(paddingRect.left));
            int y = toRect.top - fromRect.top - ((int) Math.rint(paddingRect.top));
            newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;

            mFinalWidth = newWidth += ((int) Math.rint(paddingRect.right) + (int) Math.rint(paddingRect.left));
            mFinalHeight = newHeight += ((int) Math.rint(paddingRect.bottom) + (int) Math.rint(paddingRect.top));

            ViewParent parent = focusView.getParent();
            if (parent != null) {
                if (parent.getParent() instanceof SmoothScrollLinearLayout) {
                    newY = newY - ((SmoothScrollLinearLayout) parent.getParent()).getScrollDy();
                    newX = newX + ((SmoothScrollLinearLayout) parent.getParent()).getScrollMarginLeft();
                } else if (parent.getParent().getParent() instanceof SmoothScrollLinearLayout) {
                    newY = newY - ((SmoothScrollLinearLayout) parent.getParent().getParent()).getScrollDy();
                    newX = newX + ((SmoothScrollLinearLayout) parent.getParent().getParent()).getScrollMarginLeft();
                }
            }
        }

        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
                newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
                newHeight);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1.0f));
        mAnimatorSet.setDuration(duration);
        mAnimatorSet.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mIsHideAnimationProcess) {
                    getMainUpView().setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                getMainUpView().setVisibility(isVisibleWidget() ? View.GONE : View.VISIBLE);
                //                calibrationBorder();
                alignBorderEveryTime();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        mAnimatorSet.start();
        mCurrentAnimatorSet = mAnimatorSet;
    }

    /**
     * 隐藏移动的边框.
     */
    public void setVisibleWidget(boolean isHide) {
        this.mIsHideBorder = isHide;
        getMainUpView().setVisibility(mIsHideBorder ? View.INVISIBLE : View.VISIBLE);
    }

    public boolean isVisibleWidget() {
        return this.mIsHideBorder;
    }

    /**
     * 隐藏动画移动过程
     *
     * @param isHide
     */
    public void setHideAnimationProcess(boolean isHide) {
        this.mIsHideAnimationProcess = isHide;
    }
}
