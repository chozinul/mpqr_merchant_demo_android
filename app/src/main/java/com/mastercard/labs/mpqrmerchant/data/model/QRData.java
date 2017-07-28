package com.mastercard.labs.mpqrmerchant.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class QRData implements Parcelable {
    private String merchantCode;
    private String merchantName;
    private String merchantCity;
    private String merchantCountryCode;
    private String merchantCategoryCode;
    private String merchantIdentifierVisa02;
    private String merchantIdentifierVisa03;
    private String merchantIdentifierMastercard04;
    private String merchantIdentifierMastercard05;
    private String merchantIdentifierNPCI06;
    private String merchantIdentifierNPCI07;
    private String merchantStoreId;
    private String merchantTerminalNumber;
    private String merchantMobile;
    private double transactionAmount;
    private TipType tipType;
    private double tip;
    private String currencyNumericCode;

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

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

    public String getMerchantCountryCode() {
        return merchantCountryCode;
    }

    public void setMerchantCountryCode(String merchantCountryCode) {
        this.merchantCountryCode = merchantCountryCode;
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

    public String getMerchantStoreId() {
        return merchantStoreId;
    }

    public void setMerchantStoreId(String merchantStoreId) {
        this.merchantStoreId = merchantStoreId;
    }

    public String getMerchantTerminalNumber() {
        return merchantTerminalNumber;
    }

    public void setMerchantTerminalNumber(String merchantTerminalNumber) {
        this.merchantTerminalNumber = merchantTerminalNumber;
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

    public void setMerchantMobile(String merchantMobile) {
        this.merchantMobile = merchantMobile;
    }

    public String getMerchantMobile() {
        return merchantMobile;
    }

    public enum TipType {
        FLAT,
        PERCENTAGE,
        PROMPT,
        NONE
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.merchantCode);
        dest.writeString(this.merchantName);
        dest.writeString(this.merchantCity);
        dest.writeString(this.merchantCountryCode);
        dest.writeString(this.merchantCategoryCode);
        dest.writeString(this.merchantIdentifierVisa02);
        dest.writeString(this.merchantIdentifierVisa03);
        dest.writeString(this.merchantIdentifierMastercard04);
        dest.writeString(this.merchantIdentifierMastercard05);
        dest.writeString(this.merchantIdentifierNPCI06);
        dest.writeString(this.merchantIdentifierNPCI07);
        dest.writeString(this.merchantStoreId);
        dest.writeString(this.merchantTerminalNumber);
        dest.writeDouble(this.transactionAmount);
        dest.writeString(this.merchantMobile);
        dest.writeInt(this.tipType == null ? -1 : this.tipType.ordinal());
        dest.writeDouble(this.tip);
        dest.writeString(this.currencyNumericCode);
    }

    public QRData() {
    }

    protected QRData(Parcel in) {
        this.merchantCode = in.readString();
        this.merchantName = in.readString();
        this.merchantCity = in.readString();
        this.merchantCountryCode = in.readString();
        this.merchantCategoryCode = in.readString();
        this.merchantIdentifierVisa02 = in.readString();
        this.merchantIdentifierVisa03 = in.readString();
        this.merchantIdentifierMastercard04 = in.readString();
        this.merchantIdentifierMastercard05 = in.readString();
        this.merchantIdentifierNPCI06 = in.readString();
        this.merchantIdentifierNPCI07 = in.readString();
        this.merchantStoreId = in.readString();
        this.merchantTerminalNumber = in.readString();
        this.transactionAmount = in.readDouble();
        this.merchantMobile = in.readString();
        int tmpTipType = in.readInt();
        this.tipType = tmpTipType == -1 ? null : TipType.values()[tmpTipType];
        this.tip = in.readDouble();
        this.currencyNumericCode = in.readString();
    }

    public static final Parcelable.Creator<QRData> CREATOR = new Parcelable.Creator<QRData>() {
        @Override
        public QRData createFromParcel(Parcel source) {
            return new QRData(source);
        }

        @Override
        public QRData[] newArray(int size) {
            return new QRData[size];
        }
    };
}
