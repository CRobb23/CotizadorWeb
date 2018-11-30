package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Vehicle_Promotion_Type;
import models.ER_Vehicle_Promotion_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminVehiclePromotionTypes extends AdminBaseController {

	private static void filterVehiclePromotionTypes(String name) {
		
		//Validate the parameters
		boolean validVehiclePromotionType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validVehiclePromotionType) {
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
		ModelPaginator vehiclePromotionTypes = null;
		if (filter.isValidFilter()) {			
			vehiclePromotionTypes = new ModelPaginator(ER_Vehicle_Promotion_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			vehiclePromotionTypes = new ModelPaginator(ER_Vehicle_Promotion_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Vehicle_Promotion_Type.class);
		
		vehiclePromotionTypes.orderBy("name ASC");
		vehiclePromotionTypes.setPageNumber(page);
		vehiclePromotionTypes.setPageSize(10);
		renderArgs.put("vehiclePromotionTypes", vehiclePromotionTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * VehiclePromotionTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void vehiclePromotionTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add vehiclePromotionTypes to the renderArgs
    	filterVehiclePromotionTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("vehiclePromotionType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchVehiclePromotionTypes(String vehiclePromotionType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		vehiclePromotionTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",vehiclePromotionType);
    	}
    	vehiclePromotionTypesList(vehiclePromotionType, true);
    }

    /*
     * *****************************************************************
     * Edit VehiclePromotionType
     * *****************************************************************
     */
    public static void editVehiclePromotionType(Long id) {
    	
    	if (id!=null) {
    		ER_Vehicle_Promotion_Type model = ER_Vehicle_Promotion_Type.findById(id);
    		renderArgs.put("vehiclePromotionType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit VehiclePromotionType
     * *****************************************************************
     */
    public static void saveVehiclePromotionType(@Valid ER_Vehicle_Promotion_Type vehiclePromotionType, Long vehiclePromotionTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("vehiclePromotionType.create.fielderrors"));
    		validation.keep();
    		editVehiclePromotionType(vehiclePromotionType.id);
    	} else {
    		
    		if( vehiclePromotionTypeId == null ) {
    			vehiclePromotionType.save();
    			flash.success(Messages.get("vehiclePromotionType.create.success",1));
    		} else {
    			ER_Vehicle_Promotion_Type currentVehiclePromotionType = ER_Vehicle_Promotion_Type.findById(vehiclePromotionTypeId);
    			currentVehiclePromotionType.name = vehiclePromotionType.name;
    			currentVehiclePromotionType.active = vehiclePromotionType.active;
    			currentVehiclePromotionType.save();
    			flash.success(Messages.get("vehiclePromotionType.update.success"));
    		}
    		
    	}
    	
    	vehiclePromotionTypesList(vehiclePromotionType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete VehiclePromotionType
     * *****************************************************************
     */
    public static void deleteVehiclePromotionType(Long vehiclePromotionTypeId) {
    	flash.clear();
    	
    	ER_Vehicle_Promotion_Type vehiclePromotionType = ER_Vehicle_Promotion_Type.findById(vehiclePromotionTypeId);
    	if(vehiclePromotionType.id!=null) {
    		vehiclePromotionType.delete();
        	flash.success(Messages.get("vehiclePromotionType.delete.success"));
    	}else {
    		flash.error(Messages.get("vehiclePromotionType.delete.error"));
    	}
    	
    	vehiclePromotionTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewVehiclePromotionTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Promociones.xls");
    	renderTemplate("Reports/VehiclePromotionTypes.xls");
    	
    }
    
    public static void loadVehiclePromotionTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Vehicle_Promotion_Type res = ER_Vehicle_Promotion_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Vehicle_Promotion_Type newVehiclePromotionType = new ER_Vehicle_Promotion_Type();
    					newVehiclePromotionType.transferCode = sheet.getCell(1,row).getContents();
                		newVehiclePromotionType.name = sheet.getCell(2,row).getContents();
                		newVehiclePromotionType.description = sheet.getCell(3,row).getContents();
                		newVehiclePromotionType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newVehiclePromotionType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("vehiclePromotionType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("vehiclePromotionType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("vehiclePromotionType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newVehiclePromotionTypeFromExcel();
    	
    }
    
    public static void newVehiclePromotionTypeFromExcel() {
    	render();
    }

}
