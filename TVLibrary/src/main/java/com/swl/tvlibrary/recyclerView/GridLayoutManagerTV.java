package com.swl.tvlibrary.recyclerView;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 自定义的GridLayoutManager
 * 配合RecyclerTV一起使用，
 * 解决Recycler使用在TV上的各种焦点问题，无法快速滚动问题，滚动焦点错乱问题
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class GridLayoutManagerTV extends GridLayoutManager {

    public GridLayoutManagerTV(Context context, int spanCount) {
        super(context, spanCount);
    }

    public GridLayoutManagerTV(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public GridLayoutManagerTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
        selectViewByPosition(position);
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
        GridLinearSmoothScroller linearSmoothScroller = new GridLinearSmoothScroller(recyclerView.getContext());
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }


    public PointF computeVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
    }


    /**
     * select position view
     *
     * @param targetPosition
     */
    private void selectViewByPosition(final int targetPosition) {
        Observable.timer(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        View targetView = findViewByPosition(targetPosition);
                        if (targetView != null) {
                            targetView.requestFocus();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * Base class which scrolls to selected view in onStop().
     */
    class GridLinearSmoothScroller extends LinearSmoothScroller {

        public GridLinearSmoothScroller(Context context) {
            super(context);
        }

        @Override
        protected int calculateTimeForScrolling(int dx) {
//            return super.calculateTimeForScrolling(dx);
            return 50;
        }

        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return computeVectorForPosition(targetPosition);
        }

        /**
         * 滑动完成后,让该targetPosition处的item获取焦点
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
