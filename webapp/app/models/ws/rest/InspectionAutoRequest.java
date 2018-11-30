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
	private List<Address> addresses;
	@Expose
	private List<String> phones;
	@Expose
	private String clientEmail;
	@Expose
	private String brokerEmail;
	@Expose
	private String licenseType;
	@Expose
	private String licenseNumber;

	// VEHICLE
	@Expose
	private String vehicleOwner;
	@Expose
	private String brand;
	@Expose
	private String line;
	@Expose
	private String year;
	@Expose
	private String plate;
	@Expose
	private String typeVehicle;
	@Expose
	private String color;
	@Expose
	private String engine;
	@Expose
	private String chasis;
	@Expose
	private String mileage;
	@Expose
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

	public static class Address{
		@Expose
		private String address;
		@Expose
		private String country;
		@Expose
		private String department;
		@Expose
		private String municipality;
		@Expose
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
