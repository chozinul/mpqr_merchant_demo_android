package com.mastercard.labs.mpqrmerchant.network.token;

import com.mastercard.labs.mpqrmerchant.network.LoginManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/8/17
 */
public class TokenInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        String token = LoginManager.getInstance().getToken();
        if (!LoginManager.getInstance().isUserLoggedIn() || token == null) {
            return chain.proceed(originalRequest);
        }

        // Add authorization header with updated authorization value to intercepted request
        Request authorisedRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer: " + token)
                .build();
        return chain.proceed(authorisedRequest);
    }
}