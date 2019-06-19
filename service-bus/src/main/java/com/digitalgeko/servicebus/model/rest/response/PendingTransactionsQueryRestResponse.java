package com.digitalgeko.servicebus.model.rest.response;

public class PendingTransactionsQueryRestResponse {

    private String message;
    private String quotationNumber;
    private String pendingNumbers;
    private String currency;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public String getPendingNumbers() {
        return pendingNumbers;
    }

    public void setPendingNumbers(String pendingNumbers) {
        this.pendingNumbers = pendingNumbers;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
