package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="datosClientePersonal")
@XmlAccessorType(XmlAccessType.FIELD)
public class PersonClientInputSoapRequest {

	public static final String RQ_CODE = "715";
	public static final String RS_CODE = "716";

	@XmlElement(name="fechaCotizacion")
	private String quotationDate;
	@XmlElement(name="codigoCliente")
	private String codeClient;
	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="nit")
	private String taxNumber;
	@XmlElement(name="titulo")
	private String codeTitle;
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
	@XmlElement(name="fechaNacimiento")
	private String birthdate;
	@XmlElement(name="sexo")
	private String sex;
	@XmlElement(name="edad")
	private Integer age;
	@XmlElement(name="profesion")
	private String profession;
	@XmlElement(name="dpi")
	private String identificationDocument;
	@XmlElement(name="codigoCifBanco")
	private String codeCifBank;
	@XmlElement(name="pasaporte")
	private String passport;
	@XmlElement(name="estadoCivil")
	private String civilStatus;
	@XmlElement(name="nacionalidad")
	private String nationality;
	@XmlElement(name="tipoLicencia")
	private String licenseType;
	@XmlElement(name="numeroLicencia")
	private String licenseNumber;
	@XmlElement(name="email")
	private String email;
	@XmlElement(name="vip")
	private String vip;
    
    @XmlElement(name="direcciones")
    private Address address;
	
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Address{
        @XmlElement(name="direccionCasa")
        private HomeAddress homeAddress;
    	
        @XmlElement(name="direccionTrabajo")
        private WorkAddress workAddress;
    	
    	@XmlAccessorType(XmlAccessType.FIELD)
        public static class HomeAddress{
    		@XmlElement(name="direccionC")
    		private String address;
    		@XmlElement(name="paisC")
    		private String country;
    		@XmlElement(name="departamentoC")
    		private String department;
    		@XmlElement(name="municipioC")
    		private String municipality;
    		@XmlElement(name="zonaC")
    		private String zone;
    		@XmlElement(name="telefono1")
    		private String phone1;
    		@XmlElement(name="telefono2")
    		private String phone2;
    		@XmlElement(name="telefono3")
    		private String phone3;
    		@XmlElement(name="colonia")
    		private String colony;
    		
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
			public String getPhone1() {
				return phone1;
			}
			public void setPhone1(String phone1) {
				this.phone1 = phone1;
			}
			public String getPhone2() {
				return phone2;
			}
			public void setPhone2(String phone2) {
				this.phone2 = phone2;
			}
			public String getPhone3() {
				return phone3;
			}
			public void setPhone3(String phone3) {
				this.phone3 = phone3;
			}
			public String getColony() {
				return colony;
			}
			public void setColony(String colony) {
				this.colony = colony;
			}
    	}
    	
    	@XmlAccessorType(XmlAccessType.FIELD)
        public static class WorkAddress{
    		@XmlElement(name="direccionT")
    		private String address;
    		@XmlElement(name="paisT")
    		private String country;
    		@XmlElement(name="departamentoT")
    		private String department;
    		@XmlElement(name="municipioT")
    		private String municipality;
    		@XmlElement(name="zonaT")
    		private String zone;
    		@XmlElement(name="telefonoT1")
    		private String phone1;
    		@XmlElement(name="telefonoT2")
    		private String phone2;
    		@XmlElement(name="telefonoT3")
    		private String phone3;
    		
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
			public String getPhone1() {
				return phone1;
			}
			public void setPhone1(String phone1) {
				this.phone1 = phone1;
			}
			public String getPhone2() {
				return phone2;
			}
			public void setPhone2(String phone2) {
				this.phone2 = phone2;
			}
			public String getPhone3() {
				return phone3;
			}
			public void setPhone3(String phone3) {
				this.phone3 = phone3;
			}
    	}

		public HomeAddress getHomeAddress() {
			return homeAddress;
		}
		public void setHomeAddress(HomeAddress homeAddress) {
			this.homeAddress = homeAddress;
		}
		public WorkAddress getWorkAddress() {
			return workAddress;
		}
		public void setWorkAddress(WorkAddress workAddress) {
			this.workAddress = workAddress;
		}
    }
	public String getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getCodeTitle() {
		return codeTitle;
	}
	public void setCodeTitle(String codeTitle) {
		this.codeTitle = codeTitle;
	}
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
	public String getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getProfession() {
		return profession;
	}
	public void setProfession(String profession) {
		this.profession = profession;
	}
	public String getIdentificationDocument() {
		return identificationDocument;
	}
	public void setIdentificationDocument(String identificationDocument) {
		this.identificationDocument = identificationDocument;
	}
	public String getCodeCifBank() {
		return codeCifBank;
	}
	public void setCodeCifBank(String codeCifBank) {
		this.codeCifBank = codeCifBank;
	}
	public String getPassport() {
		return passport;
	}
	public void setPassport(String passport) {
		this.passport = passport;
	}
	public String getCivilStatus() {
		return civilStatus;
	}
	public void setCivilStatus(String civilStatus) {
		this.civilStatus = civilStatus;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getVip() {
		return vip;
	}
	public void setVip(String vip) {
		this.vip = vip;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
