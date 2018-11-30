package models.ws;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="consultaValorPromedioVehiculo")
@XmlAccessorType(XmlAccessType.FIELD)
public class QueryAverageValueVehicleResponse extends BaseResponse {

	@XmlTransient
	public static final Integer TRANSACTION = 706;
	
	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="valorPromedio")
	private BigDecimal averageValue;
	@XmlElement(name="cantidadVehiculos")
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
	public Integer getTransaction() {
		return TRANSACTION;
	}
}
