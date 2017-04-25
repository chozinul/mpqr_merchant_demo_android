package com.mastercard.labs.mpqrmerchant.network;

import com.google.firebase.messaging.FirebaseMessaging;

import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.mastercard.labs.mpqrmerchant.MainApplication;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.User;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/8/17
 */
public class LoginManager {
    private static LoginManager INSTANCE;
    private SharedPreferences preferences;

    private static final String USER_ID_KEY = "userId";
    private static final String TOKEN_KEY = "token";
    private static final String SUBSCRIBED_TOPICS = "subscriptions";
    private static final String LAST_ACCESS_CODE_KEY = "lastAccessCode";
    private static String DEFAULT_LAST_ACCESS_CODE = "";

    private static final Pattern firebaseTopicPattern = Pattern.compile("[a-zA-Z0-9-_.~%]{1,900}");

    public static void init(SharedPreferences sharedPreferences) {
        INSTANCE = new LoginManager(sharedPreferences);
    }

    public static LoginManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Call `LoginManager.init(SharedPreferences)` before calling this method.");
        }
        return INSTANCE;
    }

    private LoginManager(SharedPreferences sharedPreferences) {
        this.preferences = sharedPreferences;
        try {
            AssetManager assetManager = MainApplication.getInstance().getBaseContext().getAssets();
            Properties properties = new Properties();
            properties.load(assetManager.open("init.properties"));
            DEFAULT_LAST_ACCESS_CODE = properties.getProperty(LAST_ACCESS_CODE_KEY);
        } catch (IOException e) {
            //swallow it
            DEFAULT_LAST_ACCESS_CODE = "87654321";
        }
    }

    public long getLoggedInUserId() {
        return preferences.getLong(USER_ID_KEY, -1);
    }

    public void setLoggedInUserId(long userId) {
        preferences.edit().putLong(USER_ID_KEY, userId).apply();

        User user = RealmDataSource.getInstance().getUser(userId);
        if (user != null) {
            preferences.edit().putString(LAST_ACCESS_CODE_KEY, user.getCode()).apply();
        }

        subscribeToNotifications();
    }

    public boolean isUserLoggedIn() {
        return getLoggedInUserId() != -1;
    }

    public String getToken() {
        return preferences.getString(TOKEN_KEY, null);
    }

    public void setToken(String token) {
        preferences.edit().putString(TOKEN_KEY, token).apply();
    }

    public void logout() {
        unsubscribeFromNotifications();

        RealmDataSource.getInstance().deleteUser(getLoggedInUserId());

        setLoggedInUserId(-1);
        setToken(null);
    }

    public void subscribeToNotifications() {
        User user = RealmDataSource.getInstance().getUser(getLoggedInUserId());

        Set<String> subscriptions = new HashSet<>();
        if (user != null) {
            if (user.getIdentifierMastercard04() != null) {
                subscriptions.add(user.getIdentifierMastercard04());
            }
            if (user.getIdentifierMastercard05() != null) {
                subscriptions.add(user.getIdentifierMastercard05());
            }
            if (user.getIdentifierVisa02() != null) {
                subscriptions.add(user.getIdentifierVisa02());
            }
            if (user.getIdentifierVisa03() != null) {
                subscriptions.add(user.getIdentifierVisa03());
            }
            if (user.getIdentifierNPCI06() != null) {
                subscriptions.add(user.getIdentifierNPCI06());
            }
            if (user.getIdentifierNPCI07() != null) {
                subscriptions.add(user.getIdentifierNPCI07());
            }
        }

        preferences.edit().putStringSet(SUBSCRIBED_TOPICS, subscriptions).apply();

        for (String sub : subscriptions) {
            if (firebaseTopicPattern.matcher(sub).matches()) {
                FirebaseMessaging.getInstance().subscribeToTopic(sub);
            }
        }
    }

    public void unsubscribeFromNotifications() {
        Set<String> subscriptions = preferences.getStringSet(SUBSCRIBED_TOPICS, null);
        if (subscriptions == null) {
            return;
        }

        for (String sub : subscriptions) {
            if (firebaseTopicPattern.matcher(sub).matches()) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(sub);
            }
        }
    }

    public String lastAccessToken() {
        return preferences.getString(LAST_ACCESS_CODE_KEY, DEFAULT_LAST_ACCESS_CODE);
    }
}
