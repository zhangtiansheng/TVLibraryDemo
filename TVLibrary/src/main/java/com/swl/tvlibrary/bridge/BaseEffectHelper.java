package com.swl.tvlibrary.bridge;


/**
 * 边框位置，大小校准基类
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public abstract class BaseEffectHelper {

    /**
     * 默认第一次做矫正，其他任何时候不做矫正
     */
    protected boolean isCalibration = true;

    /**
     * 默认第一次飞框的时候影藏边框，动画结束时显示边框
     */
    protected boolean isHideFirstBridge = true;

    protected int mFinalWidth, mFinalHeight;
    protected int mFinalX, mFinalY;

    /**
     * 设置边框宽高
     *
     * @param w
     * @param h
     */
    public void setFocusViewSize(int w, int h) {
        mFinalWidth = w;
        mFinalHeight = h;
    }

    /**
     * 设置边框位置
     *
     * @param x
     * @param y
     */
    public void setFocusViewLocation(int x, int y) {
        mFinalX = x;
        mFinalY = y;
    }

    /**
     * 边框矫正一次
     */
    public void setCalibration() {
        isCalibration = true;
    }

    /**
     * 边框飞行过程中影藏一次
     */
    public void setHideFirstBridge() {
        isHideFirstBridge = true;
    }


    /**
     * 边框校准
     */
    public abstract void calibrationBorder();

}
