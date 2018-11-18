package com.swl.tvlibrary.recyclerView;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义的GridLayoutManager
 * 配合RecyclerTV一起使用，
 * 解决Recycler使用在TV上的各种焦点问题，无法快速滚动问题，滚动焦点错乱问题
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class LinearLayoutManagerTV extends LinearLayoutManager {

    public LinearLayoutManagerTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public LinearLayoutManagerTV(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerTV(Context context) {
        super(context);
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
    }

    /**
     * 解决快速长按焦点丢失问题.
     *
     * @param focused
     * @param focusDirection
     * @param recycler
     * @param state
     * @return
     */
    @Override
    public View onFocusSearchFailed(View focused, int focusDirection, RecyclerView.Recycler recycler, RecyclerView.State state) {
        View nextFocus = super.onFocusSearchFailed(focused, focusDirection, recycler, state);
        return null;
    }

    /**
     * RecyclerView的smoothScrollToPosition方法最终会执行smoothScrollToPosition
     *
     * @param recyclerView
     * @param state
     * @param position
     */
    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        TvLinearSmoothScroller linearSmoothScroller = new TvLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }


    public PointF computeVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
    }


    /**
     * Base class which scrolls to selected view in onStop().
     */
    class TvLinearSmoothScroller extends LinearSmoothScroller {

        public TvLinearSmoothScroller(Context context) {
            super(context);
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return computeVectorForPosition(targetPosition);
        }

        /**
         * 滑动完成后,让该targetPosition 处的item获取焦点
         */
        @Override
        protected void onStop() {
            super.onStop();
            View targetView = findViewByPosition(getTargetPosition());
            if (targetView != null) {
                targetView.requestFocus();
            }
        }
    }

}
