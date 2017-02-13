package com.mastercard.labs.mpqrmerchant.network;

import com.mastercard.labs.mpqrmerchant.data.model.User;
import com.mastercard.labs.mpqrmerchant.network.request.LoginAccessCodeRequest;
import com.mastercard.labs.mpqrmerchant.network.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/2/17
 */
public interface MPQRPaymentService {
    @POST("/merchant/login")
    Call<LoginResponse> login(@Body LoginAccessCodeRequest request);

    @POST("/merchant/logout")
    Call<Void> logout();
}
