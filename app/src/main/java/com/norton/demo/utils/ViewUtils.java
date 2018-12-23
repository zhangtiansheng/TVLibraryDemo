package com.norton.demo.utils;

import android.content.Context;


/**
 * Created by yangsong on 2015/11/18.
 */
public class ViewUtils {

    /**
     * dip to Pixels
     *
     * @param context
     * @param dip
     * @return
     */
    public static int DipToPixels(Context context, float dip) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float valueDips = dip;
        int valuePixels = (int) (valueDips * SCALE + 0.5f);
        return valuePixels;

    }


    /**
     * px to dp
     *
     * @param context
     * @param Pixels
     * @return
     */
    public static float PixelsToDip(Context context, float Pixels) {
        final float SCALE = context.getResources().getDisplayMetrics().density;
        float dips = Pixels / SCALE;
        return dips;
    }


}
