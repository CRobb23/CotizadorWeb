package helpers;

import extensions.StringFormatExtensions;
import models.*;
import objects.CoverageCost;
import objects.ER_Quotation_Parameter;
import objects.QuotationDetail;
import play.Logger;

import java.math.BigDecimal;
import java.util.List;

public class QuotationHelper {
	
	public static ER_Product_Coverage_Deductible getDeductible(ER_Product_Coverage productCoverage, ER_Vehicle_Class vehicleClass) {
		for (ER_Product_Coverage_Deductible deductible : productCoverage.deductibles) {
			if (deductible.vehicleClass.equals(vehicleClass)) {
				return deductible;
			}
		}
		return null;
	}
	
	public static BigDecimal getParameterValue(ER_Quotation_Parameter parameter, ER_Product_Coverage productCoverage, ER_Quotation quotation) {
		
		if (parameter==null) {
			return BigDecimal.ZERO;
		}
		
		switch (productCoverage.coverage.type.code) {
		case ERConstants.CALCULATION_TYPE_AMOUNT: {
			if (parameter.value==null) {
				return BigDecimal.ZERO;
			}
			return parameter.value;
		}
		case ERConstants.CALCULATION_TYPE_RANGE: {
			if (parameter.value==null) {
				return BigDecimal.ZERO;
			}
			return parameter.value;
		}
		case ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS: {
			if (parameter.coverageValue==null || parameter.coverageValue.lowRange==null) {
				return BigDecimal.ZERO;
			}
			return parameter.coverageValue.lowRange;
		}
		}
		
		return BigDecimal.ZERO;
	}
	
	public static ER_Product_Coverage_Class_Value getVehicleClassValue(ER_Quotation_Parameter parameter, ER_Product_Coverage productCoverage, ER_Vehicle_Class vehicleClass, BigDecimal carValue) {
		
		switch (productCoverage.coverage.type.code) {
		
		case ERConstants.CALCULATION_TYPE_AMOUNT: {
			if (!productCoverage.values.isEmpty()) {
				ER_Product_Coverage_Value value =  productCoverage.values.get(0);
				for (ER_Product_Coverage_Class_Value cValue : value.values) {
					if (cValue.vehicleClass.equals(vehicleClass)) {
						return cValue;
					}
				}
			}
		}
			break;
		case ERConstants.CALCULATION_TYPE_FIXED: {
			if (!productCoverage.values.isEmpty()) {
				ER_Product_Coverage_Value value =  productCoverage.values.get(0);
				for (ER_Product_Coverage_Class_Value cValue : value.values) {
					if (cValue.vehicleClass.equals(vehicleClass)) {
						return cValue;
					}
				}
			}
		} 
			break;
		case ERConstants.CALCULATION_TYPE_FIXED_AMOUNTS: {
			if (parameter==null || parameter.coverageValue==null) {
				return null;
			}
			
			for (ER_Product_Coverage_Class_Value cValue : parameter.coverageValue.values) {
				if (cValue.vehicleClass.equals(vehicleClass)) {
					return cValue;
				}
			}
		} 
			break;
		case ERConstants.CALCULATION_TYPE_OPTIONS: {
			if (parameter==null || parameter.coverageValue==null) {
				return null;
			}
			
			for (ER_Product_Coverage_Class_Value cValue : parameter.coverageValue.values) {
				if (cValue.vehicleClass.equals(vehicleClass)) {
					return cValue;
				}
			}
		} 
			break;
		case ERConstants.CALCULATION_TYPE_RANGE: {
			
			int baseCode = productCoverage.valueBase.code;
			boolean valid = false;
			BigDecimal searchValue = BigDecimal.ZERO;
			
			if (baseCode == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE) {
				searchValue = carValue;
				valid = (carValue!=null);
			}
			
			if (baseCode == ERConstants.CALCULATION_BASE_FIELD) {
				valid = (parameter!=null);
				if (valid)
					searchValue = parameter.value;
			}
			
			if (!valid) {
				return null;
			}
			
			if (!productCoverage.values.isEmpty()) {
				for (ER_Product_Coverage_Value value : productCoverage.values) {
					BigDecimal lowRange = (value.lowRange!=null)?value.lowRange:BigDecimal.ZERO;
					BigDecimal highRange = value.highRange;
					if (searchValue.compareTo(lowRange)>=0 && (highRange == null || searchValue.compareTo(highRange)<0)) {
						for (ER_Product_Coverage_Class_Value cValue : value.values) {
							if (cValue.vehicleClass.equals(vehicleClass)) {
								return cValue;
							}
						}
					}
				}
			}
		} 
			break;
		}
		
		return null;
	}
	
