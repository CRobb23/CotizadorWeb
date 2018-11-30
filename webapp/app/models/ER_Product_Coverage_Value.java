package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ext.GsonExclude;
import extensions.StringFormatExtensions;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Valid;
import play.db.jpa.Model;

@Entity
public class ER_Product_Coverage_Value extends Model {
	
	@Min(0)
	public BigDecimal lowRange;
	
	@Min(0)
	public BigDecimal highRange;
	
	@MaxSize(75)
	@Column(length=75)
	public String optionName;
	
	@Valid
	@OneToMany(mappedBy="productCoverageValue", cascade=CascadeType.ALL)
	public List<ER_Product_Coverage_Class_Value> values;
	
	@GsonExclude
	@ManyToOne
	public ER_Product_Coverage productCoverage;
    
	public ER_Product_Coverage_Value(ER_Product_Coverage_Value value) {
		
		if(value==null){
			return;
		}
		
		this.lowRange = value.lowRange;
		this.highRange = value.highRange;
		this.optionName = value.optionName;
		
		if (value.values!=null && !value.values.isEmpty()) {
			List<ER_Product_Coverage_Class_Value> valuesCopy = new ArrayList<ER_Product_Coverage_Class_Value>();
			for (ER_Product_Coverage_Class_Value classValue : value.values) {
				ER_Product_Coverage_Class_Value valueCopy = new ER_Product_Coverage_Class_Value(classValue);
				valueCopy.productCoverageValue = this;
				valuesCopy.add(valueCopy);
			}
			this.values = valuesCopy;
		}
	}
	
	public String fieldDescription() {
		switch (this.productCoverage.coverage.type.code) {
		case 2:
			return StringFormatExtensions.decimalFormat(this.lowRange) + " - " + ((this.highRange!=null)?StringFormatExtensions.decimalFormat(this.highRange):"");
		case 3:
			return StringFormatExtensions.decimalFormat(this.lowRange);
		case 4: 
			return this.optionName;
		}
		
		return "";
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.lowRange!=null) {
			map.put("lowRange", this.lowRange);
		}
		if (this.highRange!=null) {
			map.put("highRange", this.highRange);
		}
		if (this.optionName!=null) {
			map.put("optionName", this.optionName);
		}
		
		return map;
	}
	
	public ER_Product_Coverage_Class_Value getCoverageClassValue(Long id){
		for(ER_Product_Coverage_Class_Value coverageClassValue: values){
			if(coverageClassValue.vehicleClass.id.equals(id)){
				return coverageClassValue;
			}
		}
		return null;
	}
}
