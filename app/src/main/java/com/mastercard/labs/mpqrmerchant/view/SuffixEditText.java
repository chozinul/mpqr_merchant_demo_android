package com.mastercard.labs.mpqrmerchant.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.EditText;

import com.mastercard.labs.mpqrmerchant.R;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/9/17
 */
public class SuffixEditText extends EditText {
    private TextPaint textPaint = new TextPaint();
    private String suffix = "";
    private float suffixPadding;
    private @ColorInt int suffixColor;

    public SuffixEditText(Context context) {
        super(context);
    }

    public SuffixEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs, 0);
    }

    public SuffixEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas c){
        super.onDraw(c);
        int suffixXPosition = (int) textPaint.measureText(getText().toString()) + getPaddingLeft();
        c.drawText(suffix, Math.max(suffixXPosition, suffixPadding), getBaseline(), textPaint);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        textPaint.setColor(suffixColor);
        textPaint.setTextSize(getTextSize());
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    private void getAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SuffixEditText, defStyleAttr, 0);
        if(a != null) {
            suffix = a.getString(R.styleable.SuffixEditText_suffix);
            if(suffix == null) {
                suffix = "";
            }
            suffixPadding = a.getDimension(R.styleable.SuffixEditText_suffixPadding, 0);
            suffixColor = a.getColor(R.styleable.SuffixEditText_suffixColor, getCurrentTextColor());
            a.recycle();
        }
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        invalidate();
        requestLayout();
    }

    public float getSuffixPadding() {
        return suffixPadding;
    }

    public void setSuffixPadding(float suffixPadding) {
        this.suffixPadding = suffixPadding;
        invalidate();
        requestLayout();
    }

    public @ColorInt int getSuffixColor() {
        return suffixColor;
    }

    public void setSuffixColor(@ColorInt int suffixColor) {
        this.suffixColor = suffixColor;
        invalidate();
        requestLayout();
    }
}