	public static void calculateSum(List<ER_Product_Coverage> coverages, ER_Quotation quotation, ER_Vehicle_Class vehicleClass, boolean calculateNetPrime) {
		
		BigDecimal hundred = new BigDecimal(100);
		BigDecimal netPrime = BigDecimal.ZERO;
		
		for (ER_Product_Coverage coverage : coverages) {
			
			if (calculateNetPrime) {
				if (!coverage.isPartOfNetPrime()) {					
					continue;
				}
			} else {
				netPrime = quotation.quotationDetail.getNetPrime();
				if (coverage.isPartOfNetPrime()) {
					continue;
				}
			}
			
			ER_Quotation_Parameter parameter = null;
			if (quotation.parameters!=null) {
				for (ER_Quotation_Parameter qParameter : quotation.parameters) {
					if (qParameter.productCoverage.equals(coverage)) {
						parameter = qParameter;
						break;
					}
				}
			}
			
			ER_Product_Coverage_Class_Value classValue = getVehicleClassValue(parameter, coverage, vehicleClass, quotation.carValue);
			Logger.info(coverage.coverage.name);
			if (classValue!=null) {
				boolean apply = true;
				
				if (parameter!=null) {
					if ((parameter.value==null && parameter.coverageValue==null) && parameter.productCoverage.optional !=null && parameter.productCoverage.optional) {
						if (parameter.applyInsurance==null || !parameter.applyInsurance) {
							apply = false;
						}
					}
				}
				
				if (apply) {
					BigDecimal fieldCalculation = BigDecimal.ZERO;
					
					int baseCode = classValue.productCoverageValue.productCoverage.valueBase.code;
					if (baseCode == ERConstants.CALCULATION_BASE_GTQ) {
						fieldCalculation = fieldCalculation.add(classValue.value);
						Logger.debug("Currency base, value: %s", classValue.value);
					} else if (baseCode == ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE) {
						fieldCalculation = fieldCalculation.add(classValue.value.multiply(quotation.carValue).divide(hundred));
						Logger.debug("Car value base, car value: %s", quotation.carValue);
					} else if (baseCode == ERConstants.CALCULATION_BASE_NET_PRIME_PERCENTAGE) {
						fieldCalculation = fieldCalculation.add(classValue.value.multiply(netPrime).divide(hundred));
						Logger.debug("Net primer base, net prime: %s", netPrime);
					} else if (baseCode == ERConstants.CALCULATION_BASE_FIELD) {
						BigDecimal parameterValue = getParameterValue(parameter, coverage, quotation);
						fieldCalculation = fieldCalculation.add(classValue.value.multiply(parameterValue).divide(hundred));
						Logger.debug("Field base, fieldCalculation: %s", parameterValue);
					}
					
					if (classValue.value.compareTo(BigDecimal.ZERO)!=0) {
						if (classValue.productCoverageValue.productCoverage.minimumPrime!=null && fieldCalculation.compareTo(classValue.productCoverageValue.productCoverage.minimumPrime)<0) {
							fieldCalculation = classValue.productCoverageValue.productCoverage.minimumPrime;
						}
					}
					
					CoverageCost cost = new CoverageCost(fieldCalculation);
					cost.coverageOrder = coverage.coverage.coverageOrder;
					cost.coverageName = (coverage.coverage.quotationName!=null && !coverage.coverage.quotationName.isEmpty())?coverage.coverage.quotationName:coverage.coverage.name;
					cost.coverageDescription = !coverage.coverage.quotationDescription.isEmpty() ? coverage.coverage.quotationDescription : null;
					if (parameter!=null && parameter.coverageValue!=null && parameter.coverageValue.optionName!=null) {
						cost.coverageDescription = parameter.coverageValue.optionName;
					}
					cost.coverageId = coverage.coverage.id;
					if(parameter!=null && parameter.youngerData != null){
						cost.youngerData = parameter.youngerData;
					}
					if (parameter!=null) {
						cost.coverage = parameter.coverageAmount();
					} else if (baseCode==ERConstants.CALCULATION_BASE_CAR_VALUE_PERCENTAGE) {
						cost.coverage = quotation.getOriginalCarValue();
					} else {
						cost.coverage = coverage.coverageValue;
					}
					
					cost.minimumDeductible = coverage.minimumDeductible;
					
					ER_Product_Coverage_Deductible deductible = ER_Product_Coverage_Deductible.find("productCoverage.id = ? and vehicleClass.id = ?", coverage.id, vehicleClass.id).first();
					if (deductible!=null) {
						cost.damagesDeductible = deductible.damagesDeductible;
					}
					
					boolean external = classValue.productCoverageValue.productCoverage.coverage.externals;
					boolean applyDiscount = classValue.productCoverageValue.productCoverage.coverage.applyDiscount;
					
					cost.external = external;

					quotation.quotationDetail.addCost(cost, coverage.coverage.category.name, coverage.coverage.category.description, calculateNetPrime, external, applyDiscount,coverage.minimumPrime);
					
					Logger.debug("%s: %s", cost.coverageName, fieldCalculation);
				}
			}
		}
	}
	
