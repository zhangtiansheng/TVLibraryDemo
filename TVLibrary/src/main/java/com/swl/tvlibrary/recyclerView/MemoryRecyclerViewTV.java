package com.swl.tvlibrary.recyclerView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

/**
 * 可设置默认选中的位置
 * 当RecyclerViewTV获取焦点时,默认选中上次的位置
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class MemoryRecyclerViewTV extends RecyclerViewTV {

    public View mLastItemView;

    public int mKeyDirection = KeyEvent.KEYCODE_DPAD_RIGHT;

    public boolean isSelectedDefaultIndex;

    public int mDefaultIndex;

    private AdapterDataObservable adapterDataObservable;

    public MemoryRecyclerViewTV(Context context) {
        this(context, null);
    }

    public MemoryRecyclerViewTV(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MemoryRecyclerViewTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        adapterDataObservable = new AdapterDataObservable();
    }


    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        adapter.registerAdapterDataObserver(adapterDataObservable);
    }

    @Override
    protected void onDetachedFromWindow() {
        getAdapter().unregisterAdapterDataObserver(adapterDataObservable);
        super.onDetachedFromWindow();
    }

    /**
     * set recyclerView direction
     *
     * @param keyDirection
     */
    public void setKeyDirection(int keyDirection) {
        this.mKeyDirection = keyDirection;
    }


    /**
     * @param defaultIndex
     */
    public void setDefaultIndex(int defaultIndex) {
        isSelectedDefaultIndex = true;
        mDefaultIndex = defaultIndex;
    }


    @Override
    public void requestChildFocus(View child, View focused) {
        mLastItemView = child;
        super.requestChildFocus(child, focused);
    }


    /**
     * clear last memory focus item
     *
     * @return
     */
    public void clearFocusCache() {
        mLastItemView = null;
    }


    /**
     * get request selected last focus view
     * <p>
     * 目前只能适用于右键直接到recycleView的情况
     *
     * @param keyCode
     * @param event
     * @return
     */
    public boolean dispatchKeyDown(View currentView, int keyCode, KeyEvent event) {
        if (keyCode == mKeyDirection && currentView.hasFocus() && !hasFocus()) {
            if (mLastItemView != null) {
                mLastItemView.requestFocus();
                return true;
            } else if (isSelectedDefaultIndex && getChildAt(mDefaultIndex) != null) {
                getChildAt(mDefaultIndex).requestFocus();
                return true;
            }
        }
        return false;
    }


    class AdapterDataObservable extends AdapterDataObserver {

        @Override
        public void onChanged() {
            super.onChanged();
            mLastItemView = null;
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            mLastItemView = null;
        }

    }


}
