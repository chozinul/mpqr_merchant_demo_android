package com.mastercard.labs.mpqrmerchant.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.QRData;
import com.mastercard.labs.mpqrmerchant.event.TransactionsUpdateEvent;
import com.mastercard.labs.mpqrmerchant.login.LoginActivity;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.qrcode.QRCodeActivity;
import com.mastercard.labs.mpqrmerchant.settings.SettingsActivity;
import com.mastercard.labs.mpqrmerchant.transaction.list.TransactionListActivity;
import com.mastercard.labs.mpqrmerchant.transaction.overview.TransactionOverviewFragment;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;
import com.mastercard.labs.mpqrmerchant.utils.DialogUtils;
import com.mastercard.labs.mpqrmerchant.utils.KeyboardUtils;
import com.mastercard.labs.mpqrmerchant.view.SuffixEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class MainActivity extends AppCompatActivity implements MainContract.View {
    public static String BUNDLE_USER_KEY = "userId";

    private MainContract.Presenter mPresenter;
    private long userId;

    @BindView(R.id.img_merchant_logo)
    ImageView imgMerchantLogo;

    @BindView(R.id.txt_merchant_name)
    TextView txtMerchantName;

    @BindView(R.id.txt_merchant_city)
    TextView txtMerchantCity;

    @BindView(R.id.img_settings)
    ImageButton imgSettings;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.txt_currency_value)
    TextView txtCurrencyValue;

    @BindView(R.id.txt_tip_type_value)
    TextView tipTypeValue;

    @BindView(R.id.txt_amount)
    TextView amountTitleTextView;

    @BindView(R.id.txt_amount_value)
    EditText amountEditText;

    @BindView(R.id.rl_amount)
    RelativeLayout rlAmount;

    @BindView(R.id.txt_tip)
    TextView tipTitleTextView;

    @BindView(R.id.txt_tip_value)
    SuffixEditText tipEditText;

    @BindView(R.id.rl_tip)
    RelativeLayout tipLayout;

    @BindView(R.id.txt_total_amount)
    TextView totalAmountTextView;

    private boolean blockAmountTextViewChange;
    private boolean blockTipTextViewChange;

    private ProgressDialog progressDialog;

    private AmountInputFilter tipInputFilter = new AmountInputFilter(0, Double.MAX_VALUE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        userId = getIntent().getLongExtra(BUNDLE_USER_KEY, -1L);
        // TODO: Only for debugging purposes till Login screen is implemented.
        userId = LoginManager.getInstance().getLoggedInUserId();

        mPresenter = new MainPresenter(this, RealmDataSource.getInstance(), userId);

        amountEditText.setFilters(new InputFilter[]{new AmountInputFilter(0, Double.MAX_VALUE)});
        tipEditText.setFilters(new InputFilter[]{tipInputFilter});
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTransactionsUpdateEvent(TransactionsUpdateEvent event) {
        mPresenter.start();
    }

    @OnTextChanged(value = R.id.txt_amount_value, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onAmountChanged(Editable amountText) {
        if (blockAmountTextViewChange) {
            return;
        }

        blockAmountTextViewChange = true;

        double amount = parseAmount(amountText.toString());
        updateEditText(amountEditText, amount);

        blockAmountTextViewChange = false;

        mPresenter.setAmount(amount);
    }

    @OnTextChanged(value = R.id.txt_tip_value, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onTipChanged(Editable tipText) {
        if (blockTipTextViewChange) {
            return;
        }

        blockTipTextViewChange = true;

        double tip = parseAmount(tipText.toString());
        updateEditText(tipEditText, tip);

        blockTipTextViewChange = false;

        mPresenter.setTip(tip);
    }

    @OnClick(value = R.id.rl_tip_type)
    public void onTipTypePressed(View view) {
        mPresenter.selectTipType();
    }

    @OnClick(value = R.id.btn_generate)
    public void generateQRCode(View view) {
        mPresenter.generateQRString();
    }

    @OnClick(value = R.id.btn_logout)
    public void logout(View view) {
        DialogUtils.customAlertDialogBuilder(this, R.string.logout_confirmation)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.logout();
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @OnClick(value = R.id.ll_transactions)
    public void showTransactions() {
        Intent intent = TransactionListActivity.newIntent(this, userId);

        startActivity(intent);
    }

    @OnClick(value = R.id.img_settings)
    public void showSettings() {
        Intent intent = SettingsActivity.newIntent(this, userId);

        startActivity(intent);
    }

    @OnEditorAction(value = {R.id.txt_amount_value, R.id.txt_tip_value})
    public boolean onEditorAction(EditText editText, int id, KeyEvent event) {
        if (event != null && event.getAction() != KeyEvent.ACTION_DOWN) {
            return false;
        }

        if (id == R.id.action_generate || id == EditorInfo.IME_NULL) {
            mPresenter.generateQRString();
            return true;
        }
        return false;
    }

    private double parseAmount(String amountText) {
        try {
            double amount = Double.parseDouble(amountText);
            int count = amountText.length() - amountText.indexOf(".") - 1;
            amount *= Math.pow(10, count);

            return amount / 100;
        } catch (Exception ex) {
            return 0;
        }
    }

    private void updateEditText(EditText editText, double amount) {
        editText.getText().clear();

        String text = String.format(Locale.getDefault(), "%.2f", amount);
        editText.getText().append(text);

        if (amount == 0) {
            editText.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
        } else {
            editText.setTextColor(ContextCompat.getColor(this, R.color.colorTextMainColor));
        }
    }

    @Override
    public void setName(String name) {
        txtMerchantName.setText(name);
    }

    @Override
    public void setCity(String city) {
        txtMerchantCity.setText(String.format(Locale.getDefault(), "@%s", city));
    }

    private TransactionOverviewFragment getTransactionOverviewFragment() {
        return (TransactionOverviewFragment) getSupportFragmentManager().findFragmentById(R.id.frg_transaction_overview);
    }

    @Override
    public void setTransactionTotalAmount(double amount, String currencyCode) {
        TransactionOverviewFragment transactionOverviewFragment = getTransactionOverviewFragment();
        if (transactionOverviewFragment == null) {
            return;
        }
        transactionOverviewFragment.setTotalDailyAmount(currencyCode, amount);
    }

    @Override
    public void setTotalTransactions(int size) {
        TransactionOverviewFragment transactionOverviewFragment = getTransactionOverviewFragment();
        if (transactionOverviewFragment == null) {
            return;
        }
        transactionOverviewFragment.setTotalTransactions(size);
    }

    @Override
    public void setCurrency(String currency) {
        txtCurrencyValue.setText(currency);
    }

    @Override
    public void showUserNotFound() {
        DialogUtils.customAlertDialogBuilder(this, R.string.error_user_not_found)
                .setCancelable(false)
                .setNegativeButton(R.string.logout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mPresenter.logout();
                    }
                }).show();
    }

    @Override
    public void showInvalidDataError() {
        DialogUtils.showDialog(this, 0, R.string.invalid_payment_data);
    }

    @Override
    public void setAmount(double amount) {
        blockAmountTextViewChange = true;
        updateEditText(amountEditText, amount);
        blockAmountTextViewChange = false;
    }

    @Override
    public void setStaticAmountTitle() {
        amountTitleTextView.setText(R.string.amount_static);
    }

    @Override
    public void setDynamicAmountTitle() {
        amountTitleTextView.setText(R.string.amount_dynamic);
    }

    private void setTipHasPercentage(boolean hasPercentage) {
        tipEditText.setSuffix(hasPercentage ? " %" : "");
        tipInputFilter.setMax(hasPercentage ? 100 : Double.MAX_VALUE);
    }

    @Override
    public void setFlatConvenienceFee(double fee) {
        tipTypeValue.setText(R.string.tip_type_fixed);
        tipTitleTextView.setText(R.string.flat_convenience_fee);
        setTipHasPercentage(false);
        setTip(fee);
    }

    @Override
    public void setPercentageConvenienceFee(double feePercentage) {
        tipTypeValue.setText(R.string.tip_type_percentage);
        tipTitleTextView.setText(R.string.percentage_convenience_fee);
        setTipHasPercentage(true);
        setTip(feePercentage);
    }

    @Override
    public void setPromptToEnterTip() {
        tipTypeValue.setText(R.string.tip_type_prompt);
        tipTitleTextView.setText(R.string.prompt);
        setTipHasPercentage(false);
        setTip(0);
    }

    @Override
    public void setNoTipRequired() {
        tipTypeValue.setText(R.string.tip_type_none);
        tipTitleTextView.setText(R.string.no_tip);
        setTipHasPercentage(false);
        setTip(0);
    }

    private void setTip(double amount) {
        blockTipTextViewChange = true;
        updateEditText(tipEditText, amount);
        blockTipTextViewChange = false;
    }

    @Override
    public void showTipChangeNotAllowedError() {
        DialogUtils.showDialog(this, R.string.error, R.string.error_tip_change_not_allowed);
    }

    @Override
    public void disableTipChange() {
        toggleLayout(tipLayout, tipTitleTextView, tipEditText, false);
        amountEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        KeyboardUtils.restartInput(this, amountEditText);
    }

    @Override
    public void enableTipChange() {
        toggleLayout(tipLayout, tipTitleTextView, tipEditText, true);
        amountEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        KeyboardUtils.restartInput(this, amountEditText);
    }

    private void toggleLayout(RelativeLayout layout, TextView titleTextView, EditText editText, boolean enabled) {
        if (enabled) {
            layout.setBackgroundColor(Color.TRANSPARENT);
            titleTextView.setTextColor(ContextCompat.getColor(this, R.color.colorWarmGrey));
            editText.setEnabled(true);
        } else {
            // TODO: Remove line below edit text
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDisabledDeepSeaBlue));
            titleTextView.setTextColor(ContextCompat.getColor(this, R.color.colorDeepSeaBlue));
            editText.setEnabled(false);
        }
    }

    @Override
    public void showCurrencies(final List<CurrencyCode> currencies, int selected) {
        List<String> currencyText = new ArrayList<>(currencies.size());
        for (CurrencyCode code : currencies) {
            currencyText.add(String.format(Locale.getDefault(), "%s - %s", code.toString(), code.getCurrencyName()));
        }

        new AlertDialog.Builder(this).setSingleChoiceItems(currencyText.toArray(new String[]{}), selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.setCurrencyCode(currencies.get(which));
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void showTipTypes(final List<QRData.TipType> tipTypes, int selected) {
        List<String> types = new ArrayList<>(tipTypes.size());
        for (QRData.TipType type : tipTypes) {
            types.add(type.toString());
        }

        new AlertDialog.Builder(this).setSingleChoiceItems(types.toArray(new String[]{}), selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.setTipType(tipTypes.get(which));
                dialog.dismiss();
            }
        }).show();
    }

    @Override
    public void setTotalAmount(double amount, String currencyCode) {
        totalAmountTextView.setText(String.format(Locale.getDefault(), "%s %,.2f", currencyCode, amount));

        if (amount == 0) {
            totalAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLightGrey));
        } else {
            totalAmountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorTextMainColor));
        }
    }

    @Override
    public void showQRCode(QRData qrData, String qrCodeString) {
        Intent intent = QRCodeActivity.newIntent(this, qrData, qrCodeString);
        startActivity(intent);
    }

    @Override
    public void showLogoutProgress() {
        hideLogoutProgress();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.logging_out));
        progressDialog.setCancelable(false);

        progressDialog.show();
    }

    @Override
    public void hideLogoutProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }

    @Override
    public void showLogoutFailed() {
        DialogUtils.showDialog(this, 0, R.string.logout_failed);
    }

    private class AmountInputFilter implements InputFilter {
        private double min, max;

        AmountInputFilter(double min, double max) {
            this.min = min;
            this.max = max;
        }

        void setMax(double max) {
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            String result = dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length());

            if (result.indexOf(".") != result.lastIndexOf(".")) {
                return "";
            }

            double input = parseAmount(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;

            return null;
        }

        private boolean isInRange(double a, double b, double c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}
