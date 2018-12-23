package com.norton.demo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewTreeObserver;

import com.norton.demo.R;
import com.norton.demo.utils.ViewUtils;
import com.norton.demo.view.PosterView;
import com.swl.tvlibrary.bridge.EffectNoDrawBridge;
import com.swl.tvlibrary.view.MainUpView;

/**
 * Created by WIN8 on 2018/11/18.
 */
public class MainActivity extends Activity {

    private MainUpView mainUpView;
    private PosterView posterView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainUpView = (MainUpView) findViewById(R.id.mainUpView);
        posterView = (PosterView) findViewById(R.id.broad_test);

        posterView.requestFocus();

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
                        mainUpView.setFocusView(posterView, 1.1f);
                    }
                }, 200);
                mainUpView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mainUpView.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                mainUpView.setFocusView(newFocus, oldFocus, 1.05f);
            }
        });

        findViewById(R.id.broad_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BroadTestActivity.class));
            }
        });

        findViewById(R.id.recycler_test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.recycler_test2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.listView_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ListViewTestActivity.class));
            }
        });

        findViewById(R.id.gridView_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GridViewTestActivity.class));
            }
        });

        findViewById(R.id.scale_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.relativeLayout_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.linearLayout_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
