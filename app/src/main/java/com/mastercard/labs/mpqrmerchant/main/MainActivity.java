package com.mastercard.labs.mpqrmerchant.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.view.SuffixEditText;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    @BindView(R.id.txt_total_daily_value)
    TextView txtTotalDailyValue;

    @BindView(R.id.txt_transactions_daily_value)
    TextView txtTransactionsDailyValue;

    @BindView(R.id.ll_transactions)
    LinearLayout layoutTransactions;

    @BindView(R.id.txt_currency_value)
    TextView txtCurrencyValue;

    @BindView(R.id.txt_tip_type_value)
    TextView txtTipTypeValue;

    @BindView(R.id.txt_amount)
    TextView txtAmount;

    @BindView(R.id.txt_amount_value)
    EditText txtAmountValue;

    @BindView(R.id.rl_amount)
    RelativeLayout rlAmount;

    @BindView(R.id.txt_tip)
    TextView txtTip;

    @BindView(R.id.txt_tip_value)
    SuffixEditText txtTipValue;

    @BindView(R.id.rl_tip)
    RelativeLayout rlTip;

    @BindView(R.id.ll_amount_tip_container)
    LinearLayout llAmountTipContainer;

    @BindView(R.id.txt_total_amount)
    TextView txtTotalAmount;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.start();
    }
}
