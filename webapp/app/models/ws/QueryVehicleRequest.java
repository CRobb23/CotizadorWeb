package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="consultaVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryVehicleRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 710;
	
	@XmlElement(name="moneda")
	private String currency;
	@XmlElement(name="numeroPlaca")
	private String plate;
	@XmlElement(name="numeroChassis")
	private String chassis;
	@XmlElement(name="marcaLinea")
	private String line;
	@XmlElement(name="modeloVehiculo")
	private String yearVehicle;
	
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPlate() {
		return plate;
	}
	public void setPlate(String plate) {
		this.plate = plate;
	}
	public String getChassis() {
		return chassis;
	}
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	public String getYearVehicle() {
		return yearVehicle;
	}
	public void setYearVehicle(String yearVehicle) {
		this.yearVehicle = yearVehicle;
	}

}
