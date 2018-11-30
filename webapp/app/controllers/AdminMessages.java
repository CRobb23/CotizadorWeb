package controllers;

import helpers.Filter;
import models.ER_Admin_Messages;
import play.modules.paginate.ModelPaginator;

public class AdminMessages extends AdminBaseController {

    private static void filterMail(String name) {

        //Validate the parameters
        boolean validInspection = true;
        if (name==null || name.isEmpty()) {
            validInspection = false;
        }
        //Create a new filter object and add the query for each valid parameter
        Filter filter = new Filter();

        if (validInspection) {
            filter.addQuery("messageName like ?", "%"+name+"%");
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

        //Validate the filter to create the model paginator. If the filter is invalid, all rows are returned.
        ModelPaginator Mails = null;
        if (filter.isValidFilter()) {
            Mails = new ModelPaginator(ER_Admin_Messages.class, filter.getQuery(), filter.getParametersArray());
        } else {
            Mails = new ModelPaginator(ER_Admin_Messages.class);
        }

        //inspections = new ModelPaginator(ER_Inspection.class);

        Mails.orderBy("id ASC");
        Mails.setPageNumber(page);
        Mails.setPageSize(10);
        renderArgs.put("Mails", Mails);

    }
    public static void list(String name, boolean intern) {
        flash.clear();
        if (!intern) {
            session.remove("page");
            session.remove("name");
        }
        filterMail(name);
        //if it was a search
        if(name!=null) {
            renderArgs.put("name",name);
        }
        render();
    }
    public static void searchMails(String description, boolean intern, boolean internalSearch){

        flash.clear();

        if(!internalSearch){
            session.remove("page");
        }
        //Add the http parameters to the flash scope
        params.flash();

        //Render the list
        if(!intern) {
            session.put("name",description);
        }
        list(description, true);
    }

    public static void edit(Long id) {
        ER_Admin_Messages mail = ER_Admin_Messages.findById(id);
        String mensaje="";
        renderArgs.put("mail", mail);

        switch (id.intValue()){
            case 2:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  cliente: {cliente} </br>usuario: {usuario} </br> clave: {password}";
                break;
            case 4:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Numero de inspección: {NoInspeccion}";
                break;
            case 5:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Linea: {linea}" +
                          "</br> Marca: {marca} </br> Año: {anio} </br> Placa: {placa} </br> Motor: {motor} </br> Chassis: {chasis}" +
                          "</br> Fecha expiracion: {fechaExp} </br> Caso: {caso} </br> Distribuidor: {distribuidor} </br>Cliente: {cliente}";
                break;
            case 6:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Linea: {linea}" +
                        "</br> Marca: {marca} </br> Año: {anio} </br> Placa: {placa} </br> Motor: {motor} </br> Chassis: {chasis}" +
                        "</br> Fecha expiracion: {fechaExp} </br> Caso: {caso} </br> Distribuidor: {distribuidor}";
                break;
            case 7:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Usuario: {usuario} </br> Contraseña: {password}";
                break;
            case 9:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Linea: {linea}" +
                        "</br> Marca: {marca} </br> Año: {anio} </br> Placa: {placa} </br> Motor: {motor} </br> Chassis: {chasis}" +
                        "</br> Fecha expiracion: {fechaExp} </br> Caso: {caso} </br> Distribuidor: {distribuidor} </br> Cliente: {cliente} </br> Vigencia:{vigencia}";
                break;
            case 10:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Caso: {caso}";
                break;
            case 11:
                mensaje = "Por favor para indicar datos parametrizables coloque de siguiente forma: </br>  Cliente: {cliente} </br> Poliza: {poliza}";
                break;

        }
        renderArgs.put("mensaje", mensaje);
        render();
    }

    public static void save(Long Mailid, String back, ER_Admin_Messages mail){
        if(back != null){
            renderTemplate("AdminMessages/list.html");
        }
        ER_Admin_Messages newmail = ER_Admin_Messages.findById(Mailid);
        newmail.body = mail.body;
        newmail.save();
        flash.success("Guardado exitosamente");
        edit(newmail.getId());
    }
}
