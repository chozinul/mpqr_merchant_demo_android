package com.mastercard.labs.mpqrmerchant;

import android.app.Application;
import android.content.SharedPreferences;

import com.mastercard.labs.mpqrmerchant.network.LoginManager;

import io.realm.Realm;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class MainApplication extends Application {
    private static final String SHARED_PREFERENCES_NAME = "MPQRMerchantSharedPreferences";

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        Realm.init(this);
        LoginManager.init(sharedPreferences);
    }
}
