package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="datosClientePersonal")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonClientInputSoapResponse {
	
	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="codigoCliente")
	private String codeClient;
	@XmlElement(name="cifCliente")
	private String cifClient;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public String getCifClient() {
		return cifClient;
	}
	public void setCifClient(String cifClient) {
		this.cifClient = cifClient;
	}
}
