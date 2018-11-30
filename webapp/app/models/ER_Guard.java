package models;

import play.*;
import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;
import java.util.*;

@Entity
public class ER_Guard extends Model {
	
	@Required
	@OneToOne(optional=false, cascade=CascadeType.ALL)
	public ER_Incident incident;
	
	@Required
	public Date expirationDate;
	
	@Required
	public Date creationDate;
	
	public boolean isExpired() {
		return this.expirationDate.compareTo(new Date()) <= 0;
	}
    
	@PreRemove
	public void beforeDelete() {
		List<ER_Guard_Reminder> guardReminderList = ER_Guard_Reminder.find("byGuard", this).fetch();
		Logger.debug("removing guard reminders : " + guardReminderList.size());
		Query query = JPA.em().createQuery("delete from ER_Guard_Reminder gr where gr.guard = :guard");
		query.setParameter("guard", this);
		int deleted = query.executeUpdate();
		Logger.debug("deleted " + deleted + " guard reminders");
	}
}
