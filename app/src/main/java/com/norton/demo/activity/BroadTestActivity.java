package com.norton.demo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.norton.demo.R;
import com.norton.demo.utils.ViewUtils;
import com.norton.demo.view.PosterView;
import com.swl.tvlibrary.bridge.EffectNoBorderBridge;
import com.swl.tvlibrary.bridge.EffectNoDrawBridge;
import com.swl.tvlibrary.view.MainUpView;

/**
 * Created by admin on 2018/12/20.
 */

public class BroadTestActivity extends Activity {

    private Context context = this;
    private MainUpView mainUpView;
    private PosterView posterView_1, posterView_2, posterView_3, posterView_4, posterView_5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broad_test);

        mainUpView = (MainUpView) findViewById(R.id.mainUpView);

        posterView_1 = ((PosterView) findViewById(R.id.posterView_1));
        posterView_2 = ((PosterView) findViewById(R.id.posterView_2));
        posterView_3 = ((PosterView) findViewById(R.id.posterView_3));
        posterView_4 = ((PosterView) findViewById(R.id.posterView_4));
        posterView_5 = ((PosterView) findViewById(R.id.posterView_5));

        posterView_1.requestFocus();

        mainUpView.setEffectBridge(new EffectNoDrawBridge());

        mainUpView.setUpRectResource(R.mipmap.bg_focus);

        mainUpView.setDrawUpRectPadding(new Rect(ViewUtils.DipToPixels(this, 20),
                ViewUtils.DipToPixels(this, 20),
                ViewUtils.DipToPixels(this, 20),
                ViewUtils.DipToPixels(this, 20)));

        mainUpView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mainUpView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mainUpView.setFocusView(posterView_1, 1.1f);
                    }
                }, 200);
                mainUpView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mainUpView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                mainUpView.setFocusView(newFocus, oldFocus, 1.1f);
            }
        });

        posterView_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posterView_1.closeBroader();
                posterView_2.closeBroader();
                posterView_3.closeBroader();
                posterView_4.closeBroader();
                posterView_5.closeBroader();

                mainUpView.setEffectBridge(new EffectNoDrawBridge());
                mainUpView.setUpRectResource(R.mipmap.bg_focus);
                ((EffectNoDrawBridge) mainUpView.getEffectBridge()).setTranDurAnimTime(200);
                mainUpView.setDrawUpRectPadding(new Rect(ViewUtils.DipToPixels(context, 20),
                        ViewUtils.DipToPixels(context, 20),
                        ViewUtils.DipToPixels(context, 20),
                        ViewUtils.DipToPixels(context, 20)));
            }
        });

        posterView_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posterView_1.closeBroader();
                posterView_2.closeBroader();
                posterView_3.closeBroader();
                posterView_4.closeBroader();
                posterView_5.closeBroader();

                mainUpView.setEffectBridge(new EffectNoBorderBridge());
            }
        });

        posterView_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                posterView_1.showBroader();
                posterView_2.showBroader();
                posterView_3.showBroader();
                posterView_4.showBroader();
                posterView_5.showBroader();

                mainUpView.setEffectBridge(new EffectNoBorderBridge());
            }
        });


    }
}
