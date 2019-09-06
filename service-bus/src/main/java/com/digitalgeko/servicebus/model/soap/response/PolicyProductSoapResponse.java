package com.digitalgeko.servicebus.model.soap.response;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="consultaProduto")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyProductSoapResponse {

    @XmlElement(name="msgRespuesta")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
