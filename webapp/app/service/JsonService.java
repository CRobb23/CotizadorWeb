package service;

public interface JsonService {

    String toJson(Object object);
    Object getAsJson(String jsonString, Class classObject);

}
