package controllers;

import com.google.gson.Gson;
import models.ER_User;
import models.ws.rest.SecurityResponse;
import play.Logger;
import play.Play;
import play.data.validation.*;
import play.i18n.Messages;
import play.libs.Crypto;
import play.libs.WS;
import play.mvc.*;
import java.net.URI;
import javax.servlet.http.HttpServletResponse;


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

	public static  void move() {

//		return redirect("www.yahoo.com", false);
	}

	/*
	public RedirectView redirectWithRedirectView(){

		RedirectView redirectView = new RedirectView();
		redirectView.setUrl("https://www.google.com.vn");
		return redirectView;
	}*/

    public static void savePassword(@Required String password, @Required String newPassword, @Required String confirmPassword) {
    	flash.clear();
		try {
			Logger.info("ENTRO Y NO HACE NADA");
			URI yahoo = new URI("http://www.yahoo.com");
			HttpServletResponse servletResponse = null;
			servletResponse.setHeader("Location", String.valueOf(yahoo));
			servletResponse.setStatus(302);
			servletResponse.sendRedirect(yahoo.toString());
			redirect("www.yahoo.com");



		}catch ( Exception ex )
		{
			flash.error(Messages.get("Salio en error algo."));

		}

		//HttpHeaders httpHeaders = new HttpHeaders();
		//httpHeaders.setLocation(yahoo);


		if (validation.hasErrors()) {
    		flash.error(Messages.get("profile.changepassword.error.allfieldsrequired"));
    		profile();
    	} else {
    		
    		//Check if user password is correct
    		ER_User user = ER_User.find("email = ? and password = ?", Security.connected(), new Crypto().encryptAES(password)).first();
			//ER_User user;
			// autenticar y redirigir al SSO cambiar contraseña ... podria ser abrir en otra venta.
			/*
			String WS_URL = Play.configuration.getProperty("ssoUrl");
			String WS_APPNAME = Play.configuration.getProperty("appName");
			WS.WSRequest wsRequest = WS.url(WS_URL+"/admin/aplicarCambioContrasenia");
			wsRequest.setParameter("email",user.email);
			wsRequest.setParameter("contrasenia",newPassword); // ESTO no lo deberias usar.  contraseniaConf
			wsRequest.setParameter("contraseniaConf",confirmPassword); // ESTO no lo deberias usar.  contraseniaConf
			String response = wsRequest.get().getString();
			SecurityResponse securityResponse;
			securityResponse = new Gson().fromJson(response, SecurityResponse.class);
			*/

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

    		//user.save();
    		
    		flash.success(Messages.get("profile.changepassword.success"));
    		profile();
    	}
    }
}
