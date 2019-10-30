package com.digitalgeko.servicebus.model.soap.response;


import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaProducto")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyProductSoapResponse {

    @XmlElement(name="msgRespuesta")
    private String message;

    @XmlElement(name="codigo")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
