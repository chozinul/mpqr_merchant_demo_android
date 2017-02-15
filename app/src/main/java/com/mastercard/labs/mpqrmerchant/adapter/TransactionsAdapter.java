package com.mastercard.labs.mpqrmerchant.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/15/17
 */
public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm a", Locale.getDefault());

    private List<Transaction> transactions;

    public TransactionsAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        String currency = "";
        if (transaction.getCurrencyCode() != null) {
            currency = transaction.getCurrencyCode().toString() + " ";
        }

        holder.mAmountTextView.setText(String.format(Locale.getDefault(), "%s%,.2f", currency, transaction.getTotal()));
        holder.mInvoiceTextView.setText(String.format("INV %s", transaction.getInvoiceNumber()));
        holder.mDateTextView.setText(dateFormat.format(transaction.getTransactionDate()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_transaction_amount)
        TextView mAmountTextView;

        @BindView(R.id.txt_invoice_number)
        TextView mInvoiceTextView;

        @BindView(R.id.txt_transaction_date)
        TextView mDateTextView;

        ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
