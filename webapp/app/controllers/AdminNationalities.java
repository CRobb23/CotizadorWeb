package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Nationality;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminNationalities extends AdminBaseController {

	private static void filterNationalities(String name) {
		
		//Validate the parameters
		boolean validNationality = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validNationality) {
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
		ModelPaginator nationalities = null;
		if (filter.isValidFilter()) {			
			nationalities = new ModelPaginator(ER_Nationality.class, filter.getQuery(), filter.getParametersArray());
		} else {
			nationalities = new ModelPaginator(ER_Nationality.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Nationality.class);
		
		nationalities.orderBy("name ASC");
		nationalities.setPageNumber(page);
		nationalities.setPageSize(10);
		renderArgs.put("nationalities", nationalities);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Nationalities list
	 * ************************************************************************************************************************
	 */
	
    public static void nationalitiesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add nationalities to the renderArgs
    	filterNationalities(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("nationality",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchNationalities(String nationality, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		nationalitiesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",nationality);
    	}
    	nationalitiesList(nationality, true);
    }

    /*
     * *****************************************************************
     * Edit Nationality
     * *****************************************************************
     */
    public static void editNationality(Long id) {
    	
    	if (id!=null) {
    		ER_Nationality model = ER_Nationality.findById(id);
    		renderArgs.put("nationality", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Nationality
     * *****************************************************************
     */
    public static void saveNationality(@Valid ER_Nationality nationality, Long nationalityId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("nationality.create.fielderrors"));
    		validation.keep();
    		editNationality(nationality.id);
    	} else {
    		
    		if( nationalityId == null ) {
    			nationality.save();
    			flash.success(Messages.get("nationality.create.success",1));
    		} else {
    			ER_Nationality currentNationality = ER_Nationality.findById(nationalityId);
    			currentNationality.name = nationality.name;
    			currentNationality.active = nationality.active;
    			currentNationality.save();
    			flash.success(Messages.get("nationality.update.success"));
    		}
    		
    	}
    	
    	nationalitiesList(nationality.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Nationality
     * *****************************************************************
     */
    public static void deleteNationality(Long nationalityId) {
    	flash.clear();
    	
    	ER_Nationality nationality = ER_Nationality.findById(nationalityId);
    	if(nationality.id!=null) {
    		nationality.delete();
        	flash.success(Messages.get("nationality.delete.success"));
    	}else {
    		flash.error(Messages.get("nationality.delete.error"));
    	}
    	
    	nationalitiesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewNationalityTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Nacionalidades.xls");
    	renderTemplate("Reports/Nationalities.xls");
    	
    }
    
    public static void loadNationalityFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Nationality res = ER_Nationality.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Nationality newNationality = new ER_Nationality();
    					newNationality.transferCode = sheet.getCell(1,row).getContents();
                		newNationality.name = sheet.getCell(2,row).getContents();
                		newNationality.description = sheet.getCell(3,row).getContents();
                		newNationality.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newNationality.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("nationality.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("nationality.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("nationality.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newNationalityFromExcel();
    	
    }
    
    public static void newNationalityFromExcel() {
    	render();
    }

}
