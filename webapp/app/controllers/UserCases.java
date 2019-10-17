package controllers;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import jobs.SendQuotationsJob;
import models.*;
import models.ws.*;
import objects.LoJackOptions;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.apache.commons.beanutils.BeanUtils;

import helpers.ERConstants;
import helpers.FieldAccesor;
import helpers.GeneralMethods;
import helpers.QuotationHelper;
import notifiers.Mails;
import objects.ER_Quotation_Parameter;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Transactional;
import play.i18n.Messages;
import play.modules.pdf.PDF;
import play.modules.pdf.PDF.Options;
import play.mvc.With;
import service.AverageValueQueryWebService;
import service.InsurableVehicleQueryWebService;
import service.PolicyService;
import utils.StringUtil;

@With(Secure.class)
public class UserCases extends AdminBaseController {
	
	@Inject
	static PolicyService policyService;
	@Inject
	static AverageValueQueryWebService averageValueServiceBus;
	@Inject
	static InsurableVehicleQueryWebService insurableVehicleServiceBus;

	/*
	 * ************************************************************************************************************************
	 * Ingreso de datos de cliente
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void clientInformation() {
    	flash.clear();
        renderClientInformation(null);
    }
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static void renderClientInformation(String iField) {
		Integer userRol = connectedUserRoleCode(connectedUser());
    	ER_Client client = new ER_Client();
    	client.isIndividual = true;
    	client.useDataClientPayer = true;
    	renderArgs.put("client", client);
    	if(GeneralMethods.validateParameter(iField)){
    		ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
    		renderArgs.put("incident", incident);
    		renderArgs.put("client", incident.client);
    		renderArgs.put("iField", iField);
    	}else if(userRol.equals(ERConstants.USER_ROLE_FINAL_USER)){
    		ER_User user = connectedUser();
    		client.email = user.email;
    		if(user.firstName != null){
    			String[] name = user.firstName.split(" ");
    			if(name.length > 0){
    				client.firstName = name[0];
    			}
    			if(name.length > 1){
    				client.secondName = name[1];	
    			}
    		}
    		if(user.lastName != null){
    			String[] name = user.lastName.split(" ");
    			if(name.length > 0){
    				client.firstSurname = name[0];
    			}
    			if(name.length > 1){
    				client.secondSurname = name[1];	
    			}
    		}
    		renderArgs.put("client", client);
    	}
    	renderArgs.put("societyTypes", ER_Society_Type.find("active = ?", Boolean.TRUE).fetch());
    	
    	renderTemplate("UserCases/clientInformation.html");
    }
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void clientInformationNext(@Valid ER_Client client, String iField) {
    	flash.clear();
        client.ConvertUpper();
    	vehicleInformation(client, iField);
    }
	
    @Transactional
    private static ER_Incident saveIncidentStatus(ER_Incident incident, Integer constants, Boolean saveCliente, Boolean saveVehicle, Boolean saveQuotations){
    	if(incident.id == null){
    		incident.status = ER_Incident_Status.find("byCode", ERConstants.INCIDENT_STATUS_CREATED).first();
			incident.number = ER_Incident.generateIncidentNumber();
			incident.creator = ER_User.find("byEmail", Security.connected()).first();
			incident.creationDate = new Date();
			ER_User user = connectedUser();
			if(user.distributor != null){
				incident.distributor = user.distributor;
				incident.channel = user.distributor.channel;
			}else if(user.channel!=null){
				incident.channel = user.channel;
			}
			
			return incident.save();
		}else{
			ER_Incident currentIncident = ER_Incident.findById(incident.id);
			if(saveQuotations){
				for(ER_Quotation quotation: incident.quotations){
					quotation.incident = currentIncident;
				}
				currentIncident.quotations = incident.quotations;
			}
			if(saveVehicle){
				try{
					BeanUtils.copyProperties(currentIncident.vehicle, incident.vehicle);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			if(saveCliente){
				try{
					BeanUtils.copyProperties(currentIncident.client, incident.client);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			currentIncident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_CREATED).first();
			return currentIncident.merge();
		}

    }
    
    /*
	 * ************************************************************************************************************************
	 * Ingreso de datos de vehículo
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static void vehicleInformation(@Valid ER_Client client, String iField) {
    	
    	flash.clear();
    	
    	if(validation.hasErrors()){
    		params.flash();
    		renderClientInformation(null);
    	}else{
    		ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
    		client.taxNumber = client.taxNumber.toUpperCase();
    		incident.client = client;
    		
    		Long lineId = null;
    		if(incident.vehicle != null){
    			renderArgs.put("vehicle", incident.vehicle);
    			if(incident.vehicle.line != null){
    				lineId = incident.vehicle.line.id;
    			}
    		}
    		renderArgs.put("incident", incident);
    		renderVehicleForm(incident.vehicle, incident.toJsonString(true), lineId, null);
    	}
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static void renderVehicleForm(ER_Vehicle vehicle, String iField, Long lineId, Long brandId) {
    	if(lineId != null){
    		ER_Vehicle_Line line = ER_Vehicle_Line.findById(lineId);
    		if(line != null){
    			brandId = line.brand.id;
    			//List<ER_Vehicle_Value> values = ER_Vehicle_Value.find("year != null and line.insurable = 1 and line.id = ? order by year asc", line.id).fetch();
    			//renderArgs.put("values", values);
    		}
    	}
    	if(brandId != null){
//    		List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("select distinct v.line from ER_Vehicle_Value v where v.year != null and v.line.insurable = 1 and v.line.vehicleClass IS NOT NULL and v.line.transferCode is not null and v.line.brand.id = ? order by v.line.name", brandId).fetch();
    		List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("select distinct v from ER_Vehicle_Line as v where v.brand.id = ? and v.insurable = 1 and v.transferCode is not null", brandId).fetch();
    		
			renderArgs.put("lines", lines);
    	}
    	List <ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("select distinct v.brand from ER_Vehicle_Line v where v.insurable = 1 and v.vehicleClass IS NOT NULL").fetch();
    	renderArgs.put("brands", brands);
    	renderArgs.put("vehicleTypes", ER_Vehicle_Type.find("order by name").fetch());
		renderArgs.put("vehicleRates", ER_Rate.findAll());
		renderArgs.put("erYears", ER_Year.findAll());
		renderArgs.put("vehiclePlateTypes", ER_Plate_Type.findAll());
    	renderArgs.put("iField", iField);
    	renderArgs.put("vehicle", vehicle);

        ER_General_Configuration configuration = ER_General_Configuration.find("").first();
        if (configuration!=null) {
            renderArgs.put("configuration", configuration);
        }
    	
    	renderTemplate("UserCases/vehicleInformation.html");
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void vehicleInformationNext(@Valid ER_Vehicle vehicle, String iField, String back, ER_Vehicle_Brand brand, ER_Inspection inspection, String averageValue) {
    	flash.clear();
    	
    	ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
    	vehicle.plate = vehicle.plate.toUpperCase();

		if(vehicle.quotationnNew){
			vehicle.isNew = true;
		}
		incident.vehicle = vehicle;
		//associate inspection with incident
		if(inspection!=null)
		incident.inspection = inspection;
		else {
			inspection = new ER_Inspection();
			incident.inspection = inspection;
		}


        try {
            Locale locale = new Locale("es", "GT");
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
            incident.vehicle.averageValue = BigDecimal.valueOf((Long) currencyFormatter.parse(averageValue));
        } catch (ParseException e) {
            Logger.warn("No se pudo parsear el valor: " + averageValue, e);
        }

        if(back != null){
    		validation.clear();
    		renderClientInformation(incident.toJsonString(true));
    	}
    	
    	if(incident.quotations != null){
    		List<List<ER_Quotation_Parameter>> parameters = new ArrayList<List<ER_Quotation_Parameter>>();
    		for(ER_Quotation quotation: incident.quotations){
    			quotation.detail = null;
    			parameters.add(quotation.parameters);
    		}
    		renderArgs.put("parameters", parameters);	
    	}

    	insuranceInformation(vehicle, incident.toJsonString(true), brand);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Ingreso de datos de seguro
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static void insuranceInformation(@Valid ER_Vehicle vehicle, String iField, ER_Vehicle_Brand brand) {
    	flash.clear();

		ER_Year erYear = ER_Year.findById(vehicle.erYear.id);
		QueryVehicleRequest request = new QueryVehicleRequest();
		request.setCurrency("Q");
		request.setLine(vehicle.line.transferCode);
    	request.setYearVehicle(erYear.year);
    	QueryVehicleResponse queryVehicle = insurableVehicleServiceBus.insurableVehicleQuery(request);
    	Long lineId = null;
    	Long brandId = null;
    	if(vehicle.line != null){
			lineId = vehicle.line.id;
		}
		if(brand.id != null){
			brandId = brand.id;
		}
		// Search for LoJack configs.
        Integer loJackYear = Integer.parseInt(!StringUtil.isNullOrBlank(erYear.year) ? erYear.year : "0" );
    	Logger.info("LoJack a buscar: " + loJackYear + " - " + vehicle.line.brand.name + " - " + vehicle.line.name + " - " + vehicle.line.id );
        ER_Vehicle_LoJack loJack = ER_Vehicle_LoJack.find("line.id = ? and (yearInit IS NULL or yearInit <= ?) and (yearEnd IS NULL or yearEnd >= ?)", vehicle.line.id, loJackYear, loJackYear ).first();
        Logger.info("Encontro LoJack: " + (loJack != null));
    	if(queryVehicle.isSuccessful()){
    		if(validation.hasErrors()){
        		params.flash();
        		renderVehicleForm(vehicle, iField, lineId, brandId);
        	}else{
        		renderInsuranceForm(iField, true, null, loJack != null ? loJack.id : null);
        	}
    	}else{
    		flash.error("CONSULTA VEHICULO: " + queryVehicle.getMessage());
    		renderVehicleForm(vehicle, iField, lineId, brandId);
    	}
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static void renderInsuranceForm(String iField, boolean updateVehicleValue, Long[] paymentFrecuencies, Long loJackId) {
    	if(iField != null){
    		ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
    		if(incident.vehicle != null){
    			BigDecimal carValue = BigDecimal.ZERO;
    			if(params.get("averageValue") != null && params.get("averageValue").length() > 0){
    				carValue = new BigDecimal(params.get("averageValue").replaceAll("Q", "").replaceAll(",", ""));
    			}
				if(incident.quotations != null){
					if(updateVehicleValue && BigDecimal.ZERO.compareTo(carValue) < 0) {
		    			for(ER_Quotation quotation: incident.quotations){
		    				quotation.carValue = quotation.convertCarValue(carValue);
		    			}
					}
	        		renderArgs.put("quotations", incident.quotations);
	        	}
    		}
    		renderArgs.put("incident", incident);
    	}
    	
    	//Authorized products
		renderArgs.put("products", authorizedProducts());
		
		//Facultative deductibles
		List<ER_Facultative_Deductible> facultative = ER_Facultative_Deductible.find("order by primeDiscount").fetch();
		renderArgs.put("facultative", facultative);
    	
		//Payment frecuencies options
        List<Map<String, Object>> frecuencies = GeneralMethods.getAvailableFrecuencies();
    	renderArgs.put("frecuencies", frecuencies);
    	renderArgs.put("selectedFrecuencies", paymentFrecuencies);
    	renderArgs.put("iField", iField);
    	if(params.get("averageValue") != null && params.get("averageValue").length() > 0){
    		renderArgs.put("averageValue", params.get("averageValue").replaceAll("Q", ""));
    	}

    	//Add the configuration to render args
    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	if(configuration != null){
    		renderArgs.put("configuration", configuration);
    	}
    	// If LoJack for vehicle, set data
        renderArgs.put("loJackId", loJackId);
        List<LoJackOptions> loJackOptions = GeneralMethods.getAvailableLoJacks(loJackId);
        renderArgs.put("loJackOptions", loJackOptions);
    	
    	renderTemplate("UserCases/insuranceInformation.html");
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void insuranceInformationNext(@Valid List<ER_Quotation> quotations, @Required Long[] paymentFrecuencies, String iField, String back, Long loJackId) {
    	flash.clear();
    	try {
			ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
			List<LoJackOptions> loJackOptions = GeneralMethods.getAvailableLoJacks(loJackId);
			if (quotations != null) {
				for (int i = 0; i < quotations.size(); i++) {
					ER_Quotation quotation = quotations.get(i);
					quotation.setValuesOfQuotation(quotation);
					if (quotation.hasCarValue()) {
						validation.required("quotations[" + i + "].carValue", quotation.carValue);
					}
					if (quotation.discount != null && quotation.product != null) {
						BigDecimal primeDiscount = BigDecimal.ZERO;
						if (quotation.product.hasFacultative != null && quotation.product.hasFacultative && quotation.facultative != null) {
							ER_Facultative_Deductible facultative = ER_Facultative_Deductible.findById(quotation.facultative);
							if (facultative != null) {
								primeDiscount = facultative.primeDiscount;
							}
						}
						BigDecimal authorizedDiscount = Incidents.getAuthorizedDiscount(quotation.product, quotation.carValue, primeDiscount);
						validation.max("quotations[" + i + "].discount", quotation.discount, authorizedDiscount.doubleValue());
						validation.min("quotations[" + i + "].discount", quotation.discount, 0.0);
					}
					// Validate if has average value and car value is within parameters
					if(incident.vehicle.quotationnNew != null && !incident.vehicle.quotationnNew) {
					if (incident.vehicle.averageValue != null && quotation.carValue != null) {

						BigDecimal averageValueParam = new BigDecimal(0.25);
						ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
						if (currentConfiguration.averageValueConfig != null) {
							averageValueParam = currentConfiguration.averageValueConfig.divide(BigDecimal.valueOf(100));
						}
						BigDecimal min = BigDecimal.valueOf(1).subtract(averageValueParam);
						BigDecimal max = BigDecimal.valueOf(1).add(averageValueParam);
						// Find lower and upper parameter
						BigDecimal lowerValue = incident.vehicle.averageValue.multiply(min);
						BigDecimal upperValue = incident.vehicle.averageValue.multiply(max);
						// car value within

							if (quotation.carValue.compareTo(lowerValue) < 0 || quotation.carValue.compareTo(upperValue) > 0) {
								validation.addError("quotations[" + i + "].carValue", Messages.get("quotation.form.quotation.carvaluerange"));
							}
						}

					}

					if ( quotation.carValue != null) {
						//Checks lower and higher range
						boolean valueCorrect;
						for (ER_Product_Coverage coverage : quotation.product.coverages) {
							valueCorrect = false;
							if(coverage.coverage.category.id != 1){ //Solo debe validar seccion 1 de coberturas
								break;
							}
							for (int j = 0; j < coverage.values.size(); j++) {

								if (coverage.values.get(j).lowRange == null && coverage.values.get(j).highRange == null) {
									valueCorrect = true;
									break;
								}
								else if ((coverage.values.get(j).lowRange != null && coverage.values.get(j).highRange == null && quotation.carValue.compareTo(coverage.values.get(j).lowRange) >= 0)){
									valueCorrect = true;
									break;
								}
								else if ((coverage.values.get(j).highRange != null && coverage.values.get(j).lowRange == null && quotation.carValue.compareTo(coverage.values.get(j).lowRange) <= 0)){
									valueCorrect = true;
									break;
								}
									else if ((coverage.values.get(j).lowRange != null && coverage.values.get(j).highRange != null) && (quotation.carValue.compareTo(coverage.values.get(j).highRange) <= 0 && quotation.carValue.compareTo(coverage.values.get(j).lowRange) >= 0)) {
										valueCorrect = true;
									break;
								}
							}
							if (!valueCorrect) {
								validation.addError("quotations[" + i + "].carValue", Messages.get("quotation.form.quotation.carvaluerangecoverage"));
								break;
							}
						}
					}
					if (quotation.loJack != null) {
						for (LoJackOptions loJackOption : loJackOptions) {
							if (loJackOption.number == quotation.loJack) {
								quotation.selectedLoJack = loJackOption;
								quotation.loJackRecharge = loJackOption.recharge;
								break;
							}
						}
					}
				}
			}

			incident.quotations = quotations;

			if (back != null) {
				validation.clear();
				renderArgs.put("vehicle", incident.vehicle);
				renderVehicleForm(incident.vehicle, incident.toJsonString(true), incident.vehicle.line.id, null);
			}

			renderArgs.put("incident", incident);

			insuranceSimulation(quotations, paymentFrecuencies, incident.toJsonString(true), loJackId);
		}
		catch(Exception e){
			Logger.error(e,e.getMessage());
    	}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Insurance quotation simulation
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static void insuranceSimulation(@Valid List<ER_Quotation> quotations, @Required Long[] paymentFrecuencies, String iField, Long loJackId) {
    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	ER_Coverage theftCoverage = (configuration!=null)?configuration.theftCoverage:null;
    	BigDecimal hundred = new BigDecimal(100);
    	BigDecimal partialTheftPercentage = (configuration!=null)?configuration.partialTheftPercentage.divide(hundred):BigDecimal.ZERO;
        ER_Incident incident = ER_Incident.incidentFromJson(iField, true);

        if (quotations!=null) {
	    	for (int i=0; i< quotations.size(); i++) {
	    		ER_Quotation quotation = quotations.get(i);
	    		
	    		if (quotation.product==null || quotation.product.id==null) {
	    			validation.addError("quotations["+i+"].product", Messages.get("quotation.form.quotation.required"));
	    		}
	    		
	    		List<ER_Quotation_Parameter> parameters = quotation.parameters;
	    		if (parameters!=null && !parameters.isEmpty()) {
	    			
	            	BigDecimal theftValue = BigDecimal.ZERO;
	    			
	            	int theftIndex = 0;
		    		for (int j=0; j<parameters.size();j++) {
		    			ER_Quotation_Parameter parameter = parameters.get(j);
		    			if (!parameter.validateValue()) {
		    				String key = "quotations["+i+"].parameters["+j+"].value";
		    				if (!validation.hasError(key)) {
		    					validation.addError(key, Messages.get("quotation.form.quotation.required"));
		    				}
		    			}
		    			
		    			if (parameter.productCoverage!=null && parameter.productCoverage.coverage.equals(theftCoverage)) {
		    				theftValue = (parameter.value!=null)?parameter.value:BigDecimal.ZERO;
		    				theftIndex = j;
		    			}
		    		}
		    		
		    		if (quotation.carValue!=null) {
			    		if (!(quotation.carValue.multiply(partialTheftPercentage).compareTo(theftValue)>=0)) {
			    			validation.addError("quotations["+i+"].parameters["+theftIndex+"].value", Messages.get("quotation.form.quotation.theftcoverageerror",configuration.partialTheftPercentage, "%%"));
			    		}
		    		}

                    try{
                        ER_Vehicle_Line line = ER_Vehicle_Line.findById(incident.vehicle.line.id);
                        ER_Year erYear = ER_Year.findById(incident.vehicle.erYear.id);
                        QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
                        request.setBrand(line.brand.name);
                        request.setLine(line.name);
                        request.setYear(erYear.year);
                        request.setCurrency("Q");
                        QueryAverageValueVehicleResponse queryAverage = averageValueServiceBus.averageValueQuery(request);
                        if(!FieldAccesor.isEmptyOrNull(queryAverage, "averageValue") && quotation.carValue != null){
                            // Garanteed value check
                            BigDecimal garanteedValueParam = new BigDecimal(0.15);
                            ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
                            if (currentConfiguration.garanteedValueConfig != null) {
                                garanteedValueParam = currentConfiguration.garanteedValueConfig.divide(BigDecimal.valueOf(100));
                            }
                            BigDecimal diff = queryAverage.getAverageValue().multiply(garanteedValueParam).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal min = queryAverage.getAverageValue().subtract(diff).setScale(2, RoundingMode.HALF_UP);
                            BigDecimal max = queryAverage.getAverageValue().add(diff).setScale(2, RoundingMode.HALF_UP);


                            if(quotation.carValue.compareTo(queryAverage.getAverageValue()) == 0){
                                quotation.setGaranteedValue(Boolean.TRUE);
                            }else if(quotation.carValue.compareTo(min) >= 0 && quotation.carValue.compareTo(max) <= 0){
                                quotation.setGaranteedValue(Boolean.TRUE);
                            }
                            else if(incident.vehicle.quotationnNew){
								quotation.setGaranteedValue(Boolean.TRUE);
							}
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
	    	}
    	}

    	if (validation.hasErrors() || quotations==null || quotations.size()==0) {
    		renderArgs.put("quotations", quotations);
    		
    		for (play.data.validation.Error error : validation.errors()) {
    			Logger.debug("%s: %s", error.getKey(), error.message());
    		}
    		
    		renderInsuranceForm(iField, false, paymentFrecuencies, loJackId);
    	} else {
    		
    		ER_Vehicle vehicle = incident.vehicle;
    		
    		List<Map<String, Object>> quotationsList =  new ArrayList<Map<String, Object>>();
    		for (int i=0; i<quotations.size(); i++) {
    			ER_Quotation quotation = quotations.get(i);
    			quotationsList.add(quotation.toMap());
    			QuotationHelper.calculateTotalPrime(quotation, vehicle, paymentFrecuencies);

    			if(quotation.discount != null){
    				quotation.applyDiscount(quotation.discount);
    			}

    			validation.min("quotations["+i+"].totalPrime", quotation.totalPrime, 0.01);

    		}
    		
    		if (validation.hasErrors()) {
    			renderArgs.put("quotations", quotations);
        		renderInsuranceForm(iField, false, paymentFrecuencies, loJackId);
    		}
    		
    		incident.quotations = quotations;
    		
    		renderArgs.put("quotations", quotations);
    		renderArgs.put("selectedFrecuencies", paymentFrecuencies);
    		renderArgs.put("iField", incident.toJsonString(true));
            renderArgs.put("configuration", ER_General_Configuration.find("").first());
            renderArgs.put("incident", incident);
            renderArgs.put("loJackId", loJackId);

    		renderTemplate("UserCases/insuranceSimulation.html");
    	}
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void simulationNext(String iField, String back, Long[] paymentFrecuencies, Long loJackId, String theftDeductible) {
    	flash.clear();
    	
    	ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
    	if(back != null){
    		validation.clear();
    		List<List<ER_Quotation_Parameter>> parameters = new ArrayList<List<ER_Quotation_Parameter>>();
    		for(ER_Quotation quotation: incident.quotations){
    			quotation.detail = null;
    			parameters.add(quotation.parameters);
    		}
    		renderArgs.put("parameters", parameters);
    		renderInsuranceForm(incident.toJsonString(true), false, paymentFrecuencies, loJackId);
    	}
    	renderArgs.put("incident", incident);
    	
    	incidentTask(iField);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Incident task
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    private static void incidentTask(String iField) {
    	if(iField == null){
    		renderClientInformation(null);
    	}else{
    		List<ER_Task_Type> types = ER_Task_Type.find("active = 1 order by name").fetch();
    		renderTemplate("UserCases/incidentTask.html", iField, types);
    	}
    }

	/*
	 * ************************************************************************************************************************
	 * Save quotation
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static synchronized void saveAndSend(String iField, String back, @Valid ER_Task task, Long loJackId) {
		Integer userRol = connectedUserRoleCode(connectedUser());
    	flash.clear();
    	ER_Incident incident = ER_Incident.incidentFromJson(iField, true);
    	if(back != null){
    		validation.clear();
    		for(ER_Quotation quotation: incident.quotations){
    			quotation.detail = null;
    		}
    		renderInsuranceForm(incident.toJsonString(true), false, null, loJackId);
    	}
    	// This is generating a blank page
    	/*if(validation.hasErrors()){
    		params.flash();
    		incidentTask(iField);
    	}*/
    	
