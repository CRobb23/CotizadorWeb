package controllers;

import play.Logger;
import play.Play;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;
import play.data.validation.Error;

import helpers.GeneralMethods;
import helpers.Filter;
import helpers.Filter.Operator;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.List;

import models.*;

import java.io.File;
import org.apache.commons.io.FileUtils;

@With(Secure.class)
@Check({"Administrador Maestro"})
public class AdminLogos extends AdminBaseController {

    /*
     * **********************************************************************
     * Logos List
     * **********************************************************************
     * */
    public static void search(String email, Boolean active, String all, boolean intern, boolean internalSeach){

        flash.clear();

        if(!internalSeach){
            session.remove("page");
        }

        if(!"".equals(all) && all!=null){
            session.remove("email");
            session.remove("active");
            logosList(null,null,true);
        }

        if(!intern){
            session.put("email", email);
            session.put("active", active == null ? "todos" : active.booleanValue()) ;
        }

        logosList(email, active, true);

    }

    public static void logosList(String email, Boolean active, boolean intern){

        if(!intern){
            session.remove("page");
            session.remove("email");
            session.remove("active");
        }else{
            params.flash();
        }

        filterLogos(email,active);

        render();
    }

    private static void filterLogos(String email, Boolean active){

        boolean validEmail = GeneralMethods.validateParameter(email);
        boolean validState = GeneralMethods.validateParameter(active);

        Filter filter = new Filter();

        if(validEmail){
            filter.addQuery("user.email like ?", "%"+email+"%");
        }
        if(validState){
            filter.addQuery("active = ?", active, Operator.AND);
        }

        //Exclude current user from results
        ER_User user = ER_User.find("byEmail", Security.connected()).first();
        if (user!=null) {
            filter.addQuery("user.id != ?", user.id, Operator.AND);
            filter.addQuery("user.email != ?", "admin@elroble.com", Operator.AND);
        }

        int page;
        if (params.get("page") == null) {
            // verifying if it was in other page
            if (session.get("page") == null) {
                page = 1;
            } else {
                page = Integer.parseInt(session.get("page"));
            }
        } else {
            page = Integer.parseInt(params.get("page"));
            session.put("page", page);
        }

        ModelPaginator logos = null;

        if (filter.isValidFilter()) {
            logos = new ModelPaginator(ER_User_Custom_Logo.class, filter.getQuery(), filter.getParametersArray());
            logos.setPageNumber(page);
            logos.setPageSize(10);
            renderArgs.put("logos", logos);
        }

    }


    /*
    * **********************************************************************
    * Users List
    * **********************************************************************
    * */
    public static void searchUser(String email, Long role, Boolean active, String all, boolean intern, boolean internalSearch){

        flash.clear();

        if(!internalSearch){
            session.remove("page");
        }

        //List all users if the all button is pressed
        if (!"".equals(all) && all!=null) {
            session.remove("email");
            session.remove("role");
            session.remove("active");

            editUserLogo(null, null, null, true);
        }

        //Render the list
        if(!intern) {
            session.put("email",email);
             session.put("role",role == null ? null : role);
            session.put("active", active == null ? "todos" : active.booleanValue());
        }
        editUserLogo(email, role, active,true);

    }

    public static void editUserLogo(String email, Long role, Boolean active, boolean intern) {

        if (!intern) {
            session.remove("page");
            session.remove("email");
            session.remove("role");
            session.remove("active");
            flash.clear();
        }else{
            params.flash();
        }

        List<ER_User_Role> roles = authorizedRolesForUser();
        renderArgs.put("roles", roles);

        //Add filtered users to the renderArgs
        filterUsers(email, role, active);

        //Render the list
        render();
    }

    private static void filterUsers(String email, Long role, Boolean active) {

        //Validate the parameters
        boolean validEmail = GeneralMethods.validateParameter(email);
        boolean validRole = GeneralMethods.validateParameter(role);
        boolean validState = GeneralMethods.validateParameter(active);

        //Create a new filter object and add the query for each valid parameter
        Filter filter = new Filter();

        if (validEmail) {
            filter.addQuery("email like ?", "%"+email+"%");
        }

        if (validRole) {
            filter.addQuery("role.id = ?", role, Operator.AND);
        }

        if (validState) {
            filter.addQuery("active = ?", active, Operator.AND);
        }

        //Exclude current user from results
        ER_User user = ER_User.find("byEmail", Security.connected()).first();
        if (user!=null) {
            filter.addQuery("id != ?", user.id, Operator.AND);
            filter.addQuery("email != ?", "admin@elroble.com", Operator.AND);
        }

        int page;
        if (params.get("page") == null) {
            // verifying if it was in other page
            if (session.get("page") == null) {
                page = 1;
            } else {
                page = Integer.parseInt(session.get("page"));
            }
        } else {
            page = Integer.parseInt(params.get("page"));
            session.put("page", page);
        }

        //Validate the filter to create the model paginator. If the filter is invalid, all users are returned.
        ModelPaginator users = null;
        if (filter.isValidFilter()) {
            users = new ModelPaginator(ER_User.class, filter.getQuery(), filter.getParametersArray());
            users.setPageNumber(page);
            users.setPageSize(10);

            renderArgs.put("users", users);
        }
    }



