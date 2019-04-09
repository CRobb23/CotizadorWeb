package com.digitalgeko.servicebus.outgoing.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
    private RestTemplate restTemplate;

    protected static final Logger log = LoggerFactory.getLogger(BdeoInspectionServiceRestOutbound.class);

    @Autowired
    public BdeoInspectionServiceRestOutbound(@Value("${bdeo.ws.defaulturi}") String defaultUri, RestTemplate restTemplate) {
        BASE_URL = defaultUri;
        this.restTemplate = restTemplate;
    }

    public String login(String message) {
        log.info("LOGIN REQUEST: " + message);
        String response = restTemplate.postForObject(BASE_URL + LOGIN_URL, message, String.class);
        log.info("LOGIN RESPONSE: " + response);
        return response;
    }

    public String createAutoInspection(String token, String message) {
        log.info("CREATE REQUEST: " + getFullUrl(null) + " MSG: " + message);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(message ,headers);
        String response = restTemplate.postForObject(getFullUrl(null), entity, String.class, getUriParams(token));
        log.info("CREATE RESPONSE: " + response);
        return response;
    }

    public String queryAutoInspection(String token, String id) {
        log.info("QUERY REQUEST: " + getFullUrl(id));
        String response = restTemplate.getForObject(getFullUrl(id), String.class, getUriParams(token));
        log.info("QUERY RESPONSE: " + response);
        return response;
    }

    public void deleteAutoInspection(String token, String id) {
        log.info("DELETE REQUEST: " + getFullUrl(id));
        restTemplate.delete(getFullUrl(id), getUriParams(token));
        log.info("DELETE RESPONSE: DONE");
    }

    public void updateAutoInspection(String token, String id, String message) {
        log.info("UPDATE REQUEST: " + getFullUrl(id));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity(message ,headers);
        restTemplate.put(getFullUrl(id), entity, getUriParams(token));
        log.info("UPDATE RESPONSE: DONE");
    }

    private Map<String, String> getUriParams(String token) {
        Map<String, String> uriParams = new HashMap();
        uriParams.put("token", token);
        return uriParams;
    }

    private String getFullUrl(String id) {
        if (id != null) {
            return BASE_URL + SELF_ADJUST_URL + "/" + id + SELF_ADJUST_PARAM;
        } else {
            return BASE_URL + SELF_ADJUST_URL + SELF_ADJUST_PARAM;
        }
    }

}
