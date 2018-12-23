package com.swl.tvlibrary.bridge;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.swl.tvlibrary.view.MainUpView;

/**
 * 自定义Anim Bridge DEMO. <br>
 * 如果你想实现自己不同风格的东西， <br>
 * 继承 BaseAnimBridge 重写几个函数吧. <br>
 * 后续将推出更多风格的 Anim Bridge. <br>
 * 使用方法 MainUpView.setAnimBridge(new OpenEffectBridge()); <br>
 * 解决边框位置不对的问题
 */
public class OpenEffectBridge extends BaseEffectBridgeWrapper {

    public static final int DEFAULT_TRAN_DUR_ANIM = 250;
    private int mTranDurAnimTime = DEFAULT_TRAN_DUR_ANIM;
    private AnimatorSet mCurrentAnimatorSet;
    private boolean isInDraw = false;
    private boolean mIsHide = false;
    private boolean mIsHideAnimationProcess = false;
    private boolean mAnimEnabled = true;
    private boolean isDrawUpRect = true;
    private View mFocusView;
    private NewAnimatorListener mNewAnimatorListener;

    @Override
    public void onInitBridge(MainUpView view) {
        super.onInitBridge(view);
        /**
         * 防止边框第一次出现,<br>
         * 从另一个地方飘过来的问题.<br>
         */
        view.setVisibility(View.INVISIBLE);
    }

    /**
     * 设置是否移动边框在最下层(绘制的层次). <br>
     * true : 移动边框在最上层. 反之否. <br>
     */
    public void setDrawUpRectEnabled(boolean isDrawUpRect) {
        this.isDrawUpRect = isDrawUpRect;
        getMainUpView().invalidate();
    }

    /**
     * 控件动画时间.
     */
    public void setTranDurAnimTime(int time) {
        mTranDurAnimTime = time;
    }

    public int getTranDurAnimTime() {
        return this.mTranDurAnimTime;
    }

    /**
     * 让动画失效.
     */
    public void setAnimEnabled(boolean animEnabled) {
        this.mAnimEnabled = animEnabled;
    }

    public void clearAnimator() {
        mCurrentAnimatorSet.end();
    }

    public boolean isAnimEnabled() {
        return this.mAnimEnabled;
    }

    /**
     * 隐藏移动的边框.
     */
    public void setVisibleWidget(boolean isHide) {
        this.mIsHide = isHide;
        getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
    }

    public boolean isVisibleWidget() {
        return this.mIsHide;
    }

    public interface NewAnimatorListener {
        public void onAnimationStart(OpenEffectBridge bridge, View view, Animator animation);

        public void onAnimationEnd(OpenEffectBridge bridge, View view, Animator animation);
    }

    /**
     * 监听动画的回调.
     */
    public void setOnAnimatorListener(NewAnimatorListener newAnimatorListener) {
        mNewAnimatorListener = newAnimatorListener;
    }

    public NewAnimatorListener getNewAnimatorListener() {
        return mNewAnimatorListener;
    }

    @Override
    public void onOldFocusView(View oldFocusView, float scaleX, float scaleY) {
        if (!mAnimEnabled)
            return;
        if (oldFocusView != null) {
            oldFocusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start();
        }
    }

    @Override
    public void onFocusView(View focusView, float scaleX, float scaleY) {
        mFocusView = focusView;
        if (!mAnimEnabled)
            return;
        if (focusView != null) {
            focusView.animate().scaleX(scaleX).scaleY(scaleY).setDuration(mTranDurAnimTime).start(); // 放大焦点VIEW的动画.
            runTranslateAnimation(focusView, scaleX, scaleY); // 移动边框的动画。
        }
    }

