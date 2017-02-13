package com.mastercard.labs.mpqrmerchant.login;

import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.network.ServiceGenerator;
import com.mastercard.labs.mpqrmerchant.network.request.LoginAccessCodeRequest;
import com.mastercard.labs.mpqrmerchant.network.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/7/17
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private DataSource dataSource;
    private Call<LoginResponse> loginRequest;

    public LoginPresenter(LoginContract.View view, DataSource dataSource) {
        this.mView = view;
        this.dataSource = dataSource;
    }

    @Override
    public void start() {

    }

    private boolean isPinValid(String pin) {
        return pin != null && pin.length() == 6;
    }

    private boolean isAccessCodeValid(String accessCode) {
        return accessCode != null && accessCode.length() == 8;
    }

    @Override
    public void login(String accessCode, String pin) {
        if (loginRequest != null) {
            return;
        }

        mView.clearAccessCodeError();
        mView.clearPinError();

        // Check for a valid access codes.
        if (!isAccessCodeValid(accessCode)) {
            mView.setInvalidAccessCode();
            return;
        }

        // Check for a valid pin, if the user entered one.
        if (!isPinValid(pin)) {
            mView.setInvalidPinError();
            return;
        }

        mView.showProgress();

        loginRequest = ServiceGenerator.getInstance().mpqrPaymentService().login(new LoginAccessCodeRequest(accessCode, pin));
        loginRequest.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                loginRequest = null;

                if (response.isSuccessful()) {
                    loginSuccess(response.body());

                    mView.startMainFlow();
                } else {
                    mView.setIncorrectPin();
                }

                mView.hideProgress();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                loginRequest = null;

                mView.hideProgress();

                mView.showNetworkError();
            }
        });
    }

    private void loginSuccess(LoginResponse response) {
        LoginManager.getInstance().setToken(response.getToken());

        dataSource.saveUser(response.getUser());

        LoginManager.getInstance().setLoggedInUserId(response.getUser().getId());
    }
}