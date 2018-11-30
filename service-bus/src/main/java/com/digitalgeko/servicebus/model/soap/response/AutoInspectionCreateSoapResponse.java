package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SolicitarAutoInspeccion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapResponse {

    @XmlElement(name="msgRespuesta")
    private String message;
    @XmlElement(name="numero")
    private String number;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
