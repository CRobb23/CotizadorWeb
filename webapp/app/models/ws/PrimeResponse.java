package models.ws;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="listaPrima")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrimeResponse extends BaseResponse {

	@XmlTransient
	public static final Integer TRANSACTION = 746;
	
	@XmlElement(name="msgRespuesta")
	private String message;
	
	public PrimeResponse() {}
	public PrimeResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getTransaction() {
		return TRANSACTION;
	}
}
