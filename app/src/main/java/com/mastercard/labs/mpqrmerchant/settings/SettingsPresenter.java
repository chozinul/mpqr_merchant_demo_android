package com.mastercard.labs.mpqrmerchant.settings;

import com.mastercard.labs.mpqrmerchant.data.DataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Settings;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
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
        // Generate all Settings
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

        String mobile = "No number entered";

        if (mUser.getMobile() != null)
            if (!mUser.getMobile().trim().isEmpty())
                mobile = mUser.getMobile();


        allSettings = new ArrayList<>();

        allSettings.add(new Settings(mView.getMerchantNameTitle(), mUser.getName(), true));
        allSettings.add(new Settings(mView.getCountryTitle(), country, true));
        allSettings.add(new Settings(mView.getCityTitle(), mUser.getCity(), true));
        allSettings.add(new Settings(mView.getMerchantCategoryCodeTitle(), mUser.getCategoryCode(), false));
        allSettings.add(new Settings(mView.getCardNumberTitle(), formattedCardNumber(mUser.getIdentifierMastercard04()), true));
        allSettings.add(new Settings(mView.getCurrencyTitle(), currency, true));
        allSettings.add(new Settings(mView.getStoreIdTitle(), mUser.getStoreId(), false));
        allSettings.add(new Settings(mView.getTerminalIdTitle(), mUser.getTerminalNumber(), false));
        allSettings.add(new Settings(mView.getMobileTitle(), mobile, true));

        mView.showSettings(allSettings);
    }

    private String formattedCardNumber(String cardNumber) {
        cardNumber = cardNumber.replace(" ", "");

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
        } else if (settings.getTitle().equals(mView.getCardNumberTitle())) {
            mView.showCardNumberEditor(formattedCardNumber(mUser.getIdentifierMastercard04()));
        } else if (settings.getTitle().equals(mView.getCurrencyTitle())) {
            mView.showCurrencyEditor(mUser.getCurrencyNumericCode());
        } else if (settings.getTitle().equals(mView.getCityTitle()))
            mView.showCityEditor(mUser.getCity());
        else if (settings.getTitle().equals(mView.getCountryTitle())) {
            mView.showCountryEditor(mUser.getCountryCode());
        } else if (settings.getTitle().equals(mView.getMobileTitle())) {
            mView.showMobileEditor(mUser.getMobile());
        }
    }

    @Override
    public void updateMerchantName(String value) {
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

    @Override
    public void updateMerchantCard(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        Settings settings = setting(mView.getCardNumberTitle());
        if (settings == null) {
            return;
        }

        mUser.setIdentifierMastercard04(value);
        mUser = mDataSource.saveUser(mUser);

        settings.setValue(value);

        populateView();
    }

    @Override
    public void updateCountryCode(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        Settings settings = setting(mView.getCountryTitle());
        if (settings == null) {
            return;
        }

        mUser.setCountryCode(value);
        mUser = mDataSource.saveUser(mUser);

        settings.setValue(value);

        populateView();
    }

    @Override
    public void updateCityName(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        Settings settings = setting(mView.getCityTitle());
        if (settings == null) {
            return;
        }

        mUser.setCity(value);
        mUser = mDataSource.saveUser(mUser);

        settings.setValue(value);

        populateView();
    }

    @Override
    public void updateMobile(String value) {
        if (value == null)
            return;

        if (value.trim().isEmpty()) {
            value = null;
        }

        Settings settings = setting(mView.getMobileTitle());
        if (settings == null) {
            return;
        }

        mUser.setMobile(value);
        mUser = mDataSource.saveUser(mUser);

        settings.setValue(value);

        populateView();
    }

    @Override
    public void updateCurrencyCode(String value) {
        if (value == null || value.isEmpty()) {
            return;
        }

        Settings settings = setting(mView.getCurrencyTitle());
        if (settings == null) {
            return;
        }

        mUser.setCurrencyNumericCode(value);
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
