package com.mastercard.labs.mpqrmerchant.transaction.detail;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/15/17
 */
public interface TransactionDetailContract {
    interface View extends BaseView<Presenter> {

        void showInvalidTransactionError();

        void setTotalAmount(double total, String currency);

        void setTip(double tipAmount, String currency);

        void setTransactionDate(String dateString);

        void setInvoiceNumber(String invoiceNumber);

        void setTerminalNumber(String terminalNumber);

        void setReferenceId(String referenceId);
    }

    interface Presenter extends BasePresenter {

    }
}
