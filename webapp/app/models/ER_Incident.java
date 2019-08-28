package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controllers.Incidents;
import ext.GsonExclusionStrategy;
import helpers.ERConstants;
import helpers.GeneralMethods;
import objects.CoverageCost;
import objects.CoverageCostCategory;
import objects.PaymentOption;
import objects.QuotationDetail;
import play.Logger;
import play.db.jpa.Model;
import play.libs.Crypto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
public class ER_Incident extends Model {
	
	public Long number;

	@OneToMany(mappedBy="incident", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public List<ER_Incident_Parameter> parameters;
	
	@ManyToOne(optional=false, cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.LAZY)
	public ER_Client client;
	
	@OneToMany(mappedBy="incident", orphanRemoval=true, fetch=FetchType.LAZY)
	public List<ER_Task> tasks;
	
	@OneToMany(mappedBy="incident", orphanRemoval=true, fetch=FetchType.LAZY)
	public List<ER_Guard> guards;
	
	@OneToOne(cascade={CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}, fetch=FetchType.LAZY)
	public ER_Vehicle vehicle;
	
	@OneToMany(mappedBy="incident", orphanRemoval=true, fetch=FetchType.LAZY)
	public List<ER_Quotation> quotations;
	
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	@JoinColumn(name="status_id", nullable=true)
	public ER_Incident_Status status;

	@Column(precision=19, scale=2)
	public BigDecimal selectedTotalPrime;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public ER_Declined_Sell_Reason declinedReason;

	@ManyToOne(fetch=FetchType.LAZY)
	public ER_Quotation selectedQuotation;

	@ManyToOne(fetch=FetchType.LAZY)
	public ER_ReviewDetail reviewDetail;
	
	@ManyToOne(fetch=FetchType.EAGER)
	public ER_Payment_Frecuency selectedPaymentFrecuency;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	public ER_Inspection inspection;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="id_payment", nullable=true)
	public ER_Payment payment;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public ER_User creator;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public ER_User finalizer;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public ER_Channel channel;
	
	@ManyToOne(fetch=FetchType.LAZY)
	public ER_Distributor distributor;

	public Date creationDate;
	
	public Date finalizedDate;
	
	public Date saleDate;

	public Date reviewDate;

	public String review;

    public Boolean reviewAccepted;

	@ManyToOne(fetch=FetchType.LAZY)
	public ER_User reviewUser;

	public Date policyValidity;
	
	public String branch;
	
	public String policy;

	public String policyFileName;

	public Boolean policyFileDownload;

	public String policyFilePath;

	public Boolean policyFileUpload;

