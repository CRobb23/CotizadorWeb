package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Vehicle_Type;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminVehicleTypes extends AdminBaseController {

	private static void filterVehicleTypes(String name) {
		
		//Validate the parameters
		boolean validVehicleType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validVehicleType) {
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
		ModelPaginator vehicleTypes = null;
		if (filter.isValidFilter()) {			
			vehicleTypes = new ModelPaginator(ER_Vehicle_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			vehicleTypes = new ModelPaginator(ER_Vehicle_Type.class);
		}
		
		vehicleTypes.orderBy("name ASC");
		vehicleTypes.setPageNumber(page);
		vehicleTypes.setPageSize(10);
		renderArgs.put("vehicleTypes", vehicleTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Vehicle types list
	 * ************************************************************************************************************************
	 */
	
    public static void vehicleTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add vehicle Types to the renderArgs
    	filterVehicleTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("vehicleType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchVehicleType(String type, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		vehicleTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",type);
    	}
    	vehicleTypesList(type, true);
    }

    /*
     * *****************************************************************
     * Edit Vehicle Type
     * *****************************************************************
     */
    public static void editVehicleType(Long id) {
    	
    	if (id!=null) {
    		ER_Vehicle_Type model = ER_Vehicle_Type.findById(id);
    		renderArgs.put("vehicleType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Vehicle Type
     * *****************************************************************
     */
    public static void saveVehicleType(@Valid ER_Vehicle_Type vehicleType, Long vehicleTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("vehicleType.create.fielderrors"));
    		validation.keep();
    		editVehicleType(vehicleTypeId);
    	} else {
    		
    		if( vehicleTypeId == null ) {
    			vehicleType.save();
    			flash.success(Messages.get("vehicleType.create.success",1));
    		} else {
    			ER_Vehicle_Type currentVehicleType = ER_Vehicle_Type.findById(vehicleTypeId);
    			currentVehicleType.name = vehicleType.name;
    			currentVehicleType.active = vehicleType.active;
    			currentVehicleType.save();
    			flash.success(Messages.get("vehicleType.update.success"));
    		}
    		
    	}
    	
    	vehicleTypesList(vehicleType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Vehicle Type
     * *****************************************************************
     */
    public static void deleteVehicleType(Long vehicleTypeId) {
    	flash.clear();
    	
    	ER_Vehicle_Type vehicleType = ER_Vehicle_Type.findById(vehicleTypeId);
    	if(vehicleType.id!=null) {
    		vehicleType.delete();
        	flash.success(Messages.get("vehicleType.delete.success"));
    	}else {
    		flash.error(Messages.get("vehicleType.delete.error"));
    	}
    	
    	vehicleTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewVehicleTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipos-Vehiculo.xls");
    	renderTemplate("Reports/VehicleTypes.xls");
    	
    }
    
    public static void loadVehicleTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Vehicle_Type search = ER_Vehicle_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				
    				if(search==null) {
    					ER_Vehicle_Type newVehicleType = new ER_Vehicle_Type();
                		newVehicleType.name = sheet.getCell(2,row).getContents();
                		newVehicleType.transferCode = sheet.getCell(1,row).getContents();
                		newVehicleType.active = "1".equals(sheet.getCell(3,row).getContents());
                		
                		newVehicleType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("vehicleType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("vehicleType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("vehicleType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("vehicleType.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newVehicleTypeFromExcel();
    	
    }
    
    public static void newVehicleTypeFromExcel() {
    	render();
    }

}
