package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="datosPagador")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayerResponse extends BaseResponse {
	
	@XmlTransient
	public static final Integer TRANSACTION = 726;

	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="cifCliente")
	private String cifClient;
	@XmlElement(name="codigoCliente")
	private String codeClient;
	
	public PayerResponse() {}
	public PayerResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCifClient() {
		return cifClient;
	}
	public void setCifClient(String cifClient) {
		this.cifClient = cifClient;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public Integer getTransaction() {
		return TRANSACTION;
	}
}
