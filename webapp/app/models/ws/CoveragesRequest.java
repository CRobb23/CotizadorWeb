package models.ws;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

public class CoveragesRequest {

	public static final int TRANSACTION = 740;

	@Expose
	private String quoteNumber;
	@Expose
	private String currency;
	@Expose
	private String codeAgent;
	@Expose
	private String typeClient;

	@Expose
	private List<Coverage> coverages;
	
	public static class Coverage{
		@Expose
		private String coverage;
		@Expose
		private BigDecimal rate;
		@Expose
		private BigDecimal sumAssured;
		@Expose
		private BigDecimal prime;
		@Expose
		private BigDecimal minimun;
		@Expose
		private BigDecimal percDeductible;
		@Expose
		private BigDecimal recgoCoverage;
		@Expose
		private BigDecimal desctoCoverage;
		@Expose
		private BigDecimal declarationRate;
		@Expose
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
