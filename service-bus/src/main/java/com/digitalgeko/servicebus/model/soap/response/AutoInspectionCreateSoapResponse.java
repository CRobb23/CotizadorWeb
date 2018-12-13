package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="datosAutoInspeccion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapResponse {

    @XmlElement(name="msgRespuesta")
    private String message;
    @XmlElement(name="codigoAutoInspeccion")
    private String inspectionNumber;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInspectionNumber() {
        return inspectionNumber;
    }

    public void setInspectionNumber(String inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }
}
