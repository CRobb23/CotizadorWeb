package controllers;

import helpers.Filter;
import helpers.Filter.Operator;
import helpers.GeneralMethods;
import models.*;
import org.apache.commons.io.FileUtils;
import play.Logger;
import play.Play;
import play.data.validation.Error;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

import java.io.File;
import java.util.Calendar;
import java.util.List;

@With(Secure.class)
@Check({"Administrador Maestro"})
public class AdminLogosDistributor extends AdminBaseController {

    /*
     * **********************************************************************
     * Logos List
     * **********************************************************************
     * */
    public static void search(String distributor, Boolean active, String all, boolean intern, boolean internalSeach){

        flash.clear();

        if(!internalSeach){
            session.remove("page");
        }

        if(!"".equals(all) && all!=null){
            session.remove("distributor");
            session.remove("active");
            logosList(null,null,true);
        }

        if(!intern){
            session.put("distributor", distributor);
            session.put("active", active == null ? "todos" : active.booleanValue()) ;
        }

        logosList(distributor, active, true);

    }

    public static void logosList(String distributor, Boolean active, boolean intern){

        if(!intern){
            session.remove("page");
            session.remove("distributor");
            session.remove("active");
        }else{
            params.flash();
        }

        filterLogos(distributor,active);

        render();
    }

    private static void filterLogos(String distributor, Boolean active){

        boolean validDistributor = GeneralMethods.validateParameter(distributor);
        boolean validState = GeneralMethods.validateParameter(active);

        Filter filter = new Filter();

        if(validDistributor){
            filter.addQuery("distributor.name like ?", "%"+distributor+"%");
        }
        if(validState){
            filter.addQuery("active = ?", active, Operator.AND);
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
            logos = new ModelPaginator(ER_Distributor_Custom_Logo.class, filter.getQuery(), filter.getParametersArray());
            logos.setPageNumber(page);
            logos.setPageSize(10);
            renderArgs.put("logos", logos);
        }else{
            logos = new ModelPaginator(ER_Distributor_Custom_Logo.class);
            logos.setPageNumber(page);
            logos.setPageSize(10);
            renderArgs.put("logos", logos);
        }

    }


    /*
    * **********************************************************************
    * Distributors List
    * **********************************************************************
    * */
    public static void searchDistributor(String distributor, Boolean active, String all, boolean intern, boolean internalSearch){

        flash.clear();

        if(!internalSearch){
            session.remove("page");
        }

        //List all distributors if the all button is pressed
        if (!"".equals(all) && all!=null) {
            session.remove("distributor");
            session.remove("active");

            editDistributorLogo(null, null, true);
        }

        params.flash();

        //Render the list
        if(!intern) {
            session.put("distributor",distributor);
            session.put("active", active == null ? "todos" : active.booleanValue());
        }
        editDistributorLogo(distributor, active,true);

    }

    public static void editDistributorLogo(String distributor, Boolean active, boolean intern) {

        if (!intern) {
            session.remove("page");
            session.remove("distributor");
            session.remove("active");
            flash.clear();
        }else{
            params.flash();
        }

        //Add filtered distributors to the renderArgs
        filterDistributors(distributor, active);

        //Render the list
        render();
    }

    private static void filterDistributors(String distributor, Boolean active) {

        //Validate the parameters
        boolean validDistributor = GeneralMethods.validateParameter(distributor);
        boolean validState = GeneralMethods.validateParameter(active);

        //Create a new filter object and add the query for each valid parameter
        Filter filter = new Filter();

        if (validDistributor) {
            filter.addQuery("name like ?", "%"+distributor+"%");
        }

        if (validState) {
            filter.addQuery("active = ?", active, Operator.AND);
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

        //Validate the filter to create the model paginator. If the filter is invalid, all distributors are returned.
        ModelPaginator distributors = null;
        if (filter.isValidFilter()) {
            distributors = new ModelPaginator(ER_Distributor.class, filter.getQuery(), filter.getParametersArray());
            distributors.setPageNumber(page);
            distributors.setPageSize(10);

            renderArgs.put("distributors", distributors);
        }
        else{
            distributors = new ModelPaginator(ER_Distributor.class);
            distributors.setPageNumber(page);
            distributors.setPageSize(10);

            renderArgs.put("distributors", distributors);
        }
    }



    /*
     * **********************************************************************
     * Edit Logos for Distributors
     * **********************************************************************
     * */
    public static void editLogo(Long distributorId, Long logoId){

        if(logoId!=null){
            //editando logo desde pagina principal
            ER_Distributor_Custom_Logo customLogo = ER_Distributor_Custom_Logo.findById(logoId);
            if( customLogo != null ){
                renderArgs.put("customLogo", customLogo);
                renderArgs.put("distributor", customLogo.distributor);
            }
        }
        else{
            //creando logo
            ER_Distributor_Custom_Logo customLogo = ER_Distributor_Custom_Logo.find("distributor.id", distributorId).first();
            if( customLogo != null ){
                renderArgs.put("customLogo", customLogo);
            }
            ER_Distributor distributor = ER_Distributor.findById(distributorId);
            if(distributor!=null)
                renderArgs.put("distributor", distributor);
        }

        render();
    }


    /*
     * **********************************************************************
     * Edit Logos of Distributors
     * **********************************************************************
     * */
    public static void deleteLogo(Long id){
        ER_Distributor_Custom_Logo tcustomLogo = ER_Distributor_Custom_Logo.findById(id);
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
     * Edit Logos of Distributors
     * **********************************************************************
     * */

    private static String moveFiles(File origen, Long id, String pref) throws Exception{

        String customImgPath = Play.applicationPath.getAbsolutePath()+"/public/images/custom/";

        String destinationName = origen.getName();
        if(destinationName.lastIndexOf(".") != -1 && destinationName.lastIndexOf(".") != 0)
            destinationName = "d"+id + "_"+pref+ Calendar.getInstance().getTimeInMillis() + "." +destinationName.substring(destinationName.lastIndexOf(".")+1);

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

    public static void saveLogo(ER_Distributor_Custom_Logo customLogo, ER_Distributor distributor, File logoFile, File bannerFile, String delLogo, String delBanner){

        flash.clear();

        if(validation.hasErrors()){
            for(Error error : validation.errors()){
                Logger.error(error.message());
            }
            flash.error(Messages.get("distributor.create.fielderrors"));

            params.flash();
            validation.keep();

            editLogo(distributor.id, null);
        }else{

            try{

                if(customLogo.id == null){

                    if(logoFile == null && bannerFile == null ){
                        throw new Exception("Debe cargar al menos un archivo.");
                    }

                    if(logoFile != null)
                        customLogo.logoName = moveFiles(logoFile, distributor.id, "l");
                    if(bannerFile != null)
                        customLogo.bannerName = moveFiles(bannerFile, distributor.id,"b");

                    customLogo.distributor = ER_Distributor.findById(distributor.id);
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
                            customLogo.logoName = moveFiles(logoFile, customLogo.distributor.id,"l");

                        if(bannerFile != null)
                            customLogo.bannerName = moveFiles(bannerFile, customLogo.distributor.id,"b");

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
                editLogo(distributor.id, customLogo.id);
            }

        }

    }


}
