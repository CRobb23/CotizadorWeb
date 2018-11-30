package objects;

import java.math.BigDecimal;

import play.Logger;

public class DiscountableValue {
	
	private BigDecimal value;
	
	private BigDecimal minimumValue;
	
	public DiscountableValue(BigDecimal value, BigDecimal minimumValue) {
		this.value = value;
		this.minimumValue = minimumValue;
	}
	
	public BigDecimal getDiscount(BigDecimal discountPercentage) {
		
		if (this.value==null || discountPercentage == null) {
			return BigDecimal.ZERO;
		}
		
		BigDecimal discount = this.value.multiply(discountPercentage);
		return discount;
//		
//		BigDecimal discountedValue = this.value.subtract(discount);
//		
//		if (this.minimumValue!=null && discountedValue.compareTo(this.minimumValue)<0) {
//			discountedValue = minimumValue;
//		}
//		
//		discount = this.value.subtract(discountedValue);
//		
//		if (discount.compareTo(BigDecimal.ZERO)<0) {
//			discount = BigDecimal.ZERO;
//		}
//		
//		return discount;
	}
}
