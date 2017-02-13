package com.mastercard.labs.mpqrmerchant.data.model;

import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class QRData {
    private String merchantName;
    private String merchantCity;
    private String merchantCategoryCode;
    private String merchantIdentifierVisa02;
    private String merchantIdentifierVisa03;
    private String merchantIdentifierMastercard04;
    private String merchantIdentifierMastercard05;
    private String merchantIdentifierNPCI06;
    private String merchantIdentifierNPCI07;
    private double transactionAmount;
    private TipType tipType;
    private double tip;
    private String currencyNumericCode;

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(String merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getMerchantIdentifierVisa02() {
        return merchantIdentifierVisa02;
    }

    public void setMerchantIdentifierVisa02(String merchantIdentifierVisa02) {
        this.merchantIdentifierVisa02 = merchantIdentifierVisa02;
    }

    public String getMerchantIdentifierVisa03() {
        return merchantIdentifierVisa03;
    }

    public void setMerchantIdentifierVisa03(String merchantIdentifierVisa03) {
        this.merchantIdentifierVisa03 = merchantIdentifierVisa03;
    }

    public String getMerchantIdentifierMastercard04() {
        return merchantIdentifierMastercard04;
    }

    public void setMerchantIdentifierMastercard04(String merchantIdentifierMastercard04) {
        this.merchantIdentifierMastercard04 = merchantIdentifierMastercard04;
    }

    public String getMerchantIdentifierMastercard05() {
        return merchantIdentifierMastercard05;
    }

    public void setMerchantIdentifierMastercard05(String merchantIdentifierMastercard05) {
        this.merchantIdentifierMastercard05 = merchantIdentifierMastercard05;
    }

    public String getMerchantIdentifierNPCI06() {
        return merchantIdentifierNPCI06;
    }

    public void setMerchantIdentifierNPCI06(String merchantIdentifierNPCI06) {
        this.merchantIdentifierNPCI06 = merchantIdentifierNPCI06;
    }

    public String getMerchantIdentifierNPCI07() {
        return merchantIdentifierNPCI07;
    }

    public void setMerchantIdentifierNPCI07(String merchantIdentifierNPCI07) {
        this.merchantIdentifierNPCI07 = merchantIdentifierNPCI07;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public TipType getTipType() {
        return tipType;
    }

    public void setTipType(TipType tipType) {
        this.tipType = tipType;
    }

    public double getTip() {
        return tip;
    }

    public void setTip(double tip) {
        this.tip = tip;
    }

    public String getCurrencyNumericCode() {
        return currencyNumericCode;
    }

    public void setCurrencyNumericCode(String currencyNumericCode) {
        this.currencyNumericCode = currencyNumericCode;
    }

    public double getTipAmount() {
        if (TipType.PERCENTAGE.equals(tipType)) {
            return transactionAmount * tip / 100;
        } else {
            return tip;
        }
    }

    public double getTotal() {
        return transactionAmount + getTipAmount();
    }

    public CurrencyCode getCurrencyCode() {
        return CurrencyCode.fromNumericCode(currencyNumericCode);
    }

    public enum TipType {
        FLAT,
        PERCENTAGE,
        PROMPT,
        NONE
    }
}
