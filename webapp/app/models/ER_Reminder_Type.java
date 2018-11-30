package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Reminder_Type extends Model{
	
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	@Column(name="active")
	public Boolean active;
	
	@Column(name="description", length=255)
	public String description;
	
	@Column(name="transfer_code", length=100)
	public String transferCode;
	
}
