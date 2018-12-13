package models.ws;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class YoungerRequest {

	public static final int TRANSACTION = 750;

	@Expose
	private String quoteNumber;
	@Expose
	private String codeCoverage;
	@Expose
	private String name;
	@Expose
	private String address;
	@Expose
	private Date birthdate;
	@Expose
	private String age;
	@Expose
	private String typeLicense;
	@Expose
	private String licenseNumber;
	@Expose
	private Date expirationDateFrom;
	@Expose
	private Date expirationDate;
	
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getCodeCoverage() {
		return codeCoverage;
	}
	public void setCodeCoverage(String codeCoverage) {
		this.codeCoverage = codeCoverage;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTypeLicense() {
		return typeLicense;
	}
	public void setTypeLicense(String typeLicense) {
		this.typeLicense = typeLicense;
	}
	public String getLicenseNumber() {
		return licenseNumber;
	}
	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}
	public Date getExpirationDateFrom() {
		return expirationDateFrom;
	}
	public void setExpirationDateFrom(Date expirationDateFrom) {
		this.expirationDateFrom = expirationDateFrom;
	}
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