	public String policyWebPath;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="incident", orphanRemoval=true, fetch=FetchType.LAZY)
	public Set<ER_Transaction_Status> transactionsStatus;
	
	@Column(name="emission_zone", nullable=true)
	public String emissionZone;

	public PaymentOption getSelectedPaymentOption() {
		List<PaymentOption> paymentOptions = selectedQuotation.quotationDetail.getPaymentOptions();
		ER_Payment_Frecuency currentFrecuency = selectedPaymentFrecuency;
		QuotationDetail currentQuotationDetail = selectedQuotation.quotationDetail;
		for(PaymentOption currentPayment: paymentOptions){
			if(currentPayment.frecuencyId == currentFrecuency.id) {

				currentPayment.netPrime = currentQuotationDetail.getVirginInternalPrime();
				if (currentPayment.netPrime.compareTo(currentQuotationDetail.getTotalMinimumPrime()) < 0) {
					currentPayment.netPrime = currentQuotationDetail.getTotalMinimumPrime();
				}
				return currentPayment;
			}
		}
		return null;
	}

	public BigDecimal getSelectedCoverage(String strCoverage) {
		List<CoverageCostCategory> categories = selectedQuotation.quotationDetail.getCategories();
		for(CoverageCostCategory currentCategory:categories ) {
			List<CoverageCost> costCategory = currentCategory.getCosts();
				for(CoverageCost currentCostCategory : costCategory){
					if(currentCostCategory.external && currentCostCategory.coverageName.toLowerCase().equals(strCoverage.toLowerCase())){
						return currentCostCategory.originalCost;
					}
				}
		}
		return null;
	}

    public static ER_Incident incidentFromJson(String json, boolean encrypted) {
		try{
			if(GeneralMethods.validateParameter(json)){
				if(encrypted){
					json = new Crypto().decryptAES(json);
				}
				return new Gson().fromJson(json, ER_Incident.class);
			}
		}catch(Exception e){
			Logger.error(e, "Error creating incident JSON");
		}
		return new ER_Incident();
	}
	
	public String toJsonString(boolean encrypt) {
		String json = new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy()).create().toJson(this);
		if(encrypt){
			return new Crypto().encryptAES(json);
		}
		return json;
	}
	
	public static synchronized Long generateIncidentNumber() {
		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.MILLISECOND, 0);
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH);
		int day = now.get(Calendar.DAY_OF_MONTH);
		
		long casesInDay = Incidents.dailyCorrelativeNumber; //ER_Incident.count("creationDate >= ?", now.getTime());
		Incidents.dailyCorrelativeNumber++;
		Logger.info("correlative: " + Incidents.dailyCorrelativeNumber);
		String numberString = String.format("%d%02d%02d%03d", year,(month+1),day,(casesInDay+1));
		
		return Long.parseLong(numberString);
	}
	
	public boolean isFinalized() {
		return (this.finalizedDate!=null);
	}
	
	public boolean canModifyQuotations() {
		
		if (this.status == null) {
			return false;
		}
		
		if (this.isFinalized()) {
			return false;
		}
		
		return (
				this.status.code == ERConstants.INCIDENT_STATUS_CREATED || 
				this.status.code == ERConstants.INCIDENT_STATUS_IN_PROGRESS ||
				this.status.code == ERConstants.INCIDENT_STATUS_INDICTED
				);
	}
	
	public int totalQuotations() {
		if (this.quotations!=null) {
			return this.quotations.size();
		}
		
		return 0;
	}
	
	public void completeTasks() {
		if (this.tasks!=null) {
			ER_Task_Status completeStatus = ER_Task_Status.find("code = ?", ERConstants.TASK_STATUS_COMPLETE).first(); 
			for (ER_Task task : this.tasks) {
				if (task.status.code!= ERConstants.TASK_STATUS_COMPLETE) {
					task.completionDate = new Date();
				}
				
				task.status = completeStatus;
				task.save();
			}
		}
	}
	
	public Set<ER_Transaction_Status> getTransactionsStatus(){
		if(transactionsStatus == null){
			transactionsStatus = new HashSet<ER_Transaction_Status>();
		}
		return transactionsStatus;
	}


	
	public ER_Transaction_Status getTransaction(Integer transaction){
		for(ER_Transaction_Status transactionStatus: transactionsStatus){
			if(transactionStatus.transactionCode.equals(transaction)){
				return transactionStatus;
			}
		}
		ER_Transaction_Status transactionStatus = new ER_Transaction_Status(transaction, this);
		transactionStatus.incident = this;
		getTransactionsStatus().add(transactionStatus);
		return transactionStatus;
	}

	public String getInitDate() {
        return new SimpleDateFormat("dd/MM/yyyy").format(new Date());
    }

    public String getPolicyValidityString() {
    	if(this.policyValidity != null)
			return  new SimpleDateFormat("dd/MM/yyyy").format(this.policyValidity);
    	else
    		return "";
	}

	public String getFinalizedDateCase() {
        if(this.finalizedDate != null)
            return this.finalizedDate.toString();
        else
            return "";
    }

    public String getReviewDateCase() {
        if(this.reviewDate != null)
            return this.reviewDate.toString();
        else
            return "";
    }

    public String getDictamen() {
        if(this.reviewAccepted != null && this.reviewAccepted)
            return "Aceptado";
        else if (this.reviewAccepted != null && !this.reviewAccepted)
            return "Denegado";
        else
            return "";
    }
}
