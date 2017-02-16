package com.mastercard.labs.mpqrmerchant.data.model;

import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.Date;
import java.util.Objects;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class Transaction extends RealmObject {
    @PrimaryKey
    private String referenceId;

    private double transactionAmount;
    private double tipAmount;
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
        return tipAmount;
    }

    public void setTipAmount(double tipAmount) {
        this.tipAmount = tipAmount;
    }

    public double getTotal() {
        return transactionAmount + getTipAmount();
    }

    public CurrencyCode getCurrencyCode() {
        return CurrencyCode.fromNumericCode(currencyNumericCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.transactionAmount, transactionAmount) == 0 &&
                Double.compare(that.tipAmount, tipAmount) == 0 &&
                Objects.equals(referenceId, that.referenceId) &&
                Objects.equals(currencyNumericCode, that.currencyNumericCode) &&
                Objects.equals(transactionDate, that.transactionDate) &&
                Objects.equals(invoiceNumber, that.invoiceNumber) &&
                Objects.equals(terminalNumber, that.terminalNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceId, transactionAmount, tipAmount, currencyNumericCode, transactionDate, invoiceNumber, terminalNumber);
    }
}
