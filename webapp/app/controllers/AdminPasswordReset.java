package controllers;

import notifiers.Mails;
import helpers.PasswordMethods;
import models.ER_User;
import play.Logger;
import play.data.validation.*;
import play.i18n.Messages;
import play.mvc.*;

public class AdminPasswordReset extends Controller {

	/*
	 * ************************************************************************************************************************
	 * Methods to reset the password and send it by email
	 * ************************************************************************************************************************
	 */

    public static void resetPassword() {
    	render();
    }
    
    public static void sendPassword(@Email @Required String username) {
    	
    	if (validation.hasErrors()) {
    		flash.error(Messages.get("adminpasswordreset.resetpassword.error"));
    		resetPassword();
    	} else {
    		ER_User user = ER_User.find("byEmail", username).first();
    		
    		//Check if user exists
    		if (user==null) {
    			flash.error(Messages.get("adminpasswordreset.resetpassword.error"));
    			resetPassword();
    		}
    		
    		//Reset the password and send an email
    		if (resetUserPassword(user)) {
    			flash.success(Messages.get("adminpasswordreset.resetpassword.success"));
    		} else {
    			flash.error(Messages.get("adminpasswordreset.resetpassword.erroremail"));
				resetPassword();
    		}
    		
    		//Send to login screen
    		redirect(Router.reverse("Secure.login").toString());
    	}
    }
    
    public static boolean resetUserPassword(ER_User user) {
    	
    	//Return if user is not found
    	if (user==null) {
    		return false;
    	}
    	
    	//Set user password
		String code = PasswordMethods.getCode();
		user.password = code;

		//Send the email to the user
		if (Mails.passwordReset(user, code)) {
			user.save();
			return true;
		}
		
		return false;
    }

}
