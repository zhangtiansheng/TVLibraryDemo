package com.norton.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.norton.demo.R;
import com.zhy.autolayout.AutoFrameLayout;

/**
 * @author zhangTianSheng 956122936@qq.com
 */
public class PosterView extends AutoFrameLayout implements View.OnFocusChangeListener {

    private RelativeLayout mPosterItem;
    private ImageView poster;
    private RelativeLayout poster_title;
    private TextView poster_txt_introduce;
    private SimpleFocusChange mSimpleFocusChange;
    private boolean isShowBroader;

    public PosterView(Context context) {
        this(context, null);
    }

    public PosterView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PosterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_poster_view, this, true);
        mPosterItem = (RelativeLayout) findViewById(R.id.item_poster);
        poster = (ImageView) findViewById(R.id.poster);
        poster_title = (RelativeLayout) findViewById(R.id.poster_title);
        poster_txt_introduce = (TextView) findViewById(R.id.poster_txt_introduce);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PosterView);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.PosterView_text) {
                String title = typedArray.getString(attr);
                poster_txt_introduce.setText(title);
            } else if (attr == R.styleable.PosterView_poster) {
                Drawable drawable = typedArray.getDrawable(attr);
                poster.setImageDrawable(drawable);
            }
        }
        typedArray.recycle();
        setOnFocusChangeListener(this);
    }

    public void enableViewTextScroll(boolean scroll) {
        poster_txt_introduce.setSelected(scroll);
    }

    public ImageView getPoster() {
        return poster;
    }

    public void setViewPoster(Drawable drawable) {
        poster.setImageDrawable(drawable);
    }

    public void setViewPoster(Bitmap bitmap) {
        poster.setImageBitmap(bitmap);
    }

    public void setViewIntroduce(String text) {
        poster_txt_introduce.setText(text);
        poster_title.setVisibility(View.VISIBLE);
    }

    public void showBroader() {
        isShowBroader = true;
        mPosterItem.setBackgroundResource(R.drawable.selector_shape_focus);
    }

    public void closeBroader() {
        isShowBroader = false;
        mPosterItem.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        enableViewTextScroll(hasFocus);
        if (isShowBroader) {
            if (hasFocus) {
                mPosterItem.setBackgroundResource(R.drawable.shape_bg_focus);
            } else {
                mPosterItem.setBackgroundResource(R.color.transparent);
            }
        }
        if (null != mSimpleFocusChange) {
            mSimpleFocusChange.onSimpleFocusChange(v, hasFocus);
        }
    }

    public interface SimpleFocusChange {
        void onSimpleFocusChange(View v, boolean hasFocus);
    }

    public void setOnSimpleFocusChangeListener(SimpleFocusChange onSimpleFocusChangeListener) {
        this.mSimpleFocusChange = onSimpleFocusChangeListener;
    }

}
