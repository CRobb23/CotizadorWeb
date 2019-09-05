package controllers;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Date;

import models.ER_User;
import models.ER_User_Custom_Logo;
import models.ER_Distributor_Custom_Logo;
import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.mvc.*;
import play.data.validation.*;
import play.libs.*;
import play.utils.*;
import utils.StringUtil;

public class Secure extends Controller {

    @Before(unless={"login", "authenticate", "logout", "logoutByForce","closeSession","forceCloseSession"})
    static void checkAccess() throws Throwable {
        // Check if username and token is present
        if(!session.contains("username") || !session.contains("token")) {
            flash.put("url", "GET".equals(request.method) ? request.url : Play.ctxPath + "/"); // seems a good default
            login();
        }
        // Check if username and token are valid
        boolean validSession = (Boolean) Security.invoke("validSession", session.get("username"), session.get("token"));
        if (!validSession) {
            login();
        }
        // Check if time is still valid
        boolean validTime = (Boolean) Security.invoke("validTime", session.get("usertime"));
        if (!validTime) {
            logout();
        }
        // Checks
        Check check = getActionAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
        check = getControllerInheritedAnnotation(Check.class);
        if(check != null) {
            check(check);
        }
        // Check if Access is present and check access
        Access access = getActionAnnotation(Access.class);
        if (access != null) {
            checkFullAccess();
        }
        // If here, update timestamp
        String userTime = (String) Security.invoke("userTime");
        session.put("usertime", userTime);
    }

    private static void check(Check check) throws Throwable {
        for(String profile : check.value()) {
            boolean hasProfile = (Boolean)Security.invoke("check", profile);
            if(hasProfile) {
                return;
            }
        }
        Security.invoke("onCheckFailed", "");
    }

    private static void checkFullAccess() throws Throwable {
        boolean fullAccess = (boolean) Security.invoke("checkAccess");
        if (fullAccess) {
            return;
        } else {
            Security.invoke("onCheckFailed", "");
        }
    }

    // ~~~ Login

    public static void login() throws Throwable {
        flash.keep("url");
        String authCookie = request.cookies.get("auth") != null ? request.cookies.get("auth").value : "";
        if (!StringUtil.isNullOrBlank(authCookie)) {
            logoutByForce(authCookie);
        }
        render();
    }

    public static void closeSession(String userName) throws Throwable {
        renderArgs.put("userName", userName);
        render();
    }

    public static void forceCloseSession(String userName) throws Throwable {

        try
        {
            ER_User user = ER_User.find("byEmail", userName).first();
            if (user!=null) {
                user.token = null;
                user.save();
                flash.success("Se cerró sesión en el otro dispositivo con éxito, por favor ingresa nuevamente en este dispositivo.");
                login();
            }
            else{
                flash.error("Credenciales invalidas, no es posible cerrar sesión en el otro dispositivo");
                closeSession(userName);
            }
        }
        catch ( Exception ex )
        {
            flash.error(Messages.get("user.edit.error"));
            Logger.error(ex,"close session user");
            login();
        }
    }

    public static void authenticate(@Required String username, String password) throws Throwable {
        // Check tokens
        String allowed = (String)Security.invoke("authenticate", username, password);

        if(validation.hasErrors()) {
            flash.keep("url");
            flash.error("secure.required");
            params.flash();
            login();
        }

        if(!allowed.equals("true")) {
            if(allowed.equals("token"))
            {
                closeSession(username);
            }
            else {
                flash.keep("url");
                flash.error(allowed);
                params.flash();
                login();
            }
        }
        else {
            String token = (String) Security.invoke("generateToken", username);
            // Mark user as connected
            session.put("username", username);
            session.put("token", token);
            // If here, update timestamp
            String userTime = (String) Security.invoke("userTime");
            session.put("usertime", userTime);

            //Get data of user
            ER_User userTemp = ER_User.find("email",username).first();

            //If user´s distributor has custom logo...
            ER_Distributor_Custom_Logo customLogoDistributor = null;
            if(userTemp.distributor != null)
                customLogoDistributor = ER_Distributor_Custom_Logo.find("distributor.id", userTemp.distributor.id).first();

            if(customLogoDistributor != null && customLogoDistributor.active && customLogoDistributor.logoName != null ){
                session.put("customUserLogo", customLogoDistributor.logoName);
            }
            else{
                //If user has custom logo
                ER_User_Custom_Logo customLogo = ER_User_Custom_Logo.find("user.email", username).first();
                if(customLogo != null && customLogo.active && customLogo.logoName!=null){
                    session.put("customUserLogo", customLogo.logoName);
                }
            }

            response.setCookie("auth", username, "5d");
            // Redirect to the original URL (or /)
            redirectToOriginalURL();
        }
    }



