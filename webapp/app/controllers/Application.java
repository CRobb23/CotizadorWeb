package controllers;

import static helpers.ERConstants.OUT_OF_LINE_MESSAGE;
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
		//Message for system out of line.
		ER_Admin_Messages mail = ER_Admin_Messages.findById(Long.valueOf(OUT_OF_LINE_MESSAGE));
		String body = mail.body;
		renderArgs.put("mensajeFueraDeLinea",body);
    	render();
    }
}