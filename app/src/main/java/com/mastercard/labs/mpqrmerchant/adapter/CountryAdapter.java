package com.mastercard.labs.mpqrmerchant.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.utils.CountryCode;

import java.util.List;

/**
 * Created by kaile on 29/3/17.
 */

public class CountryAdapter extends ArrayAdapter<CountryCode> {
    private int selectedIndex;
    private List<CountryCode> countryCodes;

    public CountryAdapter(Context context, List<CountryCode> countryCodes) {
        super(context, 0, countryCodes);
        this.countryCodes = countryCodes;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        if (convertView == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        } else {
            view = convertView;
        }

        TextView countryTextView = (TextView) view.findViewById(R.id.item_txt);
        RadioButton radioButton = (RadioButton) view.findViewById(R.id.rb_check);

        CountryCode countryCode = getItem(position);
        if (countryCode == null) {
            return view;
        }

        countryTextView.setText(countryCode.getCountryName());

        radioButton.setChecked(selectedIndex == position);

        return view;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public String getSelectedCountryCode() { return countryCodes.get(selectedIndex).getISO2Code(); }

}

