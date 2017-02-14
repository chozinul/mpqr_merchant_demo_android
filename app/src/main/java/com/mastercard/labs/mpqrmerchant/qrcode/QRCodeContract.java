package com.mastercard.labs.mpqrmerchant.qrcode;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/14/17
 */
public interface QRCodeContract {
    interface View extends BaseView<Presenter> {

        void showQRCode(String paymentString);

        void setTotalAmount(double total, String currencyCode);

        void setMerchantCode(String merchantCode);

        void setMerchantName(String merchantName);

        void setMerchantCity(String merchantCity);
    }

    interface Presenter extends BasePresenter {

    }
}
