package controllers;

import models.*;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminTasks extends AdminBaseController {

    public static void taskTypesList(boolean intern) {
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
    	
    	ModelPaginator types = new ModelPaginator(ER_Task_Type.class);
    	types.setPageNumber(page);
    	renderArgs.put("types", types);
        render();
    }

    public static void editTaskType(Long id) {
    	if (id!=null) {
    		ER_Task_Type type = ER_Task_Type.findById(id);
    		renderArgs.put("type", type);
    	}
    	
    	render();
    }
    
    public static void saveTaskType(@Valid ER_Task_Type type) {
    	
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		validation.keep();
    		params.flash();
    		flash.error(Messages.get("tasks.types.form.fielderrors"));
    		editTaskType(type.id);
    	} else {
    		
    		if (type.id == null) {
    			flash.success(Messages.get("tasks.types.create.success"));
    		} else {
    			flash.success(Messages.get("tasks.types.edit.success"));
    		}
    		
    		type.save();
    	}
    	
    	taskTypesList(true);
    	
    }
}
