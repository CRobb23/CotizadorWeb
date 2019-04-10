package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaDatosClientePersonal")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonDetailQuerySoapRequest {

    public static final String RQ_CODE = "775";
    public static final String RS_CODE = "776";

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
