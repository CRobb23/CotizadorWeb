package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="consultaCliente")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryClientRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 700;
	
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
