package com.mastercard.labs.mpqrmerchant.transaction.detail;

import com.mastercard.labs.mpqrmerchant.MainApplication;
import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/15/17
 */
class TransactionDetailPresenter implements TransactionDetailContract.Presenter {
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());

    private TransactionDetailContract.View mView;
    private String mReferenceId;

    private Transaction mTransaction;

    TransactionDetailPresenter(TransactionDetailContract.View view, String referenceId) {
        this.mView = view;
        this.mReferenceId = referenceId;
    }

    @Override
    public void start() {

        for (Transaction transaction : MainApplication.transactionList) {
            if (transaction.getReferenceId().equals(mReferenceId))
                mTransaction = transaction;
        }
        populateView();
    }

    private void populateView() {
        if (mTransaction == null) {
            mView.showInvalidTransactionError();
            return;
        }

        String currency = "";
        if (mTransaction.getCurrencyCode() != null) {
            currency = mTransaction.getCurrencyCode().toString();
        }

        mView.setTotalAmount(mTransaction.getTotal(), currency);
        mView.setTip(mTransaction.getTipAmount(), currency);
        mView.setTransactionDate(dateFormat.format(mTransaction.getTransactionDate()));
        mView.setInvoiceNumber(mTransaction.getInvoiceNumber());
        mView.setTerminalNumber(mTransaction.getTerminalNumber());
        mView.setReferenceId(mTransaction.getReferenceId());
    }
}
