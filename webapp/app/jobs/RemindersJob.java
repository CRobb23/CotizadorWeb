package jobs;

import helpers.ERConstants;
import models.ER_Guard_Reminder;
import models.ER_Task_Reminder;
import notifiers.Mails;
import play.Logger;
import play.jobs.Job;

import java.util.Date;
import java.util.List;

public class RemindersJob extends Job {
	public void doJob() {
		
		Date now = new Date();
        List<ER_Task_Reminder> pendingTaskReminders = ER_Task_Reminder.find("reminderDate <= ? and reminderSent = ?", now, false).fetch();
        for (ER_Task_Reminder reminder : pendingTaskReminders) {
        	
        	//Send task reminder
        	if (reminder.task!=null) {
        		if (reminder.task.status.code != ERConstants.TASK_STATUS_COMPLETE) {
        			reminder.reminderSent = true;
        			reminder.save();
        			if (Mails.taskReminder(reminder)) {
                		Logger.debug("Reminder sent to: %s", reminder.emailsList);
                	}
        		} else {
        			reminder.reminderSent = true;
        			reminder.save();
        			Logger.debug("Reminder marked as sent: %d", reminder.id);
        		}
        	}
        }
        
        List<ER_Guard_Reminder> pendingGuardReminders = ER_Guard_Reminder.find("reminderDate <= ? and reminderSent = ?", now, false).fetch();
        for (ER_Guard_Reminder reminder : pendingGuardReminders) {
        	
        	//Send guard reminder
        	if (reminder.guard!=null) {
        		if (reminder.guard.isExpired()) {
        			reminder.reminderSent = true;
        			reminder.save();
        			if (Mails.guardExpiration(reminder)) {
                		Logger.debug("Expiration notice sent to: %s", reminder.guard.incident.client.email);
                	}
        		} else {
        			reminder.reminderSent = true;
        			reminder.save();
        			if (Mails.guardReminderClient(reminder)) {
                		Logger.debug("Guard reminder sent to: %s", reminder.guard.incident.client.email);
                	}
        			
        			if (Mails.guardReminderAgent(reminder)) {
                		Logger.debug("Guard reminder sent to: %s", reminder.user.email);
                	}
        		}
        	}
        }
    }
}
