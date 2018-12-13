package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="consultaautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionQuerySoapRequest {

    public static final String RQ_CODE = "788";
    public static final String RS_CODE = "789";

    @XmlElement(name="id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
