package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="datosAutoInspeccion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapRequest {

    public static final String RQ_CODE = "784";
    public static final String RS_CODE = "785";

    // PERSON
    @XmlElement(name="primerNombre")
    private String firstName;
    @XmlElement(name="segundoNombre")
    private String secondName;
    @XmlElement(name="primerApellido")
    private String firstSurname;
    @XmlElement(name="segundoApellido")
    private String secondSurname;
    @XmlElement(name="apellidoCasada")
    private String marriedSurname;
    @XmlElement(name="dpi")
    private String identificationDocument;
    @XmlElement(name="nit")
    private String taxNumber;
    @XmlElement(name="tipoLicencia")
    private String licenseType;
    @XmlElement(name="numeroLicencia")
    private String licenseNumber;
    @XmlElement(name="anosTenencia")
    private String licenseYears;

    // VEHICLE
    @XmlElement(name="propietario")
    private String vehicleOwner;
    @XmlElement(name="domicilio")
    private String address;
    @XmlElement(name="marcaLinea")
    private String brand;
    @XmlElement(name="modelo")
    private String year;
    @XmlElement(name="placa")
    private String plate;
    @XmlElement(name="vin")
    private String vin;
    @XmlElement(name="motor")
    private String engine;
    @XmlElement(name="color")
    private String color;
    @XmlElement(name="kilometraje")
    private String mileage;
    @XmlElement(name="tipoVehiculo")
    private String typeVehicle;
    @XmlElement(name="procedencia")
    private String origin;
    @XmlElement(name="uso")
    private String use;
    @XmlElement(name="moneda")
    private String coin;

    @XmlElementWrapper(name="numerosTelefonos")
    @XmlElement(name="telefonos")
    private List<PhoneData> phones;
    @XmlElementWrapper(name="emailsCliente")
    @XmlElement(name="emails")
    private List<EmailData> emails;
    @XmlElementWrapper(name="emailsCorredor")
    @XmlElement(name="emailsCo")
    private List<EmailBrokerData> emailsBroker;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public String getMarriedSurname() {
        return marriedSurname;
    }

    public void setMarriedSurname(String marriedSurname) {
        this.marriedSurname = marriedSurname;
    }

    public String getIdentificationDocument() {
        return identificationDocument;
    }

    public void setIdentificationDocument(String identificationDocument) {
        this.identificationDocument = identificationDocument;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(String licenseType) {
        this.licenseType = licenseType;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getLicenseYears() {
        return licenseYears;
    }

    public void setLicenseYears(String licenseYears) {
        this.licenseYears = licenseYears;
    }

    public String getVehicleOwner() {
        return vehicleOwner;
    }

    public void setVehicleOwner(String vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(String typeVehicle) {
        this.typeVehicle = typeVehicle;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public List<PhoneData> getPhones() {
        return phones;
    }

    public void setPhones(List<PhoneData> phones) {
        this.phones = phones;
    }

    public List<EmailData> getEmails() {
        return emails;
    }

    public void setEmails(List<EmailData> emails) {
        this.emails = emails;
    }

    public List<EmailBrokerData> getEmailsBroker() {
        return emailsBroker;
    }

    public void setEmailsBroker(List<EmailBrokerData> emailsBroker) {
        this.emailsBroker = emailsBroker;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class PhoneData {
        @XmlElement(name="telefono")
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EmailData {
        @XmlElement(name="email")
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class EmailBrokerData {
        @XmlElement(name="emailC")
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
