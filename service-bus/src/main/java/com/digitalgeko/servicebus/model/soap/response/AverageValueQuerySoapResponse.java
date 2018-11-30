package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement(name="consultaValorPromedioVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class AverageValueQuerySoapResponse {

	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="valorPromedio")
	private BigDecimal averageValue;
	@XmlElement(name="cantidadVehiculos")
	private Integer vehicleQuantity;
	
	public AverageValueQuerySoapResponse() {}
	public AverageValueQuerySoapResponse(String message) {
		this.message = message;
	}
	
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
