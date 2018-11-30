package controllers;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Average_Value;
import models.ws.QueryAverageValueVehicleRequest;
import models.ws.QueryAverageValueVehicleResponse;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;
import service.PolicyService;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminAverageValues extends AdminBaseController{

	@Inject
	static PolicyService policyService;
	
	private static void filterAverageValues(String name) {
		
		//validate the parameters
		boolean validAverageValue = GeneralMethods.validateParameter(name);
		
		//Create a new Filter and write the query for each parameter
		Filter filter = new Filter();
		
		if(validAverageValue) {
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
		ModelPaginator averageValues = null;
		if(filter.isValidFilter()) {
			averageValues = new ModelPaginator(ER_Average_Value.class,filter.getQuery(),filter.getParametersArray());
		}else {
			averageValues = new ModelPaginator(ER_Average_Value.class);
		}
		
		averageValues.orderBy("name ASC");
		averageValues.setPageSize(10);
		averageValues.setPageNumber(page);
		
		renderArgs.put("averageValues", averageValues);
	}
	
	
	public static void averageValuesList(String name, boolean intern) {
		
		//flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
		
    	//filter by name
		filterAverageValues(name);
		
		//if it was a search
    	if(name!=null) {
    		renderArgs.put("averageValue",name);
    	}
		
		render();
	}
	
	public static void searchAverageValues(String averageValue,String all,boolean intern, boolean internalSearch) {
		
		flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		averageValuesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",averageValue);
    	}
    	averageValuesList(averageValue, true);
		
	}
	
	/*
     * *****************************************************************
     * Edit AverageValue
     * *****************************************************************
     */
    public static void editAverageValue(Long id) {
    	
    	if (id!=null) {
    		ER_Average_Value model = ER_Average_Value.findById(id);
    		renderArgs.put("averageValue", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Save AverageValue
     * *****************************************************************
     */
    public static void saveAverageValue(@Valid ER_Average_Value averageValue, Long averageValueId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("averageValue.create.fielderrors"));
    		validation.keep();
    		editAverageValue(averageValue.id);
    	} else {
    		
    		if( averageValueId == null ) {
    			averageValue.save();
    			flash.success(Messages.get("averageValue.form.create.success"));
    		} else {
    			ER_Average_Value currentAverageValue = ER_Average_Value.findById(averageValueId);
    			currentAverageValue.name = averageValue.name;
    			currentAverageValue.value = averageValue.value;
    			currentAverageValue.active = averageValue.active;
    			currentAverageValue.save();
    			flash.success(Messages.get("averageValue.update.success"));
    		}
    		
    	}
    	
    	averageValuesList(averageValue.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete AverageValue
     * *****************************************************************
     */
    public static void deleteAverageValue(Long rateId) {
    	flash.clear();
    	
    	ER_Average_Value averageValue = ER_Average_Value.findById(rateId);
    	if(averageValue.id!=null) {
    		averageValue.delete();
        	flash.success(Messages.get("averageValue.delete.success"));
    	}else {
    		flash.error(Messages.get("averageValue.delete.error"));
    	}
    	
    	averageValuesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewAverageValueTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Valor-Promedio.xls");
    	renderTemplate("Reports/AverageValues.xls");
    	
    }
    
    public static void loadAverageValueFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				//ER_Average_Value search = ER_Average_Value.find("transfer_code = ?",sheet.getCell(1,row).getContents()).first();
    				//if(search==null) {
    					ER_Average_Value newAverageValue = new ER_Average_Value();
        				newAverageValue.name = sheet.getCell(2,row).getContents();
        				newAverageValue.value = new BigDecimal(sheet.getCell(3,row).getContents());
        				if(!sheet.getCell(1,row).getContents().trim().equals("")) {
        					newAverageValue.transferCode = sheet.getCell(1,row).getContents();
        				}
        				newAverageValue.active = "1".equals(sheet.getCell(4,row).getContents());
                		
        				newAverageValue.save();
                		
                		objectsCreated++;
    				//}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("averageValue.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("averageValue.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("averageValue.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("averageValue.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newAverageValueFromExcel();
    	
    }
    
    public static void newAverageValueFromExcel() {
    	render();
    }
    
}
