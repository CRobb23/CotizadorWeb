package models.ws;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="datosdePoliza")
@XmlAccessorType(XmlAccessType.FIELD)
public class PolicyResponse extends BaseResponse {
	
	@XmlTransient
	public static final Integer TRANSACTION = 731;

	@XmlElement(name="msgRespuesta")
	private String message;
	@XmlElement(name="ramo")
	private String branch;
	@XmlElement(name="poliza")
	private String policy;
	@XmlElement(name="anexo")
	private String filename;

	public PolicyResponse() {}
	public PolicyResponse(String message) {
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public Integer getTransaction() {
		return TRANSACTION;
	}
}
