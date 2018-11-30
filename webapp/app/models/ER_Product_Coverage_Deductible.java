package models;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import ext.GsonExclude;
import play.data.validation.Min;
import play.db.jpa.Model;

@Entity
public class ER_Product_Coverage_Deductible extends Model {
	
	@Min(0)
	public BigDecimal damagesDeductible;
	
	@GsonExclude
	@ManyToOne
	public ER_Product_Coverage productCoverage;
	
	@ManyToOne
	public ER_Vehicle_Class vehicleClass;
	
	public ER_Product_Coverage_Deductible() {
		damagesDeductible = BigDecimal.ZERO;
	}
	
	public ER_Product_Coverage_Deductible(ER_Product_Coverage_Deductible deductible) {
		if (deductible==null) {
			return;
		}
		
		this.damagesDeductible = deductible.damagesDeductible;
		this.vehicleClass = deductible.vehicleClass;
	}
	
	@PostLoad
	@PrePersist
    @PreUpdate
    public void setDefaultValues() {
		if (damagesDeductible==null) {
			damagesDeductible = BigDecimal.ZERO;
		}
    }
    
}
