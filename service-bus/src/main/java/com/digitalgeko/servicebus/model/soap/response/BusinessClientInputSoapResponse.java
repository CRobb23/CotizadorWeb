package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="datosClienteEmpresarial")
@XmlAccessorType(XmlAccessType.FIELD)
public class BusinessClientInputSoapResponse {
	
	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="cifCliente")
	private String cifClient;
	@XmlElement(name="codigoCliente")
	private String codeClient;
	
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
}
