package service.implementation;

import java.math.BigDecimal;
import java.util.Date;

import models.*;
import models.ws.rest.*;

import com.google.gson.Gson;
import jobs.SendInspectionJob;
import helpers.ERConstants;
import notifiers.Mails;
import play.Logger;
import play.Play;
import play.libs.WS;
import play.libs.WS.WSRequest;
import play.modules.guice.InjectSupport;
import service.InspectionService;
import service.JsonService;
import utils.StringUtil;

import javax.inject.Inject;


@InjectSupport
public class InspectionServiceImpl extends ExternalJsonAbstractService implements InspectionService{
	
	private final String WS_INSPECTION = Play.configuration.getProperty("inspection.app");
	private final String WS_INSPECTION_BROKERS = Play.configuration.getProperty("inspection.brokers.app");
	private final String CONNECTION_ERROR = "Ha ocurrido un error en la conexión.";

    private InspectionAutoRequest autoRequest;
    private InspectionAutoResponse autoResponse;

    @Inject
    public InspectionServiceImpl(JsonService jsonService) {
        super(jsonService);
    }

    public InspectionResponse createInspection(Inspection request){
		InspectionResponse inspectionResponse = new InspectionResponse();
        try{
        	InspectionRequest inspectionRequest = new InspectionRequest();
        	inspectionRequest.setData(request);
        	
        	Logger.info("createInspection REST REQUEST:\n\n" + new Gson().toJson(inspectionRequest) + "\n");
        	
        	WSRequest wsRequest = WS.url(WS_INSPECTION + "/createQuoteInspection");
        	wsRequest.setHeader("Content-Type", "application/json");
        	wsRequest.body(new Gson().toJson(inspectionRequest));
        	inspectionResponse = new Gson().fromJson(wsRequest.post().getString(), InspectionResponse.class);

            Logger.info("createInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
        	
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

        Logger.info("finishInspection REST REQUEST:\n\n" + body + "\n");
		 
		 InspectionResponse inspectionResponse = new InspectionResponse();
		 try{
			 Object object = new Gson().fromJson(body, InspectionFinishRequest.class);
			 if(object != null){
				 InspectionFinishRequest inspectionFinishRequest = (InspectionFinishRequest) object;
				 ER_Incident incident = ER_Incident.find("number = ?", inspectionFinishRequest.getQuotationNumber()).first();
				 if(incident != null && incident.inspection != null && incident.inspection.id != null) {
                     ER_Inspection inspection = ER_Inspection.findById(incident.inspection.id);
                     if (inspection.type.code != ERConstants.INSPECTION_TYPE_ADDRESS && inspection.type.code != ERConstants.INSPECTION_TYPE_CENTER) {
                         inspectionResponse.setSuccess(false);
                         inspectionResponse.setMessage("La cotización no tiene el tipo de inspección solicitada.");
                         return inspectionResponse;
                     }
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

                            Logger.info("Datos Comunes desde app" + CommonData);
                            Logger.info("Datos Comunes de cotizador" + incident.vehicle.getFullPlate() + ";" + incident.vehicle.engine + ";" + incident.vehicle.chassis + ";" + incident.vehicle.line.name + ";" + incident.vehicle.line.brand.name);
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
                            Logger.info("Datos Comunes desde app" + CommonData);
                            Logger.info("Datos Comunes de cotizador" + incident.vehicle.getFullPlate() + ";" + incident.vehicle.engine + ";" + incident.vehicle.chassis);
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
					 SendInspectionJob sendInspectionJob = new SendInspectionJob(incident);
					 sendInspectionJob.now();
					 //Mails.finishInspection(incident);
					 
					 inspectionResponse.setSuccess(true);
					 inspectionResponse.setMessage("Satisfactorio");
				 }else{
					 inspectionResponse.setSuccess(false);
					 inspectionResponse.setMessage("La cotización no tiene una inspección asociada.");
				 }

                 Logger.info("finishInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
				 
				 return inspectionResponse;
			 }
		 }catch(Exception e){
			 e.printStackTrace();			
		 }
		
		 inspectionResponse.setSuccess(false);
		 inspectionResponse.setMessage("Ha ocurrido un error al actualizar la inspección.");

        Logger.info("finishInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
		 
		 return inspectionResponse;
	 }

	public InspectionResponse createInspectionBroker(Inspection request){
		InspectionResponse inspectionResponse = new InspectionResponse();
		try{
			InspectionRequest inspectionRequest = new InspectionRequest();
			inspectionRequest.setData(request);

            Logger.info("createInspection Brokers REST REQUEST:\n\n" + new Gson().toJson(inspectionRequest) + "\n");
			WSRequest wsRequest = WS.url(WS_INSPECTION_BROKERS + "/createQuoteInspection");
			wsRequest.setHeader("Content-Type", "application/json");
			wsRequest.body(new Gson().toJson(inspectionRequest));
			inspectionResponse = new Gson().fromJson(wsRequest.post().getString(), InspectionResponse.class);
            Logger.info("createInspection  Brokers REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
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
            Logger.info("list external Brokers REST REQUEST:");
			WSRequest wsRequest = WS.url(WS_INSPECTION_BROKERS + "/listAllBrokers");
			wsRequest.setHeader("Content-Type", "application/json");
			inspectionResponse = new Gson().fromJson(wsRequest.post().getString(), InspectionBrokerResponse.class);
            Logger.info("createInspection  Brokers REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
			return inspectionResponse;
		}catch(Exception e){
			e.printStackTrace();
		}

		inspectionResponse = new InspectionBrokerResponse();
		inspectionResponse.setSuccess(false);
		inspectionResponse.setMessage(CONNECTION_ERROR);
		return inspectionResponse;
	}

    @Override
    public InspectionAutoResponse finishAutoInspection(String body) {
        Logger.info("finishInspection REST REQUEST:\n\n" + body + "\n");

        InspectionAutoResponse inspectionResponse = new InspectionAutoResponse();
        try{
            InspectionAutoFinishRequest inspectionFinishRequest = new Gson().fromJson(body, InspectionAutoFinishRequest.class);
            ER_Incident incident = ER_Incident.find("inspection.inspectionNumber = ?", inspectionFinishRequest.getInspectionNumber()).first();
            if(incident != null ) {
                ER_Inspection inspection = incident.inspection;
                if (inspection.type.code != ERConstants.INSPECTION_TYPE_AUTO) {
                    inspectionResponse.setSuccess(false);
                    inspectionResponse.setMessage("La cotización no tiene el tipo de inspección solicitada.");
                    return inspectionResponse;
                }
                inspection.inspected = true;
                inspection.inspectionDate = new Date();
                incident.inspection = inspection.save();

                Integer inspectionStatus = inspectionFinishRequest.getStatus();
                inspection.insurable = "ASEGURABLE";
                if (inspectionStatus == 2 || inspectionStatus == 4 || inspectionStatus == 5) {
                    incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
                } else if (inspectionStatus == 3) {
                    incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_ANULLED).first();
                }
                incident.save();
                SendInspectionJob sendInspection = new SendInspectionJob(incident);
                sendInspection.now();
                //Mails.finishInspection(incident);
                inspectionResponse.setSuccess(true);
                inspectionResponse.setMessage("SATISFACTORIO");
            } else {
                inspectionResponse.setSuccess(false);
                inspectionResponse.setMessage("La cotización no tiene una inspección asociada.");
            }

            Logger.info("finishInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");

            return inspectionResponse;
        } catch(Exception e) {
            Logger.error(e, e.getMessage());
        }
        inspectionResponse.setSuccess(false);
        inspectionResponse.setMessage("Ha ocurrido un error al actualizar la inspección.");
        Logger.info("finishInspection REST RESPONSE:\n\n" + new Gson().toJson(inspectionResponse) + "\n");
        return inspectionResponse;
    }

    @Override
    public InspectionAutoResponse createAutoInspection(InspectionAutoRequest request) {
        this.autoRequest = request;
        callServiceBus();
        return autoResponse;
    }

    @Override
    protected String getEndpoint() {
        return "/inspection/auto";
    }

    @Override
    protected Object getReqObject() {
        return autoRequest;
    }

    @Override
    protected void setResObject(Object object) {
        this.autoResponse = (InspectionAutoResponse) object;
    }

    @Override
    protected Class getResClass() {
        return InspectionAutoResponse.class;
    }

}