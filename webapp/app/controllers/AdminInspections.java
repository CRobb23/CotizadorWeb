package controllers;

import java.util.List;

import helpers.Filter;
import helpers.GeneralMethods;
import models.ER_Inspection;
import models.ER_Inspection_Type;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminInspections extends AdminBaseController{
	
private static void filterInspections(String number) {
		
		//Validate the parameters
		boolean validInspection = GeneralMethods.validateParameter(number);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validInspection) {
			filter.addQuery("inspectionNumber like ?", "%"+number+"%");
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
		ModelPaginator inspections = null;
		if (filter.isValidFilter()) {			
			inspections = new ModelPaginator(ER_Inspection.class, filter.getQuery(), filter.getParametersArray());
		} else {
			inspections = new ModelPaginator(ER_Inspection.class);
		}
		
		//inspections = new ModelPaginator(ER_Inspection.class);
		
		inspections.orderBy("inspectionNumber ASC");
		inspections.setPageNumber(page);
		inspections.setPageSize(10);
		renderArgs.put("inspections", inspections);
		
	}
	
	/*
	 * ************************************************************************************************************************
	 * Inspections list
	 * ************************************************************************************************************************
	 */
	
    public static void inspectionsList(String number, boolean intern) {
    	flash.clear();
    	if (!intern) {
    		session.remove("page");
    		session.remove("number");
    	}
    	
    	//Add inspections to the renderArgs
    	filterInspections(number);
    	
    	//if it was a search
    	if(number!=null) {
    		renderArgs.put("number",number);
    	}
    	
    	//Render the list
    	render();
    }
    
    public static void searchInspections(String number, String all, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("number");
    		
    		inspectionsList(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("name",number);
    	}
    	inspectionsList(number, true);
    }

    /*
     * *****************************************************************
     * Edit Inspection
     * *****************************************************************
     */
    public static void editInspection(Long id) {
    	
    	if (id!=null) {
    		ER_Inspection model = ER_Inspection.findById(id);
    		renderArgs.put("inspection", model);
    	}
    	
    	List<ER_Inspection_Type> inspectionTypes = ER_Inspection_Type.find("order by typeOrder").fetch();
    	renderArgs.put("inspectionTypes",inspectionTypes);
    	render();
    }
    
    /*
     * *****************************************************************
     * Edit Inspection
     * *****************************************************************
     */
    public static void saveInspection(@Valid ER_Inspection inspection, Long inspectionId) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("inspection.create.fielderrors"));
    		validation.keep();
    		editInspection(inspection.id);
    	} else {
    		
    		if( inspectionId == null ) {
    			inspection.save();
    			flash.success(Messages.get("inspection.create.success",1));
    		} else {
    			ER_Inspection currentInspection = ER_Inspection.findById(inspectionId);
    			currentInspection.inspectionNumber = inspection.inspectionNumber;
    			currentInspection.inspected = inspection.inspected;
    			currentInspection.address = inspection.address;
    			currentInspection.appointmentDate = inspection.appointmentDate;
    			currentInspection.save();
    			flash.success(Messages.get("inspection.update.success"));
    		}
    		
    	}
    	
    	inspectionsList(inspection.inspectionNumber, true);
    	
    }
    
    /*
     * *****************************************************************
     * Delete Inspection
     * *****************************************************************
     */
    public static void deleteInspection(Long inspectionId) {
    	flash.clear();
    	
    	ER_Inspection inspection = ER_Inspection.findById(inspectionId);
    	if(inspection.id!=null) {
    		inspection.delete();
        	flash.success(Messages.get("inspection.delete.success"));
    	}else {
    		flash.error(Messages.get("inspection.delete.error"));
    	}
    	
    	inspectionsList(null,true);
    	
    }
	
}
