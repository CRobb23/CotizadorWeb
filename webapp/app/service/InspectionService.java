package service;

import models.ws.rest.Inspection;
import models.ws.rest.InspectionBrokerResponse;
import models.ws.rest.InspectionResponse;

public interface InspectionService {

	InspectionResponse createInspection(Inspection request);
	
	InspectionResponse finishInspection(String body);

	InspectionResponse createInspectionBroker(Inspection request);

	InspectionBrokerResponse listExternalBrokers();

}