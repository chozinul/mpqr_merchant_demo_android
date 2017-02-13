package com.mastercard.labs.mpqrmerchant.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mastercard.labs.mpqrmerchant.R;
import com.mastercard.labs.mpqrmerchant.login.LoginActivity;
import com.mastercard.labs.mpqrmerchant.main.MainActivity;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
