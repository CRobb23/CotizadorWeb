package controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ER_Geographic_Area;
import play.mvc.Controller;
import play.mvc.With;

@With(Secure.class)
public class GeographicArea extends Controller {

	public static void getGeographicAreaChildren(Long id,String tipo) {
    	Map<String, Object> response = new HashMap<String, Object>();
    	List<ER_Geographic_Area> areas;
    	if(tipo.contains("zone")) {
    		areas = ER_Geographic_Area.find("father.id = ? and active = 1 order by transfer_code asc", id).fetch();
    	}else {
    		areas = ER_Geographic_Area.find("father.id = ? and active = 1 order by name asc", id).fetch();
    	}
    	if(!areas.isEmpty()){
    		response.put("areas", areas);
    		response.put("success", true);
    	} else {
    		response.put("success", false);
    	}
    	
    	renderJSON(response);
    }
}
