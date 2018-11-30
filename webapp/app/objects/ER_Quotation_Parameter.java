package objects;

import play.*;
import play.data.binding.As;
import play.data.validation.*;
import play.db.jpa.*;
import play.i18n.Messages;

import helpers.ERConstants;

import javax.persistence.*;

import models.*;

import binders.MoneyBinder;
import ext.GsonExclude;
import extensions.StringFormatExtensions;

import java.math.BigDecimal;
import java.util.*;

public class ER_Quotation_Parameter {
    
	@Required
	public ER_Product_Coverage productCoverage;
	
	@Min(0)
	@As(binder=MoneyBinder.class)
	public BigDecimal value;
	
	public Boolean applyInsurance;
	
	public ER_Product_Coverage_Value coverageValue;
	
	@GsonExclude
	public ER_Quotation quotation;
	
	public boolean isCarValue;
	
	public boolean isEmpty() {
		return (this.value==null) && (this.coverageValue==null) && (this.applyInsurance==null || !this.applyInsurance);
	}
	
	public boolean validateValue() {
		
		try {
			
			if (productCoverage.optional != null && productCoverage.optional) {
				return true;
			}
			
			switch (productCoverage.coverage.type.code) {
				case ERConstants.CALCULATION_TYPE_AMOUNT:
				case ERConstants.CALCULATION_TYPE_RANGE:{
					if (value!=null && value.compareTo(BigDecimal.ZERO)>0) {
						return true;
					}
				}
				break;
				case ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS:
				case ERConstants.CALCULATION_TYPE_OPTIONS:{
					if (coverageValue!=null) {
						return true;
					}
				}
				break;
				case ERConstants.CALCULATION_TYPE_FIXED:{
					return true;
				}
			}
		} catch (Exception e) {
			Logger.error(e, "Error validating quotation parameter");
		}
		
		return false;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (this.productCoverage!=null) {
			map.put("productCoverage", this.productCoverage.toMap());
		}
		if (value!=null) {
			map.put("value", this.value);
		}
		if (applyInsurance!=null) {
			map.put("applyInsurance", this.applyInsurance);
		}
		if (coverageValue!=null) {
			map.put("coverageValue", this.coverageValue.toMap());
		}
		return map;
	}
	
	public BigDecimal coverageAmount() {
		BigDecimal coverageAmount = null;
		
		switch (this.productCoverage.coverage.type.code) {
		case ERConstants.CALCULATION_TYPE_AMOUNT:
		case ERConstants.CALCULATION_TYPE_RANGE:{
			coverageAmount = this.value;
		}
		break;
		case ERConstants.CALCULATION_TYPE_FIXED: {
			coverageAmount = this.productCoverage.coverageValue;
		}
		break;
		case ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS: {
			coverageAmount = this.coverageValue != null ? this.coverageValue.lowRange : BigDecimal.ZERO;
		}
		break;
		case ERConstants.CALCULATION_TYPE_OPTIONS: {
			coverageAmount = this.productCoverage.coverageValue;
		}
		break;
		}
		
		if (coverageAmount!=null && productCoverage.minimumPrime!=null && coverageAmount.compareTo(productCoverage.minimumPrime)<0) {
			coverageAmount = productCoverage.minimumPrime;
		}
		
		return coverageAmount;
	}
	
	public String valueString() {
		
		String currencySymbol = "";
		if (this.productCoverage.product.currency !=null) {
			currencySymbol = this.productCoverage.product.currency.symbol + ".";
		}
		
		switch (this.productCoverage.coverage.type.code) {
		case ERConstants.CALCULATION_TYPE_AMOUNT:
		case ERConstants.CALCULATION_TYPE_RANGE:{
			if (value!=null) {
				return currencySymbol + " " + StringFormatExtensions.decimalFormat(value.doubleValue());
			}
			
			return Messages.get("form.no");
		}
		case ERConstants.CALCULATION_TYPE_FIXED: {
			if (applyInsurance!=null) {
				return (applyInsurance)?Messages.get("form.yes"):Messages.get("form.no");
			}
			
			return Messages.get("form.no");
		}
		case ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS: {
			if (coverageValue!=null) {
				return currencySymbol + " " + StringFormatExtensions.decimalFormat(coverageValue.lowRange.doubleValue());
			}
			
			return Messages.get("form.no");
		}
		
		case ERConstants.CALCULATION_TYPE_OPTIONS: {
			if (coverageValue!=null) {
				return coverageValue.optionName;
			}
			
			return Messages.get("form.no");
		}
		
		}
		
		return null;
	}
}
