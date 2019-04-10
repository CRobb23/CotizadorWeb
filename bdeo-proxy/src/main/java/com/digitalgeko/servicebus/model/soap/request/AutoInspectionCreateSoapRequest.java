package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="crearautoinspeccionBDEO")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapRequest {

    public static final String RQ_CODE = "786";
    public static final String RS_CODE = "787";

    @XmlElement(name="caso")
    private String caseNumber;
    @XmlElement(name="nombreAsegurado")
    private String insuredName;
    @XmlElement(name="apellidoAsegurado")
    private String insuredSurname;
    @XmlElement(name="licencia")
    private String licenseNumber;
    @XmlElement(name ="email")
    private String email;
    @XmlElement(name="marca")
    private String brand;
    @XmlElement(name="linea")
    private String line;
    @XmlElement(name="modelo")
    private String model;
    @XmlElement(name="placa")
    private String plate;
    @XmlElement(name="telefono")
    private String phone;

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
