package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Agent_Code;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminAgentCodes extends AdminBaseController {

	private static void filterAgentCodes(String name) {
		
		//Validate the parameters
		boolean validAgentCode = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validAgentCode) {
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
		ModelPaginator agentCodes = null;
		if (filter.isValidFilter()) {			
			agentCodes = new ModelPaginator(ER_Agent_Code.class, filter.getQuery(), filter.getParametersArray());
		} else {
			agentCodes = new ModelPaginator(ER_Agent_Code.class);
		}
		
		//Nationalties = new ModelPaginator(ER_AgentCode.class);
		
		agentCodes.orderBy("name ASC");
		agentCodes.setPageNumber(page);
		agentCodes.setPageSize(10);
		renderArgs.put("agentCodes", agentCodes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * AgentCodes list
	 * ************************************************************************************************************************
	 */
	
    public static void agentCodesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add agentCodes to the renderArgs
    	filterAgentCodes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("agentCode",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchAgentCodes(String agentCode, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		agentCodesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",agentCode);
    	}
    	agentCodesList(agentCode, true);
    }

    /*
     * *****************************************************************
     * Edit AgentCode
     * *****************************************************************
     */
    public static void editAgentCode(Long id) {
    	
    	if (id!=null) {
    		ER_Agent_Code model = ER_Agent_Code.findById(id);
    		renderArgs.put("agentCode", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit AgentCode
     * *****************************************************************
     */
    public static void saveAgentCode(@Valid ER_Agent_Code agentCode, Long agentCodeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("agentCode.create.fielderrors"));
    		validation.keep();
    		editAgentCode(agentCode.id);
    	} else {
    		
    		if( agentCodeId == null ) {
    			agentCode.save();
    			flash.success(Messages.get("agentCode.create.success",1));
    		} else {
    			ER_Agent_Code currentAgentCode = ER_Agent_Code.findById(agentCodeId);
    			currentAgentCode.name = agentCode.name;
    			currentAgentCode.active = agentCode.active;
    			currentAgentCode.save();
    			flash.success(Messages.get("agentCode.update.success"));
    		}
    		
    	}
    	
    	agentCodesList(agentCode.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete AgentCode
     * *****************************************************************
     */
    public static void deleteAgentCode(Long agentCodeId) {
    	flash.clear();
    	
    	ER_Agent_Code agentCode = ER_Agent_Code.findById(agentCodeId);
    	if(agentCode.id!=null) {
    		agentCode.delete();
        	flash.success(Messages.get("agentCode.delete.success"));
    	}else {
    		flash.error(Messages.get("agentCode.delete.error"));
    	}
    	
    	agentCodesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewAgentCodeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Códigos-Agente.xls");
    	renderTemplate("Reports/AgentCodes.xls");
    	
    }
    
    public static void loadAgentCodeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Agent_Code res = ER_Agent_Code.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Agent_Code newAgentCode = new ER_Agent_Code();
    					newAgentCode.transferCode = sheet.getCell(1,row).getContents();
                		newAgentCode.name = sheet.getCell(2,row).getContents();
                		newAgentCode.description = sheet.getCell(3,row).getContents();
                		newAgentCode.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newAgentCode.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("agentCode.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("agentCode.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("agentCode.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newAgentCodeFromExcel();
    	
    }
    
    public static void newAgentCodeFromExcel() {
    	render();
    }

}
