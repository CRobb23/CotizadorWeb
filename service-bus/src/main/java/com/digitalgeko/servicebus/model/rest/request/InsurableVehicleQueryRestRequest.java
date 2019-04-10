package com.digitalgeko.servicebus.model.rest.request;

public class InsurableVehicleQueryRestRequest {

	private String currency;
	private String plate;
	private String chassis;
	private String line;
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
