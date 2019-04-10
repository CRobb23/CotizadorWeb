package models.ws;

import com.google.gson.annotations.Expose;

public class QueryVehicleRequest {

	@Expose
	private String currency;
	@Expose
	private String plate;
	@Expose
	private String chassis;
	@Expose
	private String line;
	@Expose
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
