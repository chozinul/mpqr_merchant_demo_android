package com.mastercard.labs.mpqrmerchant.data.model;

import com.mastercard.labs.mpqrmerchant.utils.CurrencyCode;

import java.util.Date;


/**
 * @author Muhammad Azeem (muhammad.azeem@mastercard.com) on 2/13/17
 */
public class Transaction {
    private String referenceId;

    private double transactionAmount;
    private double tipAmount;
    private String currencyNumericCode;
    private Date transactionDate;
    private String invoiceNumber;
    private String terminalNumber;

    public Transaction(){}
    public Transaction(String referenceId, double transactionAmount, double tipAmount, String currencyNumericCode, Date transactionDate, String invoiceNumber, String terminalNumber)
    {
        this.referenceId = referenceId;
        this.transactionAmount = transactionAmount;
        this.tipAmount = tipAmount;
        this.currencyNumericCode = currencyNumericCode;
        this.transactionDate = transactionDate;
        this.invoiceNumber = invoiceNumber;
        this.terminalNumber = terminalNumber;
    }
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

}
