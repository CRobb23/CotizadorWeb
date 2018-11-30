package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Profession;
import models.ER_Rate;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminRates extends AdminBaseController{

	
	private static void filterRates(String name) {
		
		//validate the parameters
		boolean validRate = GeneralMethods.validateParameter(name);
		
		//Create a new Filter and write the query for each parameter
		Filter filter = new Filter();
		
		if(validRate) {
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
		ModelPaginator rates = null;
		if(filter.isValidFilter()) {
			rates = new ModelPaginator(ER_Rate.class,filter.getQuery(),filter.getParametersArray());
		}else {
			rates = new ModelPaginator(ER_Rate.class);
		}
		
		rates.orderBy("name ASC");
		rates.setPageSize(10);
		rates.setPageNumber(page);
		
		renderArgs.put("rates", rates);
	}
	
	
	public static void ratesList(String name, boolean intern) {
		
		flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
		
    	//filter by name
		filterRates(name);
		
		//if it was a search
    	if(name!=null) {
    		renderArgs.put("rate",name);
    	}
		
		render();
	}
	
	public static void searchRates(String rate,String all,boolean intern, boolean internalSearch) {
		
		flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		ratesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",rate);
    	}
    	ratesList(rate, true);
		
	}
	
	/*
     * *****************************************************************
     * Edit Rate
     * *****************************************************************
     */
    public static void editRate(Long id) {
    	
    	if (id!=null) {
    		ER_Rate model = ER_Rate.findById(id);
    		renderArgs.put("rate", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Save Rate
     * *****************************************************************
     */
    public static void saveRate(@Valid ER_Rate rate, Long rateId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("rate.create.fielderrors"));
    		validation.keep();
    		editRate(rate.id);
    	} else {
    		
    		if( rateId == null ) {
    			rate.save();
    			flash.success(Messages.get("rate.create.success",1));
    		} else {
    			ER_Rate currentRate = ER_Rate.findById(rateId);
    			currentRate.name = rate.name;
    			currentRate.active = rate.active;
    			currentRate.save();
    			flash.success(Messages.get("rate.update.success"));
    		}
    		
    	}
    	
    	ratesList(rate.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Rate
     * *****************************************************************
     */
    public static void deleteRate(Long rateId) {
    	flash.clear();
    	
    	ER_Rate rate = ER_Rate.findById(rateId);
    	if(rate.id!=null) {
    		rate.delete();
        	flash.success(Messages.get("rate.delete.success"));
    	}else {
    		flash.error(Messages.get("rate.delete.error"));
    	}
    	
    	ratesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewRateTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tarifas.xls");
    	renderTemplate("Reports/Rates.xls");
    	
    }
    
    public static void loadRateFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Rate search = ER_Rate.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				
    				if(search==null) {
    					ER_Rate newRate = new ER_Rate();
        				newRate.name = sheet.getCell(2,row).getContents();
        				newRate.transferCode = sheet.getCell(1,row).getContents();
        				newRate.active = "1".equals(sheet.getCell(3,row).getContents());
                		
        				newRate.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("rate.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("rate.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("rate.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("rate.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newRateFromExcel();
    	
    }
    
    public static void newRateFromExcel() {
    	render();
    }
	
}
