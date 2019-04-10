package models.ws.rest;

import com.google.gson.annotations.Expose;

import java.util.List;

public class InspectionAutoRequest {

    // PERSON
    @Expose
    private String firstName;
    @Expose
    private String secondName;
    @Expose
    private String firstSurname;
    @Expose
    private String secondSurname;
    @Expose
    private String marriedSurname;
    @Expose
    private String identificationDocument;
    @Expose
    private String taxNumber;
    @Expose
    private String licenseType;
    @Expose
    private String licenseNumber;
    @Expose
    private String licenseYears;

    // VEHICLE
    @Expose
    private String vehicleOwner;
    @Expose
    private String address;
    @Expose
    private String brand;
    @Expose
    private String line;
    @Expose
    private String year;
    @Expose
    private String plate;
    @Expose
    private String vin;
    @Expose
    private String engine;
    @Expose
    private String color;
    @Expose
    private String mileage;
    @Expose
    private String typeVehicle;
    @Expose
    private String origin;
    @Expose
    private String use;
    @Expose
    private String coin;

    @Expose
    private List<PhoneData> phones;
    @Expose
    private List<EmailData> emails;
    @Expose
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

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public static class PhoneData {
        @Expose
        private String phone;

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public static class EmailData {
        @Expose
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class EmailBrokerData {
        @Expose
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
