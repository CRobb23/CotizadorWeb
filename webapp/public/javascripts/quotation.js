function init(args){
    initValidator();
    $(".typeClient").change(showFieldsClient);
    $(".typeClient").trigger("change");
    $(".typePayer").change(showFieldsPayerType);
    $(".typePayer").trigger("change");
    $("#useDataClientPayer").change(showFieldsPayer);
    $("#useDataClientPayer").trigger("change");
    $("#chargeType").change(showFieldsCreditCard);
    $("#chargeType").trigger("change");
    $("#armor").change(disabledArmorValue);
    $("#armor").trigger("change");
    $("#warranty").change(disabledBeneficiaries);
    $("#warranty").trigger("change");
    $(".datePicker").datetimepicker({
        locale: "es",
        format: "DD/MM/YYYY"
    });
    $("#country").change(refreshDepend);
    $("#country_work").change(refreshDepend);
    $("#country_business").change(refreshDepend);
    $("#country_legal").change(refreshDepend);
    $("#country_payer").change(refreshDepend);
    $("#country_work_payer").change(refreshDepend);
    $("#country_business_payer").change(refreshDepend);
    $("#country_legal_payer").change(refreshDepend);

  addEventSelectDepend("country", "department", args);
  addEventSelectDepend("department", "municipality", args);
  addEventSelectDepend("municipality", "zone", args);
  addEventSelectDepend("country_work", "work_department", args);
  addEventSelectDepend("work_department", "work_municipality", args);
  addEventSelectDepend("work_municipality", "work_zone", args);
  addEventSelectDepend("country_business", "department_business", args);
  addEventSelectDepend("department_business", "municipality_business", args);
  addEventSelectDepend("municipality_business", "zone_business", args);
  addEventSelectDepend("country_legal", "department_legal", args);
  addEventSelectDepend("department_legal", "municipality_legal", args);
  addEventSelectDepend("municipality_legal", "zone_legal", args);
  addEventSelectDepend("country_payer", "department_payer", args);
  addEventSelectDepend("department_payer", "municipality_payer", args);
  addEventSelectDepend("municipality_payer", "zone_payer", args);
  addEventSelectDepend("country_work_payer", "work_payer_department", args);
  addEventSelectDepend("work_payer_department", "work_payer_municipality", args);
  addEventSelectDepend("work_payer_municipality", "work_payer_zone", args);
  addEventSelectDepend("country_business_payer", "department_business_payer", args);
  addEventSelectDepend("department_business_payer", "municipality_business_payer", args);
  addEventSelectDepend("municipality_business_payer", "zone_business_payer", args);
  addEventSelectDepend("country_legal_payer", "department_legal_payer", args);
  addEventSelectDepend("department_legal_payer", "municipality_legal_payer", args);
  addEventSelectDepend("municipality_legal_payer", "zone_legal_payer", args);

  $("#brand_line").change(function(){
        var selectelement = $(this);
        selectelement.prop("disabled", true);
        $("#line").html("<option value=''>" + args.formSelectLoading + "</option>");
        var id = $(this).val();
        $.ajax({
                type: "POST",
                url: args.urlGetLinesForBrand,
                data: {id: id},
                success: function(response){
                $("#line").html("<option value=''>" + args.formSelect + "</option>");
                if(response.success){
                    var html = "";
                    $.each(response.lines, function(index, line){
                        html += "<option value='" + line.id + "'>" + line.name + "</option>";
                    });
                    $("#line").append(html);
                }
              },
              error: function(){
                 $("#line").html("<option value=''>" + args.formSelectError + "</option>");
              },
              complete: function(){
                  selectelement.prop("disabled", false);
              }
        });
    });
}

function addEventSelectDepend(idSelectDepend, idSelect, args){
    $("#" + idSelectDepend).change(function(){
        var selectelement = $(this);
        selectelement.prop("disabled", true);
        $("#" + idSelect).html("<option value=''>" + args.formSelectLoading + "</option>");
        var id = $(this).val();
        $.ajax({
               type: "POST",
               url: args.urlGetGeographicAreaChildren,
               data: {id: id,tipo:idSelect},
               success: function(response){
               $("#" + idSelect).html("<option value=''>" + args.formSelect + "</option>");
               if(response.success){
                   var html = "";
                   $.each(response.areas, function(index, area){
                    html += "<option value=''" + area.id + "'>" + area.name + "</option>";
                   });
                   $("#" + idSelect).append(html);
               }
             },
             error: function(){
               $("#" + idSelect).html("<option value=''>" + args.formSelectError + "</option>");
             },
             complete: function(){
                selectelement.prop("disabled", false);
             }
        });
    });
}

