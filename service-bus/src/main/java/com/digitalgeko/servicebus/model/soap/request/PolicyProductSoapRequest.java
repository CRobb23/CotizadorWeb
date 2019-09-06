package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;


@XmlRootElement(name="consultaProducto")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyProductSoapRequest {

    //TODO COLOCAR LOS NUMEROS VERDADEROS DE TRANSACCIONES
    public static final String RQ_CODE = "772";
    public static final String RS_CODE = "773";

    @XmlElement(name="id")
    private String id;

    @XmlElement(name="estado")
    private String status;

    @XmlElement(name="nombre")
    private String name;

    @XmlElement(name="descripcion")
    private String description;

    @XmlElement(name="desde")
    private String policyFrom;

    @XmlElement(name="hasta")
    private String policyTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPolicyFrom() {
        return policyFrom;
    }

    public void setPolicyFrom(String policyFrom) {
        this.policyFrom = policyFrom;
    }

    public String getPolicyTo() {
        return policyTo;
    }

    public void setPolicyTo(String policyTo) {
        this.policyTo = policyTo;
    }


}
