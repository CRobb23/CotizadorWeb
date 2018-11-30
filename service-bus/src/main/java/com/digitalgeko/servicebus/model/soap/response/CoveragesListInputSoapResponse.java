package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="listaCoberturas")
@XmlAccessorType(XmlAccessType.FIELD)
public class CoveragesListInputSoapResponse {

	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="ramo")
	private String branch;
	@XmlElement(name="poliza")
	private String policy;
	
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

}
