package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Charge_Type;
import models.ER_Charge_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminChargeTypes extends AdminBaseController {

	private static void filterChargeTypes(String name) {
		
		//Validate the parameters
		boolean validChargeType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validChargeType) {
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
		ModelPaginator chargeTypes = null;
		if (filter.isValidFilter()) {			
			chargeTypes = new ModelPaginator(ER_Charge_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			chargeTypes = new ModelPaginator(ER_Charge_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Charge_Type.class);
		
		chargeTypes.orderBy("name ASC");
		chargeTypes.setPageNumber(page);
		chargeTypes.setPageSize(10);
		renderArgs.put("chargeTypes", chargeTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * ChargeTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void chargeTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add chargeTypes to the renderArgs
    	filterChargeTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("chargeType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchChargeTypes(String chargeType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		chargeTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",chargeType);
    	}
    	chargeTypesList(chargeType, true);
    }

    /*
     * *****************************************************************
     * Edit ChargeType
     * *****************************************************************
     */
    public static void editChargeType(Long id) {
    	
    	if (id!=null) {
    		ER_Charge_Type model = ER_Charge_Type.findById(id);
    		renderArgs.put("chargeType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit ChargeType
     * *****************************************************************
     */
    public static void saveChargeType(@Valid ER_Charge_Type chargeType, Long chargeTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("chargeType.create.fielderrors"));
    		validation.keep();
    		editChargeType(chargeType.id);
    	} else {
    		
    		if( chargeTypeId == null ) {
    			chargeType.save();
    			flash.success(Messages.get("chargeType.create.success",1));
    		} else {
    			ER_Charge_Type currentChargeType = ER_Charge_Type.findById(chargeTypeId);
    			currentChargeType.name = chargeType.name;
    			currentChargeType.active = chargeType.active;
    			currentChargeType.save();
    			flash.success(Messages.get("chargeType.update.success"));
    		}
    		
    	}
    	
    	chargeTypesList(chargeType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete ChargeType
     * *****************************************************************
     */
    public static void deleteChargeType(Long chargeTypeId) {
    	flash.clear();
    	
    	ER_Charge_Type chargeType = ER_Charge_Type.findById(chargeTypeId);
    	if(chargeType.id!=null) {
    		chargeType.delete();
        	flash.success(Messages.get("chargeType.delete.success"));
    	}else {
    		flash.error(Messages.get("chargeType.delete.error"));
    	}
    	
    	chargeTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewChargeTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Método-Cobro.xls");
    	renderTemplate("Reports/ChargeTypes.xls");
    	
    }
    
    public static void loadChargeTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Charge_Type res = ER_Charge_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Charge_Type newChargeType = new ER_Charge_Type();
    					newChargeType.transferCode = sheet.getCell(1,row).getContents();
                		newChargeType.name = sheet.getCell(2,row).getContents();
                		newChargeType.description = sheet.getCell(3,row).getContents();
                		newChargeType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newChargeType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("chargeType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("chargeType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("chargeType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newChargeTypeFromExcel();
    	
    }
    
    public static void newChargeTypeFromExcel() {
    	render();
    }

}
