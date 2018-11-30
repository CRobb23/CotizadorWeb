package models;

import helpers.ReminderHelper;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PreRemove;
import javax.persistence.Query;

import play.data.binding.As;
import play.data.validation.InFuture;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
public class ER_Task extends Model {
	
	@ManyToOne(optional=false, cascade=CascadeType.ALL)
	public ER_Incident incident;
	
	@ManyToOne(optional=false)
	public ER_Task_Status status;
	
	@Required
	@ManyToOne(optional=false)
	public ER_Task_Type type;
	
	@MaxSize(150)
	@Column(length=150)
	public String description;
	
	@InFuture()
	@As("dd/MM/yyyy HH:mm")
	@Required
	public Date dueDate;
	
	public Date completionDate;
		
	public void createReminder() {
		ReminderHelper.createReminderForTask(this, this.incident.creator.email);
	}

	@PreRemove
	public void beforeDelete() {
		List<ER_Task_Reminder> reminders = ER_Task_Reminder.find("byTask", this).fetch();
		Query query = JPA.em().createQuery("delete from ER_Task_Reminder tr where tr.task = :task");
		query.setParameter("task", this);
		int deleted = query.executeUpdate();
	}
}
