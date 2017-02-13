package com.mastercard.labs.mpqrmerchant.network.response;

import com.mastercard.labs.mpqrmerchant.data.model.User;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/7/17
 */
public class LoginResponse {
    private User user;
    private String token;

    public LoginResponse(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
