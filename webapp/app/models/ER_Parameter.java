package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Parameter extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	public Boolean required;
	
	@Required
	@MaxSize(25)
	@Column(length=25)
	public String identifier;
	
	@Required
	@Max(100)
	public Integer maxLength;
	
	public Integer parameterOrder;
	
	@Required
	public Boolean active;
	
	@Required
	@ManyToOne
	public ER_Parameter_Type type;
	
	@OneToMany(mappedBy="parameter", cascade=CascadeType.ALL, orphanRemoval=true)
	public List<ER_Parameter_Option> options;
	
	public Boolean multiple;
    
}
