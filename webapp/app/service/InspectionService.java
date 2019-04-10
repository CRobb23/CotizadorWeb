package service;

import models.ws.rest.*;

public interface InspectionService {

	InspectionResponse createInspection(Inspection request);
	
	InspectionResponse finishInspection(String body);

	InspectionResponse createInspectionBroker(Inspection request);

	InspectionBrokerResponse listExternalBrokers();

	InspectionAutoResponse finishAutoInspection(String body);

	InspectionAutoResponse createAutoInspection(InspectionAutoRequest request);

}