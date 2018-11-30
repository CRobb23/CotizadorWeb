package models;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import org.apache.commons.lang.WordUtils;
import play.db.jpa.Model;
import utils.StringUtil;


@Entity
public class ER_Client extends Model {
	
	@Column(length=75)
	public String firstName;
	@Column(length=75)
	public String lastName;
	public Integer phoneNumber;
	@Column(length=75)
	public String email;
	@Column(length=20)
	public String taxNumber;
	
	/** Fields phase II */
	@Column(name="use_data_client_payer")
	public Boolean useDataClientPayer;
	@Column(name="is_individual")
	public Boolean isIndividual;
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
	@Column(name="passport", length=50)
	public String passport;
	@Column(name="license_number", length=30)
    public String licenseNumber;
	@Column(name="address")
	public String address;
	@Column(name="address_work")
	public String addressWork;
	@Column(name="phone_number1", length=20)
    public String phoneNumber1;
	@Column(name="phone_number2", length=20)
    public String phoneNumber2;
	@Column(name="phone_number3", length=20)
    public String phoneNumber3;
	@Column(name="phone_number_work1", length=20)
    public String phoneNumberWork1;
	@Column(name="phone_number_work2", length=20)
    public String phoneNumberWork2;
	@Column(name="phone_number_work3", length=20)
    public String phoneNumberWork3;
	
	/** Business Client */
	@Column(name="company_name")
	public String companyName;
	@Column(name="business_name")
	public String businessName;
	@Column(name="state_provider")
	public String stateProvider;
	@Column(name="write_number")
	public String writeNumber;
	@Column(name="write_date")
	public Date writeDate;
	@Column(name="registration_date")
	public Date registrationDate;
	@Column(name="conde_cif_ank")
	public String codeCifBank;
	@Column(name="code_client")
	public String codeClient;
	@Column(name="expose")
    public Boolean expose;
	@Column(name = "additionalEmails")
    public String additionalEmails;
	
	/** Relations */
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_title", nullable=true)
	public ER_Profession title;
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
	@JoinColumn(name="id_country_work", nullable=true)
	public ER_Geographic_Area countryWork;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_work_department", nullable=true)
	public ER_Geographic_Area workDepartment;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_work_municipality", nullable=true)
	public ER_Geographic_Area workMunicipality;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_work_zone", nullable=true)
	public ER_Geographic_Area workZone;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_nationality", nullable=true)
	public ER_Nationality nationality;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_civil_status", nullable=true)
	public ER_Civil_Status civilStatus;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_license_type", nullable=true)
	public ER_License_Type licenseType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_sex", nullable=true)
	public ER_Sex sex;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_society_type", nullable=true)
	public ER_Society_Type societyType;
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="id_economic_activity", nullable=true)
	public ER_Economic_Activity economicActivity;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_payer", nullable=true)
	public ER_Client_Payer payer;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_legal_representative", nullable=true)
	public ER_Legal_Representative legalRepresentative;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_client_PEP", nullable=true)
	public ER_Client_PEP clientPEP;
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="id_multimedia", nullable=true)
	public ER_Multimedia multimedia;

	public String getFullName() {
		StringBuilder name = new StringBuilder();
		if(isIndividual != null && isIndividual){
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
		}else{
			name.append(companyName);
		}
		return WordUtils.capitalizeFully(name.toString());
	}

	public String getLineType() {
		String line = " ";
		if(isIndividual!=null) {
			if(isIndividual) {
				line = "Línea Individual";
			}else {
				line = "Línea Empresarial";
			}
		}
		return line;
	}

	public String getIdType() {
	    if (!StringUtil.isNullOrBlank(identificationDocument)) {
	        return "DPI";
        } else if (!StringUtil.isNullOrBlank(passport)) {
	        return "Pasaporte";
        }
        return "";
    }

    public String getIdNum() {
        if (!StringUtil.isNullOrBlank(identificationDocument)) {
            return identificationDocument;
        } else if (!StringUtil.isNullOrBlank(passport)) {
            return passport;
        }
        return "";
    }

    public String getPersonType() {
	    if (Boolean.TRUE.equals(isIndividual)) {
	        return "Individual";
        } else if (Boolean.FALSE.equals(isIndividual)) {
	        return "Juridica";
        }
        return "";
    }

    public String getPersonBirthDate() {
	    if (Boolean.TRUE.equals(isIndividual) && birthdate != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(birthdate);
        }
        if (Boolean.FALSE.equals(isIndividual) && writeDate != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(writeDate);
        }
        return "";
    }

    public void ConvertUpper(){

		if (!StringUtil.isNullOrBlank(this.businessName)) {
			this.businessName= this.businessName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.additionalEmails)) {
			this.additionalEmails= this.additionalEmails.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.addressWork)) {
			this.addressWork= this.addressWork.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.address)) {
			this.address= this.address.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.companyName)) {
			this.companyName= this.companyName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.email)) {
			this.email = this.email.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.firstName)) {
			this.firstName =this.firstName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.secondName)) {
			this.secondName= this.secondName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.lastName)) {
			this.lastName =this.lastName.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.secondSurname)) {
			this.secondSurname =this.secondSurname.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.marriedSurname)) {
			this.marriedSurname = this.marriedSurname.toUpperCase();
		}
		if (!StringUtil.isNullOrBlank(this.firstSurname)) {
			this.firstSurname = this.firstSurname.toUpperCase();
		}
	}
}
