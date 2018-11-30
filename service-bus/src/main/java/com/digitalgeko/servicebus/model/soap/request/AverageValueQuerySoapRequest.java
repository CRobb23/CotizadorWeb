package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="consultaValorPromedioVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AverageValueQuerySoapRequest {

	public static final String RQ_CODE = "705";
	public static final String RS_CODE = "706";

	@XmlElement(name="marca")
	private String brand;
	@XmlElement(name="linea")
	private String line;
	@XmlElement(name="ano")
	private String year;
	@XmlElement(name="moneda")
	private String currency;
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
