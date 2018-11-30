package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import ext.GsonExclude;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Product_Discount extends Model {
	
	@Required
	@Min(0)
	@Max(50)
	public BigDecimal value;
	
	@Required
	@ManyToOne
	public ER_User_Role role;
	
	@GsonExclude
	@ManyToOne
	public ER_Product product;
	
	public ER_Product_Discount() {
		
	}
	
	public ER_Product_Discount(ER_Product_Discount discount) {
		if (discount == null) {
			return;
		}
		
		this.value = discount.value;
		this.role = discount.role;
	}
}
