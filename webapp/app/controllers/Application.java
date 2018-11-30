package controllers;

import static play.modules.pdf.PDF.renderPDF;

import helpers.ERConstants;
import helpers.QuotationHelper;

import java.util.List;

import models.*;
import notifiers.Mails;

import org.allcolor.yahp.converter.IHtmlToPdfTransformer;

import play.modules.pdf.PDF.Options;
import play.mvc.*;

@With(Secure.class)
public class Application extends AdminBaseController {
	
    public static void index() {
    	
    	if (checkRole(ERConstants.USER_ROLE_SALES_MAN)) {
    		UserCases.clientInformation();
    	}
    	
    	render();
    }
}