package models.ws;

import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(name="listaPrima")
@XmlAccessorType(XmlAccessType.FIELD)
public class PrimeRequest {

	@XmlTransient
	public static final Integer TRANSACTION = 745;
	
	@XmlElement(name="numeroCotizacion")
	private String quoteNumber;
	@XmlElement(name="moneda")
	private String currency;
	@XmlElement(name="ramo")
	private String branch;
	@XmlElement(name="poliza")
	private String policy;
	@XmlElement(name="flgsuma")
	private String flgsuma;
	@XmlElement(name="frecuenciaPago")
	private Integer frequencyPayment;
	@XmlElement(name="totalSumaAsegurada")
	private BigDecimal totalSumInsured;
	@XmlElement(name="totalPrima")
	private BigDecimal totalPrime;
	@XmlElement(name="totalAsistenciaVial")
	private BigDecimal totalRoadAssistance;
	@XmlElement(name="totalNumeroPagos")
	private Integer totalNumberPayments;
	
	@XmlElementWrapper(name="lista")
    @XmlElement(name="primas")
	private List<Prime> primes;
	
	@XmlAccessorType(XmlAccessType.FIELD)
    public static class Prime{
		@XmlElement(name="numeroPago")
		private Integer numberPayment;
		@XmlElement(name="sumaasegurada")
		private BigDecimal sumAssured;
		@XmlElement(name="primaNeta")
		private BigDecimal netPrime;
		@XmlElement(name="derechosEmision")
		private BigDecimal emissionRihts;
		@XmlElement(name="recargos")
		private BigDecimal surcharges;
		@XmlElement(name="asisto")
		private BigDecimal iAttend;
		@XmlElement(name="iva")
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
