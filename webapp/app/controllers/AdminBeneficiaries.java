package controllers;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Beneficiaries;
import models.ER_Economic_Activity;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

import java.io.File;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminBeneficiaries extends AdminBaseController {

	private static void filterBeneficiaries(String name) {
		
		//Validate the parameters
		boolean validBeneficiaries = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validBeneficiaries) {
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
		ModelPaginator beneficiaries = null;
		if (filter.isValidFilter()) {
            beneficiaries = new ModelPaginator(ER_Beneficiaries.class, filter.getQuery(), filter.getParametersArray());
		} else {
            beneficiaries = new ModelPaginator(ER_Beneficiaries.class);
		}
		
		//Nationalties = new ModelPaginator(ER_EconomicActivity.class);

        beneficiaries.orderBy("name ASC");
        beneficiaries.setPageNumber(page);
        beneficiaries.setPageSize(10);
		renderArgs.put("beneficiaries", beneficiaries);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Beneficiaries list
	 * ************************************************************************************************************************
	 */
	
    public static void beneficiariesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add economicActivities to the renderArgs
    	filterBeneficiaries(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("beneficiary",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchBeneficiaries(String beneficiary, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");

            beneficiariesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",beneficiary);
    	}
        beneficiariesList(beneficiary, true);
    }

    /*
     * *****************************************************************
     * Edit Beneficiaries
     * *****************************************************************
     */
    public static void editBeneficiaries(Long id) {
    	
    	if (id!=null) {
    		ER_Beneficiaries model = ER_Beneficiaries.findById(id);
    		renderArgs.put("beneficiaries", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Beneficiaries
     * *****************************************************************
     */
    public static void saveBeneficiaries(@Valid ER_Beneficiaries beneficiaries, Long beneficiariesId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("beneficiaries.create.fielderrors"));
    		validation.keep();
    		editBeneficiaries(beneficiaries.id);
    	} else {
    		
    		if( beneficiariesId == null ) {
                beneficiaries.save();
    			flash.success(Messages.get("beneficiaries.create.success",1));
    		} else {
    			ER_Beneficiaries currentBeneficiaries = ER_Beneficiaries.findById(beneficiariesId);
    			currentBeneficiaries.name = beneficiaries.name;
    			currentBeneficiaries.transferCode = beneficiaries.transferCode;
    			currentBeneficiaries.save();
    			flash.success(Messages.get("beneficiaries.update.success"));
    		}
    		
    	}

        beneficiariesList(beneficiaries.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Beneficiaries
     * *****************************************************************
     */
    public static void deleteBeneficiaries(Long beneficiariesId) {
    	flash.clear();
    	
    	ER_Beneficiaries beneficiaries = ER_Beneficiaries.findById(beneficiariesId);
    	if(beneficiaries.id!=null) {
            beneficiaries.delete();
        	flash.success(Messages.get("beneficiaries.delete.success"));
    	}else {
    		flash.error(Messages.get("beneficiaries.delete.error"));
    	}

        beneficiariesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewBeneficiariesTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Beneficiarios.xls");
    	renderTemplate("Reports/Beneficiaries.xls");
    	
    }
    
    public static void loadBeneficiariesFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Beneficiaries res = ER_Beneficiaries.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Beneficiaries newBeneficiaries = new ER_Beneficiaries();
    					newBeneficiaries.transferCode = sheet.getCell(1,row).getContents();
                		newBeneficiaries.name = sheet.getCell(2,row).getContents();

                		newBeneficiaries.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("beneficiaries.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("beneficiaries.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("beneficiaries.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("beneficiaries.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newBeneficiariesFromExcel();
    	
    }
    
    public static void newBeneficiariesFromExcel() {
    	render();
    }

}
