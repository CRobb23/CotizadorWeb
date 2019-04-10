package com.digitalgeko.servicebus.model.soap.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="consultaautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionQuerySoapResponse {

    @XmlElement(name="id")
    private String id;
    @XmlElement(name = "url")
    private String url;
    @XmlElement(name="caso")
    private String caseNumber;
    @XmlElement(name = "placa")
    private String plate;
    @XmlElement(name="creadoNombre")
    private String createdByName;
    @XmlElement(name="estado")
    private Integer status;
    @XmlElement(name="actualizadoId")
    private String updatedById;
    @XmlElement(name="creadoId")
    private String createdById;

    @XmlElement(name="buenasCondiciones")
    private String goodConditions;
    @XmlElement(name="antiguedad")
    private String carYears;
    @XmlElement(name = "articulo908")
    private String article908;

    @XmlElement(name = "numeroRef")
    private String numRef;
    @XmlElement(name="casoRef")
    private String caseRef;
    @XmlElement(name="nombreAsegurado")
    private String insuredName;
    @XmlElement(name="abrioEnlace")
    private String isOpen;
    @XmlElement(name="telefono")
    private String phone;
    @XmlElement(name="modelo")
    private String model;
    @XmlElement(name="puntuacion")
    private String grade;
    @XmlElement(name="empresaId")
    private String masterCompanyId;
    @XmlElement(name="licencia")
    private String licenseNumber;
    @XmlElement(name = "marca")
    private String brand;
    @XmlElement(name="creadoFecha")
    private Long createdDate;
    @XmlElement(name="prefijo")
    private String prefixInput;
    @XmlElement(name="empresa")
    private String companyName;
    @XmlElement(name="linea")
    private String line;
    @XmlElement(name="apellidoAsegurado")
    private String insuredSurname;
    @XmlElement(name="email")
    private String email;
    @XmlElement(name="nombrePropietario")
    private String owner;
    @XmlElement(name="color")
    private String color;
    @XmlElement(name="tipo")
    private String carType;
    @XmlElement(name="uso")
    private String carUse;
    @XmlElement(name="chasis")
    private String vin;
    @XmlElement(name="motor")
    private String engine;
    @XmlElement(name="kilometraje")
    private String mileage;
    @XmlElement(name="actualizadoFecha")
    private Long updatedDate;


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
}
