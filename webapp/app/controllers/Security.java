package controllers;

import models.ER_General_Configuration;
import models.ER_User;
import play.i18n.Messages;
import utils.StringUtil;
import play.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.Map;

public class Security extends Secure.Security {

	static String authenticate(String username, String password) {

		ER_User user = ER_User.connect(username, password);


		if (user!=null) {
			if (user.active) {
				if (StringUtil.isNullOrBlank(user.token)) {
					return "true";
				} else {
					//Tiene token activo en otro dispositivo
					return "token";
				}
			} else {
				return Messages.get("secure.blocked");
			}
		} else {
			return Messages.get("secure.incorrect");
		}
	}

	public boolean check(String... profiles) {
		for (String profile : profiles) {
			ER_User user = ER_User.find("byEmail", connected()).first();
			if (user!=null && user.role!=null) {
				if (user.role.name.equalsIgnoreCase(profile)) {
					return true;
				}
			}
		}

		return false;
	}

	public static boolean checkIsQA() {
		ER_User user = ER_User.find("byEmail", connected()).first();
		if (user!=null && user.role!=null) {
			if (user.isQAUser != null && user.isQAUser) {
				return true;
			}
		} else {
			return false;
		}
		return false;
	}

	static boolean check(String profile) {
		ER_User user = ER_User.find("byEmail", connected()).first();
		if (user!=null && user.role!=null) {
			if (user.email.equals(profile)) {
				return true;
			}
			return user.role.name.equalsIgnoreCase(profile);
		} else {
			return false;
		}
	}

	static boolean check(int roleCode) {
		ER_User user = ER_User.find("byEmail", connected()).first();
		if (user!=null && user.role!=null) {
			return user.role.code == roleCode;
		} else {
			return false;
		}
	}

	static boolean validSession(String username, String token) {
		ER_User user = ER_User.find("byEmailAndToken", username, token).first();
		return user != null;
	}

	static boolean validTime(String userTime) {
		if (!StringUtil.isNullOrBlank(userTime)) {
			long previousT = Long.valueOf(userTime);
			long currentT = new Date().getTime();
			ER_General_Configuration configuration = ER_General_Configuration.generalConfiguration();
			long timeout = (configuration.sessionTimeout != null ? configuration.sessionTimeout : 5) * 1000 * 60;
			if ((currentT - previousT) <= timeout) {
				return true;
			}
		}
		return false;
	}

	static String generateToken(String username) {
		ER_User user = ER_User.find("byEmail", username).first();
		if (user != null) {
			user.generateToken();
			user.save();
			return user.token;
		}
		return "";
	}

	static void onDisconnect() {
		ER_User user = ER_User.find("byEmail", connected()).first();
		if (user != null) {
			user.token = null;
			user.save();
		}
	}

	static void onDisconnectByForce(String username) {
		ER_User user = ER_User.find("byEmail", username).first();
		if (user != null) {
			user.token = null;
			user.save();
		}
	}

	static String userTime() {
		String userTime = Long.toString(new Date().getTime());
		return userTime;
	}

	static boolean checkAccess() {
		ER_General_Configuration config = ER_General_Configuration.generalConfiguration();
		if (config!=null ) {
			return Boolean.TRUE.equals(config.fullAccess);
		} else {
			return true;
		}
	}


	public String SSOAuthentication(String data, HttpServletRequest request){
		try {
			byte[] byteArray = java.util.Base64.getDecoder().decode(data);
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Map<String, String> usuario = (Map<String, String>) objectInputStream.readObject();
			HttpSession session = request.getSession(true);
			String username = usuario.get("userName");
			String token = usuario.get("token");
			session.setAttribute("tokenSso", token);
			//session.setAttribute("SPRING_SECURITY_CONTEXT_KEY", sc);
			//auth.isAuthenticated()

			if(true) {
				return "main";
			}
		}catch (ClassNotFoundException | IOException e){

			Logger.error("Error al deserealizar el usuario");
			Logger.error(e.getMessage());
			e.printStackTrace();
		}
		return "login";
	}

}
