package controllers;

import com.sun.org.apache.xpath.internal.operations.Bool;
import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.ivy.util.DateUtil;

import java.util.HashMap;

import models.*;
import play.Logger;
import play.data.binding.As;
import play.data.validation.Email;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.modules.paginate.ValuePaginator;
import play.mvc.With;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static helpers.ERConstants.INCIDENT_STATUS_FINALIZED;

@With(Secure.class)
@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
@Access
public class AdminReports extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Applied discounts report
	 * ************************************************************************************************************************
	 */
	
    public static void reportAppliedDiscounts() {
    	ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
    	if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) || userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER) || userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			List<ER_Channel> channels = new ArrayList<ER_Channel>();
			switch (connectedUserRoleCode(connectedUser)){
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
					channels =  ER_Channel.find("select c from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
				break;
				case ERConstants.USER_ROLE_SUPER_ADMIN:
					channels = ER_Channel.find("order by name").fetch();
				break;
				case ERConstants.USER_ROLE_CHANNEL_MANAGER:
					channels.add(connectedUser.channel);
				break;

			}
			renderArgs.put("channels", channels);
    	}
    	
    	Long channelId = null;
    	if (flash.contains("channelId")) {
    		String channelIdStr = flash.get("channelId");
    		if (channelIdStr!=null && !channelIdStr.isEmpty()) {
    			channelId = Long.parseLong(channelIdStr);
    		}
    	}
    	
    	if (channelId!=null) {
    		List<ER_Distributor> distributors = ER_Distributor.find("active = true and channel.id = ? order by name", channelId).fetch();
    		renderArgs.put("distributors", distributors);
    	}

        render();
    }

    public static void generateReportAppliedDiscounts(Long channelId, @Required @As("dd/MM/yyyy") Date startDate, @Required @As("dd/MM/yyyy") Date endDate) {

    	flash.clear();

    	try {
	    	if (validation.hasErrors()) {
	    		flash.error(Messages.get("reports.appliedDiscounts.fieldsError"));
	    		params.flash();
	    		validation.keep();
	    		reportAppliedDiscounts();
	    	} else {
	    		Calendar c = Calendar.getInstance();
	        	c.setTime(endDate);
	        	c.set(Calendar.HOUR_OF_DAY, 23);
	        	c.set(Calendar.MINUTE, 59);
	        	c.set(Calendar.SECOND, 59);
	        	endDate = c.getTime();

	        	if (startDate.compareTo(endDate)>=0) {
	        		flash.error(Messages.get("reports.appliedDiscounts.dateserror"));
	        		reportAppliedDiscounts();
	        	}
				ER_User user = connectedUser();

				List<ER_Quotation> quotations = null;
				switch(connectedUserRoleCode(user)){
					case ERConstants.USER_ROLE_SUPER_ADMIN:
						if (channelId==null /*&& distributorId ==null*/) {
							quotations = ER_Quotation.find("discount>0 and discountDate>= ? and discountDate<= ?", startDate, endDate).fetch();
						} else /*if (channelId!=null && distributorId==null)*/ {
							quotations = ER_Quotation.find("discount>0 and discountDate>= ? and discountDate<= ? and incident.creator in (select u from ER_Store s join s.sellers u where s.distributor.channel.id = ?)",
									startDate, endDate, channelId).fetch();
						}
					break;
					case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
						List<Long> channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", user).fetch();

						if (channelId==null  /*&& distributorId ==null*/) {
							quotations = ER_Quotation.find("discount >0 and discountDate >= ? and discountDate<= ? and incident.channel.id IN :c  " +
									"", startDate, endDate).bind("c",channelIds).fetch();
						} else /*if (channelId!=null && distributorId==null)*/ {
							quotations = ER_Quotation.find(" discount>0 and discountDate>= ? and discountDate<= ? " +
											" and incident.channel.id = ? and incident.channel.id IN :c   ",
									startDate, endDate, channelId).bind("c",channelIds).fetch();
						}
						break;
					case ERConstants.USER_ROLE_CHANNEL_MANAGER:
						quotations = ER_Quotation.find("incident.channel.id = ? and discount>0 and discountDate>= ? and discountDate<= ? ",
								 user.channel.id,startDate, endDate).fetch();
						break;
					case ERConstants.USER_ROLE_SUPERVISOR:
						List<Long> usersIds = ER_Store.find("select se.id from ER_Store s join s.administrators  a join s.sellers se where a = ? group by se", user).fetch();
						//Administradores
						List<Long> supervisoresIds = ER_Store.find("select a.id from ER_Store s join s.administrators a where s.distributor = ?", user.distributor).fetch();
						//Agrega lista de administradores a lista de usuarios
						usersIds.addAll(supervisoresIds);
						if(!usersIds.isEmpty()){
							quotations = ER_Quotation.find(" discount > 0 and discountDate >= ? and discountDate <= ?  and  incident.creator.id IN :c ",startDate, endDate ).bind("c",usersIds).fetch();
						}

						break;
					case ERConstants.USER_ROLE_SALES_MAN:
						quotations = ER_Quotation.find("incident.creator = ? and discount>0 and discountDate>= ? and discountDate<= ? ",
								user,startDate, endDate).fetch();
						break;
					case ERConstants.USER_ROLE_CABIN_AGENT: break;

				}



//	        	else if (distributorId!=null) {
//	        		quotations = ER_Quotation.find("discount>0 and discountDate>= ? and discountDate<= ? and incident.creator in (select u from ER_Store s join s.sellers u where s.distributor.id = ?)", 
//	        				startDate, endDate, distributorId).fetch();
//	        	}
	        	
	        	if (quotations==null || quotations.size()==0) {
	        		params.flash();
	        		flash.error(Messages.get("reports.appliedDiscounts.norecords"));
	        		reportAppliedDiscounts();
	        	}
	        	
	        	request.format = "xls";
	        	renderTemplate("Reports/DescuentosAplicados.xls", quotations);
	    	}
    	} catch (Exception e) {
    		flash.error(Messages.get("reports.appliedDiscounts.error"));
    		Logger.error(e, "Error generation applied discounts report");
    	}
    	
    	reportAppliedDiscounts();
    	
    }
    
    /*
	 * ************************************************************************************************************************
	 * Closed sales report
	 * ************************************************************************************************************************
	 */

	public static void reportClosedSalesWithQA(Map<String, String> search,String startDate, String endDate) {

		filterIncidents(search, startDate, endDate);
		//List<ER_Channel> channels = ER_Channel.find("select c from ER_Channel c where c.active = true").fetch();
		//renderArgs.put("channels", channels);
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
		if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) || userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER) || userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			List<ER_Channel> channels = new ArrayList<ER_Channel>();
			switch (connectedUserRoleCode(connectedUser)){
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
					channels =  ER_Channel.find("select c from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
					break;
				case ERConstants.USER_ROLE_SUPER_ADMIN:
					channels = ER_Channel.find("active =true order by name").fetch();
					break;
				case ERConstants.USER_ROLE_CHANNEL_MANAGER:
					channels.add(connectedUser.channel);
					break;
				default:
					channels.add(connectedUser.channel);
					break;

			}
			renderArgs.put("channels", channels);
		}


		if(search!=null) {
			if("export".equals(search.get("export"))) {
				if("true".equals(session.get("result"))) {
					request.format = "xls";
					renderTemplate("Reports/VentasRealizadasQA.xls",renderArgs.get("incidents"));
					flash.success(Messages.get("report.quotations.form.search.successExcel"));
				}else {
					flash.error(Messages.get("report.quotations.form.search.error"));
				}
			}
		}
		List <ER_ReviewStatus> status = ER_ReviewStatus.findAll();
		renderArgs.put("status", status);

		//Render the list
		render();
	}
    
    public static void reportClosedSales(Map<String, String> search,String startDate, String endDate) {

				//Add quotations to the renderArgs
		filterIncidents(search, startDate, endDate);

		//List<ER_Channel> channels = ER_Channel.find("select c from ER_Channel c where c.active = true").fetch();
		//renderArgs.put("channels", channels);
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
		if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) || userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER) || userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			List<ER_Channel> channels = new ArrayList<ER_Channel>();
			switch (connectedUserRoleCode(connectedUser)){
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
					channels =  ER_Channel.find("select c from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
					break;
				case ERConstants.USER_ROLE_SUPER_ADMIN:
					channels = ER_Channel.find("active =true order by name").fetch();
					break;
				case ERConstants.USER_ROLE_CHANNEL_MANAGER:
					channels.add(connectedUser.channel);
					break;
				default:
					channels.add(connectedUser.channel);
					break;

			}
			renderArgs.put("channels", channels);
		}


		if(search!=null) {
			if("export".equals(search.get("export"))) {
				if("true".equals(session.get("result"))) {
					request.format = "xls";
					renderTemplate("Reports/VentasRealizadas.xls",renderArgs.get("incidents"));
					flash.success(Messages.get("report.quotations.form.search.successExcel"));
				}else {
					flash.error(Messages.get("report.quotations.form.search.error"));
				}
			}
		}

		//Render the list
		render();

    }

	private static void filterIncidents(Map<String, String> search, String startDate, String endDate) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MM/yyyy");
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		try {
			if(GeneralMethods.validateParameter(startDate)&&GeneralMethods.validateParameter(endDate)) {
				if(simpleformat.parse(startDate).before(simpleformat.parse(endDate)) || simpleformat.parse(startDate).equals(simpleformat.parse(endDate))){
					filter.addGroupStart(Operator.AND);
					filter.addQuery("finalizedDate  >= ?", format.parse(startDate + " 00:00:00"));
					filter.addQuery("finalizedDate  <= ?", format.parse(endDate + " 23:59:59"),Operator.AND);
					filter.addGroupEnd();
				}
			}
			if(search!=null) {


				filter.addQuery("status.id = ?", Long.valueOf(INCIDENT_STATUS_FINALIZED), Operator.AND);
				if(search.get("review")!=null&&!search.get("review").isEmpty()) {
					Long review = Long.valueOf(search.get("review"));
					if 	(!search.get("review").equals("none"))
					filter.addQuery("reviewDetail.status.id = ?",review, Operator.AND);
                    else {
                        filter.addGroupStart(Operator.AND);
                        filter.addQuery("inspection.inspectionNumber = ?", true);
                        filter.addQuery("reviewDetail.status.id = ?", review, Operator.OR);
                        filter.addGroupEnd();
                    }
				}
				if(search.get("number")!=null&&!search.get("number").isEmpty()) {
					filter.addQuery("number = ?", Long.valueOf(search.get("number")), Operator.AND);
				}

				if(search.get("inspectionNumber")!=null&&!search.get("inspectionNumber").isEmpty()) {
					filter.addQuery("inspection.inspectionNumber like ?", "%"+search.get("inspectionNumber")+"%", Operator.AND);
				}
				if(search.get("channel")!=null&&!search.get("channel").isEmpty()) {
					filter.addQuery("channel.id = ?", Long.valueOf(search.get("channel")), Operator.AND);
				}
				if(search.get("broker")!=null&&!search.get("broker").isEmpty()) {
					filter.addQuery("concat(creator.firstName,ifnull(concat(' ',creator.lastName),'')) like ?", "%"+search.get("broker")+"%", Operator.AND);
				}
				if(search.get("isIndividual")!=null&&!search.get("isIndividual").isEmpty()) {
					filter.addQuery("client.isIndividual = ?", search.get("isIndividual").equals("true"), Operator.AND);
				}
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e   .printStackTrace();
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
		ModelPaginator quotations = null;
		if (filter.isValidFilter()) {
			//only can see their role
			ER_User connectedUser = connectedUser();
			GenericModel.JPAQuery query = null;
			Integer userRol = connectedUserRoleCode(connectedUser);
			if(userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
				List<Long> channelIds = null;
				channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
				String queryStr = " (";
				if (channelIds != null && !channelIds.isEmpty()) {
					queryStr = queryStr + "channel.id IN :c OR ";
				}
				queryStr = queryStr + " creator = :s )";
				if (!filter.getQuery().isEmpty()) {
					queryStr = "AND" + queryStr;
					query = ER_Incident.find(filter.getQuery() + queryStr + " order by creationDate DESC", filter.getParametersArray());
				} else {
					query = ER_Incident.find(queryStr + " order by creationDate DESC", filter.getParametersArray());
				}
				if (channelIds != null && !channelIds.isEmpty()) {
					query.bind("c", channelIds);
				}
				query.bind("s", connectedUser);
			}
			else if(userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)){
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
			}else if(userRol.equals(ERConstants.USER_ROLE_SUPERVISOR)){
				List<Long> userIds = ER_Store.find("select u.id from ER_Store s join s.sellers u  join s.administrators a where a = ?", connectedUser).fetch();
				//Administradores
				List<Long> supervisoresIds = ER_Store.find("select a.id from ER_Store s join s.administrators a where s.distributor = ?", connectedUser.distributor).fetch();
				//Agrega lista de administradores a lista de usuarios
				userIds.addAll(supervisoresIds);

				userIds.add(connectedUser.id);
				if (!filter.getQuery().isEmpty())
					query = ER_Incident.find(filter.getQuery() + " AND creator.id IN :s order by creationDate DESC", filter.getParametersArray()).bind("s", userIds);
				else
					query = ER_Incident.find(" creator.id IN :s order by creationDate DESC", filter.getParametersArray()).bind("s", userIds);
			}else if(userRol.equals(ERConstants.USER_ROLE_SALES_MAN)){
				if (!filter.getQuery().isEmpty())
					query = ER_Incident.find(filter.getQuery() + " AND creator = :s order by creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
				else
					query = ER_Incident.find( " creator = :s order by creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
			}else{
				query = ER_Incident.find(filter.getQuery() + " order by creationDate DESC", filter.getParametersArray());
			}
			//llenado
			List<ER_Incident> quotationList = query.fetch();
			ValuePaginator quotation = new ValuePaginator(quotationList);
			quotation.setPageSize(10);
			//quotations = new ModelPaginator(ER_Quotation.class, filter.getQuery(), filter.getParametersArray());
			session.put("result", !quotation.isEmpty());
			renderArgs.put("incidents", quotation);
		} /*else {
			if(filter.getQuery().isEmpty()) {
				quotations = new ModelPaginator(ER_Quotation.class,"id = -1");
			}
		}*/

		//quotations.orderBy("creationDate ASC");
		//quotations.setPageNumber(page);
		//quotations.setPageSize(10);
	}
    
    public static void generateReportClosedSales(Map<String, String> search, String startDate,String endDate) {
		flash.clear();
		reportClosedSales(search,startDate,endDate);
}
	public static void generateReportClosedSalesQA(Map<String, String> search, String startDate,String endDate) {
		flash.clear();
		reportClosedSalesWithQA(search,startDate,endDate);
	}
    
    /*
	 * ************************************************************************************************************************
	 * Lost sales report
	 * ************************************************************************************************************************
	 */
    
    public static void reportLostSales() {
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
		if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) || userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER) || userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			List<ER_Channel> channels = new ArrayList<ER_Channel>();
			switch (connectedUserRoleCode(connectedUser)){
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
					channels =  ER_Channel.find("select c from ER_Channel c join c.administrators a where a = ? and c.active =true", connectedUser()).fetch();
					break;
				case ERConstants.USER_ROLE_SUPER_ADMIN:
					channels = ER_Channel.find("active = true order by name").fetch();
					break;
				case ERConstants.USER_ROLE_CHANNEL_MANAGER:
					channels.add(connectedUser.channel);
					break;
				default:
					channels.add(connectedUser.channel);
					break;

			}
			renderArgs.put("channels", channels);
		}
    	
        render();
    }
    /*
    public static void generateReportLostSales(Long channelId, @Required @As("dd/MM/yyyy") Date startDate, @Required @As("dd/MM/yyyy") Date endDate) {
    	flash.clear();
    	
    	try {
	    	if (validation.hasErrors()) {
	    		flash.error(Messages.get("reports.lostSales.fieldsError"));
	    		params.flash();
	    		validation.keep();
	    		reportClosedSales();
	    	} else {
	    		Calendar c = Calendar.getInstance(); 
	        	c.setTime(endDate); 
	        	c.set(Calendar.HOUR_OF_DAY, 23);
	        	c.set(Calendar.MINUTE, 59);
	        	c.set(Calendar.SECOND, 59);
	        	endDate = c.getTime();
	        	
	        	if (startDate.compareTo(endDate)>=0) {
	        		flash.error(Messages.get("reports.lostSales.dateserror"));
	        		reportClosedSales();
	        	}
	        	List<ER_Incident> incidents = null;
				switch(connectedUserRoleCode()){
					case ERConstants.USER_ROLE_SUPER_ADMIN:

						if (channelId==null) {
							incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? order by finalizedDate", ERConstants.INCIDENT_STATUS_ANULLED, startDate, endDate).fetch();
						} else {
							incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and creator in (select u from ER_Store s join s.sellers u where s.distributor.channel.id = ?) order by finalizedDate",
									ERConstants.INCIDENT_STATUS_ANULLED,startDate, endDate, channelId).fetch();
						}

						break;
					case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
						List<Long> channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser()).fetch();

						if(!channelIds.isEmpty()){
							if (channelId==null) {
								incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and channel.id in :c order by finalizedDate", ERConstants.INCIDENT_STATUS_ANULLED, startDate, endDate).bind("c",channelIds).fetch();
							} else {
								incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and creator in (select u from ER_Store s join s.sellers u where s.distributor.channel.id = ?)  and channel.id in :c order by finalizedDate",
										ERConstants.INCIDENT_STATUS_ANULLED,startDate, endDate, channelId).bind("c", channelIds).fetch();
							}
						}


						break;
					case ERConstants.USER_ROLE_CHANNEL_MANAGER:
						if (channelId==null) {
							incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and channel.id =? order by finalizedDate", ERConstants.INCIDENT_STATUS_ANULLED, startDate, endDate,connectedUserChannelId()).fetch();
						} else {
							incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and creator in (select u from ER_Store s join s.sellers u where s.distributor.channel.id = ?) and channel.id =?  order by finalizedDate",
									ERConstants.INCIDENT_STATUS_ANULLED,startDate, endDate, channelId,connectedUserChannelId()).fetch();
						}
						break;
					case ERConstants.USER_ROLE_SUPERVISOR:
						List<Long> usersIds = ER_Store.find("select se.id from ER_Store s join s.administrators  a join s.sellers se where a = ? group by se", connectedUser()).fetch();
						if(!usersIds.isEmpty()){
							if (channelId==null) {
								incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and creator.id in :c order by finalizedDate ", ERConstants.INCIDENT_STATUS_ANULLED, startDate, endDate).bind("c",usersIds).fetch();
							} else {
								incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and creator in (select u from ER_Store s join s.sellers u where s.distributor.channel.id = ?) and channel.id in :c order by finalizedDate",
										ERConstants.INCIDENT_STATUS_ANULLED,startDate, endDate, channelId).bind("c", usersIds).fetch();
							}
						}

						break;
					case ERConstants.USER_ROLE_SALES_MAN:
						if (channelId==null) {
							incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ?  and creator = ? order by finalizedDate  ", ERConstants.INCIDENT_STATUS_ANULLED, startDate, endDate,connectedUser()).fetch();
						} else {
							incidents = ER_Incident.find("status.code = ? and finalizedDate>= ? and finalizedDate<= ? and creator in (select u from ER_Store s join s.sellers u where s.distributor.channel.id = ?) and creator = ? order by finalizedDate",
									ERConstants.INCIDENT_STATUS_ANULLED,startDate, endDate, channelId,connectedUser()).fetch();
						}
						break;
					case ERConstants.USER_ROLE_CABIN_AGENT: break;

				}
	        	
	        	if (incidents==null || incidents.size()==0) {
	        		params.flash();
	        		flash.error(Messages.get("reports.lostSales.norecords"));
					reportLostSales();
	        	}
	        	
	        	request.format = "xls";
	        	renderTemplate("Reports/VentasPerdidas.xls", incidents);
	    	}
    	} catch (Exception e) {
    		flash.error(Messages.get("reports.lostSales.error"));
    		Logger.error(e, "Error generation lost sales report");
    	}

		reportLostSales();
	}
	*/
