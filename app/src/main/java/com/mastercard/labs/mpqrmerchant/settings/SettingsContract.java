package com.mastercard.labs.mpqrmerchant.settings;

import com.mastercard.labs.mpqrmerchant.BasePresenter;
import com.mastercard.labs.mpqrmerchant.BaseView;
import com.mastercard.labs.mpqrmerchant.data.model.Settings;

import java.util.List;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/16/17
 */
public interface SettingsContract {
    interface View extends BaseView<Presenter> {

        void showUserNotFound();

        String getMerchantNameTitle();

        String getCountryTitle();

        String getCityTitle();

        String getMerchantCategoryCodeTitle();

        String getCardNumberTitle();

        String getCurrencyTitle();

        String getStoreIdTitle();

        String getTerminalIdTitle();

        String getMobileTitle();

        void showSettings(List<Settings> allSettings);

        void showMerchantNameEditor(String name);

        void showCardNumberEditor(String cardNumber);

        void showCurrencyEditor(String currency);

        void showCountryEditor(String country);

        void showCityEditor(String city);

        void showMobileEditor(String mobile);
    }

    interface Presenter extends BasePresenter {

        void settingsSelected(Settings settings);

        void updateMerchantName(String value);

        void updateMerchantCard(String value);

        void updateCurrencyCode(String value);

        void updateCountryCode(String value);

        void updateCityName(String value);

        void updateMobile(String value);

    }
}
