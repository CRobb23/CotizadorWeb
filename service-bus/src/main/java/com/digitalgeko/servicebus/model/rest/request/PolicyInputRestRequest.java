package com.digitalgeko.servicebus.model.rest.request;

import java.math.BigDecimal;

public class PolicyInputRestRequest {

	private String quoteNumber;
	private String quotationDate;
	private String emisionZone;
	private String paymentMethod;
	private String typePolicy;
	private String typeProduct;
	private String codeAgent;
	private String typeLine;
	private String typeProduction;
	private String validSice;
	private String validUntil;
	private String walletType;
	private String currency;
	private BigDecimal discountRate;
	
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
