package com.mastercard.labs.mpqrmerchant.main;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
interface MainContract {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

    }
}
