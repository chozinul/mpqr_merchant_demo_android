package com.mastercard.labs.mpqrmerchant.transaction.list;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;

import java.util.List;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/15/17
 */
public interface TransactionListContract {
    interface View extends BaseView<Presenter> {

        void showInvalidUser();

        void setTransactionTotalAmount(double transactionTotal, String currencyCode);

        void setTotalTransactions(int total);

        void setTransactions(List<Transaction> transactions);
    }

    interface Presenter extends BasePresenter {

    }
}
