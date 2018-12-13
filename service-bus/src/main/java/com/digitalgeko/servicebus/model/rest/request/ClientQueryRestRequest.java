package com.digitalgeko.servicebus.model.rest.request;

public class ClientQueryRestRequest {

	private String taxNumber;
	private String identificationDocument;
	private String pasport;
	private String name;
	
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getIdentificationDocument() {
		return identificationDocument;
	}
	public void setIdentificationDocument(String identificationDocument) {
		this.identificationDocument = identificationDocument;
	}
	public String getPasport() {
		return pasport;
	}
	public void setPasport(String pasport) {
		this.pasport = pasport;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
