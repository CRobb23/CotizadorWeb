package models;

import play.*;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.db.jpa.*;

import helpers.ERConstants;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Base_Field extends Model {
	
	@MaxSize(100)
	@Column(length=100)
	public String name;
	
	public Integer code;
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.code!=null) {
			map.put("code", this.code);
		}
		
		return map;
	}
	
	public static boolean fieldCodeAppliesForType(int fieldCode, int calculationTypeCode, int categoryCode) {
		if (categoryCode==ERConstants.CATEGORY_TYPE_SECTION_ONE || categoryCode==ERConstants.CATEGORY_TYPE_SECTION_TWO || categoryCode==ERConstants.CATEGORY_TYPE_SECTION_THREE) {
			if (fieldCode == ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE) {
				return false;
			}
		}
		
		if (calculationTypeCode == ERConstants.CALCULATION_TYPE_AMOUNT) {
			return (fieldCode==ERConstants.CALCULATION_BASE_FIELD || fieldCode == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE || fieldCode==ERConstants.CALCULATION_BASE_GTQ || fieldCode==ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE);
		} else if (calculationTypeCode == ERConstants.CALCULATION_TYPE_RANGE) {
			return (fieldCode==ERConstants.CALCULATION_BASE_FIELD || fieldCode == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE || fieldCode==ERConstants.CALCULATION_BASE_GTQ || fieldCode==ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE);
		} else if (calculationTypeCode == ERConstants.CALCULATION_TYPE_FIXED) {
			return (fieldCode==ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE || fieldCode==ERConstants.CALCULATION_BASE_GTQ || fieldCode==ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE);
		} else if (calculationTypeCode == ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS) {
			return (fieldCode==ERConstants.CALCULATION_BASE_FIELD || fieldCode == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE || fieldCode==ERConstants.CALCULATION_BASE_GTQ || fieldCode==ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE);
		} else if (calculationTypeCode == ERConstants.CALCULATION_TYPE_OPTIONS) {
			return (fieldCode == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE || fieldCode==ERConstants.CALCULATION_BASE_GTQ || fieldCode==ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE);
		}
		
		return false;
	}
	
	public boolean appliesForType(int calculationTypeCode, int categoryCode) {
		return ER_Base_Field.fieldCodeAppliesForType(this.code, calculationTypeCode, categoryCode);
	}
}
