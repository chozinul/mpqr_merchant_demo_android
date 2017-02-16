package com.mastercard.labs.mpqrmerchant.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.login.LoginActivity;
import com.mastercard.labs.mpqrmerchant.main.MainActivity;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.utils.DialogUtils;

public class SplashActivity extends AppCompatActivity {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                showUnrecoverableError();
            }
        } else {
            startNormalFlow();
        }
    }

    private void showUnrecoverableError() {
        DialogUtils.customAlertDialogBuilder(this, getString(R.string.common_google_play_services_unsupported_text, R.string.app_name)).setNegativeButton(R.string.dismiss, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PLAY_SERVICES_RESOLUTION_REQUEST:
                if (resultCode == RESULT_OK) {
                    startNormalFlow();
                    return;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
        showUnrecoverableError();
    }

    void startNormalFlow() {
        Intent intent;
        if (!LoginManager.getInstance().isUserLoggedIn()) {
            intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        startActivity(intent);
        finish();
    }
}
