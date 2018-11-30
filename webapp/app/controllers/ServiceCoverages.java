package controllers;

import helpers.ERConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import models.ER_Base_Field;
import models.ER_Coverage;
import play.Logger;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class ServiceCoverages extends Controller {

	public static void getCoverages(Long id) {
    	
    	List<ER_Coverage> coverages = ER_Coverage.find("category.id = ? order by coverageOrder", id).fetch();
    	if (coverages == null || coverages.isEmpty()) {
    		String message = "";
    		if (coverages==null) {
    			message = Messages.get("product.coverage.error");
    		} else {
    			message = Messages.get("product.coverage.empty");
    		}
    		
    		renderJSON("{\"success\":false, \"message\":\""+message+"\"}");
    	}

    	StringBuilder builder = new StringBuilder();
    	builder.append("[");
    	
    	for (Iterator<ER_Coverage> iterator = coverages.iterator(); iterator.hasNext();) {
			ER_Coverage coverage = iterator.next();
			
			builder.append("{");
			builder.append("\"id\":").append(coverage.id).append(",");
			builder.append("\"name\":\"").append(coverage.name).append("\",");
			builder.append("\"description\":\"").append(coverage.description.replace("\"", "\\\"")).append("\",");
			builder.append("\"quotationName\":\"").append(coverage.quotationName).append("\",");
			builder.append("\"quotationDescription\":\"").append(coverage.quotationDescription).append("\",");
			builder.append("\"fieldDescription\":\"").append(coverage.fieldDescription).append("\",");
			//TODO
			//Type
			//Category
			builder.append("\"coverageOrder\":").append(coverage.coverageOrder).append(",");
			builder.append("\"external\":").append(coverage.externals).append(",");
			builder.append("\"partOfNetPrime\":").append(coverage.partOfNetPrime).append(",");
			builder.append("\"applyDiscount\":").append(coverage.applyDiscount);
    		builder.append("}");
    		
    		if (iterator.hasNext()) {
    			builder.append(",");
    		}
		}
    	
    	builder.append("]");
    	
    	String stringBuilder = escapeStringCharacters(builder.toString());
    	
    	//TODO escape special caracteres like new line
    	renderJSON("{\"success\":true, \"coverages\":"+stringBuilder+"}");
    }
	
	public static String escapeStringCharacters(String builder){
		String escapedString = builder.replace("\n", "\\n").replace("\r", "\\r");
		
		return escapedString;
	}
    
    public static void getCoveragesFields(Long[] ids) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	
    	try {
	    	List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
	    	List<ER_Base_Field> baseFields = ER_Base_Field.find("order by code").fetch();
	    	
	    	for (Long id : ids) {
	    		Map<String, Object> object = new HashMap<String, Object>();
	    		ER_Coverage coverage = ER_Coverage.findById(id);
	    		object.put("id", id);
	    		object.put("multiple", coverage.canHaveMultipleValues());
	    		object.put("coverageName", coverage.name);
	    		object.put("category", coverage.category.id);
	    		object.put("fields", coverage.type.code);
	    		switch (coverage.type.code) {
	    			case ERConstants.CALCULATION_TYPE_RANGE: {
	    				object.put("fieldTitle", Messages.get("coverage.type.field.ranges"));
	    			}
	    				break;
	    			case ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS: {
	    				object.put("fieldTitle", Messages.get("coverage.type.field.amounts"));
	    			}
	    				break;
	    			case ERConstants.CALCULATION_TYPE_OPTIONS: { 
	    				object.put("fieldTitle", Messages.get("coverage.type.field.options"));
	    			}
	    				break;
	    		}
	    		
	    		//Set allowed base fields
	    		List<Map<String, Object>> baseFieldMaps = new ArrayList<Map<String, Object>>();
	    		for (ER_Base_Field base : baseFields) {
	    			if (base.appliesForType(coverage.type.code, coverage.category.code)) {
	    				baseFieldMaps.add(base.toMap());
	    			}
	    		}
	    		
	    		object.put("baseFields", baseFieldMaps);
	    		
	    		fields.add(object);
	    		result.put("success", true);
	    	}
	    	
	    	result.put("fields", fields);
	    	
    	} catch (Exception e) {
    		Logger.error(e, "Error getting coverage fields");
    		if (result.containsKey("classes")) {
    			result.remove("classes");
    		}
    		result.put("success", false);
    	}
    	
    	renderJSON(result);
    }
    
//    public static Li

}
