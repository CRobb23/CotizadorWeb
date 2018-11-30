package models;

import play.*;
import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import ext.GsonExclude;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Product_Discount_Range extends Model {
    
	@Required
	@Min(0)
	public BigDecimal lowRange;
	
	@Min(0)
	public BigDecimal highRange;
	
	@Required
	@Min(0)
	@Max(50)
	public BigDecimal value;
	
	@GsonExclude
	@ManyToOne
	public ER_Product product;
	
	public ER_Product_Discount_Range() {
		
	}
	
	public ER_Product_Discount_Range(ER_Product_Discount_Range discount) {
		if (discount==null) {
			return;
		}
		
		this.lowRange = discount.lowRange;
		this.highRange = discount.highRange;
		this.value = discount.value;
	}
}
