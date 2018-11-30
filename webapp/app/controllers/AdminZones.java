package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Profession;
import models.ER_Zone;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminZones extends AdminBaseController {

	private static void filterZones(String name) {
		
		//Validate the parameters
		boolean validZone = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validZone) {
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
		ModelPaginator zones = null;
		if (filter.isValidFilter()) {			
			zones = new ModelPaginator(ER_Zone.class, filter.getQuery(), filter.getParametersArray());
		} else {
			zones = new ModelPaginator(ER_Zone.class);
		}
	
		zones.orderBy("name ASC");
		zones.setPageNumber(page);
		zones.setPageSize(10);
		renderArgs.put("zones", zones);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Zones list
	 * ************************************************************************************************************************
	 */
	
    public static void zonesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add zones to the renderArgs
    	filterZones(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("zone",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchZones(String zone, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		zonesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",zone);
    	}
    	zonesList(zone, true);
    }

    /*
     * *****************************************************************
     * Edit Profession
     * *****************************************************************
     */
    public static void editZone(Long id) {
    	
    	if (id!=null) {
    		ER_Profession model = ER_Profession.findById(id);
    		renderArgs.put("profession", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Profession
     * *****************************************************************
     */
    public static void saveZone(@Valid ER_Zone zone, Long zoneId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("zone.create.fielderrors"));
    		validation.keep();
    		editZone(zone.id);
    	} else {
    		
    		if( zoneId == null ) {
    			zone.save();
    			flash.success(Messages.get("zone.create.success",1));
    		} else {
    			ER_Zone currentProfession = ER_Zone.findById(zoneId);
    			currentProfession.name = zone.name;
    			currentProfession.active = zone.active;
    			currentProfession.save();
    			flash.success(Messages.get("profession.update.success"));
    		}
    		
    	}
    	
    	zonesList(zone.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Profession
     * *****************************************************************
     */
    public static void deleteZone(Long zoneId) {
    	flash.clear();
    	
    	ER_Zone zone = ER_Zone.findById(zoneId);
    	if(zone.id!=null) {
    		zone.delete();
        	flash.success(Messages.get("profession.delete.success"));
    	}else {
    		flash.error(Messages.get("profession.delete.error"));
    	}
    	
    	zonesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewZoneTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Zonas.xls");
    	renderTemplate("Reports/Zones.xls");
    	
    }
    
    public static void loadZoneFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Zone search = ER_Zone.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				
    				if(search==null) {
    					ER_Zone newZone = new ER_Zone();
        				newZone.name = sheet.getCell(2,row).getContents();
        				newZone.transferCode = sheet.getCell(1,row).getContents();
        				newZone.active = "1".equals(sheet.getCell(3,row).getContents());
                		
        				newZone.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("zone.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("zone.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("zone.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("zone.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newZoneFromExcel();
    	
    }
    
    public static void newZoneFromExcel() {
    	render();
    }

}
