package com.digitalgeko.servicebus.model.rest.response;

public class AutoInspectionCreateRestResponse {

    private Boolean success;
    private String message;
    private String number;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
