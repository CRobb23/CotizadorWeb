package controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import helpers.ERConstants;
import helpers.Filter;
import helpers.GeneralMethods;
import models.ER_Base_Field;
import models.ER_Calculation_Type;
import models.ER_Coverage;
import models.ER_Coverage_Category;
import models.ER_Vehicle_Class;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminCoverages extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters the coverages and adds the result as a ModelPaginator object with the key "coverages"
	 * ************************************************************************************************************************
	 */
	
	private static void filterCoverages (String coverage) {
		//Validate the parameters
		boolean validCoverage = GeneralMethods.validateParameter(coverage);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validCoverage) {
			filter.addQuery("name like ?", "%"+coverage+"%");
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
		
		//Validate the filter to create the model paginator. If the filter is invalid, all users are returned.
		ModelPaginator coverages = null;
		if (filter.isValidFilter()) {			
			coverages = new ModelPaginator(ER_Coverage.class, filter.getQuery(), filter.getParametersArray());
		} else {
			coverages = new ModelPaginator(ER_Coverage.class);
		}
		
		coverages.setPageNumber(page);
		coverages.setPageSize(20);
		coverages.orderBy("category.id, coverageOrder, name ASC");
		renderArgs.put("coverages", coverages);
	}
	
	/*
	 * ************************************************************************************************************************
	 * Coverages list
	 * ************************************************************************************************************************
	 */
	
    public static void coveragesList(boolean intern) {
    	
    	if (!intern) {
    		session.remove("page");
    	}
    	
    	//Add all coverages to the list
    	filterCoverages(null);
    	
    	render();
    }
    
    /*
	 * ************************************************************************************************************************
	 * Coverage creation and editing
	 * ************************************************************************************************************************
	 */

    public static void editCoverage(Long id) {
    	List<ER_Coverage_Category> categories = ER_Coverage_Category.findAll();
    	renderArgs.put("categories", categories);
    	
    	List<ER_Calculation_Type> calculationTypes = ER_Calculation_Type.findAll();
    	renderArgs.put("calculationTypes", calculationTypes);
    	
    	if (id!=null) {
    		ER_Coverage coverage = ER_Coverage.findById(id);
    		renderArgs.put("coverage", coverage);
    	}
    	
    	render();
    }
    
    public static void saveCoverage(@Valid ER_Coverage coverage) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("brand.create.fielderrors"));
    		validation.keep();
    		editCoverage(coverage.id);
    	} else {
    		
    		if (coverage.id == null) {
    			flash.success(Messages.get("coverage.create.success"));
    		} else {
    			flash.success(Messages.get("coverage.edit.success"));
    		}
    		
    		coverage.save();
    	}
    	
    	coveragesList(true);
    }
}
