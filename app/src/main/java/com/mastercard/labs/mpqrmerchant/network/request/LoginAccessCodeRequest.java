package com.mastercard.labs.mpqrmerchant.network.request;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/7/17
 */
public class LoginAccessCodeRequest {
    private String accessCode;
    private String pin;

    public LoginAccessCodeRequest(String accessCode, String pin) {
        this.accessCode = accessCode;
        this.pin = pin;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
