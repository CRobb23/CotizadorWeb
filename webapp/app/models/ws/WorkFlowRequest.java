package models.ws;

import com.google.gson.annotations.Expose;

public class WorkFlowRequest {

	public static final int TRANSACTION = 760;

	@Expose
	private String movementType;
	@Expose
	private String subMovementType;
	@Expose
	private String documentType;
	@Expose
	private String quoteNumber;
	@Expose
	private String documentDate;
	@Expose
	private String receptionDate;
	@Expose
	private String insuredName;
	@Expose
	private String observations;
	@Expose
	private String urgent;
	@Expose
	private String line;
	@Expose
	private String reviewDate;
	@Expose
	private String dateAssignment;
	@Expose
	private String emissionDate;
	@Expose
	private String barcode;
	@Expose
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
