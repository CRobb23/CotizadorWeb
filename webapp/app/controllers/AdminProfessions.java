package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminProfessions extends AdminBaseController {

	private static void filterProfessions(String name) {
		
		//Validate the parameters
		boolean validProfession = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validProfession) {
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
		ModelPaginator professions = null;
		if (filter.isValidFilter()) {			
			professions = new ModelPaginator(ER_Profession.class, filter.getQuery(), filter.getParametersArray());
		} else {
			professions = new ModelPaginator(ER_Profession.class);
		}
		
		//professions = new ModelPaginator(ER_Profession.class);
		
		professions.orderBy("name ASC");
		professions.setPageNumber(page);
		professions.setPageSize(10);
		renderArgs.put("professions", professions);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Professions list
	 * ************************************************************************************************************************
	 */
	
    public static void professionsList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add professions to the renderArgs
    	filterProfessions(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("profession",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchProfessions(String profession, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		professionsList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",profession);
    	}
    	professionsList(profession, true);
    }

    /*
     * *****************************************************************
     * Edit Profession
     * *****************************************************************
     */
    public static void editProfession(Long id) {
    	
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
    public static void saveProfession(@Valid ER_Profession profession, Long professionId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("profession.create.fielderrors"));
    		validation.keep();
    		editProfession(profession.id);
    	} else {
    		
    		if( professionId == null ) {
    			profession.save();
    			flash.success(Messages.get("profession.create.success",1));
    		} else {
    			ER_Profession currentProfession = ER_Profession.findById(professionId);
    			currentProfession.name = profession.name;
    			currentProfession.active = profession.active;
    			currentProfession.save();
    			flash.success(Messages.get("profession.update.success"));
    		}
    		
    	}
    	
    	professionsList(profession.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Profession
     * *****************************************************************
     */
    public static void deleteProfession(Long professionId) {
    	flash.clear();
    	
    	ER_Profession profession = ER_Profession.findById(professionId);
    	if(profession.id!=null) {
    		profession.delete();
        	flash.success(Messages.get("profession.delete.success"));
    	}else {
    		flash.error(Messages.get("profession.delete.error"));
    	}
    	
    	professionsList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewProfessionTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Profesiones.xls");
    	renderTemplate("Reports/Professions.xls");
    	
    }
    
    public static void loadProfessionFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Profession res = ER_Profession.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Profession newProfession = new ER_Profession();
                		newProfession.name = sheet.getCell(2,row).getContents();
                		newProfession.transferCode = sheet.getCell(1,row).getContents();
                		newProfession.active = "1".equals(sheet.getCell(3,row).getContents());
                		
                		newProfession.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("product.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("profession.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("profession.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newProfessionFromExcel();
    	
    }
    
    public static void newProfessionFromExcel() {
    	render();
    }

}
