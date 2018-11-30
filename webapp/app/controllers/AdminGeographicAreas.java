package controllers;

import java.io.File;
import java.util.List;

import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import jxl.Sheet;
import jxl.Workbook;
import models.ER_Geographic_Area;
import models.ER_Profession;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.excel.RenderExcel;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminGeographicAreas extends AdminBaseController {

	private static void filterGeographicAreas(String name, String tipo) {
		
		//Validate the parameters
		boolean validGeographicArea = GeneralMethods.validateParameter(name);
		boolean validType = GeneralMethods.validateParameter(tipo);
		
		//Create a new filter object and add the query for each valid parameter
		StringBuilder query  = new StringBuilder("");
		
		if (validGeographicArea) {
			query.append("name like '%").append(name).append("%'");
		}
		if(validType) {
			if(query.length()>0) {
				query.append(" and ");
			}
			if(tipo.equals("PAIS")) {
				query.append("father is null ");
			}else if(tipo.equals("DEPARTAMENTO")) {
				query.append("father.father is null ");
			}else if(tipo.equals("MUNICIPIO")) {
				query.append("father.father.id is not null");
			}
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
		ModelPaginator geographicAreas = null;
		if (validType || validGeographicArea) {			
			geographicAreas = new ModelPaginator(ER_Geographic_Area.class, query.toString());
		} else {
			geographicAreas = new ModelPaginator(ER_Geographic_Area.class);
		}
		
		//Nationalties = new ModelPaginator(ER_GeographicArea.class);
		
		geographicAreas.orderBy("name ASC");
		geographicAreas.setPageNumber(page);
		geographicAreas.setPageSize(10);
		renderArgs.put("geographicAreas", geographicAreas);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * GeographicAreas list
	 * ************************************************************************************************************************
	 */
	
    public static void geographicAreasList(String name, String tipo, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add geographicAreas to the renderArgs
    	filterGeographicAreas(name,tipo);
    	
    	//if it was a search
    	if(name!=null) {
    		renderArgs.put("geographicArea",name);
    		renderArgs.put("tipo",tipo);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchGeographicAreas(String geographicArea, String tipo, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("name");
    		
    		geographicAreasList(null,null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",geographicArea);
    		session.put("tipo",tipo);
    	}
    	geographicAreasList(geographicArea,tipo, true);
    }

    /*
     * *****************************************************************
     * Edit GeographicArea
     * *****************************************************************
     */
    public static void editGeographicArea(Long id) {
    	
    	if (id!=null) {
    		ER_Geographic_Area model = ER_Geographic_Area.findById(id);
    		renderArgs.put("geographicArea", model);
    		if(model.father!=null) {
    			
    			if(model.father.father !=null) {
    				List<ER_Geographic_Area> deps = ER_Geographic_Area.find(" active = 1 and father.id = ?",model.father.father.id).fetch();
            		renderArgs.put("deps", deps);
    			}
    			
    		}
    		
    	}
		
    	List<ER_Geographic_Area> countries = ER_Geographic_Area.find(" active = 1 and father is null").fetch();
    	renderArgs.put("countries", countries);
    	
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit GeographicArea
     * *****************************************************************
     */
    public static void saveGeographicArea(@Valid ER_Geographic_Area geographicArea, Long geographicAreaId, Long pais, Long departamento) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("geographicArea.create.fielderrors"));
    		validation.keep();
    		editGeographicArea(geographicArea.id);
    	} else {
    		
    		if( geographicAreaId == null ) {
    			if(departamento==null) {
    				if(pais!=null) {//es un departamento
    					geographicArea.father = ER_Geographic_Area.findById(pais);
    				}//sino es un país
    			}else {//es un municipio
    				geographicArea.father = ER_Geographic_Area.findById(departamento);
    			}
    			geographicArea.save();
    			flash.success(Messages.get("geographicArea.create.success",1));
    		} else {
    			ER_Geographic_Area currentGeographicArea = ER_Geographic_Area.findById(geographicAreaId);
    			currentGeographicArea.name = geographicArea.name;
    			currentGeographicArea.active = geographicArea.active;
    			if(departamento==null) {
    				if(pais!=null) {//es un departamento
    					geographicArea.father = ER_Geographic_Area.findById(pais);
    				}//sino es un país
    			}else {//es un municipio
    				geographicArea.father = ER_Geographic_Area.findById(departamento);
    			}
    			currentGeographicArea.save();
    			flash.success(Messages.get("geographicArea.update.success"));
    		}
    		
    	}
    	
    	geographicAreasList(geographicArea.name,null, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete GeographicArea
     * *****************************************************************
     */
    public static void deleteGeographicArea(Long geographicAreaId) {
    	flash.clear();
    	
    	ER_Geographic_Area geographicArea = ER_Geographic_Area.findById(geographicAreaId);
    	if(geographicArea.id!=null) {
    		geographicArea.delete();
        	flash.success(Messages.get("geographicArea.delete.success"));
    	}else {
    		flash.error(Messages.get("geographicArea.delete.error"));
    	}
    	
    	geographicAreasList(null,null,true);
    	
    }
    
    
    
    /*
     * *****************************************************************
     * Download CSV template 
     * *****************************************************************
     */
    public static void downloadNewGeographicAreaTemplate() {
    	
    	request.format = "xls";
    	renderArgs.put(RenderExcel.RA_FILENAME, "Plantilla-Área-Geográfica.xls");
    	renderTemplate("Reports/GeographicAreas.xls");
    	
    }
    
    public static void main(String[] args){
    	String text = "GT 1  101";
    	System.out.println(text.substring(0, 5));
    }
    
    public static void loadGeographicAreaFromExcel(File excelFile) {
    	Workbook w;
    	
    	try {
    		
    		w = Workbook.getWorkbook(excelFile);
    		Sheet sheet = w.getSheet(0);
    		
    		Integer objectsCreated = 0;
    		Integer errors = 0;
   
    		for(Integer row = 3; row < sheet.getRows(); row++) {
    			
    			try {
    				
    				ER_Geographic_Area res = ER_Geographic_Area.find("transfer_code = ?", sheet.getCell(1,row).getContents()).first();
    				if(res==null) {
    					ER_Geographic_Area newGeographicArea = new ER_Geographic_Area();
    					newGeographicArea.transferCode = sheet.getCell(1,row).getContents();
                		newGeographicArea.name = sheet.getCell(2,row).getContents();
                		newGeographicArea.active = "1".equals(sheet.getCell(4,row).getContents());
                		
                		String tipo = sheet.getCell(5,row).getContents();
                		if("D".equals(tipo)){
                			String father = sheet.getCell(6,row).getContents();
                			newGeographicArea.father = ER_Geographic_Area.find("transfer_code = ?", father).first();
                		}else if("M".equals(tipo)){
                			newGeographicArea.father = ER_Geographic_Area.find("transfer_code = ?", newGeographicArea.transferCode.substring(0,5)).first();
                		}else if("Z".equals(tipo)){
                			newGeographicArea.father = ER_Geographic_Area.find("transfer_code = ?", newGeographicArea.transferCode.substring(0,9)).first();
                		}
                		
                		newGeographicArea.save();
                		
                		objectsCreated++;
    				}
            		
    			}catch(Exception e) {
    				flash.error(Messages.get("geographicArea.create.fielderrors"));
    	    		e.printStackTrace();
    	    		errors++;
    			}
    			
    		}
    		
    		if(errors==0) {
    			if(objectsCreated>0) {
    				flash.success(Messages.get("geographicArea.create.success",objectsCreated));
    			}else {
    				flash.error(Messages.get("geographicArea.create.emptyFile"));
    			}
    		}
    		
    	}catch (Exception e) {
    		flash.error(Messages.get("product.create.fielderrors"));
    		e.printStackTrace();
    	}
    	
    	newGeographicAreaFromExcel();
    	
    }
    
    public static void newGeographicAreaFromExcel() {
    	render();
    }

}
