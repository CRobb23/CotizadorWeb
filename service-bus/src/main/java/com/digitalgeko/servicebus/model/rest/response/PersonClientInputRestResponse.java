package com.digitalgeko.servicebus.model.rest.response;

public class PersonClientInputRestResponse {
	
	private String message;
	private String codeClient;
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
