package com.digitalgeko.servicebus.model.rest.request;

import java.math.BigDecimal;
import java.util.List;

public class CoveragesListInputRestRequest {

	private String quoteNumber;
	private String currency;
	private String codeAgent;
	private String typeClient;
	
	private List<Coverage> coverages;
	
    public static class Coverage{
		private String coverage;
		private BigDecimal rate;
		private BigDecimal sumAssured;
		private BigDecimal prime;
		private BigDecimal minimun;
		private BigDecimal percDeductible;
		private BigDecimal recgoCoverage;
		private BigDecimal desctoCoverage;
		private BigDecimal declarationRate;
		private BigDecimal discount;
		
		public String getCoverage() {
			return coverage;
		}
		public void setCoverage(String coverage) {
			this.coverage = coverage;
		}
		public BigDecimal getRate() {
			return rate;
		}
		public void setRate(BigDecimal rate) {
			this.rate = rate;
		}
		public BigDecimal getSumAssured() {
			return sumAssured;
		}
		public void setSumAssured(BigDecimal sumAssured) {
			this.sumAssured = sumAssured;
		}
		public BigDecimal getPrime() {
			return prime;
		}
		public void setPrime(BigDecimal prime) {
			this.prime = prime;
		}
		public BigDecimal getMinimun() {
			return minimun;
		}
		public void setMinimun(BigDecimal minimun) {
			this.minimun = minimun;
		}
		public BigDecimal getPercDeductible() {
			return percDeductible;
		}
		public void setPercDeductible(BigDecimal percDeductible) {
			this.percDeductible = percDeductible;
		}
		public BigDecimal getRecgoCoverage() {
			return recgoCoverage;
		}
		public void setRecgoCoverage(BigDecimal recgoCoverage) {
			this.recgoCoverage = recgoCoverage;
		}
		public BigDecimal getDesctoCoverage() {
			return desctoCoverage;
		}
		public void setDesctoCoverage(BigDecimal desctoCoverage) {
			this.desctoCoverage = desctoCoverage;
		}
		public BigDecimal getDeclarationRate() {
			return declarationRate;
		}
		public void setDeclarationRate(BigDecimal declarationRate) {
			this.declarationRate = declarationRate;
		}
		public BigDecimal getDiscount() {
			return discount;
		}
		public void setDiscount(BigDecimal discount) {
			this.discount = discount;
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
	public String getCodeAgent() {
		return codeAgent;
	}
	public void setCodeAgent(String codeAgent) {
		this.codeAgent = codeAgent;
	}
	public String getTypeClient() {
		return typeClient;
	}
	public void setTypeClient(String typeClient) {
		this.typeClient = typeClient;
	}
	public List<Coverage> getCoverages() {
		return coverages;
	}
	public void setCoverages(List<Coverage> coverages) {
		this.coverages = coverages;
	}
}
