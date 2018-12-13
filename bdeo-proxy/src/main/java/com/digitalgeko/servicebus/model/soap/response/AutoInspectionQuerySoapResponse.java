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
    @XmlElement(name="caso")
    private String caseId;
    @XmlElement(name="casoRef")
    private String caseRef;
    @XmlElement(name="direccion")
    private String address;
    @XmlElement(name="empresaId")
    private String masterCompanyId;
    @XmlElement(name="empresa")
    private String companyName;
    @XmlElement(name="abrioEnlace")
    private Boolean isOpen;
    @XmlElement(name="estado")
    private Integer status;
    @XmlElement(name="comentarios")
    private String comments;
    @XmlElement(name="creadoId")
    private String createdById;
    @XmlElement(name="creadoNombre")
    private String createdByName;
    @XmlElement(name="actualizadoId")
    private String updatedById;
    @XmlElement(name="creadoFecha")
    private Long createdDate;
    @XmlElement(name="actualizadoFecha")
    private Long updatedDate;

    @XmlElement(name="chasis")
    private String vin;
    @XmlElement(name="nombrePropietario")
    private String owner;
    @XmlElement(name="color")
    private String color;
    @XmlElement(name="dpi")
    private String dpi;
    @XmlElement(name="licencia")
    private String licenseNumber;
    @XmlElement(name="tipoLicencia")
    private String licenseType;
    @XmlElement(name="email")
    private String email;
    @XmlElement(name="motor")
    private String engine;
    @XmlElement(name="buenasCondiciones")
    private Boolean goodConditions;
    @XmlElement(name="documentos")
    private Boolean haveDocs;
    @XmlElement(name="tieneTelefono")
    private Boolean havePhone;
    @XmlElement(name="puntuacion")
    private Integer grade;
    @XmlElement(name="antiguedad")
    private Boolean carYears;
    @XmlElement(name="nombreAsegurado")
    private String insuredName;
    @XmlElement(name="apellidoAsegurado")
    private String insuredSurname;
    @XmlElement(name="direccionAsegurado")
    private String insuredStreet;
    @XmlElement(name="prefijo")
    private String prefixInput;
    @XmlElement(name="esPropietario")
    private String isOwner;
    @XmlElement(name="kilometraje")
    private String milage;
    @XmlElement(name="tenenciaLicencia")
    private Integer licenseYears;
    @XmlElement(name="linea")
    private String line;
    @XmlElement(name="modelo")
    private String model;
    @XmlElement(name="tipo")
    private String carType;
    @XmlElement(name="origen")
    private String origin;
    @XmlElement(name="superaDeducible")
    private Integer overExcess;
    @XmlElement(name="telefono")
    private String phone;
    @XmlElement(name="poliza")
    private String policy;
    @XmlElement(name="lugarApropiado")
    private Boolean goodPlace;
    @XmlElement(name="uso")
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
}
