package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import helpers.ERConstants;

import javax.persistence.*;

import ext.GsonExclude;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Coverage extends Model {
    
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@MaxSize(100)
	@Column(length=100)
	public String description;
	
	@MaxSize(75)
	@Column(length=75)
	public String quotationName;
	
	@MaxSize(100)
	@Column(length=100)
	public String quotationDescription;
	
	@MaxSize(75)
	@Column(length=75)
	public String fieldDescription;
	
	@Required
	@ManyToOne
	public ER_Calculation_Type type;
	
	@Required
	@ManyToOne
	public ER_Coverage_Category category;
	
	public Integer coverageOrder;
	
	@GsonExclude
	@Column(name="transfer_code")
	public String transferCode;
	
	public boolean externals;
	public boolean partOfNetPrime;
	public boolean applyDiscount;
	
	public ER_Coverage() {
		this.externals = false;
		this.partOfNetPrime = true;
		this.applyDiscount = true;
	}
	
	public boolean isTheftCoverage() {
		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
		if (configuration !=null) {
			return this.equals(configuration.theftCoverage);
		}
		
		return false;
	}
	
	public boolean canHaveMultipleValues() {
		if (this.type==null) {
			return false;
		}
		
		return (this.type.code == ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS || 
				this.type.code == ERConstants.CALCULATION_TYPE_RANGE || 
				this.type.code == ERConstants.CALCULATION_TYPE_OPTIONS);
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.fieldDescription!=null) {
			map.put("fieldDescription", this.fieldDescription);
		}
		if (this.type!=null) {
			map.put("type", this.type.toMap());
		}
		if (this.coverageOrder!=null) {
			map.put("coverageOrder", this.coverageOrder);
		}
		
		return map;
	}
}
