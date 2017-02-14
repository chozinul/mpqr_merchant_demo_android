package com.mastercard.labs.mpqrmerchant.main;

import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.QRData;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.Arrays;
import java.util.List;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private DataSource mDataSource;
    private long mId;

    private User mUser;

    private QRData qrData;

    public MainPresenter(MainContract.View view, DataSource dataSource, long merchantId) {
        this.mView = view;
        this.mDataSource = dataSource;
        this.mId = merchantId;

        this.qrData = new QRData();
    }

    @Override
    public void start() {
        mUser = mDataSource.getUser(mId);
        if (mUser == null) {
            mView.showInvalidDataError();
            return;
        }

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
        qrData.setMerchantTerminalId(mUser.getTerminalId());

        qrData.setTransactionAmount(0);
        qrData.setTipType(QRData.TipType.FLAT);
        qrData.setTip(0);
        qrData.setCurrencyNumericCode(mUser.getCurrencyNumericCode());

        populateView();
    }

    private void populateView() {
        double transactionTotal = 0;
        for (Transaction transaction : mUser.getTransactions()) {
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

        mView.setTotalTransactions(mUser.getTransactions().size());

        CurrencyCode currencyCode = qrData.getCurrencyCode();
        if (currencyCode != null) {
            mView.setCurrency(currencyCode.toString());
        } else {
            mView.showInvalidDataError();
            return;
        }

        mView.setAmount(qrData.getTransactionAmount());

        updateTipView();
        updateTotal();
    }

    private void updateTipView() {
        double tip = qrData.getTip();

        switch (qrData.getTipType()) {
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

        updateTotal();
    }

    @Override
    public void setTipType(QRData.TipType tipType) {
        qrData.setTipType(tipType);

        if (tipType == QRData.TipType.NONE || tipType == QRData.TipType.PROMPT) {
            qrData.setTip(0);
        }

        updateTipView();
        updateTotal();
    }

    @Override
    public void setTip(double tipAmount) {
        if (QRData.TipType.NONE == qrData.getTipType()) {
            mView.showTipChangeNotAllowedError();
            return;
        }

        qrData.setTip(tipAmount);

        updateTotal();
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
        // TODO: Validate QRData

        PushPaymentData paymentData = new PushPaymentData();
        // Version 01
        paymentData.setPayloadFormatIndicator("01");
        // 11 (static) if transaction amount is not provided else 12 (dynamic)
        paymentData.setPointOfInitiationMethod("1" + (qrData.getTransactionAmount() == 0 ? "1" : "2"));
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

        paymentData.setTransactionAmount(qrData.getTransactionAmount());
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
                paymentData.setTipOrConvenienceIndicator(null);
                break;
        }
        paymentData.setTransactionCurrencyCode(qrData.getCurrencyNumericCode());

        if (qrData.getMerchantStoreId() != null || qrData.getMerchantTerminalId() != null) {
            AdditionalData additionalData = new AdditionalData();
            additionalData.setStoreId(qrData.getMerchantStoreId());
            additionalData.setTerminalId(qrData.getMerchantTerminalId());
            paymentData.setAdditionalData(additionalData);
        }

        try {
            paymentData.validate();
        } catch (FormatException ex) {
            // TODO: Handle all kind of exceptions
            ex.printStackTrace();
            mView.showInvalidDataError();
            return;
        }

        mView.showQRCode(qrData.getMerchantCode(), paymentData.toString());
    }
}