public static void users (){

   ER_User currentUser = connectedUser();

    List<ER_User> users;
   if(connectedUserRoleCode(currentUser).equals(ERConstants.USER_ROLE_SUPER_ADMIN))
         users = ER_User.findAll();
   else
        users = ER_User.find("channel_id = ?", currentUser.channel.id).fetch();

   List<ER_Store> stores = ER_Store.findAll();
   List<ER_Product> products = ER_Product.findAll();
   Map<ER_User, String >  storeMap = new HashMap();
   Map<ER_Distributor, List<ER_Product>> productsMap = new HashMap();
   for (ER_User user: users) {
      for (ER_Store store: stores){
         if (store.distributor != null && store.distributor.channel != null && store.distributor.channel.equals(user.channel)){
            storeMap.put(user, store.name);
            break;
         }
      }
      if (!productsMap.containsKey(user.distributor)){
         List<ER_Product> distProduct = new ArrayList<ER_Product>();
         for (ER_Product product: products){
            if (product.distributors.contains(user.distributor)) {
               distProduct.add(product);
            }
         }
         productsMap.put(user.distributor,distProduct);
      }

   }

		request.format = "xls";
		renderTemplate("Reports/Usuarios.xls",users,storeMap, productsMap );
		flash.clear();

}

public static void products (Long productId){
 List<ER_Product> products = new ArrayList<ER_Product>();
        if(productId==null){
            products = ER_Product.findAll();
        }else{
           ER_Product product = ER_Product.findById(productId);
            products.add(product);
        }

   Map<Long, ER_Product_Discount> comercialManagers = new HashMap();
   Map<Long, ER_Product_Discount> channelManagers = new HashMap();
   Map<Long, ER_Product_Discount> supervisors = new HashMap();
   Map<Long, ER_Product_Discount> users = new HashMap();
   Map<Long, ER_Product_Discount> sellers = new HashMap();
   Map<Long, List<ER_Product_Discount_Range>> ranges = new HashMap();
   List<ER_Coverage> coverages = ER_Coverage.findAll();
List<ER_Product_Coverage> allProductCoverages = ER_Product_Coverage.findAll();
   Map<Long,List<Integer>> valuesSizePerCoverage = new HashMap<Long, List<Integer>>();
   Map<String, List<ER_Product_Coverage>> productCoverageValuesMap = new HashMap<String, List<ER_Product_Coverage>>();
   Map<String, List<ER_Product_Coverage_Value>> productCoverageClassValuesMap = new HashMap<String, List<ER_Product_Coverage_Value>>();
   Map<String, ER_Product_Coverage> productCoverageMap = new HashMap<String, ER_Product_Coverage>();
   Map<String, ER_Product_Coverage_Deductible> coveragesPerClass = new HashMap<String, ER_Product_Coverage_Deductible>();
   for (ER_Coverage coverage : coverages){
      for (ER_Product product: products){
         List<Integer> sizes = new ArrayList<Integer>();
        for (int i = 0 ; i< 3; i++){
	  sizes.add(i);
	}
         List<Integer> currentSize =valuesSizePerCoverage.get(coverage.getId());
         if (currentSize== null || sizes.size()>currentSize.size()){
            valuesSizePerCoverage.put(coverage.getId(),sizes);
         }
//	productCoverageValuesMap.put(coverage.getId()+"-"+product.getId(),product.coverages);
	Integer count = 0;
	for (ER_Product_Coverage product_coverage :product.coverages){
	//productCoverageMap.put(coverage.id+"-"+product.id,product_coverage);
	count++;
	if (product_coverage.deductibles!=null && !product_coverage.deductibles.isEmpty()) {
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-0", product_coverage.deductibles.get(0));
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-1", product_coverage.deductibles.get(1));
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-2", product_coverage.deductibles.get(2));
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-3", product_coverage.deductibles.get(3));
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-4", product_coverage.deductibles.get(4));
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-5", product_coverage.deductibles.get(5));
						coveragesPerClass.put(product.getId() + "-" + coverage.getId() + "-6", product_coverage.deductibles.get(6));
					}
	}

	if(product.getId()==1L){
	   System.out.println(product.discounts.size());

	}
         if (!product.discounts.isEmpty()){
            comercialManagers.put(product.getId(),product.discounts.get(0));
            channelManagers.put(product.getId(),product.discounts.get(1));
            supervisors.put(product.getId(),product.discounts.get(2));
            users.put(product.getId(),product.discounts.get(3));
            sellers.put(product.getId(),product.discounts.get(4));
         }else{
	  //  comercialManagers.put(product.getId(),null);
          //  channelManagers.put(product.getId(),null);
          //  supervisors.put(product.getId(),null);
          //  users.put(product.getId(),product.discounts.get(3).value);
          //  sellers.put(product.getId(),product.discounts.get(4).value);

	}
	List<ER_Product_Discount_Range> defaultDiscount =new ArrayList<ER_Product_Discount_Range>();

       if (!product.rangeDiscounts.isEmpty()){
		defaultDiscount = product.rangeDiscounts;
         }
            ranges.put(product.getId(),defaultDiscount);

      }
   }


    request.format = "xls";


		for (ER_Product_Coverage coverage : allProductCoverages){
			productCoverageMap.put(coverage.coverage.id+"-"+coverage.product.id,coverage);
			productCoverageClassValuesMap.put(coverage.coverage.getId()+"-"+coverage.product.getId(),coverage.values);
		}

    renderTemplate("Reports/Products.xls", products, comercialManagers, channelManagers, supervisors, users,
        sellers,ranges, coverages, valuesSizePerCoverage, productCoverageValuesMap, productCoverageClassValuesMap, productCoverageMap, coveragesPerClass);
    flash.clear();

}

	public static void productView(){
            List<ER_Product> products = ER_Product.findAll();
            render(products);
    }
	
	
	private static void filterQuotations(Map<String, String> search, String startDate, String endDate) {
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		SimpleDateFormat simpleformat = new SimpleDateFormat("dd/MM/yyyy");
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		try {
			if(GeneralMethods.validateParameter(startDate)&&GeneralMethods.validateParameter(endDate)) {
				if(simpleformat.parse(startDate).before(simpleformat.parse(endDate)) || simpleformat.parse(startDate).equals(simpleformat.parse(endDate))){
					filter.addGroupStart(Operator.AND);
					filter.addQuery("creationDate  >= ?", format.parse(startDate + " 00:00:00"));
					filter.addQuery("creationDate  <= ?", format.parse(endDate + " 23:59:59"),Operator.AND);
					filter.addGroupEnd();
				}
			}
			if(search!=null) {
				if(search.get("status")!=null&&!search.get("status").isEmpty()) {
						filter.addQuery("incident.status.id = ?", Long.valueOf(search.get("status")), Operator.AND);
				}
				if(search.get("number")!=null&&!search.get("number").isEmpty()) {
					filter.addQuery("incident.number = ?", Long.valueOf(search.get("number")), Operator.AND);
				}
				if(search.get("name")!=null&&!search.get("name").isEmpty()) {
					filter.addQuery("product.name like ?", "%"+search.get("name")+"%", Operator.AND);
				}
				if(search.get("inspectionNumber")!=null&&!search.get("inspectionNumber").isEmpty()) {
					filter.addQuery("incident.inspection.inspectionNumber like ?", "%"+search.get("inspectionNumber")+"%", Operator.AND);
				}
				if(search.get("channel")!=null&&!search.get("channel").isEmpty()) {
					filter.addQuery("incident.channel.id = ?", Long.valueOf(search.get("channel")), Operator.AND);
				}
				if(search.get("broker")!=null&&!search.get("broker").isEmpty()) {
					filter.addQuery("concat(incident.creator.firstName,ifnull(concat(' ',incident.creator.lastName),'')) like ?", "%"+search.get("broker")+"%", Operator.AND);
				}
				if(search.get("isIndividual")!=null&&!search.get("isIndividual").isEmpty()) {
					filter.addQuery("incident.client.isIndividual = ?", search.get("isIndividual").equals("true"), Operator.AND);
				}
			}
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		ModelPaginator quotations = null;
		if (filter.isValidFilter()) {
			//only can see their role
			ER_User connectedUser = connectedUser();
			GenericModel.JPAQuery query = null;
			Integer userRol = connectedUserRoleCode(connectedUser);

			if(userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
				List<Long> channelIds = null;
				channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
				String queryStr = " (";
				if (channelIds != null && !channelIds.isEmpty()) {
					queryStr = queryStr + "incident.channel.id IN :c OR ";
				}
				queryStr = queryStr + " incident.creator = :s )";
				if (!filter.getQuery().isEmpty()) {
					queryStr = "AND" + queryStr;
					query = ER_Quotation.find(filter.getQuery() + queryStr + " order by creationDate DESC", filter.getParametersArray());
				} else {
					query = ER_Quotation.find(queryStr + " order by creationDate DESC", filter.getParametersArray());
				}
				if (channelIds != null && !channelIds.isEmpty()) {
					query.bind("c", channelIds);
				}
				query.bind("s", connectedUser);
			}
			else if(userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)){
				List<Long> distributorIds = null;
				distributorIds = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d.active = true", connectedUser).fetch();
				String queryStr = " (";
				if (distributorIds != null && !distributorIds.isEmpty()) {
					queryStr = queryStr + " incident.distributor.id IN :d OR ";
				}
				queryStr = queryStr + " incident.creator = :s )";
				if (!filter.getQuery().isEmpty()) {
					queryStr = "AND " + queryStr;
					query = ER_Quotation.find(filter.getQuery() + queryStr + "  order by creationDate DESC", filter.getParametersArray());
				}else {
					query = ER_Quotation.find( queryStr + "  order by creationDate DESC", filter.getParametersArray());
				}
				if (distributorIds != null && !distributorIds.isEmpty()) {
					query.bind("d", distributorIds);
				}
				query.bind("s", connectedUser);
			}else if(userRol.equals(ERConstants.USER_ROLE_SUPERVISOR)){
				List<Long> userIds = ER_Store.find("select u.id from ER_Store s join s.sellers u  join s.administrators a where a = ?", connectedUser).fetch();
				userIds.add(connectedUser.id);
				//Administradores
				List<Long> supervisoresIds = ER_Store.find("select a.id from ER_Store s join s.administrators a where s.distributor = ?", connectedUser.distributor).fetch();
				//Agrega lista de administradores a lista de usuarios
				userIds.addAll(supervisoresIds);
				if (!filter.getQuery().isEmpty())
					query = ER_Quotation.find(filter.getQuery() + " AND incident.creator.id IN :s order by incident.creationDate DESC", filter.getParametersArray()).bind("s", userIds);
				else
					query = ER_Quotation.find(" incident.creator.id IN :s order by incident.creationDate DESC", filter.getParametersArray()).bind("s", userIds);
			}else if(userRol.equals(ERConstants.USER_ROLE_SALES_MAN)){
				if (!filter.getQuery().isEmpty())
					query = ER_Quotation.find(filter.getQuery() + " AND incident.creator = :s order by incident.creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
				else
					query = ER_Quotation.find( " incident.creator = :s order by incident.creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
			}else{
				query = ER_Quotation.find(filter.getQuery() + " order by incident.creationDate DESC", filter.getParametersArray());
			}
			//llenado
			List<ER_Quotation> quotationList = query.fetch();
			ValuePaginator quotation = new ValuePaginator(quotationList);
			quotation.setPageSize(10);
			//quotations = new ModelPaginator(ER_Quotation.class, filter.getQuery(), filter.getParametersArray());
			session.put("result", !quotation.isEmpty());
			renderArgs.put("quotations", quotation);
		} /*else {
			if(filter.getQuery().isEmpty()) {
				quotations = new ModelPaginator(ER_Quotation.class,"id = -1");
			}
		}*/
	
		//quotations.orderBy("creationDate ASC");
		//quotations.setPageNumber(page);
		//quotations.setPageSize(10);
	}
	
	/*
	 * ************************************************************************************************************************
	 * General Quotations Report
	 * ************************************************************************************************************************
	 */
	
    public static void reportQuotations(Map<String, String> search,String startDate, String endDate, boolean intern) {
    	
    	if (!intern) {
    		flash.clear();
    		session.remove("page");
    		session.remove("quotation");
    		session.remove("result");
    	}
    	
    	//Add quotations to the renderArgs
    	filterQuotations(search, startDate, endDate);
    	
    	List<ER_Incident_Status> statuses = ER_Incident_Status.find("order by code asc").fetch();
		renderArgs.put("statuses", statuses);
		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);
		
		//List<ER_Channel> channels = ER_Channel.find("select c from ER_Channel c where c.active = true").fetch();
		//renderArgs.put("channels", channels);
		if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) || userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER) || userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
			List<ER_Channel> channels = new ArrayList<ER_Channel>();
			switch (connectedUserRoleCode(connectedUser)){
				case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
					channels =  ER_Channel.find("select c from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
					break;
				case ERConstants.USER_ROLE_SUPER_ADMIN:
					channels = ER_Channel.find("active =true order by name").fetch();
					break;
				case ERConstants.USER_ROLE_CHANNEL_MANAGER:
					channels.add(connectedUser.channel);
					break;
				default:
					channels.add(connectedUser.channel);
					break;

			}
			renderArgs.put("channels", channels);
		}
		if(intern) {
			renderArgs.put("search", search);
			renderArgs.put("startDate", startDate);
			renderArgs.put("endDate", endDate);
			if("true".equals(session.get("result"))) {
				flash.success(Messages.get("report.quotations.form.search.success"));
			}else {
				flash.error(Messages.get("report.quotations.form.search.error"));
			}
		}
		
		if(search!=null) {
			if("export".equals(search.get("export"))) {
				if("true".equals(session.get("result"))) {
					request.format = "xls";
					renderTemplate("Reports/Quotations.xls",renderArgs.get("quotations"));
					flash.success(Messages.get("report.quotations.form.search.successExcel"));
				}else {
					flash.error(Messages.get("report.quotations.form.search.error"));
				}
			}
		}
    	
    	//Render the list
    	render();
    }
    
    public static void searchQuotations(Map<String, String> search, String startDate,String endDate, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("quotation",search);
    	}
    	
    	reportQuotations(search,startDate,endDate, true);
    }
    
    /*
	 * ************************************************************************************************************************
	 * Quotations of User Report 
	 * ************************************************************************************************************************
	 */
    
    private static void filterQuotationsUser(Map<String, String> search, String startDate, String endDate) {
		
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		try {
			if(GeneralMethods.validateParameter(startDate)&&GeneralMethods.validateParameter(endDate)) {
				if(format.parse(startDate).before(format.parse(endDate))){
					filter.addGroupStart(Operator.AND);
					filter.addQuery("creationDate  >= ?", format.parse(startDate));
					filter.addQuery("creationDate  <= ?", format.parse(endDate),Operator.AND);
					filter.addGroupEnd();
				}
			}
			if(search!=null) {
				if(search.get("name")!=null&&!search.get("name").isEmpty()) {
					filter.addQuery("concat(incident.client.firstName,ifnull(concat(' ',incident.client.secondName),''),ifnull(concat(' ',incident.client.firstSurname),''),ifnull(concat(' ',incident.client.secondSurname),'')) like ?", "%"+search.get("name")+"%", Operator.AND);
				}
				if(search.get("channel")!=null&&!search.get("channel").isEmpty()) {
					filter.addQuery("incident.channel.id = ?", Long.valueOf(search.get("channel")), Operator.AND);
				}
                filter.addQuery("1 = ?", 1, Operator.AND);
			}
			else
                filter.addQuery("1 = ?", 1);
            ER_User user = connectedUser();
			if (user.role.code != ERConstants.USER_ROLE_SUPER_ADMIN && user.role.code  != ERConstants.USER_ROLE_CHANNEL_MANAGER) {
                filter.addQuery("incident.creator.id = ?", connectedUser().id);
            }
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		ModelPaginator quotations = null;
		if (filter.isValidFilter()) {			
			quotations = new ModelPaginator(ER_Quotation.class, filter.getQuery(), filter.getParametersArray());
			session.put("result", !quotations.isEmpty());
		} else {
			if(filter.getQuery().isEmpty()) {
				quotations = new ModelPaginator(ER_Quotation.class,"id = -1");
			}
		}
	
		quotations.orderBy("creationDate ASC");
		quotations.setPageNumber(page);
		quotations.setPageSize(10);
		renderArgs.put("quotations", quotations);
		
	}
	
    public static void reportQuotationsUser(Map<String, String> search,String startDate, String endDate, boolean intern) {
    	
    	if (!intern) {
    		flash.clear();
    		session.remove("page");
    		session.remove("quotation");
    		session.remove("result");
    	}
    	
    	//Add quotations to the renderArgs

    	filterQuotationsUser(search, startDate, endDate);
    	
    	List<ER_Incident_Status> statuses = ER_Incident_Status.find("order by code asc").fetch();
		renderArgs.put("statuses", statuses);

		ER_User connectedUser = connectedUser();
		Integer userRol = connectedUserRoleCode(connectedUser);

        if (userRol.equals(ERConstants.USER_ROLE_SUPER_ADMIN) || userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER) || userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
            List<ER_Channel> channels = new ArrayList<ER_Channel>();
            switch (connectedUserRoleCode(connectedUser)){
                case ERConstants.USER_ROLE_COMMERCIAL_MANAGER:
                    channels =  ER_Channel.find("select c from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
                    break;
                case ERConstants.USER_ROLE_SUPER_ADMIN:
                    channels = ER_Channel.find("active =true order by name").fetch();
                    break;
                case ERConstants.USER_ROLE_CHANNEL_MANAGER:
                    channels.add(connectedUser.channel);
                    break;
                default:
                    channels.add(connectedUser.channel);
                    break;

            }
            renderArgs.put("channels", channels);
        }

		/*List<ER_Channel> channels = ER_Channel.find("select c from ER_Channel c where c.active = true").fetch();
		renderArgs.put("channels", channels);*/
		
		if(intern) {
			renderArgs.put("search", search);
			renderArgs.put("startDate", startDate);
			renderArgs.put("endDate", endDate);
			if("true".equals(session.get("result"))) {
				flash.success(Messages.get("report.quotations.form.search.success"));
			}else {
				flash.error(Messages.get("report.quotations.form.search.error"));
			}
		}
		
		if(search!=null) {
			if("export".equals(search.get("export"))) {
				if("true".equals(session.get("result"))) {
					request.format = "xls";
					renderTemplate("Reports/QuotationsUser.xls",renderArgs.get("quotations"));
					flash.success(Messages.get("report.quotations.form.search.successExcel"));
				}else {
					flash.error(Messages.get("report.quotations.form.search.error"));
				}
			}
		}
    	
    	//Render the list
    	render();
    }
    
    public static void searchQuotationsUser(Map<String, String> search, String startDate,String endDate, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("quotation",search);
    	}


    	
    	reportQuotationsUser(search,startDate,endDate, true);
    }

