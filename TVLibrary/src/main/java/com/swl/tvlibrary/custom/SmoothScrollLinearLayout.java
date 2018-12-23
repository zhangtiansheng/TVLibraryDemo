package com.swl.tvlibrary.custom;

import android.content.Context;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.zhy.autolayout.AutoLinearLayout;

/**
 * 解决当该页面使用了外边框动画时，进行页面垂直滚动时，外边框位置不对的问题
 * 页面水平滚动与此类似，也可直接继承自其它容器布局
 * 外边框使用的Anim Bridge请参考VerticalScrollBridge实现
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class SmoothScrollLinearLayout extends AutoLinearLayout {

    private ScrollerCompat mScroller;

    private int duration = 500;

    public int mScrollMarginLeft;

    public int mScrollAbsFinalY;

    public int mScrollDy;

    public SmoothScrollLinearLayout(Context context) {
        super(context);
        init();
    }

    public SmoothScrollLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmoothScrollLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mScroller = ScrollerCompat.create(getContext(), sQuinticInterpolator);
    }

    //调用此方法滚动到目标位置
    public void smoothScrollTo(int fx, int fy) {
        mScrollAbsFinalY = fy;
        int dx = fx - mScroller.getCurrX();
        int dy = mScrollDy = fy - mScroller.getCurrY();
        smoothScrollBy(dx, dy, duration);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollBy(int dx, int dy, int duration) {
        if (dx != 0 || dy != 0) {
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), dx, dy, duration);
            invalidate();
        }
    }

    //调用此方法滚动到目标位置
    public void smoothScrollToVertical(int fy) {
        mScrollAbsFinalY = fy;
        int dy = mScrollDy = fy - mScroller.getCurrY();
        smoothScrollByVertical(dy, duration);
    }

    //调用此方法设置滚动的相对偏移
    public void smoothScrollByVertical(int dy, int duration) {
        if (dy != 0) {
            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, dy, duration);
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mScrollDy = mScrollAbsFinalY - mScroller.getCurrY();
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        } else {
            mScrollDy = 0;
        }
    }

    public void setScrollMarginLeft(int mScrollMarginLeft) {
        this.mScrollMarginLeft = mScrollMarginLeft;
    }

    public int getScrollMarginLeft() {
        return mScrollMarginLeft;
    }

    public int getScrollDy() {
        return mScrollDy;
    }

    private final Interpolator sQuinticInterpolator = new Interpolator() {

        public float getInterpolation(float t) {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        }
    };


}
