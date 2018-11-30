package models.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="listaCoberturas")
@XmlAccessorType(XmlAccessType.FIELD)
public class CoveragesResponse extends BaseResponse {

	@XmlTransient
	public static final Integer TRANSACTION = 741;
	
	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="ramo")
	private String branch;
	@XmlElement(name="poliza")
	private String policy;
	
	public CoveragesResponse() {}
	public CoveragesResponse(String message) {
		this.message = message;
	}
	
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
	public Integer getTransaction() {
		return TRANSACTION;
	}
}
