package controllers;


import com.google.gson.Gson;
import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import helpers.PasswordMethods;

import java.util.List;

import models.*;
import models.ws.rest.InspectionBrokerResponse;
import models.ws.rest.SecurityResponse;
import notifiers.Mails;
import play.Logger;
import play.data.validation.Valid;
import play.data.validation.Error;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.utils.*;
import service.InspectionService;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.inject.Inject;
import play.libs.*;

@With(Secure.class)
@Check({"Administrador Maestro", "Gerente comercial", "Gerente de canal"})
public class AdminUsers extends AdminBaseController {


	@Inject
	static InspectionService inspectionService;

	/*
	 * ************************************************************************************************************************
	 * Filters the users and adds the result as a ModelPaginator object with the key "users"
	 * ************************************************************************************************************************
	 */
	
	private static void filterUsers(String email, String firstName, String lastName, Long role, Boolean active) {
		
		//Validate the parameters
		boolean validEmail = GeneralMethods.validateParameter(email);
		boolean validFirstName = GeneralMethods.validateParameter(firstName);
		boolean validLastName = GeneralMethods.validateParameter(lastName);
		boolean validRole = GeneralMethods.validateParameter(role);
		boolean validState = GeneralMethods.validateParameter(active);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validEmail) {
			filter.addQuery("email like ?", "%"+email+"%");
		}
		
		if (validFirstName) {
			filter.addQuery("firstName like ?", "%"+firstName+"%", Operator.AND);
		}
		
		if (validLastName) {
			filter.addQuery("lastName like ?", "%"+lastName+"%", Operator.AND);
		}
		
		if (validRole) {
			filter.addQuery("role.id = ?", role, Operator.AND);
		}
		
