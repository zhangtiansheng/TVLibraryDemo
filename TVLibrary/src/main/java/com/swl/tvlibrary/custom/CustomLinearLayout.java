package com.swl.tvlibrary.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhy.autolayout.AutoLinearLayout;

/**
 * 自动选择上一次丢失焦点的位置，onFocus可直接用于数据加载，防止重复触发
 * 用于LinearLayout内所有可点击view的焦点事件，点击事件的统一管理
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class CustomLinearLayout extends AutoLinearLayout implements View.OnFocusChangeListener,
        View.OnClickListener {

    private OnItemFocusChangeListener mOnItemFocusChangeListener;

    private OnItemClickListener mOnItemClickListener;

    private View mHasFocusView, mLoseFocusView;

    private int mOnKeyDirection = -1;

    private int viewDirection = View.FOCUS_RIGHT;

    public CustomLinearLayout(Context context) {
        this(context, null);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    if (mLoseFocusView != null) {
                        mLoseFocusView.requestFocus();
                    }
                    setFocusable(false);
                }
            }
        });
    }

    /**
     * 设置item
     *
     * @param args
     */
    public void setChildView(View... args) {
        for (View view : args) {
            view.setOnFocusChangeListener(this);
            view.setOnClickListener(this);
        }
    }


    public void setViewDirection(int direction) {
        viewDirection = direction;
    }


    public void setOnItemFocusChangeListener(OnItemFocusChangeListener listener) {
        mOnItemFocusChangeListener = listener;

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public View focusSearch(View focused, int direction) {
        mOnKeyDirection = direction;
        return super.focusSearch(focused, direction);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        if (hasFocus && v == mLoseFocusView) {
            if (mOnItemFocusChangeListener != null) {
                mOnItemFocusChangeListener.onLayoutFocusChange(v, true);
            }
        } else if (!hasFocus && mHasFocusView == v && mOnKeyDirection == viewDirection) {

            setFocusable(true);
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

            if (mOnItemFocusChangeListener != null) {
                mOnItemFocusChangeListener.onLayoutFocusChange(v, false);
            }
        } else {
            if (mOnItemFocusChangeListener != null) {
                mOnItemFocusChangeListener.onItemFocusChange(v, hasFocus);
            }
        }

        if (hasFocus) {
            mHasFocusView = v;
        } else {
            mLoseFocusView = v;
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v);
        }
    }


    public interface OnItemFocusChangeListener {

        /**
         * LinearLayout内所有可点击view的焦点事件
         *
         * @param v        view
         * @param hasFocus view hasFocus
         */
        void onItemFocusChange(View v, boolean hasFocus);

        /**
         * 整个LinearLayout的焦点事件
         *
         * @param v        view
         * @param hasFocus view hasFocus
         */
        void onLayoutFocusChange(View v, boolean hasFocus);
    }

    public interface OnItemClickListener {

        /**
         * LinearLayout内所有可点击view的点击事件
         *
         * @param view
         */
        void onItemClick(View view);
    }
}

