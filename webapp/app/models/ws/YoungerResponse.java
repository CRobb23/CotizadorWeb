package models.ws;

import com.google.gson.annotations.Expose;

public class YoungerResponse extends BaseResponse {

	@Expose
	private String message;

	public YoungerResponse() {}
	public YoungerResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
