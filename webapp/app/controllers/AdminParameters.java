package controllers;

import java.util.ArrayList;
import java.util.List;

import helpers.Filter;
import helpers.GeneralMethods;
import models.ER_Form;
import models.ER_Incident;
import models.ER_Incident_Parameter;
import models.ER_Parameter;
import models.ER_Parameter_Option;
import models.ER_Parameter_Type;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

@With(Secure.class)
@Check("admin@elroble.com")

public class AdminParameters extends AdminBaseController {
	
	public static void saveParameter(@Valid ER_Parameter parameter, @Valid List<ER_Parameter_Option> paramOptions) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("parameter.create.fielderrors"));
    		
    		validation.keep();
    		editParameter(parameter.id);
    	} else {
    		if (parameter.id != null) {
    			setParameterOptions(parameter, paramOptions);
    			parameter.save();
    			flash.success(Messages.get("parameter.edit.success"));
    		} else {
    			ER_Parameter similarValue = ER_Parameter.find("name = ?", parameter.name).first();
    			if (similarValue==null) {
    				Logger.info("Creating parameter: %s", parameter);
    				setParameterOptions(parameter, paramOptions);
    				parameter.save();
					flash.success(Messages.get("parameter.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("parameter.create.duplicate"));
    				editParameter(parameter.id);
    			}
    		}
    	}
    	
    	String name = session.get("name");

		if (name != null) {    			
			searchParameters(name, "",true);
		} else {
	    	parameters(null,true);
		}

    }
	
	private static void setParameterOptions(ER_Parameter parameter, List<ER_Parameter_Option> paramOptions) {
		if (parameter!=null && paramOptions!=null) {
			
			List<ER_Parameter_Option> options = new ArrayList<ER_Parameter_Option>();
			if (parameter.options!=null) {
				parameter.options.clear();
			} else {
				parameter.options = new ArrayList<ER_Parameter_Option>();
			}
			
			if(parameter.type.code == helpers.ERConstants.PARAMETER_TYPE_OPTIONS) {
				for (ER_Parameter_Option option : paramOptions) {
					String value = option.value;
					if (option.id !=null) {
						option = ER_Parameter_Option.findById(option.id);
					}
					
					option.parameter = parameter;
					option.value = value;
					options.add(option);
				}
				
				parameter.options.addAll(options);
			}
		}
	}
	
	public static void editParameter(Long id) {
    	if (id!=null) {
    		ER_Parameter parameter = ER_Parameter.findById(id);
    		renderArgs.put("parameter", parameter);
    	}
    	
    	List<ER_Parameter_Type> types = ER_Parameter_Type.find("order by name").fetch();
    	renderTemplate("AdminForms/editParameter.html", types);
    	//render();
    }
	
	public static void parameters(String name, boolean intern) {
		if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
		
    	//Add forms to the renderArgs
    	filterParameters(name);
    	
    	//Render the list
    	renderTemplate("AdminForms/parameters.html");
    	//render();
    }
	
	public static void searchParameters(String parameter, String all, boolean intern) {
    	
    	flash.clear();
    	session.remove("page");
    	//List all users if the all button is pressed
    	if (!"".equals(all) && all!=null) {
    		session.remove("name");
    		
    		parameters(null,true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
	    	session.put("name",parameter);
    	}
    	parameters(parameter, true);
    }
	
	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterParameters(String parameter) {
		
		//Validate the parameters
		boolean validParameter = GeneralMethods.validateParameter(parameter);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validParameter) {
			filter.addQuery("name like ?", "%"+parameter+"%");
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
		ModelPaginator parameters = null;
		if (filter.isValidFilter()) {			
			parameters = new ModelPaginator(ER_Parameter.class, filter.getQuery(), filter.getParametersArray());
		} else {
			parameters = new ModelPaginator(ER_Parameter.class);
		}
		
		parameters.orderBy("parameterOrder ASC, name ASC");
		parameters.setPageNumber(page);
		parameters.setPageSize(20);
		renderArgs.put("parameters", parameters);
	}

}
