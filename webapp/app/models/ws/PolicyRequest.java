package models.ws;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class PolicyRequest {

	public static final int TRANSACTION = 730;

	@Expose
	private String quoteNumber;
	@Expose
	private String quotationDate;
	@Expose
	private String emisionZone;
	@Expose
	private String paymentMethod;
	@Expose
	private String typePolicy;
	@Expose
	private String typeProduct;
	@Expose
	private String codeAgent;
	@Expose
	private String typeLine;
	@Expose
	private String productPolicy;
	@Expose
	private String typeProduction;
	@Expose
	private String validSice;
	@Expose
	private String validUntil;
	@Expose
	private String walletType;
	@Expose
	private String currency;
	@Expose
	private BigDecimal discountRate;


	public String getProductPolicy() {
		return productPolicy;
	}

	public void setProductPolicy(String productPolicy) {
		this.productPolicy = productPolicy;
	}

	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	public String getEmisionZone() {
		return emisionZone;
	}
	public void setEmisionZone(String emisionZone) {
		this.emisionZone = emisionZone;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getTypePolicy() {
		return typePolicy;
	}
	public void setTypePolicy(String typePolicy) {
		this.typePolicy = typePolicy;
	}
	public String getTypeProduct() {
		return typeProduct;
	}
	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}
	public String getCodeAgent() {
		return codeAgent;
	}
	public void setCodeAgent(String codeAgent) {
		this.codeAgent = codeAgent;
	}
	public String getTypeLine() {
		return typeLine;
	}
	public void setTypeLine(String typeLine) {
		this.typeLine = typeLine;
	}
	public String getTypeProduction() {
		return typeProduction;
	}
	public void setTypeProduction(String typeProduction) {
		this.typeProduction = typeProduction;
	}
	public String getValidSice() {
		return validSice;
	}
	public void setValidSice(String validSice) {
		this.validSice = validSice;
	}
	public String getValidUntil() {
		return validUntil;
	}
	public void setValidUntil(String validUntil) {
		this.validUntil = validUntil;
	}
	public String getWalletType() {
		return walletType;
	}
	public void setWalletType(String walletType) {
		this.walletType = walletType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(BigDecimal discountRate) {
		this.discountRate = discountRate;
	}
}
