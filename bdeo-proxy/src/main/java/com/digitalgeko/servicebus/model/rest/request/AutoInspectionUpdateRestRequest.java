package com.digitalgeko.servicebus.model.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoInspectionUpdateRestRequest {

    private String id;
    private Integer status;
    private Integer overExcess;
    private String comments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
