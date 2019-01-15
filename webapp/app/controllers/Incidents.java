package controllers;

import static play.modules.pdf.PDF.renderPDF;
import static play.modules.pdf.PDF.writePDF;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.inject.Inject;

import models.*;
import models.dto.BusinessDetailDTO;
import models.dto.PersonDetailDTO;
import models.ws.*;
import models.ws.rest.InspectionAutoRequest;
import models.ws.rest.InspectionAutoResponse;
import objects.LoJackOptions;
import org.allcolor.yahp.converter.IHtmlToPdfTransformer;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.DateConverter;

import extensions.StringFormatExtensions;
import helpers.DateHelper;
import helpers.ERConstants;
import helpers.FieldAccesor;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import helpers.QuotationHelper;
import helpers.ReminderHelper;
import models.ws.rest.Inspection;
import models.ws.rest.InspectionResponse;
import notifiers.Mails;
import objects.ER_Quotation_Parameter;
import play.Logger;
import play.Play;
import play.data.binding.As;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel.JPAQuery;
import play.i18n.Messages;
import play.modules.paginate.ValuePaginator;
import play.modules.pdf.PDF;
import play.modules.pdf.PDF.Options;
import play.mvc.With;
import service.*;
import utils.StringUtil;

@With(Secure.class)
public class Incidents extends AdminBaseController {

	@Inject
	static PolicyService policyService;
	@Inject
	static CreateRequestService createRequestService;
	@Inject
	static InspectionService inspectionService;
	@Inject
	static ClientsQueryWebService clientQueryServiceBus;
	@Inject
	static AverageValueQueryWebService averageValueServiceBus;
	@Inject
	static PersonQueryWebService personServiceBus;
	@Inject
	static BusinessQueryWebService businessServiceBus;
	@Inject
	static PolicyInputWebService policyServiceBus;

