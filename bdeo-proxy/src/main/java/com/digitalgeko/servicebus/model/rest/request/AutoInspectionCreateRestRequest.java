package com.digitalgeko.servicebus.model.rest.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoInspectionCreateRestRequest {

    @JsonProperty("case")
    private String caseNumber;
    private String phone;
    @JsonProperty("insured_name")
    private String insuredName;
    @JsonProperty("insured_surname")
    private String insuredSurname;
    @JsonProperty("identificationDocument")
    private String identificationDocument;
    @JsonProperty("registration_number")
    private String plate;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getInsuredSurname() {
        return insuredSurname;
    }

    public void setInsuredSurname(String insuredSurname) {
        this.insuredSurname = insuredSurname;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }
}
