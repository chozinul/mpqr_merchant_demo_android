package com.mastercard.labs.mpqrmerchant.network.mock;

import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mastercard.labs.mpqrmerchant.BuildConfig;
import com.mastercard.labs.mpqrmerchant.MainApplication;
import com.mastercard.labs.mpqrmerchant.data.RealmDataSource;
import com.mastercard.labs.mpqrmerchant.data.model.Transaction;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.network.LoginManager;
import com.mastercard.labs.mpqrmerchant.network.MPQRPaymentService;
import com.mastercard.labs.mpqrmerchant.network.request.LoginAccessCodeRequest;
import com.mastercard.labs.mpqrmerchant.network.response.LoginResponse;
import com.mastercard.labs.mpqrmerchant.utils.PreferenceManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import io.realm.RealmList;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.Calls;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/3/17
 */
public class MockMPQRMerchantService implements MPQRPaymentService {
    private static final String MERCHANT_NAME_KEY = "merchantName";
    private static final String MERCHANT_IDENTIFIER_KEY = "merchantIdentifier";
    private static final String MERCHANT_COUNTRY_CODE_KEY = "merchantCountryCode";
    private static final String MERCHANT_CITY_KEY = "merchantCity";
    private static final String MERCHANT_CURRENCY_NUMERIC_CODE_KEY = "merchantCurrencyNumericCode";
    private static final String MERCHANT_TRANSACTIONS_LIST_KEY = "merchantTransactions";
    private static final String MERCHANT_PHONE_KEY = "merchantPhone";

    private static final String MERCHANT_CODE = "87654321";
    private static final String MERCHANT_PIN = "123456";
    private static String DEFAULT_MERCHANT_NAME;
    private static String DEFAULT_MERCHANT_COUNTRY_CODE;
    private static String DEFAULT_MERCHANT_CITY;
    private static String DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE;

    private static String DEFAULT_MERCHANT_IDENTIFIER;
    private static String DEFAULT_MERCHANT_PHONE;

    static {
        try {
            AssetManager assetManager = MainApplication.getInstance().getBaseContext().getAssets();
            Properties properties = new Properties();
            properties.load(assetManager.open("init.properties"));
            DEFAULT_MERCHANT_NAME = properties.getProperty(MERCHANT_NAME_KEY);
            DEFAULT_MERCHANT_COUNTRY_CODE = properties.getProperty(MERCHANT_COUNTRY_CODE_KEY);
            DEFAULT_MERCHANT_CITY = properties.getProperty(MERCHANT_CITY_KEY);
            DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE = properties.getProperty(MERCHANT_CURRENCY_NUMERIC_CODE_KEY);
            DEFAULT_MERCHANT_IDENTIFIER = properties.getProperty(MERCHANT_IDENTIFIER_KEY);
            DEFAULT_MERCHANT_PHONE = properties.getProperty(MERCHANT_PHONE_KEY, "");
        } catch (IOException e) {
            //swallow it
            DEFAULT_MERCHANT_NAME = "Go Go Transport";
            DEFAULT_MERCHANT_COUNTRY_CODE = "IN";
            DEFAULT_MERCHANT_CITY = "Delhi";
            DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE = "356";
            DEFAULT_MERCHANT_IDENTIFIER = "5555222233334444";
            DEFAULT_MERCHANT_PHONE = "";
        }
    }

    private final BehaviorDelegate<MPQRPaymentService> delegate;

    private Gson gson;

    public MockMPQRMerchantService(BehaviorDelegate<MPQRPaymentService> delegate) {
        this.delegate = delegate;
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
    }

    @Override
    public Call<LoginResponse> login(@Body LoginAccessCodeRequest request) {
        if (request.getAccessCode().length() == 0 || !request.getPin().equals(MERCHANT_PIN)) {
            ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"false\"}");
            return delegate.returning(Calls.response(Response.error(404, responseBody))).login(request);
        }

        String merchantName = PreferenceManager.getInstance().getString(MERCHANT_NAME_KEY, DEFAULT_MERCHANT_NAME);
        String merchantIdentifier = PreferenceManager.getInstance().getString(MERCHANT_IDENTIFIER_KEY, DEFAULT_MERCHANT_IDENTIFIER);
        String merchantCountryCode = PreferenceManager.getInstance().getString(MERCHANT_COUNTRY_CODE_KEY, DEFAULT_MERCHANT_COUNTRY_CODE);
        String merchantCity = PreferenceManager.getInstance().getString(MERCHANT_CITY_KEY, DEFAULT_MERCHANT_CITY);
        String merchantCurrencyNumericCode = PreferenceManager.getInstance().getString(MERCHANT_CURRENCY_NUMERIC_CODE_KEY, DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE);
        String merchantPhone = PreferenceManager.getInstance().getString(MERCHANT_PHONE_KEY, DEFAULT_MERCHANT_PHONE);

        String dummyResponse = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": 1,\n" +
                "    \"code\": \"" + MERCHANT_CODE + "\",\n" +
                "    \"name\": \"" + merchantName + "\",\n" +
                "    \"city\": \"" + merchantCity + "\",\n" +
                "    \"countryCode\": \"" + merchantCountryCode + "\",\n" +
                "    \"categoryCode\": \"1234\",\n" +
                "    \"currencyNumericCode\": \"" + merchantCurrencyNumericCode + "\",\n" +
                "    \"identifierMastercard04\": \"" + merchantIdentifier + "\",\n" +
                "    \"storeId\": \"87654321\",\n" +
                "    \"terminalNumber\": \"3124652125\",\n" +
                "    \"mobile\": \"" + merchantPhone + "\",\n" +
                "    \"transactions\": []\n" +
                "  },\n" +
                "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NjIsInR5cGUiOiJjb25zdW1lciIsImlhdCI6MTQ4NjUyNTcwOSwiZXhwIjoxNDg3ODIxNzA5fQ.QbRK_RG1yr40iKK2GKmnMoBKuLxLg-X2gsKPnolyJ7w\"\n" +
                "}";

        LoginResponse response = gson.fromJson(dummyResponse, LoginResponse.class);

        return delegate.returningResponse(response).login(request);
    }

    @Override
    public Call<Void> logout() {
        return delegate.returningResponse(null).logout();
    }

    @Override
    public Call<User> save(@Body User user) {
        PreferenceManager.getInstance().putString(MERCHANT_NAME_KEY, user.getName());
        PreferenceManager.getInstance().putString(MERCHANT_IDENTIFIER_KEY, user.getIdentifierMastercard04());
        PreferenceManager.getInstance().putString(MERCHANT_COUNTRY_CODE_KEY, user.getCountryCode());
        PreferenceManager.getInstance().putString(MERCHANT_CITY_KEY, user.getCity());
        PreferenceManager.getInstance().putString(MERCHANT_CURRENCY_NUMERIC_CODE_KEY, user.getCurrencyNumericCode());
        if (user.getMobile() != null && !user.getMobile().isEmpty()) {
            PreferenceManager.getInstance().putString(MERCHANT_PHONE_KEY, user.getMobile());
        } else {
            PreferenceManager.getInstance().removeValue(MERCHANT_PHONE_KEY);
        }

        return delegate.returningResponse(user).save(user);
    }
}
