package service.implementation;

import java.math.BigDecimal;
import java.util.Date;

import models.*;
import models.ws.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import helpers.ERConstants;
import notifiers.Mails;
import play.Play;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.modules.guice.InjectSupport;
import service.InspectionService;
import utils.StringUtil;

@InjectSupport
public class InspectionServiceImpl implements InspectionService{
	
	private final Logger LOG = LoggerFactory.getLogger(InspectionService.class);
	private final String WS_INSPECTION = Play.configuration.getProperty("inspection.app");
	private final String WS_INSPECTION_BROKERS = Play.configuration.getProperty("inspection.brokers.app");
	private final String CONNECTION_ERROR = "Ha ocurrido un error en la conexión.";

	public InspectionResponse createInspection(Inspection request){
		InspectionResponse inspectionResponse = new InspectionResponse();
        try{
        	InspectionRequest inspectionRequest = new InspectionRequest();
        	inspectionRequest.setData(request);
        	
        	LOG.info("createInspection REST REQUEST:\n\n" + new Gson().toJson(inspectionRequest) + "\n");
        	
        	WSRequest wsRequest = WS.url(WS_INSPECTION + "/createQuoteInspection");
        	wsRequest.setHeader("Content-Type", "application/json");
        	wsRequest.body(new Gson().toJson(inspectionRequest));
        	inspectionResponse = new Gson().fromJson(wsRequest.post().getString(), InspectionResponse.class);
        	
        	LOG.info("createInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
        	
        	return inspectionResponse;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        inspectionResponse = new InspectionResponse();
        inspectionResponse.setSuccess(false);
		inspectionResponse.setMessage(CONNECTION_ERROR);
        return inspectionResponse;
    }
	
	public InspectionResponse 	finishInspection(String body){
		 
		 LOG.info("finishInspection REST REQUEST:\n\n" + body + "\n");
		 
		 InspectionResponse inspectionResponse = new InspectionResponse();
		 try{
			 Object object = new Gson().fromJson(body, InspectionFinishRequest.class);
			 if(object != null){
				 InspectionFinishRequest inspectionFinishRequest = (InspectionFinishRequest) object;
				 ER_Incident incident = ER_Incident.find("number = ?", inspectionFinishRequest.getQuotationNumber()).first();
				 if(incident != null && incident.inspection != null && incident.inspection.id != null) {
                     ER_Inspection inspection = ER_Inspection.findById(incident.inspection.id);
                     inspection.inspected = true;
                     inspection.inspectionNumber = inspectionFinishRequest.getInspectionNumber();
                     inspection.existentDamage = inspectionFinishRequest.getExistentDamage();
                     inspection.soundEquipment = inspectionFinishRequest.getSoundEquipment();
					 inspection.commonData = inspectionFinishRequest.getCommonData();
                     inspection.inspectionDate = new Date();
                     incident.inspection = inspection.save();

                    if (!StringUtil.isNullOrBlank(inspectionFinishRequest.getInsurable())) {
                        inspection.insurable = inspectionFinishRequest.getInsurable();
                        if (inspectionFinishRequest.getInsurable().equals("ASEGURABLE")) {
                            boolean bolCorrect=true;
                        	//Checks data
							//0:Placa,1:Motor,2:Chasis,3:Linea,4:Marca,
							String CommonData = inspectionFinishRequest.getCommonData();
                            ER_Incident_Status incidentStatusIncomplete = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INCOMPLETE).first();
							if (!CommonData.split(";")[0].equals(incident.vehicle.plate)){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app no coinciden con datos de cotizador: Datos de app Placa: " + CommonData.split(";")[0];
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
							}
							if (!CommonData.split(";")[1].equals(incident.vehicle.engine)){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app no coinciden con datos de cotizador: Datos de app Motor: " + CommonData.split(";")[1];
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
							}
							if (!CommonData.split(";")[2].equals(incident.vehicle.chassis)){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app no coinciden con datos de cotizador: Dato de app: Chasis: " + CommonData.split(";")[2];
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
							}

                            LOG.info("Datos Comunes desde app" + CommonData);
                            LOG.info("Datos Comunes de cotizador" + incident.vehicle.getFullPlate() + ";" + incident.vehicle.engine + ";" + incident.vehicle.chassis + ";" + incident.vehicle.line.name + ";" + incident.vehicle.line.brand.name);
                            if(bolCorrect) {
                                incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
                            }
                        } else if (inspectionFinishRequest.getInsurable().equals("NO_ASEGURABLE")) {
                            incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_ANULLED).first();
                        } else if (inspectionFinishRequest.getInsurable().equals("PENDIENTE")) {
                            incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION).first();
                        } else if (inspectionFinishRequest.getInsurable().equals("RESPONSABILIDAD_CIVIL")) {
                            boolean bolCorrect=true;
                            ER_Incident_Status incidentStatusIncomplete = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INCOMPLETE).first();
                            ER_Quotation quotation = ER_Quotation.findById(incident.selectedQuotation.id);
                            if(quotation.carValue.compareTo(BigDecimal.ZERO) != 0){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app indican que cotización es únicamente RC. ";
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
                            }
                            //Checks data
                            //0:Placa,1:Motor,2:Chasis,3:Linea,4:Marca,
                            String CommonData = inspectionFinishRequest.getCommonData();

                            if (!CommonData.split(";")[0].equals(incident.vehicle.plate)){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app no coinciden con datos de cotizador: Datos de app Placa: " + CommonData.split(";")[0];
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
                            }
                            if (!CommonData.split(";")[1].equals(incident.vehicle.engine)){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app no coinciden con datos de cotizador: Datos de app Motor: " + CommonData.split(";")[1];
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
                            }
                            if (!CommonData.split(";")[2].equals(incident.vehicle.chassis)){
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Error: Datos de app no coinciden con datos de cotizador: Dato de app: Chasis: " + CommonData.split(";")[2];
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = incident.selectedQuotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                bolCorrect = false;
                            }
                            LOG.info("Datos Comunes desde app" + CommonData);
                            LOG.info("Datos Comunes de cotizador" + incident.vehicle.getFullPlate() + ";" + incident.vehicle.engine + ";" + incident.vehicle.chassis);
                            if(bolCorrect) {
                                incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
                            }
                        } else {
                            incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
                        }
                    }
                    else {
                        incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
                    }
					 incident.save();
					 
					 Mails.finishInspection(incident);
					 
					 inspectionResponse.setSuccess(true);
					 inspectionResponse.setMessage("Satisfactorio");
				 }else{
					 inspectionResponse.setSuccess(false);
					 inspectionResponse.setMessage("La cotización no tiene una inspección asociada.");
				 }
				 
				 LOG.info("finishInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
				 
				 return inspectionResponse;
			 }
		 }catch(Exception e){
			 e.printStackTrace();			
		 }
		
