package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import ext.GsonExclude;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Product_Coverage_Class_Value extends Model {
    
	@ManyToOne
	public ER_Vehicle_Class vehicleClass;
	
	@Required
	@Min(0)
	public BigDecimal value;
	
	@GsonExclude
	@ManyToOne
	public ER_Product_Coverage_Value productCoverageValue;
	
	public ER_Product_Coverage_Class_Value(ER_Product_Coverage_Class_Value classValue) {
		if (classValue==null) {
			return;
		}
		
		this.vehicleClass = classValue.vehicleClass;
		this.value = classValue.value;
	}
}
