package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_WorkFlow_Movement_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminWorkFlowMovementTypes extends AdminBaseController {

	private static void filterWorkFlowMovementTypes(String name) {
		
		//Validate the parameters
		boolean validWorkFlowMovementType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validWorkFlowMovementType) {
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
		ModelPaginator workFlowMovementTypes = null;
		if (filter.isValidFilter()) {			
			workFlowMovementTypes = new ModelPaginator(ER_WorkFlow_Movement_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			workFlowMovementTypes = new ModelPaginator(ER_WorkFlow_Movement_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_WorkFlowMovementType.class);
		
		workFlowMovementTypes.orderBy("name ASC");
		workFlowMovementTypes.setPageNumber(page);
		workFlowMovementTypes.setPageSize(10);
		renderArgs.put("workFlowMovementTypes", workFlowMovementTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * WorkFlowMovementTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void workFlowMovementTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add workFlowMovementTypes to the renderArgs
    	filterWorkFlowMovementTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("workFlowMovementType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchWorkFlowMovementTypes(String workFlowMovementType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		workFlowMovementTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",workFlowMovementType);
    	}
    	workFlowMovementTypesList(workFlowMovementType, true);
    }

    /*
     * *****************************************************************
     * Edit WorkFlowMovementType
     * *****************************************************************
     */
    public static void editWorkFlowMovementType(Long id) {
    	
    	if (id!=null) {
    		ER_WorkFlow_Movement_Type model = ER_WorkFlow_Movement_Type.findById(id);
    		renderArgs.put("workFlowMovementType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit WorkFlowMovementType
     * *****************************************************************
     */
    public static void saveWorkFlowMovementType(@Valid ER_WorkFlow_Movement_Type workFlowMovementType, Long workFlowMovementTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("workFlowMovementType.create.fielderrors"));
    		validation.keep();
    		editWorkFlowMovementType(workFlowMovementType.id);
    	} else {
    		
    		if( workFlowMovementTypeId == null ) {
    			workFlowMovementType.save();
    			flash.success(Messages.get("workFlowMovementType.create.success",1));
    		} else {
    			ER_WorkFlow_Movement_Type currentWorkFlowMovementType = ER_WorkFlow_Movement_Type.findById(workFlowMovementTypeId);
    			currentWorkFlowMovementType.name = workFlowMovementType.name;
    			currentWorkFlowMovementType.active = workFlowMovementType.active;
    			currentWorkFlowMovementType.save();
    			flash.success(Messages.get("workFlowMovementType.update.success"));
    		}
    		
    	}
    	
    	workFlowMovementTypesList(workFlowMovementType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete WorkFlowMovementType
     * *****************************************************************
     */
    public static void deleteWorkFlowMovementType(Long workFlowMovementTypeId) {
    	flash.clear();
    	
    	ER_WorkFlow_Movement_Type workFlowMovementType = ER_WorkFlow_Movement_Type.findById(workFlowMovementTypeId);
    	if(workFlowMovementType.id!=null) {
    		workFlowMovementType.delete();
        	flash.success(Messages.get("workFlowMovementType.delete.success"));
    	}else {
    		flash.error(Messages.get("workFlowMovementType.delete.error"));
    	}
    	
    	workFlowMovementTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewWorkFlowMovementTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-tipo-movimiento.xls");
    	renderTemplate("Reports/WorkFlowMovementTypes.xls");
    	
    }
    
    public static void loadWorkFlowMovementTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_WorkFlow_Movement_Type res = ER_WorkFlow_Movement_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_WorkFlow_Movement_Type newWorkFlowMovementType = new ER_WorkFlow_Movement_Type();
    					newWorkFlowMovementType.transferCode = sheet.getCell(1,row).getContents();
                		newWorkFlowMovementType.name = sheet.getCell(2,row).getContents();
                		newWorkFlowMovementType.description = sheet.getCell(3,row).getContents();
                		newWorkFlowMovementType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newWorkFlowMovementType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("workFlowMovementType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("workFlowMovementType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("workFlowMovementType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newWorkFlowMovementTypeFromExcel();
    	
    }
    
    public static void newWorkFlowMovementTypeFromExcel() {
    	render();
    }

}
