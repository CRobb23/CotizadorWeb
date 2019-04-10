package models.ws;

import com.google.gson.annotations.Expose;

public class QueryAverageValueVehicleRequest {

	@Expose
	private String brand;
	@Expose
	private String line;
	@Expose
	private String year;
	@Expose
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
