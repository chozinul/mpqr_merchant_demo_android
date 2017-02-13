package com.mastercard.labs.mpqrmerchant.main;

import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.User;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;
    private DataSource mDataSource;
    private long mMerchantId;

    private User mUser;

    public MainPresenter(MainContract.View view, DataSource dataSource, long merchantId) {
        this.mView = view;
        this.mDataSource = dataSource;
        this.mMerchantId = merchantId;
    }

    @Override
    public void start() {
        mUser = mDataSource.getUser(mMerchantId);
        if (mUser == null) {
            // TODO: Throw error
        }

        populateView();
    }

    private void populateView() {

    }
}
