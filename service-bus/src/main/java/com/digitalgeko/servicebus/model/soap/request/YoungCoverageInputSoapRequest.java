package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.util.Date;

@XmlRootElement(name="menoresEdadConCoberturas")
@XmlAccessorType(XmlAccessType.FIELD)
public class YoungCoverageInputSoapRequest {

	public static final String RQ_CODE = "750";
	public static final String RS_CODE = "751";

	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="codigoCobertura")
	private String codeCoverage;
	@XmlElement(name="nombreMenor")
	private String name;
	@XmlElement(name="direccionMenor")
	private String address;
	@XmlElement(name="fechaNacimientoMenor")
	private Date birthdate;
	@XmlElement(name="edadMenor")
	private String age;
	@XmlElement(name="tipoLicenciaMenor")
	private String typeLicense;
	@XmlElement(name="numeroLicenciaMenor")
	private String licenseNumber;
	@XmlElement(name="fechaVencimientoDesde")
	private Date expirationDateFrom;
	@XmlElement(name="fechaVencimientoHasta")
	private Date expirationDate;
	
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getCodeCoverage() {
		return codeCoverage;
	}
	public void setCodeCoverage(String codeCoverage) {
		this.codeCoverage = codeCoverage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTypeLicense() {
		return typeLicense;
	}
	public void setTypeLicense(String typeLicense) {
		this.typeLicense = typeLicense;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public Date getExpirationDateFrom() {
		return expirationDateFrom;
	}
	public void setExpirationDateFrom(Date expirationDateFrom) {
		this.expirationDateFrom = expirationDateFrom;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
