package controllers;

import helpers.ERConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ER_Distributor;
import models.ER_User;
import play.mvc.*;

@With(Secure.class)
public class ServiceChannels extends Controller {

	public static void administratorsForChannel(Long id) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
    	
    	if (id!=null) {
	        List <ER_User> administrators = ER_User.find("role.code = ? and active = true and channel.id=? order by firstName", ERConstants.USER_ROLE_CHANNEL_MANAGER, id).fetch();
	        
	        if (administrators!=null) {
	        	List <Map<String, Object>> responseAdministrators = new ArrayList<Map<String, Object>>();
		        for (ER_User admin : administrators) {
		        	Map<String, Object> map = new HashMap<String, Object>();
		        	map.put("id", admin.id);
		        	map.put("name", admin.getFullName());
		        	map.put("email", admin.email);
		        	responseAdministrators.add(map);
		        }
		        
		        responseMap.put("administrators", responseAdministrators);
	        }
	        responseMap.put("success", true);
    	} else {
    		responseMap.put("success", false);
    	}
    	
        renderJSON(responseMap);
    }
	
	public static void administratorsAndSellersForDistributor(Long id) {
		Map<String, Object> responseMap = new HashMap<String, Object>();
    	
    	if (id!=null) {
	        List <ER_User> administrators = ER_User.find("role.code = ? and active = true and distributor.id=? order by firstName", ERConstants.USER_ROLE_SUPERVISOR, id).fetch();
	        List <ER_User> sellers = ER_User.find("role.code = ? and active = true and distributor.id=? order by firstName", ERConstants.USER_ROLE_SALES_MAN, id).fetch();
	        
	        if (administrators!=null) {
	        	List <Map<String, Object>> responseAdministrators = new ArrayList<Map<String, Object>>();
		        for (ER_User admin : administrators) {
		        	Map<String, Object> map = new HashMap<String, Object>();
		        	map.put("id", admin.id);
		        	map.put("name", admin.getFullName());
		        	map.put("email", admin.email);
		        	responseAdministrators.add(map);
		        }
		        responseMap.put("administrators", responseAdministrators);
	        }
	        
	        if (sellers!=null) {
	        	List <Map<String, Object>> responseSellers = new ArrayList<Map<String, Object>>();
		        for (ER_User seller : sellers) {
		        	Map<String, Object> map = new HashMap<String, Object>();
		        	map.put("id", seller.id);
		        	map.put("name", seller.getFullName());
		        	responseSellers.add(map);
		        }
		        responseMap.put("sellers", responseSellers);
	        }
	        
	        responseMap.put("success", true);
    	} else {
    		responseMap.put("success", false);
    	}
    	
        renderJSON(responseMap);
    }
	
    public static void distributorsForChannel(Long id) {
    	
    	Map<String, Object> responseMap = new HashMap<String, Object>();
    	
    	if (id!=null) {
	        List <ER_Distributor> distributors = ER_Distributor.find("active = true and channel.id = ? order by name", id).fetch();
	        
	        if (distributors!=null) {
	        	List <Map<String, Object>> responsedistributors = new ArrayList<Map<String, Object>>();
		        for (ER_Distributor distributor : distributors) {
		        	Map<String, Object> map = new HashMap<String, Object>();
		        	map.put("id", distributor.id);
		        	map.put("name", distributor.name);
		        	responsedistributors.add(map);
		        }
		        
		        responseMap.put("distributors", responsedistributors);
	        }
	        responseMap.put("success", true);
    	} else {
    		responseMap.put("success", false);
    	}
    	
        renderJSON(responseMap);
    }

}
