package models.ws.rest;

public class InspectionAutoResponse {
	
	private Boolean success;
	private String message;
	private Integer inspectionNumber;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getInspectionNumber() {
		return inspectionNumber;
	}

	public void setInspectionNumber(Integer inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}
}
