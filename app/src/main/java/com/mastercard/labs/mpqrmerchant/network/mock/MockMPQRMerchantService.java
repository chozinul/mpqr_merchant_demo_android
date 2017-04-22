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

    private static final String MERCHANT_CODE = "87654321";
    private static final String MERCHANT_PIN = "123456";
    private static String DEFAULT_MERCHANT_NAME;
    private static String DEFAULT_MERCHANT_COUNTRY_CODE;
    private static String DEFAULT_MERCHANT_CITY;
    private static String DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE;

    private static String DEFAULT_MERCHANT_IDENTIFIER;

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
        } catch (IOException e) {
            //swallow it
            DEFAULT_MERCHANT_NAME = "Go Go Transport";
            DEFAULT_MERCHANT_COUNTRY_CODE = "IN";
            DEFAULT_MERCHANT_CITY = "Delhi";
            DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE = "356";
            if (BuildConfig.FLAVOR.equals("india")) {
                DEFAULT_MERCHANT_IDENTIFIER = "5555666677778888";
            } else {
                DEFAULT_MERCHANT_IDENTIFIER = "5555222233334444";
            }
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
        if (!request.getAccessCode().equals(MERCHANT_CODE) || !request.getPin().equals(MERCHANT_PIN)) {
            ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"false\"}");
            return delegate.returning(Calls.response(Response.error(404, responseBody))).login(request);
        }

        String merchantName = PreferenceManager.getInstance().getString(MERCHANT_NAME_KEY, DEFAULT_MERCHANT_NAME);
        String merchantIdentifier = PreferenceManager.getInstance().getString(MERCHANT_IDENTIFIER_KEY, DEFAULT_MERCHANT_IDENTIFIER);
        String merchantCountryCode = PreferenceManager.getInstance().getString(MERCHANT_COUNTRY_CODE_KEY, DEFAULT_MERCHANT_COUNTRY_CODE);
        String merchantCity = PreferenceManager.getInstance().getString(MERCHANT_CITY_KEY, DEFAULT_MERCHANT_CITY);
        String merchantCurrencyNumericCode = PreferenceManager.getInstance().getString(MERCHANT_CURRENCY_NUMERIC_CODE_KEY, DEFAULT_MERCHANT_CURRENCY_NUMERIC_CODE);

        // TODO: Handle version updates because that might invalidate stored data in preferences and cause exceptions while parsing as JSON
        // Parse stored transactions
        Set<String> transactions = PreferenceManager.getInstance().getStringSet(MERCHANT_TRANSACTIONS_LIST_KEY, new HashSet<String>());
        List<Transaction> transactionList = new ArrayList<>(transactions.size());
        for (String transaction : transactions) {
            transactionList.add(gson.fromJson(transaction, Transaction.class));
        }

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
                "    \"transactions\": []\n" +
                "  },\n" +
                "  \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6NjIsInR5cGUiOiJjb25zdW1lciIsImlhdCI6MTQ4NjUyNTcwOSwiZXhwIjoxNDg3ODIxNzA5fQ.QbRK_RG1yr40iKK2GKmnMoBKuLxLg-X2gsKPnolyJ7w\"\n" +
                "}";

        LoginResponse response = gson.fromJson(dummyResponse, LoginResponse.class);
        response.getUser().setTransactions(new RealmList<>(transactionList.toArray(new Transaction[]{})));

        return delegate.returningResponse(response).login(request);
    }

    @Override
    public Call<Void> logout() {
        // Save transactions during logout so we can retrieve later
        List<Transaction> transactions = RealmDataSource.getInstance().getTransactions(LoginManager.getInstance().getLoggedInUserId());
        if (transactions != null) {
            Set<String> jsonTransactions = new HashSet<>(transactions.size());
            for (Transaction transaction : transactions) {
                try {
                    jsonTransactions.add(gson.toJson(transaction));
                } catch (Exception ex) {
                    // Ignore exception
                    ex.printStackTrace();
                }
            }

            PreferenceManager.getInstance().putStringSet(MERCHANT_TRANSACTIONS_LIST_KEY, jsonTransactions);
        }

        return delegate.returningResponse(null).logout();
    }

    @Override
    public Call<User> save(@Body User user) {
        PreferenceManager.getInstance().putString(MERCHANT_NAME_KEY, user.getName());
        PreferenceManager.getInstance().putString(MERCHANT_IDENTIFIER_KEY, user.getIdentifierMastercard04());
        PreferenceManager.getInstance().putString(MERCHANT_COUNTRY_CODE_KEY, user.getCountryCode());
        PreferenceManager.getInstance().putString(MERCHANT_CITY_KEY, user.getCity());
        PreferenceManager.getInstance().putString(MERCHANT_CURRENCY_NUMERIC_CODE_KEY, user.getCurrencyNumericCode());

        return delegate.returningResponse(user).save(user);
    }
}
