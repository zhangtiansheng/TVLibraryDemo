package com.swl.tvlibrary.custom.scaleView;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.swl.tvlibrary.R;
import com.zhy.autolayout.AutoLinearLayout;


/**
 * 自动获取焦点时View放大,失去焦点时缩小的布局
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class ZoomLinearLayout extends AutoLinearLayout {

    private Animation mAnimFocus, mAnimUnFocus;

    public ZoomLinearLayout(Context context) {
        this(context, null);
    }

    public ZoomLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFocusable(true);
        setFocusableInTouchMode(true);
        mAnimFocus = AnimationUtils.loadAnimation(context, R.anim.scale_big);
        mAnimUnFocus = AnimationUtils.loadAnimation(context, R.anim.scale_normal);
        mAnimFocus.setFillAfter(true);
        mAnimUnFocus.setFillAfter(true);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            bringToFront();
            getRootView().requestLayout();
            getRootView().invalidate();
            startAnimation(mAnimFocus);
        } else {
            startAnimation(mAnimUnFocus);
        }
    }
}
