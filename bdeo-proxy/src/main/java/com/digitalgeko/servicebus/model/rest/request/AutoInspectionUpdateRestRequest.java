package com.digitalgeko.servicebus.model.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoInspectionUpdateRestRequest {

    @JsonProperty("status")
    private Integer status;
    @JsonProperty("over_excess")
    private Integer overExcess;
    @JsonProperty("comments")
    private String comments;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOverExcess() {
        return overExcess;
    }

    public void setOverExcess(Integer overExcess) {
        this.overExcess = overExcess;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
