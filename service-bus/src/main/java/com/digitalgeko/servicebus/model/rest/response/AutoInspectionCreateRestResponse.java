package com.digitalgeko.servicebus.model.rest.response;

public class AutoInspectionCreateRestResponse {

    private Boolean success;
    private String message;
    private Integer inspectionNumber;

    public void setSuccess(Boolean success) {
        this.success = success;
    }
    public Boolean getSuccess() {
        if (success == null) {
            success = Boolean.FALSE;
        }
        return success;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getInspectionNumber() {
        return inspectionNumber;
    }

    public void setInspectionNumber(Integer inspectionNumber) {
        this.inspectionNumber = inspectionNumber;
    }
}
