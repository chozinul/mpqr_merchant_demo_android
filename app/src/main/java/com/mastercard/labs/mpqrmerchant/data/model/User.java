package com.mastercard.labs.mpqrmerchant.data.model;

import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class User extends RealmObject {
    @PrimaryKey
    private long id;

    private String code;
    private String name;
    private String city;
    private String categoryCode;
    private String countryCode;
    private String identifierVisa02;
    private String identifierVisa03;
    private String identifierMastercard04;
    private String identifierMastercard05;
    private String identifierNPCI06;
    private String identifierNPCI07;
    private String storeId;
    private String terminalNumber;
    private String currencyNumericCode;
    private RealmList<Transaction> transactions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getIdentifierVisa02() {
        return identifierVisa02;
    }

    public void setIdentifierVisa02(String identifierVisa02) {
        this.identifierVisa02 = identifierVisa02;
    }

    public String getIdentifierVisa03() {
        return identifierVisa03;
    }

    public void setIdentifierVisa03(String identifierVisa03) {
        this.identifierVisa03 = identifierVisa03;
    }

    public String getIdentifierMastercard04() {
        return identifierMastercard04;
    }

    public void setIdentifierMastercard04(String identifierMastercard04) {
        this.identifierMastercard04 = identifierMastercard04;
    }

    public String getIdentifierMastercard05() {
        return identifierMastercard05;
    }

    public void setIdentifierMastercard05(String identifierMastercard05) {
        this.identifierMastercard05 = identifierMastercard05;
    }

    public String getIdentifierNPCI06() {
        return identifierNPCI06;
    }

    public void setIdentifierNPCI06(String identifierNPCI06) {
        this.identifierNPCI06 = identifierNPCI06;
    }

    public String getIdentifierNPCI07() {
        return identifierNPCI07;
    }

    public void setIdentifierNPCI07(String identifierNPCI07) {
        this.identifierNPCI07 = identifierNPCI07;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public String getCurrencyNumericCode() {
        return currencyNumericCode;
    }

    public void setCurrencyNumericCode(String currencyNumericCode) {
        this.currencyNumericCode = currencyNumericCode;
    }

    public RealmList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(RealmList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(code, user.code) &&
                Objects.equals(name, user.name) &&
                Objects.equals(city, user.city) &&
                Objects.equals(categoryCode, user.categoryCode) &&
                Objects.equals(identifierVisa02, user.identifierVisa02) &&
                Objects.equals(identifierVisa03, user.identifierVisa03) &&
                Objects.equals(identifierMastercard04, user.identifierMastercard04) &&
                Objects.equals(identifierMastercard05, user.identifierMastercard05) &&
                Objects.equals(identifierNPCI06, user.identifierNPCI06) &&
                Objects.equals(identifierNPCI07, user.identifierNPCI07) &&
                Objects.equals(storeId, user.storeId) &&
                Objects.equals(terminalNumber, user.terminalNumber) &&
                Objects.equals(currencyNumericCode, user.currencyNumericCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, name, city, categoryCode, identifierVisa02, identifierVisa03, identifierMastercard04, identifierMastercard05, identifierNPCI06, identifierNPCI07, storeId, terminalNumber, currencyNumericCode);
    }
}
