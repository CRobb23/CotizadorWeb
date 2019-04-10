package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaCliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClientQuerySoapRequest {

	public static final String RQ_CODE = "700";
	public static final String RS_CODE = "701";

	@XmlElement(name="nit")
	private String taxNumber;
	@XmlElement(name="dpi")
	private String identificationDocument;
	@XmlElement(name="passaporte")
	private String pasport;
	@XmlElement(name="nombreCliente")
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
