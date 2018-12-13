package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ActualizacionAutoInspeccion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionUpdateSoapRequest {

    public static final String RQ_CODE = "782";
    public static final String RS_CODE = "783";

    @XmlElement(name="codigoAutoInspeccion")
    private String inspectionNumber;
    @XmlElement(name="estado")
    private Integer status;

    public String getInspectionNumber() {
        return inspectionNumber;
    }

    public void setInspectionNumber(String inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
