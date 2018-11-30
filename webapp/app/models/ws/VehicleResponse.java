package models.ws;

import com.google.gson.annotations.Expose;

public class VehicleResponse extends BaseResponse {

	@Expose
	private String message;
	
	public VehicleResponse() {}
	public VehicleResponse(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
