package objects;

import java.math.BigDecimal;

import play.Logger;

public class CoverageCost {
	
	public Integer coverageOrder;
	public Long coverageId;
	public String coverageName;
	public String coverageDescription;
	public BigDecimal totalCoverage;
	
	public BigDecimal damagesDeductible;
	public BigDecimal minimumDeductible;
	public BigDecimal coverage;
	
	public BigDecimal originalCost;
	public BigDecimal discount;
	
	public Boolean external;
	
	private void commonInit() {
		this.minimumDeductible = BigDecimal.ZERO;
	}
	
	public CoverageCost(BigDecimal originalCost) {
		this.originalCost = originalCost;
		this.commonInit();
	}
	
	public CoverageCost(BigDecimal originalCost, BigDecimal discount) {
		this.originalCost = originalCost;
		this.discount = discount;
		this.commonInit();
	}
	
	public BigDecimal getDiscountedCost() {
		if (this.discount !=null) { 
			return this.originalCost.multiply(discount);
		}
		
		return this.originalCost;
	}
}
