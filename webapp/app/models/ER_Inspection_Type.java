package models;

import play.*;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.*;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.*;

@Entity
public class ER_Inspection_Type extends Model {
    
	@Column(length=75)
	public String name;
	
	@Column(unique=true)
	public int code;
	
	public Integer typeOrder;
}
