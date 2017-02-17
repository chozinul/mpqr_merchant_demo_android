package com.mastercard.labs.mpqrmerchant.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.main.MainActivity;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.utils.KeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via access code & pin.
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private LoginContract.Presenter presenter;

    // UI references.
    @BindView(R.id.txt_access_code)
    EditText mAccessCodeEditText;

    @BindView(R.id.txt_pin)
    EditText mPinEditText;

    @BindView(R.id.login_progress)
    View mProgressView;

    @BindView(R.id.login_form)
    View mLoginFormView;

    @BindView(R.id.sign_in_btn)
    Button mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        mPinEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    login();
                    return true;
                }
                return false;
            }
        });

        presenter = new LoginPresenter(this, RealmDataSource.getInstance());
    }

    @Override
    protected void onResume() {
        super.onResume();

        presenter.start();
    }

    @OnClick(value = R.id.sign_in_btn)
    public void signInButtonPressed() {
        login();
    }

    private void login() {
        KeyboardUtils.hideKeyboard(this);
        presenter.login(mAccessCodeEditText.getText().toString(), mPinEditText.getText().toString());
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });

        mSignInButton.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public void clearAccessCodeError() {
        mAccessCodeEditText.setError(null);
        mAccessCodeEditText.clearFocus();
    }

    @Override
    public void clearPinError() {
        mPinEditText.setError(null);
        mPinEditText.clearFocus();
    }

    @Override
    public void showProgress() {
        showProgress(true);
    }

    @Override
    public void hideProgress() {
        showProgress(false);
    }

    @Override
    public void startMainFlow() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void setIncorrectPin() {
        mPinEditText.setError(getString(R.string.error_incorrect_pin));
        mPinEditText.requestFocus();
    }

    @Override
    public void setInvalidPinError() {
        mPinEditText.setError(getString(R.string.error_incorrect_pin));
        mPinEditText.requestFocus();
    }

    @Override
    public void showNetworkError() {
        Toast.makeText(this, getString(R.string.unexpected_error), Toast.LENGTH_LONG).show();
    }

    @Override
    public void setInvalidAccessCode() {
        mAccessCodeEditText.setError(getString(R.string.error_invalid_access_code, getString(R.string.access_code_length)));
        mAccessCodeEditText.requestFocus();
    }
}

