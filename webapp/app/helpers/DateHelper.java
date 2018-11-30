package helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import models.ER_General_Configuration;

public class DateHelper {

	public static Date guardExpirationDate(Date startDate) {
		
		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
		int daysToExpire = (configuration!=null && configuration.guardValidityDays!=null)?configuration.guardValidityDays:0;
		Date expireDate = new Date(startDate.getTime() + daysToExpire*24*60*60*1000);
		
		return expireDate;
		
		
	}
	
	public static Date guardReminderDate(Date expirationDate) {
		
		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
		int hoursBefore = (configuration!=null && configuration.guardReminderTime!=null)?configuration.guardReminderTime:12;
		Date prevDate = new Date(expirationDate.getTime() - hoursBefore*60*60*1000);
		
		return prevDate;
		
	}
	
	public static Date taskReminderDate(Date dueDate) {
		
		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
		int hoursBefore = (configuration!=null && configuration.taskReminderTime!=null)?configuration.taskReminderTime:24;
		Date prevDate = new Date(dueDate.getTime() - hoursBefore*60*60*1000);
		
		return prevDate;
	}
	
	public static List<Integer> validYears() {

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int endYear = currentYear + 1;
        int startYear = endYear - 25;

        ER_General_Configuration configuration = ER_General_Configuration.generalConfiguration();
        if (configuration.minimumCarYear!=null) {
            startYear = configuration.minimumCarYear;
        }

        if (configuration.maximumCarYear!=null) {
            endYear = configuration.maximumCarYear;
        }
		
		List<Integer> years = new ArrayList<Integer>();
		for (int i=startYear; i<= endYear; i++) {
			years.add(i);
		}
		
		return years;
	}
	
	public static final String formatDate(Date date, String format){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(format, new Locale("es", "GT"));
			return dateFormat.format(date);
		}catch(Exception e){
			return "";
		}
	}

    public static Date addDays(Date date, Integer noDias) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, noDias);

        return calendar.getTime();
    }

    public static Date addMonths(Date date, Integer noMeses) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, noMeses);

        return calendar.getTime();
    }
}
