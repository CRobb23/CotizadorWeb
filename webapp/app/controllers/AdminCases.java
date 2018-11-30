package controllers;

import java.util.List;

import models.*;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminCases extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * No sale reasons list
	 * ************************************************************************************************************************
	 */
	
    public static void noSaleReasonsList(boolean intern) {
    	if (!intern) {
    		session.remove("page");
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
    	
    	ModelPaginator reasons = new ModelPaginator(ER_Declined_Sell_Reason.class);
    	reasons.setPageNumber(page);
    	renderArgs.put("reasons", reasons);
    	
        render();
    }
    
    /*
	 * ************************************************************************************************************************
	 * No sale reasons creation and editing
	 * ************************************************************************************************************************
	 */
    
    public static void editNoSaleReason(Long id) {
    	
    	if (id!=null) {
    		ER_Declined_Sell_Reason reason = ER_Declined_Sell_Reason.findById(id);
    		renderArgs.put("reason", reason);
    	}
    	
    	render();
    }
    
    public static void saveNoSaleReason(@Valid ER_Declined_Sell_Reason reason) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		
    		validation.keep();
    		params.flash();
    		flash.error(Messages.get("nosalereason.create.fielderrors"));
    		editNoSaleReason(reason.id);
    	} else {
    		
    		if (reason.id == null) {
    			flash.success(Messages.get("nosalereason.create.success"));
    		} else {
    			flash.success(Messages.get("nosalereason.edit.success"));
    		}
			/*if(reason.isInspectionDeclined == null){
				reason.isInspectionDeclined = true;
			}
			else
				reason.isInspectionDeclined = false;*/
    		reason.save();
    	}
    	
    	noSaleReasonsList(true);
    	
    }

}
