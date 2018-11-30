package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Production_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminProductionTypes extends AdminBaseController {

	private static void filterProductionTypes(String name) {
		
		//Validate the parameters
		boolean validProductionType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validProductionType) {
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
		ModelPaginator productionTypes = null;
		if (filter.isValidFilter()) {			
			productionTypes = new ModelPaginator(ER_Production_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			productionTypes = new ModelPaginator(ER_Production_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_ProductionType.class);
		
		productionTypes.orderBy("name ASC");
		productionTypes.setPageNumber(page);
		productionTypes.setPageSize(10);
		renderArgs.put("productionTypes", productionTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * ProductionTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void productionTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add productionTypes to the renderArgs
    	filterProductionTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("productionType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchProductionTypes(String productionType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		productionTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",productionType);
    	}
    	productionTypesList(productionType, true);
    }

    /*
     * *****************************************************************
     * Edit ProductionType
     * *****************************************************************
     */
    public static void editProductionType(Long id) {
    	
    	if (id!=null) {
    		ER_Production_Type model = ER_Production_Type.findById(id);
    		renderArgs.put("productionType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit ProductionType
     * *****************************************************************
     */
    public static void saveProductionType(@Valid ER_Production_Type productionType, Long productionTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("productionType.create.fielderrors"));
    		validation.keep();
    		editProductionType(productionType.id);
    	} else {
    		
    		if( productionTypeId == null ) {
    			productionType.save();
    			flash.success(Messages.get("productionType.create.success",1));
    		} else {
    			ER_Production_Type currentProductionType = ER_Production_Type.findById(productionTypeId);
    			currentProductionType.name = productionType.name;
    			currentProductionType.active = productionType.active;
    			currentProductionType.save();
    			flash.success(Messages.get("productionType.update.success"));
    		}
    		
    	}
    	
    	productionTypesList(productionType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete ProductionType
     * *****************************************************************
     */
    public static void deleteProductionType(Long productionTypeId) {
    	flash.clear();
    	
    	ER_Production_Type productionType = ER_Production_Type.findById(productionTypeId);
    	if(productionType.id!=null) {
    		productionType.delete();
        	flash.success(Messages.get("productionType.delete.success"));
    	}else {
    		flash.error(Messages.get("productionType.delete.error"));
    	}
    	
    	productionTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewProductionTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Producción.xls");
    	renderTemplate("Reports/ProductionTypes.xls");
    	
    }
    
    public static void loadProductionTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Production_Type res = ER_Production_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Production_Type newProductionType = new ER_Production_Type();
    					newProductionType.transferCode = sheet.getCell(1,row).getContents();
                		newProductionType.name = sheet.getCell(2,row).getContents();
                		newProductionType.description = sheet.getCell(3,row).getContents();
                		newProductionType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newProductionType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("productionType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("productionType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("productionType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newProductionTypeFromExcel();
    	
    }
    
    public static void newProductionTypeFromExcel() {
    	render();
    }

}
