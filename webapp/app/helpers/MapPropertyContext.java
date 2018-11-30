package helpers;

import java.util.HashMap;
import java.util.Map;

import be.objectify.led.PropertyContext;

public class MapPropertyContext implements PropertyContext {
	
	private Map<String, String> data = new HashMap<String, String>();

    public void setValue(String name, String value) {
        data.put(name, value);
    }

    public String getValue(String name) {
        return data.get(name);
    }
}
