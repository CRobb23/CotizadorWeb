package com.digitalgeko.servicebus.model.rest.request;

public class PendingTransactionsRestRequest {

    private String quotationNumber;
    private String currency;

    public String getQuotationNumber() {
        return quotationNumber;
    }
    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