    /**
     * 移动边框的动画处理函数.
     */
    @Override
    public void flyWhiteBorder(final View focusView, View moveView, float scaleX, float scaleY) {

        int newWidth = 0;
        int newHeight = 0;
        int oldWidth = 0;
        int oldHeight = 0;

        float newX = 0;
        float newY = 0;

        if (focusView != null) {
            mFinalWidth = newWidth = (int) (Math.rint(focusView.getMeasuredWidth() * scaleX));
            mFinalHeight = newHeight = (int) (Math.rint(focusView.getMeasuredHeight() * scaleY));
            oldWidth = moveView.getMeasuredWidth();
            oldHeight = moveView.getMeasuredHeight();
            Rect fromRect = findLocationWithView(moveView); // 获取moveView在屏幕上的位置.
            Rect toRect = findLocationWithView(focusView);  // 获取focusView在屏幕上的位置.
            int x = toRect.left - fromRect.left;
            int y = toRect.top - fromRect.top;

            if (null != focusView.getParent()) {
                View parent = (View) focusView.getParent();
                x += parent.getTranslationX();
                y += parent.getTranslationY();
            }

            newX = x - Math.abs(focusView.getMeasuredWidth() - newWidth) / 2;
            newY = y - Math.abs(focusView.getMeasuredHeight() - newHeight) / 2;
        }

        // 取消之前的动画.
        if (mCurrentAnimatorSet != null)
            mCurrentAnimatorSet.cancel();

        ObjectAnimator transAnimatorX = ObjectAnimator.ofFloat(moveView, "translationX", newX);
        ObjectAnimator transAnimatorY = ObjectAnimator.ofFloat(moveView, "translationY", newY);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "width", oldWidth,
                newWidth);
        ObjectAnimator scaleYAnimator = ObjectAnimator.ofInt(new ScaleView(moveView), "height", oldHeight,
                newHeight);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(transAnimatorX, transAnimatorY, scaleXAnimator, scaleYAnimator);
        mAnimatorSet.setInterpolator(new DecelerateInterpolator(1));
        mAnimatorSet.setDuration(getTranDurAnimTime());
        mAnimatorSet.addListener(new AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!isDrawUpRect)
                    isInDraw = false;
                if (mIsHide) {
                    getMainUpView().setVisibility(View.INVISIBLE);
                }
                if (isHideFirstBridge) {
                    isHideFirstBridge = false;
                    getMainUpView().setVisibility(View.GONE);
                }
                if (mIsHideAnimationProcess) {
                    getMainUpView().setVisibility(View.INVISIBLE);
                }
                if (mNewAnimatorListener != null)
                    mNewAnimatorListener.onAnimationStart(OpenEffectBridge.this, focusView, animation);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (!isDrawUpRect)
                    isInDraw = false;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isDrawUpRect)
                    isInDraw = true;
                getMainUpView().setVisibility(mIsHide ? View.INVISIBLE : View.VISIBLE);
                if (mNewAnimatorListener != null)
                    mNewAnimatorListener.onAnimationEnd(OpenEffectBridge.this, focusView, animation);

                calibrationBorder();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (!isDrawUpRect)
                    isInDraw = false;
            }
        });
        mAnimatorSet.start();
        mCurrentAnimatorSet = mAnimatorSet;
    }

    /**
     * 重寫了繪製的函數.
     */
    @Override
    public boolean onDrawMainUpView(Canvas canvas) {
        canvas.save();
        if (!isDrawUpRect) {
            // 绘制阴影.
            onDrawShadow(canvas);
            // 绘制最上层的边框.
            onDrawUpRect(canvas);
        }
        // 绘制焦点子控件.
        if (mFocusView != null && (!isDrawUpRect && isInDraw)) {
            onDrawFocusView(canvas);
        }
        //
        if (isDrawUpRect) {
            // 绘制阴影.
            onDrawShadow(canvas);
            // 绘制最上层的边框.
            onDrawUpRect(canvas);
        }
        canvas.restore();
        return true;
    }

    public void onDrawFocusView(Canvas canvas) {
        View view = mFocusView;
        canvas.save();
        float scaleX = (float) (getMainUpView().getWidth()) / (float) view.getWidth();
        float scaleY = (float) (getMainUpView().getHeight()) / (float) view.getHeight();
        canvas.scale(scaleX, scaleY);
        view.draw(canvas);
        canvas.restore();
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
