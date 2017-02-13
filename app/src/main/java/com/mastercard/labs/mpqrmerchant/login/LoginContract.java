package com.mastercard.labs.mpqrmerchant.login;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/7/17
 */
public interface LoginContract {
    interface View extends BaseView<Presenter> {

        void setInvalidPinError();

        void clearAccessCodeError();

        void clearPinError();

        void showProgress();

        void hideProgress();

        void startMainFlow();

        void setIncorrectPin();

        void showNetworkError();

        void setInvalidAccessCode();
    }

    interface Presenter extends BasePresenter {

        void login(String accessCode, String pin);
    }
}
