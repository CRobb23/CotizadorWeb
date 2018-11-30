package controllers;

import helpers.ERConstants;
import helpers.ReminderHelper;

import java.util.Date;
import java.util.List;

import models.*;
import play.data.validation.*;
import play.i18n.Messages;
import play.mvc.*;

@With(Secure.class)
public class UserTasks extends AdminBaseController {

	@Access
    public static void tasksList() {
    	
    	List<ER_Task> expiredTasks = ER_Task.find("incident.creator.email = ? and dueDate <= ? and status.code = ? order by dueDate ASC", Security.connected(), new Date(), ERConstants.TASK_STATUS_PENDING).fetch();
    	List<ER_Task> pendingTasks = ER_Task.find("incident.creator.email = ? and dueDate > ? and status.code = ? order by dueDate ASC", Security.connected(), new Date(), ERConstants.TASK_STATUS_PENDING).fetch();
        render(expiredTasks, pendingTasks);
    }
    
    public static void completeTask(Long id) {
    	
    	flash.discard();
    	
    	if (id!=null) {
    		ER_Task task = ER_Task.findById(id);
    		if (task!=null) {
    			if (task.incident !=null && task.incident.creator.equals(connectedUser()) ) {
    				
    				if (task.status.code!= ERConstants.TASK_STATUS_COMPLETE) {
    					task.completionDate = new Date();
    				}
    				
    				task.status = ER_Task_Status.find("code = ?", ERConstants.TASK_STATUS_COMPLETE).first();
    				task.save();
    				
    				render(task);
    			}
    		}
    	}
    	
    	flash.error(Messages.get("tasks.list.complete.error"));
    	tasksList();
    	
    }
    
    public static void newTask(Long id) {
    	if (id!=null) {
    		ER_Incident incident = ER_Incident.findById(id);
        	if (incident!=null && incident.creator.equals(connectedUser()) && !incident.isFinalized()) {
    	    	List<ER_Task_Type> types = ER_Task_Type.find("active = 1 order by name").fetch();
    	    	render(id, types);
        	}
    	}
    	
    	tasksList();
    }
    
    public static void createTask(Long incidentId, @Valid ER_Task task) {
    	
    	flash.discard();
    	
    	if (incidentId==null) {
    		tasksList();
    	}
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		validation.keep();
    		newTask(incidentId);
    	} else {
    		ER_Incident incident = ER_Incident.findById(incidentId);
    		if (incident!=null) {
    			if (incident.creator.equals(connectedUser()) && !incident.isFinalized()) {
    				task.status = ER_Task_Status.find("code = ?", ERConstants.TASK_STATUS_PENDING).first();
    				task.incident = incident;
    				task.save();
    				
    				//Create reminders
    		    	task.createReminder();
    				
    				flash.success(Messages.get("tasks.list.create.success"));
    				
    				tasksList();
    			}
    		}
    	}
    }

}
