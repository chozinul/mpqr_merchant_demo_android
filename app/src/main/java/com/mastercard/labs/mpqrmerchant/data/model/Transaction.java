package com.mastercard.labs.mpqrmerchant.data.model;

import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class Transaction extends RealmObject {
    @PrimaryKey
    private String referenceId;

    private double transactionAmount;
    private String tipType;
    private double tip;
    private String currencyNumericCode;
    private Date transactionDate;
    private String invoiceNumber;
    private String terminalNumber;

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTipType() {
        return tipType;
    }

    public void setTipType(String tipType) {
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

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTerminalNumber() {
        return terminalNumber;
    }

    public void setTerminalNumber(String terminalNumber) {
        this.terminalNumber = terminalNumber;
    }

    public double getTipAmount() {
        if (TipInfo.PERCENTAGE_CONVENIENCE_FEE.name().equals(tipType)) {
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
}
