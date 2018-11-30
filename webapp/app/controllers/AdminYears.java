package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Rate;
import models.ER_Year;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminYears extends AdminBaseController{
	
	private static void filterYears(String year) {
		
		//validate the parameters
		boolean validYear = GeneralMethods.validateParameter(year);
		
		//Create a new Filter and write the query for each parameter
		Filter filter = new Filter();
		
		if(validYear) {
			filter.addQuery("year_number like ?", "%"+year+"%");
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
		ModelPaginator years = null;
		if(filter.isValidFilter()) {
			years = new ModelPaginator(ER_Year.class,filter.getQuery(),filter.getParametersArray());
		}else {
			years = new ModelPaginator(ER_Year.class);
		}
		
		years.orderBy("year_number ASC");
		years.setPageSize(10);
		years.setPageNumber(page);
		
		renderArgs.put("listYears", years);
	}
	
	
	public static void yearsList(String year, boolean intern) {
		
		flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("year");
    	}
		
    	//filter by name
		filterYears(year);
		
		//if it was a search
    	if(year!=null) {
    		renderArgs.put("year",year);
    	}
		
		render();
	}
	
	public static void searchYears(String year,String all,boolean intern, boolean internalSearch) {
		
		flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("year");
    		
    		yearsList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("year",year);
    	}
    	
    	yearsList(year, true);
		
	}
	
	/*
     * *****************************************************************
     * Edit Year
     * *****************************************************************
     */
    public static void editYear(Long id) {
    	
    	if (id!=null) {
    		ER_Year model = ER_Year.findById(id);
    		renderArgs.put("year", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Save Year
     * *****************************************************************
     */
    public static void saveYear(@Valid ER_Year year, Long yearId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("year.create.fielderrors"));
    		validation.keep();
    		editYear(year.id);
    	} else {
    		
    		if( yearId == null ) {
    			year.save();
    			flash.success(Messages.get("year.create.success",1));
    		} else {
    			ER_Year currentYear = ER_Year.findById(yearId);
    			currentYear.year = year.year;
    			currentYear.active = year.active;
    			currentYear.save();
    			flash.success(Messages.get("year.update.success"));
    		}
    		
    	}
    	
    	yearsList(year.year, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Year
     * *****************************************************************
     */
    public static void deleteYear(Long yearId) {
    	flash.clear();
    	
    	ER_Year year = ER_Year.findById(yearId);
    	if(year.id!=null) {
    		year.delete();
        	flash.success(Messages.get("year.delete.success"));
    	}else {
    		flash.error(Messages.get("year.delete.error"));
    	}
    	
    	yearsList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewYearTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Años.xls");
    	renderTemplate("Reports/Years.xls");
    	
    }
    
    public static void loadYearFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Year search = ER_Year.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(search==null) {
    					ER_Year newRate = new ER_Year();
        				newRate.year = sheet.getCell(2,row).getContents();
        				newRate.transferCode = sheet.getCell(1,row).getContents();
        				newRate.active = "1".equals(sheet.getCell(3,row).getContents());
                		
        				newRate.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("year.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("year.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("year.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("year.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newYearFromExcel();
    	
    }
    
    public static void newYearFromExcel() {
    	render();
    }
}
