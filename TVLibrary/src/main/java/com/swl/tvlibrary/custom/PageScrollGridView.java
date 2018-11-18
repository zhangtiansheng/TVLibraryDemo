package com.swl.tvlibrary.custom;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.AbsListView;

/**
 * 可设置默认选中的位置，且再次获取焦点,自动选择上一次丢失焦点的位置
 * 可一页一页的滚动，一行一行的滚动
 * 平滑滚动版的GridView
 * <p>
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class PageScrollGridView extends MemoryGridView implements AbsListView.OnScrollListener {

    private OnScrollListener lister;
    private int scrollDuration = 350;
    private int itemHeight = 0;
    private boolean dircert = true;
    private boolean isScroll = false;

    public PageScrollGridView(Context paramContext) {
        super(paramContext);
        setOnScrollListener(this);
    }

    public PageScrollGridView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        setOnScrollListener(this);
    }

    public PageScrollGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        setOnScrollListener(this);
    }

    @Override
    public void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
        super.onFocusChanged(paramBoolean, paramInt, paramRect);
    }

    @Override
    public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
        if (isScroll) {
            return true;
        }
        switch (paramKeyEvent.getKeyCode()) {
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (getSelectedItemPosition() % getNumColumns() == 0) {
                    return false;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (getLastVisiblePosition() != getAdapter().getCount() - 1
                        && getSelectedItemPosition() <= getLastVisiblePosition()
                        && getSelectedItemPosition() >= getFirstVisiblePosition() + getNumColumns()) {
                    startScrollDown();
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (getSelectedItemPosition() >= getFirstVisiblePosition()
                        && (getSelectedItemPosition() < getFirstVisiblePosition() + getNumColumns())
                        && getFirstVisiblePosition() != 0) {
                    startScrollUp();
                    return true;
                }
                break;
        }
        return super.onKeyDown(paramInt, paramKeyEvent);
    }

    protected void startScrollUp() {
        isScroll = true;
        dircert = false;
        smooth(-getScrollChildHeight(), scrollDuration);
    }

    protected void startScrollDown() {
        isScroll = true;
        dircert = true;
        smooth(getScrollChildHeight(), scrollDuration);
    }

    protected int getScrollChildHeight() {
        if (itemHeight == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                itemHeight = getChildAt(0).getHeight() + getVerticalSpacing();
            }
        }
        return itemHeight;
    }

    public void smooth(int paramInt1, int paramInt2) {
        smoothScrollBy(paramInt1, paramInt2);
    }

    @Override
    public void onScroll(AbsListView paramAbsListView, int paramInt1, int paramInt2, int paramInt3) {
        if (lister != null) {
            lister.onScroll(paramAbsListView, paramInt1, paramInt2, paramInt3);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView paramAbsListView, int paramInt) {
        if (paramInt == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
            selection();
        }
        if (lister != null) {
            lister.onScrollStateChanged(paramAbsListView, paramInt);
        }
    }

    protected void selection() {
        if (dircert) {
            int position = getNumColumns() + getSelectedItemPosition();
            if (position > -1 + getAdapter().getCount()) {
                position = -1 + getAdapter().getCount();
            }
            if (position >= 0 && getSelectedItemPosition() != position) {
                setSelection(position);
            }
        } else {
            int position = getSelectedItemPosition() - getNumColumns();
            if (position < 0) {
                position = 0;
            }
            if (position >= 0 && getSelectedItemPosition() != position) {
                setSelection(position);
            }
        }
        isScroll = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        return super.onTouchEvent(paramMotionEvent);
    }

    public void setScrollListener(OnScrollListener paramOnScrollListener) {
        lister = paramOnScrollListener;
    }

    public void setScrollDuration(int paramInt) {
        scrollDuration = paramInt;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        super.smoothScrollBy(dx, dy);
    }

    public void setIsScroll(boolean isScroll) {
        this.isScroll = isScroll;
    }

    public boolean getIsScroll() {
        return isScroll;
    }

    @Override
    public boolean isInTouchMode() {
        if (Build.VERSION.SDK_INT == 19) {
            // 解决4.4版本抢焦点的问题
            return !(hasFocus() && !super.isInTouchMode());
        } else {
            return super.isInTouchMode();
        }
    }

}
