package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name="datosdePoliza")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyInputSoapRequest {

	public static final String RQ_CODE = "730";
	public static final String RS_CODE = "731";

	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="fechaCotizacion")
	private String quotationDate;
	@XmlElement(name="zonaEmision")
	private String emisionZone;
	@XmlElement(name="formaPago")
	private String paymentMethod;
	@XmlElement(name="tipoPoliza")
	private String typePolicy;
	@XmlElement(name="tipoProducto")
	private String typeProduct;
	@XmlElement(name="codigoAgente")
	private String codeAgent;
	@XmlElement(name="tipoLinea")
	private String typeLine;
	@XmlElement(name="tipoProduccion")
	private String typeProduction;
	@XmlElement(name="vigenciaDesde")
	private String validSice;
	@XmlElement(name="vigenciaHasta")
	private String validUntil;
	@XmlElement(name="tipoCartera")
	private String walletType;
	@XmlElement(name="moneda")
	private String currency;
	@XmlElement(name="tasaDescuento")
	private BigDecimal discountRate;
	@XmlElement(name="polizaPorProducto")
	private String policyProduct;

	public String getPolicyProduct() {
		return policyProduct;
	}
	public void setPolicyProduct(String PolicyProduct) {
		this.policyProduct = policyProduct;
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