    public static void logout() throws Throwable {
        Security.invoke("onDisconnect");
        session.clear();
        response.setCookie("auth", "", "5d");
        Security.invoke("onDisconnected");
        flash.success("secure.logout");
        login();
    }

    public static void logoutByForce(String username) throws Throwable {
        Security.invoke("onDisconnectByForce", username);
        session.clear();
        response.setCookie("auth", "", "5d");
        login();
    }
    // ~~~ Utils

    static void redirectToOriginalURL() throws Throwable {
        Security.invoke("onAuthenticated");
        String url = flash.get("url");
        if(url == null) {
            url = Play.ctxPath + "/";
        }
        redirect(url);
    }

    public static class Security extends Controller {

        /**
         * @Deprecated
         *
         * @param username
         * @param password
         * @return
         */
        static boolean authentify(String username, String password) {
            throw new UnsupportedOperationException();
        }

        /**
         * This method is called during the authentication process. This is where you check if
         * the user is allowed to log in into the system. This is the actual authentication process
         * against a third party system (most of the time a DB).
         *
         * @param username
         * @param password
         * @return true if the authentication process succeeded
         */
        static String authenticate(String username, String password) {
            return "true";
        }

        /**
         * This method checks that a profile is allowed to view this page/method. This method is called prior
         * to the method's controller annotated with the @Check method.
         *
         * @param profile
         * @return true if you are allowed to execute this controller method.
         */
        static boolean check(String profile) {
            return true;
        }

        /**
         * This method returns the current connected username
         * @return
         */
        static String connected() {
            return session.get("username");
        }

        /**
         * Indicate if a user is currently connected
         * @return  true if the user is connected
         */
        static boolean isConnected() {
            return session.contains("username");
        }

        /**
         * This method is called after a successful authentication.
         * You need to override this method if you with to perform specific actions (eg. Record the time the user signed in)
         */
        static void onAuthenticated() {
        }

        /**
         * This method is called before a user tries to sign off.
         * You need to override this method if you wish to perform specific actions (eg. Record the name of the user who signed off)
         */
        static void onDisconnect() {
        }

        /**
         * This method is called after a successful sign off.
         * You need to override this method if you wish to perform specific actions (eg. Record the time the user signed off)
         */
        static void onDisconnected() {
        }

        /**
         * This method is called to check if a session is still valid
         * You need to override this method if you wish to perform specific actions (eg. Record the time the user signed off)
         */
        static boolean validSession(String username, String token, String userTime) {
            return false;
        }

        /**
         * This method is called to generate a new token
         * You need to override this method if you wish to perform specific actions (eg. Record the time the user signed off)
         */
        static String generateToken(String username) {
            return "";
        }

        /**
         * This method is called to create a new user timestamp
         */
        static String userTime() {
            return "";
        }

        /**
         * This method is called when a user closed the window and we need to logout by force.
         * You need to override this method if you wish to perform specific actions (eg. Record the name of the user who signed off)
         */
        static void onDisconnectByForce(String username) {
        }


        /**
         * This method is called if a check does not succeed. By default it shows the not allowed page (the controller forbidden method).
         * @param profile
         */
        static void onCheckFailed(String profile) {
            forbidden();
        }

        private static Object invoke(String m, Object... args) throws Throwable {

            try {
                return Java.invokeChildOrStatic(Security.class, m, args);
            } catch(InvocationTargetException e) {
                throw e.getTargetException();
            }
        }

    }

}
