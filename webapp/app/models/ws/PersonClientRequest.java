package models.ws;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class PersonClientRequest {

	public static final int TRANSACTION = 715;

	@Expose
	private String quotationDate;
	@Expose
	private String codeClient;
	@Expose
	private String quoteNumber;
	@Expose
	private String taxNumber;
	@Expose
	private String codeTitle;
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
	private String birthdate;
	@Expose
	private String sex;
	@Expose
	private Integer age;
	@Expose
	private String profession;
	@Expose
	private String identificationDocument;
	@Expose
	private String codeCifBank;
	@Expose
	private String passport;
	@Expose
	private String civilStatus;
	@Expose
	private String nationality;
	@Expose
	private String licenseType;
	@Expose
	private String licenseNumber;
	@Expose
	private String email;
	@Expose
	private String vip;

	@Expose
	private Address address;
	
    public static class Address{
		@Expose
		private HomeAddress homeAddress;

		@Expose
		private WorkAddress workAddress;
    	
    	public static class HomeAddress{
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
			@Expose
			private String phone1;
			@Expose
			private String phone2;
			@Expose
			private String phone3;
			@Expose
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
    	
    	public static class WorkAddress{
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
			@Expose
			private String phone1;
			@Expose
			private String phone2;
			@Expose
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
