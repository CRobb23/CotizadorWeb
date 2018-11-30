package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Coverage_Code;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminCoverageCodes extends AdminBaseController {

	private static void filterCoverageCodes(String name) {
		
		//Validate the parameters
		boolean validCoverageCode = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validCoverageCode) {
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
		ModelPaginator coverageCodes = null;
		if (filter.isValidFilter()) {			
			coverageCodes = new ModelPaginator(ER_Coverage_Code.class, filter.getQuery(), filter.getParametersArray());
		} else {
			coverageCodes = new ModelPaginator(ER_Coverage_Code.class);
		}
		
		//Nationalties = new ModelPaginator(ER_CoverageCode.class);
		
		coverageCodes.orderBy("name ASC");
		coverageCodes.setPageNumber(page);
		coverageCodes.setPageSize(10);
		renderArgs.put("coverageCodes", coverageCodes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * CoverageCodes list
	 * ************************************************************************************************************************
	 */
	
    public static void coverageCodesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add coverageCodes to the renderArgs
    	filterCoverageCodes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("coverageCode",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchCoverageCodes(String coverageCode, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		coverageCodesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",coverageCode);
    	}
    	coverageCodesList(coverageCode, true);
    }

    /*
     * *****************************************************************
     * Edit CoverageCode
     * *****************************************************************
     */
    public static void editCoverageCode(Long id) {
    	
    	if (id!=null) {
    		ER_Coverage_Code model = ER_Coverage_Code.findById(id);
    		renderArgs.put("coverageCode", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit CoverageCode
     * *****************************************************************
     */
    public static void saveCoverageCode(@Valid ER_Coverage_Code coverageCode, Long coverageCodeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("coverageCode.create.fielderrors"));
    		validation.keep();
    		editCoverageCode(coverageCode.id);
    	} else {
    		
    		if( coverageCodeId == null ) {
    			coverageCode.save();
    			flash.success(Messages.get("coverageCode.create.success",1));
    		} else {
    			ER_Coverage_Code currentCoverageCode = ER_Coverage_Code.findById(coverageCodeId);
    			currentCoverageCode.name = coverageCode.name;
    			currentCoverageCode.active = coverageCode.active;
    			currentCoverageCode.save();
    			flash.success(Messages.get("coverageCode.update.success"));
    		}
    		
    	}
    	
    	coverageCodesList(coverageCode.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete CoverageCode
     * *****************************************************************
     */
    public static void deleteCoverageCode(Long coverageCodeId) {
    	flash.clear();
    	
    	ER_Coverage_Code coverageCode = ER_Coverage_Code.findById(coverageCodeId);
    	if(coverageCode.id!=null) {
    		coverageCode.delete();
        	flash.success(Messages.get("coverageCode.delete.success"));
    	}else {
    		flash.error(Messages.get("coverageCode.delete.error"));
    	}
    	
    	coverageCodesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewCoverageCodeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Codigos-Cobertura.xls");
    	renderTemplate("Reports/CoverageCodes.xls");
    	
    }
    
    public static void loadCoverageCodeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Coverage_Code res = ER_Coverage_Code.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Coverage_Code newCoverageCode = new ER_Coverage_Code();
    					newCoverageCode.transferCode = sheet.getCell(1,row).getContents();
                		newCoverageCode.name = sheet.getCell(2,row).getContents();
                		newCoverageCode.description = sheet.getCell(3,row).getContents();
                		newCoverageCode.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newCoverageCode.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("coverageCode.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("coverageCode.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("coverageCode.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newCoverageCodeFromExcel();
    	
    }
    
    public static void newCoverageCodeFromExcel() {
    	render();
    }

}
