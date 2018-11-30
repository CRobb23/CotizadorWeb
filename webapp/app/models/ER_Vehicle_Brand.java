package models;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import ext.GsonExclude;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Vehicle_Brand extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	public BigDecimal theftDeductible;

	@Required
	@Column(name="transfer_code")
	public String transferCode;
	
	@GsonExclude
	@OneToMany(mappedBy="brand")
	public List<ER_Vehicle_Line> lines;
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.theftDeductible!=null) {
			map.put("theftDeductible", this.theftDeductible);
		}
		return map;
	}
}
