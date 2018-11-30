package controllers;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.ivy.util.DateUtil;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import models.ER_Guard_Reminder;
import models.ER_Reminder;
import notifiers.Mails;
import play.jobs.Every;
import play.jobs.Job;

@Every("45mn")
public class ReminderGuardsJobs extends Job{

	
	public void doJob() {
		
		System.out.println("tarea ejecutada");
		
		List<ER_Reminder> activeReminders = ER_Reminder.find("active = ?", true).fetch();
        for (ER_Reminder reminder: activeReminders) {
        	
        	Calendar c = Calendar.getInstance();
        	c.setTimeInMillis(System.currentTimeMillis());
        	long startTime = reminder.registerDate.getTime();
        	long endTime = c.getTime().getTime();
        	long diffTime = endTime - startTime;
        	long diffDays = diffTime / (1000 * 60 * 60 * 24);
        	
        	if((int)diffDays % reminder.dayNumber ==0) {
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
	
}
