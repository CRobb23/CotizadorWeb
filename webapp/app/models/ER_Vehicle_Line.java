package models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import ext.GsonExclude;
import play.data.validation.MaxSize;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.StringUtil;

@Entity
public class ER_Vehicle_Line extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	public Boolean insurable;
	
	@Min(1)
	@Column(length=4)
	public Integer loJackYear;
	
	@Required
	@ManyToOne
	public ER_Vehicle_Brand brand;
	
	@ManyToOne
	@Required
	public ER_Vehicle_Class vehicleClass;
	
	@GsonExclude
	@OneToMany(mappedBy="line")
	public List<ER_Vehicle_Value> values;
	
	public BigDecimal theftDeductible;

	@Required
	@Column(name="transfer_code")
	public String transferCode;
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.insurable!=null) {
			map.put("insurable", this.insurable);
		}
		if (this.brand!=null) { 
			map.put("brand", this.brand.toMap());
		}
		if (this.vehicleClass!=null) {
			map.put("vehicleClass", this.vehicleClass.toMap());
		}
		if (this.loJackYear!=null) {
			map.put("loJackYear", this.loJackYear);
		}
		if (this.theftDeductible!=null) {
			map.put("theftDeductible", this.theftDeductible);
		}
		
		return map;
	}
	public int insurableIntValue() {
		return insurable != null && insurable.booleanValue() ? 1 : 0;
	}

	public boolean getLoJackAvailable() {
	    return Boolean.TRUE.equals(insurable) && vehicleClass != null && !StringUtil.isNullOrBlank(transferCode);
    }
}
