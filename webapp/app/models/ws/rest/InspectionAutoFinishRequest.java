package models.ws.rest;

public class InspectionAutoFinishRequest {

	private String inspectionNumber;
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

