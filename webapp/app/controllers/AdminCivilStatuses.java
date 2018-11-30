package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Civil_Status;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminCivilStatuses extends AdminBaseController {

	private static void filterCivilStatuses(String name) {
		
		//Validate the parameters
		boolean validCivilStatus = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validCivilStatus) {
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
		ModelPaginator civilStatuses = null;
		if (filter.isValidFilter()) {			
			civilStatuses = new ModelPaginator(ER_Civil_Status.class, filter.getQuery(), filter.getParametersArray());
		} else {
			civilStatuses = new ModelPaginator(ER_Civil_Status.class);
		}
		
		//Nationalties = new ModelPaginator(ER_CivilStatus.class);
		
		civilStatuses.orderBy("name ASC");
		civilStatuses.setPageNumber(page);
		civilStatuses.setPageSize(10);
		renderArgs.put("civilStatuses", civilStatuses);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * CivilStatuses list
	 * ************************************************************************************************************************
	 */
	
    public static void civilStatusesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add civilStatuses to the renderArgs
    	filterCivilStatuses(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("civilStatus",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchCivilStatuses(String civilStatus, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		civilStatusesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",civilStatus);
    	}
    	civilStatusesList(civilStatus, true);
    }

    /*
     * *****************************************************************
     * Edit CivilStatus
     * *****************************************************************
     */
    public static void editCivilStatus(Long id) {
    	
    	if (id!=null) {
    		ER_Civil_Status model = ER_Civil_Status.findById(id);
    		renderArgs.put("civilStatus", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit CivilStatus
     * *****************************************************************
     */
    public static void saveCivilStatus(@Valid ER_Civil_Status civilStatus, Long civilStatusId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("civilStatus.create.fielderrors"));
    		validation.keep();
    		editCivilStatus(civilStatus.id);
    	} else {
    		
    		if( civilStatusId == null ) {
    			civilStatus.save();
    			flash.success(Messages.get("civilStatus.create.success",1));
    		} else {
    			ER_Civil_Status currentCivilStatus = ER_Civil_Status.findById(civilStatusId);
    			currentCivilStatus.name = civilStatus.name;
    			currentCivilStatus.active = civilStatus.active;
    			currentCivilStatus.save();
    			flash.success(Messages.get("civilStatus.update.success"));
    		}
    		
    	}
    	
    	civilStatusesList(civilStatus.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete CivilStatus
     * *****************************************************************
     */
    public static void deleteCivilStatus(Long civilStatusId) {
    	flash.clear();
    	
    	ER_Civil_Status civilStatus = ER_Civil_Status.findById(civilStatusId);
    	if(civilStatus.id!=null) {
    		civilStatus.delete();
        	flash.success(Messages.get("civilStatus.delete.success"));
    	}else {
    		flash.error(Messages.get("civilStatus.delete.error"));
    	}
    	
    	civilStatusesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewCivilStatusTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Estado-Civil.xls");
    	renderTemplate("Reports/CivilStatuses.xls");
    	
    }
    
    public static void loadCivilStatusFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Civil_Status res = ER_Civil_Status.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Civil_Status newCivilStatus = new ER_Civil_Status();
    					newCivilStatus.transferCode = sheet.getCell(1,row).getContents();
                		newCivilStatus.name = sheet.getCell(2,row).getContents();
                		newCivilStatus.description = sheet.getCell(3,row).getContents();
                		newCivilStatus.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newCivilStatus.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("civilStatus.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("civilStatus.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("civilStatus.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newCivilStatusFromExcel();
    	
    }
    
    public static void newCivilStatusFromExcel() {
    	render();
    }

}
