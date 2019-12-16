package helpers;

import java.util.*;

import models.*;
import controllers.Security;
import objects.LoJackOptions;
import play.Logger;
import play.mvc.Scope.Params;

public class GeneralMethods {
	
	/*
	 * ************************************************************************************************************************
	 * Validates the fields based on their types
	 * ************************************************************************************************************************
	 */
	
	public static boolean validateParameter(String param){
		return (param == null) ? false : !param.isEmpty();
	}
	
	public static boolean validateParameter(Integer param){
		return (param == null) ? false : ((param == -1) ? false : true);
	}
	
	public static boolean validateParameter(Long param){
		return (param == null) ? false : ((param == -1) ? false : true);
	}
	
	public static <V, K> boolean validateParameter(Map<K, V> mapa){
		return (mapa == null) ? false : ((mapa.size() == 0) ? false : true);
	}
	
	public static boolean validateParameter(Boolean param){
		return (param == null) ? false : true;
	}

    public static List<Map<String, Object>> getAvailableFrecuencies() {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String previousName = null;
        Map<String, Object> previousMap = null;
        ArrayList<ER_Payment_Frecuency> previousList= null;
        List<ER_Payment_Frecuency> frecuencies = ER_Payment_Frecuency.find("fractioningFee is not null and frecuency is not null and numberOfPayments > 0 ORDER BY frecuency.frecuencyInYear, numberOfPayments").fetch();
        for (ER_Payment_Frecuency frecuency : frecuencies) {

            if (previousName != frecuency.frecuency.name) {
                previousName = frecuency.frecuency.name;
                previousList = new ArrayList<ER_Payment_Frecuency>();
                previousMap = new HashMap<String, Object>();
                previousMap.put("name", previousName);
                previousMap.put("list", previousList);
                list.add(previousMap);
            }

            previousList.add(frecuency);
        }

        Logger.debug("List: %s", list);

        return list;
    }
    
    public static String getPhoneNumbersString(Params params){
    	if(params.getAll("labelPhoneNumber") != null){
    		StringBuilder phoneNumbers = new StringBuilder();
    		for(Integer i=1; i<params.getAll("labelPhoneNumber").length; i++){
    			String etiquetaPhoneNumber = params.getAll("labelPhoneNumber")[i];
    			String phoneNumber = params.getAll("phoneNumber")[i];
    			phoneNumbers.append(etiquetaPhoneNumber).append(":").append(phoneNumber).append(",");
    		}
    		return phoneNumbers.length() > 0 ? phoneNumbers.deleteCharAt(phoneNumbers.length()-1).toString() : "";
    	}
    	return null;
    }

    public static List<LoJackOptions> getAvailableLoJacks(Long loJackId) {
	    List<LoJackOptions> options = new ArrayList<>();
	    if (loJackId != null) {
            ER_Vehicle_LoJack loJack = ER_Vehicle_LoJack.findById(loJackId);
            for (int i = 1; i<= 4; i++) {
                LoJackOptions option = new LoJackOptions();
                option.number = i;
                option.description = LoJackOptions.getDescription(i);
                option.recharge = LoJackOptions.getRecharge(i, loJack);
                option.overprime = LoJackOptions.getOverprime(i, loJack);
                option.operation = LoJackOptions.getOperation(i);
                options.add(option);
            }
        }
        return options;
    }
}
