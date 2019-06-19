package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;


@XmlRootElement(name="listadeTransacciones")
@XmlAccessorType(XmlAccessType.FIELD)
public class PendingTransactionsQuerySoapRequest {

    public static final String RQ_CODE = "772";
    public static final String RS_CODE = "773";

    @XmlElement(name="numeroCotizacion")
    private String quotationNumber;

    @XmlElement(name="moneda")
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
