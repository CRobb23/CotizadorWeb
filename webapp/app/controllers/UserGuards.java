package controllers;

import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import models.ER_Guard;
import play.Logger;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.modules.paginate.ValuePaginator;
import play.mvc.With;

@With(Secure.class)
@Check("Usuario de cabina")
public class UserGuards extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterGuards(String searchField) {
		
		//Validate the parameters
		boolean valid = GeneralMethods.validateParameter(searchField);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (valid) {
			searchField = searchField.trim();
			
			filter.addGroupStart(Operator.AND);
			try {
				Long number = Long.parseLong(searchField);
				filter.addQuery("incident.number = ?", number, Operator.OR);
			} catch (Exception e) {
				filter.addQuery("concat(incident.client.firstName,' ',incident.client.lastName, incident.vehicle.plate)  like ?", "%"+searchField+"%", Operator.OR);
			}
			filter.addGroupEnd();
			
		}		
		
		if (filter.isValidFilter()) {
			//filter.addQuery("incident.status.code=?", ERConstants.INCIDENT_STATUS_PENDING_INSPECTION, Operator.AND);
		}
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		if (filter.isValidFilter()) {
			ModelPaginator guards = new ModelPaginator(ER_Guard.class, filter.getQuery(), filter.getParametersArray());
			guards.setPageSize(10);
			renderArgs.put("guards", guards);
		}
	}
	
	/*
	 * ************************************************************************************************************************
	 * Incidents list and search
	 * ************************************************************************************************************************
	 */
	@Access
	public static void guardsList(String searchField, Boolean search) {
    	
    	params.flash();
    	
    	filterGuards(searchField);
    	
    	ValuePaginator list = (ValuePaginator)renderArgs.get("incidents");
    	
    	if (search!=null && search && (list==null || list.size()==0)) {
    		flash.error(Messages.get("guards.list.noentries"));
    	}
        render();
    }
	
	public static void searchGuards(String searchField) {
    	flash.clear();
    	if (!GeneralMethods.validateParameter(searchField)) {
    		flash.error(Messages.get("guards.list.searcherror"));
    		guardsList(null, false);
    	} else {
    		guardsList(searchField, true);
    	}
    }
	
	public static void guardDetail(Long id) {
		flash.clear();
		if (id!=null) {
			ER_Guard guard = ER_Guard.findById(id);
			if (guard!=null) {
				render(guard);
			}
		}
	}

}
