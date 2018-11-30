package controllers;

import java.util.*;

import helpers.ERConstants;
import models.ER_Channel;
import models.ER_Client;
import models.ER_Distributor;
import models.ER_Incident;
import models.ER_Product;
import models.ER_Store;
import models.ER_User;
import models.ER_User_Role;
import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;

public class AdminBaseController extends Controller {

	@Before
    static void setConnectedUser() {
        ER_User user = connectedUser();
        if (user!=null) {
        	renderArgs.put("connectedUser", user.firstName + " " + user.lastName);
        }
        
        if (validation.hasErrors()) {
			Logger.debug("Validation errors");
			for (play.data.validation.Error error : validation.errors()) {
				Logger.debug("%s %s", error.getKey(), error.message());
			}
		}
    }
	
	protected static ER_User connectedUser() {
		if(Security.isConnected()) {
            ER_User user = ER_User.find("byEmail", Security.connected()).first();
           return user;
        }
		
		return null;
	}
	
	protected static Long connectedUserChannelId() {
		ER_User user = connectedUser();
		if (user!=null) {
			if (user.channel!=null) {
				return user.channel.id;
			}
		}
		
		return null;
	}
	
	protected static int connectedUserRoleCode() {
		ER_User user = connectedUser();
		Integer role = null;
		if (user!=null && user.role!=null) {
			role = user.role.code;
		}
		
		if (role!=null) {
			return role;
		}
		
		return -1;
	}
	
	protected static boolean checkRole(int code) {
		return (connectedUserRoleCode() == code);
	}
	
	protected static List<ER_User_Role> authorizedRolesForUser() {
    	List<ER_User_Role> roles = null;
    	
    	switch (connectedUserRoleCode()) {
	    	case ERConstants.USER_ROLE_SUPER_ADMIN: {
	    		roles = ER_User_Role.findAll();
	    	}
	    	break;
	    	case ERConstants.USER_ROLE_COMMERCIAL_MANAGER: {
	    		List<Integer> restricted = new ArrayList<Integer>();
	    		restricted.add(ERConstants.USER_ROLE_SUPER_ADMIN);
	    		restricted.add(ERConstants.USER_ROLE_COMMERCIAL_MANAGER);
	    		restricted.add(ERConstants.USER_ROLE_CABIN_AGENT);
	    		roles = ER_User_Role.find("code not in (:r)").bind("r", restricted).fetch();
	    	}
	    	break;
	    	case ERConstants.USER_ROLE_CHANNEL_MANAGER: {
	    		List<Integer> allowed = new ArrayList<Integer>();
	    		allowed.add(ERConstants.USER_ROLE_SALES_MAN);
	    		allowed.add(ERConstants.USER_ROLE_SUPERVISOR);
	    		roles = ER_User_Role.find("code in (:a)").bind("a", allowed).fetch();
	    	}
	    	default: {
	    		roles = new ArrayList<ER_User_Role>();
	    	}
	    	break;
    	}
    	
    	return roles;
    }
	
	protected static boolean canModifyRole(ER_User_Role role) {
		if (role!=null) {
			for (ER_User_Role authRole : authorizedRolesForUser()) {
	    		if (role.id.equals(authRole.id)) {
	    			return true;
	    		}
	    	}
		}
		
		return false;
	}

	protected static boolean canAccessChannel(ER_Channel channel) {
		
		if (channel!=null) {
			
			switch (connectedUserRoleCode()) {
				case ERConstants.USER_ROLE_SUPER_ADMIN: {
					return true;
				}
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER: {
					boolean canAccess = false;
					for(ER_User ch :channel.administrators){
						 if(ch.id == connectedUser().id){
							 canAccess = true;
						 }
					}
					return canAccess;
				}
			}
		}
		
		return false;
    }

	protected static boolean canViewIncident(Long id) {
		
		if (id==null) {
			return false;
		}
		
		ER_Incident incident = ER_Incident.findById(id);
		return canViewIncident(incident);
	}
	
	protected static boolean canViewIncident(ER_Incident incident) {
		
		if (incident!=null) {
			
			ER_User connectedUser = connectedUser();
			
			if (incident.creator.equals(connectedUser())) {
				return true;
			}
			
			switch (connectedUserRoleCode()) {
				case ERConstants.USER_ROLE_SUPER_ADMIN: {
					return true;
				}
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER: {
					List<Long> channels = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c = ? and c.active = true", connectedUser, incident.channel).fetch();
		    		return !channels.isEmpty();
				}
				case ERConstants.USER_ROLE_CHANNEL_MANAGER: {
					List<Long> distributors = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d = ? and d.active=true", connectedUser, incident.distributor).fetch();
					return !distributors.isEmpty();
				}
				case ERConstants.USER_ROLE_SUPERVISOR: {
					List<ER_Store> stores = ER_Store.find("select s from ER_Store s join s.sellers u join s.administrators a where u = ? and a = ? and  s.active = true", incident.creator, connectedUser()).fetch();
		    		return !stores.isEmpty();
				}
			}
		}
		
		return false;
	}

