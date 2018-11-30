package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Account_Line;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminAccountLines extends AdminBaseController {

	private static void filterAccountLines(String name) {
		
		//Validate the parameters
		boolean validAccountLine = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validAccountLine) {
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
		ModelPaginator accountLines = null;
		if (filter.isValidFilter()) {			
			accountLines = new ModelPaginator(ER_Account_Line.class, filter.getQuery(), filter.getParametersArray());
		} else {
			accountLines = new ModelPaginator(ER_Account_Line.class);
		}
		
		//Nationalties = new ModelPaginator(ER_AccountLine.class);
		
		accountLines.orderBy("name ASC");
		accountLines.setPageNumber(page);
		accountLines.setPageSize(10);
		renderArgs.put("accountLines", accountLines);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * AccountLines list
	 * ************************************************************************************************************************
	 */
	
    public static void accountLinesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add accountLines to the renderArgs
    	filterAccountLines(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("accountLine",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchAccountLines(String accountLine, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		accountLinesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",accountLine);
    	}
    	accountLinesList(accountLine, true);
    }

    /*
     * *****************************************************************
     * Edit AccountLine
     * *****************************************************************
     */
    public static void editAccountLine(Long id) {
    	
    	if (id!=null) {
    		ER_Account_Line model = ER_Account_Line.findById(id);
    		renderArgs.put("accountLine", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit AccountLine
     * *****************************************************************
     */
    public static void saveAccountLine(@Valid ER_Account_Line accountLine, Long accountLineId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("accountLine.create.fielderrors"));
    		validation.keep();
    		editAccountLine(accountLine.id);
    	} else {
    		
    		if( accountLineId == null ) {
    			accountLine.save();
    			flash.success(Messages.get("accountLine.create.success",1));
    		} else {
    			ER_Account_Line currentAccountLine = ER_Account_Line.findById(accountLineId);
    			currentAccountLine.name = accountLine.name;
    			currentAccountLine.active = accountLine.active;
    			currentAccountLine.save();
    			flash.success(Messages.get("accountLine.update.success"));
    		}
    		
    	}
    	
    	accountLinesList(accountLine.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete AccountLine
     * *****************************************************************
     */
    public static void deleteAccountLine(Long accountLineId) {
    	flash.clear();
    	
    	ER_Account_Line accountLine = ER_Account_Line.findById(accountLineId);
    	if(accountLine.id!=null) {
    		accountLine.delete();
        	flash.success(Messages.get("accountLine.delete.success"));
    	}else {
    		flash.error(Messages.get("accountLine.delete.error"));
    	}
    	
    	accountLinesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewAccountLineTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Nacionalidades.xls");
    	renderTemplate("Reports/AccountLines.xls");
    	
    }
    
    public static void loadAccountLineFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Account_Line res = ER_Account_Line.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Account_Line newAccountLine = new ER_Account_Line();
    					newAccountLine.transferCode = sheet.getCell(1,row).getContents();
                		newAccountLine.name = sheet.getCell(2,row).getContents();
                		newAccountLine.description = sheet.getCell(3,row).getContents();
                		newAccountLine.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newAccountLine.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("accountLine.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("accountLine.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("accountLine.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newAccountLineFromExcel();
    	
    }
    
    public static void newAccountLineFromExcel() {
    	render();
    }

}
