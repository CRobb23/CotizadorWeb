package service.implementation;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.Years;

import helpers.DateHelper;
import helpers.FieldAccesor;
import helpers.GeneralHelper;
import models.ER_General_Configuration;
import models.ER_Incident;
import models.ER_Product_Coverage;
import models.ER_Product_Coverage_Class_Value;
import models.ER_Product_Coverage_Value;
import models.ws.BusinessClientRequest;
import models.ws.CoveragesRequest;
import models.ws.CoveragesRequest.Coverage;
import models.ws.PayerRequest;
import models.ws.PaymentMethodRequest;
import models.ws.PersonClientRequest;
import models.ws.PolicyRequest;
import models.ws.PolicyResponse;
import models.ws.PrimeRequest;
import models.ws.QueryAverageValueVehicleRequest;
import models.ws.QueryClientRequest;
import models.ws.QueryVehicleRequest;
import models.ws.VehicleRequest;
import models.ws.VehicleRequest.Damage;
import models.ws.WorkFlowRequest;
import models.ws.YoungerRequest;
import objects.CoverageCost;
import objects.CoverageCostCategory;
import objects.PaymentOption;
import play.modules.guice.InjectSupport;
import service.CreateRequestService;

@InjectSupport
public class CreateRequestServiceImpl implements CreateRequestService{
	