    	try{
    		incident.status = ER_Incident_Status.find("byCode", ERConstants.INCIDENT_STATUS_CREATED).first();
    		incident.number = ER_Incident.generateIncidentNumber();
			incident.creator = ER_User.find("byEmail", Security.connected()).first();
			incident.creationDate = new Date();
			ER_User user = connectedUser();
			if(user.distributor != null){
				incident.distributor = user.distributor;
				incident.channel = user.distributor.channel;
			}else if(user.channel!=null){
				incident.channel = user.channel;
			}
    		//create inspection if car is not new
	    	if(incident.vehicle.isNew != null && !incident.vehicle.isNew){
	    		if(userRol.equals(ERConstants.USER_ROLE_RUNNER_SELLER)){
		    		//data from web service
		    		incident.inspection.inspected = false;
		    		//incident.inspection.inspectionNumber = "ws inspection number";
	    		}/*else save in ws inspections*/
	    	}
    		incident = incident.save();
	    	
	    	//Save task to database
	    	task.incident = incident;
	    	task.status = ER_Task_Status.find("code = ?", ERConstants.TASK_STATUS_PENDING).first();
	    	task.save();
	    	
	    	//Create reminders
	    	task.createReminder();
	    	
	    	//Save quotations to database
	    	for(ER_Quotation quotation : incident.quotations){
	    		boolean hasDiscount = quotation.discount != null && quotation.discount.compareTo(BigDecimal.ZERO) > 0;
	    		quotation.product = ER_Product.findById(quotation.product.id);
	    		quotation.incident = incident;
	    		quotation.creationDate = new Date();
	    		
	    		try{
	    			ER_Vehicle_Line line = ER_Vehicle_Line.findById(incident.vehicle.line.id);
					ER_Year erYear = ER_Year.findById(incident.vehicle.erYear.id);
	    			QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
	            	request.setBrand(line.brand.name);
	        		request.setLine(line.name);
	        		request.setYear(erYear.year);
	        		request.setCurrency("Q");
	        		QueryAverageValueVehicleResponse queryAverage = averageValueServiceBus.averageValueQuery(request);
	        		if(!FieldAccesor.isEmptyOrNull(queryAverage, "averageValue") && quotation.carValue != null){
	        		    // Garanteed value check
                        BigDecimal garanteedValueParam = new BigDecimal(0.15);
                        ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
                        if (currentConfiguration.garanteedValueConfig != null) {
                            garanteedValueParam = currentConfiguration.garanteedValueConfig.divide(BigDecimal.valueOf(100));
                        }
                        BigDecimal diff = queryAverage.getAverageValue().multiply(garanteedValueParam).setScale(2, RoundingMode.HALF_UP);
	        			BigDecimal min = queryAverage.getAverageValue().subtract(diff).setScale(2, RoundingMode.HALF_UP);
	        			BigDecimal max = queryAverage.getAverageValue().add(diff).setScale(2, RoundingMode.HALF_UP);
	        			
	        			if(quotation.carValue.compareTo(queryAverage.getAverageValue()) == 0){
	        				quotation.quotationDetail.setVehicleValue(queryAverage.getAverageValue());
	        			}else if(quotation.carValue.compareTo(min) >= 0 && quotation.carValue.compareTo(max) <= 0){
	        				quotation.quotationDetail.setVehicleValue(quotation.carValue);
	        			}
	        		}
				}catch(Exception e){
					e.printStackTrace();
				}
        		
	    		if(hasDiscount){
	    			quotation.discountDate = new Date();
	    			quotation.discountAuthorizedUser = connectedUser();
	    		}
	    		quotation.save();
	    		quotation.loadDetailJSON();
	    		/*if(hasDiscount){
	    			ER_Quotation originalQuotation = new ER_Quotation();
	    			originalQuotation.setValuesOfQuotation(quotation);
	    			originalQuotation.detail = quotation.detail;
	    			originalQuotation.quotationDetail = quotation.quotationDetail;
	    			originalQuotation.product = quotation.product;
	    			originalQuotation.incident = quotation.incident;
	    			originalQuotation.creationDate = quotation.creationDate;
	    			originalQuotation.carValue = quotation.carValue;
	    			originalQuotation.civilValue = quotation.civilValue;
	    			originalQuotation.injuriesValue = quotation.injuriesValue;
	    			originalQuotation.loJack = quotation.loJack;
	    			originalQuotation.selectedLoJack = quotation.selectedLoJack;
	    			originalQuotation.loJackRecharge = quotation.loJackRecharge;
	    			originalQuotation.applyDiscount(BigDecimal.ZERO);
	    			originalQuotation.totalPrime = originalQuotation.quotationDetail.getSinglePaymentPrime();
	    			originalQuotation.save();
	    			originalQuotation.loadDetailJSON();
	    		}*/
			}
	    	
	    	//Generate quotations pdfs
	    	List<ByteArrayOutputStream> streamArray = new ArrayList<ByteArrayOutputStream>();
	    	//Reload quotations list
	    	List<ER_Quotation> quotations = ER_Quotation.find("incident = ?", incident).fetch();
	    	for(ER_Quotation quotation: quotations){
	    		quotation.loadDetailJSON();
	    		streamArray.add(quotationPDFData(quotation));
	    	}

			SendQuotationsJob sendQuotationsJob = new SendQuotationsJob(streamArray, incident);
	    	sendQuotationsJob.now();

	    	//Mails.quotations(incident, streamArray, true);
	    	
	    	successful(incident.id);
    	}catch(Exception e){
    		Logger.error(e, "Error saving incident");
    		successful(null);
    	}
    }
    
    public static void main(String[] args){
		BigDecimal averageValue = new BigDecimal(50000);
		BigDecimal carValue = new BigDecimal(57500);
		
		BigDecimal diff = averageValue.multiply(new BigDecimal(0.15)).setScale(2, RoundingMode.HALF_UP);
		BigDecimal min = averageValue.subtract(diff).setScale(2, RoundingMode.HALF_UP);;
		BigDecimal max = averageValue.add(diff).setScale(2, RoundingMode.HALF_UP);
		
		System.out.println("Average Value: " + averageValue);
		System.out.println("Diff: " + diff);
		System.out.println("Min: " + min);
		System.out.println("Max: " + max);
		
		
		if(carValue.compareTo(averageValue) == 0){
			System.out.println("Car Value: " + carValue);
		}else if(carValue.compareTo(min) >= 0 && carValue.compareTo(max) <= 0){
			System.out.println("Car Value: " + carValue);
		}
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static synchronized void saveAndSendPublic(String iField, String back, Long loJackId) {
    	flash.clear();
    	ER_Incident incident = ER_Incident.incidentFromJson(iField, true);

    	if(back != null){
    		validation.clear();
    		for(ER_Quotation quotation: incident.quotations){
    			quotation.detail = null;
    		}
    		renderInsuranceForm(incident.toJsonString(true), false, null, loJackId);
    	}
    	
    	/*if(validation.hasErrors()){
    		params.flash();
    		incidentTask(iField);
    	}*/
    	
    	try{
			Integer userRol = connectedUserRoleCode(connectedUser());
    		incident.status = ER_Incident_Status.find("byCode", ERConstants.INCIDENT_STATUS_CREATED).first();
    		incident.number = ER_Incident.generateIncidentNumber();
			incident.creator = ER_User.find("byEmail", Security.connected()).first();
			incident.creationDate = new Date();
			ER_User user = connectedUser();
			if(user.distributor != null){
				incident.distributor = user.distributor;
				incident.channel = user.distributor.channel;
			}else if(user.channel != null){
				incident.channel = user.channel;
			}
    		//create inspection if car is not new
	    	if(incident.vehicle.isNew != null && !incident.vehicle.isNew){
	    		if(userRol.equals(ERConstants.USER_ROLE_RUNNER_SELLER)){
		    		incident.inspection.inspected = false;
	    		}
	    	}
    		incident = incident.save();
	    	
	    	//Save quotations to database
	    	for(ER_Quotation quotation : incident.quotations){
	    		quotation.product = ER_Product.findById(quotation.product.id);
	    		quotation.incident = incident;
	    		quotation.creationDate = new Date();
	    		
	    		try{
	    			ER_Vehicle_Line line = ER_Vehicle_Line.findById(incident.vehicle.line.id);
					ER_Year erYear = ER_Year.findById(incident.vehicle.erYear.id);
	    			QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
	            	request.setBrand(line.brand.name);
	        		request.setLine(line.name);
	        		request.setYear(erYear.year);
	        		request.setCurrency("Q");
	        		QueryAverageValueVehicleResponse queryAverage = averageValueServiceBus.averageValueQuery(request);
	        		if(queryAverage != null && queryAverage.getAverageValue() != null && quotation.carValue != null){
	        			BigDecimal diff = queryAverage.getAverageValue().multiply(new BigDecimal(0.15));
	        			BigDecimal min = queryAverage.getAverageValue().subtract(diff);
	        			BigDecimal max = queryAverage.getAverageValue().add(diff);
	        			
	        			if(quotation.carValue.compareTo(queryAverage.getAverageValue()) == 0){
	        				quotation.quotationDetail.setVehicleValue(queryAverage.getAverageValue());
	        			}else if(quotation.carValue.compareTo(min) >= -1 && quotation.carValue.compareTo(max) <= 1){
	        				quotation.quotationDetail.setVehicleValue(quotation.carValue);
	        			}
	        		}else{
						String information = line.brand.name.trim() + "|" + line.name.trim() + "|" + erYear.year.trim();
	        			ER_Average_Value averageValue = ER_Average_Value.find("name = ?", information).first();
	        	    	if(averageValue != null && quotation.carValue.compareTo(averageValue.value) == 0){
	        	    		quotation.quotationDetail.setVehicleValue(averageValue.value);
	        	    	}
	        		}
				}catch(Exception e){
					e.printStackTrace();
				}
	    		quotation.save();
	    		quotation.loadDetailJSON();
			}
	    	
	    	List<ByteArrayOutputStream> streamArray = new ArrayList<ByteArrayOutputStream>();
	    	List<ER_Quotation> quotations = ER_Quotation.find("incident = ?", incident).fetch();
	    	for(ER_Quotation quotation: quotations){
	    		quotation.loadDetailJSON();
	    		streamArray.add(quotationPDFData(quotation));
	    	}
			SendQuotationsJob sendQuotationsJob = new SendQuotationsJob(streamArray, incident);
			sendQuotationsJob.now();
	    	/*Mails.quotations(incident, streamArray, false);
	    	Mails.incidentDetail(incident);
	    	*/
	    	successful(incident.id);
    	}catch(Exception e){
    		Logger.error(e, "Error saving incident");
    		successful(null);
    	}
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void successful(Long id) {
    	if(id != null){
	    	ER_Incident incident = ER_Incident.findById(id);
	    	if(incident != null){
	    		render(incident);
	    	}
    	}
    	String errorMessage = Messages.get("quotation.form.create.error");
		render(errorMessage);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Genera los datos de la cotización de PDF
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    private static ByteArrayOutputStream quotationPDFData(ER_Quotation quotation) {
    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	
    	String additionalBenefits = "";
    	if (quotation.product.additionalBenefits != null && !"".equals(quotation.product.additionalBenefits)) {
    		additionalBenefits = quotation.product.additionalBenefits;
    	} else {
    		additionalBenefits = configuration.additionalBenefits;
    	}
    	String[] additionalBenefitsArray = additionalBenefits.split("[\r\n]+");
    	
    	//Set the size of the PDF to Letter portrait
    	Options options = new Options();
    	options.pageSize = IHtmlToPdfTransformer.LETTERP;

        Long idCreator = quotation.incident.creator.id;

		ER_User userTemp = ER_User.findById(idCreator);
		ER_Distributor_Custom_Logo customLogoDistributor = null;
		if(userTemp.distributor != null)
			customLogoDistributor = ER_Distributor_Custom_Logo.find("distributor.id",userTemp.distributor.id).first();

        Boolean hasCustomLogo = false;
        String customLogoPath = "";

		if(customLogoDistributor !=null && customLogoDistributor.active && customLogoDistributor.bannerName != null){
			hasCustomLogo = customLogoDistributor.active;
			customLogoPath = "/public/images/custom/" + customLogoDistributor.bannerName;
		}else {
			ER_User_Custom_Logo customLogo = ER_User_Custom_Logo.find("user.id", idCreator).first();
			if (customLogo != null && customLogo.bannerName != null) {
				hasCustomLogo = customLogo.active;
				customLogoPath = "/public/images/custom/" + customLogo.bannerName;
			}
		}

    	PDF.MultiPDFDocuments docs = new PDF.MultiPDFDocuments();
    	docs.add("Forms/Quotation.html", options);
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	PDF.writePDF(os, docs, quotation, configuration, additionalBenefits, additionalBenefitsArray,hasCustomLogo, customLogoPath);
    	
    	return os;
    }
    
    /**
     * Method to calculate the average Value
     * @param information
     */
    public static void getByInfo(String information) {
    	Locale locale = new Locale("es", "GT");
    	NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
    	Map<String, Object> response = new HashMap<String, Object>();
    	response.put("success", false);
    	
    	String[] data = information.split("\\|");
    	if(data.length > 2){
    		QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
        	request.setBrand(data[0]);
    		request.setLine(data[1]);
    		request.setYear(data[2]);
    		request.setCurrency("Q");
    		QueryAverageValueVehicleResponse queryAverage = averageValueServiceBus.averageValueQuery(request);
    		if(queryAverage != null && queryAverage.getAverageValue() != null){
	    		response.put("averageValue", currencyFormatter.format(queryAverage.getAverageValue()));
	    		response.put("success", true);
    		}
    	}
    	
    	renderJSON(response);
    }


}
