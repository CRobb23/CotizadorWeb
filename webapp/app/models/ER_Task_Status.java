package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.util.*;

@Entity
public class ER_Task_Status extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	public Integer code;
    
}
