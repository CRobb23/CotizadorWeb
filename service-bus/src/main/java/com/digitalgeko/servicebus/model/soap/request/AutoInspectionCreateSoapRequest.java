package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name="SolicitarAutoInspeccion")
@XmlAccessorType(XmlAccessType.FIELD)
public class AutoInspectionCreateSoapRequest {

    public static final String RQ_CODE = "490";
    public static final String RS_CODE = "491";

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
    @XmlElementWrapper(name="direcciones")
    @XmlElement(name="direccion")
    private List<Address> addresses;
    @XmlElementWrapper(name="telefonos")
    @XmlElement(name="telefono")
    private List<String> phones;
    @XmlElement(name="email")
    private String clientEmail;
    @XmlElement(name="emailCorredor")
    private String brokerEmail;
    @XmlElement(name="tipoLicencia")
    private String licenseType;
    @XmlElement(name="numeroLicencia")
    private String licenseNumber;

    // VEHICLE
    @XmlElement(name="propietario")
    private String vehicleOwner;
    @XmlElement(name="marca")
    private String brand;
    @XmlElement(name="marcaLinea")
    private String line;
    @XmlElement(name="modeloVehiculo")
    private String year;
    @XmlElement(name="placa")
    private String plate;
    @XmlElement(name="tipoVehiculo")
    private String typeVehicle;
    @XmlElement(name="color")
    private String color;
    @XmlElement(name="motor")
    private String engine;
    @XmlElement(name="chasis")
    private String chasis;
    @XmlElement(name="kilometraje")
    private String mileage;
    @XmlElement(name="tipoKilometraje")
    private String typeMileage;

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

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getBrokerEmail() {
        return brokerEmail;
    }

    public void setBrokerEmail(String brokerEmail) {
        this.brokerEmail = brokerEmail;
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

    public String getVehicleOwner() {
        return vehicleOwner;
    }

    public void setVehicleOwner(String vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
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

    public String getTypeVehicle() {
        return typeVehicle;
    }

    public void setTypeVehicle(String typeVehicle) {
        this.typeVehicle = typeVehicle;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getChasis() {
        return chasis;
    }

    public void setChasis(String chasis) {
        this.chasis = chasis;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getTypeMileage() {
        return typeMileage;
    }

    public void setTypeMileage(String typeMileage) {
        this.typeMileage = typeMileage;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Address {
        @XmlElement(name="direccion")
        private String address;
        @XmlElement(name="pais")
        private String country;
        @XmlElement(name="departamento")
        private String department;
        @XmlElement(name="municipio")
        private String municipality;
        @XmlElement(name="zona")
        private String zone;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getMunicipality() {
            return municipality;
        }

        public void setMunicipality(String municipality) {
            this.municipality = municipality;
        }

        public String getZone() {
            return zone;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }
    }
}
