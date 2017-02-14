package com.mastercard.labs.mpqrmerchant.qrcode;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.data.model.QRData;
import com.mastercard.labs.mpqrmerchant.utils.DialogUtils;
import com.mastercard.labs.mpqrmerchant.utils.QRGenerator;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QRCodeActivity extends AppCompatActivity implements QRCodeContract.View {
    private static String BUNDLE_QR_DATA = "qrData";
    private static String BUNDLE_PPSTRING_KEY = "pushPaymentString";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_qr_code)
    ImageView imgQrCode;

    @BindView(R.id.pb_qr_code)
    ProgressBar pbQrCode;

    @BindView(R.id.txt_amount_value)
    TextView txtAmountValue;

    @BindView(R.id.txt_merchant_code)
    TextView txtMerchantCode;

    @BindView(R.id.txt_merchant_name)
    TextView txtMerchantName;

    @BindView(R.id.txt_merchant_city)
    TextView txtMerchantCity;

    private String mPushPaymentString;
    private QRData mQRData;

    private QRCodeContract.Presenter mPresenter;

    private QRGenerator qrGenerator;

    public static Intent newIntent(Context context, QRData qrData, String pushPaymentString) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PPSTRING_KEY, pushPaymentString);
        bundle.putParcelable(BUNDLE_QR_DATA, qrData);

        Intent intent = new Intent(context, QRCodeActivity.class);
        intent.putExtras(bundle);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String pushPaymentString;
        QRData qrData;
        if (savedInstanceState != null) {
            mPushPaymentString = savedInstanceState.getString(BUNDLE_PPSTRING_KEY);
            mQRData = savedInstanceState.getParcelable(BUNDLE_QR_DATA);
        } else {
            mPushPaymentString = getIntent().getStringExtra(BUNDLE_PPSTRING_KEY);
            mQRData = getIntent().getParcelableExtra(BUNDLE_QR_DATA);
        }

        mPresenter = new QRCodePresenter(this, mQRData, mPushPaymentString);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_PPSTRING_KEY, mPushPaymentString);
        outState.putParcelable(BUNDLE_QR_DATA, mQRData);
    }

    @Override
    public void showQRCode(String paymentString) {
        if (qrGenerator != null && qrGenerator.getStatus() == AsyncTask.Status.RUNNING) {
            qrGenerator.cancel(true);
        }

        int width = getResources().getDimensionPixelSize(R.dimen.size_qr_code);
        int height = getResources().getDimensionPixelSize(R.dimen.size_qr_code);

        qrGenerator = new QRGenerator(width, height, new QRGenerator.QRGeneratorListener() {
            @Override
            public void qrGenerationStarted() {
                showQRLoadingProgress();
            }

            @Override
            public void qrGenerated(Bitmap bitmap) {
                hideQRLoadingProgress();

                if (bitmap == null) {
                    showCannotGenerateQRError();
                }

                imgQrCode.setImageBitmap(bitmap);
            }
        });

        qrGenerator.execute(paymentString);
    }

    private void showQRLoadingProgress() {
        imgQrCode.setVisibility(View.GONE);
        pbQrCode.setVisibility(View.VISIBLE);
    }

    private void hideQRLoadingProgress() {
        imgQrCode.setVisibility(View.VISIBLE);
        pbQrCode.setVisibility(View.GONE);
    }

    private void showCannotGenerateQRError() {
        DialogUtils.showDialog(this, 0, R.string.qr_code_generation_error);
    }

    @Override
    public void setTotalAmount(double total, String currencyCode) {
        txtAmountValue.setText(String.format(Locale.getDefault(), "%s %,.2f", currencyCode, total));
    }

    @Override
    public void setMerchantCode(String merchantCode) {
        txtMerchantCode.setText(getString(R.string.qr_merchant_code, merchantCode));
    }

    @Override
    public void setMerchantName(String merchantName) {
        txtMerchantName.setText(merchantName);
    }

    @Override
    public void setMerchantCity(String merchantCity) {
        txtMerchantCity.setText(merchantCity);
    }
}
