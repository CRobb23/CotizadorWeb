package models;

import play.*;
import play.data.validation.MaxSize;
import play.data.validation.Phone;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class ER_User_Profile extends Model {
	
	@Required
	@Phone
	public Integer phoneNumber;
	
	@Required
	@MaxSize(10)
	@Column(length=10)
	public String agentCode;
	
	@MaxSize(255)
	@Column(length=255)
	public String mailSignature;
	
	public Map<String, Object> toMap() {
    	Map<String, Object> map = new HashMap<String,Object>();
    	if (id!=null) {
    		map.put("id", this.id);
    	}
    	if (phoneNumber!=null) {
    		map.put("phoneNumber", this.phoneNumber);
    	}
    	if (agentCode!=null) {
    		map.put("agentCode", this.agentCode);
    	}
    	
    	return map;
    }
    
}
