package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="menoresEdadConCoberturas")
@XmlAccessorType(XmlAccessType.FIELD)
public class YoungerResponse extends BaseResponse {
	
	@XmlTransient
	public static final Integer TRANSACTION = 751;

	@XmlElement(name="msgRespuesta")
	private String message;

	public YoungerResponse() {}
	public YoungerResponse(String message) {
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
