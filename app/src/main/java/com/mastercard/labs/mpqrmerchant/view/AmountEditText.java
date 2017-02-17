package com.mastercard.labs.mpqrmerchant.view;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.v4.util.Pair;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.mastercard.labs.mpqrmerchant.R;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class AmountEditText extends SuffixEditText {
    private boolean isUpdating = false;

    private double amount = 0;
    private boolean isIntegerOnly;
    private Pair<Double, Double> minMax = new Pair<>(0.0, Double.MAX_VALUE);
    private AmountInputFilter inputFilter = new AmountInputFilter();

    private @ColorInt int zeroColor;
    private @ColorInt int nonZeroColor;

    private AmountListener listener;

    public AmountEditText(Context context) {
        super(context);
    }

    public AmountEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(context, attrs, 0);
    }

    public AmountEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttributes(context, attrs, defStyleAttr);
    }

    private void getAttributes(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AmountEditText, defStyleAttr, 0);
        if (a != null) {
            zeroColor = a.getColor(R.styleable.AmountEditText_zeroColor, getCurrentTextColor());
            nonZeroColor = a.getColor(R.styleable.AmountEditText_nonZeroColor, getCurrentTextColor());
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        addTextChangedListener(new AmountWatcher());
        setFilters(new InputFilter[]{inputFilter});
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        if (isIntegerOnly) {
            amount = (int) amount;
        }

        if (!isInRange(minMax.first, minMax.second, amount)) {
            return;
        }

        this.amount = amount;

        isUpdating = true;
        updateText();
        isUpdating = false;
    }

    public boolean isIntegerOnly() {
        return isIntegerOnly;
    }

    public void setIntegerOnly(boolean isIntegerOnly) {
        this.isIntegerOnly = isIntegerOnly;

        // Reset text
        setAmount(getAmount());
    }

    public void setMinMax(Pair<Double, Double> minMax) {
        this.minMax = minMax;

        // Reset if not in range
        if (!isInRange(minMax.first, minMax.second, amount)) {
            setAmount(0);
        }
    }

    public void setListener(AmountListener listener) {
        this.listener = listener;
    }

    private double parseAmount(String amountText) {
        try {
            if (isIntegerOnly()) {
                return Integer.parseInt(amountText);
            }

            double amount = Double.parseDouble(amountText);

            int count = amountText.length() - amountText.indexOf(".") - 1;
            amount *= Math.pow(10, count);

            return amount / 100;
        } catch (Exception ex) {
            return 0;
        }
    }

    private void updateText() {
        getText().clear();

        String updatedText;
        if (isIntegerOnly) {
            updatedText = String.format(Locale.getDefault(), "%.0f", amount);
        } else {
            updatedText = String.format(Locale.getDefault(), "%.2f", amount);
        }

        getText().append(updatedText);

        if (amount == 0) {
            setTextColor(zeroColor);
        } else {
            setTextColor(nonZeroColor);
        }
    }

    class AmountWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable text) {
            if (isUpdating) {
                return;
            }

            isUpdating = true;

            double amount = parseAmount(text.toString());
            setAmount(amount);

            isUpdating = false;

            if (listener != null) {
                listener.amountChanged(AmountEditText.this, amount);
            }
        }
    }

    private class AmountInputFilter implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String result = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());

            if (isIntegerOnly() && result.contains(".")) {
                return "";
            } else if (result.indexOf(".") != result.lastIndexOf(".")) {
                return "";
            }

            return null;
        }
    }

    private boolean isInRange(double min, double max, double value) {
        return max > min ? value >= min && value <= max : value >= max && value <= min;
    }

    public interface AmountListener {
        void amountChanged(AmountEditText editText, double amount);
    }
}
