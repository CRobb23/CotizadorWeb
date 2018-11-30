package controllers;

import models.ER_User;
import play.data.validation.*;
import play.i18n.Messages;
import play.libs.Crypto;
import play.mvc.*;

@With(Secure.class)
public class AdminProfile extends AdminBaseController {

	/*
	 * ************************************************************************************************************************
	 * Methods to change the password with a form
	 * ************************************************************************************************************************
	 */
	
    public static void profile() {
    	ER_User user = connectedUser();
    	if (user!=null && user.profile!=null) {
    		renderArgs.put("profile", user.profile);
    	}
        renderTemplate("UserProfile/index.html");
    }
    
    public static void savePassword(@Required String password, @Required String newPassword, @Required String confirmPassword) {
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		flash.error(Messages.get("profile.changepassword.error.allfieldsrequired"));
    		profile();
    	} else {
    		
    		//Check if user password is correct
    		ER_User user = ER_User.find("email = ? and password = ?", Security.connected(), new Crypto().encryptAES(password)).first();
    		if (user == null) {
    			flash.error(Messages.get("profile.changepassword.error.wrongpassword"));
    			profile();
    		}
    		
    		//Check if new passwords match
    		if (!newPassword.equals(confirmPassword)) {
    			flash.error(Messages.get("profile.changepassword.error.passwordnomatch"));
    			profile();
    		}
    		
    		//Check if password has minimum length
    		if (!validation.minSize(newPassword, 6).ok) {
    			flash.error(Messages.get("profile.changepassword.error.passwordincorrectlength"));
    			profile();
    		}
    		
    		//Store the new password
    		user.password = newPassword;
    		user.save();
    		
    		flash.success(Messages.get("profile.changepassword.success"));
    		profile();
    	}
    }
}