    /*
     * **********************************************************************
     * Edit Logos for Users
     * **********************************************************************
     * */
    public static void editLogo(Long userId, Long logoId){

        if(logoId!=null){
            //editando logo desde pagina principal
            ER_User_Custom_Logo customLogo = ER_User_Custom_Logo.findById(logoId);
            if( customLogo != null ){
                renderArgs.put("customLogo", customLogo);
                renderArgs.put("user", customLogo.user);
            }
        }
        else{
            //creando logo
            ER_User_Custom_Logo customLogo = ER_User_Custom_Logo.find("user.id", userId).first();
            if( customLogo != null ){
                renderArgs.put("customLogo", customLogo);
            }
            ER_User user = ER_User.findById(userId);
            if(user!=null)
                renderArgs.put("user", user);
        }

        render();
    }


    /*
     * **********************************************************************
     * Edit Logos of Users
     * **********************************************************************
     * */
    public static void deleteLogo(Long id){
        ER_User_Custom_Logo tcustomLogo = ER_User_Custom_Logo.findById(id);
        String customImgPath = Play.applicationPath.getAbsolutePath()+"/public/images/custom/";
        try
        {
            String l = tcustomLogo.logoName;
            String b = tcustomLogo.bannerName;
            File destination = new File(customImgPath, l);
            if(destination.exists())
                destination.delete();
            destination = new File(customImgPath, b);
            if(destination.exists())
                destination.delete();
            tcustomLogo.delete();
            flash.success(Messages.get("logo.delete.success"));
        }catch ( Exception ex )
        {
            flash.error(Messages.get("logo.delete.error"));
            Logger.error(ex,"logo: %d", id);
        }

        logosList(null,null,true);

    }

    /*
     * **********************************************************************
     * Edit Logos of Users
     * **********************************************************************
     * */

    private static String moveFiles(File origen, Long id, String pref) throws Exception{

        String customImgPath = Play.applicationPath.getAbsolutePath()+"/public/images/custom/";

        String destinationName = origen.getName();
        if(destinationName.lastIndexOf(".") != -1 && destinationName.lastIndexOf(".") != 0)
            destinationName = "u"+id + "_"+pref+ Calendar.getInstance().getTimeInMillis() + "." +destinationName.substring(destinationName.lastIndexOf(".")+1);

        File destination = new File(customImgPath, destinationName);

        if(destination.exists()){
            destination.delete();
        }
        FileUtils.moveFile(origen, destination);

        return destinationName;
    }

    private static void deleteFile(String name){
        String customImgPath = Play.applicationPath.getAbsolutePath()+"/public/images/custom/";
        File destination = new File(customImgPath, name);
        if(destination.exists()){
            destination.delete();
        }
    }

    public static void saveLogo(ER_User_Custom_Logo customLogo, ER_User user, File logoFile, File bannerFile, String delLogo, String delBanner){

        flash.clear();

        if(validation.hasErrors()){
            for(Error error : validation.errors()){
                Logger.error(error.message());
            }
            flash.error(Messages.get("user.create.fielderrors"));

            params.flash();
            validation.keep();

            editLogo(user.id, null);
        }else{

            try{

                if(customLogo.id == null){

                    if(logoFile == null && bannerFile == null ){
                        throw new Exception("Debe cargar al menus un archivo.");
                    }

                    if(logoFile != null)
                        customLogo.logoName = moveFiles(logoFile, user.id, "l");
                    if(bannerFile != null)
                        customLogo.bannerName = moveFiles(bannerFile, user.id,"b");

                    customLogo.user = ER_User.findById(user.id);
                    customLogo.save();
                    flash.success(Messages.get("logo.create.success"));

                }else{

                    if(delLogo != null){
                        deleteFile(customLogo.logoName);
                        customLogo.logoName = null;
                        customLogo.save();
                    }
                    else if(delBanner != null){
                        deleteFile(customLogo.bannerName);
                        customLogo.bannerName = null;
                        customLogo.save();
                    }
                    else{
                        if(logoFile != null)
                            customLogo.logoName = moveFiles(logoFile, customLogo.user.id,"l");

                        if(bannerFile != null)
                            customLogo.bannerName = moveFiles(bannerFile, customLogo.user.id,"b");

                        customLogo.save();
                    }

                    flash.success(Messages.get("logo.edit.success"));
                }

                params.flash();
                logosList(null,null,true);

            }
            catch(Exception ex){
                ex.printStackTrace();
                Logger.error(ex.getMessage());
                flash.error(ex.getMessage());
                flash.error(Messages.get("customLogo.create.fileerrors"));
                editLogo(user.id, customLogo.id);
            }

        }

    }


}
