package models;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Deductible extends Model{

	@Required
	@MaxSize(100)
	@Column(length=100)
	public String name;
	
	@Required
	public Boolean active;
	
	@Required
	@Column(precision=19, scale=2)
	public BigDecimal cost ;// =new BigDecimal(0.00);
	
	@Column(name="transfer_code")
	public String transferCode;
	
}
