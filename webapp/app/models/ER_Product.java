package models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import helpers.ERConstants;
import play.Logger;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class ER_Product extends Model {
	
	@Required
	@MaxSize(75)
	@Column(length=75)
	public String name;
	
	@Required
	@MaxSize(150)
	@Column(length=150)
	public String description;
	
	@Required
	public Boolean active;
	
	@Required
	public Boolean hasFacultative;
	
	@Required
	@ManyToOne
	public ER_Currency currency;
	
	public boolean discountedByRange;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL)
	public List<ER_Product_Coverage> coverages;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
	public List<ER_Product_Discount> discounts;
	
	@OneToMany(mappedBy="product", cascade=CascadeType.ALL, orphanRemoval=true)
	public List<ER_Product_Discount_Range> rangeDiscounts;
	
	@Required
	@MaxSize(10)
	@Column(length=10)
	public String remoteSystemCode;
	
	public String rateTypeTransferCode;
	
	@ManyToMany
	@JoinTable(name = "ER_Product_Channels", 
		joinColumns = {@JoinColumn(name = "channel_id", nullable = false) },
		inverseJoinColumns = {@JoinColumn(name = "product_id", nullable = false)})
	public List<ER_Channel> channels;
	
	@ManyToMany
	@JoinTable(name = "ER_Product_Distributors", 
		joinColumns = {@JoinColumn(name = "distributor_id", nullable = false) },
		inverseJoinColumns = {@JoinColumn(name = "product_id", nullable = false)})
	public List<ER_Distributor> distributors;
	
	@Lob
	public String additionalBenefits;

	@Column(precision=19, scale=2)
	public BigDecimal montoRestarSumaAseg ;// =new BigDecimal(0.00);

	@Column(precision=19, scale=2)
	public BigDecimal montoAgregarPrima ;//=new BigDecimal(0.00);


	@Transient
	public Boolean hasDiscount; 
	
	public boolean hasAvailableDiscount() {
		
		if (hasDiscount != null) {
			return hasDiscount;
		}
		
		return (discounts!=null && !discounts.isEmpty()) || (rangeDiscounts!=null && !rangeDiscounts.isEmpty());
	}
	
	public ER_Product(ER_Product product) {
		
		if (product==null) {
			return;
		}
		
		this.name = product.name;
		this.description = product.description;
		this.active = product.active;
		this.discountedByRange = product.discountedByRange;
		this.remoteSystemCode = product.remoteSystemCode;
		this.currency = product.currency;
		this.montoAgregarPrima = product.montoAgregarPrima;
		this.montoRestarSumaAseg = product.montoRestarSumaAseg;

		
		if (product.coverages!=null && !product.coverages.isEmpty()) {
			List<ER_Product_Coverage> coveragesCopy = new ArrayList<ER_Product_Coverage>();
			for (ER_Product_Coverage coverage : product.coverages) {
				ER_Product_Coverage coverageCopy = new ER_Product_Coverage(coverage);
				coverageCopy.product = this;
				coveragesCopy.add(coverageCopy);
			}
			this.coverages = coveragesCopy;
		}
		
		if (product.discounts!=null && !product.discounts.isEmpty()) {
			List<ER_Product_Discount> discountsCopy = new ArrayList<ER_Product_Discount>();
			for (ER_Product_Discount discount : product.discounts) {
				ER_Product_Discount discountCopy = new ER_Product_Discount(discount);
				discountCopy.product = this;
				discountsCopy.add(discountCopy);
			}
			this.discounts = discountsCopy;
		}
		
		if (product.rangeDiscounts!=null && !product.rangeDiscounts.isEmpty()) {
			List<ER_Product_Discount_Range> rangeDiscountsCopy = new ArrayList<ER_Product_Discount_Range>();
			for (ER_Product_Discount_Range discount : product.rangeDiscounts) {
				ER_Product_Discount_Range discountCopy = new ER_Product_Discount_Range(discount);
				discountCopy.product = this;
				rangeDiscountsCopy.add(discountCopy);
			}
			this.rangeDiscounts = rangeDiscountsCopy;
		}
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		if (this.id!=null) {
			map.put("id", this.id);
		}
		if (this.name!=null) {
			map.put("name", this.name);
		}
		if (this.currency!=null) {
			map.put("currency", this.currency.toMap());
		}
		
		if (this.hasFacultative!=null) {
			map.put("hasFacultative", this.hasFacultative);
		}
		
		if (this.hasDiscount==null) {
			this.hasDiscount = hasAvailableDiscount();
		}
		
		map.put("hasDiscount", this.hasDiscount);

		if (this.montoAgregarPrima!=null) {
			map.put("montoAgregarPrima", this.montoAgregarPrima);
		}

		if (this.montoRestarSumaAseg!=null) {
			map.put("montoRestarSumaAseg", this.montoRestarSumaAseg);
		}


		return map;
	}
	
	public ER_Product_Discount discountById(Long id) {
		if (this.discounts!=null && !this.discounts.isEmpty()) {
			for (ER_Product_Discount discount : this.discounts) {
				if (discount.id.equals(id)) {
					return discount;
				}
			}
		}
		
		return null;
	}
	
	public ER_Product_Discount_Range discountRangeById(Long id) {
		if (this.rangeDiscounts!=null && !this.rangeDiscounts.isEmpty()) {
			for (ER_Product_Discount_Range discount : this.rangeDiscounts) {
				if (discount.id.equals(id)) {
					return discount;
				}
			}
		}
		
		return null;
	}
	
	public void setCoveragesValues(List<ER_Product_Coverage> coverages, boolean save) {
		//Remove null values from coverages array
    	if (coverages!=null) {
    		coverages.removeAll(Collections.singleton(null));
    		
    		//Delete coverages not present in the current list
			if (this.coverages !=null) {
    			if (save) {
    				List<ER_Product_Coverage> deletedCoverages = new ArrayList<ER_Product_Coverage>(this.coverages);
    				for (int i = deletedCoverages.size() -1; i>=0; i--) {
    					ER_Product_Coverage currentValue = deletedCoverages.get(i);
    					for (ER_Product_Coverage newValue : coverages) {
    						if (currentValue.id.equals(newValue.id)) {
    							deletedCoverages.remove(i);
    						}
    					}
    				}
    				
    				for (ER_Product_Coverage deletedValue : deletedCoverages) {
    					Logger.info("Deleting coverage: " + deletedValue);
    					this.coverages.remove(deletedValue);
    					deletedValue.delete();
    				}
				}
			}
    		
			List<ER_Product_Coverage> newCoverages = new ArrayList<ER_Product_Coverage>();
    		for (ER_Product_Coverage productCoverage : coverages) {
    			//Remove null values from array
    			if (productCoverage.values!=null) {
        			productCoverage.values.removeAll(Collections.singleton(null));
        		}
    			
    			ER_Base_Field base = null;
    			if (productCoverage.valueBase!=null) {
    				base = ER_Base_Field.findById(productCoverage.valueBase.id);
    			} else {
    				base = ER_Base_Field.find("code = ?", ERConstants.CALCULATION_BASE_GTQ).first();
    			}
    			
    			ER_Coverage coverage = ER_Coverage.findById(productCoverage.coverage.id);
    			
    			List<ER_Product_Coverage_Value> productCoverageValues = productCoverage.values;
    			if (productCoverage.id!=null) {
    				
    				Boolean optional = productCoverage.optional;
    				productCoverage =  ER_Product_Coverage.findById(productCoverage.id);
    				productCoverage.optional = optional;
    				
    				//Delete values not in the current list
    				if (save) {
	    				List<ER_Product_Coverage_Value> deletedValues = new ArrayList<ER_Product_Coverage_Value>(productCoverage.values);
	    				for (int i = deletedValues.size() -1; i>=0; i--) {
	    					ER_Product_Coverage_Value currentValue = deletedValues.get(i);
	    					for (ER_Product_Coverage_Value newValue : productCoverageValues) {
	    						if (currentValue.id.equals(newValue.id)) {
	    							deletedValues.remove(i);
	    						}
	    					}
	    				}
	    				
	    				for (ER_Product_Coverage_Value deletedValue : deletedValues) {
	    					Logger.info("Deleting coverage value: " + deletedValue);
	    					productCoverage.values.remove(deletedValue);
	    					deletedValue.delete();
	    				}
    				}
    			}
    			
    			productCoverage.valueBase = base;
    			productCoverage.coverage = coverage;
    			productCoverage.product = this;
    			
    			if (save) {
    				productCoverage.save();
    			}
    			
        		for (ER_Product_Coverage_Value value : productCoverageValues) {
        			
        			List<ER_Product_Coverage_Class_Value> classValues = value.values;
        			
        			if (value.id !=null) {
        				BigDecimal lowRange = value.lowRange;
            			BigDecimal highRange = value.highRange;
            			String optionName = value.optionName;
        				value = ER_Product_Coverage_Value.findById(value.id);
        				value.lowRange = lowRange;
        				value.highRange = highRange;
        				value.optionName = optionName;
        			}
        			
        			value.productCoverage = productCoverage;
        			
        			if (save) {
        				value.save();
        			}
        			
        			for (ER_Product_Coverage_Class_Value classValue : classValues) {
        					
        				if (classValue.id != null) {
        					BigDecimal decimalValue = classValue.value;
        					classValue = ER_Product_Coverage_Class_Value.findById(classValue.id);
        					classValue.value = decimalValue;
        				}
        				classValue.productCoverageValue = value;
        				if (save) {
        					classValue.save();
        				}
        			}
        		}
        		
        		newCoverages.add(productCoverage);
        	}
    		
    		this.coverages = newCoverages;
    	} else {
    		if (save) {
				List<ER_Product_Coverage> deletedCoverages = new ArrayList<ER_Product_Coverage>(this.coverages);
				if (deletedCoverages!=null && !deletedCoverages.isEmpty()) {
					for (ER_Product_Coverage deletedValue : deletedCoverages) {
						Logger.info("Deleting coverage: " + deletedValue);
						this.coverages.remove(deletedValue);
						deletedValue.delete();
					}
				}
			}
    	}
	}
}