	public static Long dailyCorrelativeNumber;
	public static Map<String,String> searchFields;
	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterIncidents(String searchField,String multipleSearch,Map<String,String> searchFields) {
		try{
		//Validate the parameters
		boolean valid = GeneralMethods.validateParameter(searchField);
		valid = valid | GeneralMethods.validateParameter(multipleSearch);
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();

		if(valid){
			if(multipleSearch.equals("true")){
				if(connectedUser().role.code == ERConstants.USER_ROLE_FINAL_USER){
					filter.addQuery("creator.id = ?", connectedUser().id, Operator.AND);
				}
				if(!searchFields.get("number_case").equals("")) {
					if(searchFields.get("number_case").matches("[0-9]+")){
						Long number = Long.valueOf(searchFields.get("number_case"));
						filter.addQuery("number = ?", number, Operator.AND);
					}					
				}
				//search number_policy
                if(!searchFields.get("number_policy").equals("")) {
                    String policy = ("%" + searchFields.get("number_policy") + "%").trim();
                    filter.addQuery("lower(policy) like ?", policy, Operator.AND);

                }
				if(!searchFields.get("client_name").equals("")) {
					String name = ("%" + searchFields.get("client_name") + "%").toLowerCase();
					filter.addGroupStart(Operator.AND);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.secondName),''),coalesce(concat(' ',client.firstSurname),''),coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.firstSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.secondName),''),coalesce(concat(' ',client.firstSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.secondName,coalesce(concat(' ',client.firstSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.secondName,coalesce(concat(' ',client.firstSurname),''),coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.secondName,coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(client.companyName) like ?", name, Operator.OR);
					filter.addQuery("lower(client.businessName) like ?", name, Operator.OR);
					filter.addGroupEnd();
				}
				if(!searchFields.get("vehicle_plate").equals("")) {
					if(searchFields.get("vehicle_plate").indexOf("-") > 0){
						String[] plateType = searchFields.get("vehicle_plate").split("-");
						filter.addQuery("lower(vehicle.plateType.transferCode) like ?", "%" + plateType[0] + "%", Operator.AND);
						filter.addQuery("lower(vehicle.plate) like ?", "%" + plateType[1] + "%", Operator.AND);
					}else{
						filter.addQuery("lower(vehicle.plate) like ?", "%" + searchFields.get("vehicle_plate").toLowerCase() + "%", Operator.AND);
					}
				}
				if(!searchFields.get("vehicle_line").equals("")) {
					filter.addQuery("lower(vehicle.line.name) like ?", "%" + searchFields.get("vehicle_line").toLowerCase() + "%", Operator.AND);
				}
				if(!searchFields.get("vehicle_brand").equals("")) {
					filter.addQuery("lower(vehicle.line.brand.name) like ?", "%" + searchFields.get("vehicle_brand").toLowerCase() + "%", Operator.AND);
				}
				if(!searchFields.get("vehicle_year").equals("")) {
					try{
						filter.addQuery("vehicle.erYear.year = ?", searchFields.get("vehicle_year"), Operator.AND);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				if(!FieldAccesor.isEmptyOrNull(searchFields.get("case_status"))) {
					try {
						Long status = Long.parseLong(searchFields.get("case_status"));
						filter.addQuery("status.id = ?",status,Operator.AND);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
				if(!searchFields.get("incident_date").equals("")) {
					filter.addQuery("convert(nvarchar(10), creationDate, 103) = ?", searchFields.get("incident_date") , Operator.AND);
				}
				
			}else{
				searchField = searchField.trim();
				if(connectedUser().role.code == ERConstants.USER_ROLE_FINAL_USER){
					filter.addQuery("creator.id = ?", connectedUser().id, Operator.AND);
				}				
				if(searchField.matches("[0-9]+")){
					Long number = Long.valueOf(searchField);
					filter.addQuery("number = ?", number, Operator.AND);
				}else{
					String name = ("%" + searchField + "%").toLowerCase();
					filter.addGroupStart(Operator.AND);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.secondName),''),coalesce(concat(' ',client.firstSurname),''),coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.firstSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.secondName),''),coalesce(concat(' ',client.firstSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.secondName,coalesce(concat(' ',client.firstSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.secondName,coalesce(concat(' ',client.firstSurname),''),coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.secondName,coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(concat(client.firstName,coalesce(concat(' ',client.secondSurname),''))) like ?", name, Operator.OR);
					filter.addQuery("lower(client.companyName) like ?", name, Operator.OR);
					filter.addQuery("lower(client.businessName) like ?", name, Operator.OR);
					if(searchField.indexOf("-") > 0){
						String[] plateType = searchField.split("-");
						filter.addQuery("lower(vehicle.plateType.transferCode) like ?", "%" + plateType[0] + "%", Operator.OR);
						filter.addQuery("lower(vehicle.plate) like ?", "%" + plateType[1] + "%", Operator.OR);
					}else{
						filter.addQuery("lower(vehicle.plate) like ?", "%" + searchField.toLowerCase() + "%", Operator.OR);	
					}
					filter.addGroupEnd();
				}
				
			}
		}
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		if(filter.isValidFilter() ||  (multipleSearch != null && multipleSearch.equals("true"))){
			ER_User connectedUser = connectedUser();
			JPAQuery query = null;
				if(checkRole(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)){
					List<Long> channelIds = null;
					channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();

				String queryStr = " (";
				if (channelIds != null && !channelIds.isEmpty()) {
					queryStr = queryStr + " channel.id IN :c OR ";
				}
				queryStr = queryStr + " creator = :s )";
				if (!filter.getQuery().isEmpty()) {
					queryStr = "AND" + queryStr;
					query = ER_Incident.find(filter.getQuery() + queryStr + " order by creationDate DESC", filter.getParametersArray());
				}
				else {
					query = ER_Incident.find(queryStr + " order by creationDate DESC", filter.getParametersArray());
				}
				if (channelIds != null && !channelIds.isEmpty()) {
					query.bind("c", channelIds);
				}
				query.bind("s", connectedUser);
			}else if(checkRole(ERConstants.USER_ROLE_CHANNEL_MANAGER)){
				List<Long> distributorIds = null;
				distributorIds = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d.active = true", connectedUser).fetch();
				String queryStr = " (";
				if (distributorIds != null && !distributorIds.isEmpty()) {
					queryStr = queryStr + " distributor.id IN :d OR ";
				}
				queryStr = queryStr + " creator = :s )";
				if (!filter.getQuery().isEmpty()) {
					queryStr = "AND " + queryStr;
					query = ER_Incident.find(filter.getQuery() + queryStr + "  order by creationDate DESC", filter.getParametersArray());
				}else {
					query = ER_Incident.find( queryStr + "  order by creationDate DESC", filter.getParametersArray());
				}
				if (distributorIds != null && !distributorIds.isEmpty()) {
					query.bind("d", distributorIds);
				}
				query.bind("s", connectedUser);
			}else if(checkRole(ERConstants.USER_ROLE_SUPERVISOR)){
				List<Long> userIds = ER_Store.find("select u.id from ER_Store s join s.sellers u  join s.administrators a where a = ?", connectedUser).fetch();
				userIds.add(connectedUser.id);
				if (!filter.getQuery().isEmpty())
				query = ER_Incident.find(filter.getQuery() + " AND creator.id IN :s order by creationDate DESC", filter.getParametersArray()).bind("s", userIds);
				else
					query = ER_Incident.find(" creator.id IN :s order by creationDate DESC", filter.getParametersArray()).bind("s", userIds);
			}else if(checkRole(ERConstants.USER_ROLE_SALES_MAN)){
                if (!filter.getQuery().isEmpty())
				query = ER_Incident.find(filter.getQuery() + " AND creator = :s order by creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
                else
                    query = ER_Incident.find( " creator = :s order by creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
			}else{
				query = ER_Incident.find(filter.getQuery() + " order by creationDate DESC", filter.getParametersArray());
			}
			
			List<ER_Incident> incidentsList = query.fetch();
			ValuePaginator incidents = new ValuePaginator(incidentsList);
			incidents.setPageSize(10);
			renderArgs.put("incidents", incidents);
		}
		}
		catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
	}


	}
	
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void incidentsList(String searchField, Boolean search) {
		try{
    	if(search==null||search==false){
    		params.flash();
    		searchFields = new HashMap<String,String>();
    	}else{
    		renderArgs.put("multipleSearch", searchFields.get("multiple"));
    		renderArgs.put("number_case", searchFields.get("number_case"));
    		renderArgs.put("number_policy", searchFields.get("number_policy"));
    		renderArgs.put("client_name", searchFields.get("client_name"));
    		renderArgs.put("vehicle_plate", searchFields.get("vehicle_plate"));
    		renderArgs.put("vehicle_line", searchFields.get("vehicle_line"));
    		renderArgs.put("vehicle_brand", searchFields.get("vehicle_brand"));
    		renderArgs.put("vehicle_year", searchFields.get("vehicle_year"));
    		renderArgs.put("case_status", searchFields.get("case_status"));
    		renderArgs.put("incident_date", searchFields.get("incident_date"));
    		renderArgs.put("searchField",searchField);
    	}
    	String multipleSearch = searchFields.get("multiple");
    	filterIncidents(searchField, multipleSearch, searchFields);
    	
    	ValuePaginator list = (ValuePaginator) renderArgs.get("incidents");
    	
    	List<ER_Incident_Status> statuses = ER_Incident_Status.find("order by code").fetch();
    	renderArgs.put("statuses", statuses);
    	
    	if(search!=null && search && (list==null || list.size()==0)){
    		flash.error(Messages.get("incidents.list.noentries"));
    	}
    	
        render();}
		catch(Exception e){
			Logger.error("error: " + e.getMessage());
			e.printStackTrace();
		}
    }

	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void searchIncidents(String searchField,String multipleSearch) {
		try{
			flash.clear();
			if (!GeneralMethods.validateParameter(searchField)&&!GeneralMethods.validateParameter(multipleSearch)) {
					flash.error(Messages.get("incidents.list.searcherror"));

				incidentsList(null, false);
			} else {

				searchFields = new HashMap<String,String>();
				searchFields.put("number_case", params.get("number_case"));
				searchFields.put("number_policy", params.get("number_policy"));
				searchFields.put("client_name", params.get("client_name"));
				searchFields.put("vehicle_plate", params.get("vehicle_plate"));
				searchFields.put("vehicle_brand", params.get("vehicle_brand"));
				searchFields.put("vehicle_line", params.get("vehicle_line"));
				searchFields.put("vehicle_year", params.get("vehicle_year"));
				searchFields.put("case_status", params.get("case_status"));
				searchFields.put("incident_date", params.get("incident_date"));
				searchFields.put("multiple", String.valueOf(GeneralMethods.validateParameter(multipleSearch)));

				incidentsList(searchField, true);
			}
		}
		catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
		}
    }
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static void incidentDelete(Long id) {
    	ER_Incident incident = ER_Incident.findById(id);
    	incident.delete();
    	incidentsList(null, false);
    }
    /*
	 * ************************************************************************************************************************
	 * Incident detail
	 * ************************************************************************************************************************
	 */
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void incidentDetail(Long id) {
    	try{
    	if (id!=null) {
	    	ER_Incident incident = ER_Incident.findById(id);
	    	
	    	if (canViewIncident(incident)) {
	    		List<ER_Task> tasks = ER_Task.find("incident = ? and status.code != ? order by dueDate asc", incident, ERConstants.TASK_STATUS_COMPLETE).fetch();
	    	
	    		boolean isOwner = (incident.creator == connectedUser());
	    		boolean isQAUser = false;
	    		if(connectedUser().isQAUser != null && connectedUser().isQAUser)
					isQAUser = true;

	    		if (!isOwner) {
					switch (connectedUserRoleCode()) {
						case ERConstants.USER_ROLE_SUPER_ADMIN: {
							isOwner = true;
							break;
						}
						case ERConstants.USER_ROLE_COMMERCIAL_MANAGER: {
							List<Long> channels = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c = ? and c.active = true", connectedUser(), incident.channel).fetch();
							isOwner = !channels.isEmpty();
							break;
						}
						case ERConstants.USER_ROLE_CHANNEL_MANAGER: {
							List<Long> distributors = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d = ? and d.active=true", connectedUser(), incident.distributor).fetch();
							isOwner = !distributors.isEmpty();
							break;
						}
						case ERConstants.USER_ROLE_SUPERVISOR: {
							List<ER_Store> stores = ER_Store.find("select s from ER_Store s join s.sellers u join s.administrators a where u = ? and a = ? and  s.active = true", incident.creator, connectedUser()).fetch();
							isOwner = !stores.isEmpty();
							break;
						}
					}
				}
				if(incident.creator.role.code == ERConstants.USER_ROLE_FINAL_USER){
	    			isOwner = true;
	    		}
	    		ER_Admin_Messages messages = ER_Admin_Messages.findById(1L);
				String body = messages.body;
	    		render(incident, tasks, isOwner,body,isQAUser);
	    	} 
    	}
    	
    	incidentsList(null, null);
		}
		catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
	}
    	
    }

	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void reviewCase(Long id) {
		if(id != null) {
			ER_Incident incident = ER_Incident.findById(id);
			if(canViewIncident(incident)){
				boolean isQAUser = false;
				if(connectedUser().isQAUser != null && connectedUser().isQAUser)
					isQAUser = true;

				List <ER_Incident_Comments> comments = ER_Incident_Comments.find("incident_id = ?" , incident).fetch();
				render(incident, isQAUser, comments);
			}
		}
	}
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
	public static void saveQAComments(String comments, Long id,Boolean isAccepted){
		flash.clear();
		flash.discard();


		try {
			ER_Incident currentIncident = ER_Incident.findById(id);
			if(currentIncident.reviewAccepted == null) {
                currentIncident.review = comments;
                currentIncident.reviewUser = connectedUser();
                currentIncident.reviewDate = new Date();
                currentIncident.reviewAccepted = isAccepted;
                currentIncident.save();
            }
            else{
                ER_Incident_Comments comment = new ER_Incident_Comments();
                comment.comment = comments;
                comment.incident = currentIncident;
                comment.reviewDate = new Date();
                comment.user = connectedUser();
                comment.save();
            }
			finalized(currentIncident.id, true);
		}
		catch (Exception e) {
			Logger.error(e, "Error updating incident");
		}


	}
    
    /*
	 * ************************************************************************************************************************
	 * Incident attention
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void attendIncident(Long id) {
		try{
    	if(id != null){
    		ER_Incident incident = ER_Incident.findById(id);
    		if((incident != null && canViewIncident(incident)) || incident.creator.role.code == ERConstants.USER_ROLE_FINAL_USER) {
    			if( incident.status.code == ERConstants.INCIDENT_STATUS_CREATED ||
    				incident.status.code == ERConstants.INCIDENT_STATUS_IN_PROGRESS ||
    				incident.status.code == ERConstants.INCIDENT_STATUS_INDICTED || 
    				incident.status.code == ERConstants.INCIDENT_STATUS_INSPECTION ||
                    incident.status.code == ERConstants.INCIDENT_STATUS_INCOMPLETE ||
                    incident.status.code == ERConstants.INCIDENT_STATUS_COMPLETED ||
					incident.status.code == ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION ||
					incident.status.code == ERConstants.INCIDENT_STATUS_FINALIZED){
	    			List<ER_Declined_Sell_Reason> reasons = ER_Declined_Sell_Reason.find("active = ? and isInspectiondeclined= 0 order by name asc", true).fetch();
	    			ER_Quotation selectedQuotation = null;
	    			if(flash.contains("selectedQuotation")){
	    				try{
	    					selectedQuotation = ER_Quotation.findById(Long.parseLong(flash.get("selectedQuotation")));
	    				}catch(Exception e){
	    					Logger.error(e, "Error getting quotation");
	    				}
	    			}else if(incident.quotations!=null && !incident.quotations.isEmpty()){
	    				for(ER_Quotation quotation: incident.quotations){
	    					if(incident.selectedQuotation == null){
	    						selectedQuotation = incident.quotations.get(0);
	    					}else if(quotation.id.equals(incident.selectedQuotation.id)){
	    						selectedQuotation = quotation;
	    					}
	    				}
	    			}
	    			Boolean inspection = incident.inspection != null;
	    			Integer inspectionType = null;
	    			String inspectionAddress = null;
	    			Date appointmentDate = null;
	    			String inspectionNumber = null;
					String inspectionDate = null;
					Date dateInspectionDate = null;
	    			if(incident.inspection != null){
	    				if(!FieldAccesor.isEmptyOrNull(incident, "inspection.type.code")){
	    					inspectionType = incident.inspection.type.code;
	    				}
	    				if(!FieldAccesor.isEmptyOrNull(incident, "inspection.address")){
	    					inspectionAddress = incident.inspection.address;
	    				}
	    				if(!FieldAccesor.isEmptyOrNull(incident, "inspection.appointmentDate")){
	    					appointmentDate = incident.inspection.appointmentDate;
	    				}
	    				if(!FieldAccesor.isEmptyOrNull(incident, "inspection.inspectionNumber")){
	    					inspectionNumber = incident.inspection.inspectionNumber;
							dateInspectionDate = incident.inspection.inspectionDate;
	    				}
						if(!FieldAccesor.isEmptyOrNull(incident, "inspection.inspectionNumber")){
							if (!StringUtil.isNullOrBlank(incident.inspection.inspectionDate)) {
								inspectionDate = new SimpleDateFormat("yyyy-MM-dd").format(incident.inspection.inspectionDate);
								dateInspectionDate = incident.inspection.inspectionDate;
							}
						}
	    			}
	    			
	    			if(connectedUser().role.code == ERConstants.USER_ROLE_FINAL_USER){
	    				List<ER_Inspection_Type> types = ER_Inspection_Type.find("code in (1, 2) order by typeOrder ASC").fetch();
	    				renderTemplate("Incidents/attendIncidentPublic.html", incident, reasons, selectedQuotation, types, inspection, inspectionType, inspectionAddress, appointmentDate, inspectionNumber,inspectionDate);
	    			}else{
	    				List<ER_Inspection_Type> types = ER_Inspection_Type.find("order by typeOrder ASC").fetch();
	    				renderTemplate("Incidents/attendIncident.html", incident, reasons, selectedQuotation, types, inspection, inspectionType, inspectionAddress, appointmentDate, inspectionNumber,inspectionDate,dateInspectionDate);
	    			}
    			}
    		}
    		}
		}
		catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
	}
    }
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void attendInspection(Long incidentId,int isApproved) {
		if (incidentId!=null) {
			ER_Incident incident = ER_Incident.findById(incidentId);
			List<ER_Declined_Sell_Reason> reasons = ER_Declined_Sell_Reason.find("active = ? and isInspectionDeclined =1 order by name asc", true).fetch();
			renderArgs.put("incident", incident);
			renderArgs.put("isApproved",isApproved);
			renderArgs.put("reasons", reasons);
			render();
		}

	}

	
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static void saveIncidentInspection(@Required Long id, Long noSaleReason,Date inspectionDate, int isApproved) {
    	flash.clear();
    	flash.discard();
		ER_Declined_Sell_Reason reason = null;
    		ER_Incident incident = ER_Incident.findById(id);
    		boolean success = false;
    		try {

	    		if (incident!=null  && !incident.isFinalized()) {
	    			if (incident.status.code == ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION) {
	    				if (isApproved == 0) {
							incident.completeTasks();
							incident.finalizedDate = new Date();
							incident.finalizer = connectedUser();
							incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_ANULLED).first();
							reason = ER_Declined_Sell_Reason.findById(noSaleReason);
							incident.declinedReason = reason;
							incident.save();
							success = true;
						}
						else{
							incident.completeTasks();
							incident.finalizedDate = new Date();
							incident.finalizer = connectedUser();
							incident.status = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
							ER_Inspection inspection = ER_Inspection.find("id = ?", incident.inspection.id).first();
							inspection.inspectionDate = inspectionDate;
							inspection.save();
							incident.save();
							success = true;
						}
	    			}
	    		}
    		} catch (Exception e) {
    			Logger.error(e, "Error finalizing incident");
    		}
    		
    		finalized(incident.id, success);
    	}
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static void saveIncidentState(@Required Long id, @Required Integer state, Long noSaleReason,
    									 Long selectedQuotation, Long selectedPaymentForm, Boolean inspection,
    									 Integer inspectionType, String inspectionAddress,
    									 @As("dd/MM/yyyy HH:mm") Date appointmentDate,Date policyValidity,
    									 String inspectionNumber,Date inspectionDate) {
    	flash.clear();
    	flash.discard();
    	
    	ER_Incident_Status incidentStatus = null;
    	ER_Declined_Sell_Reason reason = null;
    	ER_Quotation quotation = null;
    	ER_Payment_Frecuency frecuency = null;
    	ER_Incident incident = ER_Incident.findById(id);
		ER_Incident_Status incidentStatusIncomplete = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INCOMPLETE).first();
		if(incident.inspection == null) {
			ER_Inspection newInspection = new ER_Inspection();
			incident.inspection = newInspection;
			newInspection.save();
			incident.save();
		}
    	if(!validation.hasErrors()){
        	if(state == 0){
        		if(selectedQuotation != null){
	        		quotation = ER_Quotation.findById(selectedQuotation);
	        		validation.required("selectedQuotation", quotation);
	        		if(!selectedPaymentForm.equals(0L)){
	        			frecuency = ER_Payment_Frecuency.findById(selectedPaymentForm);
	        			validation.required("selectedPaymentForm", frecuency);

	        			if(frecuency.numberOfPayments > 6 &&  incident.payment.chargeType.id == 5) {
                            flash.error("No puede seleccionar medio de pago cobrador y número de pagos mayor a 6, favor modificar.");
                            attendIncident(id);
                        }
	        		}
        		}

				// check for auto inspection


				incident.policyValidity = policyValidity;
        		if( incident.status.code.equals(ERConstants.INCIDENT_STATUS_CREATED) ||
        			incident.status.code.equals(ERConstants.INCIDENT_STATUS_IN_PROGRESS) ||
        			incident.status.code.equals(ERConstants.INCIDENT_STATUS_INDICTED) ||
        			incident.status.code.equals(ERConstants.INCIDENT_STATUS_INSPECTION) ||
                    incident.status.code.equals(ERConstants.INCIDENT_STATUS_COMPLETED) ||
                    (incident.status.code.equals(ERConstants.INCIDENT_STATUS_INCOMPLETE) && StringUtil.isNullOrBlank(inspectionNumber)))
					{
        			validation.required("inspection", inspection);
            		if(inspection != null && inspection) {
            			validation.required("inspectionType", inspectionType);
            			if(inspectionType != null){
            				if(inspectionType == ERConstants.INSPECTION_TYPE_ADDRESS){
            					validation.required(inspectionAddress);
            					validation.required(appointmentDate);
            					incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INSPECTION).first();
            				}else if(inspectionType == ERConstants.INSPECTION_TYPE_SELLER){
                                if (incident.status.code.equals(ERConstants.INCIDENT_STATUS_INCOMPLETE)) {
									incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
									ER_Inspection currentInspection = ER_Inspection.find("id = ?", incident.inspection.id).first();
									currentInspection.inspectionDate = inspectionDate;
									currentInspection.save();
									incident.save();
                                }else {
                                    validation.required(inspectionNumber);
                                    incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
									ER_Inspection currentInspection = ER_Inspection.find("id = ?", incident.inspection.id).first();
									currentInspection.inspectionDate = inspectionDate;
									currentInspection.save();
									incident.save();
								}
							} else if (inspectionType == ERConstants.INSPECTION_TYPE_AUTO) {
								incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INSPECTION).first();
            				} else {
                                incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INSPECTION).first();

            				}
            			}
            		}else{
            			incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
            		}
        		}else if (incident.status.code.equals(ERConstants.INCIDENT_STATUS_INCOMPLETE) && !StringUtil.isNullOrBlank(inspectionNumber)){
					incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_COMPLETED).first();
					if(incident.inspection != null) {
						ER_Inspection currentInspection = ER_Inspection.find("id = ?", incident.inspection.id).first();
						if (currentInspection != null) {
							currentInspection.inspectionDate = inspectionDate;
							currentInspection.inspectionNumber = inspectionNumber;
							currentInspection.save();
						} else {
							currentInspection = new ER_Inspection();
							currentInspection.inspectionDate = inspectionDate;
							currentInspection.inspectionNumber = inspectionNumber;
							currentInspection.save();
							incident.inspection = currentInspection;
						}
					}
					else {
						ER_Inspection currentInspection = new ER_Inspection();
						currentInspection.inspectionDate = inspectionDate;
						currentInspection.inspectionNumber = inspectionNumber;
						currentInspection.save();
						incident.inspection = currentInspection;
					}
                    incident.save();
				}
        		else{
        			incidentStatus = incident.status;
        		}
        	}else if(state == 1){
        		incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_ANULLED).first();
        		if(noSaleReason != null){
        			reason = ER_Declined_Sell_Reason.findById(noSaleReason);
        		}
        		validation.required("noSaleReason", reason);
        	}
        	validation.required("state", incidentStatus);
    	}
    	
    	if(validation.hasErrors()){
    		params.flash();
    		validation.keep();
    		attendIncident(id);
    	}else{
    		boolean success = false;
    		try{
    			if( incident.status.code.equals(ERConstants.INCIDENT_STATUS_CREATED) ||
            		incident.status.code.equals(ERConstants.INCIDENT_STATUS_IN_PROGRESS) ||
            		incident.status.code.equals(ERConstants.INCIDENT_STATUS_INDICTED) ||
            		incident.status.code.equals(ERConstants.INCIDENT_STATUS_INSPECTION) ||
                    incident.status.code.equals(ERConstants.INCIDENT_STATUS_COMPLETED ) ||
					(incident.status.code.equals(ERConstants.INCIDENT_STATUS_INCOMPLETE) && StringUtil.isNullOrBlank(inspectionNumber))){
    				
	    			String[] livianos = new String[]{"01", "03", "05", "08", "09", "13", "16", "17"};
	    			String[] pesados = new String[]{"02", "06", "10", "11", "12", "14", "19", "20", "21", "22", "23"};
	    			String[] motos = new String[]{"04", "07", "18"};
	    			String inspectionTypeEnum = null;
	    			for(String liviano: livianos){
	    				if(liviano.equals(incident.vehicle.type.transferCode.trim())){
	    					inspectionTypeEnum = "LIGHT_VEHICLE";
	    				}
	    			}
	    			for(String pesado: pesados){
	    				if(pesado.equals(incident.vehicle.type.transferCode.trim())){
	    					inspectionTypeEnum = "HEAVY_VEHICLE";
	    				}
	    			}
	    			for(String moto: motos){
	    				if(moto.equals(incident.vehicle.type.transferCode.trim())){
	    					inspectionTypeEnum = "MOTORCYCLE";
	    				}
	    			}
	    			ER_Inspection inspectionInfo = null;
	    			if(state==0 && inspection != null && inspection){
	    				if(incident.inspection != null){
	    					inspectionInfo = incident.inspection;
	    				}else{
	    					inspectionInfo = new ER_Inspection();
	    				}
	    				Inspection inspectionR = new Inspection();
	    				inspectionR.setClientName(incident.client.getFullName());
	    				inspectionR.setContactEmail(incident.client.email);
	    				inspectionR.setContactPhone(incident.client.phoneNumber1);
	    				inspectionInfo.type = ER_Inspection_Type.find("code = ?", inspectionType).first();

	    				if(inspectionType == ERConstants.INSPECTION_TYPE_ADDRESS){
	    					inspectionInfo.address = inspectionAddress;
	    					inspectionInfo.appointmentDate = appointmentDate;
	    					
	    					inspectionR.setAddress(inspectionAddress);
	    					inspectionR.setDate(DateHelper.formatDate(appointmentDate, "dd/MM/yyyy HH:mm:ss"));
	    					inspectionR.setQuotationNumber(incident.number);
	    					inspectionR.setInspectionLocation("DOMICILIO");
	    					inspectionR.setInspectionType(inspectionTypeEnum);
	    					ER_User user = connectedUser();
	    					InspectionResponse inspectionResponse;
	    					if (user.selectedBroker != null) {
								inspectionR.setBrokerId(user.selectedBroker);
								inspectionResponse = inspectionService.createInspectionBroker(inspectionR);
							} else {
								inspectionResponse = inspectionService.createInspection(inspectionR);
							}
	    					if(!inspectionResponse.getSuccess()){
	    						flash.error("Ha ocurrido un error en la conexión con Inspecciones.");
	    						ER_Exceptions exceptions = new ER_Exceptions();
	    						exceptions.description = "Ha ocurrido un error en la conexión con Inspecciones.";
	    						exceptions.exceptionDate = new Date();
	    						exceptions.quotation = quotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                incident.save();
                                attendIncident(id);
	    					}
	
	    					Mails.addressInspection(incident, inspectionInfo);
	    				}else if(inspectionType == ERConstants.INSPECTION_TYPE_CENTER){
	    					inspectionR.setInspectionLocation("CENTRO_ATENCION");
	    					inspectionR.setInspectionType(inspectionTypeEnum);
							inspectionR.setQuotationNumber(incident.number);
							ER_User user = connectedUser();
							InspectionResponse inspectionResponse;
							if (user.selectedBroker != null) {
								inspectionR.setBrokerId(user.selectedBroker);
								inspectionResponse = inspectionService.createInspectionBroker(inspectionR);
							} else {
								inspectionResponse = inspectionService.createInspection(inspectionR);
							}

	    					if(!inspectionResponse.getSuccess()){
	    						flash.error("Ha ocurrido un error en la conexión con Inspecciones.");
                                ER_Exceptions exceptions = new ER_Exceptions();
                                exceptions.description = "Ha ocurrido un error en la conexión con Inspecciones.";
                                exceptions.exceptionDate = new Date();
                                exceptions.quotation = quotation;
                                exceptions.active = 1;
                                exceptions.save();
                                incident.status = incidentStatusIncomplete;
                                incident.save();
	    						attendIncident(id);
	    					}
	    					
	    					Mails.centersList(incident);
	    				}else if(inspectionType == ERConstants.INSPECTION_TYPE_SELLER){
	    					inspectionInfo.inspectionNumber = inspectionNumber;
	    				//	inspectionInfo.inspectionDate = new Date();
	    					inspectionInfo.inspected = true;
	    				} else if (inspectionType == ERConstants.INSPECTION_TYPE_AUTO) {
							InspectionAutoRequest requestAuto = new InspectionAutoRequest();
							requestAuto.setFirstName(incident.client.firstName);
							requestAuto.setSecondName(incident.client.secondName);
							requestAuto.setFirstSurname(incident.client.firstSurname);
							requestAuto.setSecondSurname(incident.client.secondSurname);
							requestAuto.setMarriedSurname(incident.client.marriedSurname);
							requestAuto.setIdentificationDocument(incident.client.identificationDocument);
							requestAuto.setTaxNumber(incident.client.taxNumber);
							requestAuto.setLicenseNumber(incident.client.licenseNumber);
							requestAuto.setLicenseType(incident.client.licenseType != null ? incident.client.licenseType.transferCode : "");
							// Add PhoneData
							List<InspectionAutoRequest.PhoneData> phones = new ArrayList();
							InspectionAutoRequest.PhoneData phoneData = new InspectionAutoRequest.PhoneData();
							phoneData.setPhone(incident.client.phoneNumber1);
							phones.add(phoneData);
							requestAuto.setPhones(phones);
							// Add Email Data
							List<InspectionAutoRequest.EmailData> emails = new ArrayList<>();
							InspectionAutoRequest.EmailData emailData = new InspectionAutoRequest.EmailData();
							emailData.setEmail(incident.client.email);
							emails.add(emailData);
							requestAuto.setEmails(emails);
							// Add Email Broker Data
							List<InspectionAutoRequest.EmailBrokerData> emailsBroker = new ArrayList<>();
							InspectionAutoRequest.EmailBrokerData emailBrokerData = new InspectionAutoRequest.EmailBrokerData();
							emailBrokerData.setEmail(connectedUser().email);
							emailsBroker.add(emailBrokerData);
							requestAuto.setEmailsBroker(emailsBroker);
							//
							requestAuto.setAddress(incident.client.address);
							requestAuto.setVehicleOwner(incident.vehicle.owner);
							requestAuto.setBrand(incident.vehicle.line.name);
                            requestAuto.setLine(incident.vehicle.line.brand.name);
                            requestAuto.setYear(incident.vehicle.erYear.year);
							requestAuto.setPlate(incident.vehicle.plate);
							requestAuto.setTypeVehicle(incident.vehicle.type.transferCode);
							requestAuto.setColor(incident.vehicle.color);
							requestAuto.setEngine(incident.vehicle.engine);
							requestAuto.setVin(incident.vehicle.chassis);
							requestAuto.setMileage(incident.vehicle.mileage);
							// Other Data
							requestAuto.setLicenseYears(" ");
							requestAuto.setUse(" ");
							requestAuto.setOrigin(" ");
							requestAuto.setCoin("Q");

							InspectionAutoResponse inspectionResponse = inspectionService.createAutoInspection(requestAuto);
							if(!"SATISFACTORIO".equalsIgnoreCase(inspectionResponse.getMessage())) {
								flash.error("Ha ocurrido un error en la conexión con AutoInspecciones.");
								ER_Exceptions exceptions = new ER_Exceptions();
								exceptions.description = "Ha ocurrido un error en la conexión con AutoInspecciones.";
								exceptions.exceptionDate = new Date();
								exceptions.quotation = quotation;
								exceptions.active = 1;
								exceptions.save();
								incident.status = incidentStatusIncomplete;
								incident.save();
								attendIncident(id);
							} else {
								inspectionInfo.inspectionNumber = String.valueOf(inspectionResponse.getInspectionNumber());
							}
						}
	    				inspectionInfo.incident = incident;
	    				inspectionInfo = inspectionInfo.save();
	    			}
	    			incident.inspection = inspectionInfo;
    			}
    			
    			if(incidentStatus.code == ERConstants.INCIDENT_STATUS_ANULLED || incidentStatus.code == ERConstants.INCIDENT_STATUS_COMPLETED){
    				incident.finalizedDate = new Date();
	    			incident.finalizer = connectedUser();

	    			incident.completeTasks();
    			}
    			if(incidentStatus.code == ERConstants.INCIDENT_STATUS_INSPECTION || incidentStatus.code == ERConstants.INCIDENT_STATUS_COMPLETED){
    				incident.saleDate = new Date();
    			}
    			incident.status = incidentStatus;
    			incident.declinedReason = reason;
    			incident.selectedQuotation = quotation;
    			incident.selectedPaymentFrecuency = frecuency;
    			incident.save();
    			
    			//Generar resguardo
    			if(incidentStatus.code == ERConstants.INCIDENT_STATUS_COMPLETED || incidentStatus.code == ERConstants.INCIDENT_STATUS_INSPECTION || incidentStatus.code == ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION){
    				generateGuard(incident);
    			}
    			success = true;
    		}catch(Exception e){
    			Logger.error(e, "Error finalizing incident");
    		}
    		
    		finalized(incident.id, success);
    	}
    }
	
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void saveIncidentStatePublic(@Required Long id, 
    									 Long selectedQuotation, 
    									 Long selectedPaymentForm,  
    									 Integer inspectionType, 
    									 String inspectionAddress, 
    									 @As("dd/MM/yyyy HH:mm") 
    									 Date appointmentDate) {
    	flash.clear();
    	flash.discard();
    	
    	ER_Incident_Status incidentStatus = null;
    	ER_Declined_Sell_Reason reason = null;
    	ER_Quotation quotation = null;
    	ER_Payment_Frecuency frecuency = null;
    	ER_Incident incident = ER_Incident.findById(id);
    	
    	if(!validation.hasErrors()){
    		if(selectedQuotation != null){
        		quotation = ER_Quotation.findById(selectedQuotation);
        		validation.required("selectedQuotation", quotation);
        		if(!selectedPaymentForm.equals(0L)){
        			frecuency = ER_Payment_Frecuency.findById(selectedPaymentForm);
        			validation.required("selectedPaymentForm", frecuency);
        		}
    		}

    		if( incident.status.code.equals(ERConstants.INCIDENT_STATUS_CREATED) ||
        		incident.status.code.equals(ERConstants.INCIDENT_STATUS_IN_PROGRESS) ||
        		incident.status.code.equals(ERConstants.INCIDENT_STATUS_INDICTED) ||
        		incident.status.code.equals(ERConstants.INCIDENT_STATUS_INSPECTION) ||
				incident.status.code.equals(ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION)){
				validation.required("inspectionType", inspectionType);
				if(inspectionType != null){
					if(inspectionType == ERConstants.INSPECTION_TYPE_ADDRESS){
						validation.required(inspectionAddress);
						validation.required(appointmentDate);
					}
					incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INSPECTION).first();
				}
	        	validation.required("state", incidentStatus);
    		}
    	}
    	
    	if(validation.hasErrors()){
    		params.flash();
    		validation.keep();
    		attendIncident(id);
    	}else{
    		boolean success = false;
    		try{
    			if( incident.status.code.equals(ERConstants.INCIDENT_STATUS_CREATED) ||
            		incident.status.code.equals(ERConstants.INCIDENT_STATUS_IN_PROGRESS) ||
            		incident.status.code.equals(ERConstants.INCIDENT_STATUS_INDICTED) ||
            		incident.status.code.equals(ERConstants.INCIDENT_STATUS_INSPECTION)){
	    			String[] livianos = new String[]{"01", "03", "05", "08", "09", "13", "16", "17"};
	    			String[] pesados = new String[]{"02", "06", "10", "11", "12", "14", "19", "20", "21", "22", "23"};
	    			String[] motos = new String[]{"04", "07", "18"};
	    			String inspectionTypeEnum = null;
	    			for(String liviano: livianos){
	    				if(liviano.equals(incident.vehicle.type.transferCode.trim())){
	    					inspectionTypeEnum = "LIGHT_VEHICLE";
	    				}
	    			}
	    			for(String pesado: pesados){
	    				if(pesado.equals(incident.vehicle.type.transferCode.trim())){
	    					inspectionTypeEnum = "HEAVY_VEHICLE";
	    				}
	    			}
	    			for(String moto: motos){
	    				if(moto.equals(incident.vehicle.type.transferCode.trim())){
	    					inspectionTypeEnum = "MOTORCYCLE";
	    				}
	    			}
	    			ER_Inspection inspectionInfo = null;
					if(incident.inspection != null){
						inspectionInfo = incident.inspection;
					}else{
						inspectionInfo = new ER_Inspection();
					}
					Inspection inspectionR = new Inspection();
					inspectionR.setClientName(incident.client.getFullName());
                    inspectionR.setContactEmail(incident.client.email);
                    inspectionR.setContactPhone(incident.client.phoneNumber1);
                    inspectionInfo.type = ER_Inspection_Type.find("code = ?", inspectionType).first();
					if(inspectionType == ERConstants.INSPECTION_TYPE_ADDRESS){
						inspectionInfo.address = inspectionAddress;
						inspectionInfo.appointmentDate = appointmentDate;
						
						inspectionR.setAddress(inspectionAddress);
						inspectionR.setDate(DateHelper.formatDate(appointmentDate, "dd/MM/yyyy HH:mm:ss"));
						inspectionR.setQuotationNumber(incident.number);
						inspectionR.setInspectionLocation("DOMICILIO");
						inspectionR.setInspectionType(inspectionTypeEnum);
						InspectionResponse inspectionResponse = inspectionService.createInspection(inspectionR);
						if(!inspectionResponse.getSuccess()){
							flash.error("Ha ocurrido un error en la conexión con Inspecciones.");
                            ER_Exceptions exceptions = new ER_Exceptions();
                            exceptions.description = "Ha ocurrido un error en la conexión con Inspecciones.";
                            exceptions.exceptionDate = new Date();
                            exceptions.quotation = quotation;
                            exceptions.active = 1;
                            exceptions.save();
                            incident.status.code = ERConstants.INCIDENT_STATUS_INCOMPLETE;
                            incident.save();
							attendIncident(id);
						}
						Mails.addressInspection(incident, inspectionInfo);
					}else if(inspectionType == ERConstants.INSPECTION_TYPE_CENTER){
						inspectionR.setInspectionLocation("CENTRO_ATENCION");
						inspectionR.setInspectionType(inspectionTypeEnum);
						InspectionResponse inspectionResponse = inspectionService.createInspection(inspectionR);
						if(!inspectionResponse.getSuccess()){
							flash.error("Ha ocurrido un error en la conexión con Inspecciones.");
                            ER_Exceptions exceptions = new ER_Exceptions();
                            exceptions.description = "Ha ocurrido un error en la conexión con Inspecciones.";
                            exceptions.exceptionDate = new Date();
                            exceptions.quotation = quotation;
                            exceptions.active = 1;
                            exceptions.save();
                            incident.status.code = ERConstants.INCIDENT_STATUS_INCOMPLETE;
                            incident.save();
							attendIncident(id);
						}
						Mails.centersList(incident);
					}
					inspectionInfo.incident = incident;
					inspectionInfo = inspectionInfo.save();
					incident.inspection = inspectionInfo;
    			}
    			
    			incident.saleDate = new Date();
    			incident.status = incidentStatus;
    			incident.declinedReason = reason;
    			incident.selectedQuotation = quotation;
    			incident.selectedPaymentFrecuency = frecuency;
    			incident.save();
    			
    			generateGuard(incident);
    			success = true;
    		}catch(Exception e){
    			Logger.error(e, "Error finalizing incident");
    		}
    		
    		finalized(incident.id, success);
    	}
    }
    
    private static void generateGuard(ER_Incident incident) {
    	
    	ER_Guard guard = ER_Guard.find("incident = ?", incident).first();
    	if (guard == null) {
    		guard = new ER_Guard();
    	}
    	
    	guard.creationDate = new Date();
    	guard.expirationDate = DateHelper.guardExpirationDate(guard.creationDate);
		guard.incident = incident;
		guard.save();
		
		//Create reminder in database
		ReminderHelper.createReminderForGuard(guard);
		
		//Send email to client
		Mails.generatedGuard(guard);
    	
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void finalized(Long incidentId, Boolean success) {
    	if (incidentId!=null) {
    		ER_Incident incident = ER_Incident.findById(incidentId);
    		if (incident!=null) {
    			renderTemplate("Incidents/finalized.html", incident, success);
    		}
    	}
    	
    }
    
    /*
	 * ************************************************************************************************************************
	 * New quotation
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void newQuotation(Long incidentId) {
    	try{
    	if (incidentId!=null) {
    		ER_Incident incident = ER_Incident.findById(incidentId);
    		if(incident!=null && canViewIncident(incident) && incident.canModifyQuotations()){
    			QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
            	request.setBrand(incident.vehicle.line.brand.name);
        		request.setLine(incident.vehicle.line.name);
        		request.setYear(incident.vehicle.erYear.year);
        		request.setCurrency("Q");
        		QueryAverageValueVehicleResponse queryAverage = averageValueServiceBus.averageValueQuery(request);
        		if(queryAverage != null && queryAverage.getAverageValue() != null){
        			renderArgs.put("value", new ER_Average_Value(queryAverage.getAverageValue()));
        		}else{
        			renderArgs.put("value", new ER_Average_Value(new BigDecimal(150000)));
        		}
    			
				//Authorized products
			    renderArgs.put("products", authorizedProducts());
    			
    	    	if (flash.contains("quotation.product.id")) {
    	    		String productString = flash.get("quotation.product.id");
    	    		Long productId = (long) -1;
    	    		try {
    	    			productId = Long.parseLong(productString);
    	    		} catch (Exception e) {
    	    			Logger.error(e, "Error parsing product id");
    	    		}
    	    		
    	    		renderArgs.put("parameters", ServiceVehicles.parametersMapForProduct(productId));
    	    	}
    	    	
    	    	if (flash.contains("paymentFrecuencies")) {
    	    		try {
    	    			String frecString[] = flash.get("paymentFrecuencies").split(",");
    	    			Long[] paymentFrecuencies = new Long[frecString.length];
    	    			for (int i=0; i<frecString.length; i++) {
    	    				paymentFrecuencies[i] = Long.parseLong(frecString[i]);
    	    			}
    	    			renderArgs.put("selectedFrecuencies", paymentFrecuencies);
    	    		} catch (Exception e) {
    	    			Logger.debug(e, "Payment frecuencies: Could not parse longs", flash.get("paymentFrecuencies"));
    	    		}
    	    	}
    	    	
    	    	//Facultative deductibles
    			List<ER_Facultative_Deductible> facultative = ER_Facultative_Deductible.find("order by primeDiscount").fetch();
    			renderArgs.put("facultative", facultative);
    	    	
    	    	//Add the configuration to render args
    	    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	    	if (configuration!=null) {
    	    		renderArgs.put("configuration", configuration);
    	    	}

                List<Map<String, Object>> frecuencies = GeneralMethods.getAvailableFrecuencies();
    	    	renderArgs.put("frecuencies", frecuencies);

				// Search for LoJack configs.
				Integer loJackYear = Integer.parseInt(!StringUtil.isNullOrBlank(request.getYear()) ? request.getYear() : "0" );
				Logger.info("LoJack a buscar: " + loJackYear + " - " + incident.vehicle.line.brand.name + " - " + incident.vehicle.line.name + " - " + incident.vehicle.line.id );

				ER_Vehicle_LoJack loJack = ER_Vehicle_LoJack.find("line.id = ? and (yearInit IS NULL or yearInit <= ?) and (yearEnd IS NULL or yearEnd >= ?)", incident.vehicle.line.id, loJackYear, loJackYear ).first();
				Logger.info("Encontro LoJack: " + (loJack != null));
				if(loJack != null){
					// If LoJack for vehicle, set data
					renderArgs.put("loJackId", loJack.id);
					List<LoJackOptions> loJackOptions = GeneralMethods.getAvailableLoJacks(loJack.id);
					renderArgs.put("loJackOptions", loJackOptions);
				}
    			render(incident);
    		}
    	}
    	
    	incidentDetail(incidentId);
		}
		catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
	}

    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void simulateQuotation(@Valid ER_Quotation quotation, @Required Long[] paymentFrecuencies, Long loJackId) {
	try {
		if (quotation.incident == null || quotation.incident.id == null) {
			incidentsList(null, false);
		}
		quotation.setValuesOfQuotation(quotation);

		ER_General_Configuration configuration = ER_General_Configuration.find("").first();
		BigDecimal hundred = new BigDecimal(100);
		ER_Coverage theftCoverage = (configuration != null) ? configuration.theftCoverage : null;
		BigDecimal partialTheftPercentage = (configuration != null) ? configuration.partialTheftPercentage.divide(hundred) : BigDecimal.ZERO;

		if (quotation.product == null || quotation.product.id == null) {
			validation.addError("quotation.product", Messages.get("quotation.form.quotation.required"));
		}

		List<ER_Quotation_Parameter> parameters = quotation.parameters;
		if (parameters != null && !parameters.isEmpty()) {
			BigDecimal theftValue = BigDecimal.ZERO;

			int theftIndex = 0;
			for (int j = 0; j < parameters.size(); j++) {
				ER_Quotation_Parameter parameter = parameters.get(j);
				if (!parameter.validateValue()) {
					String key = "quotation.parameters[" + j + "].value";
					if (!validation.hasError(key)) {
						validation.addError(key, Messages.get("quotation.form.quotation.required"));
					}
				}

				if (parameter.productCoverage != null && parameter.productCoverage.coverage.equals(theftCoverage)) {
					theftValue = (parameter.value != null) ? parameter.value : BigDecimal.ZERO;
					theftIndex = j;
				}
			}

			if (quotation.carValue != null) {
				if (!(quotation.carValue.multiply(partialTheftPercentage).compareTo(theftValue) >= 0)) {
					validation.addError("quotation.parameters[" + theftIndex + "].value", Messages.get("quotation.form.quotation.theftcoverageerror", configuration.partialTheftPercentage, "%%"));
				}
			}
		}

		if (quotation.hasCarValue()) {
			validation.required("quotation.carValue", quotation.carValue);
		}

		// Validate if has average value and car value is within parameters
		if (quotation.incident.vehicle.averageValue != null && quotation.carValue != null) {
			BigDecimal averageValueParam = new BigDecimal(0.25);
			ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
			if (currentConfiguration.averageValueConfig != null) {
				averageValueParam = currentConfiguration.averageValueConfig.divide(BigDecimal.valueOf(100));
			}
			BigDecimal min = BigDecimal.valueOf(1).subtract(averageValueParam);
			BigDecimal max = BigDecimal.valueOf(1).add(averageValueParam);
			// Find lower and upper parameter
			BigDecimal lowerValue = quotation.incident.vehicle.averageValue.multiply(min);
			BigDecimal upperValue = quotation.incident.vehicle.averageValue.multiply(max);
			// car value within
			if (quotation.carValue.compareTo(lowerValue) < 0 || quotation.carValue.compareTo(upperValue) > 0) {
				validation.addError("quotation.carValue", Messages.get("quotation.form.quotation.carvaluerange"));
			}
		}

		List<LoJackOptions> loJackOptions = GeneralMethods.getAvailableLoJacks(loJackId);
		if (quotation.loJack != null) {
			for (LoJackOptions loJackOption : loJackOptions) {
				if (loJackOption.number == quotation.loJack) {
					quotation.selectedLoJack = loJackOption;
					quotation.loJackRecharge = loJackOption.recharge;
					break;
				}
			}
		}

		if (validation.hasErrors()) {
			params.flash();
			validation.keep();
			newQuotation(quotation.incident.id);

		} else {

			QuotationHelper.calculateTotalPrime(quotation, quotation.incident.vehicle, paymentFrecuencies);

			if (quotation.discount != null) {

				BigDecimal primeDiscount = BigDecimal.ZERO;
				if (quotation.product.hasFacultative != null && quotation.product.hasFacultative && quotation.facultative != null) {
					ER_Facultative_Deductible facultative = ER_Facultative_Deductible.findById(quotation.facultative);
					if (facultative != null) {
						primeDiscount = facultative.primeDiscount;
					}
				}

				BigDecimal authorizedDiscount = Incidents.getAuthorizedDiscount(quotation.product, quotation.carValue, primeDiscount);
				validation.max("quotation.discount", quotation.discount, authorizedDiscount.doubleValue());
				validation.min("quotation.discount", quotation.discount, 0.0);

				quotation.applyDiscount(quotation.discount);
			}

			validation.min("quotation.totalPrime", quotation.totalPrime, 0.01);
			if (validation.hasErrors()) {
				params.flash();
				validation.keep();
				newQuotation(quotation.incident.id);
			}

			try {
				ER_Vehicle_Line line = ER_Vehicle_Line.findById(quotation.incident.vehicle.line.id);
				ER_Year erYear = ER_Year.findById(quotation.incident.vehicle.erYear.id);
				QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
				request.setBrand(line.brand.name);
				request.setLine(line.name);
				request.setYear(erYear.year);
				request.setCurrency("Q");
				QueryAverageValueVehicleResponse queryAverage = policyService.queryAverageValueVehicle(request);
				if (!FieldAccesor.isEmptyOrNull(queryAverage, "averageValue") && quotation.carValue != null) {
					// Garanteed value check
					BigDecimal garanteedValueParam = new BigDecimal(0.15);
					ER_General_Configuration currentConfiguration = ER_General_Configuration.find("").first();
					if (currentConfiguration.garanteedValueConfig != null) {
						garanteedValueParam = currentConfiguration.garanteedValueConfig.divide(BigDecimal.valueOf(100));
					}
					BigDecimal diff = queryAverage.getAverageValue().multiply(garanteedValueParam).setScale(2, RoundingMode.HALF_UP);
					BigDecimal min = queryAverage.getAverageValue().subtract(diff).setScale(2, RoundingMode.HALF_UP);
					BigDecimal max = queryAverage.getAverageValue().add(diff).setScale(2, RoundingMode.HALF_UP);

					if (quotation.carValue.compareTo(queryAverage.getAverageValue()) == 0) {
						quotation.setGaranteedValue(Boolean.TRUE);
					} else if (quotation.carValue.compareTo(min) >= 0 && quotation.carValue.compareTo(max) <= 0) {
						quotation.setGaranteedValue(Boolean.TRUE);
					}
				}
			} catch (Exception e) {
				Logger.error(e, e.getMessage());
			}

			String jsonField = quotation.toJsonString(true);
			renderArgs.put("selectedFrecuencies", paymentFrecuencies);
			render(quotation, jsonField, configuration, loJackId);
			}
		}
		catch(Exception e){
			Logger.error("error: " + e.getMessage());
			e.printStackTrace();
		}
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void saveQuotation(String iField, Long incidentId, String back, Long[] paymentFrecuencies) {
 	try{
		if (back != null) {
			ER_Quotation quotation = ER_Quotation.quotationFromJson(iField, true);
			if (quotation.product != null) {
				flash.put("quotation.product.id", quotation.product.id);
				flash.put("quotation.carValue", quotation.carValue);
				flash.put("quotation.facultative", quotation.facultative);
				flash.put("quotation.discount", quotation.discount);
				int i = 0;

				if (quotation.parameters != null) {
					for (ER_Quotation_Parameter parameter : quotation.parameters) {

						if (parameter.validateValue()) {
							if (parameter.coverageValue != null) {
								flash.put("quotation.parameters[" + i + "].coverageValue.id", parameter.coverageValue.id);
							}
							if (parameter.value != null) {
								flash.put("quotation.parameters[" + i + "].value", StringFormatExtensions.decimalFormat(parameter.value.doubleValue()));
							}

							if (parameter.applyInsurance != null) {
								flash.put("quotation.parameters[" + i + "].applyInsurance", (parameter.applyInsurance) ? "on" : "");
							}
							i++;
						}
					}
				}
			}

			String frecuencies = "";
			if (paymentFrecuencies != null) {
				for (int i = 0; i < paymentFrecuencies.length; i++) {
					frecuencies += paymentFrecuencies[i];
					if (i != paymentFrecuencies.length - 1) {
						frecuencies += ",";
					}

				}
				if (!frecuencies.isEmpty()) {
					flash.put("paymentFrecuencies", frecuencies);
				}
			}

			newQuotation(incidentId);
		}

		ER_Incident incident = ER_Incident.findById(incidentId);
		if (canViewIncident(incident) && incident.canModifyQuotations()) {
			ER_Quotation quotation = ER_Quotation.quotationFromJson(iField, true);

			boolean hasDiscount = quotation.discount != null && quotation.discount.compareTo(BigDecimal.ZERO) > 0;
			if (hasDiscount) {
				quotation.discountDate = new Date();
				quotation.discountAuthorizedUser = connectedUser();
			}

			quotation.product = ER_Product.findById(quotation.product.id);
			quotation.incident = incident;

			quotation.creationDate = new Date();
			QueryAverageValueVehicleRequest request = new QueryAverageValueVehicleRequest();
			request.setBrand(incident.vehicle.line.brand.name);
			request.setLine(incident.vehicle.line.name);
			request.setYear(incident.vehicle.erYear.year);
			request.setCurrency("Q");
			quotation.loadDetailJSON();
			QueryAverageValueVehicleResponse queryAverage = policyService.queryAverageValueVehicle(request);
			if (!FieldAccesor.isEmptyOrNull(queryAverage, "averageValue") && quotation.quotationDetail != null && quotation.carValue != null) {
				BigDecimal diff = queryAverage.getAverageValue().multiply(new BigDecimal(0.15)).setScale(2, RoundingMode.HALF_UP);
				BigDecimal min = queryAverage.getAverageValue().subtract(diff).setScale(2, RoundingMode.HALF_UP);
				BigDecimal max = queryAverage.getAverageValue().add(diff).setScale(2, RoundingMode.HALF_UP);

				if (quotation.carValue.compareTo(queryAverage.getAverageValue()) == 0) {
					quotation.quotationDetail.setVehicleValue(queryAverage.getAverageValue());
				} else if (quotation.carValue.compareTo(min) >= 0 && quotation.carValue.compareTo(max) <= 0) {
					quotation.quotationDetail.setVehicleValue(quotation.carValue);
				}
			}
			quotation.save();

			List<ByteArrayOutputStream> streamArray = new ArrayList<ByteArrayOutputStream>();
			streamArray.add(quotationPDFData(quotation));

			boolean result = Mails.quotations(incident, streamArray, true);
			Mails.incidentDetail(incident);
			if (!result) {
				flash.put("incident.quotation.send.warning", Messages.get("incident.quotation.send.warning"));
				incidentDetail(incident.id);
			}

			flash.success(Messages.get("incident.quotation.send.success"));
			incidentDetail(incident.id);
		}

		newQuotation(incidentId);
		}
    catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
		}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Client editing
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void editClient(Long clientId, Long incidentId, Integer activeTab) {
		try {
		if (clientId != null) {
			ER_Incident incident = ER_Incident.findById(incidentId);
			ER_Client client = incident.client;
			ER_Client_Payer payer = client.payer != null ? (ER_Client_Payer) ER_Client_Payer.findById(client.payer.id) : null;
			ER_Legal_Representative legalRepresentative = client.legalRepresentative;
			ER_Legal_Representative legalRepresentativePayer = client.payer != null ? client.payer.legalRepresentativePayer : null;
			ER_Vehicle vehicle = incident.vehicle;
			ER_Payment payment = incident.payment;
			ER_Client_PEP clientPEP = client.clientPEP;
			ER_Client_PayerPEP clientPayerPEP = client.payer != null ? client.payer.clientPayerPEP : null;
			client.ConvertUpper();
			if (canViewClient(client)) {
				renderArgs.put("countries", ER_Geographic_Area.find("id_father is null order by name asc").fetch());
				renderArgs.put("professions", ER_Profession.find("order by name asc").fetch());
				renderArgs.put("beneficiaries", ER_Beneficiaries.find(" order by name asc").fetch());
//    	    	renderArgs.put("phoneNumbersString", client.phoneNumbers != null ? client.phoneNumbers : "");

				loadAddressCatalogs(client, payer);

				if (vehicle != null) {
					if (vehicle.line != null && vehicle.line.brand != null) {
						//List<ER_Vehicle_Value> values = ER_Vehicle_Value.find("year != null and line.insurable = 1 and line.id = ? order by year asc", vehicle.line.id).fetch();
						//renderArgs.put("values", values);

						//List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("select distinct v.line from ER_Vehicle_Value v where v.year != null and v.line.insurable = 1 and v.line.vehicleClass IS NOT NULL and v.line.brand.id = ? order by v.line.name", vehicle.line.brand.id).fetch();
						//renderArgs.put("lines", lines);

						List<ER_Vehicle_Line> lines = ER_Vehicle_Line.find("select distinct v from ER_Vehicle_Line as v where v.brand.id = ? and v.insurable = 1  and v.transferCode is not null order by v.name", vehicle.line.brand.id).fetch();
						renderArgs.put("lines", lines);
					}
					List<ER_Vehicle_Brand> brands = ER_Vehicle_Brand.find("select distinct v.brand from ER_Vehicle_Line v where v.insurable = 1 and v.vehicleClass IS NOT NULL order by v.brand.name").fetch();
					renderArgs.put("brands", brands);
					renderArgs.put("vehicleTypes", ER_Vehicle_Type.find("order by name").fetch());
					renderArgs.put("vehicleRates", ER_Rate.findAll());
					renderArgs.put("vehicleERYears", ER_Year.findAll());
					renderArgs.put("vehicleReminderTypes", ER_Reminder_Type.find("order by name asc").fetch());
					renderArgs.put("vehiclePlateTypes", ER_Plate_Type.findAll());
					renderArgs.put("chargeTypes", ER_Charge_Type.find("order by name asc").fetch());
					renderArgs.put("bankAccountTypes", ER_Bank_Account_Type.find("order by name asc").fetch());
					renderArgs.put("banks", ER_Bank.find("select distinct b from ER_Bank as b where b.active = ?", true).fetch());
					renderArgs.put("cardTypes", ER_Card_Type.find("order by name asc").fetch());
					renderArgs.put("cardClassz", ER_Card_Class.find("order by name asc").fetch());

					if (vehicle.armor == null) {
						vehicle.armor = "N";
					}
					if (vehicle.isNew == null) {
						vehicle.isNew = true;
					}
				}
				if (payer == null) {
					payer = new ER_Client_Payer();
					payer.isIndividual = true;
				}
				loadCatalogsClient();

				List<ER_Aditional_Multimedia> aditional_multimedia = null;
				if (client.multimedia != null)
					aditional_multimedia = ER_Aditional_Multimedia.find("multimedia_id = ?", client.multimedia.id).fetch();


				render(incident, client, vehicle, payment, payer, legalRepresentative, legalRepresentativePayer, incidentId, activeTab, clientPEP, clientPayerPEP, aditional_multimedia);
			}
		}
		incidentsList(null, null);
		}
    	catch(Exception e){
			Logger.error("error: " + e.getMessage());
			e.printStackTrace();
		}
    }

    /**
     * Load all the catalogs to the client form
     */
    private static void loadCatalogsClient() {
    	try {
			List<ER_Civil_Status> statuses = ER_Civil_Status.find("active = ? order by name", Boolean.TRUE).fetch();
			renderArgs.put("statuses", statuses);

			//-------Obtener la lista de nacionalidades ordenados por nombre, mostrando GUATEMALA como primer elemento.--------//
			List<ER_Nationality> nationalities = ER_Nationality.find("active = ? order by name", Boolean.TRUE).fetch();
			renderArgs.put("nationalities", nationalities);

			List<ER_License_Type> licenseTypes = ER_License_Type.find("active = ? order by name", Boolean.TRUE).fetch();
			renderArgs.put("licenseTypes", licenseTypes);

			List<ER_Sex> sexList = ER_Sex.find("active = ?", Boolean.TRUE).fetch();
			renderArgs.put("sex", sexList);

			List<ER_Society_Type> societyTypes = ER_Society_Type.find("active = ? order by name", Boolean.TRUE).fetch();
			renderArgs.put("societyTypes", societyTypes);

			List<ER_Economic_Activity> economicActivities = ER_Economic_Activity.find("active = ? order by name", Boolean.TRUE).fetch();
			renderArgs.put("economicActivities", economicActivities);

			List<ER_Channel> zoneList = ER_Channel.find("isPublic = ? OR isPublic IS NULL", Boolean.FALSE).fetch();
			renderArgs.put("zoneList", zoneList);
		}
        catch(Exception e){
			Logger.error("error: " + e.getMessage());
			e.printStackTrace();
		}

    }

    //* -------- Function to load address lists boxes for individual and legal client, payer forms.-----------------------------------------------
	// --------- by default country = GUATEMALA and load DEPARTMENTS for father country. if payer is null or empty, set client parameters.--------
    private static void loadAddressCatalogs(ER_Client client, ER_Client_Payer payer) {
    	try{
		if (client != null) {
			//----------------------------------------------------CLIENT MAIL ADDRESS LISTS---------------------------------------------------------------------
			if (client.country != null && client.country.id != null) {
				renderArgs.put("departments", ER_Geographic_Area.find("id_father = ? order by name asc", client.country.id).fetch());
			} else {
				client.country = ER_Geographic_Area.findById(new Long(81)); //default country
				client.nationality = ER_Nationality.findById(new Long(81)); //default nationality if client = null
				client.countryWork = client.country;
				renderArgs.put("departments", ER_Geographic_Area.find("id_father = ? order by name asc", client.country.id).fetch()); //load departments
			}
			if (client.department != null && client.department.id != null) {
				renderArgs.put("municipalities", ER_Geographic_Area.find("id_father = ? order by name asc", client.department.id).fetch());
			}
			if (client.municipality != null && client.municipality.id != null) {
				renderArgs.put("zones", ER_Geographic_Area.find("id_father = ? order by transfer_code asc", client.municipality.id).fetch());
			}
			//----------------------------------------------------CLIENT PAYMENT ADDRESS LISTS---------------------------------------------------------------------
			if (client.countryWork != null && client.countryWork.id != null) {
				renderArgs.put("workDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", client.countryWork.id).fetch());
			} else {//----Load departments with Guatemala by default, if client has not already...
				renderArgs.put("workDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", client.country.id).fetch());
			}
			if (client.workDepartment != null && client.workDepartment.id != null) {
				renderArgs.put("workMunicipalities", ER_Geographic_Area.find("id_father = ? order by name asc", client.workDepartment.id).fetch());
			}
			if (client.workMunicipality != null && client.workMunicipality.id != null) {
				renderArgs.put("workZones", ER_Geographic_Area.find("id_father = ? order by transfer_code asc", client.workMunicipality.id).fetch());
			}
			//----------------------------------------------------CLIENT LEGAL REPRESENTATIVE ADDRESS BUSINESS LIST------------------------------------------------
			if (client.legalRepresentative != null && client.legalRepresentative.country != null && client.legalRepresentative.country.id != null) {
				renderArgs.put("legalDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", client.legalRepresentative.country.id).fetch());
			} else {//Load legalDepartments with Guatemala by default, if client has not already...
				renderArgs.put("legalDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", client.country.id).fetch());
			}
			if (client.legalRepresentative != null && client.legalRepresentative.department != null && client.legalRepresentative.department.id != null) {
				renderArgs.put("legalMunicipalities", ER_Geographic_Area.find("id_father = ? order by name asc", client.legalRepresentative.department.id).fetch());
			}
			if (client.legalRepresentative != null && client.legalRepresentative.municipality != null && client.legalRepresentative.municipality.id != null) {
				renderArgs.put("legalZones", ER_Geographic_Area.find("id_father = ? order by transfer_code asc", client.legalRepresentative.municipality.id).fetch());
			}
		}
		if (payer != null) {
			//-----------------------------------------------------------------------PAYER ADDRESS MAIL LISTS --------------------------------------------------------
			if (payer.country != null && payer.country.id != null) {
				renderArgs.put("payerDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", payer.country.id).fetch());
			}
			if (payer.department != null && payer.department.id != null) {
				renderArgs.put("payerMunicipalities", ER_Geographic_Area.find("id_father = ? order by name asc", payer.department.id).fetch());
			}
			if (payer.municipality != null && payer.municipality.id != null) {
				renderArgs.put("payerZones", ER_Geographic_Area.find("id_father = ? order by name asc", payer.municipality.id).fetch());
			}
			//----------------------------------------------------PAYER PAYMENT ADDRESS LISTS---------------------------------------------------------------------
			if (payer.countryWork != null && payer.countryWork.id != null) {
				renderArgs.put("payerWorkDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", payer.countryWork.id).fetch());
			}
			if (payer.workDepartment != null && payer.workDepartment.id != null) {
				renderArgs.put("payerWorkMunicipalities", ER_Geographic_Area.find("id_father = ? order by name asc", payer.workDepartment.id).fetch());
			}
			if (payer.workMunicipality != null && payer.workMunicipality.id != null) {
				renderArgs.put("payerWorkZones", ER_Geographic_Area.find("id_father = ? order by transfer_code asc", payer.workMunicipality.id).fetch());
			}
			//----------------------------------------------------PAYER LEGAL REPRESENTATIVE ADDRESS BUSINESS LIST-----------------------------------------------
			if (payer.legalRepresentativePayer != null && payer.legalRepresentativePayer.country != null && payer.legalRepresentativePayer.country.id != null) {
				renderArgs.put("payerLegalDepartments", ER_Geographic_Area.find("id_father = ? order by name asc", payer.legalRepresentativePayer.country.id).fetch());
			}
			if (payer.legalRepresentativePayer != null && payer.legalRepresentativePayer.department != null && payer.legalRepresentativePayer.department.id != null) {
				renderArgs.put("payerLegalMunicipalities", ER_Geographic_Area.find("id_father = ? order by name asc", payer.legalRepresentativePayer.department.id).fetch());
			}
			if (payer.legalRepresentativePayer != null && payer.legalRepresentativePayer.municipality != null && payer.legalRepresentativePayer.municipality.id != null) {
				renderArgs.put("payerLegalZones", ER_Geographic_Area.find("id_father = ? order by transfer_code asc", payer.legalRepresentativePayer.municipality.id).fetch());
			}
			//----------------------------------------------------------------------------------------------------------------
		}

		}
		catch(Exception e){
			Logger.error("error: " + e.getMessage());
			e.printStackTrace();
		}
	}
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void saveClient(ER_Client client, ER_Client_Payer payer, ER_Legal_Representative legalRepresentative,
                                    ER_Legal_Representative legalRepresentativePayer, ER_Vehicle vehicle, ER_Payment payment,
                                    Long clientId, Long incidentId, String accion, String emissionZone, File dpi,
                                    File receiptServices, File circulationCard, File driverLicence, File carInvoiceNew,
                                    File anotherCompanyPolicy, File[] depositReceipt, File authorizationForm,
                                    File scanPatents, File scanLegalRepresentativeAppointment, File dpiLegalRepresentative,
                                    File rtu, File receiptServicesLegal, Integer activeTab,ER_Client_PEP clientPEP,ER_Client_PayerPEP clientPayerPEP) {
    	flash.discard();
    	if(validation.hasErrors()){
    		params.flash();
    		validation.keep();
    		editClient(clientId, incidentId, activeTab);
    	}else{
    		ER_Client currentClient  = ER_Client.findById(clientId);
    		if(canViewClient(client)){
                ER_Incident incident = ER_Incident.findById(incidentId);
                try{
                	//-------------------Only save data if exist -------------------------------------------------------------------------------------------------------------
                    payer.ConvertUpper();
                    legalRepresentative.ConvertUpper();
                    legalRepresentativePayer.ConvertUpper();
					clientPEP.ConvertUpper();
					clientPayerPEP.ConvertUpper();
                    if((payer.taxNumber != null && !payer.taxNumber.isEmpty())){
						client.useDataClientPayer = false;
						client.payer = payer;
					}
					if((legalRepresentative.passport != null && !legalRepresentative.passport.isEmpty()) | (legalRepresentative.identificationDocument != null && !legalRepresentative.identificationDocument.isEmpty())) {
						client.legalRepresentative = legalRepresentative;
					}
					if((legalRepresentativePayer.passport != null && !legalRepresentativePayer.passport.isEmpty()) | (legalRepresentativePayer.identificationDocument != null && !legalRepresentativePayer.identificationDocument.isEmpty())){
						client.payer.legalRepresentativePayer = legalRepresentativePayer;
                    }
                    if(client.expose != null && client.expose) {
                    	if (clientPEP.typeOfrelationship.equals("Parentesco") || clientPEP.typeOfrelationship.equals("Asociado")) {
							clientPEP.relationshipIsPep = true;
						    if (!clientPEP.relationship.equals("Otro"))
						    	clientPEP.specificRelationship = null;
                    	}
                    	else
							clientPEP.relationshipIsPep = false;
                        client.clientPEP = clientPEP;
                    }
					if(payer.expose != null && payer.expose) {
						if (clientPayerPEP.typeOfrelationship.equals("Parentesco") || clientPayerPEP.typeOfrelationship.equals("Asociado")) {
							clientPayerPEP.relationshipIsPep = true;
							if (!clientPayerPEP.relationship.equals("Otro"))
								clientPayerPEP.specificRelationship = null;
						}
						else
							clientPayerPEP.relationshipIsPep = false;
						payer.clientPayerPEP = clientPayerPEP;
					}
					//--------------------------------------------------------------------------------------------------------------------------------
                    Date defaultValue = null;
    				DateConverter converter = new DateConverter(defaultValue);
    				ConvertUtils.register(converter, Date.class);
					BeanUtils.copyProperties(currentClient, client);
					
					currentClient.multimedia.uploadedFilesGD = false;
		        	currentClient.multimedia.urlDPI = dpi != null ? saveFile(dpi) : currentClient.multimedia.urlDPI;
		        	currentClient.multimedia.urlReceiptServices = receiptServices != null ? saveFile(receiptServices) : currentClient.multimedia.urlReceiptServices;
		        	currentClient.multimedia.urlCirculationCard = circulationCard != null ? saveFile(circulationCard) : currentClient.multimedia.urlCirculationCard;
		        	currentClient.multimedia.urlDriverLicence = driverLicence != null ? saveFile(driverLicence) : currentClient.multimedia.urlDriverLicence;
		        	currentClient.multimedia.urlCarInvoiceNew = carInvoiceNew != null ? saveFile(carInvoiceNew) : currentClient.multimedia.urlCarInvoiceNew;
		        	currentClient.multimedia.urlAnotherCompanyPolicy = anotherCompanyPolicy != null ? saveFile(anotherCompanyPolicy) : currentClient.multimedia.urlAnotherCompanyPolicy;
		        //	currentClient.multimedia.urlDepositReceipt = depositReceipt != null ? saveFile(depositReceipt) : currentClient.multimedia.urlDepositReceipt;
		        	currentClient.multimedia.urlAuthorizationForm = authorizationForm != null ? saveFile(authorizationForm) : currentClient.multimedia.urlDepositReceipt;
		        	currentClient.multimedia.urlScanPatents = scanPatents != null ? saveFile(scanPatents) : currentClient.multimedia.urlScanPatents;
		        	currentClient.multimedia.urlScanLegalRepresentativeAppointment = scanLegalRepresentativeAppointment != null ? saveFile(scanLegalRepresentativeAppointment) : currentClient.multimedia.urlScanLegalRepresentativeAppointment;
		        	currentClient.multimedia.urlDPILegalRepresentative = dpiLegalRepresentative != null ? saveFile(dpiLegalRepresentative) : currentClient.multimedia.urlDPILegalRepresentative;
		        	currentClient.multimedia.urlRTU = rtu != null ? saveFile(rtu) : currentClient.multimedia.urlRTU;
                    currentClient.multimedia.urlReceiptServicesLegal = receiptServicesLegal != null ? saveFile(receiptServicesLegal) : currentClient.multimedia.urlReceiptServicesLegal;
					//Saves Extra files
					if(depositReceipt != null)
					{
						currentClient.multimedia.hasAditionalFilesGD = true;
					}
                    currentClient.save();
					for(File currentFile: depositReceipt){
						ER_Aditional_Multimedia aditional_multimedia = new ER_Aditional_Multimedia();
						aditional_multimedia.multimedia = currentClient.multimedia;
						aditional_multimedia.uploaded= false;
						aditional_multimedia.urlFile = saveFile(currentFile);
						aditional_multimedia.save();
					}
					if(vehicle != null && vehicle.id != null){
						ER_Vehicle currentVehicle = ER_Vehicle.findById(vehicle.id);
						vehicle.ConvertUpper();
						BeanUtils.copyProperties(currentVehicle, vehicle);
						vehicle.save();
					}
		    		if(incident.payment != null){
		    			BeanUtils.copyProperties(incident.payment, payment);
		    		}else{
		    			incident.payment = payment;
		    		}
		    		incident.emissionZone = emissionZone;
		    		incident.save();

                    String completeAction = Messages.get("client.edit.complete") != null ? Messages.get("client.edit.complete") : "Guardar y Salir";
                    String partialAction = Messages.get("client.edit.partial") != null ? Messages.get("client.edit.partial") : "Guardar Parcial";
                    if(completeAction.equals(accion)){
                        incident.status = ER_Incident_Status.find("byCode", ERConstants.INCIDENT_STATUS_INDICTED).first();
                        incident.save();
                    }else {
                        if(incident.status.code == ERConstants.INCIDENT_STATUS_CREATED){
                            incident.status = ER_Incident_Status.find("byCode", ERConstants.INCIDENT_STATUS_IN_PROGRESS).first();
                            incident.save();
                        }
                        if(partialAction.equals(accion)) {
                            editClient(clientId, incidentId, activeTab);
                        }
                    }
                }catch(Exception e){
                	Logger.error("error: " + e.getMessage());
                    e.printStackTrace();
                }
	    		flash.success(Messages.get("client.edit.success"));
	    		incidentDetail(incidentId);
    		}
    	}
    }

	private static String saveFile(File file){
		try{
			if(file != null){
				String files = Play.applicationPath.getAbsolutePath() + "/tmpFiles/";
				File directory = new File(files);
				if(!directory.exists()){
					directory.mkdirs();
				}
				String fileName = files + UUID.randomUUID().toString().replaceAll("-", "");
				fileName += file.getName().substring(file.getName().lastIndexOf("."), file.getName().length()) ;
				File newFile = new File(fileName);
				file.renameTo(newFile);
				
				return fileName;
			}
		}catch(Exception e){
			Logger.error("error: " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static void inspection(Long clientId, Long incidentId) {
    	ER_Incident incident = ER_Incident.findById(incidentId);
    	List<ER_Inspection_Type> types = ER_Inspection_Type.find("order by typeOrder ASC").fetch();
    	renderArgs.put("types",types);
    	
    	render(incident);
    }
    
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static void saveInspection(Long clientId, Long incidentId) {
    	ER_Incident incident = ER_Incident.findById(incidentId);
    	ER_Inspection inspection = new ER_Inspection();
    	
    	render(incident, inspection);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Quotation discount
	 * ************************************************************************************************************************
	 */    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static BigDecimal getAuthorizedDiscount(ER_Product product, BigDecimal carValue, BigDecimal primeDiscount) {
    	BigDecimal authorizedDiscount = BigDecimal.ZERO;
    	if (product.discountedByRange) {
    		ER_Product_Discount_Range discount = ER_Product_Discount_Range.find("product = ? and lowRange<=? and (highRange>? or highRange is null)",product, carValue, carValue).first();
    		if (discount!=null) {
    			authorizedDiscount = discount.value;
    		}
    	} else {
    		ER_User_Role role = connectedUser().role;
    		if (role.code.intValue() == ERConstants.USER_ROLE_SUPER_ADMIN) {
    			role = ER_User_Role.find("code = ?", ERConstants.USER_ROLE_COMMERCIAL_MANAGER).first();
    		}
    		
    		ER_Product_Discount discount = ER_Product_Discount.find("product = ? and role = ?", product, role).first();
        	if (discount!=null) {
        		authorizedDiscount = discount.value;
        	}
    	}
    	
    	BigDecimal left = new BigDecimal(ERConstants.MAX_DISCOUNT).subtract(primeDiscount);
    	if (authorizedDiscount.compareTo(left)>0) {
    		authorizedDiscount = left;
    	}
    	
    	return authorizedDiscount;
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static BigDecimal getAuthorizedDiscount(ER_Quotation quotation) {
    	return getAuthorizedDiscount(quotation.product, quotation.carValue, quotation.quotationDetail.getPrimeDiscount());
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void incidentQuotationDiscount(Long id) {
    	
    	if (id!=null) {
    		ER_Quotation quotation = ER_Quotation.findById(id);
    		if (quotation!=null && canViewIncident(quotation.incident)) {
    			BigDecimal authorizedDiscount = getAuthorizedDiscount(quotation);
    	    	render(quotation, authorizedDiscount);
    		}
    	}
    	
    	incidentsList(null, null);
    }
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
    public static void saveQuotationDiscount(@Required Long quotationId, @Required BigDecimal discount) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		validation.keep();
    		flash.error(Messages.get("incident.quotation.discount.error"));
    		if (quotationId!=null)
    			incidentQuotationDiscount(quotationId);
    		else
    			incidentsList(null, null);
    	} else {
    		ER_Quotation quotation = ER_Quotation.findById(quotationId);
        	if (quotation!=null && quotation.incident.canModifyQuotations()) {
        		BigDecimal authorizedDiscount = getAuthorizedDiscount(quotation);
        		if (discount.compareTo(authorizedDiscount)>0) {
        			flash.error(Messages.get("incident.quotation.discount.unauthorized", StringFormatExtensions.decimalFormat(authorizedDiscount.doubleValue()) + "%%"));
        			incidentQuotationDiscount(quotationId);
        		}
        		
        		//Save history of discounts
        		if (quotation.discount== null || quotation.discount.compareTo(discount)!=0) {
        			quotation.discountAuthorizedUser = connectedUser();
        			quotation.discountDate = new Date();
        		}
        		
        		quotation.applyDiscount(discount);
        		quotation.save();
        		flash.success(Messages.get("incident.quotation.discount.success"));
        		incidentQuotationDiscount(quotation.id);
        	}
    	}
    }

    /*
   	 * ************************************************************************************************************************
   	 * Quotation viewing
   	 * ************************************************************************************************************************
   	 */
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void viewQuotationPDF(Long id,Integer index) {
    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	ER_Quotation quotation = ER_Quotation.findById(id);
    	
    	if (quotation!=null && quotation.incident!=null && canViewIncident(quotation.incident)) {
    		//Set the size of the PDF to Letter portrait
        	Options options = new Options();
        	options.pageSize = IHtmlToPdfTransformer.LETTERP;
        	options.filename = "COTIZACION_" + index.toString() + "_CASO_" + quotation.incident.number;
        	
        	String additionalBenefits = "";
        	
        	if (quotation.product.additionalBenefits != null && !"".equals(quotation.product.additionalBenefits)) {
        		additionalBenefits = quotation.product.additionalBenefits;
        	} else {
        		additionalBenefits = configuration.additionalBenefits;
        	}
        	String[] additionalBenefitsArray = additionalBenefits.split("[\r\n]+");
        	
        	renderPDF("Forms/Quotation.html", options, quotation, configuration, additionalBenefits,additionalBenefitsArray);
    	}
    }
    
    /*
	 * ************************************************************************************************************************
	 * Quotation resend
	 * ************************************************************************************************************************
	 */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void resendQuotation(Long id) {
    	
    	if (id!=null) {
    		ER_Quotation quotation = ER_Quotation.findById(id);
    		if (quotation!=null && canViewIncident(quotation.incident) && !quotation.incident.isFinalized()) {
    			ER_Incident incident = quotation.incident;
    			if (!incident.isFinalized()) {
	    			List<ByteArrayOutputStream> streamArray = new ArrayList<ByteArrayOutputStream>();
	    			streamArray.add(quotationPDFData(quotation));
		    	
	    			boolean result = Mails.quotations(incident, streamArray, false);
	    			if (result) {
	    				flash.success(Messages.get("incident.quotation.send.success"));
	    			} else {
	    				flash.error(Messages.get("incident.quotation.send.error"));
						ER_Exceptions exceptions = new ER_Exceptions();
						exceptions.description = Messages.get("incident.quotation.send.error");
						exceptions.exceptionDate = new Date();
						exceptions.quotation = quotation;
                        exceptions.active = 1;
						exceptions.save();
	    			}
	    			incidentDetail(incident.id);
    			}
    		}
    	}
    	
    	incidentsList(null, null);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Genera los datos de la cotización de PDF
	 * ************************************************************************************************************************
	 */
    private static ByteArrayOutputStream quotationPDFData(ER_Quotation quotation) {
    	ER_General_Configuration configuration = ER_General_Configuration.find("").first();
    	
    	String additionalBenefits = "";
    	if (quotation.product.additionalBenefits != null && !"".equals(quotation.product.additionalBenefits)) {
    		additionalBenefits = quotation.product.additionalBenefits;
    	} else {
    		additionalBenefits = configuration.additionalBenefits;
    	}

		Logger.debug("Additional benefits: %s", additionalBenefits);

    	String[] additionalBenefitsArray = additionalBenefits.split("[\r\n]+");
    	
    	//Set the size of the PDF to Letter portrait
    	Options options = new Options();
    	options.pageSize = IHtmlToPdfTransformer.LETTERP;
    	
    	PDF.MultiPDFDocuments docs = new PDF.MultiPDFDocuments();
    	docs.add("Forms/Quotation.html", options);
    	ByteArrayOutputStream os = new ByteArrayOutputStream();
    	PDF.writePDF(os, docs, quotation, configuration, additionalBenefitsArray, additionalBenefits);
    	
    	return os;
    }
    
    public static void getFile(String filePath){
		renderBinary(new File(filePath));
	}
    
    
    @Check({"Usuario Final"})
    public static void requestSupport(Long incidentId){
    	ER_Incident incident = ER_Incident.findById(incidentId);
    	
    	Mails.requestSupport(incident);
    	
    	flash.success(Messages.get("incident.support.request.send"));
    	incidentDetail(incident.id);
    }
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void sendPolicyByMail(Long incidentId){
		ER_Incident incident = ER_Incident.findById(incidentId);

		Mails.sendPolicy(incident);

		flash.success("En un momento le estará llegando su póliza a su correo.");
		incidentDetail(incident.id);
	}
    //Gets the difference in days between two dates
	public static int differenceBetweenDates(Date fecha1, Date fecha2){
		long startTime = fecha1.getTime();
		long endTime = fecha2.getTime();
		long diffTime = endTime - startTime;
		long diffDays = diffTime / (1000 * 60 * 60 * 24);
		return (int)diffDays;
	}
    
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void generatePolicy(	Long incidentId, 
    									Boolean sendClient,
    									Boolean sendPayer,
    									Boolean sendPolicy,
    									Boolean sendVehicle,
    									Boolean sendCoverages,
    									Boolean sendPrimes,
    									Boolean sendPayment,
    									Boolean sendWorkFlow,
    									Boolean sendQueryVehicle){
    	try{
    	ER_Incident incident = ER_Incident.findById(incidentId);
			Logger.info("Ingresa a generar poliza del caso: " + incident.number);
			ER_Incident_Status incidentStatusIncomplete = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_INCOMPLETE).first();
    	if(incident.status.code == ERConstants.INCIDENT_STATUS_INSPECTION || incident.status.code == ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION || incident.status.code == ERConstants.INCIDENT_STATUS_INCOMPLETE){
    		flash.error("Error: La inspección no se ha completado.");
            ER_Exceptions exceptions = new ER_Exceptions();
            exceptions.description = "Error: La inspección no se ha completado.";
            exceptions.exceptionDate = new Date();
            exceptions.quotation = incident.selectedQuotation;
            exceptions.active = 1;
            exceptions.save();
    		incidentDetail(incident.id);
    	}
    	if(!isValidateFields(incident)){
    		incidentDetail(incident.id);
    	}
        //check code agent
        ER_General_Configuration configuration = ER_General_Configuration.find("").first();
        if(FieldAccesor.isEmptyOrNull(configuration.agentCodeAS400)){
            if(FieldAccesor.isEmptyOrNull(incident, "creator.profile.agentCode")){
                flash.error("Error: Usuario no tiene codigo de agente asignado.");
                ER_Exceptions exceptions = new ER_Exceptions();
                exceptions.description = "Error: Usuario no tiene codigo de agente asignado.";
                exceptions.exceptionDate = new Date();
                exceptions.quotation = incident.selectedQuotation;
                exceptions.active = 1;
                exceptions.save();
                incident.status = incidentStatusIncomplete;
                incident.save();
                incidentDetail(incident.id);
            }
        }
    	//Valida nuevamente los 6 pagos y cobrador
		ER_Payment_Frecuency frecuency = incident.selectedPaymentFrecuency;
		if(frecuency.numberOfPayments > 6 &&  incident.payment.chargeType.id == 5) {
			flash.error("No puede seleccionar medio de pago cobrador y número de pagos mayor a 6, favor modificar.");
			incidentDetail(incident.id);
		}
		ER_Quotation quotation = ER_Quotation.findById(incident.selectedQuotation.id);
    	if(quotation.carValue != null && quotation.carValue.compareTo(BigDecimal.ZERO)>0){
			if(incident.vehicle.isNew == null || (incident.vehicle.isNew != null && !incident.vehicle.isNew)) {
				if(incident.inspection == null || incident.inspection.inspectionDate == null){
					flash.error("Error: El vehiculo debe ser nuevo para no requerir inspección, por favor coloque fecha de inspección.");
					ER_Exceptions exceptions = new ER_Exceptions();
					exceptions.description = "Error: El vehiculo debe ser nuevo para no requerir inspección, por favor coloque fecha de inspección.";
					exceptions.exceptionDate = new Date();
					exceptions.quotation = incident.selectedQuotation;
					exceptions.active = 1;
					exceptions.save();
					incident.status = incidentStatusIncomplete;
					incident.save();
					incidentDetail(incident.id);
				}
				int days = differenceBetweenDates(incident.inspection.inspectionDate,incident.policyValidity);
				if (days>15 || days<0) {
					flash.error("Error: La vigencia de póliza no se encuentra dentro de los 15 días posteriores a inspección.");
					ER_Exceptions exceptions = new ER_Exceptions();
					exceptions.description = "Error: La vigencia de póliza no se encuentra dentro de los 15 días posteriores a inspección.";
					exceptions.exceptionDate = new Date();
					exceptions.quotation = incident.selectedQuotation;
					exceptions.active = 1;
					exceptions.save();
					incident.status = incidentStatusIncomplete;
					incident.save();
					incidentDetail(incident.id);
				}
				int daysFromToday = differenceBetweenDates(incident.inspection.inspectionDate, new Date());
				if (daysFromToday > 15 || daysFromToday < 0) {
					flash.error("Error: La fecha actual no se encuentra dentro de los 15 días posteriores a inspección.");
					ER_Exceptions exceptions = new ER_Exceptions();
					exceptions.description = "Error: La fecha actual no se encuentra dentro de los 15 días posteriores a inspección.";
					exceptions.exceptionDate = new Date();
					exceptions.quotation = incident.selectedQuotation;
					exceptions.active = 1;
					exceptions.save();
					incident.status = incidentStatusIncomplete;
					incident.save();
					incidentDetail(incident.id);
				}
			}
			else{
				if(incident.vehicle.invoiceDate != null) {
					int days = differenceBetweenDates(incident.vehicle.invoiceDate, incident.policyValidity);
					if (days > 30 || days < 0) {
						flash.error("Error: La vigencia de poliza no se encuentra dentro de los 30 días posteriores a factura de vehículo.");
						ER_Exceptions exceptions = new ER_Exceptions();
						exceptions.description = "Error: La vigencia de poliza no se encuentra dentro de los 30 días posteriores a factura de vehículo.";
						exceptions.exceptionDate = new Date();
						exceptions.quotation = incident.selectedQuotation;
						exceptions.active = 1;
						exceptions.save();
						incident.status = incidentStatusIncomplete;
						incident.save();
						incidentDetail(incident.id);
					}
					int daysFromToday = differenceBetweenDates(incident.vehicle.invoiceDate, new Date());
					if (daysFromToday > 30 || daysFromToday < 0) {
						flash.error("Error: La fecha actual no se encuentra dentro de los 30 días posteriores a factura de vehículo.");
						ER_Exceptions exceptions = new ER_Exceptions();
						exceptions.description = "Error: La fecha actual no se encuentra dentro de los 30 días posteriores a factura de vehículo.";
						exceptions.exceptionDate = new Date();
						exceptions.quotation = incident.selectedQuotation;
						exceptions.active = 1;
						exceptions.save();
						incident.status = incidentStatusIncomplete;
						incident.save();
						incidentDetail(incident.id);
					}

				}
				else {
					flash.error("Error: No se encuentra la fecha de factura de vehículo");
					ER_Exceptions exceptions = new ER_Exceptions();
					exceptions.description = "Error: No se encuentra la fecha de factura de vehículo";
					exceptions.exceptionDate = new Date();
					exceptions.quotation = incident.selectedQuotation;
					exceptions.active = 1;
					exceptions.save();
					incident.status = incidentStatusIncomplete;
					incident.save();
					incidentDetail(incident.id);
				}
			}
		}
		else{
			int daysFromToday = differenceBetweenDates(incident.policyValidity, new Date());
			if (daysFromToday > 8 || daysFromToday < -8) {
				flash.error("Error: La fecha actual no se encuentra dentro de los 8 días posteriores a vigencia de poliza.");
				ER_Exceptions exceptions = new ER_Exceptions();
				exceptions.description = "Error: La fecha actual no se encuentra dentro de los 8 días posteriores a vigencia de poliza.";
				exceptions.exceptionDate = new Date();
				exceptions.quotation = incident.selectedQuotation;
				exceptions.active = 1;
				exceptions.save();
				incident.status = incidentStatusIncomplete;
				incident.save();
				incidentDetail(incident.id);
			}
		}

    	
    	if(sendClient != null && sendClient){
    		if(incident.client.isIndividual != null && !incident.client.isIndividual){
    			ER_Transaction_Status transaction = incident.getTransaction(BusinessClientRequest.TRANSACTION);
    			if(!transaction.complete){
    				BusinessClientRequest businessClientRequest = createRequestService.createBusinessClientRequest(incident);
        			transaction.updateFromResponse(policyServiceBus.sendBusinessClient(businessClientRequest));
        			if(!transaction.complete){
        				incident.merge();
        				flash.error("DATOS CLIENTE EMPRESARIAL -> AS400: " + transaction.message);
                        ER_Exceptions exceptions = new ER_Exceptions();
                        exceptions.description = "DATOS CLIENTE EMPRESARIAL -> AS400: " + transaction.message;
                        exceptions.exceptionDate = new Date();
                        exceptions.quotation = incident.selectedQuotation;
                        exceptions.active = 1;
                        exceptions.save();
                        incident.status = incidentStatusIncomplete;
                        incident.save();
                        incidentDetail(incident.id);
    				}
    			}
    		}else if(incident.client.isIndividual == null || incident.client.isIndividual){
    			ER_Transaction_Status transaction = incident.getTransaction(PersonClientRequest.TRANSACTION);
    			if(!transaction.complete){
    				PersonClientRequest personClientRequest = createRequestService.createPersonClientRequest(incident);
        			transaction.updateFromResponse(policyServiceBus.sendPersonClient(personClientRequest));
        			if(!transaction.complete){
        				incident.merge();
        				flash.error("DATOS CLIENTE INDIVIDUAL -> AS400: " + transaction.message);
                        ER_Exceptions exceptions = new ER_Exceptions();
                        exceptions.description = "DATOS CLIENTE INDIVIDUAL -> AS400: " + transaction.message;
                        exceptions.exceptionDate = new Date();
                        exceptions.quotation = incident.selectedQuotation;
                        exceptions.active = 1;
                        exceptions.save();
                        incident.status = incidentStatusIncomplete;
                        incident.save();
						incidentDetail(incident.id);
    				}
    			}
    		}
    	}
    	
    	if(sendPayer != null && sendPayer){
    		ER_Transaction_Status transaction = incident.getTransaction(PayerRequest.TRANSACTION);
			if(!transaction.complete){
				transaction.updateFromResponse(policyServiceBus.sendDataPayer(createRequestService.createPayerRequest(incident)));
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS PAGADOR -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS PAGADOR -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}
    	}
    	
    	if(sendVehicle != null && sendVehicle){
    		ER_Transaction_Status transaction = incident.getTransaction(VehicleRequest.TRANSACTION);
			if(!transaction.complete){
				transaction.updateFromResponse(policyServiceBus.sendDataVehicle(createRequestService.createVehicleRequest(incident)));
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS VEHICULO -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS VEHICULO -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}
    	}
    	
    	PolicyResponse policyResponse = null;
    	if(sendPolicy != null && sendPolicy){
    		ER_Transaction_Status transaction = incident.getTransaction(PolicyRequest.TRANSACTION);
			if(!transaction.complete){
				policyResponse = policyServiceBus.sendDataPolicy(createRequestService.createPolicyRequest(incident));
				transaction.updateFromResponse(policyResponse);
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS POLIZA -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS POLIZA -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}else{
				policyResponse = transaction.getObjectResponseFromXML(PolicyResponse.class);
			}
    	}

        ER_Incident_Status incidentStatus = ER_Incident_Status.find("code = ?", ERConstants.INCIDENT_STATUS_FINALIZED).first();


    	if(sendCoverages != null && sendCoverages){
    		ER_Transaction_Status transaction = incident.getTransaction(CoveragesRequest.TRANSACTION);
			if(!transaction.complete){
				transaction.updateFromResponse(policyServiceBus.sendListCoverages(createRequestService.createCoveragesRequest(incident)));
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS COBERTURAS -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS COBERTURAS -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}
    	}
    	
    	if(sendPrimes != null && sendPrimes){
    		ER_Transaction_Status transaction = incident.getTransaction(PrimeRequest.TRANSACTION);
			if(!transaction.complete){
				transaction.updateFromResponse(policyServiceBus.sendPrimeList(createRequestService.createPrimeRequest(incident, policyResponse)));
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS PRIMA -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS PRIMA -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}
    	}
    	
    	if(sendPayment != null && sendPayment){
    		ER_Transaction_Status transaction = incident.getTransaction(PaymentMethodRequest.TRANSACTION);
			if(!transaction.complete){
				transaction.updateFromResponse(policyServiceBus.sendPaymentMethod(createRequestService.createPaymentMethodRequest(incident)));
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS FORMA DE PAGO -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS FORMA DE PAGO -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}
    	}
    	
    	if(sendWorkFlow != null && sendWorkFlow){
    		ER_Transaction_Status transaction = incident.getTransaction(WorkFlowRequest.TRANSACTION);
			if(!transaction.complete){
				transaction.updateFromResponse(policyServiceBus.sendDataWorkFlow(createRequestService.createWorkFlowRequest(incident)));
				if(!transaction.complete){
					incident.merge();
    				flash.error("DATOS WORKFLOW -> AS400: " + transaction.message);
                    ER_Exceptions exceptions = new ER_Exceptions();
                    exceptions.description = "DATOS WORKFLOW -> AS400: " + transaction.message;
                    exceptions.exceptionDate = new Date();
                    exceptions.quotation = incident.selectedQuotation;
                    exceptions.active = 1;
                    exceptions.save();
                    incident.status = incidentStatusIncomplete;
                    incident.save();
    				incidentDetail(incident.id);
				}
			}
    	}
		if(policyResponse != null){
			incident.branch = policyResponse.getBranch();
			incident.policy = policyResponse.getPolicy();
			incident.policyFileName = policyResponse.getFilename();
			incident.policyFileDownload = Boolean.FALSE;
			incident.status = incidentStatus;
			incident.save();
		}
    	incident.status = incidentStatus;
        incident.save();
    	incident.merge();
    	//Delete all the exceptions
        List <ER_Exceptions> Exceptions = ER_Exceptions.find("quotation_Id = ?", incident.selectedQuotation.getId()).fetch();
        for (ER_Exceptions except:Exceptions){
            except.active = 0;
            except.save();
        }
        //Generate all pdf to TMP and then to drive.
		List<ER_Form> availForms = ER_Form.find("order by name asc").fetch();
        //Constantes de formularios
		//1=IVE, 2=Solicitud Auto, 7=IVE JURIDICO, 8= PEP 9 = pagador PEP
		for(ER_Form form: availForms) {
			  if(form.id == 1 || form.id == 2 ||  form.id == 7 ||  form.id == 8 ||  form.id == 9)
			  {
				if  ((form.id == 9 ||  form.id == 8) && incident.client.expose == false){
					continue;
				}
				if(form.id==7 && incident.client.isIndividual == true)
				    continue;
				  if (form!=null && !form.parameters.isEmpty()) {
					  List<ER_Incident_Parameter> parameters = ER_Incident_Parameter.find("incident.id = :i AND parameter IN (:p)").bind("p", form.parameters).bind("i", incidentId).fetch();

					  if (parameters!=null) {
						  //Put the parameters in the renderArgs with the parameter identifier as the key
						  for (ER_Incident_Parameter parameter : parameters) {
							  renderArgs.put(parameter.parameter.identifier, parameter.getValueString());
						  }
					  }
				  }
				  //Set the size of the PDF to Letter portrait
				  Options options = new Options();
				  options.filename = form.name;
				  if (form.landscape) {
					  options.pageSize = IHtmlToPdfTransformer.LETTERL;
				  } else {
					  options.pageSize = IHtmlToPdfTransformer.LETTERP;
				  }

				  renderArgs.put("incident", incident);
				  renderArgs.put( "configuration", configuration);
				  //Render the PDF

				  String files = Play.applicationPath.getAbsolutePath() + "/tmpFiles/";
				  Logger.info("ruta a guardar files de forms: " +files);
				  File directory = new File(files);
				  if(!directory.exists()){
					  directory.mkdirs();
				  }
				  String generatedFileName = files + "Caso-" + incident.number + "-" +form.name + ".pdf";
				  PDF.MultiPDFDocuments docs = new PDF.MultiPDFDocuments();
				  docs.add(form.templatePath, options);
				  File filePDF = new File(generatedFileName);
				  writePDF(filePDF,docs,incident,configuration, options);
				  ER_Client currentClient = incident.client;
				  //Formulario IVE individual
				  if (form.id == 1 && incident.client.isIndividual == true){
					  currentClient.multimedia.urlFormIVE = generatedFileName;
				  }
				  //Formulario solicitud Auto
				  else if (form.id == 2){
					  currentClient.multimedia.urlformRequestAuto = generatedFileName;
				  }
				  //Formulario PEP
				  else if(form.id == 8){
					  currentClient.multimedia.urlFormPEP= generatedFileName;
				  }
                  //Formulario IVE JURIDICO
                  else if(form.id == 7){
                      currentClient.multimedia.urlFormIVE = generatedFileName;
                  }
				  //Formulario Pagador PEP
				  else if(form.id == 9){
					  currentClient.multimedia.urlFormPayerPEP = generatedFileName;
				  }
				  currentClient.save();
			  }
		}

        //Send success message to client
		Mails.welcomePolicyGenerated(incident, policyResponse.getPolicy());
    	flash.put("GeneratePolicySuccess",policyResponse.getPolicy());
    	incidentDetail(incident.id);
	}
		catch(Exception e){
		Logger.error("error: " + e.getMessage());
		e.printStackTrace();
	}
    }
    
    private static boolean isValidateFields(ER_Incident incident){
    	boolean isValid = true;

			StringBuilder errors = new StringBuilder("CAMPOS DE CLIENTE REQUERIDOS:");
		if(!checkClientsByIdExist(incident.client.taxNumber + "|" + incident.client.identificationDocument)) {
			if (incident.client.isIndividual != null && !incident.client.isIndividual) {
				errors = new StringBuilder("CAMPOS DE CLIENTE JURIDICO REQUERIDOS:");
				for (String field : fieldsBusinessClient()) {
					if (FieldAccesor.isEmptyOrNull(incident.client, field)) {
						errors.append(Messages.get("quotation.form.business.client." + field) + ", ");
						isValid = false;
					}
				}
				if (!isValid) {
					flash.error(errors.toString());
					return isValid;
				}
				errors = new StringBuilder("CAMPOS DE REPRESENTANTE LEGAL REQUERIDOS:");
				for (String field : fieldsLegalRepresentative()) {
					if (FieldAccesor.isEmptyOrNull(incident.client.legalRepresentative, field)) {
						errors.append(Messages.get("quotation.form.legalRepresentative." + field) + ", ");
						isValid = false;
					}
				}
			} else if (incident.client.isIndividual == null || incident.client.isIndividual) {
				for (String field : fieldsClient()) {
					if (FieldAccesor.isEmptyOrNull(incident.client, field)) {
						errors.append(Messages.get("quotation.form.client." + field) + ", ");
						isValid = false;
					}
				}
			}
			if (!isValid) {
				flash.error(errors.toString());
				return isValid;
			}

			if (incident.client.useDataClientPayer == null || !incident.client.useDataClientPayer) {
				errors = new StringBuilder("CAMPOS DE PAGADOR REQUERIDOS:");
				if (incident.client.payer == null) {
					incident.client.payer = new ER_Client_Payer();
				}
				if (incident.client.payer.isIndividual != null && !incident.client.payer.isIndividual) {
					errors = new StringBuilder("CAMPOS DE PAGADOR JURIDICO REQUERIDOS:");
					for (String field : fieldsBusinessClient()) {
						if (FieldAccesor.isEmptyOrNull(incident.client.payer, field)) {
							errors.append(Messages.get("quotation.form.payer." + field) + ", ");
							isValid = false;
						}
					}
				} else {
					for (String field : fieldsClient()) {
						if (FieldAccesor.isEmptyOrNull(incident.client.payer, field)) {
							errors.append(Messages.get("quotation.form.payer." + field) + ", ");
							isValid = false;
						}
					}
				}
				if (!isValid) {
					flash.error(errors.toString());
					return isValid;
				}
			}

		}
			errors = new StringBuilder("CAMPOS DE VEHICULO REQUERIDOS:");
			for (String field : fieldsVehicle()) {
				if (FieldAccesor.isEmptyOrNull(incident.vehicle, field)) {
					errors.append(Messages.get("quotation.form.vehicle." + field) + ", ");
					isValid = false;
				}
			}
			if (!isValid) {
				flash.error(errors.toString());
				return isValid;
			}

			return isValid;
    }

    /*
     * ************************************************************************************************************************
     * Client editing
     * ************************************************************************************************************************
     */
    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void editMail(Long clientId, Long incidentId) {
        if(clientId != null){
            ER_Incident incident = ER_Incident.findById(incidentId);
            ER_Client client = incident.client;
            if(canViewClient(client)){
                render(client, incident, clientId, incidentId);
            }
        }
        incidentDetail(incidentId);
    }

    @Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
    public static void saveMail(ER_Client client, Long clientId, Long incidentId) {
        flash.discard();
        if(validation.hasErrors()){
            params.flash();
            validation.keep();
            editMail(clientId, incidentId);
        }else{
            ER_Client currentClient  = ER_Client.findById(clientId);
            if(canViewClient(currentClient)){
                try{
                    currentClient.email = client.email;
                    currentClient.additionalEmails = client.additionalEmails;
                    currentClient.save();
                }catch(Exception e){
                    e.printStackTrace();
                }
                flash.success(Messages.get("client.edit.success"));
                incidentDetail(incidentId);
            }
        }
    }
    
    private static List<String> fieldsClient(){
    	List<String> fields = new ArrayList<String>();
    	fields.add("taxNumber");
    	fields.add("firstName");
    	fields.add("firstSurname");
    	fields.add("birthdate");
    	fields.add("sex");
    	fields.add("profession");
    	//fields.add("identificationDocument");
    	fields.add("civilStatus");
    	fields.add("nationality");
    	fields.add("licenseType");
    	fields.add("licenseNumber");
    	fields.add("email");
    	fields.add("country");
//    	fields.add("department");
//    	fields.add("municipality");
//    	fields.add("zone");
    	fields.add("phoneNumber1");
    	fields.add("countryWork");
//    	fields.add("workDepartment");
//    	fields.add("workMunicipality");
//    	fields.add("workZone");
    	fields.add("phoneNumberWork1");
    	
    	return fields;
    }
    
    private static List<String> fieldsBusinessClient(){
    	List<String> fields = new ArrayList<String>();
    	fields.add("societyType");
    	fields.add("companyName");
    	fields.add("businessName");
    	fields.add("economicActivity");
    	fields.add("nationality");
    	//fields.add("identificationDocument");
    	fields.add("email");
    	fields.add("stateProvider");
    	fields.add("writeNumber");
    	fields.add("writeDate");
    	fields.add("registrationDate");
    	fields.add("country");
    	fields.add("phoneNumber1");
    	
    	return fields;
    }
    
    private static List<String> fieldsLegalRepresentative(){
    	List<String> fields = new ArrayList<String>();
    	fields.add("taxNumber");
    	fields.add("firstName");
    	fields.add("firstSurname");
    	fields.add("birthdate");
    	fields.add("sex");
    	fields.add("profession");
    	//fields.add("identificationDocument");
    	fields.add("civilStatus");
    	fields.add("nationality");
    	fields.add("email");
    	fields.add("registry");
    	fields.add("caseFile");
    	fields.add("extendedIn");
    	fields.add("registrationDate");
    	fields.add("book");
    	fields.add("folio");
    	fields.add("country");
    	fields.add("phoneNumber3");
    	
    	return fields;
    }
    
    private static List<String> fieldsVehicle(){
    	List<String> fields = new ArrayList<String>();
    	fields.add("line");
    	fields.add("type");
    	fields.add("erYear");
    	fields.add("reminderType");
    	fields.add("plateType");
//    	fields.add("plate");
    	fields.add("chassis");
    	fields.add("engine");
    	fields.add("numberOfPassengers");
    	fields.add("color");
//    	fields.add("cubicCentimeter");
//    	fields.add("numberCylinders");
//    	fields.add("numberAxes");
    	fields.add("numberDoor");
//    	fields.add("helmetClass");
    	fields.add("mileage");
    	fields.add("typeMileage");
    	fields.add("fuelType");
//    	fields.add("tonnage");
    	
    	return fields;
    }


    /**
     * Method to find existing clients
     * @param information
     */
    public static void getClientsById(String information) {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("success", false);

        String[] data = information.split("\\|");
		QueryClientRequest request = new QueryClientRequest();
		if (data.length > 0 && !StringUtil.isNullOrBlank(data[0])) {
			request.setTaxNumber(data[0].replace(" ", ""));
		}
		if (data.length > 1 && !StringUtil.isNullOrBlank(data[1])) {
			request.setIdentificationDocument(data[1].replace(" ", ""));
		}
		QueryClientResponse queryClients = clientQueryServiceBus.clientQuery(request);
		if(queryClients != null){
			if (!queryClients.getMessage().contains("no existe")) {
				response.put("success", true);
				if (queryClients.getClients() != null && queryClients.getClients().size() > 1) {
					response.put("clientList", queryClients.getClients());
				} else if (queryClients.getClients() != null && !queryClients.getClients().isEmpty()) {
					response.put("client", queryClients.getClients().get(0));
				} else {
					response.put("client", queryClients);
				}
			}
		}

        renderJSON(response);
    }

	public static boolean checkClientsByIdExist(String information) {
		String[] data = information.split("\\|");
		QueryClientRequest request = new QueryClientRequest();
		if (data.length > 0 && !StringUtil.isNullOrBlank(data[0])) {
			request.setTaxNumber(data[0].replace(" ", ""));
		}
		if (data.length > 1 && !StringUtil.isNullOrBlank(data[1])) {
			request.setIdentificationDocument(data[1].replace(" ", ""));
		}
		QueryClientResponse queryClients = clientQueryServiceBus.clientQuery(request);
		if(queryClients != null){
			if (!queryClients.getMessage().contains("no existe")) {
				if (queryClients.getClients() != null && queryClients.getClients().size() > 1) {
					return true;
				} else if (queryClients.getClients() != null && !queryClients.getClients().isEmpty()) {
					return true;
				} else {
					return true;
				}
			}
		}
		return false;
	}

    public static void getPersonDetails(String information) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);

        String[] data = information.split("\\|");
        if(data.length > 0){
            if ("P".equals(data[0].replace(" ", ""))) {
                QueryPersonDetailRequest request = new QueryPersonDetailRequest();
                if (data.length > 1)
                	request.setClientCode(data[1].replace(" ", ""));
                if (data.length > 2)
                	request.setClientCif(data[2]);
                QueryPersonDetailResponse personDetail = personServiceBus.queryPersonDetail(request);
                if (personDetail != null) {
                    PersonDetailDTO personDetailDTO = PersonDetailDTO.loadData(personDetail);
                    response.put("success", true);
                    response.put("person", personDetailDTO);
                }
            } else if ("J".equals(data[0].replace(" ", ""))) {
                QueryBusinessDetailRequest request = new QueryBusinessDetailRequest();
                if (data.length > 1)
                	request.setClientCode(data[1].replace(" ", ""));
                if (data.length > 2)
                	request.setClientCif(data[2]);
                QueryBusinessDetailResponse businessDetail = businessServiceBus.queryBusinessDetail(request);
                if (businessDetail != null) {
                    BusinessDetailDTO businessDetailDTO = BusinessDetailDTO.loadData(businessDetail);
                    response.put("success", true);
                    response.put("business", businessDetailDTO);
                }
            }
        }
        renderJSON(response);
    }
}



