package models;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Reminder extends Model{

	@Column(name="case_number")
	public String caseNumber;
	
	@Column(name="day_number")
	public Integer dayNumber;
	
	public Integer hour;
	
	public Integer minute;
	
	@Required
	public Boolean active;
	
	@Column(name="custom_email")
	public String customEmail;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "register_date")
	public Date registerDate;
	
}
