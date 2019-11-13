package controllers;

import helpers.Filter;
import helpers.GeneralMethods;
import models.ER_Deductible;
import models.ER_Discount_Extracharge;
import play.i18n.Messages;
import play.modules.paginate.ModelPaginator;
import play.mvc.With;

/**
 * Created by GEKO on 11/11/19.
 */
@With(Secure.class)
@Check({"Administrador maestro", "Gerente comercial", "Gerente de canal"})
public class AdminDiscountExtracharge extends AdminBaseController{
    private static void filterChargesDiscounts(String description){
        //validate the parameters
        boolean validDeductible = GeneralMethods.validateParameter(description);

        //Create a new Filter and write the query for each parameter
        Filter filter = new Filter();

        if(validDeductible) {
            filter.addQuery("name like ?", "%"+description+"%");
            //filter.addQuery("active = ?", true);
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
        ModelPaginator chargeList = null;
        if(filter.isValidFilter()) {
            chargeList = new ModelPaginator(ER_Discount_Extracharge.class,filter.getQuery(),filter.getParametersArray());
        }else {
            chargeList = new ModelPaginator(ER_Discount_Extracharge.class);
        }

        chargeList.orderBy("value DESC");
        chargeList.setPageSize(10);
        chargeList.setPageNumber(page);

        renderArgs.put("chargeList", chargeList);
    }
    public static void discountextracharge(String description, Boolean intern) {
        if(description == null)
            description = "";
        filterChargesDiscounts(description);
        render();
    }
    public static void searchCharge(String name){
        discountextracharge(name, false);
    }
    public static void edit(Long id){
        if(id != null){
            ER_Discount_Extracharge charge = ER_Discount_Extracharge.findById(id);
            if(charge != null) {
                renderArgs.put("id", charge.getId());
                renderArgs.put("name", charge.name);
                renderArgs.put("value", charge.value);
                renderArgs.put("type", charge.type);
            }
        }
        render();
    }
    public static void newChargeFromExcel(){

    }
    public static void down(Long id){
        flash.clear();
        if(id != null){
            ER_Discount_Extracharge charge = ER_Discount_Extracharge.findById(id);
            if(charge != null){
                charge.delete();
                flash.success(Messages.get("chargeList.delete.success"));
            }else{
                flash.error(Messages.get("chargeList.delete.error"));
            }
            discountextracharge("",false);
        }
    }
    public static void save(Long id, String name, Double value,String type){
        flash.clear();

        if(validation.hasErrors()) {
            params.flash();
            flash.error(Messages.get("chargeList.create.fielderrors"));
            validation.keep();
            edit(id);
        } else {

            if( id == null ) {
                ER_Discount_Extracharge charge = new ER_Discount_Extracharge();
                charge.name = name;
                charge.value = value;
                charge.active = true;
                charge.type = type;

                charge.save();
                flash.success(Messages.get("chargeList.form.create.success",1));
            } else {
                ER_Discount_Extracharge discount_extracharge = ER_Discount_Extracharge.findById(id);
                discount_extracharge.name = name;
                discount_extracharge.value = value;
                discount_extracharge.type = type;
                discount_extracharge.save();
                flash.success(Messages.get("chargeList.update.success"));
            }

        }
        discountextracharge("",false);
    }
}

