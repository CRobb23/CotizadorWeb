package models.ws;

import com.google.gson.annotations.Expose;

public class PrimeResponse extends BaseResponse {

	@Expose
	private String message;
	
	public PrimeResponse() {}
	public PrimeResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
