package controllers;

import java.io.File;

import helpers.Filter;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Plate_Type;
import models.ER_Plate_Type;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminPlateTypes extends AdminBaseController {

	private static void filterPlateTypes(String name) {
		
		//Validate the parameters
		boolean validPlateType = GeneralMethods.validateParameter(name);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validPlateType) {
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
		ModelPaginator plateTypes = null;
		if (filter.isValidFilter()) {			
			plateTypes = new ModelPaginator(ER_Plate_Type.class, filter.getQuery(), filter.getParametersArray());
		} else {
			plateTypes = new ModelPaginator(ER_Plate_Type.class);
		}
		
		//Nationalties = new ModelPaginator(ER_Plate_Type.class);
		
		plateTypes.orderBy("name ASC");
		plateTypes.setPageNumber(page);
		plateTypes.setPageSize(10);
		renderArgs.put("plateTypes", plateTypes);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * PlateTypes list
	 * ************************************************************************************************************************
	 */
	
    public static void plateTypesList(String name, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add plateTypes to the renderArgs
    	filterPlateTypes(name);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("plateType",name);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchPlateTypes(String plateType, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		plateTypesList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",plateType);
    	}
    	plateTypesList(plateType, true);
    }

    /*
     * *****************************************************************
     * Edit PlateType
     * *****************************************************************
     */
    public static void editPlateType(Long id) {
    	
    	if (id!=null) {
    		ER_Plate_Type model = ER_Plate_Type.findById(id);
    		renderArgs.put("plateType", model);
    	}
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit PlateType
     * *****************************************************************
     */
    public static void savePlateType(@Valid ER_Plate_Type plateType, Long plateTypeId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("plateType.create.fielderrors"));
    		validation.keep();
    		editPlateType(plateType.id);
    	} else {
    		
    		if( plateTypeId == null ) {
    			plateType.save();
    			flash.success(Messages.get("plateType.create.success",1));
    		} else {
    			ER_Plate_Type currentPlateType = ER_Plate_Type.findById(plateTypeId);
    			currentPlateType.name = plateType.name;
    			currentPlateType.active = plateType.active;
    			currentPlateType.save();
    			flash.success(Messages.get("plateType.update.success"));
    		}
    		
    	}
    	
    	plateTypesList(plateType.name, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete PlateType
     * *****************************************************************
     */
    public static void deletePlateType(Long plateTypeId) {
    	flash.clear();
    	
    	ER_Plate_Type plateType = ER_Plate_Type.findById(plateTypeId);
    	if(plateType.id!=null) {
    		plateType.delete();
        	flash.success(Messages.get("plateType.delete.success"));
    	}else {
    		flash.error(Messages.get("plateType.delete.error"));
    	}
    	
    	plateTypesList(null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewPlateTypeTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Tipo-Placa.xls");
    	renderTemplate("Reports/PlateTypes.xls");
    	
    }
    
    public static void loadPlateTypeFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Plate_Type res = ER_Plate_Type.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Plate_Type newPlateType = new ER_Plate_Type();
    					newPlateType.transferCode = sheet.getCell(1,row).getContents();
                		newPlateType.name = sheet.getCell(2,row).getContents();
                		newPlateType.description = sheet.getCell(3,row).getContents();
                		newPlateType.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		newPlateType.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("plateType.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("plateType.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("plateType.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newPlateTypeFromExcel();
    	
    }
    
    public static void newPlateTypeFromExcel() {
    	render();
    }

}
