package models.ws.rest;

import java.util.Map;

public class InspectionBrokerResponse {
	
	private Boolean success;
	private String message;
	private Map<Long, String> brokerList;
	
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

	public Map<Long, String> getBrokerList() {
		return brokerList;
	}

	public void setBrokerList(Map<Long, String> brokerList) {
		this.brokerList = brokerList;
	}
}
