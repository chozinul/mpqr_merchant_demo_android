package com.mastercard.labs.mpqrmerchant;

import android.app.Application;
import android.content.SharedPreferences;

import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.utils.PreferenceManager;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class MainApplication extends Application {
    private static final String SHARED_PREFERENCES_NAME = "MPQRMerchantSharedPreferences";

    public static final String APP_VERSION = String.format("%s (v%s)", StringUtils.capitalize(BuildConfig.FLAVOR), BuildConfig.VERSION_NAME);

    protected static MainApplication appInstance = null;

    public static final ArrayList<Transaction> transactionList = new ArrayList<>(5);

    public static MainApplication getInstance() {
        return appInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);

        // Dummy set of transactions
        Date date = new Date();
        transactionList.add(new Transaction("R29D1023C1", 52.00, 0.00, "702", date, "0123123141231", "234234232"));
        transactionList.add(new Transaction("VS345023C1", 2.00, 2.00, "702", date, "0789643147897", "223486732"));
        transactionList.add(new Transaction("FW9D0456JG", 522.00, 0.23, "702", date, "0123123141231", "278904232"));
        transactionList.add(new Transaction("2K9D340JFW", 12.00, 0.30, "702", date, "8234423425231", "867865644"));
        transactionList.add(new Transaction("234D10K234", 3.00, 0.50, "702", date, "8565434534534", "357554645"));

        Realm.init(this);
        LoginManager.init(sharedPreferences);
        PreferenceManager.init(sharedPreferences);
    }
}
