package com.swl.tvlibrary.custom.adapter;


/**
 * 实现TV上获取焦点状态失去焦点状态的适配器item的更新上
 * 包含焦点颜色的更新，文字更新等
 *
 * @author zhangTianSheng 956122936@qq.com
 */
public interface BaseAdapterImpl {

    /**
     * 设置焦点所在位置
     *
     * @param position
     */
    void setSelectPosition(int position);

    /**
     * 设置焦点失去位置
     *
     * @param position
     */
    void setSecondPosition(int position);

}
