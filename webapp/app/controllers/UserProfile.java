package controllers;

import models.ER_User;
import models.ER_User_Profile;
import play.Logger;
import play.data.validation.Valid;
import play.i18n.Messages;
import play.mvc.With;

@With(Secure.class)
@Check({"Administrador maestro","Gerente comercial","Gerente de canal", "Supervisor", "Vendedor"})
public class UserProfile extends AdminBaseController {
    
    public static void saveProfile(@Valid ER_User_Profile profile) {
    	flash.clear();
    	
    	if (validation.hasErrors()) {
    		validation.keep();
    		params.flash();
    		flash.error(Messages.get("tasks.types.form.fielderrors"));
    		AdminProfile.profile();
    	}
    	
    	ER_User user = connectedUser();
    	try {
	    	ER_User_Profile userProfile = user.profile;
	    	if (userProfile!=null) {
	    		userProfile.agentCode = profile.agentCode;
	    		userProfile.phoneNumber = profile.phoneNumber;
	    		userProfile.mailSignature = profile.mailSignature;
	    		userProfile.save();
	    	} else {
	    		profile.save();
	    		user.profile = profile;
	    		user.save();
	    	}
	    	flash.success(Messages.get("profile.userprofile.success"));
    	} catch (Exception e) {
    		flash.error(Messages.get("profile.userprofile.error"));
    		Logger.error(e, "Error displaying user profile");
    	}
    	
    	AdminProfile.profile();
    }




    //AKLSFJAKLSÑDFJALSÑKDFJASDKLÑFJDAKLSÑFJADSKLFKL

	public static void ChangePasswordProfile(@Valid ER_User_Profile profile) {
		Logger.debug("ACA necesitamos armar la peticion para ir al SSO a cambiar contrasña");


    }


	}
