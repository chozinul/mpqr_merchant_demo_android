package com.mastercard.labs.mpqrmerchant.transaction.list;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.adapter.TransactionsAdapter;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.event.TransactionsUpdateEvent;
import com.mastercard.labs.mpqrmerchant.transaction.detail.TransactionDetailActivity;
import com.mastercard.labs.mpqrmerchant.transaction.overview.TransactionOverviewFragment;
import com.mastercard.labs.mpqrmerchant.utils.DialogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransactionListActivity extends AppCompatActivity implements TransactionListContract.View, TransactionsAdapter.TransactionItemListener {
    public static String BUNDLE_USER_KEY = "userId";

    @BindView(R.id.rv_transactions)
    RecyclerView mTransactionsRecyclerView;

    private long mUserId;
    private TransactionListContract.Presenter mPresenter;
    private TransactionsAdapter mTransactionsAdapter;

    public static Intent newIntent(Context context, long userId) {
        Intent intent = new Intent(context, TransactionListActivity.class);

        intent.putExtra(BUNDLE_USER_KEY, userId);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_list);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTransactionsRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mTransactionsRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        mTransactionsRecyclerView.addItemDecoration(dividerItemDecoration);

        if (savedInstanceState != null) {
            mUserId = savedInstanceState.getLong(BUNDLE_USER_KEY, -1);
        } else {
            mUserId = getIntent().getLongExtra(BUNDLE_USER_KEY, -1);
        }

        mPresenter = new TransactionListPresenter(this, RealmDataSource.getInstance(), mUserId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTransactionsUpdateEvent(TransactionsUpdateEvent event) {
        mPresenter.start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putLong(BUNDLE_USER_KEY, mUserId);

        super.onSaveInstanceState(outState);
    }

    private TransactionOverviewFragment getTransactionOverviewFragment() {
        return (TransactionOverviewFragment) getSupportFragmentManager().findFragmentById(R.id.frg_transaction_overview);
    }

    @Override
    public void showInvalidUser() {
        DialogUtils.customAlertDialogBuilder(this, R.string.unexpected_error).setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    @Override
    public void setTransactionTotalAmount(double amount, String currencyCode) {
        TransactionOverviewFragment transactionOverviewFragment = getTransactionOverviewFragment();
        if (transactionOverviewFragment == null) {
            return;
        }
        transactionOverviewFragment.setTotalDailyAmount(currencyCode, amount);
    }

    @Override
    public void setTotalTransactions(int size) {
        TransactionOverviewFragment transactionOverviewFragment = getTransactionOverviewFragment();
        if (transactionOverviewFragment == null) {
            return;
        }
        transactionOverviewFragment.setTotalTransactions(size);
    }

    @Override
    public void setTransactions(List<Transaction> transactions) {
        mTransactionsAdapter = new TransactionsAdapter(transactions, this);
        mTransactionsRecyclerView.setAdapter(mTransactionsAdapter);
    }

    @Override
    public void onTransactionItemClicked(Transaction transaction) {
        Intent intent = TransactionDetailActivity.newIntent(this, transaction.getReferenceId());
        startActivity(intent);
    }
}
