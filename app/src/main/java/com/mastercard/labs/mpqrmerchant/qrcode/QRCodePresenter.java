package com.mastercard.labs.mpqrmerchant.qrcode;

import com.mastercard.labs.mpqrmerchant.data.model.QRData;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/14/17
 */
class QRCodePresenter implements QRCodeContract.Presenter {

    private QRCodeContract.View mView;
    private QRData mQRData;
    private String mPushPaymentString;

    QRCodePresenter(QRCodeContract.View view, QRData qrData, String pushPaymentString) {
        this.mView = view;
        this.mPushPaymentString = pushPaymentString;
        this.mQRData = qrData;
    }

    @Override
    public void start() {
        mView.showQRCode(mPushPaymentString);

        String currencyCode = "";
        if (mQRData.getCurrencyCode() != null) {
            currencyCode = mQRData.getCurrencyCode().toString();
        }

        mView.setTotalAmount(mQRData.getTotal(), currencyCode);
        mView.setMerchantCode(mQRData.getMerchantCode());
        mView.setMerchantName(mQRData.getMerchantName());
        mView.setMerchantCity(mQRData.getMerchantCity());
    }
}
