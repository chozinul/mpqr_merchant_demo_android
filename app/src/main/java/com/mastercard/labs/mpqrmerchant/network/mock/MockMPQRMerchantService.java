package com.mastercard.labs.mpqrmerchant.network.mock;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mastercard.labs.mpqrmerchant.BuildConfig;
import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.network.MPQRPaymentService;
import com.mastercard.labs.mpqrmerchant.network.request.LoginAccessCodeRequest;
import com.mastercard.labs.mpqrmerchant.network.response.LoginResponse;
import com.mastercard.labs.mpqrmerchant.utils.CalculateCode;
import com.mastercard.labs.mpqrmerchant.utils.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    private static final String MERCHANT_CODE_KEY = "merchantCode";

    private static final String MERCHANT_PIN = "123456";
    private static final String DEFAULT_MERCHANT_NAME = "Go Go Transport";
    private static final String DEFAULT_MERCHANT_CODE;
    private static final String DEFAULT_MERCHANT_IDENTIFIER;
    static {
        if (BuildConfig.FLAVOR.equals("india")) {
            DEFAULT_MERCHANT_IDENTIFIER = "5555666677778888";
            DEFAULT_MERCHANT_CODE = "55667788";
        } else {
            DEFAULT_MERCHANT_IDENTIFIER = "5555222233334444";
            DEFAULT_MERCHANT_CODE = "55223344";
        }
    }

    private final BehaviorDelegate<MPQRPaymentService> delegate;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault());
    private Gson gson;

    public MockMPQRMerchantService(BehaviorDelegate<MPQRPaymentService> delegate) {
        this.delegate = delegate;
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
    }

    @Override
    public Call<LoginResponse> login(@Body LoginAccessCodeRequest request) {


        String merchantName = PreferenceManager.getInstance().getString(MERCHANT_NAME_KEY, DEFAULT_MERCHANT_NAME);
        String merchantIdentifier = PreferenceManager.getInstance().getString(MERCHANT_IDENTIFIER_KEY, DEFAULT_MERCHANT_IDENTIFIER);
        String merchantCode = PreferenceManager.getInstance().getString(MERCHANT_CODE_KEY, DEFAULT_MERCHANT_CODE);

        if (!request.getAccessCode().equals(merchantCode) || !request.getPin().equals(MERCHANT_PIN)) {
            ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"false\"}");
            return delegate.returning(Calls.response(Response.error(404, responseBody))).login(request);
        }

        String dummyResponse = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": 1,\n" +
                "    \"code\": \"" + merchantCode + "\",\n" +
                "    \"name\": \"" + merchantName + "\",\n" +
                "    \"city\": \"Delhi\",\n" +
                "    \"countryCode\": \"IN\",\n" +
                "    \"categoryCode\": \"1234\",\n" +
                "    \"currencyNumericCode\": \"356\",\n" +
                "    \"identifierMastercard04\": \"" + merchantIdentifier + "\",\n" +
                "    \"storeId\": \"87654321\",\n" +
                "    \"terminalNumber\": \"3124652125\",\n" +
                "    \"transactions\": [\n" +
                "      {\n" +
                "        \"referenceId\": \"0390284231\",\n" +
                "        \"transactionAmount\": \"190.00\",\n" +
                "        \"tipAmount\": \"15.00\",\n" +
                "        \"currencyNumericCode\": 356,\n" +
                "        \"transactionDate\": \"" + dateFormat.format(new Date()) + "\",\n" +
                "        \"invoiceNumber\": \"134652125\",\n" +
                "        \"terminalNumber\": \"3124652125\"\n" +
                "      }\n" +
                "    ]\n" +
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
        PreferenceManager.getInstance().putString(MERCHANT_CODE_KEY, CalculateCode.calculate8digit(user.getIdentifierMastercard04()));

        return delegate.returningResponse(user).save(user);
    }
}
