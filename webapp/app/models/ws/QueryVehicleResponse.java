package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="consultaVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryVehicleResponse extends BaseResponse {

	@XmlTransient
	public static final Integer TRANSACTION = 711;
	
	@XmlElement(name="msgRespuesta")
	private String message;
	
	public QueryVehicleResponse() {}
	public QueryVehicleResponse(String message) {
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
