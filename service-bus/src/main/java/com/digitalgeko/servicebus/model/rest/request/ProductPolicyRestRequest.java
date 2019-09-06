package com.digitalgeko.servicebus.model.rest.request;

public class ProductPolicyRestRequest {

    private String id;

    private String status;

    private String name;

    private String description;

    private String policyFrom;

    private String policyTo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPolicyFrom() {
        return policyFrom;
    }

    public void setPolicyFrom(String policyFrom) {
        this.policyFrom = policyFrom;
    }

    public String getPolicyTo() {
        return policyTo;
    }

    public void setPolicyTo(String policyTo) {
        this.policyTo = policyTo;
    }
}
