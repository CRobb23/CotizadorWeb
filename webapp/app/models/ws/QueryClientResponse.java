package models.ws;

import com.google.gson.annotations.Expose;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class QueryClientResponse extends BaseResponse {

	@Expose
	private String message;
	@Expose
	private String taxNumber;
	@Expose
	private String identificationDocument;
	@Expose
	private String pasport;
	@Expose
	private String name;
	@Expose
	private String cifClient;
	@Expose
	private String codeClient;
	@Expose
	private String personType;

    public QueryClientResponse() {}
	public QueryClientResponse(String message) {
		this.message = message;
	}

	@Expose
	private List<QueryClientResponse> clients;
	
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
	public List<QueryClientResponse> getClients() {
		return clients;
	}
	public void setClients(List<QueryClientResponse> clients) {
		this.clients = clients;
	}

    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
}
