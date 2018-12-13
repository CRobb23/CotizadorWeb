package com.digitalgeko.servicebus.model.rest.request;

import java.math.BigDecimal;
import java.util.List;

public class PrimeInputRestRequest {

	private String quoteNumber;
	private String currency;
	private String branch;
	private String policy;
	private String flgsuma;
	private Integer frequencyPayment;
	private BigDecimal totalSumInsured;
	private BigDecimal totalPrime;
	private BigDecimal totalRoadAssistance;
	private Integer totalNumberPayments;
	
	private List<Prime> primes;
	
    public static class Prime{
		private Integer numberPayment;
		private BigDecimal sumAssured;
		private BigDecimal netPrime;
		private BigDecimal emissionRihts;
		private BigDecimal surcharges;
		private BigDecimal iAttend;
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
