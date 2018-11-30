package controllers;

import helpers.ERConstants;
import helpers.GeneralMethods;
import models.ER_Incident;
import models.ER_Incident_Status;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;
import play.i18n.Messages;

@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminIncidents extends AdminBaseController {

    private static void filterInspections(String number) {
        try{
        //Validate the parameters
        boolean validInspection = GeneralMethods.validateParameter(number);
            String query = " 1=1 ";
        if (validInspection) {
            query += "and number = "+number;
        }
            query += " and status_id = " + ERConstants.INCIDENT_STATUS_APPROVED_INSPECTION;
            query += " and finalizer_id is null";
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

        //Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
        ModelPaginator incidents= null;
            incidents = new ModelPaginator(ER_Incident.class,query);
        incidents.orderBy("creationDate desc");
        incidents.setPageNumber(page);
        incidents.setPageSize(10);
        renderArgs.put("incidents", incidents);
        }catch(Exception e){
            e.printStackTrace();
            e.getMessage();
        }
    }

    public static void pendingIncidents(String number, boolean intern) {

        if (!intern) {
            session.remove("page");
            session.remove("number");
        }

        //Add inspections to the renderArgs
        filterInspections(number);

        //if it was a search
        if(number!=null) {
            renderArgs.put("number",number);
        }
        //Render the list */
        render();
    }

    public static void searchIncidents(String number, String all, boolean intern, boolean internalSearch){

        flash.clear();

        if(!internalSearch){
            session.remove("page");
        }

        //List all if the all button is pressed
        if (!"".equals(all) && all!=null) {
            session.remove("number");
            pendingIncidents(null,true);
        }

        //Add the http parameters to the flash scope
        params.flash();

        //Render the list
        if(!intern) {
            session.put("name",number);
        }
        pendingIncidents(number, true);
    }
}

