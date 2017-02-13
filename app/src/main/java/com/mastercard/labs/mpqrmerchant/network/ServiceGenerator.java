package com.mastercard.labs.mpqrmerchant.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.mastercard.labs.mpqrmerchant.network.mock.MockMPQRMerchantService;
import com.mastercard.labs.mpqrmerchant.network.token.TokenInterceptor;

import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.mock.MockRetrofit;
import retrofit2.mock.NetworkBehavior;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/2/17
 */
public class ServiceGenerator {
    private static String apiBaseUrl = "http://localhost:8080";
    private static Boolean mockPaymentService = true;
    private static Retrofit retrofit;
    private static MockRetrofit mockRetrofit;

    private static Dispatcher dispatcher;

    private static ServiceGenerator INSTANCE = new ServiceGenerator();

    // No need to instantiate this class.
    private ServiceGenerator() {
        setApiBaseUrl(apiBaseUrl);
    }

    public static ServiceGenerator getInstance() {
        return INSTANCE;
    }

    public void setApiBaseUrl(String newApiBaseUrl) {
        // Cancel all pending requests
        if (dispatcher != null) {
            dispatcher.cancelAll();
        }

        apiBaseUrl = newApiBaseUrl;
        dispatcher = new Dispatcher();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().dispatcher(dispatcher).addInterceptor(loggingInterceptor).addInterceptor(new TokenInterceptor());

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();

        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .baseUrl(apiBaseUrl);

        retrofit = builder.build();

        // Build mock retrofit also
        NetworkBehavior networkBehavior = NetworkBehavior.create();
        networkBehavior.setFailurePercent(0);
        MockRetrofit.Builder mockBuilder = new MockRetrofit.Builder(retrofit)
                .networkBehavior(networkBehavior);

        mockRetrofit = mockBuilder.build();
    }

    public MPQRPaymentService mpqrPaymentService() {
        if (mockPaymentService) {
            return new MockMPQRMerchantService(mockRetrofit.create(MPQRPaymentService.class));
        } else {
            return retrofit.create(MPQRPaymentService.class);
        }
    }
}
