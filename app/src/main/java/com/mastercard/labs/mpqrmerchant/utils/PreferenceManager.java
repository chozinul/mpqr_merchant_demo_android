package com.mastercard.labs.mpqrmerchant.utils;

import android.content.SharedPreferences;

import java.util.Set;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/23/17
 */
public class PreferenceManager {
    private static PreferenceManager INSTANCE;

    private SharedPreferences preferences;

    public static void init(SharedPreferences preferences) {
        INSTANCE = new PreferenceManager(preferences);
    }

    public static PreferenceManager getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Call `PreferenceManager.init(SharedPreferences)` before calling this method.");
        }
        return INSTANCE;
    }

    private PreferenceManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void putString(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void putStringSet(String key, Set<String> value) {
        preferences.edit().putStringSet(key, value).apply();
    }

    public Set<String> getStringSet(String key, Set<String> defaultValues) {
        return preferences.getStringSet(key, defaultValues);
    }

    public boolean removeValue(String key) {
        return preferences.edit().remove(key).commit();
    }
}
