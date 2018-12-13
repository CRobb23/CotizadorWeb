package models.ws;

import com.google.gson.annotations.Expose;

public class CoveragesResponse extends BaseResponse {

	@Expose
	private String message;
	@Expose
	private String branch;
	@Expose
	private String policy;

	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}

}
