package models.ws.rest;

public class InspectionAutoFinishRequest {

	private String message;
	private String inspectionNumber;
	private String status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInspectionNumber() {
		return inspectionNumber;
	}
	public void setInspectionNumber(String inspectionNumber) {
		this.inspectionNumber = inspectionNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}

