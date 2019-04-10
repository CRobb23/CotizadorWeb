package models.ws;

import com.google.gson.annotations.Expose;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class BusinessClientRequest {

	public static final int TRANSACTION = 720;

	@Expose
	private String quoteNumber;
	@Expose
	private String quotationDate;
	@Expose
	private String taxNumber;
	@Expose
	private String societyType;
	@Expose
	private String companyName;
	@Expose
	private String businessName;
	@Expose
	private String economicActivity;
	@Expose
	private String nationality;
	@Expose
	private String dpiCompany;
	@Expose
	private String stateProvider;
	@Expose
	private String email;
	@Expose
	private String writeNumber;
	@Expose
	private String writeDate;
	@Expose
	private String registrationDate;
	@Expose
	private String condeCifBank;
	@Expose
	private String codeClient;

	@Expose
	private LegalRepresentative legalRepresentative;
	@Expose
	private Address address;

    public static class LegalRepresentative{
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
		private String nationality;
		@Expose
		private String register;
		@Expose
		private String caseFile;
		@Expose
		private String extendedIn;
		@Expose
		private String registrationDate;
		@Expose
		private String birthdate;
		@Expose
		private String profession;
		@Expose
		private String book;
		@Expose
		private String folio;
		@Expose
		private String dpi;
		@Expose
		private String taxNumber;
		@Expose
		private String passport;
		@Expose
		private String sex;
		@Expose
		private String civilStatus;
		@Expose
		private String email;
		@Expose
		private String monthlyAmountIncome;
		@Expose
		private String monthlyAmountOfIncome;
    	
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
		public String getNationality() {
			return nationality;
		}
		public void setNationality(String nationality) {
			this.nationality = nationality;
		}
		public String getRegister() {
			return register;
		}
		public void setRegister(String register) {
			this.register = register;
		}
		public String getCaseFile() {
			return caseFile;
		}
		public void setCaseFile(String caseFile) {
			this.caseFile = caseFile;
		}
		public String getExtendedIn() {
			return extendedIn;
		}
		public void setExtendedIn(String extendedIn) {
			this.extendedIn = extendedIn;
		}
		public String getRegistrationDate() {
			return registrationDate;
		}
		public void setRegistrationDate(String registrationDate) {
			this.registrationDate = registrationDate;
		}
		public String getBirthdate() {
			return birthdate;
		}
		public void setBirthdate(String birthdate) {
			this.birthdate = birthdate;
		}
		public String getProfession() {
			return profession;
		}
		public void setProfession(String profession) {
			this.profession = profession;
		}
		public String getBook() {
			return book;
		}
		public void setBook(String book) {
			this.book = book;
		}
		public String getFolio() {
			return folio;
		}
		public void setFolio(String folio) {
			this.folio = folio;
		}
		public String getDpi() {
			return dpi;
		}
		public void setDpi(String dpi) {
			this.dpi = dpi;
		}
		public String getTaxNumber() {
			return taxNumber;
		}
		public void setTaxNumber(String taxNumber) {
			this.taxNumber = taxNumber;
		}
		public String getPassport() {
			return passport;
		}
		public void setPassport(String passport) {
			this.passport = passport;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getCivilStatus() {
			return civilStatus;
		}
		public void setCivilStatus(String civilStatus) {
			this.civilStatus = civilStatus;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMonthlyAmountIncome() {
			return monthlyAmountIncome;
		}
		public void setMonthlyAmountIncome(String monthlyAmountIncome) {
			this.monthlyAmountIncome = monthlyAmountIncome;
		}
		public String getMonthlyAmountOfIncome() {
			return monthlyAmountOfIncome;
		}
		public void setMonthlyAmountOfIncome(String monthlyAmountOfIncome) {
			this.monthlyAmountOfIncome = monthlyAmountOfIncome;
		}
    }

    public static class Address{
        @Expose
		private WorkAddress workAddress;

		@Expose
		private RepresentativeLegalAddress representativeLegalAddress;

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
        
    	public static class RepresentativeLegalAddress{
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
		public WorkAddress getWorkAddress() {
			return workAddress;
		}
		public void setWorkAddress(WorkAddress workAddress) {
			this.workAddress = workAddress;
		}
		public RepresentativeLegalAddress getRepresentativeLegalAddress() {
			return representativeLegalAddress;
		}
		public void setRepresentativeLegalAddress(RepresentativeLegalAddress representativeLegalAddress) {
			this.representativeLegalAddress = representativeLegalAddress;
		}
    }
    
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getQuotationDate() {
		return quotationDate;
	}
	public void setQuotationDate(String quotationDate) {
		this.quotationDate = quotationDate;
	}
	public String getTaxNumber() {
		return taxNumber;
	}
	public void setTaxNumber(String taxNumber) {
		this.taxNumber = taxNumber;
	}
	public String getSocietyType() {
		return societyType;
	}
	public void setSocietyType(String societyType) {
		this.societyType = societyType;
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
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getDpiCompany() {
		return dpiCompany;
	}
	public void setDpiCompany(String dpiCompany) {
		this.dpiCompany = dpiCompany;
	}
	public String getStateProvider() {
		return stateProvider;
	}
	public void setStateProvider(String stateProvider) {
		this.stateProvider = stateProvider;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
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
	public String getCondeCifBank() {
		return condeCifBank;
	}
	public void setCondeCifBank(String condeCifBank) {
		this.condeCifBank = condeCifBank;
	}
	public String getCodeClient() {
		return codeClient;
	}
	public void setCodeClient(String codeClient) {
		this.codeClient = codeClient;
	}
	public LegalRepresentative getLegalRepresentative() {
		return legalRepresentative;
	}
	public void setLegalRepresentative(LegalRepresentative legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
}
