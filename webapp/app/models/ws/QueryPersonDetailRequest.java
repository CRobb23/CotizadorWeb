package models.ws;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaDatosClientePersonal")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryPersonDetailRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 775;
	
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
