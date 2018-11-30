package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Payment_Type;
import models.ER_Payment_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminPaymentTypes extends AdminBaseController {

	private static void filterPaymentTypes(String name) {
		
		//Validate the parameters
		boolean validPaymentType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validPaymentType) {
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
		ModelPaginator paymentTypes = null;
		if (filter.isValidFilter()) {			
			paymentTypes = new ModelPaginator(ER_Payment_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			paymentTypes = new ModelPaginator(ER_Payment_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Payment_Type.class);
		
		paymentTypes.orderBy("name ASC");
		paymentTypes.setPageNumber(page);
		paymentTypes.setPageSize(10);
		renderArgs.put("paymentTypes", paymentTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * PaymentTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void paymentTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add paymentTypes to the renderArgs
    	filterPaymentTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("paymentType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchPaymentTypes(String paymentType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		paymentTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",paymentType);
    	}
    	paymentTypesList(paymentType, true);
    }

    /*
     * *****************************************************************
     * Edit PaymentType
     * *****************************************************************
     */
    public static void editPaymentType(Long id) {
    	
    	if (id!=null) {
    		ER_Payment_Type model = ER_Payment_Type.findById(id);
    		renderArgs.put("paymentType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit PaymentType
     * *****************************************************************
     */
    public static void savePaymentType(@Valid ER_Payment_Type paymentType, Long paymentTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("paymentType.create.fielderrors"));
    		validation.keep();
    		editPaymentType(paymentType.id);
    	} else {
    		
    		if( paymentTypeId == null ) {
    			paymentType.save();
    			flash.success(Messages.get("paymentType.create.success",1));
    		} else {
    			ER_Payment_Type currentPaymentType = ER_Payment_Type.findById(paymentTypeId);
    			currentPaymentType.name = paymentType.name;
    			currentPaymentType.active = paymentType.active;
    			currentPaymentType.save();
    			flash.success(Messages.get("paymentType.update.success"));
    		}
    		
    	}
    	
    	paymentTypesList(paymentType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete PaymentType
     * *****************************************************************
     */
    public static void deletePaymentType(Long paymentTypeId) {
    	flash.clear();
    	
    	ER_Payment_Type paymentType = ER_Payment_Type.findById(paymentTypeId);
    	if(paymentType.id!=null) {
    		paymentType.delete();
        	flash.success(Messages.get("paymentType.delete.success"));
    	}else {
    		flash.error(Messages.get("paymentType.delete.error"));
    	}
    	
    	paymentTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewPaymentTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Método-Pago.xls");
    	renderTemplate("Reports/PaymentTypes.xls");
    	
    }
    
    public static void loadPaymentTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Payment_Type res = ER_Payment_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Payment_Type newPaymentType = new ER_Payment_Type();
    					newPaymentType.transferCode = sheet.getCell(1,row).getContents();
                		newPaymentType.name = sheet.getCell(2,row).getContents();
                		newPaymentType.description = sheet.getCell(3,row).getContents();
                		newPaymentType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newPaymentType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("paymentType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("paymentType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("paymentType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newPaymentTypeFromExcel();
    	
    }
    
    public static void newPaymentTypeFromExcel() {
    	render();
    }

}
