package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Rate;
import models.ER_Uninsurable;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminUninsurables extends AdminBaseController{

	private static void filterUninsurables(String name) {
		
		//validate the parameters
		boolean validUninsurable = GeneralMethods.validateParameter(name);
		
		//Create a new Filter and write the query for each parameter
		Filter filter = new Filter();
		
		if(validUninsurable) {
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
		ModelPaginator uninsurables = null;
		if(filter.isValidFilter()) {
			uninsurables = new ModelPaginator(ER_Uninsurable.class,filter.getQuery(),filter.getParametersArray());
		}else {
			uninsurables = new ModelPaginator(ER_Uninsurable.class);
		}
		
		uninsurables.orderBy("name ASC");
		uninsurables.setPageSize(10);
		uninsurables.setPageNumber(page);
		
		renderArgs.put("uninsurables", uninsurables);
	}
	
	
	public static void uninsurablesList(String name, boolean intern) {
		
		flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
		
    	//filter by name
		filterUninsurables(name);
		
		//if it was a search
    	if(name!=null) {
    		renderArgs.put("uninsurable",name);
    	}
		
		render();
	}
	
	public static void searchUninsurables(String uninsurable,String all,boolean intern, boolean internalSearch) {
		
		flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		uninsurablesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",uninsurable);
    	}
    	uninsurablesList(uninsurable, true);
		
	}
	
	/*
     * *****************************************************************
     * Edit Uninsurable
     * *****************************************************************
     */
    public static void editUninsurable(Long id) {
    	
    	if (id!=null) {
    		ER_Uninsurable model = ER_Uninsurable.findById(id);
    		renderArgs.put("uninsurable", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Save Uninsurable
     * *****************************************************************
     */
    public static void saveUninsurable(@Valid ER_Uninsurable uninsurable, Long uninsurableId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("uninsurable.create.fielderrors"));
    		validation.keep();
    		editUninsurable(uninsurable.id);
    	} else {
    		
    		if( uninsurableId == null ) {
    			uninsurable.save();
    			flash.success(Messages.get("uninsurable.create.success",1));
    		} else {
    			ER_Uninsurable currentUninsurable = ER_Uninsurable.findById(uninsurableId);
    			currentUninsurable.name = uninsurable.name;
    			currentUninsurable.active = uninsurable.active;
    			currentUninsurable.save();
    			flash.success(Messages.get("uninsurable.update.success"));
    		}
    		
    	}
    	
    	uninsurablesList(uninsurable.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Uninsurable
     * *****************************************************************
     */
    public static void deleteUninsurable(Long rateId) {
    	flash.clear();
    	
    	ER_Uninsurable uninsurable = ER_Uninsurable.findById(rateId);
    	if(uninsurable.id!=null) {
    		uninsurable.delete();
        	flash.success(Messages.get("uninsurable.delete.success"));
    	}else {
    		flash.error(Messages.get("uninsurable.delete.error"));
    	}
    	
    	uninsurablesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewUninsurableTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-No-Asegurables.xls");
    	renderTemplate("Reports/Uninsurables.xls");
    	
    }
    
    public static void loadUninsurableFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Uninsurable search =  ER_Uninsurable.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(search==null) {
    					ER_Uninsurable newUninsurable = new ER_Uninsurable();
        				newUninsurable.name = sheet.getCell(2,row).getContents();
        				newUninsurable.transferCode = sheet.getCell(1,row).getContents();
        				newUninsurable.active = "1".equals(sheet.getCell(3,row).getContents());
                		
        				newUninsurable.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("uninsurable.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("uninsurable.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("uninsurable.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("uninsurable.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newUninsurableFromExcel();
    	
    }
    
    public static void newUninsurableFromExcel() {
    	render();
    }
	
}
