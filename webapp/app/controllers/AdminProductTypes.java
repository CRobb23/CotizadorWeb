package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Product_Type;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminProductTypes extends AdminBaseController {

	private static void filterProductTypes(String name) {
		
		//Validate the parameters
		boolean validProductType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validProductType) {
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
		ModelPaginator productTypes = null;
		if (filter.isValidFilter()) {			
			productTypes = new ModelPaginator(ER_Product_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			productTypes = new ModelPaginator(ER_Product_Type.class);
		}
		
		productTypes.orderBy("name ASC");
		productTypes.setPageNumber(page);
		productTypes.setPageSize(10);
		renderArgs.put("productTypes", productTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Product types list
	 * ************************************************************************************************************************
	 */
	
    public static void productTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add product Types to the renderArgs
    	filterProductTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("productType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchProductType(String type, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		productTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",type);
    	}
    	productTypesList(type, true);
    }

    /*
     * *****************************************************************
     * Edit Product Type
     * *****************************************************************
     */
    public static void editProductType(Long id) {
    	
    	if (id!=null) {
    		ER_Product_Type model = ER_Product_Type.findById(id);
    		renderArgs.put("productType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Product Type
     * *****************************************************************
     */
    public static void saveProductType(@Valid ER_Product_Type productType, Long productTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("productType.create.fielderrors"));
    		validation.keep();
    		editProductType(productTypeId);
    	} else {
    		
    		if( productTypeId == null ) {
    			productType.save();
    			flash.success(Messages.get("productType.create.success",1));
    		} else {
    			ER_Product_Type currentProductType = ER_Product_Type.findById(productTypeId);
    			currentProductType.name = productType.name;
    			currentProductType.active = productType.active;
    			currentProductType.save();
    			flash.success(Messages.get("productType.update.success"));
    		}
    		
    	}
    	
    	productTypesList(productType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Product Type
     * *****************************************************************
     */
    public static void deleteProductType(Long productTypeId) {
    	flash.clear();
    	
    	ER_Product_Type productType = ER_Product_Type.findById(productTypeId);
    	if(productType.id!=null) {
    		productType.delete();
        	flash.success(Messages.get("productType.delete.success"));
    	}else {
    		flash.error(Messages.get("productType.delete.error"));
    	}
    	
    	productTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewProductTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipos-Producto.xls");
    	renderTemplate("Reports/ProductTypes.xls");
    	
    }
    
    public static void loadProductTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Product_Type search = ER_Product_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				
    				if(search==null) {
    					ER_Product_Type newProductType = new ER_Product_Type();
                		newProductType.name = sheet.getCell(2,row).getContents();
                		newProductType.transferCode = sheet.getCell(1,row).getContents();
                		newProductType.active = "1".equals(sheet.getCell(3,row).getContents());
                		
                		newProductType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("productType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("productType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("productType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("productType.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newProductTypeFromExcel();
    	
    }
    
    public static void newProductTypeFromExcel() {
    	render();
    }

}
