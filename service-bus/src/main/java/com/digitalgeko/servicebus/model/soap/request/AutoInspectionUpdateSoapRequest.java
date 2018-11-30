package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ActualizacionAutoInspeccion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionUpdateSoapRequest {

    public static final String RQ_CODE = "490";
    public static final String RS_CODE = "491";

    @XmlElement(name="mensaje")
    private String message;
    @XmlElement(name="numero")
    private String inspectionNumber;
    @XmlElement(name="estado")
    private String status;

}
