package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="datosdePoliza")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyInputSoapResponse {
	
	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="ramo")
	private String branch;
	@XmlElement(name="poliza")
	private String policy;
	@XmlElement(name="anexo")
	private String filename;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

}
