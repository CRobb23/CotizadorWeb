package controllers;

import java.io.File;
import java.math.BigDecimal;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Deductible;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminDeductibles extends AdminBaseController{

	private static void filterDeductibles(String name) {
		
		//validate the parameters
		boolean validDeductible = GeneralMethods.validateParameter(name);
		
		//Create a new Filter and write the query for each parameter
		Filter filter = new Filter();
		
		if(validDeductible) {
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
		ModelPaginator deductibles = null;
		if(filter.isValidFilter()) {
			deductibles = new ModelPaginator(ER_Deductible.class,filter.getQuery(),filter.getParametersArray());
		}else {
			deductibles = new ModelPaginator(ER_Deductible.class);
		}
		
		deductibles.orderBy("name ASC");
		deductibles.setPageSize(10);
		deductibles.setPageNumber(page);
		
		renderArgs.put("deductibles", deductibles);
	}
	
	
	public static void deductiblesList(String name, boolean intern) {
		
		//flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
		
    	//filter by name
		filterDeductibles(name);
		
		//if it was a search
    	if(name!=null) {
    		renderArgs.put("deductible",name);
    	}
		
		render();
	}
	
	public static void searchDeductibles(String deductible,String all,boolean intern, boolean internalSearch) {
		
		flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		deductiblesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",deductible);
    	}
    	deductiblesList(deductible, true);
		
	}
	
	/*
     * *****************************************************************
     * Edit Deductible
     * *****************************************************************
     */
    public static void editDeductible(Long id) {
    	
    	if (id!=null) {
    		ER_Deductible model = ER_Deductible.findById(id);
    		renderArgs.put("deductible", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Save Deductible
     * *****************************************************************
     */
    public static void saveDeductible(@Valid ER_Deductible deductible, Long deductibleId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("deductible.create.fielderrors"));
    		validation.keep();
    		editDeductible(deductible.id);
    	} else {
    		
    		if( deductibleId == null ) {
    			deductible.save();
    			flash.success(Messages.get("deductible.form.create.success",1));
    		} else {
    			ER_Deductible currentDeductible = ER_Deductible.findById(deductibleId);
    			currentDeductible.name = deductible.name;
    			currentDeductible.cost = deductible.cost;
    			currentDeductible.active = deductible.active;
    			currentDeductible.save();
    			flash.success(Messages.get("deductible.update.success"));
    		}
    		
    	}
    	
    	deductiblesList(deductible.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Deductible
     * *****************************************************************
     */
    public static void deleteDeductible(Long rateId) {
    	flash.clear();
    	
    	ER_Deductible deductible = ER_Deductible.findById(rateId);
    	if(deductible.id!=null) {
    		deductible.delete();
        	flash.success(Messages.get("deductible.delete.success"));
    	}else {
    		flash.error(Messages.get("deductible.delete.error"));
    	}
    	
    	deductiblesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewDeductibleTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Deducibles.xls");
    	renderTemplate("Reports/Deductibles.xls");
    	
    }
    
    public static void loadDeductibleFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Deductible search = ER_Deductible.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				
    				if(search==null) {
    					ER_Deductible newDeductible = new ER_Deductible();
        				newDeductible.name = sheet.getCell(2,row).getContents();
        				newDeductible.cost = new BigDecimal(sheet.getCell(3,row).getContents());
        				newDeductible.transferCode = sheet.getCell(1,row).getContents();
        				newDeductible.active = "1".equals(sheet.getCell(4,row).getContents());
                		
        				newDeductible.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("deductible.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("deductible.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("deductible.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("deductible.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newDeductibleFromExcel();
    	
    }
    
    public static void newDeductibleFromExcel() {
    	render();
    }
	
}
