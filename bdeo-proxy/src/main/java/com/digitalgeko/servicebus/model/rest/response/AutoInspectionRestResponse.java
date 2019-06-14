package com.digitalgeko.servicebus.model.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AutoInspectionRestResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("case_id")
    private String caseNumber;
    @JsonProperty("registration_number")
    private String plate;
    @JsonProperty("created_by")
    private String createdByName;
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("updatedBy")
    private String updatedById;
    @JsonProperty("createdBy")
    private String createdById;

    @JsonProperty("goodConditions")
    private String goodConditions;
    @JsonProperty("carYears")
    private String carYears;
    @JsonProperty("art908")
    private String article908;

    @JsonProperty("numRef")
    private String numRef;
    @JsonProperty("caseRef")
    private String caseRef;
    @JsonProperty("insured_name")
    private String insuredName;
    @JsonProperty("isOpen")
    private String isOpen;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("model")
    private String model;
    @JsonProperty("grade")
    private String grade;
    @JsonProperty("master_company_id")
    private String masterCompanyId;
    @JsonProperty("driving_license")
    private String licenseNumber;
    @JsonProperty("brand")
    private String brand;
    @JsonProperty("createdAt")
    private Long createdDate;
    @JsonProperty("prefixInput")
    private String prefixInput;
    @JsonProperty("company_name")
    private String companyName;
    @JsonProperty("line")
    private String line;
    @JsonProperty("insured_surname")
    private String insuredSurname;
    @JsonProperty("email")
    private String email;
    @JsonProperty("circulationOwner")
    private String owner;
    @JsonProperty("color")
    private String color;
    @JsonProperty("typeCar")
    private String carType;
    @JsonProperty("useCar")
    private String carUse;
    @JsonProperty("chassis")
    private String vin;
    @JsonProperty("engine")
    private String engine;
    @JsonProperty("kmCar")
    private String mileage;
    @JsonProperty("updatedAt")
    private Long updatedDate;
    @JsonProperty("images")
    private List<String> images;
    @JsonProperty("report")
    private String report;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getGoodConditions() {
        return goodConditions;
    }

    public void setGoodConditions(String goodConditions) {
        this.goodConditions = goodConditions;
    }

    public String getCarYears() {
        return carYears;
    }

    public void setCarYears(String carYears) {
        this.carYears = carYears;
    }

    public String getArticle908() {
        return article908;
    }

    public void setArticle908(String article908) {
        this.article908 = article908;
    }

    public String getNumRef() {
        return numRef;
    }

    public void setNumRef(String numRef) {
        this.numRef = numRef;
    }

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(String isOpen) {
        this.isOpen = isOpen;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getMasterCompanyId() {
        return masterCompanyId;
    }

    public void setMasterCompanyId(String masterCompanyId) {
        this.masterCompanyId = masterCompanyId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public String getPrefixInput() {
        return prefixInput;
    }

    public void setPrefixInput(String prefixInput) {
        this.prefixInput = prefixInput;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getInsuredSurname() {
        return insuredSurname;
    }

    public void setInsuredSurname(String insuredSurname) {
        this.insuredSurname = insuredSurname;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getCarUse() {
        return carUse;
    }

    public void setCarUse(String carUse) {
        this.carUse = carUse;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
