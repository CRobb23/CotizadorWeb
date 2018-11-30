package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="datosWorkFlow")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkFlowRequest {
	
	@XmlTransient
	public static final Integer TRANSACTION = 760;

	@XmlElement(name="tipoMovimiento")
	private String movementType;
	@XmlElement(name="subTipoMovimiento")
	private String subMovementType;
	@XmlElement(name="tipoDocumento")
	private String documentType;
	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="fechaDocumento")
	private String documentDate;
	@XmlElement(name="fechaRecepcion")
	private String receptionDate;
	@XmlElement(name="nombreAsegurado")
	private String insuredName;
	@XmlElement(name="observaciones")
	private String observations;
	@XmlElement(name="urgente")
	private String urgent;
	@XmlElement(name="linea")
	private String line;
	@XmlElement(name="fechaRevision")
	private String reviewDate;
	@XmlElement(name="fechaAsignacion")
	private String dateAssignment;
	@XmlElement(name="fechaEmision")
	private String emissionDate;
	@XmlElement(name="codigoBarras")
	private String barcode;
	@XmlElement(name="moneda")
	private String currency;
	
	public String getMovementType() {
		return movementType;
	}
	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}
	public String getSubMovementType() {
		return subMovementType;
	}
	public void setSubMovementType(String subMovementType) {
		this.subMovementType = subMovementType;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(String documentDate) {
		this.documentDate = documentDate;
	}
	public String getReceptionDate() {
		return receptionDate;
	}
	public void setReceptionDate(String receptionDate) {
		this.receptionDate = receptionDate;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getObservations() {
		return observations;
	}
	public void setObservations(String observations) {
		this.observations = observations;
	}
	public String getUrgent() {
		return urgent;
	}
	public void setUrgent(String urgent) {
		this.urgent = urgent;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getDateAssignment() {
		return dateAssignment;
	}
	public void setDateAssignment(String dateAssignment) {
		this.dateAssignment = dateAssignment;
	}
	public String getEmissionDate() {
		return emissionDate;
	}
	public void setEmissionDate(String emissionDate) {
		this.emissionDate = emissionDate;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
