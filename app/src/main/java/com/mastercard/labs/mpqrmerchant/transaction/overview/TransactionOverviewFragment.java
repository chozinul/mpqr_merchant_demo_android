package com.mastercard.labs.mpqrmerchant.transaction.overview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass. Use the {@link TransactionOverviewFragment#newInstance}
 * factory method to create an instance of this fragment.
 */
public class TransactionOverviewFragment extends Fragment {
    private static final String ARG_TRANSACTION_CURRENCY = "transactionCurrency";
    private static final String ARG_TRANSACTIONS_TOTAL_AMOUNT = "transactionsTotalAmount";
    private static final String ARG_TOTAL_TRANSACTIONS = "totalTransactions";

    private String transactionCurrency;
    private double transactionsTotalAmount;
    private int totalTransactions;

    @BindView(R.id.txt_total_daily_value)
    TextView totalDailyAmountTextView;

    @BindView(R.id.txt_transactions_daily_value)
    TextView totalDailyTransactionsTextView;

    private Unbinder unbinder;

    public TransactionOverviewFragment() {
        // Required empty public constructor
    }

    public static TransactionOverviewFragment newInstance(String transactionCurrency, double transactionsTotalAmount, int totalTransactions) {
        TransactionOverviewFragment fragment = new TransactionOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSACTION_CURRENCY, transactionCurrency);
        args.putDouble(ARG_TRANSACTIONS_TOTAL_AMOUNT, transactionsTotalAmount);
        args.putInt(ARG_TOTAL_TRANSACTIONS, totalTransactions);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transactionCurrency = getArguments().getString(ARG_TRANSACTION_CURRENCY);
            transactionsTotalAmount = getArguments().getDouble(ARG_TRANSACTIONS_TOTAL_AMOUNT);
            totalTransactions = getArguments().getInt(ARG_TRANSACTIONS_TOTAL_AMOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_overview, container, false);
        unbinder = ButterKnife.bind(this, view);

        setTotalDailyAmount(transactionCurrency, transactionsTotalAmount);
        setTotalTransactions(totalTransactions);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    public void setTotalDailyAmount(String currencyCode, double amount) {
        this.transactionCurrency = currencyCode;
        this.transactionsTotalAmount = amount;

        totalDailyAmountTextView.setText(String.format(Locale.getDefault(), "%s %,.2f", currencyCode, amount));
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;

        totalDailyTransactionsTextView.setText(String.valueOf(totalTransactions));
    }
}
