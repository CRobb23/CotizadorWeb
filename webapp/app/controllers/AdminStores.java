package controllers;

import java.util.ArrayList;
import java.util.List;


import helpers.ERConstants;
import helpers.Filter;
import helpers.GeneralMethods;
import helpers.Filter.Operator;
import models.*;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class  	AdminStores extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Filters
	 * ************************************************************************************************************************
	 */
	
	private static void filterStores(Long channelId, Long distributorId, String store, Boolean active ) {
		
		//Validate the parameters
		boolean validChannel= GeneralMethods.validateParameter(channelId);
		boolean validDistributor= GeneralMethods.validateParameter(distributorId);
		boolean validStore= GeneralMethods.validateParameter(store);
		boolean validState = GeneralMethods.validateParameter(active);
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		
		if (validChannel) {
			filter.addQuery("distributor.channel.id = ?", channelId);
		}
		
		if (validDistributor) {
			filter.addQuery("distributor.id = ?", distributorId, Operator.AND);
		}
		
		if (validStore) {
			filter.addQuery("name like ?", "%"+store+"%", Operator.AND);
		}
		if (validState) {
			filter.addQuery("active = ?", active, Operator.AND);
		}
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);

		if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			filter.addQuery("distributor.channel = ?", connectedUser.channel, Operator.AND);
		} else if (userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)) {
			filter.addQuery("distributor = ?", connectedUser.distributor, Operator.AND);
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
		ModelPaginator stores = null;
		if (filter.isValidFilter()) {			
			stores = new ModelPaginator(ER_Store.class, filter.getQuery(), filter.getParametersArray());
		} else {
			stores = new ModelPaginator(ER_Store.class);
		}
		
		stores.orderBy("name ASC");
		stores.setPageNumber(page);
		stores.setPageSize(10);
		renderArgs.put("stores", stores);
	}
	
	/*
	 * ************************************************************************************************************************
	 * Stores list
	 * ************************************************************************************************************************
	 */
    @Access
    public static void storesList(Long channelId, Long distributorId, String store, boolean intern,Boolean active) {
    	if (!intern) {
    		session.remove("page");
    		session.remove("channelId");
    		session.remove("distributorId");
    		session.remove("store");
			session.remove("active");
    	}else{
    		params.flash();
    	}
    	
		filterStores(channelId, distributorId, store,active);
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
		if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN)) {
			if (channelId!=null) {
				List<ER_Distributor> distributors = ER_Distributor.find("active = true and channel.id = ? order by name", channelId).fetch();
				renderArgs.put("distributors", distributors);
			}
		} else if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			List<ER_Distributor> distributors = ER_Distributor.find("active = true and channel.id = ? order by name", connectedUser.channel.id).fetch();
			renderArgs.put("distributors", distributors);
		}
		
		List<ER_Channel> channels = ER_Channel.find("active = true order by name").fetch();
        render(channels);
    }
	
	public static void searchStores(Long channelId, Long distributorId, String store, String all, boolean intern, boolean internalSearch,Boolean active) {
		//flash.clear();
		
		if(!internalSearch){
    		session.remove("page");
    	}
		
    	//List all if the all button is pressed
		if (!"".equals(all) && all!=null) {
			session.remove("channelId");
    		session.remove("distributorId");
    		session.remove("store");
			session.remove("active");
    		System.out.println("pase por storesList(null,null,null,true,true)");
			storesList(null, null, null, true, null);
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("channelId",channelId);
			session.put("distributorId",distributorId);
			session.put("store",store);
			session.put("active",active);
			System.out.println("active ?" + active );
			System.out.println("pase por storesList(channelId, distributorId, store, true,active);");
    	}
    	storesList(channelId, distributorId, store, true,active);
    }
	
	/*
	 * ************************************************************************************************************************
	 * Distributors creation and editing
	 * ************************************************************************************************************************
	 */
	
	private static void addUserChannelArg() {
		Integer userRol = connectedUserRoleCode(connectedUser());
		if (userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)) {
			ER_User user = connectedUser();
			renderArgs.put("userDistributor", user.distributor);
		}
	}
    
	private static void addFilteredSupervisorsAndSellers(Long distributorId, ER_Store store) {
		List<ER_User> administrators = null;
		List<ER_User> sellers = null;
		if (distributorId!=null) {
			administrators = ER_User.find("role.code = ? and distributor.id = ? and active = true order by firstName", ERConstants.USER_ROLE_SUPERVISOR, distributorId).fetch();
			sellers = ER_User.find("role.code = ? and distributor.id = ? and active = true order by firstName", ERConstants.USER_ROLE_SALES_MAN, distributorId).fetch();
		}

		if (sellers!=null) {
			String[] assigned = (flash.contains("store.sellers[].id"))?flash.get("store.sellers[].id").split(","):null;
			if (assigned!=null) {
				Long[] data = new Long[assigned.length];  
				for (int i = 0; i < assigned.length; i++) {  
				  data[i] = Long.valueOf(assigned[i]);  
				}
				List<ER_User> assignedSellers = ER_User.find("role.code = ? and distributor.id = (?) and id in (:a) and active = true order by firstName", 
						ERConstants.USER_ROLE_SALES_MAN, distributorId).bind("a", data).fetch();
				sellers.removeAll(assignedSellers);
				renderArgs.put("assigned", assignedSellers);
			} else if (store!=null) {
				sellers.removeAll(store.sellers);
				renderArgs.put("assigned", store.sellers);
			}
		}


		if(administrators !=null){
			String[] current_administrators = (flash.contains("store.administrators[].id")) ? flash.get("store.administrators[].id").split(","):null;
			if(current_administrators != null){
				Long[] data = new Long[current_administrators.length];
				for(int i = 0; i < current_administrators.length ; i++){
					data[i] = Long.valueOf(current_administrators[i]);
				}
				List<ER_User> assignedAdministrators = ER_User.find("role.code = ? and distributor.id = (?) and id in (:a) and active = true order by firstName",
						ERConstants.USER_ROLE_SUPERVISOR, distributorId).bind("a", data).fetch();
				administrators.removeAll(assignedAdministrators);
				renderArgs.put("assigned_administrators", assignedAdministrators);
			}else if(store !=null){
				administrators.removeAll(store.administrators);
				renderArgs.put("assigned_administrators", store.administrators);

			}
		}
		
		renderArgs.put("administrators", administrators);
		renderArgs.put("sellers", sellers);
	}

