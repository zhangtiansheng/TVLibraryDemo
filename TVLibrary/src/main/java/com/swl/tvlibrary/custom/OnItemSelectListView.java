package com.swl.tvlibrary.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

/**
 * 修复ListView被选中无法触发Selected事件
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class OnItemSelectListView extends ListView implements View.OnFocusChangeListener {

    private OnItemSelectedListener mOnItemSelectedListener;

    public OnItemSelectListView(Context context) {
        super(context);
        setOnFocusChangeListener(this);
    }

    public OnItemSelectListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOnFocusChangeListener(this);
    }

    public OnItemSelectListView(Context context, AttributeSet attributeSet, int style) {
        super(context, attributeSet, style);
        setOnFocusChangeListener(this);
    }

    @Override
    public void setOnItemSelectedListener(@Nullable OnItemSelectedListener listener) {
        mOnItemSelectedListener = listener;
        super.setOnItemSelectedListener(listener);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (mOnItemSelectedListener != null) {
                mOnItemSelectedListener.onItemSelected(this, getSelectedView(), getSelectedItemPosition(), getSelectedItemId());
            }
        }
    }
}
