package com.digitalgeko.servicebus.model.rest.response;

import java.util.List;

public class ClientQueryRestResponse {

	private String message;
	private String taxNumber;
	private String identificationDocument;
	private String pasport;
	private String name;
	private String cifClient;
	private String codeClient;
    private String personType;

	private List<ClientQueryRestResponse> clients;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
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
	public List<ClientQueryRestResponse> getClients() {
		return clients;
	}
	public void setClients(List<ClientQueryRestResponse> clients) {
		this.clients = clients;
	}

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
}
