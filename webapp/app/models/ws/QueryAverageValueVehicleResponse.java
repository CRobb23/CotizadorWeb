package models.ws;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class QueryAverageValueVehicleResponse extends BaseResponse {

	@Expose
	private String message;
	@Expose
	private BigDecimal averageValue;
	@Expose
	private Integer vehicleQuantity;
	
	public QueryAverageValueVehicleResponse() {}
	public QueryAverageValueVehicleResponse(String message) {
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
