package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="consultaCliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientQuerySoapResponse {

	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="nit")
	private String taxNumber;
	@XmlElement(name="dpi")
	private String identificationDocument;
	@XmlElement(name="passaporte")
	private String pasport;
	@XmlElement(name="nombreCliente")
	private String name;
	@XmlElement(name="cifCliente")
	private String cifClient;
	@XmlElement(name="codigoCliente")
	private String codeClient;
    @XmlElement(name="tipoPersona")
    private String personType;

    public ClientQuerySoapResponse() {}
	public ClientQuerySoapResponse(String message) {
		this.message = message;
	}
	
    @XmlElementWrapper(name="listaClientes")
    @XmlElement(name="cliente")
	private List<ClientQuerySoapResponse> clients;
	
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
	public List<ClientQuerySoapResponse> getClients() {
		return clients;
	}
	public void setClients(List<ClientQuerySoapResponse> clients) {
		this.clients = clients;
	}

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
}
