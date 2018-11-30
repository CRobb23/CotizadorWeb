package models;

import play.*;
import play.db.jpa.*;

import javax.persistence.*;

import objects.QuotationDetail;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.*;

@Entity
public class ER_Task_Reminder extends Model {
	
	@ManyToOne
	public ER_User user;
	
	public Date creationDate;
	
	public Date reminderDate;
	
	public String subject;
	
	public boolean reminderSent;
	
	@Column(columnDefinition = "TEXT")
	public String emails;
	
	@Transient
	public List<String> emailsList;
	
	@ManyToOne(cascade=CascadeType.ALL)
	public ER_Task task;
	
	@PrePersist
	@PreUpdate
	public void generateDetailJSON() {
		if (this.emailsList!=null) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<String>>(){}.getType();
			this.emails = gson.toJson(this.emailsList, type).toString();
		}
	}
	
	@PostLoad
	public void loadDetailJSON() {
		if (this.emails!=null) {
			Gson gson = new Gson();
			Type type = new TypeToken<List<String>>(){}.getType();
			this.emailsList = gson.fromJson(this.emails, type);
		}
	}
}
