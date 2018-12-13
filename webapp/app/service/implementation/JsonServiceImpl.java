package service.implementation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import play.Logger;
import play.modules.guice.InjectSupport;
import service.JsonService;

@InjectSupport
public class JsonServiceImpl implements JsonService {

    private final Gson gson;

    @Inject
    public JsonServiceImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public String toJson(Object object) {
        Gson gson = new GsonBuilder().setDateFormat("dd/MM/YYYY HH:mm:ss").excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(object);
    }

    @Override
    public Object getAsJson(String jsonString, Class classObject) {
        Object object = null;
        try {
            object = gson.fromJson(jsonString, classObject);
        } catch (Exception ex) {
            Logger.info(ex, ex.getMessage());
            Logger.error("backend" , " convirtiendo request a persistence " , " persistence invalido " , ex);
        }
        return object;
    }

}