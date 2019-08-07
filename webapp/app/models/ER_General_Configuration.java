package models;

import play.data.validation.*;
import play.db.jpa.*;

import javax.persistence.*;

import java.math.BigDecimal;

@Entity
public class ER_General_Configuration extends Model {
	
	@Required
	@Min(0)
	public BigDecimal partialTheftPercentage;
	
	@OneToOne
	@Required
	public ER_Coverage theftCoverage;
	
	@OneToOne
	@Required
	public ER_Coverage totalTheftCoverage;
	
	@OneToOne
	@Required
	public ER_Coverage injuriesCoverage;
	
	@OneToOne
	@Required
	public ER_Coverage thirdInjuriesCoverage;
	
	@OneToOne
	@Required
	public ER_Coverage civilResponsabilityCoverage;
	
	@Required
	@Min(0)
	public BigDecimal ivaPercentage;
	
	@Required
	@Min(0)
	public BigDecimal emissionFeePercentage;
	
	@Required
	@Min(0)
	public Integer guardValidityDays;
	
	@Required
	@Min(0)
	public Integer taskReminderTime;
	
	@Required
	@Min(0)
	public Integer guardReminderTime;
	
	@Required
	@Min(0)
	public BigDecimal maxValueWithoutLoJack;

    @Required
    @Min(0)
    public BigDecimal theftDeductibleWithoutLowJack;
	
	@Required
	@Lob
	public String additionalBenefits;
	
	@Required
	@Lob
	public String observations;

    @Required
    @Min(0)
    public Integer minimumCarYear;

    @Required
    @Min(0)
    public Integer maximumCarYear;

    @Required
    @Min(0)
    public BigDecimal garanteedValueConfig;

    @Required
    @Min(0)
    public BigDecimal averageValueConfig;

    public Boolean emissionFeeFirstPayment;
    
    public String agentCodeAS400;

    public Long sessionTimeout;

    public Boolean fullAccess;

	@Column(length=8000)
    public String guardMails;
	
	public void setParametersInConfiguration(ER_General_Configuration configuration) {
		this.totalTheftCoverage = configuration.totalTheftCoverage;
		this.partialTheftPercentage = configuration.partialTheftPercentage;
		this.theftCoverage = configuration.theftCoverage;
		this.injuriesCoverage = configuration.injuriesCoverage;
		this.thirdInjuriesCoverage = configuration.thirdInjuriesCoverage;
		this.civilResponsabilityCoverage = configuration.civilResponsabilityCoverage;
		this.ivaPercentage = configuration.ivaPercentage;
		this.emissionFeePercentage = configuration.emissionFeePercentage;
		this.emissionFeeFirstPayment = configuration.emissionFeeFirstPayment;
		this.guardValidityDays = configuration.guardValidityDays;
		this.guardReminderTime = configuration.guardReminderTime;
		this.taskReminderTime = configuration.taskReminderTime;
		this.maxValueWithoutLoJack = configuration.maxValueWithoutLoJack;
        this.theftDeductibleWithoutLowJack = configuration.theftDeductibleWithoutLowJack;
		this.additionalBenefits = configuration.additionalBenefits;
		this.observations = configuration.observations;
        this.minimumCarYear = configuration.minimumCarYear;
        this.maximumCarYear = configuration.maximumCarYear;
        this.agentCodeAS400 = configuration.agentCodeAS400;
        this.guardMails = configuration.guardMails;
        this.garanteedValueConfig = configuration.garanteedValueConfig;
        this.averageValueConfig = configuration.averageValueConfig;
        this.sessionTimeout = configuration.sessionTimeout;
        this.fullAccess = configuration.fullAccess;
	}

	/**
	 * replaced on Incidents.viewQuotationPDF()
	 * */
	@Deprecated()
	public String[] additionalBenefitsArray() {
		if (this.additionalBenefits!=null && !this.additionalBenefits.isEmpty()) {
			return additionalBenefits.split("[\r\n]+");
		}
		
		return null;
	}
	
	public String[] observationsArray() {
		if (this.observations!=null && !this.observations.isEmpty()) {
			return observations.split("[\r\n]+");
		}
		
		return null;
	}

    public static ER_General_Configuration generalConfiguration() {
        ER_General_Configuration configuration = ER_General_Configuration.find("").first();
        return configuration;
    }
}
