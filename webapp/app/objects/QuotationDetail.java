package objects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import helpers.ERConstants;
import models.ER_Payment_Frecuency;
import play.Logger;
import sun.rmi.runtime.Log;

public class QuotationDetail {
	
	private List<CoverageCostCategory> categories;
	
	private String vehicleBrand;
	private String vehicleLine;
	private BigDecimal theftDeductible;
	private Integer vehicleYear;
	private String vehicleClass;
	private BigDecimal vehicleValue;
	private Boolean requiresLoJack;
    private String facultativeType ;
    private BigDecimal facultativeDiscount;
	
	private BigDecimal netPrime;
	private BigDecimal internalPrime;
	private BigDecimal externalPrime;
	private BigDecimal virginInternalPrime;

	//Este campo se queda para soportar cotizaciones anteriores.
	private BigDecimal discountablePrime;
	private List<DiscountableValue> discountableValues;
	private BigDecimal totalMinimumPrime;
	
	private BigDecimal emissionFee;
	private Boolean emissionFeeFirstPayment;
	private BigDecimal vat;
	
	private BigDecimal additionalDiscount;
	private BigDecimal primeDiscount;
	private BigDecimal deductibleIncrease;
	
	private Date generationDate;
	
	private String currencySymbol;
	
	private List<PaymentOption> paymentOptions;
	
	private BigDecimal yearPayment;

	private BigDecimal loJackFee;
	private Integer loJackOp;
	private BigDecimal loJackData;

	private void commonInit() {
		this.generationDate = new Date();
		this.categories = new ArrayList<CoverageCostCategory>();
		this.netPrime = BigDecimal.ZERO;
		this.deductibleIncrease = BigDecimal.ZERO;
		this.totalMinimumPrime = BigDecimal.ZERO;
		
		this.internalPrime = BigDecimal.ZERO;
		this.externalPrime = BigDecimal.ZERO;
		this.discountablePrime = BigDecimal.ZERO;
		this.discountableValues = new ArrayList<DiscountableValue>();
		
		this.primeDiscount = BigDecimal.ZERO;
		this.additionalDiscount = BigDecimal.ZERO;
	}
	
	public QuotationDetail() {
		commonInit();
	}
	
	public QuotationDetail(Boolean emissionFeeFirstPayment, BigDecimal emissionFee, BigDecimal vat, BigDecimal primeDiscount, BigDecimal deductibleIncrease) {
		commonInit();
		this.emissionFeeFirstPayment = emissionFeeFirstPayment;
		this.emissionFee = emissionFee;
		this.vat = vat;
		this.primeDiscount = primeDiscount;
		this.deductibleIncrease = deductibleIncrease;
	}
	
