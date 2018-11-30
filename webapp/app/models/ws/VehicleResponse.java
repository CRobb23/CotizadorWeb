package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="datosdeVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class VehicleResponse extends BaseResponse {
	
	@XmlTransient
	public static final Integer TRANSACTION = 736;

	@XmlElement(name="msgRespuesta")
	private String message;
	
	public VehicleResponse() {}
	public VehicleResponse(String message) {
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