	public QueryClientRequest createQueryRequest(ER_Incident incident){
    	try{
    		QueryClientRequest request = new QueryClientRequest();
    		request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.taxNumber));
        	request.setIdentificationDocument(incident.client.identificationDocument);
        	request.setName(incident.client.getFullName());
	    	
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public QueryAverageValueVehicleRequest createQueryAverageValueVehicleRequest(ER_Incident incident){
    	try{
    		QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
    		request.setBrand(incident.vehicle.line.brand.name.replace(" ", ""));
    		request.setLine(incident.vehicle.line.name.replace(" ", ""));
    		request.setYear(incident.vehicle.erYear.year);
    		request.setCurrency("Q");
	    	
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public QueryVehicleRequest createQueryVehicleRequest(ER_Incident incident){
    	try{
    		QueryVehicleRequest request = new QueryVehicleRequest();
    		request.setCurrency("Q");
//    		request.setPlate(incident.vehicle.plate);
//    		request.setChassis(incident.vehicle.chassis);
    		request.setLine(incident.vehicle.line.transferCode);
	    	request.setYearVehicle(incident.vehicle.erYear.year);
	    	
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public PersonClientRequest createPersonClientRequest(ER_Incident incident){
    	try{
    		PersonClientRequest request = new PersonClientRequest();
    		request.setTaxNumber(incident.client.taxNumber);
			//request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.taxNumber));
			request.setQuoteNumber(incident.number.toString());
        	request.setQuotationDate(DateHelper.formatDate(incident.selectedQuotation.creationDate, "dd/MM/yyyy"));
        	request.setBirthdate(DateHelper.formatDate(incident.client.birthdate, "dd/MM/yyy"));
	    	request.setEmail(incident.client.email);
	    	request.setFirstName(incident.client.firstName);
	    	request.setFirstSurname(incident.client.firstSurname);
	    	request.setSecondName(incident.client.secondName);
	    	request.setSecondSurname(incident.client.secondSurname);
	    	request.setMarriedSurname(incident.client.marriedSurname);
	    	request.setIdentificationDocument(incident.client.identificationDocument);
	    	request.setLicenseNumber(incident.client.licenseNumber);
	    	//request.setCodeTitle(incident.client.title.transferCode);
	    	request.setCivilStatus(incident.client.civilStatus.transferCode);
	    	request.setNationality(incident.client.nationality.transferCode);
	    	request.setProfession(incident.client.profession.transferCode);
	    	request.setSex(incident.client.sex.transferCode);
	    	request.setLicenseType(incident.client.licenseType.transferCode);
	    	request.setAge(Years.yearsBetween(new DateTime(incident.client.birthdate), new DateTime()).getYears());
	    	request.setCodeCifBank(incident.client.codeCifBank);
	    	request.setPassport(incident.client.passport);
	    	request.setVip(null);
	    	request.setAddress(new PersonClientRequest.Address());
	    	request.getAddress().setHomeAddress(new PersonClientRequest.Address.HomeAddress());
	    	request.getAddress().getHomeAddress().setAddress(incident.client.address);
	    	request.getAddress().getHomeAddress().setColony(null);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.country.transferCode"))
	    	request.getAddress().getHomeAddress().setCountry(incident.client.country.transferCode);
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.department.transferCode")){
	    		request.getAddress().getHomeAddress().setDepartment(incident.client.department.transferCode);
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.municipality.transferCode")){
	    		request.getAddress().getHomeAddress().setMunicipality(incident.client.municipality.transferCode.replace(" ", "-"));
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.zone.transferCode")){
	    		request.getAddress().getHomeAddress().setZone(incident.client.zone.transferCode.replace(" ", "-"));
	    	}
	    	request.getAddress().getHomeAddress().setPhone1(incident.client.phoneNumber1);
	    	request.getAddress().getHomeAddress().setPhone2(incident.client.phoneNumber2);
	    	request.getAddress().getHomeAddress().setPhone3(incident.client.phoneNumber3);
	    	request.getAddress().setWorkAddress(new PersonClientRequest.Address.WorkAddress());
			if(!FieldAccesor.isEmptyOrNull(incident, "client.addressWork"))
	    	request.getAddress().getWorkAddress().setAddress(incident.client.addressWork);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.countryWork.transferCode"))
	    	request.getAddress().getWorkAddress().setCountry(incident.client.countryWork.transferCode);
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.workDepartment.transferCode")){
	    		request.getAddress().getWorkAddress().setDepartment(incident.client.workDepartment.transferCode);
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.workMunicipality.transferCode")){
	    		request.getAddress().getWorkAddress().setMunicipality(incident.client.workMunicipality.transferCode.replace(" ", "-"));
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.workZone.transferCode")){
	    		request.getAddress().getWorkAddress().setZone(incident.client.workZone.transferCode.replace(" ", "-"));
	    	}
	    	request.getAddress().getWorkAddress().setPhone1(incident.client.phoneNumberWork1);
	    	request.getAddress().getWorkAddress().setPhone2(incident.client.phoneNumberWork2);
	    	request.getAddress().getWorkAddress().setPhone3(incident.client.phoneNumberWork3);
	    	
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public BusinessClientRequest createBusinessClientRequest(ER_Incident incident){
    	try{
    		BusinessClientRequest request = new BusinessClientRequest();
			request.setCodeClient(incident.client.codeClient);
    		request.setTaxNumber(incident.client.taxNumber);
			//request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.taxNumber));
			request.setQuoteNumber(incident.number.toString());
        	request.setQuotationDate(DateHelper.formatDate(incident.selectedQuotation.creationDate, "dd/MM/yyyy"));
        	request.setTaxNumber(incident.client.taxNumber);
        	request.setSocietyType(incident.client.societyType.transferCode);
        	request.setCompanyName(incident.client.companyName);
        	request.setBusinessName(incident.client.businessName);
        	request.setEconomicActivity(incident.client.economicActivity.transferCode);
        	request.setNationality(incident.client.nationality.transferCode);
        	request.setDpiCompany(incident.client.identificationDocument);
        	request.setStateProvider(incident.client.stateProvider);
	    	request.setEmail(incident.client.email);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.writeNumber"))
	    	request.setWriteNumber(incident.client.writeNumber);
	    	request.setWriteDate(DateHelper.formatDate(incident.client.writeDate, "dd/MM/yyyy"));
	    	request.setRegistrationDate(DateHelper.formatDate(incident.client.registrationDate, "dd/MM/yyyy"));
	    	request.setCondeCifBank(incident.client.codeCifBank);
	    	request.setCodeClient(incident.client.codeClient);


	    	request.setLegalRepresentative(new BusinessClientRequest.LegalRepresentative());

	    	request.getLegalRepresentative().setFirstName(incident.client.legalRepresentative.firstName);
	    	request.getLegalRepresentative().setSecondName(incident.client.legalRepresentative.secondName);
	    	request.getLegalRepresentative().setFirstSurname(incident.client.legalRepresentative.firstSurname);
	    	request.getLegalRepresentative().setSecondSurname(incident.client.legalRepresentative.secondSurname);
	    	request.getLegalRepresentative().setMarriedSurname(incident.client.legalRepresentative.marriedSurname);
	    	request.getLegalRepresentative().setNationality(incident.client.legalRepresentative.nationality.transferCode);
	    	request.getLegalRepresentative().setRegister(incident.client.legalRepresentative.registry);
	    	request.getLegalRepresentative().setCaseFile(incident.client.legalRepresentative.caseFile);
	    	request.getLegalRepresentative().setExtendedIn(incident.client.legalRepresentative.extendedIn);
	    	request.getLegalRepresentative().setRegistrationDate(DateHelper.formatDate(incident.client.legalRepresentative.registrationDate, "dd/MM/yyyy"));
	    	request.getLegalRepresentative().setBirthdate(DateHelper.formatDate(incident.client.legalRepresentative.birthdate, "dd/MM/yyyy"));
	    	request.getLegalRepresentative().setProfession(incident.client.legalRepresentative.profession.transferCode);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.book"))
	    	request.getLegalRepresentative().setBook(incident.client.legalRepresentative.book);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.folio"))
	    	request.getLegalRepresentative().setFolio(incident.client.legalRepresentative.folio);
	    	request.getLegalRepresentative().setDpi(incident.client.legalRepresentative.identificationDocument);
	    	request.getLegalRepresentative().setTaxNumber(incident.client.legalRepresentative.taxNumber);
	    	request.getLegalRepresentative().setPassport(incident.client.legalRepresentative.passport);
	    	request.getLegalRepresentative().setSex(incident.client.legalRepresentative.sex.transferCode);
	    	request.getLegalRepresentative().setCivilStatus(incident.client.legalRepresentative.civilStatus.transferCode);
	    	request.getLegalRepresentative().setEmail(incident.client.legalRepresentative.email);
//	    	request.getLegalRepresentative().setMonthlyAmountIncome(incident.client.legalRepresentative.monthlyAmountIncome);
//	    	request.getLegalRepresentative().setMonthlyAmountOfIncome(incident.client.legalRepresentative.monthlyAmountOfIncome);
	    	
	    	request.setAddress(new BusinessClientRequest.Address());
	    	request.getAddress().setWorkAddress(new BusinessClientRequest.Address.WorkAddress());
			if(!FieldAccesor.isEmptyOrNull(incident, "client.address"))
	    	request.getAddress().getWorkAddress().setAddress(incident.client.address);
	    	request.getAddress().getWorkAddress().setColony(null);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.country.transferCode"))
	    	request.getAddress().getWorkAddress().setCountry(incident.client.country.transferCode);
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.department.transferCode")){
	    		request.getAddress().getWorkAddress().setDepartment(incident.client.department.transferCode);
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.municipality.transferCode")){
	    		request.getAddress().getWorkAddress().setMunicipality(incident.client.municipality.transferCode.replace(" ", "-"));
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.zone.transferCode")){
	    		request.getAddress().getWorkAddress().setZone(incident.client.zone.transferCode.replace(" ", "-"));
	    	}
			if(!FieldAccesor.isEmptyOrNull(incident, "client.phoneNumber1"))
	    	request.getAddress().getWorkAddress().setPhone1(incident.client.phoneNumber1);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.phoneNumber2"))
	    	request.getAddress().getWorkAddress().setPhone2(incident.client.phoneNumber2);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.phoneNumber3"))
	    	request.getAddress().getWorkAddress().setPhone3(incident.client.phoneNumber3);	    	
	    	request.getAddress().setRepresentativeLegalAddress(new BusinessClientRequest.Address.RepresentativeLegalAddress());
	    	if (incident.client.legalRepresentative.address!=null)
	    	request.getAddress().getRepresentativeLegalAddress().setAddress(incident.client.legalRepresentative.address);
	    	else
				request.getAddress().getRepresentativeLegalAddress().setAddress("");
			if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.country.transferCode"))
	    	request.getAddress().getRepresentativeLegalAddress().setCountry(incident.client.legalRepresentative.country.transferCode);
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.department.transferCode")){
	    		request.getAddress().getRepresentativeLegalAddress().setDepartment(incident.client.legalRepresentative.department.transferCode);
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.municipality.transferCode")){
	    		request.getAddress().getRepresentativeLegalAddress().setMunicipality(incident.client.legalRepresentative.municipality.transferCode.replace(" ", "-"));
	    	}
	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.zone.transferCode")){
	    		request.getAddress().getRepresentativeLegalAddress().setZone(incident.client.legalRepresentative.zone.transferCode.replace(" ", "-"));
	    	}
			if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.phoneNumber1"))
	    	request.getAddress().getRepresentativeLegalAddress().setPhone1(incident.client.legalRepresentative.phoneNumber1);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.phoneNumber2"))
	    	request.getAddress().getRepresentativeLegalAddress().setPhone2(incident.client.legalRepresentative.phoneNumber2);
			if(!FieldAccesor.isEmptyOrNull(incident, "client.legalRepresentative.phoneNumber3"))
	    	request.getAddress().getRepresentativeLegalAddress().setPhone3(incident.client.legalRepresentative.phoneNumber3);
	    	
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public PayerRequest createPayerRequest(ER_Incident incident){
    	try{
    		PayerRequest request = new PayerRequest();
    		if(incident.client.useDataClientPayer != null && incident.client.useDataClientPayer){
        		if(incident.client.isIndividual != null && !incident.client.isIndividual){
                    request.setTypeClient("2");

                    request.setTaxNumber(incident.client.taxNumber);
					//request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.taxNumber));
					request.setQuoteNumber(incident.number.toString());
                    request.setTaxNumber(incident.client.taxNumber);
                    request.setTypeSociety(incident.client.societyType.transferCode);
                    request.setCompanyName(incident.client.companyName);
                    request.setBusinessName(incident.client.businessName);
                    request.setEconomicActivity(incident.client.economicActivity.transferCode);
                    request.setNationality(incident.client.nationality.transferCode);
                    request.setStateProvider(incident.client.stateProvider);
                    request.setEmail(incident.client.email);
                    request.setWriteNumber(incident.client.writeNumber);
                    request.setWriteDate(DateHelper.formatDate(incident.client.writeDate, "dd/MM/yyyy"));
                    request.setRegistrationDate(DateHelper.formatDate(incident.client.registrationDate, "dd/MM/yyyy"));
                    request.setCodeClient(incident.client.codeClient);

                    request.setAddress(new PayerRequest.Address());
                    request.getAddress().setWorkAddress(new PayerRequest.Address.WorkAddress());
					if(!FieldAccesor.isEmptyOrNull(incident, "client.client.address")){
						request.getAddress().getWorkAddress().setAddress(incident.client.address);
					}
					if(!FieldAccesor.isEmptyOrNull(incident, "client.country.transferCode")){
						request.getAddress().getWorkAddress().setDepartment(incident.client.country.transferCode);
					}
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.department.transferCode")){
                        request.getAddress().getWorkAddress().setDepartment(incident.client.department.transferCode);
                    }
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.municipality.transferCode")){
                        request.getAddress().getWorkAddress().setMunicipality(incident.client.municipality.transferCode.replace(" ", "-"));
                    }
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.zone.transferCode")){
                        request.getAddress().getWorkAddress().setZone(incident.client.zone.transferCode.replace(" ", "-"));
                    }
                    request.getAddress().getWorkAddress().setPhone1(incident.client.phoneNumber1);
                    request.getAddress().getWorkAddress().setPhone2(incident.client.phoneNumber2);
                    request.getAddress().getWorkAddress().setPhone3(incident.client.phoneNumber3);

        		}else if(incident.client.isIndividual == null || incident.client.isIndividual){
        			request.setTypeClient("5");

            		request.setTaxNumber(incident.client.taxNumber);
					//request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.taxNumber));
					request.setQuoteNumber(incident.number.toString());
                	request.setBirthdate(DateHelper.formatDate(incident.client.birthdate, "dd/MM/yyy"));
        	    	request.setEmail(incident.client.email);
        	    	request.setFirstName(incident.client.firstName);
        	    	request.setFirstSurname(incident.client.firstSurname);
        	    	request.setSecondName(incident.client.secondName);
        	    	request.setSecondSurname(incident.client.secondSurname);
        	    	request.setMarriedSurname(incident.client.marriedSurname);
        	    	request.setIdentificationDocument(incident.client.identificationDocument);
        	    	request.setLicenseNumber(incident.client.licenseNumber);
        	    	//request.setCodeTitle(incident.client.title.transferCode);
        	    	request.setCivilStatus(incident.client.civilStatus.transferCode);
        	    	request.setNationality(incident.client.nationality.transferCode);
        	    	request.setProfession(incident.client.profession.transferCode);
        	    	request.setSex(incident.client.sex.transferCode);
        	    	request.setLicenseType(incident.client.licenseType.transferCode);
        	    	request.setAge(Years.yearsBetween(new DateTime(incident.client.birthdate), new DateTime()).getYears());
        	    	request.setCodeCifBank(incident.client.codeCifBank);
        	    	request.setPassport(incident.client.passport);
        	    	request.setAddress(new PayerRequest.Address());
        	    	request.getAddress().setHomeAddress(new PayerRequest.Address.HomeAddress());
        	    	request.getAddress().getHomeAddress().setAddress(incident.client.address);
        	    	request.getAddress().getHomeAddress().setColony(null);
					if(!FieldAccesor.isEmptyOrNull(incident, "client.country.transferCode")){
						request.getAddress().getHomeAddress().setDepartment(incident.client.country.transferCode);
					}
        	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.department.transferCode")){
        	    		request.getAddress().getHomeAddress().setDepartment(incident.client.department.transferCode);
        	    	}
        	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.municipality.transferCode")){
        	    		request.getAddress().getHomeAddress().setMunicipality(incident.client.municipality.transferCode.replace(" ", "-"));
        	    	}
        	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.zone.transferCode")){
        	    		request.getAddress().getHomeAddress().setZone(incident.client.zone.transferCode.replace(" ", "-"));
        	    	}
        	    	request.getAddress().getHomeAddress().setPhone1(incident.client.phoneNumber1);
        	    	request.getAddress().getHomeAddress().setPhone2(incident.client.phoneNumber2);
        	    	request.getAddress().getHomeAddress().setPhone3(incident.client.phoneNumber3);
        	    	request.getAddress().setWorkAddress(new PayerRequest.Address.WorkAddress());
        	    	request.getAddress().getWorkAddress().setAddress(incident.client.addressWork);
					if(!FieldAccesor.isEmptyOrNull(incident, "client.countryWork.transferCode")){
						request.getAddress().getWorkAddress().setDepartment(incident.client.countryWork.transferCode);
					}
        	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.workDepartment.transferCode")){
        	    		request.getAddress().getWorkAddress().setDepartment(incident.client.workDepartment.transferCode);
        	    	}
        	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.workMunicipality.transferCode")){
        	    		request.getAddress().getWorkAddress().setMunicipality(incident.client.workMunicipality.transferCode.replace(" ", "-"));
        	    	}
        	    	if(!FieldAccesor.isEmptyOrNull(incident, "client.workZone.transferCode")){
        	    		request.getAddress().getWorkAddress().setZone(incident.client.workZone.transferCode.replace(" ", "-"));
        	    	}
        	    	request.getAddress().getWorkAddress().setPhone1(incident.client.phoneNumberWork1);
        	    	request.getAddress().getWorkAddress().setPhone2(incident.client.phoneNumberWork2);
        	    	request.getAddress().getWorkAddress().setPhone3(incident.client.phoneNumberWork3);
        		}
    		}else{
    		    if (incident.client.payer.isIndividual != null && !incident.client.payer.isIndividual) {
                    request.setTypeClient("2");

                    request.setTaxNumber(incident.client.payer.taxNumber);
					//request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.payer.taxNumber));
					request.setQuoteNumber(incident.number.toString());
                    request.setTaxNumber(incident.client.payer.taxNumber);
                    request.setTypeSociety(incident.client.payer.societyType.transferCode);
                    request.setCompanyName(incident.client.payer.companyName);
                    request.setBusinessName(incident.client.payer.businessName);
                    request.setEconomicActivity(incident.client.payer.economicActivity.transferCode);
                    request.setNationality(incident.client.payer.nationality.transferCode);
                    request.setStateProvider(incident.client.payer.stateProvider);
                    request.setEmail(incident.client.payer.email);
                    request.setWriteNumber(incident.client.payer.writeNumber);
                    request.setWriteDate(DateHelper.formatDate(incident.client.payer.writeDate, "dd/MM/yyyy"));
                    request.setRegistrationDate(DateHelper.formatDate(incident.client.payer.registrationDate, "dd/MM/yyyy"));
                    request.setCodeClient(incident.client.payer.codeClient);

                    request.setAddress(new PayerRequest.Address());
                    request.getAddress().setWorkAddress(new PayerRequest.Address.WorkAddress());
                    request.getAddress().getWorkAddress().setAddress(incident.client.payer.address);
					if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.country.transferCode")){
						request.getAddress().getWorkAddress().setDepartment(incident.client.payer.country.transferCode);
					}
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.department.transferCode")){
                        request.getAddress().getWorkAddress().setDepartment(incident.client.payer.department.transferCode);
                    }
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.municipality.transferCode")){
                        request.getAddress().getWorkAddress().setMunicipality(incident.client.payer.municipality.transferCode.replace(" ", "-"));
                    }
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.zone.transferCode")){
                        request.getAddress().getWorkAddress().setZone(incident.client.payer.zone.transferCode.replace(" ", "-"));
                    }
                    request.getAddress().getWorkAddress().setPhone1(incident.client.payer.phoneNumber1);
                    request.getAddress().getWorkAddress().setPhone2(incident.client.payer.phoneNumber2);
                    request.getAddress().getWorkAddress().setPhone3(incident.client.payer.phoneNumber3);
                } else {
                    request.setTypeClient("5");

                    request.setTaxNumber(incident.client.payer.taxNumber);
					//request.setTaxNumber(GeneralHelper.addDashToNit(incident.client.payer.taxNumber));
					request.setQuoteNumber(incident.number.toString());
                    request.setBirthdate(DateHelper.formatDate(incident.client.payer.birthdate, "dd/MM/yyy"));
                    request.setEmail(incident.client.payer.email);
                    request.setFirstName(incident.client.payer.firstName);
                    request.setFirstSurname(incident.client.payer.firstSurname);
                    request.setSecondName(incident.client.payer.secondName);
                    request.setSecondSurname(incident.client.payer.secondSurname);
                    request.setMarriedSurname(incident.client.payer.marriedSurname);
                    request.setIdentificationDocument(incident.client.payer.identificationDocument);
                    request.setLicenseNumber(incident.client.payer.licenseNumber);
                    //request.setCodeTitle(incident.client.payer.title.transferCode);
                    request.setCivilStatus(incident.client.payer.civilStatus.transferCode);
                    request.setNationality(incident.client.payer.nationality.transferCode);
                    request.setProfession(incident.client.payer.profession.transferCode);
                    request.setSex(incident.client.payer.sex.transferCode);
                    request.setLicenseType(incident.client.payer.licenseType.transferCode);
                    request.setAge(Years.yearsBetween(new DateTime(incident.client.payer.birthdate), new DateTime()).getYears());
                    request.setCodeCifBank(incident.client.payer.codeCifBank);
                    request.setPassport(incident.client.payer.passport);
                    request.setAddress(new PayerRequest.Address());
                    request.getAddress().setHomeAddress(new PayerRequest.Address.HomeAddress());
                    request.getAddress().getHomeAddress().setAddress(incident.client.payer.address);
                    request.getAddress().getHomeAddress().setColony(null);
					if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.country.transferCode")){
						request.getAddress().getHomeAddress().setDepartment(incident.client.payer.country.transferCode);
					}
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.department.transferCode")){
                        request.getAddress().getHomeAddress().setDepartment(incident.client.payer.department.transferCode);
                    }
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.municipality.transferCode")){
                        request.getAddress().getHomeAddress().setMunicipality(incident.client.payer.municipality.transferCode.replace(" ", "-"));
                    }
                    if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.zone.transferCode")){
                        request.getAddress().getHomeAddress().setZone(incident.client.payer.zone.transferCode.replace(" ", "-"));
                    }
                    request.getAddress().getHomeAddress().setPhone1(incident.client.payer.phoneNumber1);
                    request.getAddress().getHomeAddress().setPhone2(incident.client.payer.phoneNumber2);
                    request.getAddress().getHomeAddress().setPhone3(incident.client.payer.phoneNumber3);
                    if (incident.client.payer.addressWork != null) {
                        request.getAddress().setWorkAddress(new PayerRequest.Address.WorkAddress());
                        request.getAddress().getWorkAddress().setAddress(incident.client.payer.addressWork);
						if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.countryWork.transferCode")){
							request.getAddress().getWorkAddress().setDepartment(incident.client.payer.countryWork.transferCode);
						}
                        if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.workDepartment.transferCode")){
                            request.getAddress().getWorkAddress().setDepartment(incident.client.payer.workDepartment.transferCode);
                        }
                        if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.workMunicipality.transferCode")){
                            request.getAddress().getWorkAddress().setMunicipality(incident.client.payer.workMunicipality.transferCode.replace(" ", "-"));
                        }
                        if(!FieldAccesor.isEmptyOrNull(incident, "client.payer.workZone.transferCode")){
                            request.getAddress().getWorkAddress().setZone(incident.client.payer.workZone.transferCode.replace(" ", "-"));
                        }
                        request.getAddress().getWorkAddress().setPhone1(incident.client.payer.phoneNumberWork1);
                        request.getAddress().getWorkAddress().setPhone2(incident.client.payer.phoneNumberWork2);
                        request.getAddress().getWorkAddress().setPhone3(incident.client.payer.phoneNumberWork3);
                    }
                }
    		}
    		
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public PolicyRequest createPolicyRequest(ER_Incident incident){
    	try{
    		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    		PolicyRequest request = new PolicyRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCurrency("Q");
    		request.setQuotationDate(DateHelper.formatDate(incident.selectedQuotation.creationDate, "dd/MM/yyyy"));
    		request.setPaymentMethod("01");
    		request.setTypeLine((incident.client.isIndividual != null && !incident.client.isIndividual) ? "02" : "01");
    		request.setValidSice(DateHelper.formatDate(incident.policyValidity, "dd/MM/yyyy"));
    		request.setValidUntil(DateHelper.formatDate(DateHelper.addMonths(incident.policyValidity, 12), "dd/MM/yyyy"));
    		request.setCodeAgent("801");
    		if(!FieldAccesor.isEmptyOrNull(configuration.agentCodeAS400)){
    			request.setCodeAgent(configuration.agentCodeAS400);
    		}
    		if(!FieldAccesor.isEmptyOrNull(incident, "creator.profile.agentCode")){
    			request.setCodeAgent(incident.creator.profile.agentCode);
    		}
    		BigDecimal totalDiscount = BigDecimal.ZERO;
			totalDiscount = totalDiscount.add(incident.selectedQuotation.quotationDetail.getPrimeDiscount());
			totalDiscount = totalDiscount.add(incident.selectedQuotation.quotationDetail.getDiscount());
    		request.setDiscountRate(totalDiscount.setScale(2, RoundingMode.HALF_UP));
    		
    		if(incident.channel != null && !FieldAccesor.isEmptyOrNull(incident.channel.transferCode)){
    			request.setEmisionZone(incident.channel.transferCode);
    		}else if(!FieldAccesor.isEmptyOrNull(incident.emissionZone)){
    			request.setEmisionZone(incident.emissionZone);
    		}else{
    			// 0101|OFICINA CENTRAL
    			request.setEmisionZone("0101");
    		}
    		request.setTypePolicy("1");
    		request.setTypeProduct("1");
    		// 1|PRODUCCION DIRECTA
    		request.setTypeProduction("01");
    		if (incident.channel != null && incident.channel.portfolioType != null) {
				request.setWalletType(incident.channel.portfolioType.transferCode);
			} else {
				// 01|CARTERA DIRECTA LINEAS PERSONALES
				request.setWalletType("01");
			}

	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }

    public VehicleRequest createVehicleRequest(ER_Incident incident){
    	try{
    		VehicleRequest request = new VehicleRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCurrency("Q");
    		request.setChasis(incident.vehicle.chassis);
    		request.setEngine(incident.vehicle.engine);
    		request.setPlate(incident.vehicle.plateType.transferCode.trim() + incident.vehicle.plate);
            request.setLine(String.format("%6s", incident.vehicle.line.transferCode));
    		request.setTypeVehicle(incident.vehicle.type.transferCode);
    		request.setRateType(incident.selectedQuotation.product.rateTypeTransferCode);
    		request.setArmor(incident.vehicle.armor);
    		request.setArmorValue(incident.vehicle.armorValue);
    		request.setReminderCode(incident.vehicle.reminderType.transferCode);
    		request.setYear(incident.vehicle.erYear.year);
    		request.setNumberOfPassengers(incident.vehicle.numberOfPassengers);
    		request.setColor(incident.vehicle.color);
    		request.setCubicCentimeter(incident.vehicle.cubicCentimeter);
    		request.setNumberCylinders(incident.vehicle.numberCylinders);
    		request.setNumberAxes(incident.vehicle.numberAxes);
    		request.setNumberDoor(incident.vehicle.numberDoor);
    		request.setHelmetClass(incident.vehicle.helmetClass);
    		request.setVehicleValue(incident.selectedQuotation.carValue);
    		request.setMileage(incident.vehicle.mileage);
    		request.setTypeMileage(incident.vehicle.typeMileage);
    		request.setFuelType(incident.vehicle.fuelType);
    		request.setTonnage(incident.vehicle.tonnage);
    		request.setLoanNumber(incident.vehicle.loanNumber);
    		request.setWarranty(incident.vehicle.warranty);
    		request.setBeneficiaryCodeWarranty(incident.vehicle.beneficiaries != null ? incident.vehicle.beneficiaries.transferCode : null);
    		request.setPreExistingDamage(incident.vehicle.preExistingDamage);
    		request.setPromotionCode(incident.vehicle.promotionCode);
    		request.setSpecialTeam(incident.vehicle.specialTeam);
    		request.setVehicleOwner(incident.vehicle.owner);

    		if(incident.vehicle.isNew != null && incident.vehicle.isNew){
    			request.setSelection("S");	
    		}else{
    			request.setSelection("N");
    		}
    		if(incident.inspection != null && incident.inspection.inspectionNumber != null){
    			request.setInspectionNumber(incident.inspection.inspectionNumber);
    			if(!FieldAccesor.isEmptyOrNull(incident, "inspection.existentDamage")){
    				request.setDamageList(new ArrayList<Damage>());
    				Integer beginIndex = 0;
    				Integer endIndex = 60;
    				request.setPreExistingDamage("S");
    				if(incident.inspection.existentDamage.length() <= endIndex){
    					VehicleRequest.Damage damage = new VehicleRequest.Damage();
    					damage.setNumberLine(1);
    					damage.setDescriptionLine(incident.inspection.existentDamage);
    					request.getDamageList().add(damage);
    				}else{
    					int lines = incident.inspection.existentDamage.length() / 60;
    					if (incident.inspection.existentDamage.length() % 60 > 1) {
    						lines++;
						}
	    				for(int i=1; i <= lines; i++){
	    					VehicleRequest.Damage damage = new VehicleRequest.Damage();
	    					damage.setNumberLine(i);
	    					if (endIndex > incident.inspection.existentDamage.length()) {
								endIndex = incident.inspection.existentDamage.length();
							}
	    					damage.setDescriptionLine(incident.inspection.existentDamage.substring(beginIndex, endIndex));
	    					request.getDamageList().add(damage);
	    					beginIndex = (beginIndex + 60);
	    					endIndex = (endIndex + 60);
	    				}
    				}
    			}
    			if (!FieldAccesor.isEmptyOrNull(incident, "inspection.soundEquipment")) {
    			    request.setSpecialTeam("S");
                    request.setSpecialTeamList(new ArrayList<VehicleRequest.SpecialTeam>());
                    Integer beginIndex = 0;
                    Integer endIndex = 30;
                    if(incident.inspection.soundEquipment.length() <= endIndex){
                        VehicleRequest.SpecialTeam specialTeam = new VehicleRequest.SpecialTeam();
                        specialTeam.setNumberLine(1);
                        specialTeam.setDescriptionLine(incident.inspection.soundEquipment);
                        request.getSpecialTeamList().add(specialTeam);
                    }else{
						int lines = incident.inspection.soundEquipment.length() / 30;
						if (incident.inspection.soundEquipment.length() % 30 > 1) {
							lines++;
						}
						if (lines > 6) {
							lines = 6;
						}
						for(int i=1; i <= lines; i++){
							VehicleRequest.SpecialTeam specialTeam = new VehicleRequest.SpecialTeam();
                            specialTeam.setNumberLine(i);
							if (endIndex > incident.inspection.soundEquipment.length()) {
								endIndex = incident.inspection.soundEquipment.length();
							}
							specialTeam.setDescriptionLine(incident.inspection.soundEquipment.substring(beginIndex, endIndex));
                            request.getSpecialTeamList().add(specialTeam);
                            beginIndex = (beginIndex + 30);
                            endIndex = (endIndex + 30);
                        }
                    }
                }
    		}else{
    			request.setInspectionNumber("000000");
    		}
    		if (!incident.selectedQuotation.hasCarValue()) {
    		    request.setOnlyThird("T");
            }

	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }

    public CoveragesRequest createCoveragesRequest(ER_Incident incident){
    	try{
    		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    		CoveragesRequest request = new CoveragesRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCurrency("Q");

    		if(!FieldAccesor.isEmptyOrNull(configuration.agentCodeAS400)){
    			request.setCodeAgent(configuration.agentCodeAS400);
    		}
    		if(!FieldAccesor.isEmptyOrNull(incident, "creator.profile.agentCode")){
    			request.setCodeAgent(incident.creator.profile.agentCode);
    		}



    		request.setTypeClient((incident.client.isIndividual != null && !incident.client.isIndividual) ? "2" : "5");
    		request.setCoverages(new ArrayList<Coverage>());
    		
    		if(incident.selectedQuotation.quotationDetail.getVehicleValue() != null && incident.selectedQuotation.quotationDetail.getVehicleValue().compareTo(BigDecimal.ZERO) > 0){
    			Coverage coverage = new Coverage();
        		coverage.setCoverage("37");
        		coverage.setMinimun(BigDecimal.ZERO);
        		coverage.setPercDeductible(BigDecimal.ZERO);
        		coverage.setDesctoCoverage(BigDecimal.ZERO);
        		coverage.setDeclarationRate(BigDecimal.ZERO);
        		coverage.setDiscount(BigDecimal.ZERO);
        		coverage.setSumAssured(BigDecimal.ZERO);
        		coverage.setRate(BigDecimal.ZERO);
        		coverage.setPrime(BigDecimal.ZERO);
        		request.getCoverages().add(coverage);
    		}
    		
    		for(ER_Product_Coverage productCoverage: incident.selectedQuotation.product.coverages){
    			if(productCoverage.coverage.transferCode != null && !"".equals(productCoverage.coverage.transferCode.trim())){
    				Coverage coverage = new Coverage();
            		coverage.setCoverage(productCoverage.coverage.transferCode);
            		coverage.setMinimun(productCoverage.minimumPrime);
            		coverage.setPercDeductible(BigDecimal.ZERO);
            		coverage.setDesctoCoverage(BigDecimal.ZERO);
            		coverage.setDeclarationRate(BigDecimal.ZERO);
            		coverage.setDiscount(BigDecimal.ZERO);
            		coverage.setSumAssured(BigDecimal.ZERO);
            		coverage.setRate(BigDecimal.ZERO);
            		coverage.setPrime(BigDecimal.ZERO);
            		if(coverage.getMinimun().compareTo(BigDecimal.ZERO) <= 0){
            			coverage.setMinimun(productCoverage.minimumDeductible);	
            		}
            		
        			for(CoverageCostCategory category: incident.selectedQuotation.quotationDetail.getCategories()){
            			for(CoverageCost cost: category.getCosts()){
            				if(cost.coverageId.equals(productCoverage.coverage.id)){
            					coverage.setMinimun(cost.minimumDeductible);
            					// LoJack
                                if (configuration.totalTheftCoverage.id.equals(productCoverage.coverage.id) && incident.selectedQuotation.getLoJackDeductible() != null) {
                                    coverage.setPercDeductible(incident.selectedQuotation.getLoJackDeductible());
                                } else {
                                    coverage.setPercDeductible(cost.damagesDeductible);
                                }
            					if(productCoverage.coverage.id == 4l || productCoverage.coverage.id == 29){
            						coverage.setSumAssured(cost.coverage.multiply(new BigDecimal(incident.vehicle.numberOfPassengers)).setScale(2, RoundingMode.HALF_UP));
                        		}else{
                        			coverage.setSumAssured(cost.coverage);
                        		}
            					ER_Product_Coverage_Class_Value coverageClassValue = null;
            					for(ER_Product_Coverage_Value coverageValue: productCoverage.values){
            						BigDecimal lowRange = coverageValue.lowRange != null ? coverageValue.lowRange : BigDecimal.ZERO;
            						if(	coverage.getSumAssured() != null && coverage.getSumAssured().compareTo(lowRange) >= 0 &&
            							(coverageValue.highRange == null || coverage.getSumAssured().compareTo(coverageValue.highRange) < 0)){
            							if(!FieldAccesor.isEmptyOrNull(incident, "vehicle.line.vehicleClass.id")){
            								coverageClassValue = coverageValue.getCoverageClassValue(incident.vehicle.line.vehicleClass.id);	
            							}
            						}
                				}

            					BigDecimal totalDiscount = BigDecimal.ZERO;
								totalDiscount = totalDiscount.add(incident.selectedQuotation.quotationDetail.getPrimeDiscount());
								totalDiscount = totalDiscount.add(incident.selectedQuotation.quotationDetail.getDiscount());
            					if(productCoverage.valueBase.code.equals(1)){
            						if(coverageClassValue != null){
            							coverage.setPrime(cost.originalCost);
            							if(productCoverage.coverage.applyDiscount){
	        								BigDecimal discount = coverage.getPrime().multiply(totalDiscount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
	            							coverage.setPrime(coverage.getPrime().subtract(discount));
            							}
            						}
        							if(coverage.getSumAssured() != null && coverage.getSumAssured().compareTo(BigDecimal.ZERO) > 0){
        								coverage.setRate(coverage.getPrime().divide(coverage.getSumAssured(), 15, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
        							}
        						}else if(productCoverage.valueBase.code.equals(3)){
        							if(coverageClassValue != null){
        								coverage.setPrime(cost.originalCost);
        								coverage.setRate(coverageClassValue.value);
        								if(productCoverage.coverage.applyDiscount){
	        								BigDecimal discount = coverage.getPrime().multiply(totalDiscount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
	            							coverage.setPrime(coverage.getPrime().subtract(discount));
            							}
        							}
        						}else if(productCoverage.valueBase.code.equals(2) || productCoverage.valueBase.code.equals(4)){
        							if(coverageClassValue != null){
            							coverage.setRate(coverageClassValue.value);
            						}
        							coverage.setPrime(cost.originalCost);
        							if(productCoverage.coverage.applyDiscount){
        								BigDecimal discount = coverage.getPrime().multiply(totalDiscount).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
            							coverage.setPrime(coverage.getPrime().subtract(discount));
        							}
        						}
            					request.getCoverages().add(coverage);
                			}
                		}
            		}
    			}
    		}
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public PrimeRequest createPrimeRequest(ER_Incident incident, PolicyResponse policy){
    	try{
    		PrimeRequest request = new PrimeRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCurrency("Q");
    		request.setBranch(policy.getBranch());
    		request.setPolicy(policy.getPolicy());
    		request.setFrequencyPayment(incident.selectedPaymentFrecuency.frecuency.code);
    		request.setPrimes(new ArrayList<PrimeRequest.Prime>());
    		
    		Integer numberOfPayments = incident.selectedPaymentFrecuency.numberOfPayments;
    		for(PaymentOption paymentOption: incident.selectedQuotation.quotationDetail.getPaymentOptions()){
    			if(paymentOption.numberOfPayments.equals(numberOfPayments) && paymentOption.frecuency.equals(incident.selectedPaymentFrecuency.frecuency.name)){
    				paymentOption.netPrime = incident.selectedQuotation.quotationDetail.getInternalPrime();
    				for(Integer i=1; i<=numberOfPayments; i++){
    	    			PrimeRequest.Prime prime = new PrimeRequest.Prime();
    	    			prime.setNumberPayment(i);
    	    			prime.setSumAssured(new BigDecimal(0));
    	    			if (incident.selectedQuotation.carValue != null) {
							prime.setSumAssured(incident.selectedQuotation.carValue.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP));
						}
    	    			prime.setNetPrime(paymentOption.netPrime.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP));
						prime.setEmissionRihts(paymentOption.getTotalEmissionFee().divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP));
						prime.setSurcharges(paymentOption.getTotalFractioningFee().divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP));
						prime.setiAttend(paymentOption.coverageExternal.divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP));
						prime.setIva(paymentOption.getIva().divide(new BigDecimal(numberOfPayments), 2, RoundingMode.HALF_UP));
    	    			if(i.equals(1)){
							BigDecimal sumAssured = new BigDecimal(0);
    	    				if (incident.selectedQuotation.carValue != null) {
								sumAssured = incident.selectedQuotation.carValue.subtract(prime.getSumAssured().multiply(new BigDecimal(numberOfPayments)));
							}
    						BigDecimal netPrime = paymentOption.netPrime.subtract(prime.getNetPrime().multiply(new BigDecimal(numberOfPayments)));
    						BigDecimal emission = paymentOption.getTotalEmissionFee().subtract(prime.getEmissionRihts().multiply(new BigDecimal(numberOfPayments)));
    						BigDecimal surcharges = paymentOption.getTotalFractioningFee().subtract(prime.getSurcharges().multiply(new BigDecimal(numberOfPayments)));
    						BigDecimal iAttend = paymentOption.coverageExternal.subtract(prime.getiAttend().multiply(new BigDecimal(numberOfPayments)));
    						BigDecimal iva = paymentOption.getIva().subtract(prime.getIva().multiply(new BigDecimal(numberOfPayments)));

    						prime.setSumAssured(prime.getSumAssured().add(sumAssured).setScale(2, RoundingMode.HALF_UP));
        	    			prime.setNetPrime(prime.getNetPrime().add(netPrime).setScale(2, RoundingMode.HALF_UP));
    						prime.setEmissionRihts(prime.getEmissionRihts().add(emission).setScale(2, RoundingMode.HALF_UP));
    						prime.setSurcharges(prime.getSurcharges().add(surcharges).setScale(2, RoundingMode.HALF_UP));
    						prime.setiAttend(prime.getiAttend().add(iAttend).setScale(2, RoundingMode.HALF_UP));
    						prime.setIva(prime.getIva().add(iva).setScale(2, RoundingMode.HALF_UP));
    	    			}
    	    			request.getPrimes().add(prime);
    	    		}
    			}
    		}
    		
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public PaymentMethodRequest createPaymentMethodRequest(ER_Incident incident){
    	try{
    		PaymentMethodRequest request = new PaymentMethodRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCurrency("Q");
    		request.setPaymentMethod(incident.payment.chargeType.transferCode);
    		if(incident.payment.chargeType.id == 2){
    			request.setCardType(incident.payment.bankAccountType.transferCode);
    			request.setBankAccountType(incident.payment.bank.transferCode);
    		}else if(incident.payment.chargeType.id == 3){
    			request.setCardType(incident.payment.cardType.transferCode);
    			request.setCardClass(incident.payment.cardClass.transferCode);
    			request.setCardCode(incident.payment.cardClass.transferCode2);
    		}
    		request.setCardNumber(incident.payment.numberAccountCard);
    		
    		if(incident.payment.expirationDate != null){
    			request.setCardExpirationDate("01/" + DateHelper.formatDate(incident.payment.expirationDate, "MM/yyyy"));	
    		}
    		request.setNumberInstallments(null);
    		
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public YoungerRequest createYoungerRequest(ER_Incident incident){
    	try{
    		YoungerRequest request = new YoungerRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCodeCoverage(null);
    		request.setName(null);
    		request.setAddress(null);
    		request.setBirthdate(null);
    		request.setAge(null);
    		request.setTypeLicense(null);
    		request.setLicenseNumber(null);
    		request.setExpirationDate(null);
    		request.setExpirationDateFrom(null);

	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
    
    public WorkFlowRequest createWorkFlowRequest(ER_Incident incident){
    	try{
    		String date = DateHelper.formatDate(incident.selectedQuotation.creationDate, "dd/MM/yyyy");
    		WorkFlowRequest request = new WorkFlowRequest();
    		request.setQuoteNumber(incident.number.toString());
    		request.setCurrency("Q");
    		request.setMovementType("01");
    		request.setSubMovementType("01");
    		request.setDocumentType("01");
    		request.setDocumentDate(date);
    		request.setReceptionDate(date);
    		request.setInsuredName(null);
    		request.setObservations(null);
    		request.setUrgent("N");
    		request.setLine((incident.client.isIndividual != null && !incident.client.isIndividual) ? "02" : "01");
    		request.setReviewDate(date);
    		request.setDateAssignment(date);
    		request.setEmissionDate(date);
    		request.setBarcode(null);
    		
	    	return request;
		}catch(Exception e){
			e.printStackTrace();
		}
    	return null;
    }
}