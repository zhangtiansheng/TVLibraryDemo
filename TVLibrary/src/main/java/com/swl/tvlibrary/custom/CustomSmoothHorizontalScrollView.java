package com.swl.tvlibrary.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.HorizontalScrollView;

import com.swl.tvlibrary.R;

import java.lang.reflect.Field;

/**
 * 平滑滚动的HorizontalScrollView
 * 可设置平滑滚动时间
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class CustomSmoothHorizontalScrollView extends HorizontalScrollView {

    private final String TAG = "SmoothHorizontalScrollView";
    private FixedSpeedOverScroller scroller;
    private Field mLastScrollField;
    private int mScrollDuration = 400;
    private int scrollDistance;

    public CustomSmoothHorizontalScrollView(Context context) {
        this(context, null, 0);
    }

    public CustomSmoothHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSmoothHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setSmoothScrollingEnabled(true);
        setSoundEffectsEnabled(true);
        initScroller(context, mScrollDuration);
        initLastScrollField();
    }

    private void initScroller(Context context, int duration) {
        try {
            Field field = HorizontalScrollView.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedOverScroller(context);
            field.set(this, scroller);
            scroller.setScrollDuration(duration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initLastScrollField() {
        try {
            mLastScrollField = HorizontalScrollView.class.getDeclaredField("mLastScroll");
            mLastScrollField.setAccessible(true);
            mLastScrollField.set(this, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0)
            return 0;

        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;

        int fadingEdge = this.getResources().getDimensionPixelSize(R.dimen.fading_edge);

        // leave room for left fading edge as long as rect isn't at very left
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }

        // leave room for right fading edge as long as rect isn't at very right
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }

        int scrollXDelta = 0;

        if (rect.right > screenRight && rect.left > screenLeft) {
            // need to move right to get it in view: move right just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.width() > width) {
                // just enough to get screen size chunk on
                scrollXDelta += (rect.left - screenLeft);
            } else {
                // get entire rect at right of screen
                scrollXDelta += (rect.right - screenRight);
            }

            // make sure we aren't scrolling beyond the end of our content
            int right = getChildAt(0).getRight();
            int distanceToRight = right - screenRight;
            scrollXDelta = Math.min(scrollXDelta, distanceToRight);

        } else if (rect.left < screenLeft && rect.right < screenRight) {
            // need to move right to get it in view: move right just enough so
            // that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.width() > width) {
                // screen size chunk
                scrollXDelta -= (screenRight - rect.right);
            } else {
                // entire rect at left
                scrollXDelta -= (screenLeft - rect.left);
            }

            // make sure we aren't scrolling any further than the left our
            // content
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        scrollDistance = scrollXDelta;
        return scrollXDelta;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mLastScrollField != null) {
            try {
                mLastScrollField.set(this, 0);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /**
     * 判断是否需要滚动
     *
     * @return
     */
    public boolean isSmoothScrollX() {
        return scrollDistance != 0 ? true : false;
    }


    /**
     * 获得滚动距离
     *
     * @return
     */
    public int getScrollDistance() {
        return scrollDistance;
    }

    /**
     * 设置滚动时间
     *
     * @param mScrollDuration
     */
    public void setScrollDuration(int mScrollDuration) {
        this.mScrollDuration = mScrollDuration;
        scroller.setScrollDuration(mScrollDuration);
    }


    /**
     * 获得滚动时间
     *
     * @return
     */
    public int getScrollDuration() {
        return mScrollDuration;
    }

}
