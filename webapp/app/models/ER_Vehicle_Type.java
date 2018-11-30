package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import ext.GsonExclude;
import play.db.jpa.Model;

@Entity
public class ER_Vehicle_Type extends Model{

	@GsonExclude
	@Column(name="name")
	public String name;
	@GsonExclude
	@Column(name="active")
	public Boolean active;
	
	@Column(name="transfer_code", length=100)
	public String transferCode;
	
}