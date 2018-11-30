package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.apache.ivy.util.DateUtil;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Workbook;
import jxl.write.DateFormat;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.ER_Guard_Reminder;
import models.ER_Reminder;
import notifiers.Mails;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminReminders extends AdminBaseController{

private static void filterReminders(String caseNumber) {
		
		//validate the parameters
		boolean validReminder = GeneralMethods.validateParameter(caseNumber);
		
		//Create a new Filter and write the query for each parameter
		Filter filter = new Filter();
		
		if(validReminder) {
			filter.addQuery("case_number like ?", "%"+caseNumber+"%");
		}
		
		int page;
		if (params.get("page") == null) {
			// verifying if it was in other page
			if (session.get("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(session.get("page"));
			}
		} else {
			page = Integer.parseInt(params.get("page"));
			session.put("page", page);
		}
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		ModelPaginator reminders = null;
		if(filter.isValidFilter()) {
			reminders = new ModelPaginator(ER_Reminder.class,filter.getQuery(),filter.getParametersArray());
		}else {
			reminders = new ModelPaginator(ER_Reminder.class);
		}
		
		reminders.orderBy("case_number ASC");
		reminders.setPageSize(10);
		reminders.setPageNumber(page);
		
		renderArgs.put("reminders", reminders);
	}
	
	
	public static void remindersList(String caseNumber, boolean intern) {
		
		//flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
		
    	//filter by name
		filterReminders(caseNumber);
		
		//if it was a search
    	if(caseNumber!=null) {
    		renderArgs.put("reminder",caseNumber);
    	}
		
		render();
	}
	
	public static void searchReminders(String reminder,String all,boolean intern, boolean internalSearch) {
		
		flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		remindersList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",reminder);
    	}
    	remindersList(reminder, true);
		
	}
	
	/*
     * *****************************************************************
     * Edit Reminder
     * *****************************************************************
     */
    public static void editReminder(Long id) {
    	
    	if (id!=null) {
    		ER_Reminder model = ER_Reminder.findById(id);
    		renderArgs.put("reminder", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Save Reminder
     * *****************************************************************
     */
    public static void saveReminder(@Valid ER_Reminder reminder, Long reminderId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("reminder.create.fielderrors"));
    		validation.keep();
    		editReminder(reminder.id);
    	} else {
    		
    		if( reminderId == null ) {
    			reminder.save();
    			flash.success(Messages.get("reminder.form.create.success"));
    		} else {
    			ER_Reminder currentReminder = ER_Reminder.findById(reminderId);
    			currentReminder.caseNumber = reminder.caseNumber;
    			currentReminder.dayNumber = reminder.dayNumber;
    			currentReminder.hour = reminder.hour;
    			currentReminder.minute = reminder.minute;
    			currentReminder.active = reminder.active;
    			currentReminder.save();
    			flash.success(Messages.get("reminder.update.success"));
    		}
    		
    	}
    	
    	remindersList(reminder.caseNumber, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Reminder
     * *****************************************************************
     */
    public static void deleteReminder(Long reminderId) {
    	flash.clear();
    	
    	ER_Reminder reminder = ER_Reminder.findById(reminderId);
    	if(reminder.id!=null) {
    		reminder.delete();
        	flash.success(Messages.get("reminder.delete.success"));
    	}else {
    		flash.error(Messages.get("reminder.delete.error"));
    	}
    	
    	remindersList(null,true);
    	
    }
    
    //Ejecutar cada 15 minutos
    public static void sendExcelGuards() {
    	System.out.println("tarea ejecutada");
		
		List<ER_Reminder> activeReminders = ER_Reminder.find("active = ?", true).fetch();
        for (ER_Reminder reminder: activeReminders) {
        	
        	Calendar c = Calendar.getInstance();
        	c.setTimeInMillis(System.currentTimeMillis());
        	
        	if(reminder.hour==c.get(Calendar.HOUR_OF_DAY)) {
        		if( reminder.minute >= c.get(Calendar.MINUTE) || reminder.minute <= c.get(Calendar.MINUTE) ) {
        			
        			WritableWorkbook report;
        			File excelFile = new File("/tmp/guardInspections.xls");
        			try {
        				report = Workbook.createWorkbook(excelFile);
    					WritableSheet excelSheet = report.createSheet("Resguardos", 0);
    					
    					Label inspectionNumber = new Label(1, 1, "No. Inspección");
    					excelSheet.addCell(inspectionNumber);
    					Label guardDate = new Label(2, 1, "Fecha Resguardo")	;
    					excelSheet.addCell(guardDate);
    					Label clientName = new Label(3,1, "Nombre de Cliente");
    					excelSheet.addCell(clientName);
    					
    					Integer rowNumber = 2;
    					
    					List<ER_Guard_Reminder> pendingGuardReminders = ER_Guard_Reminder.find("reminderDate <= ? and reminderSent = ?", c.getTime(), false).fetch();
            	        for (ER_Guard_Reminder guardReminder : pendingGuardReminders) {
            	        	
            	        	
            	        	try {            					
            					
            					jxl.write.Number col1 = new Number(1, rowNumber, guardReminder.guard.incident.number);
            					excelSheet.addCell(col1);
            					SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            					Label col2 = new Label(2, rowNumber, formatter.format(guardReminder.guard.expirationDate));
            					excelSheet.addCell(col2);
            					Label col3 = new Label(3,rowNumber, guardReminder.guard.incident.client.getFullName());
            					excelSheet.addCell(col3);
        						
        					} catch (Exception e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					} 
            	        	
            	        	rowNumber++;
            	        	
            	        }
            	        
            	        report.write();
            	        report.close();
            	        
            	        Mails.guardReport(excelFile.getAbsolutePath(), reminder.customEmail);
            	        
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
        			
        		}
        	}
        	
        } 
    }
}
