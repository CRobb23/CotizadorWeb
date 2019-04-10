package com.digitalgeko.servicebus.model.rest.response;

import java.math.BigDecimal;

public class AverageValueQueryRestResponse {

	private String message;
	private BigDecimal averageValue;
	private Integer vehicleQuantity;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public BigDecimal getAverageValue() {
		return averageValue;
	}
	public void setAverageValue(BigDecimal averageValue) {
		this.averageValue = averageValue;
	}
	public Integer getVehicleQuantity() {
		return vehicleQuantity;
	}
	public void setVehicleQuantity(Integer vehicleQuantity) {
		this.vehicleQuantity = vehicleQuantity;
	}

}
