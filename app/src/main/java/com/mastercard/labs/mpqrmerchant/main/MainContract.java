package com.mastercard.labs.mpqrmerchant.main;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;
import com.mastercard.labs.mpqrmerchant.data.model.QRData;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.List;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
interface MainContract {
    interface View extends BaseView<Presenter> {

        void setName(String name);

        void setCity(String city);

        void setTransactionTotalAmount(double amount, String currencyCode);

        void setTotalTransactions(int size);

        void setCurrency(String currency);

        void showInvalidDataError();

        void setAmount(double transactionAmount);

        void setFlatConvenienceFee(double tip);

        void enableTipChange();

        void setPercentageConvenienceFee(double tip);

        void setPromptToEnterTip();

        void showTipChangeNotAllowedError();

        void setNoTipRequired();

        void disableTipChange();

        void showCurrencies(List<CurrencyCode> currencies, int selected);

        void showTipTypes(List<QRData.TipType> tipTypes, int selected);

        void setTotalAmount(double amount, String currencyCode);
    }

    interface Presenter extends BasePresenter {

        void setAmount(double amount);

        void setTipType(QRData.TipType tipType);

        void setTip(double tipAmount);

        void setCurrencyCode(CurrencyCode currencyCode);

        void selectCurrency();

        void selectTipType();
    }
}
