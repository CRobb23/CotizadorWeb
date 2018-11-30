package models;

import play.*;
import play.data.validation.Min;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.*;
import play.i18n.Messages;

import helpers.ERConstants;

import javax.persistence.*;

import ext.GsonExclude;
import extensions.StringFormatExtensions;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Product_Coverage extends Model {
	
	@ManyToOne(optional=false)
	public ER_Coverage coverage;
	
	public Boolean optional;
	
	@Valid
	@OneToMany(mappedBy="productCoverage", cascade=CascadeType.ALL)
	public List<ER_Product_Coverage_Value> values;
	
	@OneToMany(mappedBy="productCoverage", cascade=CascadeType.ALL)
	public List<ER_Product_Coverage_Deductible> deductibles;
	
	@GsonExclude
	@ManyToOne
	public ER_Product product;
	
	@Min(0)
	public BigDecimal minimumDeductible;
	
	@Min(0)
	public BigDecimal minimumPrime;
	
	@Min(0)
	public BigDecimal coverageValue;
	
	@ManyToOne
	public ER_Base_Field valueBase;
	
	public ER_Product_Coverage() {
		minimumDeductible = BigDecimal.ZERO;
		minimumPrime = BigDecimal.ZERO;
	}
	
	public ER_Product_Coverage(ER_Product_Coverage coverage) {
		
		if (coverage==null) {
			return;
		}
		
		this.coverage = coverage.coverage;
		this.optional = coverage.optional;
		this.minimumDeductible = coverage.minimumDeductible;
		this.minimumPrime = coverage.minimumPrime;
		this.coverageValue = coverage.coverageValue;
		this.valueBase = coverage.valueBase;
		
		if (coverage.values!=null && !coverage.values.isEmpty()) {
			List<ER_Product_Coverage_Value> valuesCopy = new ArrayList<ER_Product_Coverage_Value>();
			for (ER_Product_Coverage_Value value : coverage.values) {
				ER_Product_Coverage_Value valueCopy = new ER_Product_Coverage_Value(value);
				valueCopy.productCoverage = this;
				valuesCopy.add(valueCopy);
			}
			this.values = valuesCopy;
		}
		
		if (coverage.deductibles!=null && !coverage.deductibles.isEmpty()) {
			List<ER_Product_Coverage_Deductible> deductiblesCopy = new ArrayList<ER_Product_Coverage_Deductible>();
			for (ER_Product_Coverage_Deductible deductible : coverage.deductibles) {
				ER_Product_Coverage_Deductible deductibleCopy = new ER_Product_Coverage_Deductible(deductible);
				deductibleCopy.productCoverage = this;
				deductiblesCopy.add(deductibleCopy);
			}
			this.deductibles = deductiblesCopy;
		}
	}
	
	public String fieldsDescription() {
		
		switch (this.coverage.type.code) {
			case 2:
				return Messages.get("coverage.type.field.ranges");
			case 3:
				return Messages.get("coverage.type.field.amounts");
			case 4:
				return Messages.get("coverage.type.field.options");
			default:
				return null;
		}
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.optional!=null) {
			map.put("optional", this.optional);
		}
		if (this.coverage!=null) {
			map.put("coverage", this.coverage.toMap());
		}
		if (this.valueBase!=null) {
			map.put("valueBase", this.valueBase.toMap());
		}
		
		if (this.values!=null && !this.values.isEmpty()) {
			List<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
			for (ER_Product_Coverage_Value value : this.values) {
				parameters.add(value.toMap());
			}
			map.put("values", parameters);
		}
		
		return map;
	}
	
	public boolean isField() {
		
		if (this.valueBase == null) {
			return false;
		}
		
		if (this.valueBase.code == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE) {
			return false;
		}
		
		if ((this.coverage.type.code == ERConstants.CALCULATION_TYPE_AMOUNT || this.coverage.type.code == ERConstants.CALCULATION_TYPE_RANGE)) {
			return true;
		}
		return false;
	}
	
	public boolean isPartOfNetPrime() {
		
		boolean applyInNetPrime = false;
		
		if (coverage!=null && coverage.partOfNetPrime) {
			applyInNetPrime = coverage.partOfNetPrime;
		}
		
		if (valueBase!= null && valueBase.code!=null) {
			return valueBase.code != ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE && applyInNetPrime; 
		}
		
		return applyInNetPrime;
	}
	
	public List<Map<String, Object>> getOptions() {
		return getOptions(null);
	}
	
	public List<Map<String, Object>> getOptions(ER_Product product) {
		if (this.coverage.type.code == ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS) {
			List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
			for (ER_Product_Coverage_Value value : this.values) {
				if (value.lowRange!=null) {
					Map<String, Object> optionMap = new HashMap<String, Object>();
    				optionMap.put("id", value.id);
    				
    				String currencySymbol = "";
    				if (this.product !=null && this.product.currency!=null) {
    					currencySymbol = this.product.currency.symbol + ".";
    				} else if (product!=null && product.currency!=null) {
    					currencySymbol = product.currency.symbol + ".";
    				}
    				
    				optionMap.put("value", currencySymbol + " " + StringFormatExtensions.decimalFormat(value.lowRange));
    				options.add(optionMap);
				}
			}
			
			return options;
		}
		
		if (this.coverage.type.code == ERConstants.CALCULATION_TYPE_OPTIONS) {
			List<Map<String, Object>> options = new ArrayList<Map<String, Object>>();
			for (ER_Product_Coverage_Value value : this.values) {
				if (value.optionName!=null) {
					Map<String, Object> optionMap = new HashMap<String, Object>();
    				optionMap.put("id", value.id);
    				optionMap.put("value", value.optionName);
    				options.add(optionMap);
				}
			}
			
			return options;
		}
		
		return null;
	}
}
