package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Card_Type;
import models.ER_Card_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminCardTypes extends AdminBaseController {

	private static void filterCardTypes(String name) {
		
		//Validate the parameters
		boolean validCardType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validCardType) {
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
		ModelPaginator cardTypes = null;
		if (filter.isValidFilter()) {			
			cardTypes = new ModelPaginator(ER_Card_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			cardTypes = new ModelPaginator(ER_Card_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Card_Type.class);
		
		cardTypes.orderBy("name ASC");
		cardTypes.setPageNumber(page);
		cardTypes.setPageSize(10);
		renderArgs.put("cardTypes", cardTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * CardTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void cardTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add cardTypes to the renderArgs
    	filterCardTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("cardType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchCardTypes(String cardType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		cardTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",cardType);
    	}
    	cardTypesList(cardType, true);
    }

    /*
     * *****************************************************************
     * Edit CardType
     * *****************************************************************
     */
    public static void editCardType(Long id) {
    	
    	if (id!=null) {
    		ER_Card_Type model = ER_Card_Type.findById(id);
    		renderArgs.put("cardType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit CardType
     * *****************************************************************
     */
    public static void saveCardType(@Valid ER_Card_Type cardType, Long cardTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("cardType.create.fielderrors"));
    		validation.keep();
    		editCardType(cardType.id);
    	} else {
    		
    		if( cardTypeId == null ) {
    			cardType.save();
    			flash.success(Messages.get("cardType.create.success",1));
    		} else {
    			ER_Card_Type currentCardType = ER_Card_Type.findById(cardTypeId);
    			currentCardType.name = cardType.name;
    			currentCardType.active = cardType.active;
    			currentCardType.save();
    			flash.success(Messages.get("cardType.update.success"));
    		}
    		
    	}
    	
    	cardTypesList(cardType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete CardType
     * *****************************************************************
     */
    public static void deleteCardType(Long cardTypeId) {
    	flash.clear();
    	
    	ER_Card_Type cardType = ER_Card_Type.findById(cardTypeId);
    	if(cardType.id!=null) {
    		cardType.delete();
        	flash.success(Messages.get("cardType.delete.success"));
    	}else {
    		flash.error(Messages.get("cardType.delete.error"));
    	}
    	
    	cardTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewCardTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Tarjeta.xls");
    	renderTemplate("Reports/CardTypes.xls");
    	
    }
    
    public static void loadCardTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Card_Type res = ER_Card_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Card_Type newCardType = new ER_Card_Type();
    					newCardType.transferCode = sheet.getCell(1,row).getContents();
                		newCardType.name = sheet.getCell(2,row).getContents();
                		newCardType.description = sheet.getCell(3,row).getContents();
                		newCardType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newCardType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("cardType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("cardType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("cardType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newCardTypeFromExcel();
    	
    }
    
    public static void newCardTypeFromExcel() {
    	render();
    }

}