	public BigDecimal applyDiscount(BigDecimal discount) {
		
		if (discount!=null) {
			
			BigDecimal appliedDiscount = discount;
			
			if (this.primeDiscount!=null) {
				BigDecimal left = new BigDecimal(ERConstants.MAX_DISCOUNT).subtract(this.primeDiscount).subtract(appliedDiscount);
				if (left.compareTo(BigDecimal.ZERO) < 0) {
					appliedDiscount = appliedDiscount.add(left);
				}
			}
			
			this.additionalDiscount = discount;
			this.updatePaymentOptions();
			
			return appliedDiscount;
		}
		
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getDiscount() {
		return this.additionalDiscount;
	}
	
	public BigDecimal getPrimeDiscount() {
		return primeDiscount;
	}
	public void setPrimeDiscount(BigDecimal primeDiscount) {
		this.primeDiscount = primeDiscount;
	}

	public String getCurrencySymbol() {
		return currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getVehicleBrand() {
		return vehicleBrand;
	}

	public void setVehicleBrand(String vehicleBrand) {
		this.vehicleBrand = vehicleBrand;
	}

	public BigDecimal getTheftDeductible() {
		return theftDeductible;
	}

	public void setTheftDeductible(BigDecimal theftDeductible) {
		this.theftDeductible = theftDeductible;
	}

	public String getVehicleLine() {
		return vehicleLine;
	}

	public void setVehicleLine(String vehicleLine) {
		this.vehicleLine = vehicleLine;
	}

	public Integer getVehicleYear() {
		return vehicleYear;
	}

	public void setVehicleYear(Integer vehicleYear) {
		this.vehicleYear = vehicleYear;
	}

	public String getVehicleClass() {
		return vehicleClass;
	}

	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}

	public BigDecimal getVehicleValue() {
		return vehicleValue;
	}

	public void setVehicleValue(BigDecimal vehicleValue) {
		this.vehicleValue = vehicleValue;
	}
	
	public Boolean getRequiresLoJack() {
		return requiresLoJack;
	}

	public void setRequiresLoJack(Boolean requiresLoJack) {
		this.requiresLoJack = requiresLoJack;
	}

	public Date getGenerationDate() {
		return this.generationDate;
	}
	
	public BigDecimal getNetPrime() {
		return netPrime;
	}

    public BigDecimal getTotalMinimumPrime() {
        return totalMinimumPrime;
    }

    public String getFacultativeType() {
        return facultativeType;
    }

    public void setFacultativeType(String facultativeType) {
        this.facultativeType = facultativeType;
    }

    public BigDecimal getFacultativeDiscount() {
        return facultativeDiscount;
    }

    public void setFacultativeDiscount(BigDecimal facultativeDiscount) {
        this.facultativeDiscount = facultativeDiscount;
    }

    public BigDecimal deductibleIncreaseMultiplier() {
		
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal multiplier = BigDecimal.ONE;
		if (this.deductibleIncrease!=null) {
			multiplier = multiplier.add(this.deductibleIncrease.divide(hundred));
		}
		
		return multiplier;
	}
	
	public List<CoverageCostCategory> getCategories() {
		return this.categories;
	}
	
	public CoverageCostCategory categoryForName(String name) {
		if(categories != null){
			for(CoverageCostCategory category: categories){
				if(category.name.equals(name)){
					return category;
				}
			}
		}
		
		CoverageCostCategory category = new CoverageCostCategory();
		category.name = name;
		categories.add(category);
		
		return category;
	}
	
	public void addCost(CoverageCost cost, String categoryName, String categoryDescription, boolean netPrime, boolean external, boolean applyDiscount,BigDecimal minimumPrime) {
		
		CoverageCostCategory category = this.categoryForName(categoryName);
		category.description = categoryDescription;
		
		BigDecimal value = cost.getDiscountedCost();
		if (minimumPrime!=null) {
			this.totalMinimumPrime = this.totalMinimumPrime.add(minimumPrime);
		}
		
		if (netPrime) {
			this.netPrime = this.netPrime.add(value);
		}
		
		if (external) {
			this.externalPrime = this.externalPrime.add(value);
		} else {
			this.internalPrime = this.internalPrime.add(value);
		}
		
		if (applyDiscount) {
			DiscountableValue discountable = new DiscountableValue(value, minimumPrime);
			this.discountableValues.add(discountable); 
		}
		
		if (cost.damagesDeductible != null) {
			BigDecimal multiplier = this.deductibleIncreaseMultiplier();
			cost.damagesDeductible = cost.damagesDeductible.multiply(multiplier);
		}
		
		category.addCost(cost);
	}

	public void AddExtraValueInternalPrime(BigDecimal addValue)
	{
		BigDecimal intval = this.getInternalPrime();

		Logger.debug("intval " + intval.toString() );
		Logger.debug("AddExtraValueInternalPrime  " + addValue.toString() );
		if (intval== null)
		{
			Logger.debug("Nuevo internal prime  " + addValue.toString() );
			this.internalPrime = addValue;
		}
		else
		{
			Logger.debug("internal prime  almacenado " + this.internalPrime.toString() );
			this.internalPrime = intval.add(addValue);

			Logger.debug("internal prime  nuevo " + this.internalPrime.toString() );
		}
	}

	public BigDecimal getInternalPrime() {
		
		if (this.internalPrime!=null && this.internalPrime.compareTo(BigDecimal.ZERO) == 0) {
			return this.internalPrime;
		}
		
		BigDecimal hundred = new BigDecimal(100);
		
		BigDecimal totalDiscount = BigDecimal.ZERO;
		if (this.primeDiscount!=null) {
			totalDiscount = totalDiscount.add(this.primeDiscount);
		}
		
		if (this.additionalDiscount!=null) {
			totalDiscount = totalDiscount.add(this.additionalDiscount);
		}
		
		BigDecimal discountPercentage = (totalDiscount.divide(hundred));
		BigDecimal discountValue = BigDecimal.ZERO;
		discountValue = this.internalPrime.multiply(discountPercentage);
		if (loJackOp != null && loJackOp == 2) {
			discountValue = this.virginInternalPrime.multiply(discountPercentage);
		}

		/*BigDecimal discountValue = BigDecimal.ZERO;
		if (this.discountablePrime==null || this.discountableValues.isEmpty()) {
			discountValue = discountValue.add(this.discountablePrime.multiply(discountPercentage));
		} else {
			for (DiscountableValue value : this.discountableValues) {
				discountValue = discountValue.add(value.getDiscount(discountPercentage));
			}
		}
		// ifLoJack, make discount for it
		if (this.loJackFee != null) {
			discountValue = discountValue.add(loJackFee.multiply(discountPercentage));
		}*/

		BigDecimal internalPrime = this.internalPrime.subtract(discountValue);
				
		if (this.totalMinimumPrime!=null && internalPrime.compareTo(this.totalMinimumPrime)<0) {
			internalPrime = this.totalMinimumPrime;
		}
		
		return internalPrime;
	}

	public BigDecimal getVirginInternalPrime() {

		if (this.virginInternalPrime!=null && this.virginInternalPrime.compareTo(BigDecimal.ZERO) == 0) {
			return this.virginInternalPrime;
		}

		BigDecimal hundred = new BigDecimal(100);

		BigDecimal totalDiscount = BigDecimal.ZERO;
		if (this.primeDiscount!=null) {
			totalDiscount = totalDiscount.add(this.primeDiscount);
		}

		if (this.additionalDiscount!=null) {
			totalDiscount = totalDiscount.add(this.additionalDiscount);
		}

		BigDecimal discountPercentage = (totalDiscount.divide(hundred));
		BigDecimal discountValue = this.virginInternalPrime.multiply(discountPercentage);
		/*BigDecimal discountValue = BigDecimal.ZERO;
		if (this.discountablePrime==null || this.discountableValues.isEmpty()) {
			discountValue = discountValue.add(this.discountablePrime.multiply(discountPercentage));
		} else {
			for (DiscountableValue value : this.discountableValues) {
				discountValue = discountValue.add(value.getDiscount(discountPercentage));
			}
		}*/

		BigDecimal virginInternalPrime = this.virginInternalPrime.subtract(discountValue);

		if (this.totalMinimumPrime!=null && virginInternalPrime.compareTo(this.totalMinimumPrime)<0) {
			internalPrime = this.totalMinimumPrime;
		}

		return virginInternalPrime;
	}
	
	public BigDecimal getTotalPrime() {
        return externalPrime.add(getInternalPrime());
	}
	
	public BigDecimal getTotalEmissionFee() {
		if(emissionFee != null && internalPrime != null) {
		    return emissionFee.multiply(getInternalPrime());
		}
		
		return BigDecimal.ZERO;
	}
	
	public BigDecimal getTotalVat(BigDecimal paymentFractioningFee) {
	    BigDecimal newVatSum = getTotalPrime().add(getTotalEmissionFee());
		if(paymentFractioningFee != null){
			newVatSum = newVatSum.add(paymentFractioningFee);
		}
		
		return newVatSum.multiply(vat);
	}
	
	public BigDecimal getSinglePaymentPrime() {
		if(yearPayment == null){
            BigDecimal base = getTotalPrime().add(getTotalEmissionFee());
			yearPayment = optionAmount(base);
		}
		
		return yearPayment;
	}
	
	public List<PaymentOption> getPaymentOptions() {
		return paymentOptions;
	}
	
	private BigDecimal optionAmount(BigDecimal base) {
		BigDecimal totalPayment = this.getTotalVat(BigDecimal.ZERO).add(base); 
		return totalPayment;
	}
	
	private BigDecimal optionAmount(PaymentOption option, BigDecimal fractionableBase, BigDecimal base) {
		BigDecimal paymentFractioningFee = fractionableBase.multiply(option.fractioningFee.divide(new BigDecimal(100)));
		BigDecimal divisor = new BigDecimal(option.numberOfPayments);
		BigDecimal totalPayment = getTotalVat(paymentFractioningFee).setScale(2, RoundingMode.HALF_UP).add(base).add(paymentFractioningFee);
		
		return totalPayment.divide(divisor, 10, RoundingMode.HALF_UP);
	}
	
	public void updatePaymentOptions() {
		BigDecimal base = getTotalPrime().add(getTotalEmissionFee());
		Logger.info("TOTAL PRIME 2: " + getTotalPrime());
		Logger.info("TOTAL FEE 2: " + getTotalEmissionFee());
		Logger.info("INTERNAL PRIME 2: " + getInternalPrime());
		BigDecimal fractionableBase = getInternalPrime();
		
		if(paymentOptions != null){
			for(PaymentOption option: paymentOptions){
				if(emissionFeeFirstPayment != null && emissionFeeFirstPayment && option.numberOfPayments > 1){
					option.amount = optionAmount(option, fractionableBase, getTotalPrime());	
				}else{
					option.amount = optionAmount(option, fractionableBase, base);
				}
				
				if(option.numberOfPayments == 1){
					yearPayment = option.amount;
				}
			}
		}
	}
	
	public void calculatePaymentOptions(List<ER_Payment_Frecuency> payments) {
		BigDecimal base = getTotalPrime().add(getTotalEmissionFee());
		Logger.info("TOTAL PRIME: " + getTotalPrime());
		Logger.info("TOTAL FEE: " + getTotalEmissionFee());
		Logger.info("INTERNAL PRIME: " + getInternalPrime());
		BigDecimal fractionableBase = getInternalPrime();
		
		paymentOptions = new ArrayList<PaymentOption>();
		
		if(payments != null && !payments.isEmpty()){
			for(ER_Payment_Frecuency payment: payments){
				PaymentOption option = new PaymentOption();
				option.frecuencyId = payment.id;
				option.numberOfPayments = payment.numberOfPayments;
				option.frecuency = payment.frecuency.name;
				option.fractioningFee = payment.fractioningFee;
				option.emissionFeePercentage = emissionFee;
				option.coverageExternal = externalPrime;
				option.ivaPercentage = vat;
				if(emissionFeeFirstPayment != null && emissionFeeFirstPayment && payment.numberOfPayments > 1){
					option.amount = optionAmount(option, fractionableBase, getTotalPrime());
					option.emissionFeeFirstPayment = emissionFeeFirstPayment;
				}else{
					option.amount = optionAmount(option, fractionableBase, base);
				}
				
				paymentOptions.add(option);
				if(payment.numberOfPayments == 1){
					Logger.info("AMOUNT: " + option.amount);
					yearPayment = option.amount;
				}
			}
		}
	}

    public void addLoJackCost(Integer operation, BigDecimal loJackAmount) {
	    if (operation == 1) {
        	BigDecimal amount = loJackAmount.divide(new BigDecimal(100));
            loJackFee = internalPrime.multiply(amount);
            internalPrime = internalPrime.add(loJackFee);
		}
        if (operation == 2) {
            loJackFee = loJackAmount;
            internalPrime = internalPrime.add(loJackFee);
        }
        loJackOp = operation;
	    loJackData = loJackAmount;
    }

    public void saveInternalPrime() {
	    virginInternalPrime = internalPrime;
    }

	public BigDecimal getLoJackFee() {
		if (this.loJackFee!=null && this.loJackFee.compareTo(BigDecimal.ZERO) == 0) {
			return this.loJackFee;
		}
		if (this.additionalDiscount.compareTo(BigDecimal.ZERO) == 0) {
			return this.loJackFee;
		}
		if (loJackOp == null ) {
			return this.loJackFee;
		}
		BigDecimal loJack = new BigDecimal(0);
		if (loJackOp == 1) {
			BigDecimal amount = loJackData.divide(new BigDecimal(100));
			loJack = getVirginInternalPrime().multiply(amount);
		}
		if (loJackOp == 2) {
			loJack = loJackData;
		}
		return loJack;
	}

}
