package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Task_Type extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	@MaxSize(150)
	@Column(length=150)
	public String description;
	
	@Required
	public Boolean active;
    
}
