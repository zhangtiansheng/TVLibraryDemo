package com.swl.tvlibrary.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.swl.tvlibrary.custom.adapter.BaseAdapterImpl;

/**
 * 可设置默认选中的位置，且再次获取焦点,自动选择上一次丢失焦点的位置的ListView
 * <p>
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class MemoryListView extends ListView {

    private int selectPosition = -1;

    public MemoryListView(Context context) {
        super(context);
    }

    public MemoryListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MemoryListView(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * 设置ListView自动选中position，且不会触发onItemSelect事件
     *
     * @param selectPosition
     */
    @Override
    public void setSelection(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    /**
     * 设置ListView选中position，且会触发onItemSelect事件
     *
     * @param selectPosition
     */
    public void setSelectionTriggerItemSelect(int selectPosition) {
        super.setSelection(selectPosition);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        int lastSelectItem = getSelectedItemPosition();
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!(getAdapter() instanceof BaseAdapterImpl)) {
            return;
        }
        View localView = getSelectedView();
        int j = 0;
        if (localView != null) {
            j = (int) localView.getY();
        }
        BaseAdapterImpl adapter = (BaseAdapterImpl) getAdapter();
        if (gainFocus) {
            if (selectPosition > 0) {
                setSelectionFromTop(selectPosition, 0);
                adapter.setSecondPosition(-1);
                selectPosition = -1;
            } else {
                setSelectionFromTop(lastSelectItem, j);
                adapter.setSecondPosition(-1);
            }
        } else {
            adapter.setSecondPosition(lastSelectItem);
        }
    }
}