/*	protected static boolean canViewReport() {

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
	}*/
	
	/*
	 * ************************************************************************************************************************
	 * Exceptions Quotations Report
	 * ************************************************************************************************************************
	 */
    
    private static void filterQuotationsException(Map<String, String> search, String startDate, String endDate) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		//Create a new filter object and add the query for each valid parameter
		Filter filter = new Filter();
		try {
			if (GeneralMethods.validateParameter(startDate) && GeneralMethods.validateParameter(endDate)) {
				if (format.parse(startDate).before(format.parse(endDate))) {
					filter.addGroupStart(Operator.AND);
					filter.addQuery("exceptionDate >= ?", format.parse(startDate));
					filter.addQuery("exceptionDate  <= ?", format.parse(endDate), Operator.AND);
					filter.addGroupEnd();
				}
			}
			if (search != null) {
				if (search.get("status") != null && !search.get("status").isEmpty()) {
					filter.addQuery("quotation.incident.status.id = ?", Long.valueOf(search.get("status")), Operator.AND);
				}
				if (search.get("channel") != null && !search.get("channel").isEmpty()) {
					filter.addQuery("quotation.incident.channel.id = ?", Long.valueOf(search.get("channel")), Operator.AND);
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		filter.addQuery("active = ?", 1, Operator.AND);
		//only can see their role
			if (filter.isValidFilter()) {
				//Checks role
				ER_User connectedUser = connectedUser();
				GenericModel.JPAQuery query = null;
				Integer userRol = connectedUserRoleCode(connectedUser);
				if(userRol.equals(ERConstants.USER_ROLE_COMMERCIAL_MANAGER)) {
					List<Long> channelIds = null;
					channelIds = ER_Channel.find("select c.id from ER_Channel c join c.administrators a where a = ? and c.active = true", connectedUser).fetch();
					String queryStr = " (";
					if (channelIds != null && !channelIds.isEmpty()) {
						queryStr = queryStr + "quotation.incident.channel.id IN :c OR ";
					}
					queryStr = queryStr + " quotation.incident.creator = :s )";
					if (!filter.getQuery().isEmpty()) {
						queryStr = "AND" + queryStr;
						query = ER_Exceptions.find(filter.getQuery() + queryStr + " order by quotation.incident.creationDate DESC", filter.getParametersArray());
					} else {
						query = ER_Exceptions.find(queryStr + " order by quotation.incident.creationDate DESC", filter.getParametersArray());
					}
					if (channelIds != null && !channelIds.isEmpty()) {
						query.bind("c", channelIds);
					}
					query.bind("s", connectedUser);
				}
				else if(userRol.equals(ERConstants.USER_ROLE_CHANNEL_MANAGER)){
					List<Long> distributorIds = null;
					distributorIds = ER_Distributor.find("select d.id from ER_Distributor d join d.administrators a where a = ? and d.active = true", connectedUser).fetch();
					String queryStr = " (";
					if (distributorIds != null && !distributorIds.isEmpty()) {
						queryStr = queryStr + " quotation.incident.distributor.id IN :d OR ";
					}
					queryStr = queryStr + " quotation.incident.creator = :s )";
					if (!filter.getQuery().isEmpty()) {
						queryStr = "AND " + queryStr;
						query = ER_Exceptions.find(filter.getQuery() + queryStr + "  order by quotation.incident.creationDate DESC", filter.getParametersArray());
					}else {
						query = ER_Exceptions.find( queryStr + "  order by quotation.incident.creationDate DESC", filter.getParametersArray());
					}
					if (distributorIds != null && !distributorIds.isEmpty()) {
						query.bind("d", distributorIds);
					}
					query.bind("s", connectedUser);
				}else if(userRol.equals(ERConstants.USER_ROLE_SUPERVISOR)){
					List<Long> userIds = ER_Store.find("select u.id from ER_Store s join s.sellers u  join s.administrators a where a = ?", connectedUser).fetch();
					userIds.add(connectedUser.id);
					//Administradores
					List<Long> supervisoresIds = ER_Store.find("select a.id from ER_Store s join s.administrators a where s.distributor = ?", connectedUser.distributor).fetch();
					//Agrega lista de administradores a lista de usuarios
					userIds.addAll(supervisoresIds);
					if (!filter.getQuery().isEmpty())
						query = ER_Exceptions.find(filter.getQuery() + " AND quotation.incident.creator.id IN :s order by quotation.incident.creationDate DESC", filter.getParametersArray()).bind("s", userIds);
					else
						query = ER_Exceptions.find(" quotation.incident.creator.id IN :s order by quotation.incident.creationDate DESC", filter.getParametersArray()).bind("s", userIds);
				}else if(userRol.equals(ERConstants.USER_ROLE_SALES_MAN)){
					if (!filter.getQuery().isEmpty())
						query = ER_Exceptions.find(filter.getQuery() + " AND quotation.incident.creator = :s order by quotation.incident.creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
					else
						query = ER_Exceptions.find( " quotation.incident.creator = :s order by quotation.incident.creationDate DESC", filter.getParametersArray()).bind("s", connectedUser);
				}else{
					query = ER_Exceptions.find(filter.getQuery() + " order by quotation.incident.creationDate DESC", filter.getParametersArray());
				}
				List<ER_Exceptions> ExceptionsList = query.fetch();
				ValuePaginator exceptions = new ValuePaginator(ExceptionsList);
				exceptions.setPageSize(10);
				session.put("result", !exceptions.isEmpty());
				renderArgs.put("exceptions", exceptions);
			}
	}
	
    public static void reportQuotationsException(Map<String, String> search,String startDate, String endDate, boolean intern) {
    	
    	if (!intern) {
    		flash.clear();
    		session.remove("page");
    		session.remove("exceptions");
    		session.remove("result");
    	}

			//Add quotations to the renderArgs
			filterQuotationsException(search, startDate, endDate);

			List<ER_Incident_Status> statuses = ER_Incident_Status.find("order by code asc").fetch();
			renderArgs.put("statuses", statuses);


			List<ER_Channel> channels = ER_Channel.find("select c from ER_Channel c where c.active = true").fetch();
			renderArgs.put("channels", channels);

			if (intern) {
				renderArgs.put("search", search);
				renderArgs.put("startDate", startDate);
				renderArgs.put("endDate", endDate);
				if ("true".equals(session.get("result"))) {
					flash.success(Messages.get("report.quotations.form.search.success"));
				} else {
					flash.error(Messages.get("report.quotations.form.search.error"));
				}
			}

			if (search != null) {
				if ("export".equals(search.get("export"))) {
					if ("true".equals(session.get("result"))) {
						request.format = "xls";
						renderTemplate("Reports/QuotationsException.xls", renderArgs.get("exceptions"));
						flash.success(Messages.get("report.quotations.form.search.successExcel"));
					} else {
						flash.error(Messages.get("report.quotations.form.search.error"));
					}
				}
			}
    	//Render the list
    	render();
    }
    
    public static void searchQuotationsException(Map<String, String> search, String startDate,String endDate, boolean intern, boolean internalSearch){
    	
    	flash.clear();
    	
    	if(!internalSearch){
    		session.remove("page");
    	}
    	
    	//Add the http parameters to the flash scope
    	params.flash();
    	
    	//Render the list
    	if(!intern) {
    		session.put("quotation",search);
    	}


    	
    	reportQuotationsException(search,startDate,endDate, true);
    }

}


