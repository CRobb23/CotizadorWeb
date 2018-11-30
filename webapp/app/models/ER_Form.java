package models;

import play.*;
import play.db.jpa.*;
import play.data.validation.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Form extends Model {
	
	@Required
	@MaxSize(150)
	@Column(length=150)
	public String templatePath;
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	public boolean landscape;
	
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "ER_Form_Parameters", 
		joinColumns = {@JoinColumn(name = "form_id", nullable = false) },
		inverseJoinColumns = {@JoinColumn(name = "parameter_id", nullable = false)}
	)
	@OrderBy("parameterOrder")
	public List<ER_Parameter> parameters;
}
