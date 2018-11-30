package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Bank;
import models.ER_Bank_Account_Type;
import models.ER_Bank_Account_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminBankAccountTypes extends AdminBaseController {

	private static void filterBankAccountTypes(String name) {
		
		//Validate the parameters
		boolean validBankAccountType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validBankAccountType) {
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
		ModelPaginator bankAccountTypes = null;
		if (filter.isValidFilter()) {			
			bankAccountTypes = new ModelPaginator(ER_Bank_Account_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			bankAccountTypes = new ModelPaginator(ER_Bank_Account_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Bank_Account_Type.class);
		
		bankAccountTypes.orderBy("name ASC");
		bankAccountTypes.setPageNumber(page);
		bankAccountTypes.setPageSize(10);
		renderArgs.put("bankAccountTypes", bankAccountTypes);
		}
	
	/*
	 * ************************************************************************************************************************
	 * BankAccountTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void bankAccountTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add bankAccountTypes to the renderArgs
    	filterBankAccountTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("bankAccountType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchBankAccountTypes(String bankAccountType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		bankAccountTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",bankAccountType);
    	}
    	bankAccountTypesList(bankAccountType, true);
    }

    /*
     * *****************************************************************
     * Edit BankAccountType
     * *****************************************************************
     */
    public static void editBankAccountType(Long id) {
    	
    	if (id!=null) {
    		ER_Bank_Account_Type model = ER_Bank_Account_Type.findById(id);
    		renderArgs.put("bankAccountType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit BankAccountType
     * *****************************************************************
     */
    public static void saveBankAccountType(@Valid ER_Bank_Account_Type bankAccountType, Long bankAccountTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("bankAccountType.create.fielderrors"));
    		validation.keep();
    		editBankAccountType(bankAccountType.id);
    	} else {
    		
    		if( bankAccountTypeId == null ) {
    			bankAccountType.save();
    			flash.success(Messages.get("bankAccountType.create.success",1));
    		} else {
    			ER_Bank_Account_Type currentBankAccountType = ER_Bank_Account_Type.findById(bankAccountTypeId);
    			currentBankAccountType.name = bankAccountType.name;
    			currentBankAccountType.active = bankAccountType.active;
    			currentBankAccountType.save();
    			flash.success(Messages.get("bankAccountType.update.success"));
    		}
    		
    	}
    	
    	bankAccountTypesList(bankAccountType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete BankAccountType
     * *****************************************************************
     */
    public static void deleteBankAccountType(Long bankAccountTypeId) {
    	flash.clear();
    	
    	ER_Bank_Account_Type bankAccountType = ER_Bank_Account_Type.findById(bankAccountTypeId);
    	if(bankAccountType.id!=null) {
    		bankAccountType.delete();
        	flash.success(Messages.get("bankAccountType.delete.success"));
    	}else {
    		flash.error(Messages.get("bankAccountType.delete.error"));
    	}
    	
    	bankAccountTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewBankAccountTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipos-Cuenta-Bancaria.xls");
    	renderTemplate("Reports/BankAccountTypes.xls");
    	
    }
    
    public static void loadBankAccountTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Bank_Account_Type res = ER_Bank_Account_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Bank_Account_Type newBankAccountType = new ER_Bank_Account_Type();
    					newBankAccountType.transferCode = sheet.getCell(1,row).getContents();
                		newBankAccountType.name = sheet.getCell(2,row).getContents();
                		newBankAccountType.description = sheet.getCell(3,row).getContents();
                		newBankAccountType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newBankAccountType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("bankAccountType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("bankAccountType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("bankAccountType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newBankAccountTypeFromExcel();
    	
    }
    
    public static void newBankAccountTypeFromExcel() {
    	render();
    }

}
