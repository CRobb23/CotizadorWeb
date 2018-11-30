package models;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class ER_Beneficiaries extends Model{
	
	@MaxSize(250)
	@Column(length=250)
	public String name;
	
	@Required
    @Column(name="transfer_code", length=100)
	public String transferCode;
	
}
