package com.digitalgeko.servicebus.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRestResponse {

    @JsonProperty("access_token")
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
