package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Average_Value extends Model{

	@Required
	@MaxSize(100)
	@Column(length=100)
	public String name;
	
	@Required
	public Boolean active;
	
	@Required
	@Column(precision=19, scale=2)
	public BigDecimal value;
	
	@Column(name="transfer_code")
	public String transferCode;
	
	public ER_Average_Value(){}
	
	public ER_Average_Value(BigDecimal value){
		this.value = value;
	}
}
