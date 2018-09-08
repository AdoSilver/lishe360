package com.cardeanutrition.lishe360app.services;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageViews extends ImageView {

    private float hwRatio = 1f;
    private static final String key_hwRatio = "hwRatio";

    public SquareImageViews(Context context) {
        super(context);
        setAttributes(context, null);
    }

    public SquareImageViews(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttributes(context, attrs);
    }

    public SquareImageViews(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAttributes(context, attrs);
    }

    private void setAttributes(Context context, AttributeSet attrs) {
        if (attrs == null) return;
        String packageName = "http://schemas.android.com/apk/res-auto";
        hwRatio = attrs.getAttributeFloatValue(packageName, key_hwRatio, 1f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int calculatedHeight = calculateHeightByRatio(width);

        if (calculatedHeight != height) {
            setMeasuredDimension(width, calculatedHeight);
        }

    }

    private int calculateHeightByRatio(int side) {
        return (int) (hwRatio * (float) side);
    }

    public float getHwRatio() {
        return hwRatio;
    }

    public void setXyRatio(float xyRatio) {
        this.hwRatio = xyRatio;
        this.invalidate();
    }

}