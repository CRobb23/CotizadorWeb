package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="consultaDatosClienteEmpresarial")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessDetailQuerySoapRequest {

    public static final String RQ_CODE = "780";
    public static final String RS_CODE = "781";

    @XmlElement(name="codigoCliente")
    private String clientCode;
    @XmlElement(name="cifCliente")
    private String clientCif;


    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientCif() {
        return clientCif;
    }

    public void setClientCif(String clientCif) {
        this.clientCif = clientCif;
    }
}
