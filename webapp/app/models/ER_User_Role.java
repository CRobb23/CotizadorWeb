package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_User_Role extends Model {
    
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	@MaxSize(100)
	@Column(length=100)
	public String description;
	
	@Required
	public Integer code;
	
	public Map<String, Object> toMap() {
    	Map<String, Object> map = new HashMap<String,Object>();
    	if (name!=null) {
    		map.put("name", this.name);
    	}
    	if (code!=null) {
    		map.put("code", this.code);
    	}
    	return map;
	}
	
}
