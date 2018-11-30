package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Sex;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminSex extends AdminBaseController {

	private static void filterSex(String name) {
		
		//Validate the parameters
		boolean validSex = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validSex) {
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
		ModelPaginator sexList = null;
		if (filter.isValidFilter()) {			
			sexList = new ModelPaginator(ER_Sex.class, filter.getQuery(), filter.getParametersArray());
		} else {
			sexList = new ModelPaginator(ER_Sex.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Sex.class);
		
		sexList.orderBy("name ASC");
		sexList.setPageNumber(page);
		sexList.setPageSize(10);
		renderArgs.put("sexList", sexList);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Sex list
	 * ************************************************************************************************************************
	 */
	
    public static void sexList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add nationalities to the renderArgs
    	filterSex(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("sex",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchSex(String sex, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		sexList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",sex);
    	}
    	sexList(sex, true);
    }

    /*
     * *****************************************************************
     * Edit Sex
     * *****************************************************************
     */
    public static void editSex(Long id) {
    	
    	if (id!=null) {
    		ER_Sex model = ER_Sex.findById(id);
    		renderArgs.put("sex", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Sex
     * *****************************************************************
     */
    public static void saveSex(@Valid ER_Sex sex, Long sexId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("sex.create.fielderrors"));
    		validation.keep();
    		editSex(sex.id);
    	} else {
    		
    		if( sexId == null ) {
    			sex.save();
    			flash.success(Messages.get("sex.create.success",1));
    		} else {
    			ER_Sex currentSex = ER_Sex.findById(sexId);
    			currentSex.name = sex.name;
    			currentSex.active = sex.active;
    			currentSex.save();
    			flash.success(Messages.get("sex.update.success"));
    		}
    		
    	}
    	
    	sexList(sex.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Sex
     * *****************************************************************
     */
    public static void deleteSex(Long sexId) {
    	flash.clear();
    	
    	ER_Sex sex = ER_Sex.findById(sexId);
    	if(sex.id!=null) {
    		sex.delete();
        	flash.success(Messages.get("sex.delete.success"));
    	}else {
    		flash.error(Messages.get("sex.delete.error"));
    	}
    	
    	sexList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewSexTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Sexo.xls");
    	renderTemplate("Reports/Sex.xls");
    	
    }
    
    public static void loadSexFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Sex res = ER_Sex.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Sex newSex = new ER_Sex();
    					newSex.transferCode = sheet.getCell(1,row).getContents();
                		newSex.name = sheet.getCell(2,row).getContents();
                		newSex.description = sheet.getCell(3,row).getContents();
                		newSex.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newSex.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("sex.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("sex.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("sex.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newSexFromExcel();
    	
    }
    
    public static void newSexFromExcel() {
    	render();
    }

}
