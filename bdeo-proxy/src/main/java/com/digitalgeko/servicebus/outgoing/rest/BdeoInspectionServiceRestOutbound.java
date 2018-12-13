package com.digitalgeko.servicebus.outgoing.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class BdeoInspectionServiceRestOutbound {

    private static final String LOGIN_URL = "/login";
    private static final String SELF_ADJUST_URL = "/selfadjust";
    private static final String SELF_ADJUST_PARAM = "?access_token={token}";
    private static String BASE_URL = "";

    @Autowired
    public BdeoInspectionServiceRestOutbound(@Value("${bdeo.ws.defaulturi}") String defaultUri) {
        BASE_URL = defaultUri;
    }

    public String login(String message) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(BASE_URL + LOGIN_URL, message, String.class);
        return response;
    }

    public String createAutoInspection(String token, String message) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(getFullUrl(null), message, String.class, getUriParams(token));
        return response;
    }

    public String queryAutoInspection(String token, String id) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(getFullUrl(id), String.class, getUriParams(token));
        return response;
    }

    public void deleteAutoInspection(String token, String id) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(getFullUrl(id), getUriParams(token));
    }

    public void updateAutoInspection(String token, String id, String message) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put(getFullUrl(id), message, getUriParams(token));
    }

    private Map<String, String> getUriParams(String token) {
        Map<String, String> uriParams = new HashMap();
        uriParams.put("token", token);
        return uriParams;
    }

    private String getFullUrl(String id) {
        if (id != null) {
            return BASE_URL + SELF_ADJUST_URL + SELF_ADJUST_PARAM;
        } else {
            return BASE_URL + SELF_ADJUST_URL + "/" + id + SELF_ADJUST_PARAM;
        }
    }

}
