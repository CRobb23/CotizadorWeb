package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_License_Type;
import models.ER_License_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminLicenseTypes extends AdminBaseController {

	private static void filterLicenseTypes(String name) {
		
		//Validate the parameters
		boolean validLicenseType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validLicenseType) {
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
		ModelPaginator licenseTypes = null;
		if (filter.isValidFilter()) {			
			licenseTypes = new ModelPaginator(ER_License_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			licenseTypes = new ModelPaginator(ER_License_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_License_Type.class);
		
		licenseTypes.orderBy("name ASC");
		licenseTypes.setPageNumber(page);
		licenseTypes.setPageSize(10);
		renderArgs.put("licenseTypes", licenseTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * LicenseTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void licenseTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add licenseTypes to the renderArgs
    	filterLicenseTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("licenseType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchLicenseTypes(String licenseType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		licenseTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",licenseType);
    	}
    	licenseTypesList(licenseType, true);
    }

    /*
     * *****************************************************************
     * Edit LicenseType
     * *****************************************************************
     */
    public static void editLicenseType(Long id) {
    	
    	if (id!=null) {
    		ER_License_Type model = ER_License_Type.findById(id);
    		renderArgs.put("licenseType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit LicenseType
     * *****************************************************************
     */
    public static void saveLicenseType(@Valid ER_License_Type licenseType, Long licenseTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("licenseType.create.fielderrors"));
    		validation.keep();
    		editLicenseType(licenseType.id);
    	} else {
    		
    		if( licenseTypeId == null ) {
    			licenseType.save();
    			flash.success(Messages.get("licenseType.create.success",1));
    		} else {
    			ER_License_Type currentLicenseType = ER_License_Type.findById(licenseTypeId);
    			currentLicenseType.name = licenseType.name;
    			currentLicenseType.active = licenseType.active;
    			currentLicenseType.save();
    			flash.success(Messages.get("licenseType.update.success"));
    		}
    		
    	}
    	
    	licenseTypesList(licenseType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete LicenseType
     * *****************************************************************
     */
    public static void deleteLicenseType(Long licenseTypeId) {
    	flash.clear();
    	
    	ER_License_Type licenseType = ER_License_Type.findById(licenseTypeId);
    	if(licenseType.id!=null) {
    		licenseType.delete();
        	flash.success(Messages.get("licenseType.delete.success"));
    	}else {
    		flash.error(Messages.get("licenseType.delete.error"));
    	}
    	
    	licenseTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewLicenseTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Licencia.xls");
    	renderTemplate("Reports/LicenseTypes.xls");
    	
    }
    
    public static void loadLicenseTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_License_Type res = ER_License_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_License_Type newLicenseType = new ER_License_Type();
    					newLicenseType.transferCode = sheet.getCell(1,row).getContents();
                		newLicenseType.name = sheet.getCell(2,row).getContents();
                		newLicenseType.description = sheet.getCell(3,row).getContents();
                		newLicenseType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newLicenseType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("licenseType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("licenseType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("licenseType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newLicenseTypeFromExcel();
    	
    }
    
    public static void newLicenseTypeFromExcel() {
    	render();
    }

}
