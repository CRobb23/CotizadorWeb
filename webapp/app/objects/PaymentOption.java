package objects;

import play.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaymentOption {
	
	public Integer numberOfPayments;
	
	public String frecuency;
	
	public BigDecimal amount;
	
	public Long frecuencyId;
	
	public BigDecimal fractioningFee;
	
	public Boolean emissionFeeFirstPayment;
	
	public BigDecimal discountPercentage;
	
	public BigDecimal ivaPercentage;
	
	public BigDecimal emissionFeePercentage;
	
	public BigDecimal coverageExternal = BigDecimal.ZERO;
	
	public BigDecimal netPrime;

	public BigDecimal virginNetPrime;

	public BigDecimal loJackFee;

	public BigDecimal discount;


	public BigDecimal addCoverageExternal(BigDecimal coverageExternal){
		this.coverageExternal = this.coverageExternal.add(coverageExternal);
		
		return this.coverageExternal;
	}
	
	public BigDecimal getAmountWithTotalEmissionFee(){
		return amount.add(getTotalEmissionFee());
	}
	
	public Integer getNumberOfPaymentsWithTotalEmissionFee(){
		return 1;
	}
	
	public BigDecimal getTotalEmissionFee(){
		BigDecimal total = netPrime;
		if (this.loJackFee != null && this.loJackFee.compareTo(BigDecimal.ZERO) > 0) {
			total = total.add(loJackFee);
		}
		return  total.multiply(emissionFeePercentage).setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getTotalFractioningFee(){
		BigDecimal total = netPrime;
		if (this.loJackFee != null && this.loJackFee.compareTo(BigDecimal.ZERO) > 0) {
			total = total.add(loJackFee);
		}
		return total.multiply(fractioningFee).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getIva(){
		BigDecimal total = netPrime;
		if (this.loJackFee != null && this.loJackFee.compareTo(BigDecimal.ZERO) > 0) {
			total = total.add(loJackFee);
		}
		BigDecimal totalWithoutIva = total.add(getTotalEmissionFee()).add(getTotalFractioningFee()).add(coverageExternal);
		return totalWithoutIva.multiply(ivaPercentage).setScale(2, RoundingMode.HALF_UP);
	}
	
	public BigDecimal getTotal(){
		BigDecimal total = netPrime;
		if (this.loJackFee != null && this.loJackFee.compareTo(BigDecimal.ZERO) > 0) {
			total = total.add(loJackFee);
		}
		return total.add(getTotalEmissionFee()).add(getTotalFractioningFee()).add(getIva()).add(coverageExternal);
	}
	
	public BigDecimal getPayment(){
		return getTotal().divide(new BigDecimal(numberOfPayments), 3, RoundingMode.HALF_UP);
	}

}
