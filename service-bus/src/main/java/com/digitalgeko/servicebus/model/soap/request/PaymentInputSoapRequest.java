package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="mediosdePago")
@XmlAccessorType(XmlAccessType.FIELD)
public class PaymentInputSoapRequest {

	public static final String RQ_CODE = "755";
	public static final String RS_CODE = "756";

	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="medioCobro")
	private String paymentMethod;
	@XmlElement(name="numeroCuentaTarjeta")
	private String cardNumber;
	@XmlElement(name="tipoCuentaTarjeta")
	private String cardType;
	@XmlElement(name="codigoCuentaTarjeta")
	private String cardCode;
	@XmlElement(name="claseTarjeta")
	private String cardClass;
	@XmlElement(name="fechaVencimientoTarjeta")
	private String cardExpirationDate;
	@XmlElement(name="numeroCuotas")
	private Integer numberInstallments;
	@XmlElement(name="moneda")
	private String currency;
	@XmlElement(name="tipoCuentaBancaria")
	private String bankAccountType;
	
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getCardClass() {
		return cardClass;
	}
	public void setCardClass(String cardClass) {
		this.cardClass = cardClass;
	}
	public String getCardExpirationDate() {
		return cardExpirationDate;
	}
	public void setCardExpirationDate(String cardExpirationDate) {
		this.cardExpirationDate = cardExpirationDate;
	}
	public Integer getNumberInstallments() {
		return numberInstallments;
	}
	public void setNumberInstallments(Integer numberInstallments) {
		this.numberInstallments = numberInstallments;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBankAccountType() {
		return bankAccountType;
	}
	public void setBankAccountType(String bankAccountType) {
		this.bankAccountType = bankAccountType;
	}
}