function refreshDepend(){
    if($(this).attr("id") == "country"){
       $("#municipality").html("<option value=''>Seleccionar</option>");
       $("#zone").html("<option value=''>Seleccionar</option>");
    }
    if($(this).attr("id") == "country_work"){
       $("#work_municipality").html("<option value=''>Seleccionar</option>");
       $("#work_zone").html("<option value=''>Seleccionar</option>");
    }
    if($(this).attr("id") == "country_business"){
       $("#municipality_business").html("<option value=''>Seleccionar</option>");
       $("#zone_business").html("<option value=''>Seleccionar</option>");
    }
    if($(this).attr("id") == "country_legal"){
       $("#municipality_legal").html("<option value=''>Seleccionar</option>");
       $("#zone_legal").html("<option value=''>Seleccionar</option>");
    }
    if($(this).attr("id") == "country_payer"){
       $("#municipality_payer").html("<option value=''>Seleccionar</option>");
       $("#zone_payer").html("<option value=''>Seleccionar</option>");
    }
    if($(this).attr("id") == "country_work_payer"){
       $("#work_payer_municipality").html("<option value=''>Seleccionar</option>");
       $("#work_payer_zone").html("<option value=''>Seleccionar</option>");
    }
}

function showFieldsPayer(){
  if(!$(this).is(":checked")){
        $(".fieldsPayer input").removeAttr("disabled");
        $(".fieldsPayer select").removeAttr("disabled");
        $(".fieldsPayer").show();
  }else{
        $(".fieldsPayer").hide();
        $(".fieldsPayer input").attr("disabled", "disabled");
        $(".fieldsPayer select").attr("disabled", "disabled");
  }
}

function showFieldsClient(){
    if($(this).attr("id") == "clienteIndividual" && $(this).is(":checked")){
        $(".fieldsClient input").removeAttr("disabled");
        $(".fieldsClient select").removeAttr("disabled");
        $(".fieldsClient").show();
        $(".fieldsLegal").hide();
        $(".fieldsLegal input").attr("disabled", "disabled");
        $(".fieldsLegal select").attr("disabled", "disabled");
    }else if($(this).attr("id") == "clienteJuridico" && $(this).is(":checked")){
        $(".fieldsLegal input").removeAttr("disabled");
        $(".fieldsLegal select").removeAttr("disabled");
        $(".fieldsLegal").show();
        $(".fieldsClient").hide();
        $(".fieldsClient input").attr("disabled", "disabled");
        $(".fieldsClient select").attr("disabled", "disabled");
	}
}

function showFieldsPayerType(){
  if($(this).attr("id") == "pagadorIndividual" && $(this).is(":checked")){
    $(".fieldsClientPayer input").removeAttr("disabled");
    $(".fieldsClientPayer select").removeAttr("disabled");
    $(".fieldsClientPayer").show();
    $(".fieldsLegalPayer").hide();
    $(".fieldsLegalPayer input").attr("disabled", "disabled");
    $(".fieldsLegalPayer select").attr("disabled", "disabled");
  }else if($(this).attr("id") == "pagadorJuridico" && $(this).is(":checked")){
    $(".fieldsLegalPayer input").removeAttr("disabled");
    $(".fieldsLegalPayer select").removeAttr("disabled");
    $(".fieldsLegalPayer").show();
    $(".fieldsClientPayer").hide();
    $(".fieldsClientPayer input").attr("disabled", "disabled");
    $(".fieldsClientPayer select").attr("disabled", "disabled");
  }
}

function showFieldsCreditCard(){
    if($("#chargeType").val() == 2){
        $(".bank").show();
        $(".creditCard").hide();
        $(".field").show();
    }else if($("#chargeType").val() == 3){
        $(".creditCard").show();
        $(".bank").hide();
        $(".field").show();
    }else{
        $(".creditCard").hide();
        $(".bank").hide();
        $(".fields").hide();
    }
}

