package com.norton.demo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.norton.demo.R;
import com.zhy.autolayout.AutoFrameLayout;

/**
 * @author zhangTianSheng 956122936@qq.com
 */
public class SimpleImageButton extends AutoFrameLayout implements View.OnFocusChangeListener {

    private ImageView mImageView;
    private TextView mTextView;
    private OnViewFocusChangeListener mFocusChangeListener;

    public SimpleImageButton(Context context) {
        this(context, null);
    }

    public SimpleImageButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_custom_image_button, this, true);
        mImageView = (ImageView) view.findViewById(R.id.item_image);
        mTextView = (TextView) view.findViewById(R.id.item_text);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleImageButton);
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.SimpleImageButton_button_text) {
                String title = typedArray.getString(attr);
                mTextView.setText(title);
            } else if (attr == R.styleable.SimpleImageButton_button_icon) {
                Drawable drawable = typedArray.getDrawable(attr);
                mImageView.setImageDrawable(drawable);
            }
        }
        typedArray.recycle();
        setViewEnable();
        this.setOnFocusChangeListener(this);
    }


    public void setViewTextSize(float size) {
        mTextView.setTextSize(size);
    }


    public void setViewText(String text) {
        mTextView.setText(text);
    }

    public void setViewEnable() {
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        this.setClickable(true);
    }

    public void setViewDisable() {
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        this.setClickable(false);
    }

    public String getText() {
        return mTextView.getText().toString();
    }

    public TextView getCustomTextView() {
        return mTextView;
    }

    public void setViewImageRotate(float value) {
        mImageView.animate().rotation(value).setDuration(300).start();
    }

    public void setmImageView(int background) {
        mImageView.setImageResource(background);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {

        } else {

        }
        if (null != mFocusChangeListener) {
            mFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    public void setOnViewFocusChangeListener(OnViewFocusChangeListener onViewFocusChangeListener) {
        this.mFocusChangeListener = onViewFocusChangeListener;
    }

    public interface OnViewFocusChangeListener {
        void onFocusChange(View v, boolean hasFocus);
    }


}
