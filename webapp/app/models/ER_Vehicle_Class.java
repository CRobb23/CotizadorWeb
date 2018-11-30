package models;

import play.*;
import play.data.validation.MaxSize;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Vehicle_Class extends Model {
	
	@MaxSize(10)
	@Column(length=10)
	public String code;
	
	@MaxSize(100)
	@Column(length=100)
	public String description;
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.code!=null) {
			map.put("code", this.code);
		}
		
		return map;
	}
    
}
