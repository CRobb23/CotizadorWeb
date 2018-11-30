package com.digitalgeko.servicebus.outgoing.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AutoInspectionServiceRestOutbound {

    private static final String CONSUME_URL = "/InspectionsController/finishAutoInspection";
    private static String BASE_URL = "";

    @Autowired
    public AutoInspectionServiceRestOutbound(@Value("${rest.ws.defaulturi}") String defaultUri) {
        BASE_URL = defaultUri;
    }

    public String updateAutoInspectionState(String message) {
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject(BASE_URL + CONSUME_URL, message, String.class);
        return response;
    }
}
