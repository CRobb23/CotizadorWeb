package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name="listadeTransacciones")
@XmlAccessorType(XmlAccessType.FIELD)
public class PendingTransactionsQuerySoapResponse {

    @XmlElement(name="msgRespuesta")
    private String message;
    @XmlElement(name="numeroCotizacion")
    private BigDecimal quotationNumber;
    @XmlElement(name="numerosPendientes")
    private Integer pendingNumbers;
    @XmlElement(name="moneda")
    private String currency;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(BigDecimal quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public Integer getPendingNumbers() {
        return pendingNumbers;
    }

    public void setPendingNumbers(Integer pendingNumbers) {
        this.pendingNumbers = pendingNumbers;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
