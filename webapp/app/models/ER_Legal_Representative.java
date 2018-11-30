package models;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang.WordUtils;

import play.data.validation.Max;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;
import utils.StringUtil;

@Entity
public class ER_Legal_Representative extends Model {
	
	@Column(name="first_name", length=75)
	public String firstName;
	@Column(name="second_name", length=75)
	public String secondName;
	@Column(name="first_surname", length=75)
	public String firstSurname;
	@Column(name="second_surname", length=75)
	public String secondSurname;
	@Column(name="married_surname", length=50)
	public String marriedSurname;
	@Column(name="birthdate")
	public Date birthdate;
	@Column(name="identification_document", length=30)
	public String identificationDocument;
	@Column(name="tax_number", length=20)
	public String taxNumber;
	@Column(name="passport", length=50)
	public String passport;
	
	@Column(name="registry")
	public String registry;
	@Column(name="case_file")
	public String caseFile;
	@Column(name="extended_in")
	public String extendedIn;
	@Column(name="registration_date")
	public Date registrationDate;
	@Column(name="book")
	public String book;
	@Column(name="folio")
	public String folio;
	@Column(name="email",length=75)
	public String email;
	@Required @Min(0) @Max(50) @Column(name="monthly_amount_income")
	public BigDecimal monthlyAmountIncome;
	@Required @Min(0) @Max(50) @Column(name="monthly_amount_of_income")
	public BigDecimal monthlyAmountOfIncome;
	@Column(name="address")
	public String address;
	@Column(name="phone_number1", length=20)
	public String phoneNumber1;
	@Column(name="phone_number2", length=20)
	public String phoneNumber2;
	@Column(name="phone_number3", length=20)
	public String phoneNumber3;
	
	/** Relations */
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_profession", nullable=true)
	public ER_Profession profession;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_country", nullable=true)
	public ER_Geographic_Area country;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_department", nullable=true)
	public ER_Geographic_Area department;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_municipality", nullable=true)
	public ER_Geographic_Area municipality;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_zone", nullable=true)
	public ER_Geographic_Area zone;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_nationality", nullable=true)
	public ER_Nationality nationality;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_civil_status", nullable=true)
	public ER_Civil_Status civilStatus;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_sex", nullable=true)
	public ER_Sex sex;

	public String getFullName() {
		StringBuilder name = new StringBuilder();
		if(firstName != null && !firstName.isEmpty()){
			name.append(firstName);
		}
		if(secondName != null && !secondName.isEmpty()){
			name.append(" ").append(secondName);
		}
		if(firstSurname != null && !firstSurname.isEmpty()){
			name.append(" ").append(firstSurname);
		}
		if(secondSurname != null && !secondSurname.isEmpty()){
			name.append(" ").append(secondSurname);
		}
		if(marriedSurname != null && !marriedSurname.isEmpty()){
			name.append(" ").append(marriedSurname);
		}
		return WordUtils.capitalizeFully(name.toString());
	}

	public void ConvertUpper(){

		if (!StringUtil.isNullOrBlank(this.firstName)) {
			this.firstName =this.firstName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.secondName)) {
			this.secondName= this.secondName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.firstSurname)) {
			this.firstSurname= this.firstSurname.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.secondSurname)) {
			this.secondSurname= this.secondSurname.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.marriedSurname)) {
			this.marriedSurname= this.marriedSurname.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.identificationDocument)) {
			this.identificationDocument = this.identificationDocument.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.taxNumber)) {
			this.taxNumber =this.taxNumber.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.passport)) {
			this.passport= this.passport.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.registry)) {
			this.registry =this.registry.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.caseFile)) {
			this.caseFile =this.caseFile.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.extendedIn)) {
			this.extendedIn = this.extendedIn.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.book)) {
			this.book = this.book.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.email)) {
			this.email = this.email.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.address)) {
			this.address = this.address.toUpperCase();
		}
	}
}
