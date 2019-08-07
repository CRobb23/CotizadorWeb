package models.ws;

import com.google.gson.annotations.Expose;

public class QueryPendingTransactionsResponse extends BaseResponse {

    @Expose
    private String pendingNumbers;

    @Expose
    private String currency;

    @Expose
    private String quotationNumber;

    @Expose
    private String message;


    public String getPendingNumbers() {
        return pendingNumbers;
    }

    public void setPendingNumbers(String cotizacionesPendientes) {
        this.pendingNumbers = cotizacionesPendientes;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }



}
