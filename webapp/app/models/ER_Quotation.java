package models;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import binders.MoneyBinder;
import ext.GsonExclude;
import helpers.ERConstants;
import helpers.GeneralMethods;
import objects.*;
import play.Logger;
import play.data.binding.As;
import play.data.validation.Min;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class ER_Quotation extends Model {
	
	@GsonExclude
	@ManyToOne(optional=false, cascade=CascadeType.ALL)
	public ER_Incident incident;
	@Required
	@ManyToOne(optional=false)
	public ER_Product product;

	@Column(precision=19, scale=2)
	public BigDecimal totalPrime;
	@Column(precision=19, scale=2)
	public BigDecimal discountedPrime;
	@Min(0)
	@As(binder=MoneyBinder.class)
	public BigDecimal carValue;
	public BigDecimal civilValue;
	public BigDecimal injuriesValue;
	public BigDecimal discount;
	@GsonExclude
	@ManyToOne
	public ER_User discountAuthorizedUser;
	public Date discountDate;
	public Date creationDate;
	@Valid
	@OneToMany(mappedBy="quotation", cascade=CascadeType.ALL)
	@Transient
	public List<ER_Quotation_Parameter> parameters;
	@Lob
	public String detail;
	@Transient
	public QuotationDetail quotationDetail;
	@Transient
	public Long facultative;
    @Transient
    public BigDecimal originalCarValue;
    @Transient
    public Integer loJack;

    public String theftDeductible;
    @Transient
    public LoJackOptions selectedLoJack;
    @Column
    public BigDecimal loJackRecharge;
    @Column
    private Boolean garanteedValue;



    public ER_Quotation() {
		this.discount = BigDecimal.ZERO;
	}

// -------------------------------------Methods----------------------------------------------
    public BigDecimal getOriginalCarValue() {
        if (originalCarValue!=null) {
            return originalCarValue;
        }

        return carValue;
    }

	public boolean hasCarValue() {
		
		if (this.product==null || this.product.id == null) {
			return false;
		}
		
		List <ER_Product_Coverage> coverages = ER_Product_Coverage.find("product.id = ?", product.id).fetch();
		for (ER_Product_Coverage coverage : coverages) {
			if (coverage.valueBase!=null && coverage.valueBase.code == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE) {
				return true;
			}
		}
		
		return false;
	}
	
	public void applyDiscount(BigDecimal discount) {
		if (discount!=null) {
			this.discount = this.quotationDetail.applyDiscount(discount);
			this.generateDetailJSON();
			this.discountedPrime = this.quotationDetail.getSinglePaymentPrime();
		}
	}
	
	public boolean carValueIsChanged() {
		ER_Vehicle_Value originalCarValue = ER_Vehicle_Value.find("line = ? and year = ?", incident.vehicle.line, incident.vehicle.year).first();
		if (originalCarValue==null) {
			return true;
		}
		
		return (originalCarValue.value.compareTo(this.carValue)!=0);
	}
	
	public boolean agreedValue() {
		
		if (this.carValue==null) {
			return false;
		}
		
		BigDecimal vehicleValue = this.quotationDetail.getVehicleValue();
		if (vehicleValue==null) {
			return false;
		}
		
		return (vehicleValue.compareTo(this.carValue)==0);
	}
	
	public void setValuesOfQuotation(ER_Quotation quotation) {
		
		if (quotation.product != null) {
			if (quotation.product.id != null) {
				ER_Product product = ER_Product.findById(quotation.product.id);
				quotation.product = product;
			} else {
				quotation.product = null;
			}
		}
		
		if (quotation.parameters!=null) {
			for (ER_Quotation_Parameter parameter : quotation.parameters) {
				
				if (parameter.productCoverage!=null && parameter.productCoverage.id !=null) {
					ER_Product_Coverage productCoverage = ER_Product_Coverage.findById(parameter.productCoverage.id);
					parameter.productCoverage = productCoverage;
				}
				
				if (parameter.coverageValue!=null) {
					if (parameter.coverageValue.id != null) {
						ER_Product_Coverage_Value coverageValue = ER_Product_Coverage_Value.findById(parameter.coverageValue.id);
						parameter.coverageValue = coverageValue;
					} else {
						if (parameter.productCoverage.optional) {
							parameter.applyInsurance = false;
						}
						parameter.coverageValue = null;
					}
				}
			}
		}
	}
	
	public static ER_Quotation quotationFromJson(String json, boolean encrypted) {
		try {
			if (GeneralMethods.validateParameter(json)) {
				Gson gson = new Gson();
				if (encrypted) {
					json = new Crypto().decryptAES(json);
				}
				return gson.fromJson(json, ER_Quotation.class);
			}
		} catch (Exception e) {
			Logger.error(e, "Error generating quotation from JSON");
		}

		return new ER_Quotation();
	}
	
	public String toJsonString(boolean encrypt) {	
		
		Map<String, Object> map = this.toMap();
		
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
		String json = gson.toJson(map, type).toString();
		
		if (encrypt) {
			return new Crypto().encryptAES(json);
		}
		
		return json;
		
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		
		if (this.product!=null) {
			map.put("product", this.product.toMap());
		}
		
		if (this.parameters!=null && !this.parameters.isEmpty()) {
			List<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
			for (ER_Quotation_Parameter parameter : this.parameters) {
				parameters.add(parameter.toMap());
			}
			map.put("parameters", parameters);
		}
		
		if (this.facultative!=null) {
			map.put("facultative", this.facultative);
		}
		
		if (this.totalPrime!=null) {
			map.put("totalPrime", this.totalPrime);
		}
		
		if (this.carValue!=null) {
			map.put("carValue", this.carValue);
		}
		
		if (this.civilValue!=null) {
			map.put("civilValue", this.civilValue);
		}

		if (this.injuriesValue!=null) {
			map.put("injuriesValue", this.injuriesValue);
		}
		
		if (this.discount!=null) {
			map.put("discount", this.discount);
		}
		
		if (this.discountedPrime!=null) {
			map.put("discountedPrime", this.discountedPrime);
		}
		
		if (this.detail!=null) {
			map.put("detail", this.detail);
		}

		if (this.selectedLoJack != null) {
		    map.put("selectedLoJack", this.selectedLoJack);
        }
        if (this.loJackRecharge != null) {
		    map.put("loJackRecharge", this.loJackRecharge);
        }
		if (this.garanteedValue != null) {
			map.put("garanteedValue", this.garanteedValue);
		}

		return map;
	}
	
	@PrePersist
	@PreUpdate
	public void generateDetailJSON() {
		if (this.quotationDetail!=null) {
			Gson gson = new Gson();
			this.detail = gson.toJson(this.quotationDetail, QuotationDetail.class).toString();
		}
	}
	
	@PostLoad
	public void loadDetailJSON() {
		if (this.detail!=null) {
			Gson gson = new Gson();
			this.quotationDetail = gson.fromJson(this.detail, QuotationDetail.class);
		}
	}
	
	public BigDecimal getValueForCoverage(ER_Coverage coverage) {
		if (coverage==null) {
			return null;
		}
		Logger.info("DETAIL: " + (this.quotationDetail != null));
		/*
		if (this.parameters!=null && !this.parameters.isEmpty()) {
			for (ER_Quotation_Parameter parameter : this.parameters) {
				if (parameter.productCoverage.coverage.id.equals(coverage.id)) {
					return parameter.value;
				}
			}
		}*/

		if (this.quotationDetail != null && !this.quotationDetail.getCategories().isEmpty()) {
		    for (CoverageCostCategory category : this.quotationDetail.getCategories()) {
                for (CoverageCost cost : category.getCosts()) {
                    if (coverage.getId().equals(cost.coverageId)) {
                        return cost.coverage;
                    }
                }
            }
        }
		
		return null;
	}
	
	public BigDecimal convertCarValue(BigDecimal value) {
		
		if (value==null) {
			return null;
		}
		
		BigDecimal exchangeRate = null;
		if (this.product!=null && this.product.currency!=null) {
			exchangeRate = this.product.currency.exchangeRate;
		}
		
		if (exchangeRate ==null) {
			exchangeRate = BigDecimal.ONE;
		}
		
		return value.divide(exchangeRate, BigDecimal.ROUND_HALF_UP);
	}
	
	public BigDecimal convertCarValueToUSD(BigDecimal value) {
		
		ER_Currency usd = ER_Currency.find("code = ?", ERConstants.CURRENCY_USD).first();
		
		if (this.product.currency!=null && this.product.currency.exchangeRate!=null) {
			value = value.multiply(this.product.currency.exchangeRate);
		}
		
		if (usd!=null && usd.exchangeRate!=null) {
			return value.divide(usd.exchangeRate, BigDecimal.ROUND_HALF_UP);
		}
		
		return null;
	}

    public BigDecimal getLoJackDeductible() {
        Logger.info("LoJack Recharge: " + (loJackRecharge != null) + " - " + loJackRecharge);
	    if (loJackRecharge != null) {
	        return loJackRecharge;
        }
        return null;
    }

    public Boolean getGaranteedValue() {
        return garanteedValue;
    }

    public void setGaranteedValue(Boolean garanteedValue) {
        this.garanteedValue = garanteedValue;
    }

    //---Calcula los deducibles de la seccion I: retorna un HashMap con los nombres de las categorias como key y como valor el deducible ----------------------------------------
    public Map<String, Double> Deductibles(){
        ER_General_Configuration conf = ER_General_Configuration.find("").first();
        Map<String, Double> map = new HashMap<>();
        List<CoverageCostCategory> coverageCategory = quotationDetail.getCategories();  //name, description, List<CoverageCost>
        List<CoverageCost> coverageCost = coverageCategory.get(0).getCosts(); //Order, id, name, descrip, totalCoverage,

        for (int i=0; i < coverageCost.size(); i++) {
            CoverageCost cost = coverageCost.get(i);
            BigDecimal value = null; // valor de deducible
			Double porcentaje = null; // porcentaje de deducible
            if (cost.coverageId == conf.injuriesCoverage.id){
                BigDecimal totalCoverage;
                if (incident.vehicle.numberOfPassengers != null & incident.vehicle.numberOfPassengers > 0) {
                    totalCoverage = cost.coverage.multiply(BigDecimal.valueOf(incident.vehicle.numberOfPassengers));
                    porcentaje = cost.damagesDeductible.doubleValue();
                    value = totalCoverage;
                }else{
                    totalCoverage = cost.coverage;
                    value = totalCoverage;
                }
                if(cost.damagesDeductible != null){
                    BigDecimal deductibleInjuries = totalCoverage.multiply(cost.damagesDeductible.divide(BigDecimal.valueOf(100)));
                    value = deductibleInjuries;
                }
            }
            if (cost.coverageId == conf.totalTheftCoverage.id){
                BigDecimal deductibleTheft;
                if (quotationDetail.getTheftDeductible() != null){
                    if(loJack != null){
                        deductibleTheft = cost.coverage.multiply(getLoJackDeductible().divide(BigDecimal.valueOf(100)));
                        value = deductibleTheft;
                        porcentaje = getLoJackDeductible().doubleValue();
                    }else {
                        deductibleTheft = cost.coverage.multiply(quotationDetail.getTheftDeductible().divide(BigDecimal.valueOf(100)));
                        value = deductibleTheft;
                        porcentaje = quotationDetail.getTheftDeductible().doubleValue();
                    }
                }else {
                    if (cost.damagesDeductible != null){
                        if (getLoJackDeductible() != null){
                            deductibleTheft = cost.coverage.multiply(getLoJackDeductible().divide(BigDecimal.valueOf(100)));
                            value = deductibleTheft;
                            porcentaje = getLoJackDeductible().doubleValue();
                        }else {
                            deductibleTheft = cost.coverage.multiply(cost.damagesDeductible.divide(BigDecimal.valueOf(100)));
                            value = deductibleTheft;
                            porcentaje = cost.damagesDeductible.doubleValue();
                        }
                    }
                }
            }else if(cost.coverage.compareTo(BigDecimal.ZERO) > 0){
                BigDecimal deductible;
                if (cost.damagesDeductible != null){
                    deductible = cost.coverage.multiply(cost.damagesDeductible.divide(BigDecimal.valueOf(100)));
                    value = deductible;
                    porcentaje = cost.damagesDeductible.doubleValue();
                }
            }
            map.put(cost.coverageName.toLowerCase().trim(), porcentaje);
        }
        return map;
	}

	public BigDecimal getSelectedTotalPrime() {
    	for (PaymentOption option : quotationDetail.getPaymentOptions()) {
    		if (option.frecuencyId == incident.selectedPaymentFrecuency.id) {
    			return option.amount.multiply(new BigDecimal(option.numberOfPayments));
			}
		}
		return discountedPrime;
	}
}
