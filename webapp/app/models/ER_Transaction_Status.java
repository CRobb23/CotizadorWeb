package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import helpers.FieldAccesor;
import models.ws.BaseResponse;
import models.ws.PolicyResponse;
import play.db.jpa.Model;
import utils.XmlMarshalUtil;

@Entity
public class ER_Transaction_Status extends Model {
	
	@Column(name="transaction_code")
	public Integer transactionCode;
	
	@Column(name="message")
	public String message;
	
	@Column(name="complete")
	public Boolean complete;
	
	@Column(name="xml") @Lob
	public String xml;
	
	@Column(name="transaction_date")
	public Date transactionDate;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_incident", nullable=false)
	public ER_Incident incident;
	
	public ER_Transaction_Status() {}
	public ER_Transaction_Status(Integer transaction, ER_Incident incident) {
		this.transactionCode = transaction;
		this.incident = incident;
		complete = false;
		transactionDate = new Date();
	}

	public void updateFromResponse(Object response, String json){
		BaseResponse baseResponse = (BaseResponse) response;
		xml = json;
		message = baseResponse.getMessage();
		complete = baseResponse.isSuccessful();
	}
	
	public <T> T getObjectResponseFromXML(Class clazz){
		if(!FieldAccesor.isEmptyOrNull(xml)){
			return (T) XmlMarshalUtil.fromXml(clazz, xml);
		}
		return null;
	}

	public String getXMLFromObject(Object obj){
		if(obj != null){
			xml =  XmlMarshalUtil.toXml(obj.getClass(), obj);
		}
		return xml;
	}

}
