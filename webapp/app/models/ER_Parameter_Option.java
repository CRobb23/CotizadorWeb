package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Parameter_Option extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String value;
	
	@Required
	@ManyToOne
	public ER_Parameter parameter;
	
	public int optionOrder; 
	
}