//	public static void deleteStore(Long id) {
//
//		ER_Store store = null;
//
//		if (id!=null) {
//
//			store = ER_Store.findById(id);
//
//			if (store!=null)
//			{
//				store.delete();
//			}
//			flash.success(Messages.get("store.delete.success"));
//		}
//
//		storesList(null,null,null,true,true);
//
//	}

    public static void editStore(Long id) {
    	
    	Long channelId = null;
    	Long distributorId = null;
    	ER_Store store = null;

		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
    	if (id!=null) {
    		
    		store = ER_Store.findById(id);

    		if (store!=null && store.distributor!=null) {
				if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
					if (!store.distributor.channel.equals(connectedUser.channel)) {
						store=null;
					} else {
						channelId = store.distributor.channel.id;
					}
				} else if (userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)) {
					if (!store.distributor.equals(connectedUser.distributor)) {
						store=null;
					} else {
						channelId = store.distributor.channel.id;
					}
				} else {
					channelId = store.distributor.channel.id;
				}
    		}
    		
    		
    		renderArgs.put("store", store);
    	}
    	
    	if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN)) {
    		List<ER_Channel> channels = ER_Channel.find("active = true order by name").fetch();
    		renderArgs.put("channels", channels);
    		
    		if (store!=null) {
    			distributorId = store.distributor.id;
    		}
    	}
    	
    	if (userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
    		channelId = connectedUser.channel.id;
    		if (store!=null) {
    			distributorId = store.distributor.id;
    		}
    	} else {
    		if (flash.contains("channelId")) {
        		String idString = flash.get("channelId");
        		if (!idString.isEmpty()) {
        			channelId = Long.parseLong(idString);
        		}
        	}
    	}
    	
    	if (userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)) {
			if(connectedUser.distributor != null){
				distributorId = connectedUser.distributor.id;
			}
    	} else {
    		if (flash.contains("store.distributor.id")) {
        		String idString = flash.get("store.distributor.id");
        		if (!idString.isEmpty()) {
        			distributorId = Long.parseLong(idString);
        		}
        	}
    	}
    	
    	if (channelId!=null) {
    		List<ER_Distributor> distributors = ER_Distributor.find("active = true and channel.id = ? order by name", channelId).fetch();
    		renderArgs.put("distributors", distributors);
    	}
    	

    	addUserChannelArg();
    	addFilteredSupervisorsAndSellers(distributorId, (ER_Store) renderArgs.get("store"));
        render();
    }

    public static void saveStore(@Valid ER_Store store, Long storeId, Long channelId) {
    	flash.clear();


    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("store.create.fielderrors"));
    		validation.keep();
    		editStore(store.id);
    	} else {

			ER_User connectedUser = connectedUser();
			Integer userRol = connectedUserRoleCode(connectedUser);

    		if (userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)) {
    			store.distributor = connectedUser.distributor;
    		}
    		
    		if (storeId!=null) {
    			ER_Store currentStore = ER_Store.findById(storeId);
    			currentStore.name = store.name;

				currentStore.administrators.clear();
    			if (store.administrators !=null && !store.administrators.isEmpty()) {
					for(ER_User administrator : store.administrators){
						if(administrator.id != null){
							ER_User user = ER_User.findById(administrator.id);
							currentStore.administrators.add(user);
							Logger.info(user.getFullName());
						}
					}
    			}
    			currentStore.distributor = ER_Distributor.findById(store.distributor.id);
				currentStore.active =  store.active;
    			currentStore.sellers = null;
    			currentStore.save();
    			
    			List<ER_User> currentSellers = new ArrayList<ER_User>();
    			if (store.sellers!=null) {
	    			for (ER_User seller : store.sellers) {
	    				currentSellers.add(seller);
	    			}
    			}
    			currentStore.sellers = currentSellers;
    			currentStore.save();
    			flash.success(Messages.get("store.edit.success"));
    		} else {
    			store.save();
    			flash.success(Messages.get("store.create.success"));
    		}
    	}
    	
  
    	Long  sessionChannelId = "".equals(session.get("channelId")) || "null".equals(session.get("channelId")) ||session.get("channelId")==null ? null : Long.parseLong(session.get("channelId"));
    	Long  distributorId = "".equals(session.get("distributorId")) || "null".equals(session.get("distributorId")) ||session.get("distributorId")==null ? null : Long.parseLong(session.get("distributorId"));
    	String sessionStore = session.get("store");

		if (sessionChannelId != null || distributorId != null || sessionStore != null) {    			
			searchStores(sessionChannelId, distributorId, sessionStore, "", true, true,store.active);
		} else {
			storesList(null,null,null,true,true);
		}

    }

	public static void deleteStore(Long id){

		ER_Store  store = null;
		if (id!=null) {
			store = ER_Store.findById(id);
		}
		boolean errorscaptured = false;
		try
		{
			store.delete();
			flash.success(Messages.get("store.delete.success"));
		}
		catch ( Exception ex )
		{
			flash.error(Messages.get("store.delete.error"));
			errorscaptured = true;
			Logger.error(ex,"store: %d", id);

		}
		if (errorscaptured)
		{
			Long  sessionChannelId = "".equals(session.get("channelId")) || "null".equals(session.get("channelId")) ||session.get("channelId")==null ? null : Long.parseLong(session.get("channelId"));
			Long  distributorId = "".equals(session.get("distributorId")) || "null".equals(session.get("distributorId")) ||session.get("distributorId")==null ? null : Long.parseLong(session.get("distributorId"));
			String sessionStore = session.get("store");

			if (sessionChannelId != null || distributorId != null || sessionStore != null) {
				storesList(sessionChannelId, distributorId, sessionStore, true, store.active);
			} else {
				storesList(null,null,null,true,true);
			}
		}
		else
		{
			storesList(null,null,null,true,true);
		}
	}

}
