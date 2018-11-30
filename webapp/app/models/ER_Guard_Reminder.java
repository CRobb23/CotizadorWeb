package models;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import play.db.jpa.Model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Entity
public class ER_Guard_Reminder extends Model {

	@ManyToOne
	public ER_User user;
	
	public Date creationDate;
	
	public Date reminderDate;
	
	public boolean reminderSent;
	
	@ManyToOne(cascade=CascadeType.ALL)
	public ER_Guard guard;
}