function disabledArmorValue(){
    if($("#armor").val() == "S"){
        $("#vehicle_armorValue").removeAttr("disabled","");
    }else{
        $("#vehicle_armorValue").attr("disabled","disabled");
    }
}

function disabledBeneficiaries(){
    if($("#warranty").val() == "S"){
        $("#vehicle_beneficiaries_id").removeAttr("disabled","");
    }else{
        $("#vehicle_beneficiaries_id").attr("disabled","disabled");
    }
}

function initValidator(){
    var validator = $("#formClient").validate({
        rules: {
              "client.taxNumber": {
        validarNit: true,
        required: {
          depends: function(){
            return $(this).attr("validate") == "true";
          }
        }
      },
      "client.firstName": {
        required: true,
        maxlength: 100
      },
      "client.firstSurname": {
        required: true,
        maxlength: 100
      },
      "client.birthdate": {
        required: true
      },
      "client.sex.id": {
        required: true
      },
      "client.profession.id": {
        required: true
      },
      "client.identificationDocument": {
        maxlength: 30,
        number: true,
        required: {
                  depends: function(){
                           return $("#client_passport").val().trim() == "";
                  }
            }
      },
      "client.passport": {
        maxlength: 50,
        required: {
                  depends: function(){
                           return $("#client_identificationDocument").val().trim() == "";
                  }
             }
      },
      "client.civilStatus.id": {
        required: true
      },
      "client.nationality.id": {
        required: true
      },
      "client.typeLicense.id": {
        required: true
      },
      "client.licenseNumber": {
        required: true,
        number: true,
        maxlength: 30
      },
      "client.email": {
        required: true,
        maxlength: 75
      },
      "client.address": {
        required: true,
        maxlength: 255
      },
      "client.addressWork": {
        required: true,
        maxlength: 255
      },
      "client.country.id": {
        required: true
      },
      "client.department.id": {
        required: {
          depends: function(){
            let options = jQuery("#department option");
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "client.municipality.id": {
        required: {
                   depends: function(){
            let options = jQuery("#municipality option");
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
                      }
                }
      },
      "client.zone.id": {
        required: {
                  depends: function(){
                      let options = jQuery("#zone option");
                      var required = true;
                      if(options.size() < 2){
                         required = false;
                      }
                      return required;
                   }
             }
      },
      "client.phoneNumber1": {
        required: true,
        number: true
      },
        "client.phoneNumber2": {
            number: true,
            required:true,
            isCellphone:true,
            minlength: 8,
            maxlength: 8
         },
        "client.phoneNumber3": {
            number: true
        },
      "client.countryWork.id": {
        required: true
      },
      "client.workDepartment.id": {
        required: {
                  depends: function(){
            let options = jQuery("#work_department option");
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
                 }
            }
      },
      "client.workMunicipality.id": {
        required: {
            depends: function(){
            let options = jQuery("#work_municipality option");
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
                   }
            }
      },
      "client.workZone.id": {
        required: {
                   depends: function(){
                   let options = jQuery("#work_zone option");
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
                  }
            }
      },
      "client.phoneNumberWork1": {
        required: true,
        number: true
      },
      /*Business Client*/
      "client.societyType.id": {
        required: true
      },
      "client.companyName": {
        required: true
      },
      "client.businessName": {
        required: true
      },
      "client.economicActivity.id": {
        required: true
      },
      "client.writeNumber": {
        required: true
      },
      "client.writeDate": {
        required: true
      },
      "client.department.id": {
        required: {
          depends: function(){
            let options = jQuery("#department_business option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "client.municipality.id": {
        required: {
          depends: function(){
            let options = jQuery("#municipality_business option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "client.zone.id": {
        required: {
          depends: function(){
            let options = jQuery("#zone_business option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      /*Legal Representative*/
      "legalRepresentative.taxNumber": {
        validarNit: true,
        required: {
                   depends: function(){
                   return $(this).attr("validate") == "true";
                   }
                }
      },
      "legalRepresentative.firstName": {
        required: true,
        maxlength: 100
      },
      "legalRepresentative.firstSurname": {
        required: true,
        maxlength: 100
      },
      "legalRepresentative.birthdate": {
        required: true
      },
      "legalRepresentative.sex.id": {
        required: true
      },
      "legalRepresentative.profession.id": {
        required: true
      },
      "legalRepresentative.identificationDocument": {
        maxlength: 30,
        number: true,
        required: {
               depends: function(){
                 return $("#legalRepresentative_passport").val().trim() == "";
                }
               }
      },
      "legalRepresentative.passport": {
        maxlength: 50,
        required: {
                    depends: function(){
                    return $("#legalRepresentative_identificationDocument").val().trim() == "";
                    }
                   }
      },
      "legalRepresentative.civilStatus.id": {
        required: true
      },
      "legalRepresentative.nationality.id": {
        required: true,
      },
      "legalRepresentative.email": {
        required: true,
        maxlength: 75
      },
      "legalRepresentative.registry": {
        required: true
      },
      "legalRepresentative.caseFile": {
        required: true
      },
      "legalRepresentative.extendedIn": {
        required: true
      },
      "legalRepresentative.registrationDate": {
        required: true
      },
      "legalRepresentative.book": {
        required: true
      },
      "legalRepresentative.folio": {
        required: true
      },
      "legalRepresentative.country.id": {
        required: true
      },
      "legalRepresentative.department.id": {
        required: {
            depends: function(){
            let options = jQuery("#department_legal option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
            }
      },
      "legalRepresentative.municipality.id": {
        required: {
            depends: function(){
            let options = jQuery("#municipality_legal option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
           }
      },
      "legalRepresentative.zone.id": {
        required: {
          depends: function(){
            let options = jQuery("#municipality_legal option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
         }
      },

      /*Client Payer*/
      "payer.taxNumber": {
        required: true,
        validarNit: true
      },
      "payer.firstName": {
        required: true
      },
      "payer.firstSurname": {
        required: true
      },
      "payer.birthdate": {
        required: true
      },
      "payer.sex.id": {
        required: true
      },
      "payer.profession.id": {
        required: true
      },
      "payer.identificationDocument": {
        maxlength: 30,
        number: true,
        required: {
                    depends: function(){
                    return $("#payer_passport").val().trim() == "";
                 }
            }
      },
      "payer.passport": {
        maxlength: 50,
        required: {
                  depends: function(){
                  return $("#payer_identificationDocument").val().trim() == "";
                  }
              }
      },
      "payer.civilStatus.id": {
        required: true
      },
      "payer.nationality.id": {
        required: true
      },
      "payer.licenseType.id": {
        required: true
      },
      "payer.licenseNumber": {
        required: true,
        number: true
      },
      "payer.email": {
        required: true
      },
      "payer.address": {
        required: true
      },
      "payer.country.id": {
        required: true
      },
      "payer.department.id": {
        required: {
          depends: function(){
            let options = jQuery("#department_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
             }
          }
      },
      "payer.municipality.id": {
        required: {
            depends: function(){
            let options = jQuery("#municipality_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
         }
      },
      "payer.zone.id": {
        required: {
            depends: function(){
            let options = jQuery("#zone_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
          }
      },
      "payer.phoneNumber1": {
        required: true,
        number: true
      },
      "payer.addressWork": {
        required: true
      },
      "payer.countryWork.id": {
        required: true
      },
      "payer.workDepartment.id": {
        required: {
            depends: function(){
            let options = jQuery("#work_payer_department option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
          }
      },
      "payer.workMunicipality.id": {
        required: {
            depends: function(){
            let options = jQuery("#work_payer_municipality option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
          }
      },
      "payer.workZone.id": {
        required: {
            depends: function(){
            let options = jQuery("#work_payer_zone option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
            }
          }
      },
      "payer.phoneNumberWork1": {
        required: true,
        number: true
      },

      /*Business Client Payer*/
      "payer.societyType.id": {
        required: true
      },
      "payer.companyName": {
        required: true
      },
      "payer.businessName": {
        required: true
      },
      "payer.economicActivity.id": {
        required: true
      },
      "payer.nationality.id": {
        required: true
      },
      "payer.writeNumber": {
        required: true
      },
      "payer.writeDate": {
        required: true
      },
      "payer.department.id": {
        required: {
          depends: function(){
            let options = jQuery("#department_business_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "payer.municipality.id": {
        required: {
          depends: function(){
            let options = jQuery("#municipality_business_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "payer.zone.id": {
        required: {
          depends: function(){
            let options = jQuery("#zone_business_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      /*Legal Representative*/
      "legalRepresentativePayer.taxNumber": {
        validarNit: true,
        required: {
          depends: function(){
            return $(this).attr("validate") == "true";
          }
        }
      },
      "legalRepresentativePayer.firstName": {
        required: true,
        maxlength: 100
      },
      "legalRepresentativePayer.firstSurname": {
        required: true,
        maxlength: 100
      },
      "legalRepresentativePayer.birthdate": {
        required: true
      },
      "legalRepresentativePayer.sex.id": {
        required: true
      },
      "legalRepresentativePayer.profession.id": {
        required: true
      },
      "legalRepresentativePayer.identificationDocument": {
        maxlength: 30,
        number: true,
        required: {
          depends: function(){
            return $("#legalRepresentative_passport").val().trim() == "";
          }
        }
      },
      "legalRepresentativePayer.passport": {
        maxlength: 50,
        required: {
          depends: function(){
            return $("#legalRepresentative_identificationDocument").val().trim() == "";
          }
        }
      },
      "legalRepresentativePayer.civilStatus.id": {
        required: true
      },
      "legalRepresentativePayer.nationality.id": {
        required: true,
        maxlength: 50
      },
      "legalRepresentativePayer.email": {
        required: true,
        maxlength: 75
      },
      "legalRepresentativePayer.registry": {
        required: true
      },
      "legalRepresentativePayer.caseFile": {
        required: true
      },
      "legalRepresentativePayer.extendedIn": {
        required: true
      },
      "legalRepresentativePayer.registrationDate": {
        required: true
      },
      "legalRepresentativePayer.book": {
        required: true
      },
      "legalRepresentativePayer.folio": {
        required: true
      },
      "legalRepresentativePayer.country.id": {
        required: true
      },
      "legalRepresentativePayer.department.id": {
        required: {
          depends: function(){
            let options = jQuery("#department_legal_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "legalRepresentativePayer.municipality.id": {
        required: {
          depends: function(){
            let options = jQuery("#municipality_legal_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },
      "legalRepresentativePayer.zone.id": {
        required: {
          depends: function(){
            let options = jQuery("#municipality_legal_payer option")
            var required = true;
            if (options.size() < 2) {
              required = false;
            }
            return required;
          }
        }
      },

      /*Client Multimedia*/
      "dpi": {
        required: {
                depends: function(){
                return $("#urlDPI").val().trim() == "";
             	}
             }
      },
      "receiptServices": {
        required: {
                  depends: function(){
                  return $("#urlReceiptServices").val().trim() == ""
                  && jQuery("#circulationCard")[0].files.length == 0;
                  }
                 }
      },
      "circulationCard": {
        required: {
                   depends: function(){
                    return $("#urlCirculationCard").val().trim() == ""
                    && jQuery("#receiptServices")[0].files.length == 0;
                   }
                   }
      },
      "driverLicence": {
        required: {
                  depends: function(){
                  return $("#urlDriverLicence").val().trim() == "";
                  }
                }
      },
      "authorizationForm": {
        required:  {
                   depends: function(){
                   if($("#isIndividual").is(":checked") && $("#urlAuthorizationForm").val().trim() == ""){
                        return true;
                        }else if(!$("#isIndividual").is(":checked") && $("#urlAuthorizationForm_business").val().trim() == ""){
                        return true;
                   }
            return false;
            }
         }
      },
      /*Business Client Multimedia*/
      "scanPatents": {
        required: {
                  depends: function(){
                  return $("#urlScanPatents").val().trim() == "";
                  }
        }
      },
      "scanLegalRepresentativeAppointment": {
        required: {
                  depends: function(){
                  return $("#urlScanLegalRepresentativeAppointment").val().trim() == "";
                  }
        }
      },
      "dpiLegalRepresentative": {
        required: {
                  depends: function(){
                  return $("#urlDPILegalRepresentative").val().trim() == "";
                  }
        }
      },

      /*Vehicle*/
      "vehicle.chassis": {
        required: true,
        minlength: 17,
        maxlength: 17
      },
      "vehicle.engine": {
        required: true
      },
      "vehicle.plateType.id": {
        required: true
      },
      "vehicle.plate": {
        required: {
                   depends: function(){
                    return $("#plateType").val().trim() != "9";
                   }
        },
        validatePlate: {
                   depends: function(){
                    return $("#plateType").val().trim() != "9";
                   }
        }
      },
      "vehicle.brand.id": {
        required: true
      },
      "vehicle.line.id": {
        required: true
      },
      "vehicle.type.id": {
        required: true
      },
      "vehicle.rate.id": {
        required: true
      },
      "vehicle.erYear.id": {
        required: true
      },
      "vehicle.armor": {
        required: true
      },
      "vehicle.alarmCode": {
        required: true
      },
      "vehicle.numberOfPassengers": {
        required: true
      },
      "vehicle.color": {
        required: true
      },
      "vehicle.mileage": {
        required: true
      },
      "vehicle.loanNumber": {
        required: true
      },
      "vehicle.warranty": {
        required: true
      },
      "vehicle.numberDoor":{
        required: true
      },
      "vehicle.reminderType.id":{
        required: true
      },

      /* Payment */
      "payment.chargeType.id":{
        required : true
      },
      "payment.numberAccountCard":{
        required : true
      },
      "payment.bankAccountType.id":{
        required : true
      },
      "payment.bank.id":{
        required : true
      },
      "payment.cardType.id":{
        required : true
      },
      "payment.cardClass.id":{
        required : true
      },
      "payment.expirationDate":{
        required : true
      },
      "payment.codeAccountCard":{
        required : true
      }
    },
    messages: {
      "client.taxNumber": {
        required: "Requerido",
        validarNit : "NIT inválido"
      },
      "client.firstName": {
        required: "Requerido",
        maxlength: "100 caracteres maximo"
      },
      "client.firstSurname": {
        required: "Requerido",
        maxlength: "100 caracteres maximo"
      },
      "client.birthdate": "Requerido",
      "client.sex.id": "Requerido",
      "client.profession.id": "Requerido",
      "client.identificationDocument": {
        required: "Requerido",
        number: "Solo debe contener números",
        maxlength: "30 caracteres maximo"
      },
      "client.passport": {
        required: "Requerido",
        maxlength: "50 caracteres maximo"
      },
      "client.civilStatus.id": "Requerido",
      "client.nationality.id": "Requerido",
      "client.typeLicense.id": "Requerido",
      "client.licenseNumber": {
        required: "Requerido",
        number: "Solo debe contener números",
        maxlength: "30 caracteres maximo"
      },
      "client.email": {
        required: "Requerido",
        maxlength: "75 caracteres maximo"
      },
      "client.address": {
        required: "Requerido",
        maxlength: "255 caracteres maximo"
      },
      "client.addressWork": {
        required: "Requerido",
        maxlength: "255 caracteres maximo"
      },
      "client.country.id": "Requerido",
      "client.department.id": "Requerido",
      "client.municipality.id": "Requerido",
      "client.zone.id": "Requerido",
      "client.phoneNumber1": {
        required: "Requerido",
        number: "Solo debe contener números"
      },
        "client.phoneNumber2": {
            number: "Solo debe contener números",

        },
        "client.phoneNumber3": {
            number: "Solo debe contener números"
        },
      "client.countryWork.id": "Requerido",
      "client.workDepartment.id": "Requerido",
      "client.workMunicipality.id": "Requerido",
      "client.workZone.id": "Requerido",
      "client.phoneNumberWork1": {
        required: "Requerido",
        number: "Solo debe contener números"
      },

      /*Business Client*/
      "client.societyType.id": "Requerido",
      "client.companyName": "Requerido",
      "client.businessName": "Requerido",
      "client.economicActivity.id": "Requerido",
      "client.writeNumber": "Requerido",
      "client.writeDate": "Requerido",

      /*Legal Representative*/
      "legalRepresentative.taxNumber":  {
        required: "Requerido",
        validarNit : "NIT inválido"
      },
      "legalRepresentative.firstName": {
        required: "Requerido",
        maxlength: "100 caracteres maximo"
      },
      "legalRepresentative.firstSurname": {
        required: "Requerido",
        maxlength: "100 caracteres maximo"
      },
      "legalRepresentative.birthdate": "Requerido",
      "legalRepresentative.sex.id": "Requerido",
      "legalRepresentative.profession.id": "Requerido",
      "legalRepresentative.identificationDocument": {
        required: "Requerido",
        maxlength: "30 caracteres maximo",
        number: "Solo debe contener números"
      },
      "legalRepresentative.passport": {
        required: "Requerido",
        maxlength: "50 caracteres maximo"
      },
      "legalRepresentative.civilStatus.id": "Requerido",
      "legalRepresentative.nationality.id": "Requerido",
      "legalRepresentative.email": {
        required: "Requerido",
        maxlength: "75 caracteres maximo"
      },
      "legalRepresentative.registry": "Requerido",
      "legalRepresentative.caseFile": "Requerido",
      "legalRepresentative.extendedIn": "Requerido",
      "legalRepresentative.registrationDate": "Requerido",
      "legalRepresentative.book": "Requerido",
      "legalRepresentative.folio": "Requerido",
      "legalRepresentative.country.id": "Requerido",
      "legalRepresentative.department.id": "Requerido",
      "legalRepresentative.municipality.id": "Requerido",
      "legalRepresentative.zone.id": "Requerido",

      /*Client Payer*/
      "payer.taxNumber":  {
        required: "Requerido",
        validarNit : "NIT inválido"
      },
      "payer.firstName": "Requerido",
      "payer.firstSurname": "Requerido",
      "payer.birthdate": "Requerido",
      "payer.sex.id": "Requerido",
      "payer.profession.id": "Requerido",
      "payer.identificationDocument": {
        required: "Requerido",
        maxlength: "30 caracteres maximo",
        number: "Solo debe contener números"
      },
      "payer.passport": {
        required: "Requerido",
        maxlength: "50 caracteres maximo"
      },
      "payer.civilStatus.id": "Requerido",
      "payer.nationality.id": "Requerido",
      "payer.licenseType.id": "Requerido",
      "payer.licenseNumber": {
        required: "Requerido",
        number: "Solo debe contener números"
      },
      "payer.email": "Requerido",
      "payer.address": "Requerido",
      "payer.country.id": "Requerido",
      "payer.department.id": "Requerido",
      "payer.municipality.id": "Requerido",
      "payer.zone.id": "Requerido",
      "payer.phoneNumber1": "Requerido",
      "payer.addressWork": "Requerido",
      "payer.countryWork.id": "Requerido",
      "payer.workDepartment.id": "Requerido",
      "payer.workMunicipality.id": "Requerido",
      "payer.workZone.id": "Requerido",
      "payer.phoneNumberWork1": "Requerido",

      /*Business Client Payer*/
      "payer.societyType.id": "Requerido",
      "payer.companyName": "Requerido",
      "payer.businessName": "Requerido",
      "payer.economicActivity.id": "Requerido",
      "payer.writeNumber": "Requerido",
      "payer.writeDate": "Requerido",

      /*Legal Representative*/
      "legalRepresentativePayer.taxNumber":  {
        required: "Requerido",
        validarNit : "NIT inválido"
      },
      "legalRepresentativePayer.firstName": {
        required: "Requerido",
        maxlength: "100 caracteres maximo"
      },
      "legalRepresentativePayer.firstSurname": {
        required: "Requerido",
        maxlength: "100 caracteres maximo"
      },
      "legalRepresentativePayer.birthdate": "Requerido",
      "legalRepresentativePayer.sex.id": "Requerido",
      "legalRepresentativePayer.profession.id": "Requerido",
      "legalRepresentativePayer.identificationDocument": {
        required: "Requerido",
        maxlength: "30 caracteres maximo",
        number: "Solo debe contener números"
      },
      "legalRepresentativePayer.passport": {
        required: "Requerido",
        maxlength: "50 caracteres maximo"
      },
      "legalRepresentativePayer.civilStatus.id": "Requerido",
      "legalRepresentativePayer.nationality.id": "Requerido",
      "legalRepresentativePayer.email": {
        required: "Requerido",
        maxlength: "75 caracteres maximo"
      },
      "legalRepresentativePayer.registry": "Requerido",
      "legalRepresentativePayer.caseFile": "Requerido",
      "legalRepresentativePayer.extendedIn": "Requerido",
      "legalRepresentativePayer.registrationDate": "Requerido",
      "legalRepresentativePayer.book": "Requerido",
      "legalRepresentativePayer.folio": "Requerido",
      "legalRepresentativePayer.country.id": "Requerido",
      "legalRepresentativePayer.department.id": "Requerido",
      "legalRepresentativePayer.municipality.id": "Requerido",
      "legalRepresentativePayer.zone.id": "Requerido",

      /* Client Multimedia */
      "dpi": "Requerido",
      "receiptServices": "Requerido",
      "circulationCard": "Requerido",
      "driverLicence": "Requerido",
      "authorizationForm": "Requerido",

      /*Business Client Multimedia*/
      "scanPatents": "Requerido",
      "scanLegalRepresentativeAppointment": "Requerido",
      "dpiLegalRepresentative": "Requerido",

      /*Vehicle*/
      "vehicle.chassis":{
        required : "Requerido",
        minlength : "El No. de chasis debe ser de 17 caracteres",
        maxlength : "El No. de chasis debe ser de 17 caracteres",
      },
      "vehicle.engine": "Requerido",
      "vehicle.plateType.id": "Requerido",
      "vehicle.plate": {
        required: "Requerido",
        validatePlate: "Formato de placa inválido."
      },
      "vehicle.brand.id": "Requerido",
      "vehicle.line.id": "Requerido",
      "vehicle.type.id": "Requerido",
      "vehicle.rate.id": "Requerido",
      "vehicle.erYear.id": "Requerido",
      "vehicle.armor": "Requerido",
      "vehicle.alarmCode": "Requerido",
      "vehicle.numberOfPassengers": "Requerido",
      "vehicle.color": "Requerido",
      "vehicle.mileage": "Requerido",
      "vehicle.loanNumber": "Requerido",
      "vehicle.warranty": "Requerido",
      "vehicle.reminderType.id": "Requerido",
      "vehicle.numberDoor": "Requerido",

      /* Payment */
      "payment.chargeType.id": "Requerido",
      "payment.numberAccountCard": "Requerido",
      "payment.bankAccountType.id": "Requerido",
      "payment.bank.id": "Requerido",
      "payment.cardType.id": "Requerido",
      "payment.cardClass.id": "Requerido",
      "payment.expirationDate": "Requerido",
      "payment.codeAccountCard": "Requerido"
    },
    highlight: function(element){
      $(element).closest(".form-group").removeClass("has-success").addClass("has-error");
    },
    unhighlight: function(element){
      $(element).closest(".form-group").removeClass("has-error").addClass("has-success");
    },
      errorElement: "span",
      errorClass: "help-block"
	});

    $.validator.addMethod("isCellphone", function(Number, element) {
        if  (Number.startsWith("2") || Number.startsWith("1"))
            return false;
        else
            return true;
    }, "No valido");
     // Validar NIT
     $.validator.addMethod("validarNit", function(value, el, param){
     if (value.toString() == "") {
        return true;
        }
        if(value.toString().toLowerCase() == "cf" || value.toString().toLowerCase() == "c/f"){
            return true;
        }

        var nitRegExp = new RegExp("^[0-9]+(-?[0-9kK])?$");
        if (!nitRegExp.test(value)) {
            return false;
        }

        value = value.replace(/-/, "");
        var lastChar = value.length - 1;
        var number = value.substring(0, lastChar);
        var expectedCheker = value.substring(lastChar, lastChar + 1).toLowerCase();

        var factor = number.length + 1;
        var total = 0;

        for (var i = 0; i < number.length; i++) {
            var character = number.substring(i, i + 1);
            var digit = parseInt(character, 10);

            total += (digit * factor);
            factor = factor - 1;
        }

        var modulus = (11 - (total % 11)) % 11;
        var computedChecker = (modulus == 10 ? "k" : modulus.toString());

        return expectedCheker === computedChecker;

    }, "NIT inválido");
    $.validator.addMethod("validatePlate", function(value, element){
        let regexpr = /[0-9]{3}[B-DF-HJ-NP-TV-Z]{3}/;
        return regexpr.test(value);
    }, "Formato de placa inválido.");

}

