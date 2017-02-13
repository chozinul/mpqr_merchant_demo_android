package com.mastercard.labs.mpqrmerchant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.List;
import java.util.Locale;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class CurrencyAdapter extends ArrayAdapter<CurrencyCode> {
    private int selectedIndex;

    public CurrencyAdapter(Context context, List<CurrencyCode> currencyCodes) {
        super(context, 0, currencyCodes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null) {
            view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        } else {
            view = convertView;
        }

        TextView currencyTextView = (TextView) view.findViewById(android.R.id.text1);

        CurrencyCode currencyCode = getItem(position);
        if (currencyCode == null) {
            return view;
        }

        currencyTextView.setText(String.format(Locale.getDefault(), "%s - %s", currencyCode.toString(), currencyCode.getCurrencyName()));

//        radioButton.setChecked(selectedIndex == position);

        return view;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }
}
