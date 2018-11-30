package controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import models.*;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminChannels extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterChannels(String channel, Long adminId,Boolean active) {
		
		//Validate the parameters
		boolean validChannel= GeneralMethods.validateParameter(channel);
		boolean validAdmin= GeneralMethods.validateParameter(adminId);
		boolean validEstate= GeneralMethods.validateParameter(active);
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validChannel) {
			filter.addQuery("name like ?", "%"+channel+"%");
		}
		
		if (validAdmin) {
			filter.addQuery("administrator.id = ?", adminId, Operator.AND);
		}

		if (validAdmin) {
			filter.addQuery("active = ?", adminId, Operator.AND);
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
		ModelPaginator channels = null;
		if (filter.isValidFilter()) {			
			channels = new ModelPaginator(ER_Channel.class, filter.getQuery(), filter.getParametersArray());
		} else {
			channels = new ModelPaginator(ER_Channel.class);
		}
		
		channels.orderBy("name ASC");
		channels.setPageNumber(page);
		channels.setPageSize(10);
		renderArgs.put("channels", channels);
	}
    
    /*
	 * ************************************************************************************************************************
	 * Channels list
	 * ************************************************************************************************************************
	 */
	@Access
	public static void channelsList(String channel, Long adminId, boolean intern,Boolean active) {
		
		if (!intern) {
			session.remove("page");
    		session.remove("channel");
    		session.remove("adminId");
			session.remove("active");
    	}
		
		List<ER_User> administrators = channelAdministrators();
		filterChannels(channel, adminId,active);

        render(administrators);
    }
	
	public static void searchChannels(String channel, Long adminId, String all, boolean intern,Boolean active) {
		flash.clear();
		session.remove("page");
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
    		session.remove("channel");
    		session.remove("adminId");
    		
    		channelsList(null,null,true,null);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
	    	session.put("channel",channel);
			session.put("adminId",adminId);
			session.put("active",active);
    	}
    	channelsList(channel, adminId, true,active);
    }
	
	private static List<ER_User> channelAdministrators() {
		List<ER_User> administrators = null;
		
		if (checkRole(ERConstants.USER_ROLE_SUPER_ADMIN)) {
    		administrators = ER_User.find("role.code = ? and active = true order by firstName", ERConstants.USER_ROLE_COMMERCIAL_MANAGER).fetch();
    	} else {
    		administrators = new ArrayList<ER_User>();
    	}
		
		return administrators;
	}
	
	/*
	 * ************************************************************************************************************************
	 * Channels creation and editing
	 * ************************************************************************************************************************
	 */
   
    public static void editChannel(Long id) {
    	List<ER_User> administrators = channelAdministrators();
    	List<ER_Portfolio_Type> portfolios = ER_Portfolio_Type.findAll();

    	if (id!=null) {
    		ER_Channel channel = ER_Channel.findById(id);
    		renderArgs.put("channel", channel);
    		
        	renderArgs.put("assigned", channel.administrators);
        	
        	for (ER_User assigned : channel.administrators) {
        		if (administrators.contains(assigned)) {
        			administrators.remove(assigned);
        		}
        	}
        	
        	renderArgs.put("administrators", administrators);
        	renderArgs.put("portfolios", portfolios);
        	render();
    	} else {
    		render(administrators, portfolios);
    	}
    }
    
    public static void saveChannel(@Valid ER_Channel channel) {
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("channel.create.fielderrors"));
    		validation.keep();
    		editChannel(channel.id);
    	} else {
    		if (channel.id != null) {
    			channel.save();
    			flash.success(Messages.get("channel.edit.success"));
    		} else {
    			ER_Channel similarValue = ER_Channel.find("name = ?", channel.name).first();
    			
    			if (similarValue==null) {
    				channel.save();
        			flash.success(Messages.get("channel.create.success"));
    			} else {
    				params.flash();
    				flash.error(Messages.get("channel.create.duplicate"));
    				editChannel(channel.id);
    			}
    		}
    	}
    	
    	String sessionChannel = session.get("channel");
		Long  adminId = "".equals(session.get("adminId")) || "null".equals(session.get("adminId")) ||session.get("adminId")==null ? null : Long.parseLong(session.get("adminId"));

		if (sessionChannel != null || adminId != null) {    			
			searchChannels(sessionChannel, adminId, "", true,channel.active);
		} else {
			channelsList(null,null,true,channel.active);
		}
    	
    }

	public static void deleteChannel(Long id){


		ER_Channel channel = null;
		if (id!=null) {
			channel  = ER_Channel.findById(id);
		}
		boolean errorscaptured = false;
		try
		{
			channel.delete();
			flash.success(Messages.get("channel.delete.success"));
		}
		catch ( Exception ex )
		{
			flash.error(Messages.get("channel.delete.error"));
			errorscaptured = true;
			Logger.error(ex,"channel: %d", id);



		}
		if (errorscaptured)
		{

			String sessionChannel = session.get("channel");
			Long  adminId = "".equals(session.get("adminId")) || "null".equals(session.get("adminId")) ||session.get("adminId")==null ? null : Long.parseLong(session.get("adminId"));

			if (sessionChannel != null || adminId != null) {
				channelsList(sessionChannel, adminId, true, channel.active);
			} else {
				channelsList(null,null,true,null);
			}
		}
		else
		{
			channelsList(null,null,true,null);
		}
	}



}
