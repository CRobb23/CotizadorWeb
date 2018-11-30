package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="datosWorkFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkFlowResponse extends BaseResponse {
	
	@XmlTransient
	public static final Integer TRANSACTION = 761;

	@XmlElement(name="msgRespuesta")
	private String message;
	
	public WorkFlowResponse() {}
	public WorkFlowResponse(String message) {
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
