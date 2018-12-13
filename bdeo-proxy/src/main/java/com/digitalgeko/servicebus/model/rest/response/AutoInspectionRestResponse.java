package com.digitalgeko.servicebus.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AutoInspectionRestResponse {

    private String id;
    @JsonProperty("case_id")
    private String caseId;
    private String caseRef;
    private String address;
    @JsonProperty("master_company_id")
    private String masterCompanyId;
    @JsonProperty("company_name")
    private String companyName;
    private Boolean isOpen;
    private Integer status;
    private String comments;
    @JsonProperty("createdBy")
    private String createdById;
    @JsonProperty("created_by")
    private String createdByName;
    @JsonProperty("updatedBy")
    private String updatedById;
    @JsonProperty("createdAt")
    private Long createdDate;
    @JsonProperty("updatedAt")
    private Long updatedDate;

    private List<InspectionImage> images;

    @JsonProperty("chassis")
    private String vin;
    @JsonProperty("circulationOwner")
    private String owner;
    private String color;
    private String dpi;
    @JsonProperty("drivingLicense")
    private String licenseNumber;
    @JsonProperty("drivingLicenseTypes")
    private String licenseType;
    private String email;
    private String engine;
    private Boolean goodConditions;
    private Boolean haveDocs;
    private Boolean havePhone;
    private Integer grade;
    private Boolean carYears;
    @JsonProperty("insured_name")
    private String insuredName;
    @JsonProperty("insured_surname")
    private String insuredSurname;
    private String insuredStreet;
    private String prefixInput;
    private String isOwner;
    @JsonProperty("kmCar")
    private String milage;
    @JsonProperty("licenseYear")
    private Integer licenseYears;
    private String line;
    private String model;
    @JsonProperty("typeCar")
    private String carType;
    private String origin;
    @JsonProperty("over_excess")
    private Integer overExcess;
    private String phone;
    @JsonProperty("policy_number")
    private String policy;
    @JsonProperty("safePlace")
    private Boolean goodPlace;
    @JsonProperty("useCar")
    private String carUse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseRef() {
        return caseRef;
    }

    public void setCaseRef(String caseRef) {
        this.caseRef = caseRef;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMasterCompanyId() {
        return masterCompanyId;
    }

    public void setMasterCompanyId(String masterCompanyId) {
        this.masterCompanyId = masterCompanyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Boolean getOpen() {
        return isOpen;
    }

    public void setOpen(Boolean open) {
        isOpen = open;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedById() {
        return createdById;
    }

    public void setCreatedById(String createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getUpdatedById() {
        return updatedById;
    }

    public void setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<InspectionImage> getImages() {
        return images;
    }

    public void setImages(List<InspectionImage> images) {
        this.images = images;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
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

    public String getDpi() {
        return dpi;
    }

    public void setDpi(String dpi) {
        this.dpi = dpi;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public Boolean getGoodConditions() {
        return goodConditions;
    }

    public void setGoodConditions(Boolean goodConditions) {
        this.goodConditions = goodConditions;
    }

    public Boolean getHaveDocs() {
        return haveDocs;
    }

    public void setHaveDocs(Boolean haveDocs) {
        this.haveDocs = haveDocs;
    }

    public Boolean getHavePhone() {
        return havePhone;
    }

    public void setHavePhone(Boolean havePhone) {
        this.havePhone = havePhone;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Boolean getCarYears() {
        return carYears;
    }

    public void setCarYears(Boolean carYears) {
        this.carYears = carYears;
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

    public String getInsuredStreet() {
        return insuredStreet;
    }

    public void setInsuredStreet(String insuredStreet) {
        this.insuredStreet = insuredStreet;
    }

    public String getPrefixInput() {
        return prefixInput;
    }

    public void setPrefixInput(String prefixInput) {
        this.prefixInput = prefixInput;
    }

    public String getIsOwner() {
        return isOwner;
    }

    public void setIsOwner(String isOwner) {
        this.isOwner = isOwner;
    }

    public String getMilage() {
        return milage;
    }

    public void setMilage(String milage) {
        this.milage = milage;
    }

    public Integer getLicenseYears() {
        return licenseYears;
    }

    public void setLicenseYears(Integer licenseYears) {
        this.licenseYears = licenseYears;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public Integer getOverExcess() {
        return overExcess;
    }

    public void setOverExcess(Integer overExcess) {
        this.overExcess = overExcess;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public Boolean getGoodPlace() {
        return goodPlace;
    }

    public void setGoodPlace(Boolean goodPlace) {
        this.goodPlace = goodPlace;
    }

    public String getCarUse() {
        return carUse;
    }

    public void setCarUse(String carUse) {
        this.carUse = carUse;
    }

    public class InspectionImage {
        @JsonProperty("front")
        private String front;
        @JsonProperty("rear")
        private String rear;
        @JsonProperty("frontRight")
        private String frontRight;
        @JsonProperty("rearRight")
        private String rearRight;
        @JsonProperty("right")
        private String right;
        @JsonProperty("tablero")
        private String board;
        @JsonProperty("paneInterior")
        private String interiorBoard;
        @JsonProperty("maletero")
        private String trunk;
        @JsonProperty("llanta")
        private String tyre;
        @JsonProperty("llantaRepuesto")
        private String trunkTyre;
        @JsonProperty("capo")
        private String hood;
        @JsonProperty("accesoriosRadioAire")
        private String radioAir;
        @JsonProperty("accesoriosAlzaVidrios")
        private String windowSwitch;
        @JsonProperty("tarjetaCirculacion")
        private String registration;
        @JsonProperty("license")
        private String license;
        @JsonProperty("dpi")
        private String dpi;
        @JsonProperty("car")
        private String car;
        @JsonProperty("signature")
        private String signature;

        public String getFront() {
            return front;
        }

        public void setFront(String front) {
            this.front = front;
        }

        public String getRear() {
            return rear;
        }

        public void setRear(String rear) {
            this.rear = rear;
        }

        public String getFrontRight() {
            return frontRight;
        }

        public void setFrontRight(String frontRight) {
            this.frontRight = frontRight;
        }

        public String getRearRight() {
            return rearRight;
        }

        public void setRearRight(String rearRight) {
            this.rearRight = rearRight;
        }

        public String getRight() {
            return right;
        }

        public void setRight(String right) {
            this.right = right;
        }

        public String getBoard() {
            return board;
        }

        public void setBoard(String board) {
            this.board = board;
        }

        public String getInteriorBoard() {
            return interiorBoard;
        }

        public void setInteriorBoard(String interiorBoard) {
            this.interiorBoard = interiorBoard;
        }

        public String getTrunk() {
            return trunk;
        }

        public void setTrunk(String trunk) {
            this.trunk = trunk;
        }

        public String getTyre() {
            return tyre;
        }

        public void setTyre(String tyre) {
            this.tyre = tyre;
        }

        public String getTrunkTyre() {
            return trunkTyre;
        }

        public void setTrunkTyre(String trunkTyre) {
            this.trunkTyre = trunkTyre;
        }

        public String getHood() {
            return hood;
        }

        public void setHood(String hood) {
            this.hood = hood;
        }

        public String getRadioAir() {
            return radioAir;
        }

        public void setRadioAir(String radioAir) {
            this.radioAir = radioAir;
        }

        public String getWindowSwitch() {
            return windowSwitch;
        }

        public void setWindowSwitch(String windowSwitch) {
            this.windowSwitch = windowSwitch;
        }

        public String getRegistration() {
            return registration;
        }

        public void setRegistration(String registration) {
            this.registration = registration;
        }

        public String getLicense() {
            return license;
        }

        public void setLicense(String license) {
            this.license = license;
        }

        public String getDpi() {
            return dpi;
        }

        public void setDpi(String dpi) {
            this.dpi = dpi;
        }

        public String getCar() {
            return car;
        }

        public void setCar(String car) {
            this.car = car;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }
}
