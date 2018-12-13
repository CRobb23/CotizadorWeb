package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="mediosdePago")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInputSoapResponse {
	
	@XmlElement(name="msgRespuesta")
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
