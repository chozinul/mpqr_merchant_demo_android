package com.mastercard.labs.mpqrmerchant.transaction.list;

import com.mastercard.labs.mpqrmerchant.MainApplication;
import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.List;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/15/17
 */
public class TransactionListPresenter implements TransactionListContract.Presenter {
    private TransactionListContract.View mView;
    private DataSource mDataSource;
    private long mId;

    private User mUser;
    private List<Transaction> mTransactions;

    TransactionListPresenter(TransactionListContract.View view, DataSource dataSource, long merchantId) {
        this.mView = view;
        this.mDataSource = dataSource;
        this.mId = merchantId;
    }

    @Override
    public void start() {
        mUser = mDataSource.getUser(mId);
        if (mUser == null) {
            mView.showInvalidUser();
            return;
        }

        mTransactions = MainApplication.transactionList;

        populateView();
    }

    private void populateView() {
        double transactionTotal = 0;
        for (Transaction transaction : mTransactions) {
            transactionTotal += transaction.getTotal();
        }

        CurrencyCode merchantCurrencyCode = CurrencyCode.fromNumericCode(mUser.getCurrencyNumericCode());
        if (merchantCurrencyCode != null) {
            mView.setTransactionTotalAmount(transactionTotal, merchantCurrencyCode.toString());
        } else {
            mView.setTransactionTotalAmount(transactionTotal, "");
        }

        mView.setTotalTransactions(mTransactions.size());

        mView.setTransactions(mTransactions);
    }
}