	protected static boolean canViewClient(ER_Client client) {
		if (client !=null) {			
			
			ER_User connectedUser = connectedUser();
			
			switch (connectedUserRoleCode()) {
				case ERConstants.USER_ROLE_SUPER_ADMIN: {
					return true;
				}
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER: {
					List<Long> channels = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
					List<ER_Incident> incidents = ER_Incident.find("client = :c and channel.id in :i")
							.bind("c", client)
							.bind("i", channels)
							.fetch();
		    		return !incidents.isEmpty();
				}
				case ERConstants.USER_ROLE_CHANNEL_MANAGER: {
					
					List<Long> distributors = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d.active =true ", connectedUser).fetch();
					List<ER_Incident> incidents = ER_Incident.find("client = :c and distributor.id in :i")
							.bind("c", client)
							.bind("i", distributors)
							.fetch();
		    		return !incidents.isEmpty();
				}
				case ERConstants.USER_ROLE_SUPERVISOR: {
					long incidentsCount = ER_Incident.count("client = ? and (creator in (select u from ER_Store s join s.sellers u join s.administrators a where a = ?) or creator = ?)", client, connectedUser(), connectedUser());
		    	    return incidentsCount>0;
				}
				case ERConstants.USER_ROLE_SALES_MAN: {
					long incidentsCount = ER_Incident.count("client = ? and creator = ?", client, connectedUser());
		    	    return incidentsCount>0;
				}
				case ERConstants.USER_ROLE_FINAL_USER: {
					long incidentsCount = ER_Incident.count("client = ? and creator = ?", client, connectedUser());
		    	    return incidentsCount>0;
				}
			}
		}
		
		return false;
	}

	protected static List<ER_Product> authorizedProducts() {
		List<ER_Product> productList = new ArrayList<>();

		List<Long> channels = null;
		List<Long> distributors = null;
		
		switch(connectedUserRoleCode()){
			case ERConstants.USER_ROLE_SUPER_ADMIN:
				productList = ER_Product.find("select p from ER_Product p where p.active = ? and size(p.coverages)>0 order by p.name", true).fetch();
				//return new ArrayList<ER_Product>(new HashSet<ER_Product>(products));
				Collections.sort(productList, sortProducts);
				break;
			case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
				channels = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ?", connectedUser()).fetch();
				distributors = ER_Distributor.find("select d.id from ER_Distributor d join d.channel.administrators a where a = ? and d.active = true", connectedUser()).fetch();
				break;
			case ERConstants.USER_ROLE_CHANNEL_MANAGER:
				channels = ER_Channel.find("select d.channel.id from ER_Distributor d join d.administrators a where a = ? and d.active= true group by d.channel", connectedUser()).fetch();
				distributors = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d.active= true", connectedUser()).fetch();
				break;
			case ERConstants.USER_ROLE_SUPERVISOR:
		    	channels = ER_Channel.find("select s.distributor.channel.id from ER_Store s join s.administrators a where a = ? and s.active= true group by s.distributor.channel", connectedUser()).fetch();
		    	distributors = ER_Distributor.find("select s.distributor.id from ER_Store s join s.administrators a where a = ? and s.active= true group by s.distributor", connectedUser()).fetch();
		    	break;
			case ERConstants.USER_ROLE_SALES_MAN:
		    	distributors = ER_Distributor.find("select s.distributor.id from ER_Store s join s.sellers u where u = ? and s.active =true group by s.distributor", connectedUser()).fetch();
		    	channels = ER_Channel.find("select s.distributor.channel.id from ER_Store s join s.sellers u where u = ? and s.active =true group by s.distributor.channel", connectedUser()).fetch();
		    	break;
			case ERConstants.USER_ROLE_FINAL_USER:
				productList = ER_Product.find("select p from ER_Product p inner join p.distributors d where d.channel.isPublic = true and p.active = ? and size(p.coverages)>0 order by p.name", true).fetch();
		    	//return new ArrayList<ER_Product>(new HashSet<ER_Product>(products));
				Collections.sort(productList, sortProducts);
				break;
		}
		
		if (channels!=null && !channels.isEmpty() && (distributors==null || distributors.isEmpty())) {
			productList = ER_Product.find("select p from ER_Product p left outer join p.distributors d left outer join p.channels c where p.active = :a and c.id in (:c) and size(p.coverages)>0 group by p order by p.name")
					.bind("a", true).bind("c",channels).fetch();
					//return new ArrayList<ER_Product>(new HashSet<ER_Product>(products));
					Collections.sort(productList, sortProducts);
		} else if (distributors!=null && !distributors.isEmpty() && (channels==null || channels.isEmpty())) {
			productList = ER_Product.find("select p from ER_Product p left outer join p.distributors d left outer join p.channels c where p.active = :a and d.id in (:d) and size(p.coverages)>0 group by p order by p.name")
					.bind("a", true).bind("d",distributors).fetch();
					//return new ArrayList<ER_Product>(new HashSet<ER_Product>(products));
					Collections.sort(productList, sortProducts);
		} else if (channels!=null && !channels.isEmpty() && distributors!=null && !distributors.isEmpty()) {
			productList = ER_Product.find("select p from ER_Product p left outer join p.distributors d left outer join p.channels c where p.active = :a and (d.id in (:d) or c.id in (:c)) and size(p.coverages)>0  order by p.name")
					.bind("a", true).bind("d",distributors).bind("c",channels).fetch();
					//return new ArrayList<ER_Product>(new HashSet<ER_Product>(products));
					Collections.sort(productList, sortProducts);
		}

		final Set<String> seenProductNames = new HashSet<>();
		productList.removeIf(p -> !seenProductNames.add(p.name));

		return productList;
	}

	protected static Comparator<ER_Product> sortProducts = new Comparator<ER_Product>() {
		@Override
		public int compare(ER_Product p1, ER_Product p2) {
			String productName1 = p1.name.toUpperCase().trim();
			String productName2 = p2.name.toUpperCase().trim();
			return productName1.compareTo(productName2);
		}
	};
}
