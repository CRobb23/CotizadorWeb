package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import ext.GsonExclude;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Year extends Model{

	@Required
	@MaxSize(4)
	@Column(length=4,name="year_number")
	public String year;
	
	@GsonExclude
	@Required
	public Boolean active;
	
	@GsonExclude
	@Column(name="transfer_code", length=100)
	public String transferCode;
	
}
