package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.data.validation.MaxSize;
import play.db.jpa.Model;

@Entity
public class ER_Incident_Status extends Model {
	
	@MaxSize(50)
	@Column(length=50)
	public String name;
	
	public Integer code;
    
}
