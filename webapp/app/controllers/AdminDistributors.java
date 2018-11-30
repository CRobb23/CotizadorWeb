package controllers;

import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.ER_Channel;
import models.ER_Distributor;
import models.ER_User;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;


@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial"})
public class AdminDistributors extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterDistributors(Long channelId, String distributor,Boolean active) {
		
		//Validate the parameters
		boolean validChannel= GeneralMethods.validateParameter(channelId);
		boolean validDistributor= GeneralMethods.validateParameter(distributor);

		boolean validState = GeneralMethods.validateParameter(active);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validChannel) {
			filter.addQuery("channel.id = ?", channelId);
		}
		
		if (validDistributor) {
			filter.addQuery("name like ?", "%"+distributor+"%", Operator.AND);
		}
		
		if (checkRole(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			filter.addQuery("channel = ?", connectedUser().channel, Operator.AND);
		}
		if (validState) {
			filter.addQuery("active = ?", active, Operator.AND);
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
		
		//Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
		ModelPaginator distributors = null;
		if (filter.isValidFilter()) {			
			distributors = new ModelPaginator(ER_Distributor.class, filter.getQuery(), filter.getParametersArray());
		} else {
			distributors = new ModelPaginator(ER_Distributor.class);
		}
		
		distributors.orderBy("name ASC");
		distributors.setPageNumber(page);
		distributors.setPageSize(10);
		renderArgs.put("distributors", distributors);
	}
	
	/*
	 * ************************************************************************************************************************
	 * Distributors list
	 * ************************************************************************************************************************
	 */
	
	private static void addUserChannelArg() {
		if (checkRole(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			ER_User user = connectedUser();
			renderArgs.put("userChannel", user.channel);
		}
	}
	
	private static void addChannelsArg() {
		if (checkRole(ERConstants.USER_ROLE_SUPER_ADMIN)) {
			List<ER_Channel> channels = ER_Channel.find("active = true order by name").fetch();
			renderArgs.put("channels", channels);
		}
	}
	
	private static void addDistributorAdministratorsArg(Long channelId, List<ER_User> assigned) {
		List<ER_User> administrators = null;
		
		if (channelId!=null) {
			administrators = ER_User.find("role.code = ? and channel.id = ? and active = true order by firstName", ERConstants.USER_ROLE_CHANNEL_MANAGER, channelId).fetch();
			for (ER_User user : assigned) {
    		if (administrators.contains(user)) {
    			administrators.remove(user);
    		}
    	}
		}
		
		renderArgs.put("administrators", administrators);
	}

	@Access
    public static void distributorsList(Long channelId, String distributor, boolean intern,Boolean active) {
    	if (!intern) {
    		session.remove("page");
    		session.remove("channelId");
    		session.remove("distributor");
			session.remove("active");
    		//flash.clear();
    	}else{
    		params.flash();
    	}
    	
		filterDistributors(channelId, distributor,active);
		
		addChannelsArg();
		
        render();
    }
	
	public static void searchDistributors(Long channelId, String distributor, String all, boolean intern, boolean internalSearch,Boolean active) {
		//flash.clear();
		
		if(!internalSearch){
    		session.remove("page");
    	}
		
    	//List all users if the all button is pressed
    	if (!"".equals(all) && all!=null) {
    		session.remove("channelId");
    		session.remove("distributor");
			session.remove("active");
    		
    		distributorsList(null,null,true,null);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("channelId",channelId);
			session.put("distributor",distributor);
			session.put("active",distributor)
			;
    	}
    	distributorsList(channelId, distributor,true,active);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Distributors creation and editing
	 * ************************************************************************************************************************
	 */
    
    public static void editDistributor(Long id) {
    	List<ER_User> administrators = Collections.emptyList();
    	
    	ER_Distributor distributor = null;
    	if (id!=null) {
    		distributor = ER_Distributor.findById(id);
    		if (checkRole(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
    			if (distributor.channel.equals(connectedUser().channel)) {
    				renderArgs.put("distributor", distributor);
    			}
    		} else {
    			renderArgs.put("distributor", distributor);
    		}
    		renderArgs.put("assigned", distributor.administrators);
    		administrators = distributor.administrators;
    	}
    	
    	if (checkRole(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
    		addDistributorAdministratorsArg(connectedUserChannelId(),administrators);
    	} else {
    		if (flash.contains("distributor.channel.id")) {
        		String channelFlash = flash.get("distributor.channel.id");
        		if (checkRole(ERConstants.USER_ROLE_SUPER_ADMIN) && !channelFlash.isEmpty()) {
            		addDistributorAdministratorsArg(Long.parseLong(channelFlash),administrators);
            	}
        	} else {
        		if (distributor!=null) {
        			addDistributorAdministratorsArg(distributor.channel.id,administrators);
        		}
        	}
    	}
    	
    	addChannelsArg();
    	addUserChannelArg();
    	
        render();
    }

	public static void saveDistributor(@Valid ER_Distributor distributor) {
    	//flash.clear();
    	System.out.print(" save 1");
    	if (validation.hasErrors()) {
			System.out.print(" save 2");
    		params.flash();
    		flash.error(Messages.get("channel.create.fielderrors"));
    		validation.keep();
    		editDistributor(distributor.id);
    	} else {
			System.out.print(" save 3");
    		if (checkRole(ERConstants.USER_ROLE_COMMERCIAL_MANAGER) && canAccessChannel(distributor.channel)) {
    			distributor.channel = connectedUser().channel;
    		}
			System.out.print(" save 31");
    		if (distributor.id != null) {
    			distributor.save();
    			flash.success(Messages.get("distributor.edit.success"));
    		} else {
				System.out.print(" save 32");
    			ER_Distributor similarValue = ER_Distributor.find("name = ? and channel.id = ?", distributor.name, distributor.channel.id).first();
    			
    			if (similarValue==null) {
    				distributor.save();
        			flash.success(Messages.get("distributor.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("distributor.create.duplicate"));
    				editDistributor(distributor.id);
    			}
    		}
    	}
		System.out.println(" save 4");
    	Long  channelId = "".equals(session.get("channelId")) || "null".equals(session.get("channelId")) ||session.get("channelId")==null ? null : Long.parseLong(session.get("channelId"));
    	String sessionDistributor = session.get("distributor");
		System.out.println(" save 5");
		if (channelId != null || sessionDistributor != null) {
			System.out.print(" save 7");
			searchDistributors(channelId, sessionDistributor,"",true,true,distributor.active);
		} else {
	    	distributorsList(null, null, true,null);
			System.out.print(" save 6");
		}

    }

	public static void deleteDistributor(Long id){

		System.out.print("paso 1");

		ER_Distributor distributor = null;
		if (id!=null) {
			distributor = ER_Distributor.findById(id);
		}
		System.out.print("paso 2");
		boolean errorscaptured = false;
		try
		{
			distributor.delete();
			flash.success(Messages.get("distributor.delete.success"));
		}
		catch ( Exception ex )
		{
			flash.error(Messages.get("distributor.delete.error"));
			errorscaptured = true;
			Logger.error(ex,"distribudor: %d", id);

		}
		System.out.print("paso 3 " + errorscaptured);
		if (errorscaptured)
		{
			Long  channelId = "".equals(session.get("channelId")) || "null".equals(session.get("channelId")) ||session.get("channelId")==null ? null : Long.parseLong(session.get("channelId"));
			String sessionDistributor = session.get("distributor");
			System.out.print("paso 4 " + errorscaptured);
			distributorsList(channelId, sessionDistributor, true,distributor.active);

		}
		else
		{

			distributorsList(null, null, true,null);
		}
	}

}
