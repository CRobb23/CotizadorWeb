package controllers;

import javax.inject.Inject;

import com.google.gson.Gson;

import models.ws.rest.InspectionRequest;
import models.ws.rest.InspectionResponse;
import play.mvc.Controller;
import service.InspectionService;

public class InspectionsController extends Controller {
	
	@Inject
	static InspectionService inspectionService;
	
	public static void finishInspection(String body){
		InspectionResponse inspectionResponse = inspectionService.finishInspection(body);
		
		renderJSON(inspectionResponse);
	 }

	public static void createQuoteInspection(String body){
		 System.out.println("finishInspection REST REQUEST:\n\n" + body + "\n");
		 
		 Object object = new Gson().fromJson(body, InspectionRequest.class);
		 if(object != null){
			 InspectionRequest inspectionRequest = (InspectionRequest) object;
			 System.out.println(new Gson().toJson(inspectionRequest));
		 }
		 
		 InspectionResponse inspectionResponse = new InspectionResponse();
		 inspectionResponse.setSuccess(true);
		 inspectionResponse.setMessage("Satisfactorio");
		 renderJSON(inspectionResponse);
	 }
}