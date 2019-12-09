package objects;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by GEKO on 10/17/19.
 */
public class CoverageYoungerData {
    public String name;
    public String license;
    public String birthDate;

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String,Object>();
        if (name!= null)
            map.put("name",name);
        if (license != null)
            map.put("license",license);
        if (birthDate != null)
            map.put("birthDate", birthDate);

        return map;
    }
}
