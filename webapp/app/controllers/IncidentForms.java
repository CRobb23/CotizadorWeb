package controllers;

import static play.modules.pdf.PDF.renderPDF;
import helpers.ERConstants;
import helpers.Filter;
import helpers.GeneralMethods;
import helpers.Filter.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.*;

import org.allcolor.yahp.converter.IHtmlToPdfTransformer;

import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.GenericModel.JPAQuery;
import play.i18n.Messages;
import play.libs.Crypto;
import play.modules.paginate.ValuePaginator;
import play.modules.pdf.PDF.Options;
import play.mvc.*;

@With(Secure.class)
public class IncidentForms extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
	private static void filterIncidents(String searchField) {
		
		//Validate the parameters
		boolean valid = GeneralMethods.validateParameter(searchField);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (valid) {
			searchField = searchField.trim();
			
			filter.addGroupStart(Operator.AND);
			try {
				Long number = Long.parseLong(searchField);
				filter.addQuery("number = ?", number, Operator.OR);
			} catch (Exception e) {
				Logger.debug("Field not a number: Searching by other fields");
				filter.addQuery("concat(client.firstName,' ',client.lastName,vehicle.plate)  like ?", "%"+searchField+"%", Operator.OR);
			}
			filter.addGroupEnd();
			
		}	
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		if (filter.isValidFilter()) {
			
			filter.addGroupStart(Operator.AND);
			filter.addQuery("status.code=?", ERConstants.INCIDENT_STATUS_SOLD, Operator.OR);
			filter.addQuery("status.code=?", ERConstants.INCIDENT_STATUS_INSPECTION, Operator.OR);
			filter.addGroupEnd();
			
			ER_User connectedUser = connectedUser();
			JPAQuery query = null;

			Integer userRol = connectedUserRoleCode(connectedUser);
			if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
				
				List<Long> channelIds = null;
				channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active =true", connectedUser).fetch();
				
				query = ER_Incident.find(filter.getQuery() + " AND (channel.id IN :c OR creator = :s) order by creationDate DESC",
						filter.getParametersArray())
						.bind("c", channelIds)
						.bind("s", connectedUser);
				
			} else if (userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)) {
				
				List<Long> distributorIds = null;
				distributorIds = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d.active =true", connectedUser).fetch();
				
				query = ER_Incident.find(filter.getQuery() + " AND (distributor.id IN (:d) OR creator = :s) order by creationDate DESC",
						filter.getParametersArray())
						.bind("d", distributorIds)
						.bind("s", connectedUser);
				
			} else if (userRol.equals(ERConstants.USER_ROLE_SUPERVISOR)) {
				List<Long> userIds = ER_Store.find("select u.id from ER_Store s join s.sellers u join d.administrators a where a = ?", connectedUser).fetch();
				//Administradores
				List<Long> supervisoresIds = ER_Store.find("select a.id from ER_Store s join s.administrators a where s.distributor = ?", connectedUser.distributor).fetch();
				//Agrega lista de administradores a lista de usuarios
				userIds.addAll(supervisoresIds);


				userIds.add(connectedUser.id);
				
				query = ER_Incident.find(filter.getQuery() + " AND creator.id IN (:s) order by creationDate DESC",
						filter.getParametersArray())
						.bind("s", userIds);
				
			} else if (userRol.equals(ERConstants.USER_ROLE_SALES_MAN)) {
				
				query = ER_Incident.find(filter.getQuery() + " AND creator = :s order by creationDate DESC",
						filter.getParametersArray())
						.bind("s", connectedUser);
				
			} else {
				query = ER_Incident.find(filter.getQuery() + " order by creationDate DESC",
						filter.getParametersArray());
			}
			
			List<ER_Incident> incidentsList = query.fetch();
			ValuePaginator incidents = new ValuePaginator(incidentsList);
			incidents.setPageSize(10);
			renderArgs.put("incidents", incidents);
		}
	}
	
	/*
	 * ************************************************************************************************************************
	 * Incidents list and search
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
	@Access
	public static void incidentsList(String searchField, Boolean search) {
    	
    	params.flash();
    	
    	filterIncidents(searchField);
    	
    	ValuePaginator list = (ValuePaginator)renderArgs.get("incidents");
    	
    	if (search!=null && search && (list==null || list.size()==0)) {
    		flash.error(Messages.get("incidents.list.noentries"));
    	}
        render();
    }
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
	public static void searchIncidents(String searchField) {
    	flash.clear();
    	if (!GeneralMethods.validateParameter(searchField)) {
    		flash.error(Messages.get("incidents.list.searcherror"));
    		incidentsList(null, false);
    	} else {
    		incidentsList(searchField, true);
    	}
    	
    }
	
	/*
	 * ************************************************************************************************************************
	 * Show forms per incident
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void showAvailForms(Long incidentId) {
		
		if (canViewIncident(incidentId)) {		
			List<ER_Form> availForms = ER_Form.findAll();
	
			if(availForms != null) {
				renderArgs.put("availForms", availForms);
			}
			
			renderArgs.put("incidentId", incidentId);
			renderTemplate("IncidentForms/availForms.html");
		}
		
		incidentsList(null, null);
	}
	
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void printIncidentForm(@Required Long incidentId, @Required Long formId) {
		
		if (!validation.hasErrors()) {
			try {
				ER_Form form = ER_Form.findById(formId);
				ER_Incident incident = ER_Incident.findById(incidentId);
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
				ER_General_Configuration configuration = ER_General_Configuration.find("").first();
				renderArgs.put( "configuration", configuration);
	        	//Render the PDF
	            renderPDF("" + form.templatePath, options);
			} catch (Exception e) {
				Logger.error(e, "No se pudo mostrar el formulario");
			}
		}
	}
	
	/*
	 * ************************************************************************************************************************
	 * Show parameters for incident and form
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void editIncidentParameters(Long incidentId, Long formId) {
		renderIncidentParameters(incidentId, formId);
	}

	/*
	 * ************************************************************************************************************************
	 * Incident parameters: Busca el tipo de formulario correspondiente al incidente, luego busca los parametros que debe llenar el formulario.
	 * ************************************************************************************************************************
	 */
	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	private static void renderIncidentParameters(Long incidentId, Long formId)
	{
		List<ER_Incident_Parameter> incidentParams = new ArrayList<ER_Incident_Parameter>();

		List<Long> incidentParamsIds = new ArrayList<Long>();
		for(ER_Incident_Parameter incidentParam : incidentParams ) {
			incidentParamsIds.add(incidentParam.id);
		}

		ER_Form form = ER_Form.findById(formId);
		List<ER_Parameter> parameters = ER_Parameter.find("select p from ER_Parameter p where p.active = :a AND p IN :fp").bind("a", true).bind("fp", form.parameters).fetch();
		for (int i=parameters.size()-1; i>=0; i--) {
			ER_Parameter param = parameters.get(i);
			if (!param.active) {
				parameters.remove(i);
			}
		}
		
		ER_Incident_Parameter incidentParameter;
		for(ER_Parameter parameter : parameters) {
				List<ER_Incident_Parameter> iParams = ER_Incident_Parameter.find("parameter.id = :p AND incident_id= :i").bind("p", parameter.id).bind("i", incidentId).fetch(1);
				if(iParams == null || iParams.size() == 0) {
					incidentParameter = new ER_Incident_Parameter();
				} else {
					incidentParameter = iParams.get(0);
				}
				incidentParams.add(incidentParameter);
		}
		
		renderArgs.put("incidentId", incidentId); 
		renderArgs.put("incidentParams", incidentParams);
		renderArgs.put("formParams", parameters);
		renderArgs.put("form", form);
		
		renderTemplate("IncidentForms/editIncidentParameter.html");
	}

	@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor", "Usuario Final"})
	public static void saveIncidentParameters(@Required Long incidentId, @Required Long formId, @Valid List<ER_Incident_Parameter> incidentParams) {
		
		flash.clear();
		
		try {
			for (int i=0; i<incidentParams.size(); i++) {
				ER_Incident_Parameter param = incidentParams.get(i);
				ER_Parameter parameter = ER_Parameter.findById(param.parameter.id);
				if (parameter.type.code!= ERConstants.PARAMETER_TYPE_BOOLEAN && parameter.type.code!= ERConstants.PARAMETER_TYPE_OPTIONS) {
					validation.maxSize(param.value, parameter.maxLength);
				}
			}
		} catch (Exception e) {
			Logger.debug(e,"No parameter ids");
		}
		
		if (validation.hasErrors()) {
			//params.flash();
			Map<String, String> data = params.allSimple();
			
			for (int i=0; i<incidentParams.size(); i++) {
				ER_Incident_Parameter param = incidentParams.get(i);
				data.put("incidentParams["+i+"].value", param.value);
			}
			renderArgs.put("data", data);
			
			
			flash.error(Messages.get("parameter.create.fielderrors"));
			
			renderIncidentParameters(incidentId, formId);
		} else {
			
			ER_Incident incident;
			incident = ER_Incident.findById(incidentId);
			
			if (incident != null && incidentParams != null) {
				
				Crypto crypto = new Crypto();
				
				String paramValue;
				for(ER_Incident_Parameter incidentParameter : incidentParams)
				{
					paramValue = incidentParameter.value;
					
					try {
						ER_Parameter parameter = ER_Parameter.findById(incidentParameter.parameter.id);
						if (parameter.type.code == ERConstants.PARAMETER_TYPE_BOOLEAN) {
							String[] components = paramValue.split(",");
							if (components[0].equals("on")){
								paramValue = "1";
							} else if (components[0].equals("off")) {
								paramValue = "0";
							}
						}
						if (parameter.type.code==ERConstants.PARAMETER_TYPE_ACCOUNT) {
							paramValue = crypto.encryptAES(paramValue);
						}
					} catch (Exception e) {
						Logger.info(e, "Invalid boolean");
					}
					
					if(incidentParameter.id != null)
						incidentParameter = ER_Incident_Parameter.findById(incidentParameter.id);
					
					if(incidentParameter != null)
					{
						incidentParameter.value = paramValue;
						incidentParameter.save();
					}
				}
				flash.success(Messages.get("parameters.edit.success"));
				showAvailForms(incidentId);
			}
		}
	}

}
