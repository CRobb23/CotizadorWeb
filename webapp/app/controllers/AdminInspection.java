package controllers;

import helpers.ERConstants;
import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

import models.ER_Attachment;

import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.db.jpa.Blob;
import play.i18n.Messages;
import play.libs.MimeTypes;
import play.modules.paginate.ModelPaginator;
import play.mvc.*;

@With(Secure.class)
@Check("Administrador maestro")
public class AdminInspection extends AdminBaseController {

    public static void centers() {
    	ER_Attachment centersFile = ER_Attachment.find("code = ?", ERConstants.FILE_CENTERS_LIST).first();
    	render(centersFile);
    }
    
    public static void viewFile(Long id) {
    	if (id!=null) {
    		ER_Attachment centersFile = ER_Attachment.findById(id);
    		if (centersFile!=null) {
    			response.setContentTypeIfNotSet(centersFile.attachment.type());
    			java.io.InputStream binaryData = centersFile.attachment.get();
    			if (binaryData!=null) {
    				renderBinary(binaryData, centersFile.fileName);
    			}
    		}
    	}
    	
    	notFound();
    	
    }
    
    public static void saveCentersList(@Required File file) {
    	
    	flash.clear();
    	if (validation.hasErrors()) {
    		params.flash();
    		flash.error(Messages.get("center.create.fielderrors"));
    		validation.keep();
    		centers();
    	} else {
    		
    		ER_Attachment centersFile = ER_Attachment.find("code = ?", ERConstants.FILE_CENTERS_LIST).first();
    		if (centersFile==null) {
    			centersFile = new ER_Attachment();
    			centersFile.code = ERConstants.FILE_CENTERS_LIST;
    		}
    		
    		try {
    			FileInputStream stream = new FileInputStream(file);
    			String type = MimeTypes.getContentType(file.getName());
    			if (centersFile.attachment!=null) {
    				centersFile.attachment.getFile().delete();
    			}
    			
    			centersFile.fileName = file.getName();
    			centersFile.attachment = new Blob();
    			centersFile.attachment.set(stream, type);
    			centersFile.save();
    			flash.success(Messages.get("centers.update.success"));
    		} catch (Exception e) {
    			Logger.error(e, "Error updating centers list file");
    			flash.error(Messages.get("centers.update.failed"));
    		}
    	}
    	
    	centers();
    }
}