		if (validState) {
			filter.addQuery("active = ?", active, Operator.AND);
		}
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
		if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			filter.addQuery("channel = ?", connectedUser.channel, Operator.AND);
		} else if (!userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN)) {
			filter.addQuery("channel = ?", connectedUser.channel, Operator.AND);
			filter.addQuery("distributor = ?", connectedUser.distributor, Operator.AND);
		}
		
		List<ER_User_Role> roles = authorizedRolesForUser();
		if (!userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) && !roles.isEmpty()) {
			filter.addGroupStart(Operator.AND);
			for (ER_User_Role userRole : roles) {
				filter.addQuery("role = ?", userRole, Operator.OR);
			}
			filter.addGroupEnd();
		}
		
		//Exclude current user from results
		ER_User user = ER_User.find("byEmail", Security.connected()).first();
		if (user!=null) {
			filter.addQuery("id != ?", user.id, Operator.AND);
			filter.addQuery("email != ?", "admin@elroble.com", Operator.AND);
		}
		
		int page;
		if (params.get("page") == null) {
			// verifying if it was in other page
			if (session.get("page") == null) {
				page = 1;
			} else {
				page = Integer.parseInt(session.get("page"));
			}
		} else {
			page = Integer.parseInt(params.get("page"));
			session.put("page", page);
		}
		
		//Validate the filter to create the model paginator. If the filter is invalid, all users are returned.
		ModelPaginator users = null;
		if (filter.isValidFilter()) {			
			users = new ModelPaginator(ER_User.class, filter.getQuery(), filter.getParametersArray());
			users.setPageNumber(page);
			users.setPageSize(10);
			renderArgs.put("users", users);
		}
	}
	
	/*
	 * ************************************************************************************************************************
	 * Users lists
	 * ************************************************************************************************************************
	 */

    public static void usersList(String email, String firstName, String lastName, Long role, Boolean active, boolean intern) {
    	
    	if (!intern) {
    		session.remove("page");
    		session.remove("email");
    		session.remove("firstName");
    		session.remove("lastName");
    		session.remove("role");
    		session.remove("active");
    		flash.clear();
    	}else{
    		params.flash();
    	}
    	
    	List<ER_User_Role> roles = authorizedRolesForUser();
    	renderArgs.put("roles", roles);
    	
    	//Add filtered users to the renderArgs
    	filterUsers(email, firstName, lastName, role, active);
    	
    	//Add the http parameters to the flash scope
//    	params.flash();
    	
    	//Render the list
    	render();
    }
    
    public static void search(String email, String firstName, String lastName, Long role, Boolean active, String all, boolean intern, boolean internalSearch) {
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//List all users if the all button is pressed
    	if (!"".equals(all) && all!=null) {
    		session.remove("email");
    		session.remove("firstName");
    		session.remove("lastName");
    		session.remove("role");
    		session.remove("active");
    		
    		usersList(null, null, null, null, null,true);
    	}
    	
    	//Render the list
    	if(!intern) {
	    	session.put("email",email);
			session.put("firstName",firstName);
			session.put("lastName",lastName);
			session.put("role",role == null ? null : role);
			session.put("active", active == null ? "todos" : active.booleanValue());
    	}
    	usersList(email, firstName, lastName, role, active,true);
    }
    
    /*
	 * ************************************************************************************************************************
	 * User creation and editing
	 * ************************************************************************************************************************
	 */
    
    public static void editUser(Long userId) {
    	//Add the user roles to the render args
		List<ER_User_Role> roles = authorizedRolesForUser();
    	renderArgs.put("roles", roles);
    	
    	Long channelId = null;
		Integer userRol = connectedUserRoleCode(connectedUser());
    	if (userId!=null) {
    		ER_User user = ER_User.findById(userId);
    		if (user!=null && canModifyRole(user.role)) {
    			renderArgs.put("user", user);
    		}
    		
    		if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN)) {
    			if (user.channel!=null) {
    				channelId = user.channel.id;
    			}
        	}
    	}
    	
    	if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
    		channelId = connectedUserChannelId();
    	}
    	
    	if (channelId!=null) {
    		List<ER_Distributor> distributors = ER_Distributor.find("active = true and channel.id = ?", channelId).fetch();
			renderArgs.put("distributors", distributors);
    	}
    	
    	if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN)) {
    		List<ER_Channel> channels = ER_Channel.find("active = true order by name").fetch();
    		renderArgs.put("channels", channels);
    	}

		InspectionBrokerResponse brokerList = inspectionService.listExternalBrokers();
    	if (brokerList.getBrokerList() != null) {
    		renderArgs.put("brokers", brokerList.getBrokerList());
		}
    	
    	//Render the template
        render();
    }
    
    public static void saveUser(ER_User user, String reset, String agentCode, Integer phoneNumber,Boolean isQAUser,Boolean isCommercialQAUser,Boolean isCaseAnalyst) {
		Logger.info("*ENTRO ACA saveUser");
    	flash.clear();
    	
    	boolean uniqueUser = true;
		Integer userRol = connectedUserRoleCode(connectedUser());
    	if (user.id==null){
    		//Check if user is unique
    		ER_User emailUser = ER_User.find("email=?", user.email).first();
    		if (emailUser!=null) {
    			uniqueUser = false;
    		}
    	} else {
    		ER_User emailUser = ER_User.find("email=? and id != ?", user.email, user.id).first();
    		if (emailUser!=null) {
    			uniqueUser = false;
    		}
    	}
    	
    	if(validation.hasErrors() || !uniqueUser) {
			for(Error error : validation.errors()) {
				Logger.error(error.message());
			}
			Logger.error("Es unico = "+ uniqueUser);

    		flash.error(Messages.get("user.create.fielderrors"));
    		
    		//Add the parameters to the flash scope
    		params.flash();
    		validation.keep();
    		
    		//Add an error to the email field if the user is not unique
    		if (!uniqueUser) {
    			validation.addError("user.email", Messages.get("user.create.emailnotunique", user.email));
    		}

    		editUser(user.id);
    	} else {
    		
	    	try {
	    		
	    		if (!canModifyRole(user.role)) {
	    			flash.error(Messages.get("user.create.notauthorized"));
	        		
	        		//Add the parameters to the flash scope
	        		params.flash();
	        		validation.keep();
	        		
	        		editUser(user.id);
	    		}
	    		
	    		//Set user channel
	    		if (user.channel!=null && userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN)) {
	        		user.channel = ER_Channel.findById(user.channel.id);
	        	} else {
	        		user.channel = connectedUser().channel;
	        	}

	        	//Set QA USER
				if(isQAUser == null)
				{
                    isQAUser = Boolean.FALSE;
				}
				user.setQAUser(isQAUser);

				//Set Commercial QA USER
				if(isCommercialQAUser == null)
				{
					isCommercialQAUser = Boolean.FALSE;
				}
				user.setCommercialQAUser(isCommercialQAUser);

				//Set Case Analyst QA USER
				if(isCaseAnalyst == null)
				{
					isCaseAnalyst = Boolean.FALSE;
				}
				user.setCaseAnalyst(isCaseAnalyst);

				//Set user distributor
	    		if (user.distributor!=null && (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) ||
						userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER))) {
	        		user.distributor = ER_Distributor.findById(user.distributor.id);
	        	} else {
	        		user.distributor = connectedUser().distributor;
	        	}
	    		
		    	if (user.id == null) {
		    		//Set user password  charlie123@elroble.com  83AKmJm6*  nuevapass: 83AKmJm7*
					String code = PasswordMethods.getCode();
					Logger.info("***********CONTRASEÑA sin BASE 64:"+code);
					Base64.Encoder encoder = Base64.getEncoder();
					String code_encript = encoder.encodeToString(code.getBytes(StandardCharsets.UTF_8) );
				  	user.password = code;
					//user.password = code_encript;
					Logger.info("***********CONTRASEÑA EN BASE 64:"+code_encript);
		    		//Send the email to the user
					//if (Mails.welcome(user, user.password)) {
                    String WS_URL = Play.configuration.getProperty("ssoUrl");
                    String WS_APPNAME = Play.configuration.getProperty("appName");
                    WS.WSRequest wsRequest = WS.url(WS_URL+"/ws/usuarios/crearUsuario");
                    wsRequest.setParameter("email",user.email);
                    wsRequest.setParameter("password","NADA"); // ESTO no lo deberias usar.
					 /* YA NO VOY A HACER NADA CON LA CONTRASEÑA, QUE la genere el SSO para poder usar la funcion Guardar y envio de corre
					 * dese el SSO */
                    wsRequest.setParameter("aplicacion",WS_APPNAME);
                    wsRequest.setParameter("firstName",user.firstName);
                    wsRequest.setParameter("lastName",user.lastName);
                    wsRequest.setParameter("profile_id",87l);

                    String response = wsRequest.get().getString();
                    SecurityResponse securityResponse;
                    securityResponse = new Gson().fromJson(response, SecurityResponse.class);

                    Logger.info("SERA QUE FUNCION? "+response );
					Logger.info("If:"+securityResponse.getMensaje()+" T/F:"+ securityResponse.getMensaje().equals("ok"));
                    if(securityResponse.getMensaje().equals("ok")){
						Logger.info("ENTRO EN    EL IF");
                    	user.save();
                        flash.success(Messages.get("user.create.success"));
                    }

		    	} else {
					Logger.debug("ENTRO EN EL ELSE");
					/*Logica va a ser:
					* sin importar yo llamo a mi funcion enviando el usuario, perfil, activo/inactivo
					* el plan seria que el SSO vea si esta cambiando el estatus y entonces poner o quitar el perfil.
					* */
					String WS_URL = Play.configuration.getProperty("ssoUrl");
					String WS_APPNAME = Play.configuration.getProperty("appName");
					WS.WSRequest wsRequest = WS.url(WS_URL+"/ws/usuarios/ActualizarEstatusUsuarioCotizador");
					wsRequest.setParameter("email",user.email);
					wsRequest.setParameter("profile_id",87l);
					wsRequest.setParameter("active", user.active);
					String response = wsRequest.get().getString();
					SecurityResponse securityResponse;
					securityResponse = new Gson().fromJson(response, SecurityResponse.class);
					Logger.debug("Retorno respons:"+securityResponse.getMensaje());
					user.save();
		    		flash.success(Messages.get("user.edit.success"));
		    	}

                try {
                    ER_User_Profile userProfile = user.profile;
                    if (userProfile!=null) {
                        userProfile.agentCode = agentCode;
                        userProfile.phoneNumber = phoneNumber;
						userProfile.save();
                    } else {
                        userProfile = new ER_User_Profile();
						userProfile.agentCode = agentCode;
						userProfile.phoneNumber = phoneNumber;
                        userProfile.save();
                        user.profile = userProfile;
                        user.save();
                    }
                    flash.success(Messages.get("profile.userprofile.success"));
                } catch (Exception e) {
                    flash.error(Messages.get("profile.userprofile.error"));
                    Logger.error(e, "Error displaying user profile");
                }
		    	
	    	} catch (Exception e) {
	    		Logger.error(e, "Error saving user");
	    		if (user.id==null) {
	    			flash.error(Messages.get("user.create.error"));
	    		} else {
	    			flash.error(Messages.get("user.edit.error"));
	    		}
	    		
	    		params.flash();
	    		editUser(user.id);
	    	}
	    	
	    	String email = session.get("email");
    		String firstName = session.get("firstName");
    		String lastName = session.get("lastName");
    		Long  role = "".equals(session.get("role")) || "null".equals(session.get("role")) ||session.get("role")==null ? null : Long.parseLong(session.get("role"));
    		Logger.info("valor de activo:"+user.active);
    		Boolean active = "todos".equals(session.get("active")) || null == session.get("active") ? null : Boolean.parseBoolean(session.get("active"));
			if (email != null || firstName != null || lastName != null || role != null || active != null) {
    			search(email, firstName, lastName, role, active, "",true,true);
    		} else {
    	    	usersList(null, null, null, null, null,true);
    		}
    		
	    }
    	
    }

	public static void signOffUser(Long id){
		ER_User  tuser = ER_User.findById(id);
		ER_Channel channel = null;
		if (id!=null) {
			tuser  = ER_User.findById(id);
		}
		boolean errorscaptured = false;
		try
		{
			tuser.token = null;
			tuser.save();
			flash.success(Messages.get("user.edit.signOffSuccess"));
		}
		catch ( Exception ex )
		{
			flash.error(Messages.get("user.edit.error"));
			errorscaptured = true;
			Logger.error(ex,"user: %d", id);
		}
		if (errorscaptured)
		{
			String email = session.get("email");
			String firstName = session.get("firstName");
			String lastName = session.get("lastName");
			Long  role = "".equals(session.get("role")) || "null".equals(session.get("role")) ||session.get("role")==null ? null : Long.parseLong(session.get("role"));
			Boolean active = "todos".equals(session.get("active")) || null == session.get("active") ? null : Boolean.parseBoolean(session.get("active"));
			if (email != null || firstName != null || lastName != null || role != null || active != null) {
				System.out.print("paso 4 " + errorscaptured);
				usersList(email, firstName,lastName,role,active,true);
			} else {
				usersList(null, null, null, null, null,true);
			}
		}
		else
		{
			usersList(null, null, null, null, null,true);
		}
	}


	/** Charlie Robb estoy creando esto con el objetivo de cambiar la logica de borar usuarios por desactivar usaurios.
	 *   No queremos perder la data generada por el usuario dentro del cotizador y desactivandolo se va a evitar el accesso.
	 */
    public static void deactivateUser(Long id){
		ER_User  tuser = ER_User.findById(id);
		ER_Channel channel = null;
		if (id!=null) {
			tuser  = ER_User.findById(id);
		}
		boolean errorscaptured = false;
		Logger.info("ACTIVO?:"+tuser.getActive());
		tuser.setActive(false);
		Logger.info("LO DESACTIVE?:"+tuser.getActive());
		try
		{
			tuser.save();
			flash.success(Messages.get("user.delete.success"));
		}
		catch ( Exception ex )
		{
			flash.error(Messages.get("user.delete.error"));
			errorscaptured = true;
			Logger.error(ex,"user: %d", id);
		}
		if(errorscaptured){
			String email = session.get("email");
			String firstName = session.get("firstName");
			String lastName = session.get("lastName");
			Long  role = "".equals(session.get("role")) || "null".equals(session.get("role")) ||session.get("role")==null ? null : Long.parseLong(session.get("role"));
			Boolean active = "todos".equals(session.get("active")) || null == session.get("active") ? null : Boolean.parseBoolean(session.get("active"));
			if (email != null || firstName != null || lastName != null || role != null || active != null) {
				System.out.print("paso 4 " + errorscaptured);
				usersList(email, firstName,lastName,role,active,true);
			} else {
				usersList(null, null, null, null, null,true);
			}
		}else{
			usersList(null, null, null, null, null,true);
		}

	}


	public static void deleteUser(Long id){
		ER_User  tuser = ER_User.findById(id);
		ER_Channel channel = null;
		if (id!=null) {
			tuser  = ER_User.findById(id);
		}
		boolean errorscaptured = false;
		try
		{
			tuser.delete();
			flash.success(Messages.get("user.delete.success"));
		}
		catch ( Exception ex )
		{
			flash.error(Messages.get("user.delete.error"));
			errorscaptured = true;
			Logger.error(ex,"user: %d", id);
		}
		if (errorscaptured)
		{
			String email = session.get("email");
			String firstName = session.get("firstName");
			String lastName = session.get("lastName");
			Long  role = "".equals(session.get("role")) || "null".equals(session.get("role")) ||session.get("role")==null ? null : Long.parseLong(session.get("role"));
			Boolean active = "todos".equals(session.get("active")) || null == session.get("active") ? null : Boolean.parseBoolean(session.get("active"));
			if (email != null || firstName != null || lastName != null || role != null || active != null) {
				System.out.print("paso 4 " + errorscaptured);
				usersList(email, firstName,lastName,role,active,true);
			} else {
				usersList(null, null, null, null, null,true);
			}
		}
		else
		{
			usersList(null, null, null, null, null,true);
		}
	}
}
