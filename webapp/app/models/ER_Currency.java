package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Currency extends Model {
	
	@Column(length=30)
	public String name;
	
	@Column(length=5, nullable=false)
	public String symbol;
	
	@Column(unique=true)
	public int code;
	
	public boolean active;
	
	@Required
	@Min(0)
	public BigDecimal exchangeRate;
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.symbol!=null) {
			map.put("symbol", this.symbol);
		}
		if (this.exchangeRate!=null) {
			map.put("exchangeRate", this.exchangeRate);
		}
		
		map.put("code", this.code);
		
		return map;
	}
}
