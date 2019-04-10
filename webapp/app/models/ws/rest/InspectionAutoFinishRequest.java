package models.ws.rest;

import com.google.gson.annotations.Expose;

public class InspectionAutoFinishRequest {

	@Expose
	private String inspectionNumber;
	@Expose
	private Integer status;

	public String getInspectionNumber() {
		return inspectionNumber;
	}
	public void setInspectionNumber(String inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

