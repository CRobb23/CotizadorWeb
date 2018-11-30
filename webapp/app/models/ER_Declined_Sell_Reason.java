package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Declined_Sell_Reason extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	@MaxSize(100)
	@Column(length=100)
	public String description;
	
	@Required
	public Boolean active;

	@Required
	public Boolean isInspectionDeclined;
    
}
