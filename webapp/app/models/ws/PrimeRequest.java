package models.ws;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.List;

public class PrimeRequest {

	public static final int TRANSACTION = 745;

	@Expose
	private String quoteNumber;
	@Expose
	private String currency;
	@Expose
	private String branch;
	@Expose
	private String policy;
	@Expose
	private String flgsuma;
	@Expose
	private Integer frequencyPayment;
	@Expose
	private BigDecimal totalSumInsured;
	@Expose
	private BigDecimal totalPrime;
	@Expose
	private BigDecimal totalRoadAssistance;
	@Expose
	private Integer totalNumberPayments;

	@Expose
	private List<Prime> primes;
	
	public static class Prime{
		@Expose
		private Integer numberPayment;
		@Expose
		private BigDecimal sumAssured;
		@Expose
		private BigDecimal netPrime;
		@Expose
		private BigDecimal emissionRihts;
		@Expose
		private BigDecimal surcharges;
		@Expose
		private BigDecimal iAttend;
		@Expose
		private BigDecimal iva;
		
		public Integer getNumberPayment() {
			return numberPayment;
		}
		public void setNumberPayment(Integer numberPayment) {
			this.numberPayment = numberPayment;
		}
		public BigDecimal getSumAssured() {
			return sumAssured;
		}
		public void setSumAssured(BigDecimal sumAssured) {
			this.sumAssured = sumAssured;
		}
		public BigDecimal getNetPrime() {
			return netPrime;
		}
		public void setNetPrime(BigDecimal netPrime) {
			this.netPrime = netPrime;
		}
		public BigDecimal getEmissionRihts() {
			return emissionRihts;
		}
		public void setEmissionRihts(BigDecimal emissionRihts) {
			this.emissionRihts = emissionRihts;
		}
		public BigDecimal getSurcharges() {
			return surcharges;
		}
		public void setSurcharges(BigDecimal surcharges) {
			this.surcharges = surcharges;
		}
		public BigDecimal getiAttend() {
			return iAttend;
		}
		public void setiAttend(BigDecimal iAttend) {
			this.iAttend = iAttend;
		}
		public BigDecimal getIva() {
			return iva;
		}
		public void setIva(BigDecimal iva) {
			this.iva = iva;
		}
	}
	public String getQuoteNumber() {
		return quoteNumber;
	}
	public void setQuoteNumber(String quoteNumber) {
		this.quoteNumber = quoteNumber;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getBranch() {
		return branch;
	}
	public void setBranch(String branch) {
		this.branch = branch;
	}
	public String getPolicy() {
		return policy;
	}
	public void setPolicy(String policy) {
		this.policy = policy;
	}
	public String getFlgsuma() {
		return flgsuma;
	}
	public void setFlgsuma(String flgsuma) {
		this.flgsuma = flgsuma;
	}
	public Integer getFrequencyPayment() {
		return frequencyPayment;
	}
	public void setFrequencyPayment(Integer frequencyPayment) {
		this.frequencyPayment = frequencyPayment;
	}
	public BigDecimal getTotalSumInsured() {
		return totalSumInsured;
	}
	public void setTotalSumInsured(BigDecimal totalSumInsured) {
		this.totalSumInsured = totalSumInsured;
	}
	public BigDecimal getTotalPrime() {
		return totalPrime;
	}
	public void setTotalPrime(BigDecimal totalPrime) {
		this.totalPrime = totalPrime;
	}
	public BigDecimal getTotalRoadAssistance() {
		return totalRoadAssistance;
	}
	public void setTotalRoadAssistance(BigDecimal totalRoadAssistance) {
		this.totalRoadAssistance = totalRoadAssistance;
	}
	public Integer getTotalNumberPayments() {
		return totalNumberPayments;
	}
	public void setTotalNumberPayments(Integer totalNumberPayments) {
		this.totalNumberPayments = totalNumberPayments;
	}
	public List<Prime> getPrimes() {
		return primes;
	}
	public void setPrimes(List<Prime> primes) {
		this.primes = primes;
	}
}
