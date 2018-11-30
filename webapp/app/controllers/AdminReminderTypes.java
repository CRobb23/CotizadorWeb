package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Reminder_Type;
import models.ER_Reminder_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminReminderTypes extends AdminBaseController {

	private static void filterReminderTypes(String name) {
		
		//Validate the parameters
		boolean validReminderType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validReminderType) {
			filter.addQuery("name like ?", "%"+name+"%");
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
		ModelPaginator reminderTypes = null;
		if (filter.isValidFilter()) {			
			reminderTypes = new ModelPaginator(ER_Reminder_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			reminderTypes = new ModelPaginator(ER_Reminder_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Reminder_Type.class);
		
		reminderTypes.orderBy("name ASC");
		reminderTypes.setPageNumber(page);
		reminderTypes.setPageSize(10);
		renderArgs.put("reminderTypes", reminderTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * ReminderTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void reminderTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add reminderTypes to the renderArgs
    	filterReminderTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("reminderType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchReminderTypes(String reminderType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		reminderTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",reminderType);
    	}
    	reminderTypesList(reminderType, true);
    }

    /*
     * *****************************************************************
     * Edit ReminderType
     * *****************************************************************
     */
    public static void editReminderType(Long id) {
    	
    	if (id!=null) {
    		ER_Reminder_Type model = ER_Reminder_Type.findById(id);
    		renderArgs.put("reminderType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit ReminderType
     * *****************************************************************
     */
    public static void saveReminderType(@Valid ER_Reminder_Type reminderType, Long reminderTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("reminderType.create.fielderrors"));
    		validation.keep();
    		editReminderType(reminderType.id);
    	} else {
    		
    		if( reminderTypeId == null ) {
    			reminderType.save();
    			flash.success(Messages.get("reminderType.create.success",1));
    		} else {
    			ER_Reminder_Type currentReminderType = ER_Reminder_Type.findById(reminderTypeId);
    			currentReminderType.name = reminderType.name;
    			currentReminderType.active = reminderType.active;
    			currentReminderType.save();
    			flash.success(Messages.get("reminderType.update.success"));
    		}
    		
    	}
    	
    	reminderTypesList(reminderType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete ReminderType
     * *****************************************************************
     */
    public static void deleteReminderType(Long reminderTypeId) {
    	flash.clear();
    	
    	ER_Reminder_Type reminderType = ER_Reminder_Type.findById(reminderTypeId);
    	if(reminderType.id!=null) {
    		reminderType.delete();
        	flash.success(Messages.get("reminderType.delete.success"));
    	}else {
    		flash.error(Messages.get("reminderType.delete.error"));
    	}
    	
    	reminderTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewReminderTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Alarma.xls");
    	renderTemplate("Reports/ReminderTypes.xls");
    	
    }
    
    public static void loadReminderTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Reminder_Type res = ER_Reminder_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Reminder_Type newReminderType = new ER_Reminder_Type();
    					newReminderType.transferCode = sheet.getCell(1,row).getContents();
                		newReminderType.name = sheet.getCell(2,row).getContents();
                		newReminderType.description = sheet.getCell(3,row).getContents();
                		newReminderType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newReminderType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("reminderType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("reminderType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("reminderType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newReminderTypeFromExcel();
    	
    }
    
    public static void newReminderTypeFromExcel() {
    	render();
    }

}
