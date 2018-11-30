package controllers;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Portfolio_Type;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

import java.io.File;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminPortfolioTypes extends AdminBaseController {

	private static void filterPortfolioTypes(String name) {
		
		//Validate the parameters
		boolean validPortfolioType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validPortfolioType) {
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
		ModelPaginator portfolioTypes = null;
		if (filter.isValidFilter()) {			
			portfolioTypes = new ModelPaginator(ER_Portfolio_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			portfolioTypes = new ModelPaginator(ER_Portfolio_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_PortfolioType.class);
		
		portfolioTypes.orderBy("name ASC");
		portfolioTypes.setPageNumber(page);
		portfolioTypes.setPageSize(10);
		renderArgs.put("portfolioTypes", portfolioTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * PortfolioTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void portfolioTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add portfolioTypes to the renderArgs
    	filterPortfolioTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("portfolioType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchPortfolioTypes(String portfolioType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		portfolioTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",portfolioType);
    	}
    	portfolioTypesList(portfolioType, true);
    }

    /*
     * *****************************************************************
     * Edit PortfolioType
     * *****************************************************************
     */
    public static void editPortfolioType(Long id) {
    	
    	if (id!=null) {
    		ER_Portfolio_Type model = ER_Portfolio_Type.findById(id);
    		renderArgs.put("portfolioType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit PortfolioType
     * *****************************************************************
     */
    public static void savePortfolioType(@Valid ER_Portfolio_Type portfolioType, Long portfolioTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("portfolioType.create.fielderrors"));
    		validation.keep();
    		editPortfolioType(portfolioType.id);
    	} else {
    		
    		if( portfolioTypeId == null ) {
    			portfolioType.save();
    			flash.success(Messages.get("portfolioType.create.success",1));
    		} else {
    			ER_Portfolio_Type currentPortfolioType = ER_Portfolio_Type.findById(portfolioTypeId);
    			currentPortfolioType.name = portfolioType.name;
    			currentPortfolioType.active = portfolioType.active;
    			currentPortfolioType.save();
    			flash.success(Messages.get("portfolioType.update.success"));
    		}
    		
    	}
    	
    	portfolioTypesList(portfolioType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete PortfolioType
     * *****************************************************************
     */
    public static void deletePortfolioType(Long portfolioTypeId) {
    	flash.clear();
    	
    	ER_Portfolio_Type portfolioType = ER_Portfolio_Type.findById(portfolioTypeId);
    	if(portfolioType.id!=null) {
    		portfolioType.delete();
        	flash.success(Messages.get("portfolioType.delete.success"));
    	}else {
    		flash.error(Messages.get("portfolioType.delete.error"));
    	}
    	
    	portfolioTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewPortfolioTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Cuentas.xls");
    	renderTemplate("Reports/PortfolioTypes.xls");
    	
    }
    
    public static void loadPortfolioTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Portfolio_Type res = ER_Portfolio_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Portfolio_Type newPortfolioType = new ER_Portfolio_Type();
    					newPortfolioType.transferCode = sheet.getCell(1,row).getContents();
                		newPortfolioType.name = sheet.getCell(2,row).getContents();
                		newPortfolioType.description = sheet.getCell(3,row).getContents();
                		newPortfolioType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newPortfolioType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("portfolioType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("portfolioType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("portfolioType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newPortfolioTypeFromExcel();
    	
    }
    
    public static void newPortfolioTypeFromExcel() {
    	render();
    }

}
