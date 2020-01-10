package controllers;

import java.util.UUID;

import com.google.gson.Gson;
import helpers.ERConstants;
import helpers.FieldAccesor;
import jobs.UserRegistrationJob;
import models.ER_Channel;
import models.ER_User;
import models.ER_User_Role;
import models.ws.rest.SecurityResponse;
import notifiers.Mails;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.i18n.Messages;
import play.libs.Codec;
import play.libs.Images;
import play.libs.WS;
import play.mvc.Controller;

import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;

public class UserRegistration extends Controller {

    public static void index() {
    	String randomID = Codec.UUID();
    	render(randomID);
    }


    public static SecurityResponse RegGlobal(ER_User user, WS.WSRequest wsRequest){
        String WS_URL = Play.configuration.getProperty("ssoUrl");
        String WS_APPNAME = Play.configuration.getProperty("appName");
        wsRequest = WS.url(WS_URL+"/ws/usuarios/crearUsuario");
        wsRequest.setParameter("email",user.email);
        wsRequest.setParameter("firstName",user.firstName);
        wsRequest.setParameter("lastName",user.lastName);
        wsRequest.setParameter("profile_id",88l);
        String response = wsRequest.get().getString();
        SecurityResponse securityResponse = new Gson().fromJson(response, SecurityResponse.class);
        return securityResponse;
	}


    public static void register(ER_User user, String code, String randomID){
		SecurityResponse securityResponse; String response;
		boolean responseLocal, responseGlobal;
		WS.WSRequest wsRequest;
    	validation.equals(code, Cache.get(randomID)).message("Código inválido intente nuevamente.");
    	validation.required(user.email).message("Correo Electrónico Requerido.");

        if(validation.hasErrors()){
            renderTemplate("UserRegistration/index.html", user, randomID);
        }else {
            String WS_URL = Play.configuration.getProperty("ssoUrl");
            String WS_APPNAME = Play.configuration.getProperty("appName");
            wsRequest = WS.url(WS_URL + "/ws/usuarios/autenticar");
            wsRequest.setParameter("usuario", user.email);
            wsRequest.setParameter("aplicacion", WS_APPNAME);
            response = wsRequest.get().getString();
            securityResponse = new Gson().fromJson(response, SecurityResponse.class);
            Logger.info("Que regresa" + response.toString());
            ER_User existe_usuario = ER_User.find("email=? ", user.email).first();
            if (existe_usuario!=null) {
                responseLocal = true;
            }else {
                responseLocal = false;
            }
            if(securityResponse.getCodigo() == 200){ //significa que el usuario existe en Global
                responseGlobal = true;
            }else {responseGlobal = false;}
            Logger.info("response:"+responseGlobal+" Local:"+responseLocal);

            // responseGlobal = true; responseLocal = true; // Test de las opciones

            if(responseGlobal && responseLocal){
                //existe en ambos lugares, reportar error.
                flash.keep("url");
                flash.error("El usuario ya existe en la plataforma");
                params.flash();
                renderTemplate("UserRegistration/index.html", user, randomID);
            }
            if(responseGlobal && !responseLocal) {
                //existe global pero no local, crear usuario en SSO
                user.save();


            } if(!responseGlobal && responseLocal){
                //no existe global pero si local OJO sera que estas despedido? deberia de volver a crearlo?
                //todo POSIBLE MENSAJE DE ERROR PORQUE EXISTE! O podria ser un usuario que fue creado mucho antes con un uso minimo.
                flash.keep("url");
                flash.error("No se creo la cuenta, usuario bloqueado en SSO, caso 1 en 1,000,000");
                params.flash();
                renderTemplate("UserRegistration/index.html", user, randomID);


            }
            if(!responseGlobal && !responseLocal){
                // hoy si persona nueva, crear su usuario.
                securityResponse = RegGlobal(user, wsRequest);
                Logger.info("retorno del security:"+ securityResponse+ " getcod:"+ securityResponse.getCodigo());
                if(securityResponse.getCodigo() == 200) { //significa que el usuario existe en Global
                    Logger.info("ANTES DE CREAR EL USARIO:"+ user.email+" Nombre:"+user.firstName);
                    user.save();
                    Logger.info("SE DEBIO CREAR EL USUARIO");
                }else{
                    flash.keep("url");
                    flash.error(securityResponse.getMensaje());
                    params.flash();
                    renderTemplate("UserRegistration/index.html", user, randomID);
                }

            }
            /* lo ultimo si es existoso lo anterior */
            user.active = false;
            user.token = UUID.randomUUID().toString().replaceAll("-", "");
            Cache.delete(randomID);flash.keep("url");
            flash.success("El usuario creado");
            params.flash();
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
