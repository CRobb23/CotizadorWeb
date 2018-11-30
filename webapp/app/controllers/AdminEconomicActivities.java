package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Economic_Activity;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminEconomicActivities extends AdminBaseController {

	private static void filterEconomicActivities(String name) {
		
		//Validate the parameters
		boolean validEconomicActivity = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validEconomicActivity) {
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
		ModelPaginator economicActivities = null;
		if (filter.isValidFilter()) {			
			economicActivities = new ModelPaginator(ER_Economic_Activity.class, filter.getQuery(), filter.getParametersArray());
		} else {
			economicActivities = new ModelPaginator(ER_Economic_Activity.class);
		}
		
		//Nationalties = new ModelPaginator(ER_EconomicActivity.class);
		
		economicActivities.orderBy("name ASC");
		economicActivities.setPageNumber(page);
		economicActivities.setPageSize(10);
		renderArgs.put("economicActivities", economicActivities);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * EconomicActivities list
	 * ************************************************************************************************************************
	 */
	
    public static void economicActivitiesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add economicActivities to the renderArgs
    	filterEconomicActivities(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("economicActivity",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchEconomicActivities(String economicActivity, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		economicActivitiesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",economicActivity);
    	}
    	economicActivitiesList(economicActivity, true);
    }

    /*
     * *****************************************************************
     * Edit EconomicActivity
     * *****************************************************************
     */
    public static void editEconomicActivity(Long id) {
    	
    	if (id!=null) {
    		ER_Economic_Activity model = ER_Economic_Activity.findById(id);
    		renderArgs.put("economicActivity", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit EconomicActivity
     * *****************************************************************
     */
    public static void saveEconomicActivity(@Valid ER_Economic_Activity economicActivity, Long economicActivityId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("economicActivity.create.fielderrors"));
    		validation.keep();
    		editEconomicActivity(economicActivity.id);
    	} else {
    		
    		if( economicActivityId == null ) {
    			economicActivity.save();
    			flash.success(Messages.get("economicActivity.create.success",1));
    		} else {
    			ER_Economic_Activity currentEconomicActivity = ER_Economic_Activity.findById(economicActivityId);
    			currentEconomicActivity.name = economicActivity.name;
    			currentEconomicActivity.active = economicActivity.active;
    			currentEconomicActivity.save();
    			flash.success(Messages.get("economicActivity.update.success"));
    		}
    		
    	}
    	
    	economicActivitiesList(economicActivity.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete EconomicActivity
     * *****************************************************************
     */
    public static void deleteEconomicActivity(Long economicActivityId) {
    	flash.clear();
    	
    	ER_Economic_Activity economicActivity = ER_Economic_Activity.findById(economicActivityId);
    	if(economicActivity.id!=null) {
    		economicActivity.delete();
        	flash.success(Messages.get("economicActivity.delete.success"));
    	}else {
    		flash.error(Messages.get("economicActivity.delete.error"));
    	}
    	
    	economicActivitiesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewEconomicActivityTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Actividad-Economica.xls");
    	renderTemplate("Reports/EconomicActivities.xls");
    	
    }
    
    public static void loadEconomicActivityFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Economic_Activity res = ER_Economic_Activity.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Economic_Activity newEconomicActivity = new ER_Economic_Activity();
    					newEconomicActivity.transferCode = sheet.getCell(1,row).getContents();
                		newEconomicActivity.name = sheet.getCell(2,row).getContents();
                		newEconomicActivity.description = sheet.getCell(3,row).getContents();
                		newEconomicActivity.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newEconomicActivity.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("economicActivity.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("economicActivity.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("economicActivity.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newEconomicActivityFromExcel();
    	
    }
    
    public static void newEconomicActivityFromExcel() {
    	render();
    }

}
