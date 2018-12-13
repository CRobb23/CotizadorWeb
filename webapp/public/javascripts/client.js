function init(args) {
    initValidator();
    showFieldsCreditCard();
    $(".typeClient").change(showFieldsClient);
    $(".typeClient").trigger("change");
    $("#useDataClientPayer").change(showFieldsPayer);
    $("#useDataClientPayer").trigger("change");
    $(".typePayer").change(showFieldsPayerType);
    $(".typePayer").trigger("change");
    $("#chargeType").change(showFieldsCreditCard);
    $("#chargeType").trigger("change");
    $("#armor").change(disabledArmorValue);
    $("#armor").trigger("change");
    $("#warranty").change(disabledBeneficiaries);
    $("#warranty").trigger("change");
    /*Para PEP*/
    $("#client_expose").change(showFieldsClientPEP);
    $("#client_expose").trigger("change");
    $(".typePEP").change(changePEP);
    $(".typePEP").trigger("change");
    $(".relationPEP").change(changeRelationPEP);
    $(".relationPEP").trigger("change");
    /*Para clientPEP*/
    $("#payer_expose").change(showFieldsClientPayerPEP);
    $("#payer_expose").trigger("change");
    $(".typePayerPEP").change(changePayerPEP);
    $(".typePayerPEP").trigger("change");

    $(".relationPayerPEP").change(changeRelationPayerPEP);
    $(".relationPayerPEP").trigger("change");

    $("#cardClass").change(ChangedCardClass);
    $("#cardClass").trigger("change");

    //Hide if not null
    if ($("#client_address").val() != ""){
        $("#client_address").prop("type", "password");
    $("#clientAddressLbl").html($("#client_address").val().substring($("#client_address").val().length - 15, $("#client_address").val().length));
    $("#country").rules("remove", "required");
    $("#department").rules("remove", "required");
    $("#municipality").rules("remove", "required");
    $("#zone").rules("remove", "required");
    }
    if ($("#client_phoneNumber1").val() != "") {
        $("#client_phoneNumber1").prop("type", "password");
        $("#clientPhoneNumberLbl").html($("#client_phoneNumber1").val().substring($("#client_phoneNumber1").val().length - 3,$("#client_phoneNumber1").val().length));
    }
    if ($("#client_phoneNumber2").val() != "") {
        $("#client_phoneNumber2").prop("type", "password");
        $("#clientPhoneNumberLbl2").html($("#client_phoneNumber2").val().substring($("#client_phoneNumber2").val().length - 3,$("#client_phoneNumber2").val().length));
    }
    if ($("#client_phoneNumber3").val() != "") {
        $("#client_phoneNumber3").prop("type", "password");
        $("#clientPhoneNumberLbl3").html($("#client_phoneNumber3").val().substring($("#client_phoneNumber3").val().length - 3,$("#client_phoneNumber3").val().length));
    }
    if ($("#client_addressWork").val() != "") {
        $("#client_addressWork").prop("type", "password");
        $("#clientWorkAddressLbl").html($("#client_addressWork").val().substring($("#client_addressWork").val().length - 15,$("#client_addressWork").val().length));
        $("#country_work").rules("remove","required");
        $("#work_department").rules("remove","required");
        $("#work_municipality").rules("remove","required");
        $("#work_zone").rules("remove","required");
    }
    if ($("#client_phoneNumberWork1").val() != "") {
        $("#client_phoneNumberWork1").prop("type", "password");
        $("#clientWorkPhoneNumberLbl").html($("#client_phoneNumberWork1").val().substring($("#client_phoneNumberWork1").val().length - 3,$("#client_phoneNumberWork1").val().length));
    }
    if ($("#client_phoneNumberWork2").val() != "") {
        $("#client_phoneNumberWork2").prop("type", "password");
        $("#clientWorkPhoneNumberLbl2").html($("#client_phoneNumberWork2").val().substring($("#client_phoneNumberWork2").val().length - 3,$("#client_phoneNumberWork2").val().length));
    }
    if ($("#client_phoneNumberWork3").val() != "") {
        $("#client_phoneNumberWork3").prop("type", "password");
        $("#clientWorkPhoneNumberLbl3").html($("#client_phoneNumberWork3").val().substring($("#client_phoneNumberWork3").val().length - 3,$("#client_phoneNumberWork3").val().length));
    }
    if ($("#client_address_business").val() != "") {
        $("#client_address_business").prop("type", "password");
        $("#clientBussinessWorkAddressLbl").html($("#client_address_business").val().substring($("#client_address_business").val().length - 15,$("#client_address_business").val().length));
        $("#country_business").rules("remove","required");
        $("#department_business").rules("remove","required");
        $("#municipality_business").rules("remove","required");
        $("#zone_business").rules("remove","required");
    }
    if ($("#client_phoneNumber1_business").val() != "") {
        $("#client_phoneNumber1_business").prop("type", "password");
        $("#clientBussinessPhoneNumberLbl").html($("#client_phoneNumber1_business").val().substring($("#client_phoneNumber1_business").val().length - 3,$("#client_phoneNumber1_business").val().length));
    }
    if ($("#client_phoneNumber2_business").val() != "") {
        $("#client_phoneNumber2_business").prop("type", "password");
        $("#clientBussinessPhoneNumberLbl2").html($("#client_phoneNumber2_business").val().substring($("#client_phoneNumber2_business").val().length - 3,$("#client_phoneNumber2_business").val().length));
    }
    if ($("#client_phoneNumber3_business").val() != "") {
        $("#client_phoneNumber3_business").prop("type", "password");
        $("#clientBussinessPhoneNumberLbl3").html($("#client_phoneNumber3_business").val().substring($("#client_phoneNumber3_business").val().length - 3,$("#client_phoneNumber3_business").val().length));
    }
    if ($("#legalRepresentative_address").val() != "") {
        $("#legalRepresentative_address").prop("type", "password");
        $("#clientLegalBussinessWorkAddressLbl").html($("#legalRepresentative_address").val().substring($("#legalRepresentative_address").val().length - 15,$("#legalRepresentative_address").val().length));
        $("#country_legal").rules("remove","required");
        $("#department_legal").rules("remove","required");
        $("#municipality_legal").rules("remove","required");
        $("#zone_legal").rules("remove","required");
    }
    if ($("#legalRepresentative_phoneNumber1").val() != "") {
        $("#legalRepresentative_phoneNumber1").prop("type", "password");
        $("#clientLegalBussinessPhoneNumberLbl").html($("#legalRepresentative_phoneNumber1").val().substring($("#legalRepresentative_phoneNumber1").val().length - 3,$("#legalRepresentative_phoneNumber1").val().length));
    }
    if ($("#legalRepresentative_phoneNumber2").val() != "") {
        $("#legalRepresentative_phoneNumber2").prop("type", "password");
        $("#clientLegalBussinessPhoneNumberLb2").html($("#legalRepresentative_phoneNumber2").val().substring($("#legalRepresentative_phoneNumber2").val().length - 3,$("#legalRepresentative_phoneNumber2").val().length));
    }
    if ($("#legalRepresentative_phoneNumber3").val() != "") {
        $("#legalRepresentative_phoneNumber3").prop("type", "password");
        $("#clientLegalBussinessPhoneNumberLb3").html($("#legalRepresentative_phoneNumber3").val().substring($("#legalRepresentative_phoneNumber3").val().length - 3,$("#legalRepresentative_phoneNumber3").val().length));
    }
    if ($("#payer_address").val() != "") {
        $("#payer_address").prop("type", "password");
        $("#payerAddressLbl").html($("#payer_address").val().substring($("#payer_address").val().length - 15,$("#payer_address").val().length));
        $("#country_payer").rules("remove","required");
        $("#department_payer").rules("remove","required");
        $("#municipality_payer").rules("remove","required");
        $("#zone_payer").rules("remove","required");
    }
    if ($("#payer_phoneNumber1").val() != "") {
        $("#payer_phoneNumber1").prop("type", "password");
        $("#payerPhoneNumberLbl").html($("#payer_phoneNumber1").val().substring($("#payer_phoneNumber1").val().length - 3,$("#payer_phoneNumber1").val().length));
    }
    if ($("#payer_phoneNumber2").val() != "") {
        $("#payer_phoneNumber2").prop("type", "password");
        $("#payerPhoneNumberLb2").html($("#payer_phoneNumber2").val().substring($("#payer_phoneNumber2").val().length - 3,$("#payer_phoneNumber2").val().length));
    }
    if ($("#payer_phoneNumber3").val() != "") {
        $("#payer_phoneNumber3").prop("type", "password");
        $("#payerPhoneNumberLb3").html($("#payer_phoneNumber3").val().substring($("#payer_phoneNumber3").val().length - 3,$("#payer_phoneNumber3").val().length));
    }
    if ($("#payer_addressWork").val() != "") {
        $("#payer_addressWork").prop("type", "password");
        $("#payerWorkAddressLbl").html($("#payer_addressWork").val().substring($("#payer_addressWork").val().length - 15,$("#payer_addressWork").val().length));
        $("#country_work_payer").rules("remove","required");
        $("#work_payer_department").rules("remove","required");
        $("#work_payer_municipality").rules("remove","required");
        $("#work_payer_zone").rules("remove","required");
    }
    if ($("#payer_phoneNumberWork1").val() != "") {
        $("#payer_phoneNumberWork1").prop("type", "password");
        $("#payerWorkPhoneNumberLbl").html($("#payer_phoneNumberWork1").val().substring($("#payer_phoneNumberWork1").val().length - 3,$("#payer_phoneNumberWork1").val().length));
    }
    if ($("#payer_phoneNumberWork2").val() != "") {
        $("#payer_phoneNumberWork2").prop("type", "password");
        $("#payerWorkPhoneNumberLb2").html($("#payer_phoneNumberWork2").val().substring($("#payer_phoneNumberWork2").val().length - 3,$("#payer_phoneNumberWork2").val().length));
    }
    if ($("#payer_phoneNumberWork3").val() != "") {
        $("#payer_phoneNumberWork3").prop("type", "password");
        $("#payerWorkPhoneNumberLb3").html($("#payer_phoneNumberWork3").val().substring($("#payer_phoneNumberWork3").val().length - 3,$("#payer_phoneNumberWork3").val().length));
    }
    if ($("#payer_address_business").val() != "") {
        $("#payer_address_business").prop("type", "password");
        $("#payerBussinessWorkAddressLbl").html($("#payer_address_business").val().substring($("#payer_address_business").val().length - 15,$("#payer_address_business").val().length));
        $("#country_business_payer").rules("remove","required");
        $("#department_business_payer").rules("remove","required");
        $("#municipality_business_payer").rules("remove","required");
        $("#zone_business_payer").rules("remove","required");
    }
    if ($("#payer_phoneNumber1_business").val() != "") {
        $("#payer_phoneNumber1_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLbl").html($("#payer_phoneNumber1_business").val().substring($("#payer_phoneNumber1_business").val().length - 3,$("#payer_phoneNumber1_business").val().length));
    }
    if ($("#payer_phoneNumber2_business").val() != "") {
        $("#payer_phoneNumber2_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLb2").html($("#payer_phoneNumber2_business").val().substring($("#payer_phoneNumber2_business").val().length - 3,$("#payer_phoneNumber2_business").val().length));
    }
    if ($("#payer_phoneNumber3_business").val() != "") {
        $("#payer_phoneNumber3_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLb3").html($("#payer_phoneNumber3_business").val().substring($("#payer_phoneNumber3_business").val().length - 3,$("#payer_phoneNumber3_business").val().length));
    }
    if ($("#legalRepresentativePayer_address").val() != "") {
        $("#legalRepresentativePayer_address").prop("type", "password");
        $("#legalRepresentantivePayerAddressLbl").html($("#legalRepresentativePayer_address").val().substring($("#legalRepresentativePayer_address").val().length - 15,$("#legalRepresentativePayer_address").val().length));
        $("#country_legal_payer").rules("remove","required");
        $("#department_legal_payer").rules("remove","required");
        $("#municipality_legal_payer").rules("remove","required");
        $("#zone_legal_payer").rules("remove","required");
    }
    if ($("#legalRepresentativePayer_phoneNumber1").val() != "") {
        $("#legalRepresentativePayer_phoneNumber1").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLbl").html($("#legalRepresentativePayer_phoneNumber1").val().substring($("#legalRepresentativePayer_phoneNumber1").val().length - 3,$("#legalRepresentativePayer_phoneNumber1").val().length));
    }
    if ($("#legalRepresentativePayer_phoneNumber2").val() != "") {
        $("#legalRepresentativePayer_phoneNumber2").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLb2").html($("#legalRepresentativePayer_phoneNumber2").val().substring($("#legalRepresentativePayer_phoneNumber2").val().length - 3,$("#legalRepresentativePayer_phoneNumber2").val().length));
    }
    if ($("#legalRepresentativePayer_phoneNumber3").val() != "") {
        $("#legalRepresentativePayer_phoneNumber3").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLb3").html($("#legalRepresentativePayer_phoneNumber3").val().substring($("#legalRepresentativePayer_phoneNumber3").val().length - 3,$("#legalRepresentativePayer_phoneNumber3").val().length));
    }
    //
    $("#checkClientTaxNumber").click(function(){
        checkClient();
        return false;
    });
    $("#checkClientIdentificationDoc").click(function(){
        checkClient();
        return false;
    });
    $("#checkPayerTaxNumber").click(function(){
        checkPayer();
        return false;
    });
    $("#checkPayerIdentificationDoc").click(function(){
        checkPayer();
        return false;
    });

    $("#useSameAddress").change(function() {
        FillClientFields();
    });

    $("#useSamePayerAddress").change(function() {
        FillPayerFields();
    });

    function FillPayerFields() {
        if ($("#useSamePayerAddress").is(":checked")) {
            $("#country_work_payer").val($("#country_payer").val());
            $("#work_payer_department").val($("#department_payer").val());
            $("#work_payer_department").trigger("change");
            var x = $("#municipality_payer").val();
            $("#work_payer_municipality").attr("value", x);
            $("#work_payer_municipality").trigger("change");

            $("#work_payer_zone").val($("#zone_payer").val());
            $("#work_payer_zone").trigger("change");

            $("#payer_phoneNumberWork1").val($("#payer_phoneNumber1").val());
            $("#payer_phoneNumberWork2").val($("#payer_phoneNumber2").val());
            $("#payer_phoneNumberWork3").val($("#payer_phoneNumber3").val());
            $("#payer_addressWork").val($("#payer_address").val());
            $("#payer_addressWork").prop("type", "password");
            $("#payer_phoneNumberWork1").prop("type", "password");
            $("#payer_phoneNumberWork2").prop("type", "password");
            $("#payer_phoneNumberWork3").prop("type", "password");
        }
    }

    function FillClientFields() {
        if ($("#useSameAddress").is(":checked")) {
             $("#country_work").val($("#country").val());
             $("#work_department").val($("#department").val());
             $("#work_department").trigger("change");
            var x = $("#municipality").val();
            $("#work_municipality").attr("value", x);
            $("#work_municipality").trigger("change");

            $("#work_zone").val($("#zone").val());
            $("#work_zone").trigger("change");

            $("#client_phoneNumberWork1").val($("#client_phoneNumber1").val());
            $("#client_phoneNumberWork2").val($("#client_phoneNumber2").val());
            $("#client_phoneNumberWork3").val($("#client_phoneNumber3").val());
            $("#client_addressWork").val($("#client_address").val());
            $("#client_addressWork").prop("type", "password");
            $("#client_phoneNumberWork1").prop("type", "password");
            $("#client_phoneNumberWork2").prop("type", "password");
            $("#client_phoneNumberWork3").prop("type", "password");
        }
    }


  $(".datePicker").datetimepicker({
        locale: "es",
        format: "DD/MM/YYYY",
        dayViewHeaderFormat: "MMMM YYYY"
   });
    $("#country").change(refreshDepend);
    $("#country_work").change(refreshDepend);
    $("#country_business").change(refreshDepend);
    $("#country_legal").change(refreshDepend);
    $("#country_payer").change(refreshDepend);
    $("#country_work_payer").change(refreshDepend);
    $("#country_business_payer").change(refreshDepend);
    $("#country_legal_payer").change(refreshDepend);

    $("#client_licenseNumber").prop('maxlength', 13);
    $("#payer_licenseNumber").prop('maxlength', 13);

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
               type:"POST",
               url: args.urlGetLinesForBrand,
               data: {id: id},
                    success: function(response){
                    $("#line").html('<option value=">' + args.formSelect + '"</option>');
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

  /*$("#client_taxNumber").change(function() {
    checkClient();
  });

  $("#client_identificationDocument").change(function() {
    checkClient();
  });

  $("#payer_taxNumber").change(function() {
    checkPayer();
  });

  $("#payer_identificationDocument").change(function() {
    checkPayer();
  });*/


}
function checkClient() {
  var taxNumber = "NULL";
  var idNumber = "NULL";

  if ($("#client_taxNumber").val() != "") {
    taxNumber = $("#client_taxNumber").val().trim();
  }
  if ($("#client_identificationDocument").val() != "") {
    idNumber = $("#client_identificationDocument").val().trim();
  }

  $.ajax({
    url:"/incident/getClientsById",
    type:"post",
    data:{information:taxNumber+"|"+idNumber},
    complete:function(result){
      console.log(result.responseJSON);
      if(result.responseJSON.success){
        if (result.responseJSON.client != null) {
          personDetails(result.responseJSON.client);
        } else if (result.responseJSON.clientList != null) {
          modalClient(result.responseJSON.clientList);
        }
      } else {
          $("#client_codeClient").val("");
      }
    }
  });
}

function checkPayer() {
  var taxNumber = "";
  var idNumber = "";

  if ($("#payer_taxNumber").val() != "") {
    taxNumber = $("#payer_taxNumber").val().trim();
  }
  if ($("#payer_identificationDocument").val() != "") {
    idNumber = $("#payer_identificationDocument").val().trim();
  }

  $.ajax({
    url:"/incident/getClientsById",
    type:"post",
    data:{information:taxNumber+"|"+idNumber},
    complete:function(result){
      console.log(result.responseJSON);
      if(result.responseJSON.success){
        if (result.responseJSON.client != null) {
          personPayerDetails(result.responseJSON.client);
        } else if (result.responseJSON.clientList != null) {
          modalPayer(result.responseJSON.clientList);
        }
      } else {
          $("#payer_codeClient").val("");
      }
    }
  });
}

function personDetails(client) {
  $.ajax({
    url:"/incident/getPersonDetails",
    type:"post",
    data:{information:client.personType + "|" + client.codeClient +"|"+ client.cifClient},
    complete:function(result){
      console.log(result.responseJSON);
      if(result.responseJSON.success){
        if (result.responseJSON.person != null) {
            $("#client_nationality").rules("remove","required");
            $("#profession").rules("remove","required");
            $("#client_address").rules("remove","required");
            $("#country").rules("remove","required");
            $("#department").rules("remove","required");
            $("#municipality").rules("remove","required");
            $("#zone").rules("remove","required");
            $("#client_phoneNumber1").rules("remove","required");
            $("#client_phoneNumber2").rules("remove","required");
            $("#client_addressWork").rules("remove","required");
            $("#country_work").rules("remove","required");
            $("#work_department").rules("remove","required");
            $("#work_municipality").rules("remove","required");
            $("#client_phoneNumberWork1").rules("remove","required");
            $("#client_phoneNumberWork2").rules("remove","required");
            $("#btnEditPartial").hide();
            fillPerson(result.responseJSON.person);
        } else if (result.responseJSON.business != null) {
            $("#client_nationality").rules("remove","required");
            $("#client_companyName").rules("remove","required");
            $("#client_businessName").rules("remove","required");
            $("#society_type").rules("remove","required");
            $("#client_economic_activity").rules("remove","required");
            $("#stateProvider").rules("remove","required");
            $("#client_registrationDate").rules("remove","required");
            $("#client_writeNumber").rules("remove","required");
            $("#client_writeDate").rules("remove","required");
            $("#client_address_business").rules("remove","required");
            $("#country_business").rules("remove","required");
            $("#department_business").rules("remove","required");
            $("#municipality_business").rules("remove","required");
            $("#zone_business").rules("remove","required");
            $("#client_phoneNumber1_business").rules("remove","required");
            $("#client_phoneNumber2_business").rules("remove","required");
            $("#legalRepresentative_taxNumber").rules("remove","required");
            $("#legalRepresentative_firstName").rules("remove","required");
            $("#legalRepresentative_firstSurname").rules("remove","required");
            $("#legalRepresentative_birthdate").rules("remove","required");
            $("#sexRep").rules("remove","required");
            $("#professionRep").rules("remove","required");
            $("#legalRepresentative_identificationDocument").rules("remove","required");
            $("#civilStatusRep").rules("remove","required");
            $("#legal_nationality").rules("remove","required");
            $("#legalRepresentative_email").rules("remove","required");
            $("#legalRepresentative_registry").rules("remove","required");
            $("#legalRepresentative_caseFile").rules("remove","required");
            $("#legalRepresentative_extendedIn").rules("remove","required");
            $("#legalRepresentative_registrationDate").rules("remove","required");
            $("#legalRepresentative_book").rules("remove","required");
            $("#legalRepresentative_folio").rules("remove","required");
            $("#legalRepresentative_address").rules("remove","required");
            $("#country_legal").rules("remove","required");
            $("#department_legal").rules("remove","required");
            $("#zone_legal").rules("remove","required");
            $("#municipality_legal").rules("remove","required");
            $("#legalRepresentative_phoneNumber3").rules("remove","required");
            $("#legalRepresentative_phoneNumber2").rules("remove","required");
            $("#zone_legal").rules("remove","required");
            $("#btnEditPartial").hide();
            fillBusiness(result.responseJSON.business);
        }
      }
    }
  });
}

function personPayerDetails(client) {
  $.ajax({
    url:"/incident/getPersonDetails",
    type:"post",
    data:{information:client.personType + "|" + client.codeClient +"|"+ client.cifClient},
    complete:function(result){
      console.log(result.responseJSON);
      if(result.responseJSON.success){
        if (result.responseJSON.person != null) {
            $("#payer_nationality").rules("remove","required");
            $("#payer_email").rules("remove","required");
            $("#profession").rules("remove","required");
            $("#payer_licenseType").rules("remove","required");
            $("#payer_licenseNumber").rules("remove","required");
            $("#payer_address").rules("remove","required");
            $("#country_payer").rules("remove","required");
            $("#department_payer").rules("remove","required");
            $("#municipality_payer").rules("remove","required");
            $("#zone_payer").rules("remove","required");
            $("#payer_phoneNumber1").rules("remove","required");
            $("#payer_phoneNumber2").rules("remove","required");
            $("#payer_addressWork").rules("remove","required");
            $("#country_work_payer").rules("remove","required");
            $("#work_payer_department").rules("remove","required");
            $("#work_payer_zone").rules("remove","required");
            $("#work_payer_municipality").rules("remove","required");
            $("#payer_phoneNumberWork1").rules("remove","required");
            $("#payer_phoneNumberWork2").rules("remove","required");
            $("#btnEditPartial").hide();
          fillPersonPayer(result.responseJSON.person);
        } else if (result.responseJSON.business != null) {
            $("#payer_nationality").rules("remove","required");
            $("#payer_email").rules("remove","required");
            $("#payer_address_business").rules("remove","required");
            $("#society_type").rules("remove","required");
            $("#country_business_payer").rules("remove","required");
            $("#department_business_payer").rules("remove","required");
            $("#municipality_business_payer").rules("remove","required");
            $("#zone_business_payer").rules("remove","required");
            $("#legalRepresentativePayer_taxNumber").rules("remove","required");
            $("#legalRepresentativePayer_firstName").rules("remove","required");
            $("#legalRepresentativePayer_firstSurname").rules("remove","required");
            $("#legalRepresentativePayer_birthdate").rules("remove","required");
            $("#sexRep").rules("remove","required");
            $("#professionRep").rules("remove","required");
            $("#legalRepresentativePayer_identificationDocument").rules("remove","required");
            $("#civilStatusRep").rules("remove","required");
            $("#legal_nationality").rules("remove","required");
            $("#legalRepresentativePayer_email").rules("remove","required");
            $("#legalRepresentativePayer_registry").rules("remove","required");
            $("#legalRepresentativePayer_caseFile").rules("remove","required");
            $("#legalRepresentativePayer_extendedIn").rules("remove","required");
            $("#legalRepresentativePayer_registrationDate").rules("remove","required");
            $("#legalRepresentative_identificationDocument").rules("remove","required");
            $("#legalRepresentativePayer_book").rules("remove","required");
            $("#legalRepresentativePayer_folio").rules("remove","required");
            $("#legalRepresentativePayer_address").rules("remove","required");
            $("#country_legal_payer").rules("remove","required");
            $("#department_legal_payer").rules("remove","required");
            $("#zone_legal_payer").rules("remove","required");
            $("#municipality_legal_payer").rules("remove","required");
            $("#legalRepresentativePayer_phoneNumber3").rules("remove","required");
            $("#legalRepresentativePayer_phoneNumber2").rules("remove","required");
            $("#btnEditPartial").hide();
          fillBusinessPayer(result.responseJSON.business);
        }
      }
    }
  });
}

function fillPerson(person) {
  console.log("Cargando persona individual: ");
  $("#clienteIndividual").click();

  $("#client_taxNumber").val(person.taxNumber);
  $("#client_identificationDocument").val(person.identificationDocument);
  if (person.codeClient != null) {
      $("#client_codeClient").val(person.codeClient);
  }
  if (person.firstName != null) {
      $("#client_firstName").val(person.firstName);
  }
  if (person.secondName != null) {
      $("#client_secondName").val(person.secondName);
  }
  if (person.firstSurname != null) {
      $("#client_firstSurname").val(person.firstSurname);
  }
  if (person.secondSurname != null) {
      $("#client_secondSurname").val(person.secondSurname);
  }
  if (person.marriedSurname != null) {
      $("#client_marriedSurname").val(person.marriedSurname);
  }
  if (person.birthdate != null) {
      $("#client_birthdate").val(person.birthdate);
  }
  if (person.sex != null) {
      $("#sex").val(person.sex).change();
  }
  if (person.profession != null) {
      $("#profession").val(person.profession).change();
  }
  if (person.passport != null) {
      $("#client_passport").val(person.passport);
  }
  if (person.civilStatus != null) {
      $("#civilStatus").val(person.civilStatus).change();
  }
  if (person.nationality != null) {
      $("#client_nationality").val(person.nationality).change();
  }
  if (person.email != null) {
      $("#client_email").val(person.email);
  }
  if (person.licenseType != null) {
      $("#client_licenseType").val(person.licenseType).change();
  }
  if (person.licenseNumber != null) {
      $("#client_licenseNumber").val(person.licenseNumber);
  }
  if (person.codeCifBank != null) {
      $("#client_codeCifBank").val(person.codeCifBank);
  }
  if (person.addressHome != null) {
      $("#client_address").prop("type", "password");
      $("#clientAddressLbl").html(person.addressHome.substring(person.addressHome.length - 15,person.addressHome.length));
      $("#client_address").val(person.addressHome);
      $("#country").rules("remove","required");
      $("#department").rules("remove","required");
      $("#municipality").rules("remove","required");
      $("#zone").rules("remove","required");
  }
  if (person.departmentHome != null) {
      $("#department_selection").val(person.departmentHome);
  }
  if (person.municipalityHome != null) {
      $("#municipality_selection").val(person.municipalityHome);
  }
  if (person.zoneHome != null) {
      $("#zone_selection").val(person.zoneHome);
  }
  if (person.countryHome != null) {
      $("#country_selection").val(person.countryHome);
      $("#country").val(person.countryHome).change();
  }

  if (person.phone1Home != null) {
      $("#client_phoneNumber1").prop("type", "password");
      $("#clientPhoneNumberLbl").html(person.phone1Home.substring(person.phone1Home.length - 3,person.phone1Home.length));
      $("#client_phoneNumber1").val(person.phone1Home);
  }
  if (person.phone2Home != null) {
      $("#client_phoneNumber2").val(person.phone2Home);
      $("#client_phoneNumber2").prop("type", "password");
      $("#clientPhoneNumberLbl2").html(person.phone2Home.substring(person.phone2Home.length - 3,person.phone2Home.length));
  }
  if (person.phone3Home != null) {
      $("#client_phoneNumber3").val(person.phone3Home);
      $("#client_phoneNumber3").prop("type", "password");
      $("#clientPhoneNumberLbl3").html(person.phone3Home.substring(person.phone3Home.length - 3,person.phone3Home.length));
  }
  if (person.addressWork != null) {
      $("#client_addressWork").prop("type", "password");
      $("#client_addressWork").val(person.addressWork);
      $("#clientWorkAddressLbl").html(person.addressWork.substring(person.addressWork.length - 15,person.addressWork.length));
      $("#country_work").rules("remove","required");
      $("#work_department").rules("remove","required");
      $("#work_municipality").rules("remove","required");
      $("#work_zone").rules("remove","required");
  }

  if (person.departmentWork != null) {
      $("#work_department_selection").val(person.departmentWork);
  }
  if (person.municipalityWork != null) {
      $("#work_municipality_selection").val(person.municipalityWork);
  }
  if (person.zoneWork != null) {
      $("#work_zone_selection").val(person.zoneWork);
  }
  if (person.countryWork != null) {
      $("#country_work_selection").val(person.countryWork);
      $("#country_work").val(person.countryWork).change();
  }

  if (person.phone1Work != null) {
      $("#client_phoneNumberWork1").prop("type", "password");
      $("#clientWorkPhoneNumberLbl").html(person.phone1Work.substring(person.phone1Work.length - 3,person.phone1Work.length));
      $("#client_phoneNumberWork1").val(person.phone1Work);
  }
  if (person.phone2Work != null) {
      $("#client_phoneNumberWork2").val(person.phone2Work);
      $("#client_phoneNumberWork2").prop("type", "password");
      $("#clientWorkPhoneNumberLbl2").html(person.phone2Work.substring(person.phone2Work.length - 3,person.phone2Work.length));
  }
  if (person.phone3Work != null) {
      $("#client_phoneNumberWork3").val(person.phone3Work);
      $("#client_phoneNumberWork3").prop("type", "password");
      $("#clientWorkPhoneNumberLbl3").html(person.phone3Work.substring(person.phone3Work.length - 3,person.phone3Work.length));
  }
}

function fillBusiness(business) {
  console.log("Cargando persona juridica: ");
  $("#clienteJuridico").click();

  $("#client_taxNumber").val(business.taxNumber);
  $("#client_identificationDocument").val(business.identificationDocument);
  if (business.codeClient != null) {
      $("#client_codeClient").val(business.codeClient);
  }
  if (business.companyName != null) {
      $("#client_companyName").val(business.companyName);
  }
    if (business.businessName != null) {
        $("#client_businessName").val(business.businessName);
    }
    if (business.societyType != null) {
        $("#society_type").val(business.societyType).change();
    }
    if (business.economicActivity != null) {
        $("#client_economic_activity").val(business.economicActivity).change();
    }
    if (business.nationality != null) {
        $("#client_nationality").val(business.nationality).change();
    }
    if (business.email != null) {
        $("#client_email").val(business.email);
    }
    if (business.stateProvider != null) {
        $("#stateProvider").val(business.stateProvider).change();
    }
    if (business.registrationDate != null) {
        $("#client_registrationDate").val(business.registrationDate);
    }
    if (business.writeNumber != null) {
        $("#client_writeNumber").val(business.writeNumber);
    }
    if (business.writeDate != null) {
        $("#client_writeDate").val(business.writeDate);
    }
    if (business.condeCifBank != null) {
        $("#client_codeCifBank").val(business.condeCifBank);
    }

    if (business.addressWork != null) {
        $("#client_address_business").val(business.addressWork);
        $("#client_address_business").prop("type", "password");
        $("#clientBussinessWorkAddressLbl").html(business.addressWork.substring(business.addressWork.length - 15,business.addressWork.length));
        $("#country_business").rules("remove","required");
        $("#department_business").rules("remove","required");
        $("#municipality_business").rules("remove","required");
        $("#zone_business").rules("remove","required");
  }
    if (business.departmentWork != null) {
        $("#department_business_selection").val(business.departmentWork);
    }
    if (business.municipalityWork != null) {
        $("#municipality_business_selection").val(business.municipalityWork);
    }
    if (business.zoneWork != null) {
        $("#zone_business_selection").val(business.zoneWork);
    }
    if (business.countryWork != null) {
        $("#country_business_selection").val(business.countryWork);
        $("#country_business").val(business.countryWork).change();
    }

    if (business.phone1Work != null) {
        $("#client_phoneNumber1_business").val(business.phone1Work);
        $("#client_phoneNumber1_business").prop("type", "password");
        $("#clientBussinessPhoneNumberLbl").html(business.phone1Work.substring(business.phone1Work.length - 3,business.phone1Work.length));
    }
    if (business.phone2Work != null) {
        $("#client_phoneNumber2_business").val(business.phone2Work);
        $("#client_phoneNumber2_business").prop("type", "password");
        $("#clientBussinessPhoneNumberLbl2").html(business.phone2Work.substring(business.phone2Work.length - 3,business.phone2Work.length));
    }
    if (business.phone3Work != null) {
        $("#client_phoneNumber3_business").val(business.phone3Work);
        $("#client_phoneNumber3_business").prop("type", "password");
        $("#clientBussinessPhoneNumberLbl3").html(business.phone3Work.substring(business.phone3Work.length - 3,business.phone3Work.length));
    }

    if (business.taxNumberRep != null) {
        $("#legalRepresentative_taxNumber").val(business.taxNumberRep);
    }
    if (business.firstNameRep != null) {
        $("#legalRepresentative_firstName").val(business.firstNameRep);
    }
    if (business.secondNameRep != null) {
        $("#legalRepresentative_secondName").val(business.secondNameRep);
    }
    if (business.firstSurnameRep != null) {
        $("#legalRepresentative_firstSurname").val(business.firstSurnameRep);
    }
    if (business.secondSurnameRep != null) {
        $("#legalRepresentative_secondSurname").val(business.secondSurnameRep);
    }
    if (business.marriedSurnameRep != null) {
        $("#legalRepresentative_marriedSurname").val(business.marriedSurnameRep);
    }
    if (business.birthdateRep != null) {
        $("#legalRepresentative_birthdate").val(business.birthdateRep);
    }
    if (business.sexRep != null) {
        $("#sexRep").val(business.sexRep).change();
    }
    if (business.professionRep != null) {
        $("#professionRep").val(business.professionRep).change();
    }
    if (business.dpiRep != null) {
        $("#legalRepresentative_identificationDocument").val(business.dpiRep);
    }
    if (business.passportRep != null) {
        $("#legalRepresentative_passport").val(business.passportRep);
    }
    if (business.civilStatusRep != null) {
        $("#civilStatusRep").val(business.civilStatusRep).change();
    }
    if (business.nationalityRep != null) {
        $("#legal_nationality").val(business.nationalityRep).change();
    }
    if (business.emailRep != null) {
        $("#legalRepresentative_email").val(business.emailRep);
    }
    if (business.registerRep != null) {
        $("#legalRepresentative_registry").val(business.registerRep);
    }
    if (business.caseFileRep != null) {
        $("#legalRepresentative_caseFile").val(business.caseFileRep);
    }
    if (business.extendedInRep != null) {
        $("#legalRepresentative_extendedIn").val(business.extendedInRep);
    }
    if (business.registrationDateRep != null) {
        $("#legalRepresentative_registrationDate").val(business.registrationDateRep);
    }
    if (business.bookRep != null) {
        $("#legalRepresentative_book").val(business.bookRep);
    }
    if (business.folioRep != null) {
        $("#legalRepresentative_folio").val(business.folioRep);
    }

    if (business.addressLegal != null) {
        $("#legalRepresentative_address").val(business.addressLegal);
        $("#legalRepresentative_address").prop("type", "password");
        $("#clientLegalBussinessWorkAddressLbl").html(business.addressLegal.substring(business.addressLegal.length - 15,business.addressLegal.length));
        $("#country_legal").rules("remove","required");
        $("#department_legal").rules("remove","required");
        $("#municipality_legal").rules("remove","required");
        $("#zone_legal").rules("remove","required");
    }
    if (business.departmentLegal != null) {
        $("#department_legal_selection").val(business.departmentLegal);
    }
    if (business.municipalityLegal != null) {
        $("#municipality_legal_selection").val(business.municipalityLegal);
    }
    if (business.zoneLegal != null) {
        $("#zone_legal_selection").val(business.zoneLegal);
    }
    if (business.countryLegal != null) {
        $("#country_legal_selection").val(business.countryLegal);
        $("#country_legal").val(business.countryLegal).change();
    }

    if (business.phone1Legal != null) {
        $("#legalRepresentative_phoneNumber1").prop("type", "password");
        $("#legalRepresentative_phoneNumber1").val(business.phone1Legal);
        $("#clientLegalBussinessPhoneNumberLbl").html(business.addressLegal.substring(business.phone1Legal.length - 3,business.phone1Legal.length));
    }
    if (business.phone2Legal != null) {
        $("#legalRepresentative_phoneNumber2").val(business.phone2Legal);
        $("#legalRepresentative_phoneNumber2").prop("type", "password");
        $("#clientLegalBussinessPhoneNumberLb2").html(business.phone2Legal.substring(business.phone2Legal.length - 3,business.phone2Legal.length));
    }
    if (business.phone3Legal != null) {
        $("#legalRepresentative_phoneNumber3").val(business.phone3Legal);
        $("#legalRepresentative_phoneNumber3").prop("type", "password");
        $("#clientLegalBussinessPhoneNumberLb3").html(business.phone3Legal.substring(business.phone3Legal.length - 3,business.phone3Legal.length));
    }

}

function fillPersonPayer(person) {
  console.log("Cargando persona individual: ");
  $("#pagadorIndividual").click();

  $("#payer_taxNumber").val(person.taxNumber);
  $("#payer_identificationDocument").val(person.identificationDocument);
  if (person.codeClient != null) {
      $("#payer_codeClient").val(person.codeClient);
  }
  if (person.firstName != null) {
      $("#payer_firstName").val(person.firstName);
  }
  if (person.secondName != null) {
      $("#payer_secondName").val(person.secondName);
  }
  if (person.firstSurname != null) {
      $("#payer_firstSurname").val(person.firstSurname);
  }
  if (person.secondSurname != null) {
      $("#payer_secondSurname").val(person.secondSurname);
  }
  if (person.marriedSurname != null) {
      $("#payer_marriedSurname").val(person.marriedSurname);
  }
  if (person.birthdate != null) {
      $("#payer_birthdate").val(person.birthdate);
  }
  if (person.sex != null) {
      $("#sex").val(person.sex).change();
  }
  if (person.profession != null) {
      $("#profession").val(person.profession).change();
  }
  if (person.passport != null) {
      $("#payer_passport").val(person.passport);
  }
  if (person.civilStatus != null) {
      $("#civilStatus").val(person.civilStatus).change();
  }
  if (person.nationality != null) {
      $("#payer_nationality").val(person.nationality).change();
  }
  if (person.email != null) {
      $("#payer_email").val(person.email);
  }
  if (person.licenseType != null) {
      $("#payer_licenseType").val(person.licenseType).change();
  }
  if (person.licenseNumber != null) {
      $("#payer_licenseNumber").val(person.licenseNumber);
  }
  if (person.codeCifBank != null) {
      $("#payer_codeCifBank").val(person.codeCifBank);

  }
  if (person.addressHome != null) {
      $("#payer_address").prop("type", "password");
      $("#payer_address").val(person.addressHome);
      $("#payerAddressLbl").html(person.addressHome.substring(person.addressHome.length - 15,person.addressHome.length));
      $("#country_payer").rules("remove","required");
      $("#department_payer").rules("remove","required");
      $("#municipality_payer").rules("remove","required");
      $("#zone_payer").rules("remove","required");
  }
  if (person.departmentHome != null) {
      $("#department_payer_selection").val(person.departmentHome);
  }
  if (person.municipalityHome != null) {
      $("#municipality_payer_selection").val(person.municipalityHome);
  }
  if (person.zoneHome != null) {
      $("#zone_payer_selection").val(person.zoneHome);
  }
  if (person.countryHome != null) {
      $("#country_payer_selection").val(person.countryHome);
      $("#country_payer").val(person.countryHome).change();
  }

  if (person.phone1Home != null) {
      $("#payer_phoneNumber1").prop("type", "password");
      $("#payer_phoneNumber1").val(person.phone1Home);
      $("#payerPhoneNumberLbl").html(person.phone1Home.substring(person.phone1Home.length - 3,person.phone1Home.length));
  }
  if (person.phone2Home != null) {
      $("#payer_phoneNumber2").val(person.phone2Home);
      $("#payer_phoneNumber2").prop("type", "password");
      $("#payerPhoneNumberLb2").html(person.phone2Home.substring(person.phone2Home.length - 3,person.phone2Home.length));
  }
  if (person.phone3Home != null) {
      $("#payer_phoneNumber3").val(person.phone3Home);
      $("#payer_phoneNumber3").prop("type", "password");
      $("#payerPhoneNumberLb3").html(person.phone3Home.substring(person.phone3Home.length - 3,person.phone3Home.length));
  }
  if (person.addressWork != null) {
      $("#payer_addressWork").prop("type", "password");
      $("#payer_addressWork").val(person.addressWork);
      $("#payerWorkAddressLbl").html(person.addressWork.substring(person.addressWork.length - 15,person.addressWork.length));
      $("#country_work_payer").rules("remove","required");
      $("#work_payer_department").rules("remove","required");
      $("#work_payer_municipality").rules("remove","required");
      $("#work_payer_zone").rules("remove","required");

  }
  if (person.departmentWork != null) {
      $("#work_department_payer_selection").val(person.departmentWork);
  }
  if (person.municipalityWork != null) {
      $("#work_municipality_payer_selection").val(person.municipalityWork);
  }
  if (person.zoneWork != null) {
      $("#work_zone_payer_selection").val(person.zoneWork);
  }
  if (person.countryWork != null) {
      $("#country_work_payer_selection").val(person.countryWork);
      $("#country_work_payer").val(person.countryWork).change();
  }

  if (person.phone1Work != null) {
      $("#payer_phoneNumberWork1").prop("type", "password");
      $("#payerWorkPhoneNumberLbl").html(person.phone1Work.substring(person.phone1Work.length - 3,person.phone1Work.length));
      $("#payer_phoneNumberWork1").val(person.phone1Work);
  }
  if (person.phone2Work != null) {
      $("#payer_phoneNumberWork2").val(person.phone2Work);
      $("#payer_phoneNumberWork2").prop("type", "password");
      $("#payerWorkPhoneNumberLb2").html(person.phone2Work.substring(person.phone2Work.length - 3,person.phone2Work.length));
  }
  if (person.phone3Work != null) {
      $("#payer_phoneNumberWork3").val(person.phone3Work);
      $("#payer_phoneNumberWork3").prop("type", "password");
      $("#payerWorkPhoneNumberLb3").html(person.phone3Work.substring(person.phone3Work.length - 3,person.phone3Work.length));
  }
}

function fillBusinessPayer(business) {
  console.log("Cargando persona juridica: ");
  $("#pagadorJuridico").click();

  $("#payer_taxNumber").val(business.taxNumber);
  $("#payer_identificationDocument").val(business.identificationDocument);
  if (business.codeClient != null) {
      $("#payer_codeClient").val(business.codeClient);
  }
  if (business.companyName != null) {
      $("#payer_companyName").val(business.companyName);
  }
  if (business.businessName != null) {
      $("#payer_businessName").val(business.businessName);
  }
  if (business.societyType != null) {
      $("#society_type").val(business.societyType).change();
  }
  if (business.economicActivity != null) {
      $("#payer_economic_activity").val(business.economicActivity).change();
  }
  if (business.nationality != null) {
      $("#payer_nationality").val(business.nationality).change();
  }
  if (business.email != null) {
      $("#payer_email").val(business.email);
  }
  if (business.stateProvider != null) {
      $("#stateProvider").val(business.stateProvider).change();
  }
  if (business.registrationDate != null) {
      $("#payer_registrationDate").val(business.registrationDate);
  }
  if (business.writeNumber != null) {
      $("#payer_writeNumber").val(business.writeNumber);
  }
  if (business.writeDate != null) {
      $("#payer_writeDate").val(business.writeDate);
  }
  if (business.condeCifBank != null) {
      $("#payer_codeCifBank").val(business.condeCifBank);

  }
  if (business.addressWork != null) {
      $("#payer_address_business").prop("type", "password");
      $("#payerBussinessWorkAddressLbl").html(business.addressWork.substring(business.addressWork.length - 15,business.addressWork.length));
      $("#payer_address_business").val(business.addressWork);
      $("#country_business_payer").rules("remove","required");
      $("#department_business_payer").rules("remove","required");
      $("#municipality_business_payer").rules("remove","required");
      $("#zone_business_payer").rules("remove","required");
  }
  if (business.departmentWork != null) {
      $("#department_business_payer_selection").val(business.departmentWork);
  }
  if (business.municipalityWork != null) {
      $("#municipality_business_payer_selection").val(business.municipalityWork);
  }
  if (business.zoneWork != null) {
      $("#zone_business_payer_selection").val(business.zoneWork);
  }
  if (business.countryWork != null) {
      $("#country_business_payer_selection").val(business.countryWork);
      $("#country_business_payer").val(business.countryWork).change();
  }

  if (business.phone1Work != null) {
      $("#payer_phoneNumber1_business").prop("type", "password");
      $("#payerBussinessPhoneNumberLbl").html(business.phone1Work.substring(business.phone1Work.length - 3,business.phone1Work.length));
      $("#payer_phoneNumber1_business").val(business.phone1Work);
  }
  if (business.phone2Work != null) {
      $("#payer_phoneNumber2_business").val(business.phone2Work);
      $("#payer_phoneNumber2_business").prop("type", "password");
      $("#payerBussinessPhoneNumberLb2").html(business.phone2Work.substring(business.phone2Work.length - 3,business.phone2Work.val().length));
  }
  if (business.phone3Work != null) {
      $("#payer_phoneNumber3_business").val(business.phone3Work);
      $("#payer_phoneNumber3_business").prop("type", "password");
      $("#payerBussinessPhoneNumberLb3").html(business.phone3Work.substring(business.phone3Work.length - 3,business.phone3Work.length));

  }
  if (business.taxNumberRep != null) {
      $("#legalRepresentativePayer_taxNumber").val(business.taxNumberRep);
  }
  if (business.firstNameRep != null) {
      $("#legalRepresentativePayer_firstName").val(business.firstNameRep);
  }
  if (business.secondNameRep != null) {
      $("#legalRepresentativePayer_secondName").val(business.secondNameRep);
  }
  if (business.firstSurnameRep != null) {
      $("#legalRepresentativePayer_firstSurname").val(business.firstSurnameRep);
  }
  if (business.secondSurnameRep != null) {
      $("#legalRepresentativePayer_secondSurname").val(business.secondSurnameRep);
  }
  if (business.marriedSurnameRep != null) {
      $("#legalRepresentativePayer_marriedSurname").val(business.marriedSurnameRep);
  }
  if (business.birthdateRep != null) {
      $("#legalRepresentativePayer_birthdate").val(business.birthdateRep);
  }
  if (business.sexRep != null) {
      $("#sexRep").val(business.sexRep).change();
  }
  if (business.professionRep != null) {
      $("#professionRep").val(business.professionRep).change();
  }
  if (business.dpiRep != null) {
      $("#legalRepresentativePayer_identificationDocument").val(business.dpiRep);
  }
  if (business.passportRep != null) {
      $("#legalRepresentativePayer_passport").val(business.passportRep);
  }
  if (business.civilStatusRep != null) {
      $("#civilStatusRep").val(business.civilStatusRep).change();
  }
  if (business.nationalityRep != null) {
      $("#legal_nationality").val(business.nationalityRep).change();
  }
  if (business.emailRep != null) {
      $("#legalRepresentativePayer_email").val(business.emailRep);
  }
  if (business.registerRep != null) {
      $("#legalRepresentativePayer_registry").val(business.registerRep);
  }
  if (business.caseFileRep != null) {
      $("#legalRepresentativePayer_caseFile").val(business.caseFileRep);
  }
  if (business.extendedInRep != null) {
      $("#legalRepresentativePayer_extendedIn").val(business.extendedInRep);
  }
  if (business.registrationDateRep != null) {
      $("#legalRepresentativePayer_registrationDate").val(business.registrationDateRep);
  }
  if (business.bookRep != null) {
      $("#legalRepresentativePayer_book").val(business.bookRep);
  }
  if (business.folioRep != null) {
      $("#legalRepresentativePayer_folio").val(business.folioRep);
  }
  if (business.addressLegal != null) {
      $("#legalRepresentativePayer_address").prop("type", "password");
      $("#legalRepresentativePayer_address").val(business.addressLegal);
      $("#legalRepresentantivePayerAddressLbl").html(business.addressLegal.substring(business.addressLegal.length - 15,business.addressLegal.length));
      $("#country_legal_payer").rules("remove","required");
      $("#department_legal_payer").rules("remove","required");
      $("#municipality_legal_payer").rules("remove","required");
      $("#zone_legal_payer").rules("remove","required");
  }
  if (business.departmentLegal != null) {
      $("#department_legal_payer_selection").val(business.departmentLegal);
  }
  if (business.municipalityLegal != null) {
      $("#municipality_legal_payer_selection").val(business.municipalityLegal);
  }
  if (business.zoneLegal != null) {
      $("#zone_legal_payer_selection").val(business.zoneLegal);
  }
  if (business.countryLegal != null) {
      $("#country_legal_payer_selection").val(business.countryLegal);
      $("#country_legal_payer").val(business.countryLegal).change();
  }
  if (business.phone1Legal != null) {
      $("#legalRepresentativePayer_phoneNumber1").prop("type", "password");
      $("#legalRepresentantivePayerPhoneNumberLbl").html(business.phone1Legal.substring(business.phone1Legal.length - 3,business.phone1Legal.length));
      $("#legalRepresentativePayer_phoneNumber1").val(business.phone1Legal);

  }
  if (business.phone2Legal != null) {
      $("#legalRepresentativePayer_phoneNumber2").val(business.phone2Legal);
      $("#legalRepresentativePayer_phoneNumber2").prop("type", "password");
      $("#legalRepresentantivePayerPhoneNumberLb2").html(business.phone2Legal.substring(business.phone2Legal.length - 3,business.phone2Legal.length));
  }
  if (business.phone3Legal != null) {
      $("#legalRepresentativePayer_phoneNumber3").val(business.phone3Legal);
      $("#legalRepresentativePayer_phoneNumber3").prop("type", "password");
      $("#legalRepresentantivePayerPhoneNumberLb3").html(business.phone3Legal.substring(business.phone3Legal.length - 3,business.phone3Legal.length));
  }
}

function modalClient(clients) {
  console.log(clients);
  $("#clientsTable").children().remove();
  // Fill table with clients data
  for (i=0; i<clients.length; i+=1) {
    client = clients[i];
    console.log($("clientsTable"));
    $("#clientsTable").append("<tr class='clientsRow'><td><label class='code'><input type='radio' id='"+client.codeClient+"' name='optradio'/>" + client.codeClient +"</label></td><td><label for='"+client.codeClient+"'>"+client.name+"</label></td><td><label for='"+client.codeClient+"'>"+client.cifClient+"</label></td></tr>");
  }
  var dialog = $( "#dialog-clients" ).dialog({
    dialogClass: "no-close",
    autoOpen: false,
    height: 400,
    width: 350,
    modal: true,
    buttons: [
      {
        text: "Seleccionar",
        click: function() {
          if (selectClient(clients)) {
            $(this).dialog("close");
          }
        }
      }
    ]
  });
  dialog.dialog( "open" );
}

function selectClient(clients) {
  console.log($(".clientsRow"));
  var clientCode = "";
  var countChecked = 0;
  $(".clientsRow").each(function () {
    if(!$(this).find("input[type='radio']").is(":checked")){
      countChecked++;
    } else {
      var radioValue = $(this).find("input[type='radio']:checked").val();
      clientCode = $(this).find("label[class='code']").text();
      console.log("This row has checked: "+clientCode);
    }
  });
  if (countChecked == clients.length){
    alert("Debe seleccionar un cliente para continuar.");
    return false;
  }
  for (i=0; i<clients.length; i+=1) {
    client = clients[i];
    if (client.codeClient == clientCode) {
      personDetails(client);
    }
  }
  return true;
}

function modalPayer(clients) {
  console.log(clients);
  $("#payersTable").children().remove();
  // Fill table with clients data
  for (i=0; i<clients.length; i+=1) {
    client = clients[i];
    console.log($("clientsTable"));
    $("#payersTable").append("<tr class='payersRow'><td><label class='code'><input type='radio' id='"+client.codeClient+"' name='optradio'/>" + client.codeClient +"</label></td><td><label for='"+client.codeClient+"'>"+client.name+"</label></td><td><label for='"+client.codeClient+"'>"+client.cifClient+"</label></td></tr>");
  }
  var dialog = $( "#dialog-payers" ).dialog({
    dialogClass: "no-close",
    autoOpen: false,
    height: 400,
    width: 350,
    modal: true,
    buttons: [
      {
        text: "Seleccionar",
        click: function() {
          if (selectPayer(clients)) {
            $(this).dialog("close");
          }
        }
      }
    ]
  });
  dialog.dialog( "open" );
}

function selectPayer(clients) {
  console.log($(".payersRow"));
  var clientCode = "";
  var countChecked = 0;
  $(".payersRow").each(function () {
    if(!$(this).find("input[type='radio']").is(":checked")){
      countChecked++;
    } else {
      var radioValue = $(this).find("input[type='radio']:checked").val();
      clientCode = $(this).find("label[class='code']").text();
      console.log("This row has checked: "+clientCode);
    }
  });
  if (countChecked == clients.length){
    alert("Debe seleccionar un pagador para continuar.");
    return false;
  }
  for (i=0; i<clients.length; i+=1) {
    client = clients[i];
    if (client.codeClient == clientCode) {
      personPayerDetails(client);
    }
  }
  return true;
}

function addEventSelectDepend(idSelectDepend, idSelect, args){
        $("#" + idSelectDepend).change(function(){
        var selectelement = $(this);
        selectelement.prop("disabled", true);
        $("#" + idSelect).html("<option value=''>" + args.formSelectLoading + "</option>");
        var id = $(this).val();
        $.ajax({
                type:"POST",
                url: args.urlGetGeographicAreaChildren,
                data: {id: id,tipo:idSelect},
                success: function(response){
                        $("#" + idSelect).html("<option value=''>" + args.formSelect + "</option>");
                        if(response.success){
                        var html = "";
                        $.each(response.areas, function(index, area){
                        html += "<option value='" + area.id + "'>" + area.name + "</option>";
                    });
                    $("#" + idSelect).append(html);
                    var selectVal = $("#" + idSelect + "_selection").val();
                    if (selectVal != "") {
                    $("#" + idSelect).val(selectVal).change();
          }
          }
           },
           error: function(){
           $("#" + idSelect).html("<option value=''>" + args.formSelectError + "</option>");
           },
           complete: function() {
               if (idSelect == "work_municipality") {
                   $("#" + idSelect).val($("#municipality").val());
                   $("#work_municipality").trigger("change");
               }
               else if (idSelect == "work_zone") {
                   $("#" + idSelect).val($("#zone").val());
               }
               else if (idSelect == "work_payer_municipality") {
                   $("#" + idSelect).val($("#municipality_payer").val());
                   $("#work_payer_municipality").trigger("change");
               }
               else if (idSelect == "work_payer_zone") {
                   $("#" + idSelect).val($("#zone_payer").val());
               }
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
        $("#work_municipality").html("<option value='''>Seleccionar</option>");
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
        $("#useSameAddress").show();
         $("#useSamePayerAddressAddress").show();
        $("#lblUseSameAddress").show();
    }
    else if($(this).attr("id") == "clienteJuridico" && $(this).is(":checked")){
        $(".fieldsLegal input").removeAttr("disabled");
        $(".fieldsLegal select").removeAttr("disabled");
        $(".fieldsLegal").show();
        $(".fieldsClient").hide();
        $(".fieldsClient input").attr("disabled", "disabled");
        $(".fieldsClient select").attr("disabled", "disabled");
        $("#useSameAddress").hide();
        $("#useSamePayerAddressAddress").hide();
        $("#lblUseSameAddress").hide();
    }
}

function showFieldsClientPEP(){
    if($(this).attr("id") == "client_expose" && $(this).is(":checked")){
        $(".fieldsClientPEP").show();
        $(".fieldsClientPEP select").removeAttr("disabled");
        $(".fieldsClientPEP input").removeAttr("disabled");
    }else {
        $(".fieldsClientPEP").hide();
        $(".fieldsClientPEP input").attr("disabled", "disabled");
        $(".fieldsClientPEP select").attr("disabled", "disabled");
    }
}
function changePEP(){
    if($(this).attr("id") == "radPEPParentesco" && $(this).is(":checked")){

        $(".relationPEP").empty();
        $("#lblRelationship").text("Parentesco:");
        $(".relationPEP").append( "<option value='Padre'>Padre</option>");
        $(".relationPEP").append( "<option value='Madre'>Madre</option>");
        $(".relationPEP").append( "<option value='Hijo(a)'>Hijo(a)</option>");
        $(".relationPEP").append( "<option value='Hermano(a)'>Hermano(a)</option>");
        $(".relationPEP").append( "<option value='C&oacute;nyuge'>C&oacute;nyuge</option>");
        $(".relationPEP").append( "<option value='Otro'>Otro</option>");
        $("#divRelative").show();
        $(".relationPEP").trigger("change");

    }else if($(this).attr("id") == "radPEPAsociado" && $(this).is(":checked")){
        $("#divRelative").show();
        $(".relationPEP").empty();
        $("#lblRelationship").text("Motivo:");
        $(".relationPEP").append( "<option value='Profesionales'>Profesionales</option>");
        $(".relationPEP").append( "<option value='Pol&iacute;ticos'>Pol&iacute;ticos</option>");
        $(".relationPEP").append( "<option value='Comerciales'>Comerciales</option>");
        $(".relationPEP").append( "<option value='Negocios'>Negocios</option>");
        $(".relationPEP").append( "<option value='Otro'>Otro</option>");
        $(".relationPEP").trigger("change");
    }
    else if($(this).attr("id") == "radPEPNinguno" && $(this).is(":checked")){
        $("#divRelative").hide();
    }
}

function changeRelationPEP() {
    if ($(this).find(":selected").val() == "Otro"){
       $("#divOtro").show();
    }
    else
    {
        $("#clientPEP.specificRelationship").val("");
        $("#divOtro").hide();
    }
}

function showFieldsClientPayerPEP(){
    if($(this).attr("id") == "payer_expose" && $(this).is(":checked")){
        $(".fieldsClientPayerPEP").show();
        $(".fieldsClientPayerPEP select").removeAttr("disabled");
        $(".fieldsClientPayerPEP input").removeAttr("disabled");
    }else {
        $(".fieldsClientPayerPEP").hide();
        $(".fieldsClientPayerPEP input").attr("disabled", "disabled");
        $(".fieldsClientPayerPEP select").attr("disabled", "disabled");
    }
}
function changePayerPEP(){
    if($(this).attr("id") == "radPEPPayerParentesco" && $(this).is(":checked")){

        $(".relationPayerPEP").empty();
        $("#lblRelationshipPayer").text("Parentesco:");
        $(".relationPayerPEP").append( "<option value='Padre'>Padre</option>");
        $(".relationPayerPEP").append( "<option value='Madre'>Madre</option>");
        $(".relationPayerPEP").append( "<option value='Hijo(a)'>Hijo(a)</option>");
        $(".relationPayerPEP").append( "<option value='Hermano(a)'>Hermano(a)</option>");
        $(".relationPayerPEP").append( "<option value='C&oacute;nyuge'>C&oacute;nyuge</option>");
        $(".relationPayerPEP").append( "<option value='Otro'>Otro</option>");
        $("#divRelativePayer").show();
        $(".relationPayerPEP").trigger("change");

    }else if($(this).attr("id") == "radPEPPayerAsociado" && $(this).is(":checked")){
        $("#divRelativePayer").show();
         $(".relationPayerPEP").empty();
        $("#lblRelationshipPayer").text("Motivo:");
         $(".relationPayerPEP").append("<option value='Profesionales'>Profesionales</option>");
         $(".relationPayerPEP").append("<option value='Pol&iacute;ticos'>Pol&iacute;ticos</option>");
         $(".relationPayerPEP").append("<option value='Comerciales'>Comerciales</option>");
         $(".relationPayerPEP").append("<option value='Negocios'>Negocios</option>");
         $(".relationPayerPEP").append("<option value='Otro'>Otro</option>");
         $(".relationPayerPEP").trigger("change");
    }
    else if($(this).attr("id") == "radPEPPayerNinguno" && $(this).is(":checked")){
        $("#divRelativePayer").hide();
    }
}

function changeRelationPayerPEP() {
    if ($(this).find(":selected").val() == "Otro"){
        $("#divOtroPayer").show();
    }
    else
    {
        $("#clientPayerPEP.specificRelationship").val("");
        $("#divOtroPayer").hide();
    }
}

function showFieldsPayerType(){
  if($(this).attr("id") == "pagadorIndividual" && $(this).is(":checked")){
    $(".fieldsClientPayer input").removeAttr("disabled");
    $(".fieldsClientPayer select").removeAttr("disabled");
    $(".fieldsClientPayer").show();
    $(".fieldsLegalPayer").hide();
      $("#lbluseSamePayerAddress").show()
      $("#useSamePayerAddress").show();
    $(".fieldsLegalPayer input").attr("disabled", "disabled");
    $(".fieldsLegalPayer select").attr("disabled", "disabled");

  }else if($(this).attr("id") == "pagadorJuridico" && $(this).is(":checked")){
    $(".fieldsLegalPayer input").removeAttr("disabled");
    $(".fieldsLegalPayer select").removeAttr("disabled");
    $("#lbluseSamePayerAddress").hide();
      $("#useSamePayerAddress").hide();
    $(".fieldsLegalPayer").show();
    $(".fieldsClientPayer").hide();
    $(".fieldsClientPayer input").attr("disabled", "disabled");
    $(".fieldsClientPayer select").attr("disabled", "disabled");

  }
}

function changeFieldsClient(){
    if($("#clienteIndividual").is(":checked")){
        $(".fieldsClient input").removeAttr("disabled");
        $(".fieldsClient select").removeAttr("disabled");
        $(".fieldsClient").show();
        $(".fieldsLegal").hide();
        $(".fieldsLegal input").attr("disabled", "disabled");
        $(".fieldsLegal select").attr("disabled", "disabled");
    }else if($("#clienteJuridico").is(":checked")){
        $(".fieldsLegal input").removeAttr("disabled");
        $(".fieldsLegal select").removeAttr("disabled");
        $(".fieldsLegal").show();
        $(".fieldsClient").hide();
        $(".fieldsClient input").attr("disabled", "disabled");
        $(".fieldsClient select").attr("disabled", "disabled");
    }
}
function changeFieldsPayerType(){
    if($("#pagadorIndividual").is(":checked")){
        $(".fieldsClientPayer input").removeAttr("disabled");
        $(".fieldsClientPayer select").removeAttr("disabled");
        $(".fieldsClientPayer").show();
        $(".fieldsLegalPayer").hide();
        $(".fieldsLegalPayer input").attr("disabled", "disabled");
        $(".fieldsLegalPayer select").attr("disabled", "disabled");
    }else if($("#pagadorJuridico").is(":checked")){
        $(".fieldsLegalPayer input").removeAttr("disabled");
        $(".fieldsLegalPayer select").removeAttr("disabled");
        $(".fieldsLegalPayer").show();
        $(".fieldsClientPayer").hide();
        $(".fieldsClientPayer input").attr("disabled", "disabled");
        $(".fieldsClientPayer select").attr("disabled", "disabled");
    }
}



function isCreditCardValid(val) {
    var TipoTarjeta = $("#cardClass").val();
    if($("#chargeType").val() == 3) {
        if (val.startsWith("3") && TipoTarjeta == "3") {
            $("#formClient").validate();
            $("#payment_numberAccountCard").rules("remove");
            $("#payment_numberAccountCard").prop('maxlength', 15);
            $("#payment_numberAccountCard").rules("add", {
                required: true,
                minlength: 15,
                maxlength: 15,
                messages: {
                    required: "Requerido",
                    minlength: "Por favor ingrese 15 dígitos",
                    maxlength: "Por favor ingrese 15 dígitos"
                }
            });
        }
        else if ((val.startsWith("4") || val.startsWith("5")) && (TipoTarjeta == "1" || TipoTarjeta == "2")) {
            $("#formClient").validate();
            $("#payment_numberAccountCard").rules("remove");
            $("#payment_numberAccountCard").prop('maxlength', 16);
            $.validator.addMethod("creditCardValid", function (cardNumber, element) {
                if (creditCardCheck(cardNumber))
                    return true;
                else
                    return false;
            }, "Tarjeta invalida");
            $("#payment_numberAccountCard").rules("add", {
                required: true,
                minlength: 16,
                maxlength: 16,
                creditCardValid: true,
                messages: {
                    required: "Requerido",
                    minlength: "Por favor ingrese 16 dígitos",
                    maxlength: "Por favor ingrese 16 dígitos"
                }
            });
        }
        else {
            $("#formClient").validate();
            $("#payment_numberAccountCard").rules("remove");
            $("#payment_numberAccountCard").rules("add", {
                required: true,
                creditCardStartingWith345: true,
                messages: {
                    required: "Requerido",
                    creditCartStartingWit345: "Tarjeta Invalida"
                }
            });
        }
    }
}
function creditCardCheck(val) {
    var sum = 0;
    for (var i = 0; i < val.length; i++) {
        var intVal = parseInt(val.substr(i, 1));
        if (i % 2 == 0) {
            intVal *= 2;
            if (intVal > 9) {
                intVal = 1 + (intVal % 10);
            }
        }
        sum += intVal;
    }
    return (sum % 10) == 0;
}

function ChangedCardClass(){
    isCreditCardValid($("#payment_numberAccountCard").val());
}

function showFieldsCreditCard(){

    //Cuenta Bancaria
        if($("#chargeType").val() == 2){
           $(".bank").show();
           $(".creditCard").hide();
           $(".field").show();
            $("#chkAgree").show();
            $("#formClient").validate();
            $("#payment_numberAccountCard").rules( "remove" );
            $("#payment_numberAccountCard").off()
            $("#payment_numberAccountCard").prop('maxlength',10);
            $("#payment_numberAccountCard" ).rules( "add", {
                required: true,
                messages: {
                    required: "Requerido",
                    minlength: "Por favor ingrese 10 dígitos",
                    maxlength: "Por favor ingrese 10 dígitos"
                }
            });
            $("#payment_isAgree" ).rules( "add", {
                required: true,
                messages: {
                    required: "Requerido"
                }
            });
            //Tarjeta Credito
        }else if($("#chargeType").val() == 3){
           $(".creditCard").show();
            $("#chkAgree").show();
           $(".bank").hide();
           $(".field").show();
            $("#payment_isAgree" ).rules( "add", {
                required: true,
                messages: {
                    required: "Requerido"
                }
            });
           var TipoTarjeta = $("#cardClass").val();
           if (TipoTarjeta == "3")
           {
               $("#formClient").validate();
               $( "#payment_numberAccountCard").rules( "remove" );
               $("#payment_numberAccountCard").prop('maxlength', 15);
               $( "#payment_numberAccountCard" ).rules( "add", {
                   required: true,
                   minlength: 15,
                   maxlength: 15,
                   messages: {
                       required: "Requerido",
                       minlength: "Por favor ingrese 15 dígitos",
                       maxlength: "Por favor ingrese 15 dígitos"
                   }
               });
           }
           else if(TipoTarjeta == "1" || TipoTarjeta == "2") {
               $("#formClient").validate();
               $("#payment_numberAccountCard").rules( "remove" );
               $("#payment_numberAccountCard").prop('maxlength', 16);
               $.validator.addMethod("creditCardValid", function(cardNumber, element) {
                   if  (creditCardCheck(cardNumber))
                       return true;
                   else
                       return false;
               }, "Tarjeta invalida");
               $("#payment_numberAccountCard" ).rules( "add", {
                   required: true,
                   minlength: 16,
                   maxlength: 16,
                   creditCardValid:true,
                   messages: {
                       required: "Requerido",
                       minlength: "Por favor ingrese 16 dígitos",
                       maxlength: "Por favor ingrese 16 dígitos"
                   }
               });
           }
           else{
               $("#formClient").validate();
               $("#payment_numberAccountCard").rules( "remove" );
               $("#payment_numberAccountCard" ).rules( "add", {
                   required: true,
                   creditCardStartingWith345:true,
                   messages: {
                       required: "Requerido",
                       creditCartStartingWit345: "Tarjeta Invalida"
                   }
               });
           }
           //Check Credit card
            $( "#payment_numberAccountCard").keyup(function() {
                isCreditCardValid($("#payment_numberAccountCard").val());
            });
           $("#payment_numberAccountCard").change(
                function() {
                    isCreditCardValid($("#payment_numberAccountCard").val());
                });

        }else{
           $(".creditCard").hide();
           $(".bank").hide();
           $(".fields").hide();
           $("#chkAgree").hide();
           $("#payment_isAgree" ).rules("remove");

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
       $("#beneficiaries").removeAttr("disabled","");
    }else{
       $("#beneficiaries").attr("disabled","disabled");
    }
}

function disableValidation() {
    $("#do_validation").val("false");
}

function enableValidation() {
    $("#do_validation").val("true");
}
function initValidator(){
        var validator = $("#formClient").validate({
        submitHandler: function(form) {
            var doValidation = $("#do_validation").val();
            var valid = true;
            if (doValidation == "true") {
                var current;
                var panelId;
                var tab = $("#tabs");
                var tabs_count = tab.children("ul").children("li").length;
                var active = 0;
                while (active < tabs_count && valid) {
                    tab.tabs("option", "active", active);
                    current = tab.tabs("option", "active");
                    panelId = $("#tabs ul a").eq(current).attr("href");
                    $(panelId).find(":input").each(function() {
                        if (!validator.element(this) && valid) {
                            valid = false;
                        }
                    });
                    if (!valid) {
                        break;
                    }
                    active++;
                }
            }
            if (valid) {
                form.submit();
            }
        },
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
                "client.licenseType.id": {
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
                        if( $("#clienteJuridico").is(":checked")){
                            return true;
                        }else{
                            return $("#client_passport").val().trim() == "";
                        }
                    }
                }
              },
              "client.passport": {
                maxlength: 50,
                required: {
                    depends: function(){
                        if($("#clienteJuridico").prop("checked")){
                            return false;
                        }else {
                            return $("#client_identificationDocument").val().trim() == "";
                        }
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
                maxlength: 13,
                minlength:13
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
                    required: true,
                    isCellphone: true,
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
                "client.phoneNumberWork2": {
                    number: true,
                    required: true,
                    isCellphone: true,
                    minlength: 8,
                    maxlength: 8
                },
                "client.phoneNumberWork3": {
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
                    let options = jQuery("#department_business option");
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
                    let options = jQuery("#municipality_business option");
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
                required: true
              },
              "legalRepresentative.email": {
                required: true,
                maxlength: 75
              },
              "legalRepresentative.registry": {
                required: true
              },
              "legalRepresentative.caseFile": {
                required: true,
                number: true
              },
                "legalRepresentative.phoneNumber1": {
                    number: true
                },
                "legalRepresentative.phoneNumber2": {
                    number: true,
                    required: true,
                    isCellphone: true,
                    minlength: 8,
                    maxlength: 8
                },
                "legalRepresentative.phoneNumber3": {
                    required: true,
                    number: true
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
                    let options = jQuery("#municipality_legal option");
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
                    let options = jQuery("#municipality_legal option");
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
                        if($("#pagadorJuridico").prop("checked")){
                            return true
                        }else{
                            return $("#payer_passport").val().trim() == "";
                        }
                    }
                }
              },
              "payer.passport": {
                maxlength: 50,
                required: {
                    depends: function(){
                        if($("#pagadorJuridico").prop("checked")){
                            return false;
                        }else {
                            return $("#payer_identificationDocument").val().trim() == "";
                        }
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
                number: true,
                minlength:13,
                maxlength:13
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
                "payer.phoneNumber2": {
                    number: true,
                    required: true,
                    isCellphone: true,
                    minlength: 8,
                    maxlength: 8
                },
                "payer.phoneNumber3": {
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
                "payer.phoneNumberWork2": {
                    number: true,
                    required: true,
                    isCellphone: true,
                    minlength: 8,
                    maxlength: 8
                },
                "payer.phoneNumberWork3": {
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
                    return $("#legalRepresentativePayer_passport").val().trim() == "";
                  }
                }
              },
              "legalRepresentativePayer.passport": {
                maxlength: 50,
                required: {
                  depends: function(){
                    return $("#legalRepresentativePayer_identificationDocument").val().trim() == "";

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
                required: true,
                number: true
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
                "legalRepresentativePayer.phoneNumber1":{
                    number: true
                },
                "legalRepresentativePayer.phoneNumber2": {
                    number: true,
                    required: true,
                    isCellphone: true,
                    minlength: 8,
                    maxlength: 8
                },
                "legalRepresentativePayer.phoneNumber3":{
                    required: true,
                    number: true
                },
                /*Client PEP*/
                "clientPEP.relationFirstSurname": {
                    required: true
                },
                "clientPEP.relationFirtName": {
                    required: true
                },
                /*Payer PEP*/
                "clientPayerPEP.relationFirstSurname": {
                    required: true
                },
                "clientPayerPEP.relationFirtName": {
                    required: true
                },

              /*Client Multimedia*/
              "dpi": {
                required: {
                            depends: function(){
                                return $("#urlDPI").val().trim() == "" && $("#client_codeClient").val() == "";
                            }
                        }
              },
              "receiptServices": {
                required: {
                            depends: function(){
                                return $("#urlReceiptServices").val().trim() == "" && $("#client_codeClient").val() == "";
                            }
                        }
              },
              "circulationCard": {
                required: {
                            depends: function(){
                                return $("#urlCirculationCard").val().trim() == ""
                                    && jQuery('#receiptServices')[0].files.length == 0;
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
              /*"authorizationForm": {
                required:  {
                            depends: function(){
                                if($("#isIndividual").is(":checked") && $("#isNew_true").is(":checked") && $("#urlAuthorizationForm").val().trim() == ""){
                                    return true;
                                }else if(!$("#isIndividual").is(":checked")  && $("#isNew_true").is(":checked")  && $("#urlAuthorizationForm_business").val().trim() == ""){
                                    return true;
                                }
                    return false;
                            }
                        }
              },*/
              "carInvoiceNew": {
                required:  {
                    depends: function(){
                        if($("#isIndividual").is(":checked") && $("#isNew_true").is(":checked") && $("#urlCarInvoiceNew").val().trim() == ""){
                            return true;
                        }else if(!$("#isIndividual").is(":checked")  && $("#isNew_true").is(":checked")  && $("#urlCarInvoiceNew_business").val().trim() == ""){
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
                                return $("#urlScanPatents").val().trim() == "" && $("#client_codeClient").val() == "";
                            }
                }
              },
              "scanLegalRepresentativeAppointment": {
                required: {
                            depends: function(){
                                return $("#urlScanLegalRepresentativeAppointment").val().trim() == "" && $("#client_codeClient").val() == "";
                            }
                }
              },
              "dpiLegalRepresentative": {
                required: {
                            depends: function(){
                                return $("#urlDPILegalRepresentative").val().trim() == "" && $("#client_codeClient").val() == "";
                            }
                }
              },
              "rtu": {
                required: {
                  depends: function(){
                    return $("#urlRTU").val().trim() == "" && $("#client_codeClient").val() == "";
                  }
                }
              },
              "receiptServicesLegal": {
                required: {
                  depends: function(){
                    return $("#urlReceiptServicesLegal").val().trim() == "" && $("#client_codeClient").val() == "";
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
              "vehicle.owner": {
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
                required: true,
                number: true
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
      "client.licenseType.id": "Requerido",
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
        maxlength: "13 caracteres maximo",
        minlength: "13 caracteres mínimo"
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
            required: "Requerido",
            minlength: "Por favor ingrese 8 dígitos",
            maxlength: "Por favor ingrese 8 dígitos"

        },
        "client.phoneNumber3": {
            required: "Requerido",
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
        "client.phoneNumberWork2": {
            number: "Solo debe contener números",
            required: "Requerido",
            minlength: "Por favor ingrese 8 dígitos",
            maxlength: "Por favor ingrese 8 dígitos"
        },
        "client.phoneNumberWork3": {
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
      "legalRepresentative.caseFile": {
        required: "Requerido",
        number: "Solo debe contener números"
      },
      "legalRepresentative.extendedIn": "Requerido",
      "legalRepresentative.registrationDate": "Requerido",
      "legalRepresentative.book": "Requerido",
      "legalRepresentative.folio": "Requerido",
      "legalRepresentative.country.id": "Requerido",
      "legalRepresentative.department.id": "Requerido",
      "legalRepresentative.municipality.id": "Requerido",
      "legalRepresentative.zone.id": "Requerido",
      "legalRepresentative.phoneNumber1":{
          required: "Requerido",
          number: "Solo debe contener números"
      },
        "legalRepresentative.phoneNumber2":{
            number: "Solo debe contener números",
            required: "Requerido",
            minlength: "Por favor ingrese 8 dígitos",
            maxlength: "Por favor ingrese 8 dígitos"
        },
        "legalRepresentative.phoneNumber3":{
            number: "Solo debe contener números"
        },
        /*Client PEP*/
        "clientPEP.relationFirtName" :{
            required: "Requerido"
        },
        "clientPEP.relationFirstSurname":{
            required: "Requerido"
        },
        /*Client Payer PEP*/
        "clientPayerPEP.relationFirtName" :{
            required: "Requerido"
        },
        "clientPayerPEP.relationFirstSurname":{
            required: "Requerido"
        },

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
        number: "Solo debe contener números",
        minlength: "13 caracteres mínimo",
        maxlength: "13 caracteres mínimo"
      },
      "payer.email": "Requerido",
      "payer.address": "Requerido",
      "payer.country.id": "Requerido",
      "payer.department.id": "Requerido",
      "payer.municipality.id": "Requerido",
      "payer.zone.id": "Requerido",
      "payer.phoneNumber1": {
            required: "Requerido",
            number: "Solo debe contener números"
        },
        "payer.phoneNumber2": {
            number: "Solo debe contener números",
            required: "Requerido",
            minlength: "Por favor ingrese 8 dígitos",
            maxlength: "Por favor ingrese 8 dígitos"
        },
        "payer.phoneNumber3": {
            required: "Requerido",
            number: "Solo debe contener números"
        },

      "payer.addressWork": "Requerido",
      "payer.countryWork.id": "Requerido",
      "payer.workDepartment.id": "Requerido",
      "payer.workMunicipality.id": "Requerido",
      "payer.workZone.id": "Requerido",
      "payer.phoneNumberWork1": {
        required: "Requerido",
            number: "Solo debe contener números"
        },
        "payer.phoneNumberWork2": {
            number: "Solo debe contener números",
            required: "Requerido",
            minlength: "Por favor ingrese 8 dígitos",
            maxlength: "Por favor ingrese 8 dígitos"
        },
        "payer.phoneNumberWork3": {
            number: "Solo debe contener números"
        },

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
      "legalRepresentativePayer.caseFile": {
        required: "Requerido",
        number: "Solo debe contener números"
      },
      "legalRepresentativePayer.extendedIn": "Requerido",
      "legalRepresentativePayer.registrationDate": "Requerido",
      "legalRepresentativePayer.book": "Requerido",
      "legalRepresentativePayer.folio": "Requerido",
      "legalRepresentativePayer.country.id": "Requerido",
      "legalRepresentativePayer.department.id": "Requerido",
      "legalRepresentativePayer.municipality.id": "Requerido",
      "legalRepresentativePayer.zone.id": "Requerido",
      "legalRepresentativePayer.phoneNumber1":{
          required: "Requerido",
          number: "Solo debe contener números"
      },
        "legalRepresentativePayer.phoneNumber2":{
            number: "Solo debe contener números",
            required: "Requerido",
            minlength: "Por favor ingrese 8 dígitos",
            maxlength: "Por favor ingrese 8 dígitos"
        },
        "legalRepresentativePayer.phoneNumber3":{
            number: "Solo debe contener números"
        },

      /* Client Multimedia */
      "dpi":"Requerido",
      "receiptServices": "Requerido",
      /*"circulationCard": "Requerido",
      "driverLicence": "Requerido",
      "authorizationForm": "Requerido",*/

      /*Business Client Multimedia*/
      "scanPatents": "Requerido",
      "scanLegalRepresentativeAppointment": "Requerido",
      "dpiLegalRepresentative": "Requerido",
      "rtu": "Requerido",
      "receiptServicesLegal": "Requerido",

      /*Vehicle*/
      "vehicle.chassis":{
        required : "Requerido",
        minlength : "El No. de chasis debe ser de 17 caracteres",
        maxlength : "El No. de chasis debe ser de 17 caracteres",
      },
      "vehicle.owner":      "Requerido",
      "vehicle.engine": "Requerido",
      "vehicle.plateType.id": "Requerido",
      "vehicle.plate": {
        required: "Requerido",
        validatePlate: "Ingrese mayúsculas y no debe contener vocales."
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
      "vehicle.mileage": {
        required: "Requerido",
        number: "Solo debe contener números"
      },
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
        if  ((Number.startsWith("2") || Number.startsWith("1")))
            return false;
        else
            return true;
    }, "No valido");

    $.validator.addMethod("creditCardStartingWith345", function(cardNumber, element) {
        if  ((!cardNumber.startsWith("4") || !cardNumber.startsWith("5") || !cardNumber.startsWith("3")) && ($("#cardClass").val() != "1" || $("#cardClass").val() != "3"))
            return false;
        else
            return true;
    }, "Tarjeta Invalida");

    // Validar NIT
    $.validator.addMethod("validarNit", function(value, el, param){
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
        //let cadena = value.toString();
     // let cerosIzquierda = 8 - cadena.length;
     // for (let i = 0; i < cerosIzquierda; i++){
     // }
     // var digitos = cadena.substring(0,7);
     // var contador = 8;
     // var suma = 0;
     // for(i=0; i<7; i++){
     // suma += parseInt(digitos[i]) * contador;
     // contador--;
     // }
     // var residuo = suma%11;
     // var res = 11 - residuo;
     // res = res%11;
     //
     //if(res == 10){
     //return cadena.substring(7,8).toUpperCase() == 'K';
     //}else{
     //return cadena.substring(7,8) == res;
     //}
    }, "NIT inválido");
    $.validator.addMethod("validatePlate", function(value, element){
        let regexpr = /[0-9]{3}[B-DF-HJ-NP-TV-Z]{3}/;
        return regexpr.test(value);
    }, "Ingrese mayúsculas y no debe contener vocales.");
    $.validator.setDefaults({
         ignore: ""
  })

  var activeTab = $("#active_tab").val();
  var tabs = $("#tabs").tabs({
    active: activeTab,
    beforeActivate: function(event, ui) {
      var valid = true;
      var current = $(this).tabs("option", "active");
      var panelId = $("#tabs ul a").eq(current).attr("href");
      $(panelId).find(":input").each(function() {
        if (!validator.element(this) && valid) {
          valid = false;
        }
      });

      return valid;
    },
    activate: function(event, ui) {
      var index = $(this).tabs("option", "active");
      $("#active_tab").val(index);
    }
  });
}

