package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.ERConstants;
import helpers.Filter.Operator;
import models.*;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class ServiceVehicles extends AdminBaseController {

	public static void vehicleLinesForBrandString(String brand) {
		Map<String, Object> response =new HashMap<String, Object>();


//    	List<ER_Vehicle_Line> vehicleLines = ER_Vehicle_Line.find("select distinct v.line from ER_Vehicle_Value v where v.year != null and v.line.brand.id = ? and v.line.insurable = 1 and v.line.vehicleClass IS NOT NULL order by v.line.name", id).fetch();
		ER_Vehicle_Brand vehicleBrands = ER_Vehicle_Brand.find("select distinct v from ER_Vehicle_Brand as v where v.name = ? ", brand).first();


		List<ER_Vehicle_Line> vehicleLines = ER_Vehicle_Line.find("select distinct v from ER_Vehicle_Line as v where v.brand.id = ? and v.insurable = 1 and v.transferCode is not null order by v.name", vehicleBrands.getId()).fetch();
		if (vehicleLines!=null) {
			List<Map<String, String>> lines = new ArrayList<Map<String, String>>();
			for (ER_Vehicle_Line line : vehicleLines) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", Long.toString(line.id));
				map.put("name", line.name);
				lines.add(map);
			}
			response.put("lines", lines);
			response.put("success", true);
		} else {
			response.put("success", false);
		}

		renderJSON(response);
	}

	public static void vehicleLinesForBrand(Long id) {
    	Map<String, Object> response =new HashMap<String, Object>();

    	
//    	List<ER_Vehicle_Line> vehicleLines = ER_Vehicle_Line.find("select distinct v.line from ER_Vehicle_Value v where v.year != null and v.line.brand.id = ? and v.line.insurable = 1 and v.line.vehicleClass IS NOT NULL order by v.line.name", id).fetch();
    	List<ER_Vehicle_Line> vehicleLines = ER_Vehicle_Line.find("select distinct v from ER_Vehicle_Line as v where v.brand.id = ? and v.insurable = 1 and v.transferCode is not null", id).fetch();
    	if (vehicleLines!=null) {
    		List<Map<String, String>> lines = new ArrayList<Map<String, String>>();
    		for (ER_Vehicle_Line line : vehicleLines) {
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("id", Long.toString(line.id));
    			map.put("name", line.name);
    			lines.add(map);
    		}
    		response.put("lines", lines);
    		response.put("success", true);
    	} else {
    		response.put("success", false);
    	}
    	
    	renderJSON(response);
    }
	
	public static void vehicleValuesForLine(Long id) {
    	Map<String, Object> response =new HashMap<String, Object>();

    	List<ER_Vehicle_Value> vehicleValues = ER_Vehicle_Value.find("year != null and line.id = ? and line.insurable = 1 line.vehicleClass IS NOT NULL order by year asc", id).fetch();
    	if (vehicleValues!=null) {
    		List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    		for (ER_Vehicle_Value value : vehicleValues) {
    			Map<String, String> map = new HashMap<String, String>();
    			map.put("id", Long.toString(value.id));
    			map.put("year", value.year.toString());
    			values.add(map);
    		}
    		response.put("values", values);
    		response.put("success", true);
    	} else {
    		response.put("success", false);
    	}
    	
    	renderJSON(response);
    }
	
	public static Map<String, Object> parametersMapForProduct(Long id) {
		Map<String, Object> response =new HashMap<String, Object>();

		ER_Product product = null;
		if (id!=null) {
			product = ER_Product.findById(id);
		}
		String currencySymbol = "";
		BigDecimal exchangeRate = null;
		if (product!=null && product.currency!=null) {
			currencySymbol = product.currency.symbol;
			exchangeRate = product.currency.exchangeRate;
			
			if(connectedUser().role.code != ERConstants.USER_ROLE_FINAL_USER){
				response.put("discountAvailable", product.hasAvailableDiscount());
			}
			
			if (product.hasFacultative !=null) {
				response.put("facultative", product.hasFacultative);
			}
		}
		
		if (exchangeRate == null) {
			exchangeRate = BigDecimal.ONE;
		}
		
    	List<ER_Product_Coverage> productCoverages = ER_Product_Coverage.find("product.id = ? order by coverage.category.id", id).fetch();
    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	
    	if (productCoverages!=null) {
    		List<Map<String, Object>> fields = new ArrayList<Map<String, Object>>();
    		
    		boolean requestCarValue = false;
    		
    		for (ER_Product_Coverage productCoverage : productCoverages) {
    			
    			if (productCoverage.coverage.equals(configuration.thirdInjuriesCoverage)) {
    				continue;
    			}
    			
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.put("id", productCoverage.id);
    			map.put("name", productCoverage.coverage.fieldDescription.toUpperCase());
    			map.put("as_code",productCoverage.coverage.transferCode);
    			
    			if (productCoverage.optional!=null) {
    				map.put("optional", productCoverage.optional);
    			}
    			
    			if (productCoverage.coverage.equals(configuration.theftCoverage)) {
    				map.put("leyend", Messages.get("quotation.form.quotation.theftcoveragleyend", configuration.partialTheftPercentage, "%"));
    			}
    			
    			if (productCoverage.isField()) {
    				map.put("field", true);
    			}
    			
    			if (productCoverage.valueBase !=null && productCoverage.valueBase.code == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE) {
    				requestCarValue = true;
    			}
    			
    			List<Map<String, Object>> options = productCoverage.getOptions();
    			if (options!=null) {
    				map.put("options", options);
    			}
    			
    			fields.add(map);
    		}
    		
    		if (requestCarValue) {
    			Map<String, Object> map = new HashMap<String, Object>();
    			map.put("name", Messages.get("quotation.form.quotation.carValue"));
    			map.put("field", true);
    			map.put("carValue", true);
    			fields.add(0, map);
    		}
    		
    		response.put("currency", currencySymbol);
    		response.put("exchangeRate", exchangeRate);
    		response.put("fields", fields);
    		response.put("success", true);
    	} else {
    		response.put("success", false);
    	}
    	
    	return response;
	}
	
	public static void parametersForProduct(Long id) {
    	renderJSON(parametersMapForProduct(id));
    }

}
