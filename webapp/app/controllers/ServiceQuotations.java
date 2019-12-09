package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import extensions.StringFormatExtensions;
import models.ER_Quotation;
import objects.PaymentOption;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class ServiceQuotations extends Controller {

    public static void paymentOptionsForQuotation(Long id) {
    	Map<String, Object> responseMap = new HashMap<String, Object>();
    	responseMap.put("success", false);
    	
    	if (id!=null) {
	        ER_Quotation quotation = ER_Quotation.findById(id);
	        
	        if (quotation!=null) {
	        	List <Map<String, Object>> responseOptions = new ArrayList<Map<String, Object>>();
		        for (PaymentOption option : quotation.quotationDetail.getPaymentOptions()) {
		        	Map<String, Object> map = new HashMap<String, Object>();
		        	Integer numberOfPayments = option.numberOfPayments;
		        	if (numberOfPayments!=null && numberOfPayments >0) {
		        		map.put("value", (option.frecuencyId!=null)?option.frecuencyId:0);
		        		
		        		String currencySymbol = "";
		        		if (quotation.product.currency!=null) {
		        			currencySymbol = quotation.quotationDetail.getCurrencySymbol() + ".";
		        		}
		        		
		        		map.put("name", numberOfPayments + " pago" + ((numberOfPayments>1)?"s":"") + " " + option.frecuency.toLowerCase() + ((numberOfPayments>1)?"es":"") + " - "+ currencySymbol + " " + StringFormatExtensions.decimalFormat(option.amount.doubleValue()) + " - Prima Total " + currencySymbol + StringFormatExtensions.decimalFormat(option.amount.multiply(new BigDecimal(option.numberOfPayments))) );
		        	}
		        	responseOptions.add(map);
		        }
		        
		        responseMap.put("options", responseOptions);
	        	responseMap.put("success", true);
	        }
    	}
    	
        renderJSON(responseMap);
    }
	public static void specialEquipmentQuotation(Long id) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
		boolean equipoEspecial = false;
		responseMap.put("success", false);
		if (id!=null) {
			ER_Quotation quotation = ER_Quotation.findById(id);
			if (quotation != null) {
				JsonObject joQuotationDetail = new JsonParser().parse(quotation.detail).getAsJsonObject();
				JsonArray jaCategory = joQuotationDetail.getAsJsonArray("categories");
				for(int i = 0; i < jaCategory.size(); i++){
					JsonObject joCategoria = jaCategory.get(i).getAsJsonObject();
					if(joCategoria.get("name").getAsString().equals("Coberturas Adicionales")){
						JsonArray jaCosts = joCategoria.getAsJsonArray("costs");
						for(int j = 0; j <jaCosts.size(); j++){
							JsonObject joCost = jaCosts.get(j).getAsJsonObject();
							if(joCost.get("coverageName").getAsString().equals("EQUIPO ESPECIAL")){
								equipoEspecial = true;
								break;
							}
						}
						if(equipoEspecial)
							break;
					}
				}
			}
		}
		responseMap.put("mostrarEquipoEspecial",equipoEspecial);
		renderJSON(responseMap);
	}

}
