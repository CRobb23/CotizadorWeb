package com.digitalgeko.servicebus.model.soap.request;

import javax.xml.bind.annotation.*;

@XmlRootElement(name="datosPagador")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayerInputSoapRequest {

	public static final String RQ_CODE = "725";
	public static final String RS_CODE = "726";

	@XmlElement(name="codigoClientePagador")
	private String codeClient;
	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="nitPagador")
	private String taxNumber;
	@XmlElement(name="tituloPagador")
	private String codeTitle;
	@XmlElement(name="primerNombrePagador")
	private String firstName;
	@XmlElement(name="segundoNombrePagador")
	private String secondName;
	@XmlElement(name="primerApellidoPagador")
	private String firstSurname;
	@XmlElement(name="segundoApellidoPagador")
	private String secondSurname;
	@XmlElement(name="apellidoCasadaPagador")
	private String marriedSurname;
	@XmlElement(name="fechaNacimientoPagador")
	private String birthdate;
	@XmlElement(name="sexoPagador")
	private String sex;
	@XmlElement(name="edadPagador")
	private Integer age;
	@XmlElement(name="profesionPagador")
	private String profession;
	@XmlElement(name="dpiPagador")
	private String identificationDocument;
	@XmlElement(name="codigoCifBancoPagador")
	private String codeCifBank;
	@XmlElement(name="pasaportePagador")
	private String passport;
	@XmlElement(name="estadoCivilPagador")
	private String civilStatus;
	@XmlElement(name="nacionalidadPagador")
	private String nationality;
	@XmlElement(name="tipoLicenciaPagador")
	private String licenseType;
	@XmlElement(name="numeroLicenciaPagador")
	private String licenseNumber;
	@XmlElement(name="emailPagador")
	private String email;
	@XmlElement(name="tipoCliente")
	private String typeClient;
	@XmlElement(name = "tipoSociedad")
    private String typeSociety;
    @XmlElement(name = "nombreEmpresa")
    private String companyName;
    @XmlElement(name = "razonSocial")
    private String businessName;
    @XmlElement(name = "actividadEconomica")
    private String economicActivity;
    @XmlElement(name = "proveedorEstado")
    private String stateProvider;
    @XmlElement(name = "numeroEscritura")
    private String writeNumber;
    @XmlElement(name = "fechaEscritura")
    private String writeDate;
    @XmlElement(name = "fechaConstitucion")
    private String registrationDate;

    @XmlElement(name="direccionesPagador")
    private Address address;
	
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Address{
        @XmlElement(name="direccionCasaPagador")
        private HomeAddress homeAddress;
    	
        @XmlElement(name="direccionTrabajoPagador")
        private WorkAddress workAddress;
    	
    	@XmlAccessorType(XmlAccessType.FIELD)
        public static class HomeAddress{
    		@XmlElement(name="direccionCP")
    		private String address;
    		@XmlElement(name="paisCP")
    		private String country;
    		@XmlElement(name="departamentoCP")
    		private String department;
    		@XmlElement(name="municipioCP")
    		private String municipality;
    		@XmlElement(name="zonaCP")
    		private String zone;
    		@XmlElement(name="telefono1CP")
    		private String phone1;
    		@XmlElement(name="telefono2CP")
    		private String phone2;
    		@XmlElement(name="telefono3CP")
    		private String phone3;
    		@XmlElement(name="coloniaCP")
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
    		@XmlElement(name="direccionTP")
    		private String address;
    		@XmlElement(name="paisTP")
    		private String country;
    		@XmlElement(name="departamentoTP")
    		private String department;
    		@XmlElement(name="municipioTP")
    		private String municipality;
    		@XmlElement(name="zonaTP")
    		private String zone;
    		@XmlElement(name="telefonoTP1")
    		private String phone1;
    		@XmlElement(name="telefonoTP2")
    		private String phone2;
    		@XmlElement(name="telefonoTP3")
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
	public String getTypeClient() {
		return typeClient;
	}
	public void setTypeClient(String typeClient) {
		this.typeClient = typeClient;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}

    public String getTypeSociety() {
        return typeSociety;
    }

    public void setTypeSociety(String typeSociety) {
        this.typeSociety = typeSociety;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getEconomicActivity() {
        return economicActivity;
    }

    public void setEconomicActivity(String economicActivity) {
        this.economicActivity = economicActivity;
    }

    public String getStateProvider() {
        return stateProvider;
    }

    public void setStateProvider(String stateProvider) {
        this.stateProvider = stateProvider;
    }

    public String getWriteNumber() {
        return writeNumber;
    }

    public void setWriteNumber(String writeNumber) {
        this.writeNumber = writeNumber;
    }

    public String getWriteDate() {
        return writeDate;
    }

    public void setWriteDate(String writeDate) {
        this.writeDate = writeDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }
}