	public static void calculateTotalPrime(ER_Quotation quotation, ER_Vehicle vehicle, Long[] paymentFrecuencies) {
		
		ER_Vehicle_Class vehicleClass =vehicle.line.vehicleClass;
		
		if (quotation.product!=null) {
			
			List<ER_Product_Coverage> coverages = quotation.product.coverages;
			Logger.debug("Coverages: %s", coverages);
			
			ER_General_Configuration configuration = ER_General_Configuration.find("").first();
			
			boolean hasThirdInjuries = false;
			int injuriesPosition = 0;
			
			if (quotation.parameters!=null) {
				for (ER_Quotation_Parameter qParameter : quotation.parameters) {
					
					if (qParameter.productCoverage.coverage.equals(configuration.civilResponsabilityCoverage)) {
						quotation.civilValue = qParameter.coverageAmount();
						continue;
					}
					
					if (qParameter.productCoverage.coverage.equals(configuration.injuriesCoverage)) {
						quotation.injuriesValue = qParameter.coverageAmount();
						injuriesPosition = quotation.parameters.indexOf(qParameter) + 1;
						continue;
					}
					
					if (qParameter.productCoverage.coverage.equals(configuration.thirdInjuriesCoverage)) {
						hasThirdInjuries = true;
						continue;
					}
				}
			}
			
			//Adds the third injuries parameter based on the selected value in the injuriesCoverage
			List<ER_Quotation_Parameter> parametersTemporal = quotation.parameters;
			if (quotation.injuriesValue != null && !hasThirdInjuries) {
				for (ER_Product_Coverage productCoverage : coverages) {
					if (productCoverage.coverage.equals(configuration.thirdInjuriesCoverage)) {
						for (ER_Product_Coverage_Value value : productCoverage.values) {
							if (value.lowRange.equals(quotation.injuriesValue)) {
								
								ER_Quotation_Parameter thirdInjuriesParam = new ER_Quotation_Parameter();
								thirdInjuriesParam.applyInsurance = true;
								thirdInjuriesParam.coverageValue = value;
								thirdInjuriesParam.productCoverage = productCoverage;
								thirdInjuriesParam.quotation = quotation;
								for(ER_Quotation_Parameter parametro : parametersTemporal){
									if(parametro.productCoverage.coverage.transferCode != null &&
											productCoverage.coverage.transferCode != null &&
											parametro.productCoverage.coverage.transferCode.equals(productCoverage.coverage.transferCode)){
										thirdInjuriesParam.youngerData = quotation.parameters.get(injuriesPosition).youngerData;
										break;
									}
								}
								quotation.parameters.add(injuriesPosition, thirdInjuriesParam);
								break;
							}
						}
						break;
					}
				}
			}
			
			BigDecimal primeDiscount = null;
			BigDecimal deductibleIncrease = null;

			if (quotation.product.hasFacultative !=null && quotation.product.hasFacultative && quotation.facultative!=null) {

				Logger.debug("si hay descuento facultativo!!!");
				Logger.debug("facultativo:" +quotation.facultative.toString());
				ER_Facultative_Deductible facultative = ER_Facultative_Deductible.findById(quotation.facultative);
				if (facultative!=null) {
					Logger.debug("el facultativo si exisita ");
					primeDiscount = facultative.primeDiscount;
					deductibleIncrease = facultative.deductibleIncrease;
				}
			}
			
			if (primeDiscount==null || deductibleIncrease == null) {
				primeDiscount = BigDecimal.ZERO;
				deductibleIncrease = BigDecimal.ZERO;
			}
			
			BigDecimal hundred = new BigDecimal(100);
			quotation.quotationDetail = new QuotationDetail(configuration.emissionFeeFirstPayment, configuration.emissionFeePercentage.divide(hundred), configuration.ivaPercentage.divide(hundred), primeDiscount, deductibleIncrease);



			Logger.debug("---------------------- Calculando prima --------------------------------");
			Logger.debug("Clase: %s, Producto :%s", vehicle.line.vehicleClass.code, quotation.product.name);
			Logger.debug("Descuento en prima: %s%%", primeDiscount);
			Logger.debug("Suma asegurada: %s %s", quotation.product.currency.symbol, quotation.carValue);

            quotation.originalCarValue = quotation.carValue;

			if (quotation.product.montoRestarSumaAseg == null) {
				Logger.debug("montoRestarSumaAseg  NULL");
			}
			else
			{
				Logger.debug("montoRestarSumaAseg  " + quotation.product.montoRestarSumaAseg);
			}
			if (quotation.product.montoAgregarPrima == null) {
				Logger.debug("montoAgregarPrima  NULL");
			}
			else
			{
				Logger.debug("montoAgregarPrima  " + quotation.product.montoAgregarPrima);
			}

			// byron guerrero - 11/11/15
			if(quotation.hasCarValue() && quotation.product.montoRestarSumaAseg!= null)
			{
				Logger.debug("carvalue  " + quotation.carValue.toString());

				if (quotation.carValue.compareTo(quotation.product.montoRestarSumaAseg) > 0 )
				{
					quotation.carValue = quotation.carValue.subtract(quotation.product.montoRestarSumaAseg);
					Logger.debug("nuevo carvalue  " + quotation.carValue.toString());
				}
			}

			// byron guerrero - 11/11/15
			calculateSum(coverages, quotation, vehicleClass, true);
			calculateSum(coverages, quotation, vehicleClass, false);

            // Gustavo Gomez - Manejo de Alto Riesgo
            quotation.quotationDetail.saveInternalPrime();
            if (quotation.selectedLoJack != null && quotation.selectedLoJack.overprime != null) {
                quotation.quotationDetail.addLoJackCost(quotation.selectedLoJack.operation, quotation.selectedLoJack.overprime);
            }


            if (paymentFrecuencies!=null) {
				List<ER_Payment_Frecuency> payments = ER_Payment_Frecuency.find("id in (:f)").bind("f", paymentFrecuencies).fetch();
				quotation.quotationDetail.calculatePaymentOptions(payments);
			} else {
				List<ER_Payment_Frecuency> payments = ER_Payment_Frecuency.find("").fetch(1);
				quotation.quotationDetail.calculatePaymentOptions(payments);
			}
			
			BigDecimal exchangeRate = null;
			if (quotation.product!=null && quotation.product.currency!=null) {
				quotation.quotationDetail.setCurrencySymbol(quotation.product.currency.symbol);
				exchangeRate = quotation.product.currency.exchangeRate;
				if (exchangeRate == null) {
					exchangeRate = BigDecimal.ONE;
				}
			}
			
			try {
				quotation.quotationDetail.setVehicleBrand(vehicle.line.brand.name);
				quotation.quotationDetail.setVehicleLine(vehicle.line.name);
				quotation.quotationDetail.setVehicleClass(vehicle.line.vehicleClass.code);
				quotation.quotationDetail.setVehicleYear(vehicle.year);

                boolean modifiedTheftDeductible = false;
				if (vehicle.requiresLoJack()) {
					if (vehicle.line.theftDeductible != null) {
						quotation.quotationDetail.setTheftDeductible(vehicle.line.theftDeductible);
                        modifiedTheftDeductible = true;
					} else if (vehicle.line.brand.theftDeductible != null) {
						quotation.quotationDetail.setTheftDeductible(vehicle.line.brand.theftDeductible);
                        modifiedTheftDeductible = true;
					}
				}

				if (quotation.quotationDetail!=null) {
					quotation.quotationDetail.setRequiresLoJack(vehicle.requiresLoJack());
				}
				
				if (configuration.maxValueWithoutLoJack!=null && quotation.carValue!=null) {
					int result = quotation.convertCarValueToUSD(quotation.carValue).compareTo(configuration.maxValueWithoutLoJack);
					if (result >=0) {
						quotation.quotationDetail.setRequiresLoJack(false);
					}
				}

                //José Andrade: Se agregó cambio de deducible de robo a 25 si no se cambió y requiere lo jack
                if (quotation.quotationDetail!=null && quotation.quotationDetail.getRequiresLoJack() && !modifiedTheftDeductible) {
                    if (configuration.theftDeductibleWithoutLowJack != null) {
                        quotation.quotationDetail.setTheftDeductible(configuration.theftDeductibleWithoutLowJack);
                    }
                }


                //José Andrade: Se modificó el cálculo de deducible factultativo para calcularlo después de aplicar cambios en lo jack
                if (quotation.product.hasFacultative !=null && quotation.product.hasFacultative && quotation.facultative!=null)
                {
                    BigDecimal multiplier = BigDecimal.ONE;
                    if (deductibleIncrease!=null) {
                        multiplier = multiplier.add(deductibleIncrease.divide(hundred));
                        if (quotation.quotationDetail.getTheftDeductible() != null) {
                            quotation.quotationDetail.setTheftDeductible(quotation.quotationDetail.getTheftDeductible().multiply(multiplier));
                        }

                        quotation.quotationDetail.setFacultativeType(StringFormatExtensions.formatDeductibleIncrease(deductibleIncrease));
                        quotation.quotationDetail.setFacultativeDiscount(primeDiscount);
                    }
                }
				
				ER_Vehicle_Value value = ER_Vehicle_Value.find("line = ? and year = ?", vehicle.line, vehicle.year).first();
				if (value!=null) {

					quotation.quotationDetail.setVehicleValue(quotation.convertCarValue(value.value));
				}
			} catch (Exception e) {
				Logger.error(e,"QuotationHelper: Error al asignar vehículo");
			}

			quotation.carValue = quotation.getOriginalCarValue();

			// byron guerrero - 11/11/15
			if(quotation.hasCarValue() && quotation.product.montoAgregarPrima!= null)
			{
				Logger.debug("antiguo InternalPrime  " + quotation.quotationDetail.getInternalPrime());
				quotation.quotationDetail.AddExtraValueInternalPrime(quotation.product.montoAgregarPrima);
                Logger.debug("nuevo InternalPrime  " + quotation.quotationDetail.getInternalPrime());
			}

			// byron guerrero - 11/11/15


            quotation.totalPrime = quotation.quotationDetail.getSinglePaymentPrime();
			Logger.debug("TOTAL: " + quotation.totalPrime);
            quotation.discountedPrime = quotation.quotationDetail.getSinglePaymentPrime();
            Logger.debug("DISCOUNTED: " + quotation.discountedPrime);
            quotation.quotationDetail.updatePaymentOptions();

			quotation.generateDetailJSON();

			Logger.debug("Prima neta: %s", quotation.quotationDetail.getNetPrime());
			Logger.debug("Gastos de emisión: %s", quotation.quotationDetail.getTotalEmissionFee());
			Logger.debug("Total IVA: %s", quotation.quotationDetail.getTotalVat(BigDecimal.ZERO));
			Logger.debug("Prima total: %s", quotation.quotationDetail.getSinglePaymentPrime());

			Logger.debug("JSON: %s", quotation.detail);
		}
	}

}
