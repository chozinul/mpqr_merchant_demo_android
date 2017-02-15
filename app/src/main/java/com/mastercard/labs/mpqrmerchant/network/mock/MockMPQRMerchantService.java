package com.mastercard.labs.mpqrmerchant.network.mock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mastercard.labs.mpqrmerchant.network.MPQRPaymentService;
import com.mastercard.labs.mpqrmerchant.network.request.LoginAccessCodeRequest;
import com.mastercard.labs.mpqrmerchant.network.response.LoginResponse;

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
    private final BehaviorDelegate<MPQRPaymentService> delegate;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz", Locale.getDefault());
    private Gson gson;

    public MockMPQRMerchantService(BehaviorDelegate<MPQRPaymentService> delegate) {
        this.delegate = delegate;
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
    }

    @Override
    public Call<LoginResponse> login(@Body LoginAccessCodeRequest request) {
        if (!request.getAccessCode().equals("12345678") || !request.getPin().equals("123456")) {
            ResponseBody responseBody = ResponseBody.create(MediaType.parse("application/json"), "{\"success\": \"false\"}");
            return delegate.returning(Calls.response(Response.error(404, responseBody))).login(request);
        }

        String dummyResponse = "{\n" +
                "  \"user\": {\n" +
                "    \"id\": 1,\n" +
                "    \"code\": \"12345678\",\n" +
                "    \"name\": \"FarmtoTable F&B\",\n" +
                "    \"city\": \"Delhi\",\n" +
                "    \"countryCode\": \"IN\",\n" +
                "    \"categoryCode\": \"1234\",\n" +
                "    \"currencyNumericCode\": \"356\",\n" +
                "    \"identifierMastercard04\": \"5555222233334444\",\n" +
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
}
