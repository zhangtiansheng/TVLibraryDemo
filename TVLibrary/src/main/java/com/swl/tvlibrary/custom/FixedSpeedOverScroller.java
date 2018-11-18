package com.swl.tvlibrary.custom;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

/**
 * OverScroller
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class FixedSpeedOverScroller extends OverScroller {

    private int mDuration = 500;

    public FixedSpeedOverScroller(Context context) {
        super(context);
    }

    public FixedSpeedOverScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }

    public void setScrollDuration(int time) {
        mDuration = time;
    }


    public int getScrollDuration() {
        return mDuration;
    }

}
