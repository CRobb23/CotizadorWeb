package helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import play.Logger;
import play.i18n.Messages;
import models.*;

public class ReminderHelper {
	
	public static boolean createReminderForTask(ER_Task task, String email) {
		try {
			
			Date today = new Date();
			
			ER_Task_Reminder reminder = new ER_Task_Reminder();
			reminder.subject = Messages.get("reminders.task.subject", task.type.name);
			reminder.user = task.incident.creator;
			reminder.task = task;
			reminder.creationDate = today;
			
			//Reminder for due date
			List<String> emails = new ArrayList<String>();
			emails.add(email);
			reminder.emailsList = emails;
			reminder.reminderDate = task.dueDate;
			reminder.save();
			
			Date prevDate = DateHelper.taskReminderDate(task.dueDate);
			
			//Reminder for configured hours before
			if (prevDate.compareTo(today)>0) {
				ER_Task_Reminder secondReminder = new ER_Task_Reminder();
				secondReminder.subject = reminder.subject;
				secondReminder.user = reminder.user;
				secondReminder.creationDate = reminder.creationDate;
				secondReminder.emailsList = emails;
				secondReminder.reminderDate = prevDate;
				secondReminder.task = task;
				secondReminder.save();
			}
			
		} catch (Exception e) {
			Logger.error(e, "Error creating task reminder");
			return false;
		}
		
		return true;
		
	}
	
	public static boolean createReminderForGuard(ER_Guard guard) {
		
		try {
			
			Date today = new Date();
			
			ER_Guard_Reminder reminder = new ER_Guard_Reminder();
			reminder.user = guard.incident.creator;
			reminder.guard = guard;
			reminder.creationDate = today;
			
			//Reminder for due date
			reminder.reminderDate = guard.expirationDate;
			reminder.save();
		
			Date prevDate = DateHelper.guardReminderDate(guard.expirationDate);
		
			//Reminder for configured hours before
			if (prevDate.compareTo(today)>0) {
				ER_Guard_Reminder secondReminder = new ER_Guard_Reminder();
				secondReminder.user = reminder.user;
				secondReminder.creationDate = reminder.creationDate;
				secondReminder.reminderDate = prevDate;
				secondReminder.guard = guard;
				secondReminder.save();
			}
		
		} catch (Exception e) {
			Logger.error(e, "Error creating guard reminder");
			return false;
		}
		
		return true;
	}

}
