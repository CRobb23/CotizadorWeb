package jobs;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import play.jobs.*;
import play.test.Fixtures;
import models.*;

@OnApplicationStart
public class Bootstrap extends Job {

	public void doJob() {
        
        if (ER_Base_Field.count()==0) {
        	Fixtures.loadModels("ER_Base_Field.yml");
        }
        
        if (ER_Calculation_Type.count()==0) {
        	Fixtures.loadModels("ER_Calculation_Type.yml");
        }
        
        if (ER_Coverage_Category.count()==0) {
        	Fixtures.loadModels("ER_Coverage_Category.yml");
        }
        
        if (ER_Parameter_Type.count()==0) {
        	Fixtures.loadModels("ER_Parameter_Type.yml");
        }
        
        if (ER_User.count()==0 && ER_User_Role.count()==0) {
        	Fixtures.loadModels("ER_User.yml");
        }
        
        if (ER_Vehicle_Class.count()==0) {
        	Fixtures.loadModels("ER_Vehicle_Class.yml");
        }
        
        if (Frecuency.count()==0 && ER_Payment_Frecuency.count()==0) {
        	Fixtures.loadModels("Frecuency.yml");
        }
        
        if (ER_Incident_Status.count()==0) {
        	Fixtures.loadModels("ER_Incident_Status.yml");
        }
        
        if (ER_Task_Status.count()==0) {
        	Fixtures.loadModels("ER_Task_Status.yml");
        }
        
        if (ER_Currency.count()==0) {
        	Fixtures.loadModels("ER_Currency.yml");
        }
        
        if (ER_Inspection_Type.count()==0) {
        	Fixtures.loadModels("ER_Inspection_Type.yml");
        }
        
        //Get current time to determine reminders job start time
        Calendar calendar = Calendar.getInstance();
        int minutes = calendar.get(Calendar.MINUTE);
       
        if (minutes>=0 && minutes<15) {
        	calendar.set(Calendar.MINUTE, 15);
        } else if (minutes>=15 && minutes<30) {
        	calendar.set(Calendar.MINUTE,30);
        } else if (minutes>=30 && minutes<45) {
        	calendar.set(Calendar.MINUTE,45);
        } else if (minutes>=45 && minutes<59) {
        	calendar.add(Calendar.HOUR_OF_DAY, 1);
        	calendar.set(Calendar.MINUTE,0);
        }
        
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        
        //Start job at the determined date
        new Timer().schedule(new TimerTask() {          
            @Override
            public void run() {
            	RemindersJob job = new RemindersJob();
                job.every(60*15);
                job.now();       
            }
        }, calendar.getTime());
    }
}
