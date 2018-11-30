package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class InsurableVehicleQuerySoapResponse {

	@XmlElement(name="msgRespuesta")
	private String message;
	
	public InsurableVehicleQuerySoapResponse() {}
	public InsurableVehicleQuerySoapResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
