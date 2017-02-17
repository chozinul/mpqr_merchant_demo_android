package com.mastercard.labs.mpqrmerchant.settings;

import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Settings;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/16/17
 */
public class SettingsPresenter implements SettingsContract.Presenter {
    private SettingsContract.View mView;
    private DataSource mDataSource;
    private long mId;

    private User mUser;
    private List<Settings> allSettings;

    SettingsPresenter(SettingsContract.View view, DataSource dataSource, long merchantId) {
        this.mView = view;
        this.mDataSource = dataSource;
        this.mId = merchantId;
    }

    @Override
    public void start() {
        mUser = mDataSource.getUser(mId);
        if (mUser == null) {
            mView.showUserNotFound();
            return;
        }

        populateView();
    }

    private void populateView() {
        // TODO: Find a proper way to inject resource strings
        // Generate allSettings
        String country = "UNKNOWN";
        if (mUser.getCountryCode() != null) {
            Locale countryLocale = new Locale("", mUser.getCountryCode());
            if (!countryLocale.getDisplayCountry().isEmpty()) {
                country = countryLocale.getDisplayCountry();
            }
        }

        String currency = "UNKNOWN";
        CurrencyCode merchantCurrencyCode = CurrencyCode.fromNumericCode(mUser.getCurrencyNumericCode());
        if (merchantCurrencyCode != null) {
            currency = merchantCurrencyCode.toString();
        }

        allSettings = new ArrayList<>();

        allSettings.add(new Settings(mView.getMerchantNameTitle(), mUser.getName(), true));
        allSettings.add(new Settings(mView.getCountryTitle(), country, false));
        allSettings.add(new Settings(mView.getCityTitle(), mUser.getCity(), false));
        allSettings.add(new Settings(mView.getMerchantCategoryCodeTitle(), mUser.getCategoryCode(), false));
        allSettings.add(new Settings(mView.getCardNumberTitle(), formattedCardNumber(mUser.getIdentifierMastercard04()), false));
        allSettings.add(new Settings(mView.getCurrencyTitle(), currency, false));
        allSettings.add(new Settings(mView.getStoreIdTitle(), mUser.getStoreId(), false));
        allSettings.add(new Settings(mView.getTerminalIdTitle(), mUser.getTerminalNumber(), false));

        mView.showSettings(allSettings);
    }

    private String formattedCardNumber(String cardNumber) {
        String formatted = "";
        for (int i = 0; i < cardNumber.length(); i++) {
            if (i % 4 == 0) {
                formatted += " ";
            }
            formatted += cardNumber.charAt(i);
        }

        return formatted.trim();
    }

    @Override
    public void settingsSelected(Settings settings) {
        if (!settings.isEditable()) {
            return;
        }

        if (settings.getTitle().equals(mView.getMerchantNameTitle())) {
            mView.showMerchantNameEditor(mUser.getName());
        }
    }

    @Override
    public void merchantNameUpdated(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        Settings settings = setting(mView.getMerchantNameTitle());
        if (settings == null) {
            return;
        }

        mUser.setName(value);
        mUser = mDataSource.saveUser(mUser);

        settings.setValue(value);

        populateView();
    }

    private Settings setting(String title) {
        for (Settings settings : allSettings) {
            if (settings.getTitle().equals(title)) {
                return settings;
            }
        }

        return null;
    }
}
