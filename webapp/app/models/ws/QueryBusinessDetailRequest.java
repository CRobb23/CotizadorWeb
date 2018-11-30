package models.ws;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaDatosClienteEmpresarial")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryBusinessDetailRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 780;

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
