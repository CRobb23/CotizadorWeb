package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="actualizaautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionUpdateSoapResponse {

    @XmlElement(name="msgRespuesta")
    private String message;
    @XmlElement(name = "id")
    private String id;

    public AutoInspectionUpdateSoapResponse(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
