package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Society_Type;
import models.ER_Society_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminSocietyTypes extends AdminBaseController {

	private static void filterSocietyTypes(String name) {
		
		//Validate the parameters
		boolean validSocietyType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validSocietyType) {
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
		ModelPaginator societyTypes = null;
		if (filter.isValidFilter()) {			
			societyTypes = new ModelPaginator(ER_Society_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			societyTypes = new ModelPaginator(ER_Society_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Society_Type.class);
		
		societyTypes.orderBy("name ASC");
		societyTypes.setPageNumber(page);
		societyTypes.setPageSize(10);
		renderArgs.put("societyTypes", societyTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * SocietyTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void societyTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add societyTypes to the renderArgs
    	filterSocietyTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("societyType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchSocietyTypes(String societyType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		societyTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",societyType);
    	}
    	societyTypesList(societyType, true);
    }

    /*
     * *****************************************************************
     * Edit SocietyType
     * *****************************************************************
     */
    public static void editSocietyType(Long id) {
    	
    	if (id!=null) {
    		ER_Society_Type model = ER_Society_Type.findById(id);
    		renderArgs.put("societyType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit SocietyType
     * *****************************************************************
     */
    public static void saveSocietyType(@Valid ER_Society_Type societyType, Long societyTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("societyType.create.fielderrors"));
    		validation.keep();
    		editSocietyType(societyType.id);
    	} else {
    		
    		if( societyTypeId == null ) {
    			societyType.save();
    			flash.success(Messages.get("societyType.create.success",1));
    		} else {
    			ER_Society_Type currentSocietyType = ER_Society_Type.findById(societyTypeId);
    			currentSocietyType.name = societyType.name;
    			currentSocietyType.active = societyType.active;
    			currentSocietyType.save();
    			flash.success(Messages.get("societyType.update.success"));
    		}
    		
    	}
    	
    	societyTypesList(societyType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete SocietyType
     * *****************************************************************
     */
    public static void deleteSocietyType(Long societyTypeId) {
    	flash.clear();
    	
    	ER_Society_Type societyType = ER_Society_Type.findById(societyTypeId);
    	if(societyType.id!=null) {
    		societyType.delete();
        	flash.success(Messages.get("societyType.delete.success"));
    	}else {
    		flash.error(Messages.get("societyType.delete.error"));
    	}
    	
    	societyTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewSocietyTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Sociedad.xls");
    	renderTemplate("Reports/SocietyTypes.xls");
    	
    }
    
    public static void loadSocietyTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Society_Type res = ER_Society_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Society_Type newSocietyType = new ER_Society_Type();
    					newSocietyType.transferCode = sheet.getCell(1,row).getContents();
                		newSocietyType.name = sheet.getCell(2,row).getContents();
                		newSocietyType.description = sheet.getCell(3,row).getContents();
                		newSocietyType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newSocietyType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("societyType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("societyType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("societyType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newSocietyTypeFromExcel();
    	
    }
    
    public static void newSocietyTypeFromExcel() {
    	render();
    }

}
