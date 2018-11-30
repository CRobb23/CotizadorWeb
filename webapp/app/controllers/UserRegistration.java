package controllers;

import java.util.UUID;

import helpers.ERConstants;
import helpers.FieldAccesor;
import models.ER_Channel;
import models.ER_User;
import models.ER_User_Role;
import notifiers.Mails;
import play.cache.Cache;
import play.data.validation.Required;
import play.i18n.Messages;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;

public class UserRegistration extends Controller {

    public static void index() {
    	String randomID = Codec.UUID();
    	render(randomID);
    }
    
    public static void register(ER_User user, String code, String randomID){
    	validation.equals(code, Cache.get(randomID)).message("Código inválido intente nuevamente.");
    	validation.required(user.email).message("Correo Electrónico Requerido.");
    	
	    if(validation.hasErrors()){
	    	renderTemplate("UserRegistration/index.html", user, randomID);
	    }else{
	    	user.active = false;
	    	user.token = UUID.randomUUID().toString().replaceAll("-", "");
	    	user.save();
	    	
	    	Cache.delete(randomID);
	    	Mails.welcomeUserFinal(user);
	    	render();
	    }
    }
    
    public static void active(String token){
    	validation.required(token).message("Token Requerido.");
    	
	    if(!validation.hasErrors()){
	    	ER_User user = ER_User.find("byToken", token).first();
	    	user.role = ER_User_Role.find("byCode", ERConstants.USER_ROLE_FINAL_USER).first();
	    	user.channel = ER_Channel.find("byIsPublic", true).first();
	    	user.active = true;
	    	user.token = null;
	    	user.save();
	    	
	    	flash.success(Messages.get("userRegistration.user.emailActivation"));
	    }
	    render();
    }

	public static void mailExists(String email) {
    	ER_User user = ER_User.find("lower(email) = ? ", email).first();
    	renderJSON(!(user != null));
    }
	
	public static void captcha(String id) {
	    Images.Captcha captcha = Images.captcha();
	    String code = captcha.getText("#644546");
	    Cache.set(id, code, "10mn");
	    renderBinary(captcha);
	}
}
