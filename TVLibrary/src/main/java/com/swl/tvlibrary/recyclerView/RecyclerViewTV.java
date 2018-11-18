package com.swl.tvlibrary.recyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;

/**
 * RecyclerView TV适配版本.
 * <p>
 * 支持下拉加载更多，解决RecyclerView在tv上的各种问题
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public class RecyclerViewTV extends RecyclerView {

    private OnScroll mOnScroll;
    private OnLoadMoreListener mOnLoadMoreListener;
    private int scrollDistance;

    private long scrollY;

    public RecyclerViewTV(Context context) {
        this(context, null);
    }

    public RecyclerViewTV(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public RecyclerViewTV(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private boolean mSelectedItemCentered = false;
    private int mSelectedItemOffsetStart;
    private int mSelectedItemOffsetEnd;
    private int offset = -1;
    public LayoutManager layoutManager;
    private int mLoadMoreItemOffset = 1;
    private boolean isInterceptLeftEvent;

    private void init(Context context) {
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setHasFixedSize(true);
        setWillNotDraw(true);
        setOverScrollMode(View.OVER_SCROLL_NEVER);
        setChildrenDrawingOrderEnabled(true);

        setClipChildren(false);
        setClipToPadding(false);

        setClickable(false);
        setFocusable(false);
        setFocusableInTouchMode(false);

        /**
         防止RecyclerView刷新时焦点不错乱bug的步骤如下:
         (1)adapter执行setHasStableIds(true)方法
         (2)重写getItemId()方法,让每个view都有各自的id
         (3)RecyclerView的动画必须去掉
         */
        setItemAnimator(null);

        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case SCROLL_STATE_IDLE:
                        if (null != mOnScroll) {
                            mOnScroll.onIdle();
                        }
                        if (null != mOnLoadMoreListener) {
                            if (getLastVisiblePosition() >= getAdapter().getItemCount() - mLoadMoreItemOffset) {
                                mOnLoadMoreListener.onLoadMore();
                            }
                        }
                        break;
                    case SCROLL_STATE_SETTLING:
                        if (null != mOnScroll) {
                            mOnScroll.onSetting();
                        }
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollY += dy;
            }
        });
    }


    public long getTotalScrollY() {
        return scrollY;
    }

    private int getFreeWidth() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }

    private int getFreeHeight() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    public void onChildAttachedToWindow(View child) {
        super.onChildAttachedToWindow(child);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean hasFocus() {
        return super.hasFocus();
    }

    /**
     * recycleView绘制不出来时，需要调用
     */
    public void setRedrawItem() {
        setHasFixedSize(false);
    }

    /**
     * 设置相差多少个item时，触发加载更多
     *
     * @param offset
     */
    public void setLoadMoreItemOffset(int offset) {
        mLoadMoreItemOffset = offset;
    }


    /**
     * 设置是否拦截左键事件，拦截之后，事件处理交给recycleView自己处理，不向上传递
     *
     * @param isIntercept
     */
    public void setInterceptLeftEvent(boolean isIntercept) {
        isInterceptLeftEvent = isIntercept;
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        layoutManager = layout;
    }

    @Override
    public boolean isInTouchMode() {
        // 解决4.4版本抢焦点的问题
        if (Build.VERSION.SDK_INT == 19) {
            return !(hasFocus() && !super.isInTouchMode());
        } else {
            return super.isInTouchMode();
        }
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        if (null != child) {
            if (mSelectedItemCentered) {
                mSelectedItemOffsetStart = !isVertical() ? (getFreeWidth() - child.getWidth()) : (getFreeHeight() - child.getHeight());
                mSelectedItemOffsetStart /= 2;
                mSelectedItemOffsetEnd = mSelectedItemOffsetStart;
            }
        }
        super.requestChildFocus(child, focused);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();
        final int parentRight = getWidth() - getPaddingRight();
        final int parentBottom = getHeight() - getPaddingBottom();

        final int childLeft = child.getLeft() + rect.left;
        final int childTop = child.getTop() + rect.top;

//        final int childLeft = child.getLeft() + rect.left - child.getScrollX();
//        final int childTop = child.getTop() + rect.top - child.getScrollY();

        final int childRight = childLeft + rect.width();
        final int childBottom = childTop + rect.height();

        final int offScreenLeft = Math.min(0, childLeft - parentLeft - mSelectedItemOffsetStart);
        final int offScreenTop = Math.min(0, childTop - parentTop - mSelectedItemOffsetStart);
        final int offScreenRight = Math.max(0, childRight - parentRight + mSelectedItemOffsetEnd);
        final int offScreenBottom = Math.max(0, childBottom - parentBottom + mSelectedItemOffsetEnd);

        final boolean canScrollHorizontal = getLayoutManager().canScrollHorizontally();
        final boolean canScrollVertical = getLayoutManager().canScrollVertically();

        // Favor the "start" layout direction over the end when bringing one side or the other
        // of a large rect into view. If we decide to bring in end because start is already
        // visible, limit the scroll such that start won't go out of bounds.
        final int dx;
        if (canScrollHorizontal) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                dx = offScreenRight != 0 ? offScreenRight
                        : Math.max(offScreenLeft, childRight - parentRight);
            } else {
                dx = offScreenLeft != 0 ? offScreenLeft
                        : Math.min(childLeft - parentLeft, offScreenRight);
            }
        } else {
            dx = 0;
        }

        // Favor bringing the top into view over the bottom. If top is already visible and
        // we should scroll to make bottom visible, make sure top does not go out of bounds.
        final int dy;
        if (canScrollVertical) {
            dy = offScreenTop != 0 ? offScreenTop : Math.min(childTop - parentTop, offScreenBottom);
        } else {
            dy = 0;
        }
        offset = isVertical() ? dy : dx;
        if (dx != 0 || dy != 0) {
            if (immediate) {
                scrollBy(dx, dy);
            } else {
                smoothScrollBy(dx, dy);
            }
            return true;
        }

        return false;
    }

    private int mCurrentKeyDirect;
    private View mCurrentFocusView;

    private Handler mHandler = new Handler();

    private Runnable delayRunnable = new Runnable() {
        @Override
        public void run() {
            View leftView = FocusFinder.getInstance().findNextFocus(RecyclerViewTV.this, mCurrentFocusView, mCurrentKeyDirect);
            if (leftView != null) {
                leftView.requestFocus();
            }
        }
    };


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result = super.dispatchKeyEvent(event);
        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT
                || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP || event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
            mCurrentFocusView = getFocusedChild();
            if (mCurrentFocusView == null) {
                return result;
            } else {
                int dy = 0;
                int dx = 0;
                if (getChildCount() > 0) {
                    View firstView = this.getChildAt(0);
                    dy = firstView.getHeight();
                    dx = firstView.getWidth();
                }

                if (event.getAction() == KeyEvent.ACTION_UP) {
                    return false;
                } else {
                    if (isHorizontalLayoutManger()) {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            View rightView = FocusFinder.getInstance().findNextFocus(this, mCurrentFocusView, View.FOCUS_RIGHT);
                            if (rightView != null) {
                                rightView.requestFocus();
                                return true;
                            } else if (canScrollHorizontally(1)) {
                                this.smoothScrollBy(dx, 0);
                                mCurrentKeyDirect = View.FOCUS_RIGHT;
                                mHandler.removeCallbacks(delayRunnable);
                                mHandler.postDelayed(delayRunnable, 100);
                                return true;
                            }
                        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                            View leftView = FocusFinder.getInstance().findNextFocus(this, mCurrentFocusView, View.FOCUS_LEFT);
                            if (leftView != null) {
                                leftView.requestFocus();
                                return true;
                            } else if (canScrollHorizontally(-1)) {
                                this.smoothScrollBy(-dx, 0);
                                mCurrentKeyDirect = View.FOCUS_LEFT;
                                mHandler.removeCallbacks(delayRunnable);
                                mHandler.postDelayed(delayRunnable, 100);
                                return true;
                            }
                        }
                    } else {
                        if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_DOWN) {
                            View downView = FocusFinder.getInstance().findNextFocus(this, mCurrentFocusView, View.FOCUS_DOWN);
                            if (downView != null) {
                                downView.requestFocus();
                                return true;
                            } else if (canScrollVertically(1)) {
                                this.smoothScrollBy(0, dy);
                                mCurrentKeyDirect = View.FOCUS_DOWN;
                                mHandler.removeCallbacks(delayRunnable);
                                mHandler.postDelayed(delayRunnable, 100);
                                return true;
                            }
                        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_UP) {
                            View upView = FocusFinder.getInstance().findNextFocus(this, mCurrentFocusView, View.FOCUS_UP);
                            if (upView != null) {
                                upView.requestFocus();
                                return true;
                            } else if (canScrollVertically(-1)) {
                                this.smoothScrollBy(0, -dy);
                                mCurrentKeyDirect = View.FOCUS_UP;
                                mHandler.removeCallbacks(delayRunnable);
                                mHandler.postDelayed(delayRunnable, 100);
                                return true;
                            }
                        } else if (isInterceptLeftEvent && event.getKeyCode() == KeyEvent.KEYCODE_DPAD_LEFT) {
                            View leftView = FocusFinder.getInstance().findNextFocus(this, mCurrentFocusView, View.FOCUS_LEFT);
                            if (leftView != null) {
                                leftView.requestFocus();
                                return true;
                            }
                        } else if (event.getKeyCode() == KeyEvent.KEYCODE_DPAD_RIGHT) {
                            //如果右方向的下一个view的position小于等于当前view的，则设置此方向下一个焦点为本身，避免焦点上移。注：子view必须设置id
                            View rightView = FocusFinder.getInstance().findNextFocus(this, mCurrentFocusView, View.FOCUS_RIGHT);
                            if (rightView != null) {
                                int oldPosition = this.getChildLayoutPosition(mCurrentFocusView);
                                int newPosition = this.getChildLayoutPosition(rightView);
                                if (oldPosition >= newPosition) {
                                    mCurrentFocusView.setNextFocusRightId(mCurrentFocusView.getId());
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * 最后的位置.
     */
    public int findLastVisibleItemPosition() {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * findFirstVisibleItemPosition
     */
    public int findFirstVisibleItemPosition() {
        if (layoutManager instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        if (layoutManager instanceof GridLayoutManager) {
            return ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }
        return RecyclerView.NO_POSITION;
    }

    /**
     * 获取选中ITEM的滚动偏移量
     *
     * @return
     */
    public int getSelectedItemScrollOffset() {
        return offset;
    }

    private boolean cannotScrollForwardOrBackward(int value) {
//        return cannotScrollBackward(value) || cannotScrollForward(value);
        return false;
    }

    /**
     * 判断第一个位置，没有移动.
     * getStartWithPadding --> return (mIsVertical ? getPaddingTop() : getPaddingLeft());
     */
    public boolean cannotScrollBackward(int delta) {
        return (getFirstVisiblePosition() == 0 && delta <= 0);
    }

    /**
     * 判断是否达到了最后一个位置，没有再移动了.
     * getEndWithPadding -->  mIsVertical ?  (getHeight() - getPaddingBottom()) :
     * (getWidth() - getPaddingRight());
     */
    public boolean cannotScrollForward(int delta) {
        return ((getFirstVisiblePosition() + getLayoutManager().getChildCount()) == getLayoutManager().getItemCount()) && (delta >= 0);
    }

    @Override
    public int getBaseline() {
        return super.getBaseline();
    }


    @Override
    public void smoothScrollBy(int dx, int dy) {
        if (scrollDistance == 0) {
            super.smoothScrollBy(dx, dy);
        } else {
            if (dy < 0) {
                // 向上
                super.smoothScrollBy(dx, dy);
            } else {
                // 向下
                if (!isScrolling()) {
                    super.smoothScrollBy(dx, scrollDistance);
                }
            }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(delayRunnable);
    }

    public int getSelectedItemOffsetStart() {
        return mSelectedItemOffsetStart;
    }

    public int getSelectedItemOffsetEnd() {
        return mSelectedItemOffsetEnd;
    }

    /**
     * 判断是垂直，还是横向.
     */
    private boolean isVertical() {
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
            return LinearLayoutManager.VERTICAL == llm.getOrientation();
        }
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            return GridLayoutManager.VERTICAL == glm.getOrientation();
        }
        return false;
    }

    /**
     * 设置选中的Item距离开始或结束的偏移量；
     * 与滚动方向有关；
     * 与setSelectedItemAtCentered()方法二选一
     *
     * @param offsetStart
     * @param offsetEnd   从结尾到你移动的位置.
     */
    public void setSelectedItemOffset(int offsetStart, int offsetEnd) {
        setSelectedItemAtCentered(false);
        this.mSelectedItemOffsetStart = offsetStart;
        this.mSelectedItemOffsetEnd = offsetEnd;
    }

    /**
     * 设置选中的Item居中；
     * 与setSelectedItemOffset()方法二选一
     *
     * @param isCentered
     */
    public void setSelectedItemAtCentered(boolean isCentered) {
        this.mSelectedItemCentered = isCentered;
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return super.getChildDrawingOrder(childCount, i);
    }

    public int getFirstVisiblePosition() {
        if (getChildCount() == 0) {
            return 0;
        } else {
            return getChildLayoutPosition(getChildAt(0));
        }
    }

    public int getLastVisiblePosition() {
        final int childCount = getChildCount();
        if (childCount == 0) {
            return 0;
        } else {
            return getChildLayoutPosition(getChildAt(childCount - 1));
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
    }

    /**
     * 判断是否为横向布局
     */
    private boolean isHorizontalLayoutManger() {
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
            return LinearLayoutManager.HORIZONTAL == llm.getOrientation();
        }
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) layoutManager;
            return GridLayoutManager.HORIZONTAL == glm.getOrientation();
        }
        return false;
    }

    public boolean isScrolling() {
        return getScrollState() == SCROLL_STATE_SETTLING;
    }

    public static abstract class OnScroll {
        public abstract void onIdle();

        public abstract void onSetting();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    public void setOnScrollChangeListener(OnScroll onScrollChangeListener) {
        mOnScroll = onScrollChangeListener;
    }

    public void setScrollDistance(int scrollDistance) {
        this.scrollDistance = scrollDistance;
    }


}
