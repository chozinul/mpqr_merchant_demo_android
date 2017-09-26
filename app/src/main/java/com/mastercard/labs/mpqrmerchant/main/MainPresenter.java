package com.mastercard.labs.mpqrmerchant.main;

import com.mastercard.labs.mpqrmerchant.MainApplication;
import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.QRData;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.network.ServiceGenerator;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;
import com.mastercard.mpqr.pushpayment.exception.FormatException;
import com.mastercard.mpqr.pushpayment.exception.InvalidTagValueException;
import com.mastercard.mpqr.pushpayment.exception.RFUTagException;
import com.mastercard.mpqr.pushpayment.model.AdditionalData;
import com.mastercard.mpqr.pushpayment.model.PushPaymentData;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private DataSource mDataSource;
    private long mId;

    private User mUser;

    private QRData qrData = new QRData();

    MainPresenter(MainContract.View view, DataSource dataSource, long merchantId) {
        this.mView = view;
        this.mDataSource = dataSource;
        this.mId = merchantId;
    }

    @Override
    public void start() {
        mUser = mDataSource.getUser(mId);
        if (mUser == null) {
            mView.showUserNotFound();
            return;
        }

        fillQRData();

        populateView();
    }

    private void fillQRData() {
        qrData = new QRData();

        qrData.setMerchantCode(mUser.getCode());
        qrData.setMerchantName(mUser.getName());
        qrData.setMerchantCity(mUser.getCity());
        qrData.setMerchantCountryCode(mUser.getCountryCode());
        qrData.setMerchantCategoryCode(mUser.getCategoryCode());
        qrData.setMerchantIdentifierVisa02(mUser.getIdentifierVisa02());
        qrData.setMerchantIdentifierVisa03(mUser.getIdentifierVisa03());
        qrData.setMerchantIdentifierMastercard04(mUser.getIdentifierMastercard04());
        qrData.setMerchantIdentifierMastercard05(mUser.getIdentifierMastercard05());
        qrData.setMerchantIdentifierNPCI06(mUser.getIdentifierNPCI06());
        qrData.setMerchantIdentifierNPCI07(mUser.getIdentifierNPCI07());
        qrData.setMerchantStoreId(mUser.getStoreId());
        qrData.setMerchantTerminalNumber(mUser.getTerminalNumber());
        if (mUser.getMobile() != null && !mUser.getMobile().isEmpty()) {
            qrData.setMerchantMobile(mUser.getMobile());
        }
        qrData.setTransactionAmount(0);
        qrData.setTipType(QRData.TipType.NONE);
        qrData.setTip(0);
        qrData.setCurrencyNumericCode(mUser.getCurrencyNumericCode());
    }

    private void populateView() {
        double transactionTotal = 0;
        for (Transaction transaction : MainApplication.transactionList) {
            transactionTotal += transaction.getTotal();
        }

        mView.setName(mUser.getName());
        mView.setCity(mUser.getCity());

        CurrencyCode merchantCurrencyCode = CurrencyCode.fromNumericCode(mUser.getCurrencyNumericCode());
        if (merchantCurrencyCode != null) {
            mView.setTransactionTotalAmount(transactionTotal, merchantCurrencyCode.toString());
        } else {
            mView.setTransactionTotalAmount(transactionTotal, "");
        }

        mView.setTotalTransactions(MainApplication.transactionList.size());

        CurrencyCode currencyCode = qrData.getCurrencyCode();
        if (currencyCode != null) {
            mView.setCurrency(currencyCode.toString());
        } else {
            mView.showInvalidDataError();
            return;
        }

        setAmount(qrData.getTransactionAmount());

        updateTipAndTotalView();
    }

    private void updateTipAndTotalView() {
        double tip = qrData.getTip();

        QRData.TipType tipType = qrData.getTipType();
        if (tipType == null) {
            tipType = QRData.TipType.NONE;
        }

        switch (tipType) {
            case FLAT:
                mView.setFlatConvenienceFee(tip);
                mView.enableTipChange();
                break;
            case PERCENTAGE:
                mView.setPercentageConvenienceFee(tip);
                mView.enableTipChange();
                break;
            case PROMPT:
                mView.setPromptToEnterTip();
                mView.disableTipChange();
                break;
            case NONE:
                mView.setNoTipRequired();
                mView.disableTipChange();
        }

        updateTotal();
    }

    @Override
    public void setAmount(double amount) {
        qrData.setTransactionAmount(amount);

        if (amount == 0) {
            mView.setStaticAmountTitle();
        } else {
            mView.setDynamicAmountTitle();
        }

        mView.setAmount(amount);

        updateTotal();
    }

    @Override
    public void setTipType(QRData.TipType tipType) {
        // Reset tips
        qrData.setTip(0);
        qrData.setTipType(tipType);

        updateTipAndTotalView();
    }

    @Override
    public void setTip(double tipAmount) {
        if (QRData.TipType.NONE == qrData.getTipType()) {
            mView.showTipChangeNotAllowedError();
            return;
        }

        qrData.setTip(tipAmount);

        updateTipAndTotalView();
    }

    @Override
    public void setCurrencyCode(CurrencyCode currencyCode) {
        // Assign only if valid numeric code
        if (currencyCode != null) {
            qrData.setCurrencyNumericCode(currencyCode.getNumericCode());
            mView.setCurrency(currencyCode.toString());
            updateTotal();
        } else {
            mView.showInvalidDataError();
        }
    }

    private void updateTotal() {
        double total = qrData.getTotal();

        String currencyCode = "";
        if (qrData.getCurrencyCode() != null) {
            currencyCode = qrData.getCurrencyCode().toString();
        }

        mView.setTotalAmount(total, currencyCode);
    }

    @Override
    public void selectCurrency() {
        List<CurrencyCode> currencies = Arrays.asList(CurrencyCode.INR, CurrencyCode.SGD);
        CurrencyCode currencyCode = qrData.getCurrencyCode();
        mView.showCurrencies(currencies, currencyCode == null ? -1 : currencies.indexOf(currencyCode));
    }

    @Override
    public void selectTipType() {
        List<QRData.TipType> tipTypes = Arrays.asList(QRData.TipType.values());
        QRData.TipType selected = qrData.getTipType();
        mView.showTipTypes(tipTypes, selected == null ? -1 : tipTypes.indexOf(selected));
    }

    @Override
    public void generateQRString() {

        PushPaymentData paymentData = new PushPaymentData();

        paymentData.setPayloadFormatIndicator("01");

        if (qrData.getTransactionAmount() != 0) {
            paymentData.setTransactionAmount(qrData.getTransactionAmount());
        }
        paymentData.setMerchantName(qrData.getMerchantName());
        paymentData.setMerchantCity(qrData.getMerchantCity());
        paymentData.setCountryCode(qrData.getMerchantCountryCode());
        paymentData.setMerchantCategoryCode(qrData.getMerchantCategoryCode());
        if (qrData.getMerchantIdentifierVisa02() != null) {
            paymentData.setMerchantIdentifierVisa02(qrData.getMerchantIdentifierVisa02());
        }
        if (qrData.getMerchantIdentifierVisa03() != null) {
            paymentData.setMerchantIdentifierVisa03(qrData.getMerchantIdentifierVisa03());
        }
        if (qrData.getMerchantIdentifierMastercard04() != null) {
            paymentData.setMerchantIdentifierMastercard04(qrData.getMerchantIdentifierMastercard04());
        }
        if (qrData.getMerchantIdentifierMastercard05() != null) {
            paymentData.setMerchantIdentifierMastercard05(qrData.getMerchantIdentifierMastercard05());
        }
        if (qrData.getMerchantIdentifierNPCI06() != null) {
            paymentData.setMerchantIdentifierNPCI06(qrData.getMerchantIdentifierNPCI06());
        }
        if (qrData.getMerchantIdentifierNPCI07() != null) {
            paymentData.setMerchantIdentifierNPCI07(qrData.getMerchantIdentifierNPCI07());
        }

        switch (qrData.getTipType()) {
            case FLAT:
                paymentData.setTipOrConvenienceIndicator(PushPaymentData.TipConvenienceIndicator.FLAT_CONVENIENCE_FEE);
                paymentData.setValueOfConvenienceFeeFixed(qrData.getTip());
                break;
            case PERCENTAGE:
                paymentData.setTipOrConvenienceIndicator(PushPaymentData.TipConvenienceIndicator.PERCENTAGE_CONVENIENCE_FEE);
                paymentData.setValueOfConvenienceFeePercentage(qrData.getTip());
                break;
            case PROMPT:
                paymentData.setTipOrConvenienceIndicator(PushPaymentData.TipConvenienceIndicator.PROMTED_TO_ENTER_TIP);
                break;
            case NONE:
                break;
        }
        paymentData.setTransactionCurrencyCode(qrData.getCurrencyNumericCode());

        if (qrData.getMerchantStoreId() != null || qrData.getMerchantTerminalNumber() != null || qrData.getMerchantMobile() != null) {
            AdditionalData additionalData = new AdditionalData();
            if (qrData.getMerchantStoreId() != null) {
                additionalData.setStoreId(qrData.getMerchantStoreId());
            }
            if (qrData.getMerchantTerminalNumber() != null) {
                additionalData.setTerminalId(qrData.getMerchantTerminalNumber());
            }
            if (qrData.getMerchantMobile() != null) {
                additionalData.setMobileNumber(qrData.getMerchantMobile());
            }
            paymentData.setAdditionalData(additionalData);
        }

        try {
            String paymentString = paymentData.generatePushPaymentString();
            mView.showQRCode(qrData, paymentString);
            mView.showExceptionMessage(paymentString);
        } catch (Exception e) {
            mView.showExceptionMessage(e.getMessage());
            if (e instanceof RFUTagException)
                mView.showExceptionMessage("Error at: " + ((RFUTagException) e).getTag() + ", field is reserved for use and should not be entered");
            else if (e instanceof InvalidTagValueException)
                mView.showExceptionMessage("Error at: " + ((InvalidTagValueException) e).getTagString() + ", Price fields entered cannot be zero and must align with currency's decimal rules");
        }

    }

    @Override
    public void logout() {
        mView.showLogoutProgress();

        ServiceGenerator.getInstance().mpqrPaymentService().logout().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mView.hideLogoutProgress();

                LoginManager.getInstance().logout();

                mView.showLoginActivity();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mView.hideLogoutProgress();

                if (!call.isCanceled()) {
                    mView.showLogoutFailed();
                }
            }
        });
    }

}
