package models.ws;

import com.google.gson.annotations.Expose;

public class PersonClientResponse extends BaseResponse {

	@Expose
	private String message;
	@Expose
	private String codeClient;
	@Expose
	private String cifClient;
	
	public PersonClientResponse() {}
	public PersonClientResponse(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public String getCifClient() {
		return cifClient;
	}
	public void setCifClient(String cifClient) {
		this.cifClient = cifClient;
	}
}
