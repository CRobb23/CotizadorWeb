package models.ws;

import com.google.gson.annotations.Expose;

public class QueryPendingTransactionsRequest {

    @Expose
    private String currency;

    @Expose
    private String quotationNumber;

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
