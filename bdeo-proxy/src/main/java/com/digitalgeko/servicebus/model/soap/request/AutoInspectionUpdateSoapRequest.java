package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="actualizaautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionUpdateSoapRequest {

    public static final String RQ_CODE = "790";
    public static final String RS_CODE = "791";

    @XmlElement(name="id")
    private String id;
    @XmlElement(name="estado")
    private Integer status;
    @XmlElement(name="sobreexceso")
    private Integer overExcess;
    @XmlElement(name="comentarios")
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOverExcess() {
        return overExcess;
    }

    public void setOverExcess(Integer overExcess) {
        this.overExcess = overExcess;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
