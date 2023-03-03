package com.digwex.components.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.digwex.R;

/**
 * TODO: document your custom view class.
 */
public class PercentLayout extends RelativeLayout {

    private float mWidthPercent = 1.0f;
    private float mHeightPercent = 1.0f;
    private float mLeftPercent = .0f;
    private float mTopPercent = .0f;

    public PercentLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public PercentLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public PercentLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        @SuppressLint("Recycle") final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.PercentLayout, defStyle, 0);

        mWidthPercent = a.getFloat(
                R.styleable.PercentLayout_withPercent, mWidthPercent);

        mHeightPercent = a.getFloat(
                R.styleable.PercentLayout_heightPercent,
                mHeightPercent);

        mLeftPercent = a.getFloat(
                R.styleable.PercentLayout_leftPercent,
                mLeftPercent);

        mTopPercent = a.getFloat(
                R.styleable.PercentLayout_topPercent,
                mTopPercent);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }
}
