package com.mastercard.labs.mpqrmerchant.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.adapter.CountryAdapter;
import com.mastercard.labs.mpqrmerchant.adapter.CurrencyAdapter;
import com.mastercard.labs.mpqrmerchant.adapter.SettingsAdapter;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Settings;
import com.mastercard.labs.mpqrmerchant.utils.CountryCode;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;
import com.mastercard.labs.mpqrmerchant.utils.DialogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends AppCompatActivity implements SettingsContract.View, SettingsAdapter.SettingsItemListener {
    public static String BUNDLE_USER_KEY = "userId";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_merchant_logo)
    ImageView mMerchantLogoImageView;

    @BindView(R.id.rv_settings)
    RecyclerView mSettingsRecyclerView;

    private long mUserId;
    private SettingsContract.Presenter mPresenter;
    private SettingsAdapter mSettingsAdapter;

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, SettingsActivity.class);

        intent.putExtra(BUNDLE_USER_KEY, userId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSettingsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mSettingsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        mSettingsRecyclerView.addItemDecoration(dividerItemDecoration);

        if (savedInstanceState != null) {
            mUserId = savedInstanceState.getLong(BUNDLE_USER_KEY, -1);
        } else {
            mUserId = getIntent().getLongExtra(BUNDLE_USER_KEY, -1);
        }

        mPresenter = new SettingsPresenter(this, RealmDataSource.getInstance(), mUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    public void showUserNotFound() {
        DialogUtils.customAlertDialogBuilder(this, R.string.error_user_not_found)
                .setCancelable(false)
                .setNegativeButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                }).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(BUNDLE_USER_KEY, mUserId);

        super.onSaveInstanceState(outState);
    }

    @Override
    public String getMerchantNameTitle() {
        return getString(R.string.merchant_name);
    }

    @Override
    public String getCountryTitle() {
        return getString(R.string.country);
    }

    @Override
    public String getCityTitle() {
        return getString(R.string.city);
    }

    @Override
    public String getMerchantCategoryCodeTitle() {
        return getString(R.string.merchant_category_code);
    }

    @Override
    public String getCardNumberTitle() {
        return getString(R.string.card_number);
    }

    @Override
    public String getCurrencyTitle() {
        return getString(R.string.currency);
    }

    @Override
    public String getStoreIdTitle() {
        return getString(R.string.store_id);
    }

    @Override
    public String getTerminalIdTitle() {
        return getString(R.string.terminal_no);
    }

    @Override
    public void showSettings(List<Settings> allSettings) {
        mSettingsAdapter = new SettingsAdapter(allSettings, this);
        mSettingsRecyclerView.setAdapter(mSettingsAdapter);
    }

    @Override
    public void showMerchantNameEditor(String name) {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        int padding = getResources().getDimensionPixelSize(R.dimen.size_14);
        layout.setPadding(padding, 0, padding, 0);

        final EditText input = new EditText(layout.getContext());
        input.setText(name);

        layout.addView(input, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.merchant_name)
                .setMessage(R.string.enter_merchant_name)
                .setView(layout)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.updateMerchantName(input.getText().toString());
                    }
                }).create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        dialog.show();

        input.setSelection(input.length(), input.length());
    }

    @Override
    public void onSettingsItemClicked(Settings settings) {
        mPresenter.settingsSelected(settings);
    }

    @Override
    public void showCardNumberEditor(String cardNumber) {
        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.credit_card_edittext_height));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(params);

        int padding = getResources().getDimensionPixelSize(R.dimen.size_14);
        layout.setPadding(padding, 0, padding, 0);

        final EditText input = new EditText(layout.getContext());
        input.setText(cardNumber);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.addTextChangedListener(new FourDigitCardFormatWatcher());
        input.setKeyListener(DigitsKeyListener.getInstance("0123456789 "));
        input.setFilters(new InputFilter[] {new InputFilter.LengthFilter(16 + 3)});

//        final ImageView imageView = new ImageView(layout.getContext());
//        imageView.setImageResource(R.drawable.mastercard_logo);

        LinearLayout.LayoutParams inputParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        inputParams.weight = 1;
        layout.addView(input, inputParams);

//        layout.addView(imageView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.card_number)
                .setMessage(R.string.enter_card_number)
                .setView(layout)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing as we are going to override this behavior
                    }
                }).create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        dialog.show();

        input.setSelection(input.length(), input.length());

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Validation should be done in presenter
                if (!isValidCardNumber(input.getText().toString().replace(" ", ""))) {
                    input.setError(getString(R.string.error_invalid_card_number));
                } else {
                    dialog.dismiss();
                    mPresenter.updateMerchantCard(input.getText().toString().replace(" ", ""));
                }
            }
        });
    }

    @Override
    public void showCurrencyEditor(String currencyCode) {

        int selectedCurrency = 0;
        List<CurrencyCode> currencyList = Arrays.asList(CurrencyCode.values());
        final CurrencyAdapter adapter = new CurrencyAdapter(this, currencyList);

        for (CurrencyCode currency: currencyList)
        {
            if (currency.getNumericCode().equals(currencyCode)) {
                selectedCurrency = currencyList.indexOf(currency);
                adapter.setSelectedIndex(selectedCurrency);
            }
        }

        //TODO: setSelectedIndex
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(adapter, selectedCurrency, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.setSelectedIndex(which);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setCancelable(true)
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updateCurrencyCode(adapter.getSelectedCurrencyCode());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void showCountryEditor(String country) {

        int selectedCountry = 0;
        List<CountryCode> countryList = Arrays.asList(CountryCode.values());
        final CountryAdapter adapter = new CountryAdapter(this, countryList);

        for (CountryCode cCountry:  countryList)
        {
            if (cCountry.getISO2Code().equals(country)) {
                Log.d(country, "Matched");
                selectedCountry = countryList.indexOf(cCountry);
                Log.d(country, "Matched");
                adapter.setSelectedIndex(selectedCountry);
            }
        }

        //TODO: setSelectedIndex
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setSingleChoiceItems(adapter, selectedCountry, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.setSelectedIndex(which);
                        adapter.notifyDataSetChanged();
                    }
                })
                .setCancelable(true)
                .setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.updateCountryCode(adapter.getSelectedCountryCode());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void showCityEditor(String city) {

        LinearLayout layout = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);

        int padding = getResources().getDimensionPixelSize(R.dimen.size_14);
        layout.setPadding(padding, 0, padding, 0);

        final EditText input = new EditText(layout.getContext());
        input.setText(city);

        layout.addView(input, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.city)
                .setMessage(R.string.enter_city_name)
                .setView(layout)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.updateCityName(input.getText().toString());
                    }
                }).create();

        Window window = dialog.getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        dialog.show();

        input.setSelection(input.length(), input.length());
    }

    private boolean isValidCardNumber(String cardNumber) {
        //Pattern mastercardPattern = Pattern.compile("^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$");
        //return mastercardPattern.matcher(cardNumber).matches();
        return cardNumber.length() == 16;
    }

    static class FourDigitCardFormatWatcher implements TextWatcher {
        private static final char space = ' ';

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Remove spacing char
            if (s.length() > 0 && (s.length() % 5) == 0) {
                final char c = s.charAt(s.length() - 1);
                if (space == c) {
                    s.delete(s.length() - 1, s.length());
                }
            }
            if (s.length() > 0 && (s.length() % 5) == 0) {
                char c = s.charAt(s.length() - 1);
                if (Character.isDigit(c) && TextUtils.split(s.toString(), String.valueOf(space)).length <= 3) {
                    s.insert(s.length() - 1, String.valueOf(space));
                }
            }
        }
    }
}
