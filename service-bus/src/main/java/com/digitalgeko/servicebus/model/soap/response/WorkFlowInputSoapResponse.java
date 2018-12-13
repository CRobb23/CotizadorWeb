package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="datosWorkFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkFlowInputSoapResponse {
	
	@XmlElement(name="msgRespuesta")
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
