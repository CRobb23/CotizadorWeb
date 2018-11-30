package controllers;

import helpers.ERConstants;

import java.util.ArrayList;
import java.util.List;

import models.ER_Coverage;
import models.ER_Currency;
import models.ER_Facultative_Deductible;
import models.ER_General_Configuration;
import models.ER_Payment_Frecuency;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.mvc.With;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminConfiguration extends AdminBaseController {

    public static void index() {
    	renderIndex();
    }
    
    private static void renderIndex() {
    	
    	if (renderArgs.get("facultative") == null) {
    		List<ER_Facultative_Deductible> facultative = ER_Facultative_Deductible.findAll();
    		renderArgs.put("facultative", facultative);
    	}
    	
    	List<ER_Payment_Frecuency> payments = ER_Payment_Frecuency.findAll();
    	renderArgs.put("payments", payments);
    	
    	List<ER_Coverage> valuecoverages = ER_Coverage.find("type.code = ?", ERConstants.CALCULATION_TYPE_RANGE).fetch();
    	renderArgs.put("valuecoverages", valuecoverages);
    	
    	List<ER_Coverage> coverages = ER_Coverage.find("type.code = ?", ERConstants.CALCULATION_TYPE_AMOUNT).fetch();
    	renderArgs.put("coverages", coverages);
    	
    	List<ER_Coverage> fixedamountscoverages = ER_Coverage.find("type.code = ?", ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS).fetch();
    	renderArgs.put("fixedamountscoverages", fixedamountscoverages);
    	
    	if (renderArgs.get("configuration") == null) {
	    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
	    	if (configuration!=null) {
	    		renderArgs.put("configuration", configuration);
	    	}
    	}
    	
    	ER_Currency defaultCurrency = ER_Currency.find("code = ?", ERConstants.CURRENCY_GTQ).first();
    	renderArgs.put("defaultCurrency", defaultCurrency);
    	
    	List<ER_Currency> currencies = ER_Currency.find("code != ? order by code", ERConstants.CURRENCY_GTQ).fetch();
    	renderArgs.put("currencies", currencies);

    	List<Long> timeoutList = new ArrayList<>();
    	for (long i=5; i<= 60; i=i+5){
    		timeoutList.add(i);
		}
    	renderArgs.put("timeoutList", timeoutList);
    	
    	render("AdminConfiguration/index.html");
    }
    
    public static void saveConfiguration(@Valid ER_General_Configuration configuration, @Valid List<ER_Payment_Frecuency> payments, @Valid List<ER_Currency> currencies, @Valid List<ER_Facultative_Deductible> facultative) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		
    		renderArgs.put("error", Messages.get("configuration.form.validationerror"));
    		renderArgs.put("configuration", configuration);
    		renderArgs.put("facultative", facultative);
    		renderIndex();
    		
    	} else {
    		
    		try {
    			
    			for (ER_Payment_Frecuency payment : payments) {
    				ER_Payment_Frecuency currentPayment = ER_Payment_Frecuency.findById(payment.id);
    				if (currentPayment!=null) {
    					currentPayment.fractioningFee = payment.fractioningFee;
    					currentPayment.save();
    				}
    			}
    			
    			if (facultative==null) {
    				List<ER_Facultative_Deductible> current = ER_Facultative_Deductible.findAll();
    				for (ER_Facultative_Deductible deductible : current) {
    					deductible.delete();
    				}
    			} else {
    				List<ER_Facultative_Deductible> deleted = ER_Facultative_Deductible.findAll();
    				
	    			for (ER_Facultative_Deductible deductible : facultative) {
	    				if (deductible.id!=null) {
	    					ER_Facultative_Deductible currentDeductible = ER_Facultative_Deductible.findById(deductible.id);
	    					deleted.remove(currentDeductible);
	    					currentDeductible.deductibleIncrease = deductible.deductibleIncrease;
	    					currentDeductible.primeDiscount = deductible.primeDiscount;
	    					currentDeductible.save();
	    				} else {
	    					deductible.save();
	    				}
	        		}
	    			
	    			if(!deleted.isEmpty()) {
		    			for (ER_Facultative_Deductible deductible : deleted) {
		    				deductible.delete();
		    			}
	    			}
    			}
    			
    			for (ER_Currency currency : currencies) {
    				ER_Currency currentCurrency = ER_Currency.findById(currency.id);
    				if (currentCurrency!=null) {
    					currentCurrency.exchangeRate = currency.exchangeRate;
    					currentCurrency.save();
    				}
    			}
    			
	    		ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
	    		if (currentConfiguration == null) {
	    			configuration.save();
	    		} else {
	    			currentConfiguration.setParametersInConfiguration(configuration);
	    			currentConfiguration.save();
	    		}
	    		flash.success(Messages.get("configuration.form.success"));
    		} catch (Exception e) {
    			Logger.error(e, "General configuration save error");
    			flash.error(Messages.get("configuration.form.error"));
    		}
    	}
    	
    	index();
    	
    }

}
