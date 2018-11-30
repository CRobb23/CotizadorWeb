package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="consultaValorPromedioVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryAverageValueVehicleRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 705;
	
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