		 inspectionResponse.setSuccess(false);
		 inspectionResponse.setMessage("Ha ocurrido un error al actualizar la inspección.");
		 
		 LOG.info("finishInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
		 
		 return inspectionResponse;
	 }

	public InspectionResponse createInspectionBroker(Inspection request){
		InspectionResponse inspectionResponse = new InspectionResponse();
		try{
			InspectionRequest inspectionRequest = new InspectionRequest();
			inspectionRequest.setData(request);

			LOG.info("createInspection Brokers REST REQUEST:\n\n" + new Gson().toJson(inspectionRequest) + "\n");
			WSRequest wsRequest = WS.url(WS_INSPECTION_BROKERS + "/createQuoteInspection");
			wsRequest.setHeader("Content-Type", "application/json");
			wsRequest.body(new Gson().toJson(inspectionRequest));
			inspectionResponse = new Gson().fromJson(wsRequest.post().getString(), InspectionResponse.class);
			LOG.info("createInspection  Brokers REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
			return inspectionResponse;
		}catch(Exception e){
			e.printStackTrace();
		}

		inspectionResponse = new InspectionResponse();
		inspectionResponse.setSuccess(false);
		inspectionResponse.setMessage(CONNECTION_ERROR);
		return inspectionResponse;
	}

	public InspectionBrokerResponse listExternalBrokers(){
		InspectionBrokerResponse inspectionResponse = new InspectionBrokerResponse();
		try{
			LOG.info("list external Brokers REST REQUEST:");
			WSRequest wsRequest = WS.url(WS_INSPECTION_BROKERS + "/listAllBrokers");
			wsRequest.setHeader("Content-Type", "application/json");
			inspectionResponse = new Gson().fromJson(wsRequest.post().getString(), InspectionBrokerResponse.class);
			LOG.info("createInspection  Brokers REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
			return inspectionResponse;
		}catch(Exception e){
			e.printStackTrace();
		}

		inspectionResponse = new InspectionBrokerResponse();
		inspectionResponse.setSuccess(false);
		inspectionResponse.setMessage(CONNECTION_ERROR);
		return inspectionResponse;
	}
	
}