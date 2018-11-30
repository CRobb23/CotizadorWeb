package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Document_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminDocumentTypes extends AdminBaseController {

	private static void filterDocumentTypes(String name) {
		
		//Validate the parameters
		boolean validDocumentType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validDocumentType) {
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
		ModelPaginator documentTypes = null;
		if (filter.isValidFilter()) {			
			documentTypes = new ModelPaginator(ER_Document_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			documentTypes = new ModelPaginator(ER_Document_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_DocumentType.class);
		
		documentTypes.orderBy("name ASC");
		documentTypes.setPageNumber(page);
		documentTypes.setPageSize(10);
		renderArgs.put("documentTypes", documentTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * DocumentTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void documentTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add documentTypes to the renderArgs
    	filterDocumentTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("documentType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchDocumentTypes(String documentType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		documentTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",documentType);
    	}
    	documentTypesList(documentType, true);
    }

    /*
     * *****************************************************************
     * Edit DocumentType
     * *****************************************************************
     */
    public static void editDocumentType(Long id) {
    	
    	if (id!=null) {
    		ER_Document_Type model = ER_Document_Type.findById(id);
    		renderArgs.put("documentType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit DocumentType
     * *****************************************************************
     */
    public static void saveDocumentType(@Valid ER_Document_Type documentType, Long documentTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("documentType.create.fielderrors"));
    		validation.keep();
    		editDocumentType(documentType.id);
    	} else {
    		
    		if( documentTypeId == null ) {
    			documentType.save();
    			flash.success(Messages.get("documentType.create.success",1));
    		} else {
    			ER_Document_Type currentDocumentType = ER_Document_Type.findById(documentTypeId);
    			currentDocumentType.name = documentType.name;
    			currentDocumentType.active = documentType.active;
    			currentDocumentType.save();
    			flash.success(Messages.get("documentType.update.success"));
    		}
    		
    	}
    	
    	documentTypesList(documentType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete DocumentType
     * *****************************************************************
     */
    public static void deleteDocumentType(Long documentTypeId) {
    	flash.clear();
    	
    	ER_Document_Type documentType = ER_Document_Type.findById(documentTypeId);
    	if(documentType.id!=null) {
    		documentType.delete();
        	flash.success(Messages.get("documentType.delete.success"));
    	}else {
    		flash.error(Messages.get("documentType.delete.error"));
    	}
    	
    	documentTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewDocumentTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Documento.xls");
    	renderTemplate("Reports/DocumentTypes.xls");
    	
    }
    
    public static void loadDocumentTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Document_Type res = ER_Document_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Document_Type newDocumentType = new ER_Document_Type();
    					newDocumentType.transferCode = sheet.getCell(1,row).getContents();
                		newDocumentType.name = sheet.getCell(2,row).getContents();
                		newDocumentType.description = sheet.getCell(3,row).getContents();
                		newDocumentType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newDocumentType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("documentType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("documentType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("documentType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newDocumentTypeFromExcel();
    	
    }
    
    public static void newDocumentTypeFromExcel() {
    	render();
    }

}
