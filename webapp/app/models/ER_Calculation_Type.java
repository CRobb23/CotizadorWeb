package models;

import play.*;
import play.data.validation.Max;
import play.data.validation.MaxSize;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Calculation_Type extends Model {

	@MaxSize(50)
	@Column(length=50)
	public String name;
	
	public Integer code;
	
	@MaxSize(150)
	@Column(length=150)
	public String description;
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.code!=null) {
			map.put("code", this.code);
		}
		
		return map;
	}
}
