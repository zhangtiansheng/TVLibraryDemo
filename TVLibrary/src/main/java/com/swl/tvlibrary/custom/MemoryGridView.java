package com.swl.tvlibrary.custom;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

/**
 * 可设置默认选中的位置，且再次获取焦点,自动选择上一次丢失焦点的位置的GridView
 * <p>
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class MemoryGridView extends GridView {

    private boolean isMemoryFocus = true;
    private int lastSelectPosition = -1;
    private View lastSelectView = null;

    public MemoryGridView(Context paramContext) {
        super(paramContext);
    }

    public MemoryGridView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MemoryGridView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    @Override
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener listener) {
        super.setOnItemSelectedListener(listener);
    }

    @Override
    public void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect) {
        lastSelectPosition = getSelectedItemPosition();
        lastSelectView = getSelectedView();
        super.onFocusChanged(paramBoolean, paramInt, paramRect);
        // 适配4.4版本无法记忆上次选中位置的问题
        if (lastSelectView == null || lastSelectPosition == -1) {
            lastSelectPosition = getSelectedItemPosition();
            lastSelectView = getSelectedView();
        }
        if (isMemoryFocus) {
            if (paramBoolean) {
                setSelection(lastSelectPosition);
                if (getOnItemSelectedListener() != null) {
                    getOnItemSelectedListener().onItemSelected(this, lastSelectView, lastSelectPosition, getSelectedItemId());
                }
            }
        }
    }

    public void setIsMemoryFocus(boolean paramBoolean) {
        isMemoryFocus = paramBoolean;
    }

    public View getLastSelectView() {
        return lastSelectView;
    }

    public int getLastSelectItemPosition() {
        return lastSelectPosition;
    }

    public void setLastSelectView(View paramView) {
        lastSelectView = paramView;
    }

    public void setLastSelectItemPosition(int paramInt) {
        lastSelectPosition = paramInt > 0 ? paramInt : 0;
    }

}
