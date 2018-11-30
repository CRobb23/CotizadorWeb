package controllers;

import static play.modules.pdf.PDF.renderPDF;

import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import org.allcolor.yahp.converter.IHtmlToPdfTransformer;

import helpers.Filter;
import helpers.GeneralMethods;
import models.ER_Channel;
import models.ER_Distributor;
import models.ER_Form;
import models.ER_Form;
import models.ER_Incident;
import models.ER_Incident_Parameter;
import models.ER_Parameter;
import models.ER_Parameter_Type;
import models.ER_Product;
import models.ER_Store;
import models.ER_User;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.modules.pdf.PDF.Options;
import play.mvc.With;

@With(Secure.class)
@Check("admin@elroble.com")

public class AdminForms extends AdminBaseController {
	
	public static void saveForm(@Valid ER_Form form, Long formId) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("form.create.fielderrors"));
    		validation.keep();
    		editForm(form.id);
    	} else {
    		if (formId != null) {
    			ER_Form currentForm = ER_Form.findById(formId);
    			currentForm.name = form.name;
    			currentForm.templatePath = form.templatePath;
    			
    			currentForm.parameters = null;
    			currentForm.save();
    			
    			List<ER_Parameter> formParams = new ArrayList<ER_Parameter>();
    			if(form.parameters != null) {
    				for(ER_Parameter param : form.parameters)
    					formParams.add(param);
    			}
    			currentForm.parameters = formParams;
    			currentForm.save();
    			flash.success(Messages.get("form.edit.success"));
    			
    		} else {
    			ER_Form similarValue = ER_Form.find("name = ?", form.name).first();
    			
    			if (similarValue==null) {
    				form.save();
					flash.success(Messages.get("form.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("form.create.duplicate"));
    				editForm(form.id);
    			}
    		}
    	}
    	
    	String sessionForm = session.get("form");

		if (sessionForm != null) {    			
			searchForms(sessionForm, "", true);
		} else {
	    	forms(null, true);
		}
    }
	
	public static void editForm(Long id) {
		
		List<ER_Parameter> formParams = ER_Parameter.find("order by parameterOrder").fetch();
		
    	if (id!=null) {
    		ER_Form form = ER_Form.findById(id);
    		
    		if(form != null && form.parameters != null) {
    			formParams.removeAll(form.parameters);
    		}
    		
    		renderArgs.put("form", form);
    		renderArgs.put("assigned", form.parameters);
    	}
    	
    	renderArgs.put("formParams", formParams);
    	
    	render();
    }
	
	public static void forms(String name, boolean intern) {
		
		if (!intern) {
    		session.remove("page");
    		session.remove("name");
    	}
    	
    	//Add forms to the renderArgs
    	filterForms(name);
    	
    	//Render the list
    	render();
    }
	
	public static void searchForms(String form, String all, boolean intern) {
    	
    	flash.clear();
    	
    	//List all users if the all button is pressed
    	session.remove("page");
    	if (!"".equals(all) && all!=null) {
    		session.remove("form");
    		
    		forms(null, true);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("form",form);
    	}
    	forms(form, intern);
    }
	
	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterForms(String form) {
		
		//Validate the parameters
		boolean validForm = GeneralMethods.validateParameter(form);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validForm) {
			filter.addQuery("name like ?", "%"+form+"%");
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
		ModelPaginator forms = null;
		if (filter.isValidFilter()) {			
			forms = new ModelPaginator(ER_Form.class, filter.getQuery(), filter.getParametersArray());
		} else {
			forms = new ModelPaginator(ER_Form.class);
		}
		
		forms.orderBy("name ASC");
		forms.setPageNumber(page);
		forms.setPageSize(20);
		renderArgs.put("forms", forms);
	}
}
