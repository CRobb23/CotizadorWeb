function init(args) {
    initValidator();
    $("#useDataClientPayer").change(showFieldsPayer);
    $("#useDataClientPayer").trigger("change");
    $(".typePayer").change(showFieldsPayerType);
    $(".typePayer").trigger("change");

    /*Para clientPEP*/
    $("#payer_expose").change(showFieldsClientPayerPEP);
    $("#payer_expose").trigger("change");
    $(".typePayerPEP").change(changePayerPEP);
    $(".typePayerPEP").trigger("change");

    $(".relationPayerPEP").change(changeRelationPayerPEP);
    $(".relationPayerPEP").trigger("change");

    if ($("#payer_address").val() != "") {
        $("#payer_address").prop("type", "password");
        $("#payerAddressLbl").html($("#payer_address").val().substring($("#payer_address").val().length - 15, $("#payer_address").val().length));
        $("#country_payer").rules("remove", "required");
        $("#department_payer").rules("remove", "required");
        $("#municipality_payer").rules("remove", "required");
        $("#zone_payer").rules("remove", "required");
    }
    if ($("#payer_phoneNumber1").val() != "") {
        $("#payer_phoneNumber1").prop("type", "password");
        $("#payerPhoneNumberLbl").html($("#payer_phoneNumber1").val().substring($("#payer_phoneNumber1").val().length - 3, $("#payer_phoneNumber1").val().length));
    }
    if ($("#payer_phoneNumber2").val() != "") {
        $("#payer_phoneNumber2").prop("type", "password");
        $("#payerPhoneNumberLb2").html($("#payer_phoneNumber2").val().substring($("#payer_phoneNumber2").val().length - 3, $("#payer_phoneNumber2").val().length));
    }
    if ($("#payer_phoneNumber3").val() != "") {
        $("#payer_phoneNumber3").prop("type", "password");
        $("#payerPhoneNumberLb3").html($("#payer_phoneNumber3").val().substring($("#payer_phoneNumber3").val().length - 3, $("#payer_phoneNumber3").val().length));
    }
    if ($("#payer_addressWork").val() != "") {
        $("#payer_addressWork").prop("type", "password");
        $("#payerWorkAddressLbl").html($("#payer_addressWork").val().substring($("#payer_addressWork").val().length - 15, $("#payer_addressWork").val().length));
        $("#country_work_payer").rules("remove", "required");
        $("#work_payer_department").rules("remove", "required");
        $("#work_payer_municipality").rules("remove", "required");
        $("#work_payer_zone").rules("remove", "required");
    }
    if ($("#payer_phoneNumberWork1").val() != "") {
        $("#payer_phoneNumberWork1").prop("type", "password");
        $("#payerWorkPhoneNumberLbl").html($("#payer_phoneNumberWork1").val().substring($("#payer_phoneNumberWork1").val().length - 3, $("#payer_phoneNumberWork1").val().length));
    }
    if ($("#payer_phoneNumberWork2").val() != "") {
        $("#payer_phoneNumberWork2").prop("type", "password");
        $("#payerWorkPhoneNumberLb2").html($("#payer_phoneNumberWork2").val().substring($("#payer_phoneNumberWork2").val().length - 3, $("#payer_phoneNumberWork2").val().length));
    }
    if ($("#payer_phoneNumberWork3").val() != "") {
        $("#payer_phoneNumberWork3").prop("type", "password");
        $("#payerWorkPhoneNumberLb3").html($("#payer_phoneNumberWork3").val().substring($("#payer_phoneNumberWork3").val().length - 3, $("#payer_phoneNumberWork3").val().length));
    }
    if ($("#payer_address_business").val() != "") {
        $("#payer_address_business").prop("type", "password");
        $("#payerBussinessWorkAddressLbl").html($("#payer_address_business").val().substring($("#payer_address_business").val().length - 15, $("#payer_address_business").val().length));
        $("#country_business_payer").rules("remove", "required");
        $("#department_business_payer").rules("remove", "required");
        $("#municipality_business_payer").rules("remove", "required");
        $("#zone_business_payer").rules("remove", "required");
    }
    if ($("#payer_phoneNumber1_business").val() != "") {
        $("#payer_phoneNumber1_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLbl").html($("#payer_phoneNumber1_business").val().substring($("#payer_phoneNumber1_business").val().length - 3, $("#payer_phoneNumber1_business").val().length));
    }
    if ($("#payer_phoneNumber2_business").val() != "") {
        $("#payer_phoneNumber2_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLb2").html($("#payer_phoneNumber2_business").val().substring($("#payer_phoneNumber2_business").val().length - 3, $("#payer_phoneNumber2_business").val().length));
    }
    if ($("#payer_phoneNumber3_business").val() != "") {
        $("#payer_phoneNumber3_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLb3").html($("#payer_phoneNumber3_business").val().substring($("#payer_phoneNumber3_business").val().length - 3, $("#payer_phoneNumber3_business").val().length));
    }
    if ($("#legalRepresentativePayer_address").val() != "") {
        $("#legalRepresentativePayer_address").prop("type", "password");
        $("#legalRepresentantivePayerAddressLbl").html($("#legalRepresentativePayer_address").val().substring($("#legalRepresentativePayer_address").val().length - 15, $("#legalRepresentativePayer_address").val().length));
        $("#country_legal_payer").rules("remove", "required");
        $("#department_legal_payer").rules("remove", "required");
        $("#municipality_legal_payer").rules("remove", "required");
        $("#zone_legal_payer").rules("remove", "required");
    }
    if ($("#legalRepresentativePayer_phoneNumber1").val() != "") {
        $("#legalRepresentativePayer_phoneNumber1").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLbl").html($("#legalRepresentativePayer_phoneNumber1").val().substring($("#legalRepresentativePayer_phoneNumber1").val().length - 3, $("#legalRepresentativePayer_phoneNumber1").val().length));
    }
    if ($("#legalRepresentativePayer_phoneNumber2").val() != "") {
        $("#legalRepresentativePayer_phoneNumber2").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLb2").html($("#legalRepresentativePayer_phoneNumber2").val().substring($("#legalRepresentativePayer_phoneNumber2").val().length - 3, $("#legalRepresentativePayer_phoneNumber2").val().length));
    }
    if ($("#legalRepresentativePayer_phoneNumber3").val() != "") {
        $("#legalRepresentativePayer_phoneNumber3").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLb3").html($("#legalRepresentativePayer_phoneNumber3").val().substring($("#legalRepresentativePayer_phoneNumber3").val().length - 3, $("#legalRepresentativePayer_phoneNumber3").val().length));
    }


    $("#checkPayerTaxNumber").click(function () {
        checkPayer();
        return false;
    });
    $("#checkPayerIdentificationDoc").click(function () {
        checkPayer();
        return false;
    });

    $("#useSamePayerAddress").change(function () {
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

    $(".datePicker").datetimepicker({
        locale: "es",
        format: "DD/MM/YYYY",
        dayViewHeaderFormat: "MMMM YYYY"
    });

    $("#country_payer").change(refreshDepend);
    $("#country_work_payer").change(refreshDepend);
    $("#country_business_payer").change(refreshDepend);
    $("#country_legal_payer").change(refreshDepend);

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


    $("#country_payer").trigger("change");
    $("#country_legal_payer").trigger("change");
    $("#country_work_payer").trigger("change");
    $("#country_business_payer").trigger("change");
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
        url: "/incident/getClientsById",
        type: "post",
        data: {information: taxNumber + "|" + idNumber},
        complete: function (result) {
            console.log(result.responseJSON);
            if (result.responseJSON.success) {
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

function personPayerDetails(client) {
    $.ajax({
        url: "/incident/getPersonDetails",
        type: "post",
        data: {information: client.personType + "|" + client.codeClient + "|" + client.cifClient},
        complete: function (result) {
            console.log(result.responseJSON);
            if (result.responseJSON.success) {
                if (result.responseJSON.person != null) {
                    $("#payer_nationality").rules("remove", "required");
                    $("#payer_email").rules("remove", "required");
                    $("#profession").rules("remove", "required");
                    $("#payer_licenseType").rules("remove", "required");
                    $("#payer_licenseNumber").rules("remove", "required");
                    $("#payer_address").rules("remove", "required");
                    $("#country_payer").rules("remove", "required");
                    $("#department_payer").rules("remove", "required");
                    $("#municipality_payer").rules("remove", "required");
                    $("#zone_payer").rules("remove", "required");
                    $("#payer_phoneNumber1").rules("remove", "required");
                    $("#payer_phoneNumber2").rules("remove", "required");
                    $("#payer_addressWork").rules("remove", "required");
                    $("#country_work_payer").rules("remove", "required");
                    $("#work_payer_department").rules("remove", "required");
                    $("#work_payer_zone").rules("remove", "required");
                    $("#work_payer_municipality").rules("remove", "required");
                    $("#payer_phoneNumberWork1").rules("remove", "required");
                    $("#payer_phoneNumberWork2").rules("remove", "required");
                    $("#btnEditPartial").hide();
                    fillPersonPayer(result.responseJSON.person);
                } else if (result.responseJSON.business != null) {
                    $("#payer_nationality").rules("remove", "required");
                    $("#payer_email").rules("remove", "required");
                    $("#payer_address_business").rules("remove", "required");
                    $("#society_type").rules("remove", "required");
                    $("#country_business_payer").rules("remove", "required");
                    $("#department_business_payer").rules("remove", "required");
                    $("#municipality_business_payer").rules("remove", "required");
                    $("#zone_business_payer").rules("remove", "required");
                    $("#legalRepresentativePayer_taxNumber").rules("remove", "required");
                    $("#legalRepresentativePayer_firstName").rules("remove", "required");
                    $("#legalRepresentativePayer_firstSurname").rules("remove", "required");
                    $("#legalRepresentativePayer_birthdate").rules("remove", "required");
                    $("#sexRep").rules("remove", "required");
                    $("#professionRep").rules("remove", "required");
                    $("#legalRepresentativePayer_identificationDocument").rules("remove", "required");
                    $("#civilStatusRep").rules("remove", "required");
                    $("#legal_nationality").rules("remove", "required");
                    $("#legalRepresentativePayer_email").rules("remove", "required");
                    $("#legalRepresentativePayer_registry").rules("remove", "required");
                    $("#legalRepresentativePayer_caseFile").rules("remove", "required");
                    $("#legalRepresentativePayer_extendedIn").rules("remove", "required");
                    $("#legalRepresentativePayer_registrationDate").rules("remove", "required");
                    $("#legalRepresentative_identificationDocument").rules("remove", "required");
                    $("#legalRepresentativePayer_book").rules("remove", "required");
                    $("#legalRepresentativePayer_folio").rules("remove", "required");
                    $("#legalRepresentativePayer_address").rules("remove", "required");
                    $("#country_legal_payer").rules("remove", "required");
                    $("#department_legal_payer").rules("remove", "required");
                    $("#zone_legal_payer").rules("remove", "required");
                    $("#municipality_legal_payer").rules("remove", "required");
                    $("#legalRepresentativePayer_phoneNumber3").rules("remove", "required");
                    $("#legalRepresentativePayer_phoneNumber2").rules("remove", "required");
                    $("#btnEditPartial").hide();
                    fillBusinessPayer(result.responseJSON.business);
                }
            }
        }
    });
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
        $("#payerAddressLbl").html(person.addressHome.substring(person.addressHome.length - 15, person.addressHome.length));
        $("#country_payer").rules("remove", "required");
        $("#department_payer").rules("remove", "required");
        $("#municipality_payer").rules("remove", "required");
        $("#zone_payer").rules("remove", "required");
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
        $("#payerPhoneNumberLbl").html(person.phone1Home.substring(person.phone1Home.length - 3, person.phone1Home.length));
    }
    if (person.phone2Home != null) {
        $("#payer_phoneNumber2").val(person.phone2Home);
        $("#payer_phoneNumber2").prop("type", "password");
        $("#payerPhoneNumberLb2").html(person.phone2Home.substring(person.phone2Home.length - 3, person.phone2Home.length));
    }
    if (person.phone3Home != null) {
        $("#payer_phoneNumber3").val(person.phone3Home);
        $("#payer_phoneNumber3").prop("type", "password");
        $("#payerPhoneNumberLb3").html(person.phone3Home.substring(person.phone3Home.length - 3, person.phone3Home.length));
    }
    if (person.addressWork != null) {
        $("#payer_addressWork").prop("type", "password");
        $("#payer_addressWork").val(person.addressWork);
        $("#payerWorkAddressLbl").html(person.addressWork.substring(person.addressWork.length - 15, person.addressWork.length));
        $("#country_work_payer").rules("remove", "required");
        $("#work_payer_department").rules("remove", "required");
        $("#work_payer_municipality").rules("remove", "required");
        $("#work_payer_zone").rules("remove", "required");

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
        $("#payerWorkPhoneNumberLbl").html(person.phone1Work.substring(person.phone1Work.length - 3, person.phone1Work.length));
        $("#payer_phoneNumberWork1").val(person.phone1Work);
    }
    if (person.phone2Work != null) {
        $("#payer_phoneNumberWork2").val(person.phone2Work);
        $("#payer_phoneNumberWork2").prop("type", "password");
        $("#payerWorkPhoneNumberLb2").html(person.phone2Work.substring(person.phone2Work.length - 3, person.phone2Work.length));
    }
    if (person.phone3Work != null) {
        $("#payer_phoneNumberWork3").val(person.phone3Work);
        $("#payer_phoneNumberWork3").prop("type", "password");
        $("#payerWorkPhoneNumberLb3").html(person.phone3Work.substring(person.phone3Work.length - 3, person.phone3Work.length));
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
        $("#payerBussinessWorkAddressLbl").html(business.addressWork.substring(business.addressWork.length - 15, business.addressWork.length));
        $("#payer_address_business").val(business.addressWork);
        $("#country_business_payer").rules("remove", "required");
        $("#department_business_payer").rules("remove", "required");
        $("#municipality_business_payer").rules("remove", "required");
        $("#zone_business_payer").rules("remove", "required");
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
        $("#payerBussinessPhoneNumberLbl").html(business.phone1Work.substring(business.phone1Work.length - 3, business.phone1Work.length));
        $("#payer_phoneNumber1_business").val(business.phone1Work);
    }
    if (business.phone2Work != null) {
        $("#payer_phoneNumber2_business").val(business.phone2Work);
        $("#payer_phoneNumber2_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLb2").html(business.phone2Work.substring(business.phone2Work.length - 3, business.phone2Work.val().length));
    }
    if (business.phone3Work != null) {
        $("#payer_phoneNumber3_business").val(business.phone3Work);
        $("#payer_phoneNumber3_business").prop("type", "password");
        $("#payerBussinessPhoneNumberLb3").html(business.phone3Work.substring(business.phone3Work.length - 3, business.phone3Work.length));

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
        $("#legalRepresentantivePayerAddressLbl").html(business.addressLegal.substring(business.addressLegal.length - 15, business.addressLegal.length));
        $("#country_legal_payer").rules("remove", "required");
        $("#department_legal_payer").rules("remove", "required");
        $("#municipality_legal_payer").rules("remove", "required");
        $("#zone_legal_payer").rules("remove", "required");
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
        $("#legalRepresentantivePayerPhoneNumberLbl").html(business.phone1Legal.substring(business.phone1Legal.length - 3, business.phone1Legal.length));
        $("#legalRepresentativePayer_phoneNumber1").val(business.phone1Legal);

    }
    if (business.phone2Legal != null) {
        $("#legalRepresentativePayer_phoneNumber2").val(business.phone2Legal);
        $("#legalRepresentativePayer_phoneNumber2").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLb2").html(business.phone2Legal.substring(business.phone2Legal.length - 3, business.phone2Legal.length));
    }
    if (business.phone3Legal != null) {
        $("#legalRepresentativePayer_phoneNumber3").val(business.phone3Legal);
        $("#legalRepresentativePayer_phoneNumber3").prop("type", "password");
        $("#legalRepresentantivePayerPhoneNumberLb3").html(business.phone3Legal.substring(business.phone3Legal.length - 3, business.phone3Legal.length));
    }
}

function modalPayer(clients) {
    console.log(clients);
    $("#payersTable").children().remove();
    // Fill table with clients data
    for (i = 0; i < clients.length; i += 1) {
        client = clients[i];
        console.log($("clientsTable"));
        $("#payersTable").append("<tr class='payersRow'><td><label class='code'><input type='radio' id='" + client.codeClient + "' name='optradio'/>" + client.codeClient + "</label></td><td><label for='" + client.codeClient + "'>" + client.name + "</label></td><td><label for='" + client.codeClient + "'>" + client.cifClient + "</label></td></tr>");
    }
    var dialog = $("#dialog-payers").dialog({
        dialogClass: "no-close",
        autoOpen: false,
        height: 400,
        width: 350,
        modal: true,
        buttons: [
            {
                text: "Seleccionar",
                click: function () {
                    if (selectPayer(clients)) {
                        $(this).dialog("close");
                    }
                }
            }
        ]
    });
    dialog.dialog("open");
}

function selectPayer(clients) {
    console.log($(".payersRow"));
    var clientCode = "";
    var countChecked = 0;
    $(".payersRow").each(function () {
        if (!$(this).find("input[type='radio']").is(":checked")) {
            countChecked++;
        } else {
            var radioValue = $(this).find("input[type='radio']:checked").val();
            clientCode = $(this).find("label[class='code']").text();
            console.log("This row has checked: " + clientCode);
        }
    });
    if (countChecked == clients.length) {
        alert("Debe seleccionar un pagador para continuar.");
        return false;
    }
    for (i = 0; i < clients.length; i += 1) {
        client = clients[i];
        if (client.codeClient == clientCode) {
            personPayerDetails(client);
        }
    }
    return true;
}

function addEventSelectDepend(idSelectDepend, idSelect, args) {
    $("#" + idSelectDepend).change(function () {
        var selectelement = $(this);
        selectelement.prop("disabled", true);
        $("#" + idSelect).html("<option value=''>" + args.formSelectLoading + "</option>");
        var id = $(this).val();
        $.ajax({
            type: "POST",
            url: args.urlGetGeographicAreaChildren,
            data: {id: id, tipo: idSelect},
            success: function (response) {
                $("#" + idSelect).html("<option value=''>" + args.formSelect + "</option>");
                if (response.success) {
                    var html = "";
                    $.each(response.areas, function (index, area) {
                        html += "<option value='" + area.id + "'>" + area.name + "</option>";
                    });
                    $("#" + idSelect).append(html);
                    var selectVal = $("#" + idSelect + "_selection").val();
                    if (selectVal != "") {
                        $("#" + idSelect).val(selectVal).change();
                    }
                }
            },
            error: function () {
                $("#" + idSelect).html("<option value=''>" + args.formSelectError + "</option>");
            },
            complete: function () {
                if (idSelect == "work_payer_municipality") {
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

function refreshDepend() {
    if ($(this).attr("id") == "country_payer") {
        $("#municipality_payer").html("<option value=''>Seleccionar</option>");
        $("#zone_payer").html("<option value=''>Seleccionar</option>");
    }
    else if ($(this).attr("id") == "country_work_payer") {
        $("#work_payer_municipality").html("<option value=''>Seleccionar</option>");
        $("#work_payer_zone").html("<option value=''>Seleccionar</option>");
    }
}

function showFieldsPayer() {
    if (!$(this).is(":checked")) {
        $(".fieldsPayer input").removeAttr("disabled");
        $(".fieldsPayer select").removeAttr("disabled");
        $(".fieldsPayer").show();
    } else {
        $(".fieldsPayer").hide();
        $(".fieldsPayer input").attr("disabled", "disabled");
        $(".fieldsPayer select").attr("disabled", "disabled");
    }
}

function showFieldsClientPayerPEP() {
    if ($(this).attr("id") == "payer_expose" && $(this).is(":checked")) {
        $(".fieldsClientPayerPEP").show();
        $(".fieldsClientPayerPEP select").removeAttr("disabled");
        $(".fieldsClientPayerPEP input").removeAttr("disabled");
    } else {
        $(".fieldsClientPayerPEP").hide();
        $(".fieldsClientPayerPEP input").attr("disabled", "disabled");
        $(".fieldsClientPayerPEP select").attr("disabled", "disabled");
    }
}

function changePayerPEP() {
    if ($(this).attr("id") == "radPEPPayerParentesco" && $(this).is(":checked")) {

        $(".relationPayerPEP").empty();
        $("#lblRelationshipPayer").text("Parentesco:");
        $(".relationPayerPEP").append("<option value='Padre'>Padre</option>");
        $(".relationPayerPEP").append("<option value='Madre'>Madre</option>");
        $(".relationPayerPEP").append("<option value='Hijo(a)'>Hijo(a)</option>");
        $(".relationPayerPEP").append("<option value='Hermano(a)'>Hermano(a)</option>");
        $(".relationPayerPEP").append("<option value='C&oacute;nyuge'>C&oacute;nyuge</option>");
        $(".relationPayerPEP").append("<option value='Otro'>Otro</option>");
        $("#divRelativePayer").show();
        $(".relationPayerPEP").trigger("change");

    } else if ($(this).attr("id") == "radPEPPayerAsociado" && $(this).is(":checked")) {
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
    else if ($(this).attr("id") == "radPEPPayerNinguno" && $(this).is(":checked")) {
        $("#divRelativePayer").hide();
    }
}

function changeRelationPayerPEP() {
    if ($(this).find(":selected").val() == "Otro") {
        $("#divOtroPayer").show();
    }
    else {
        $("#clientPayerPEP.specificRelationship").val("");
        $("#divOtroPayer").hide();
    }
}

function showFieldsPayerType() {
    if ($(this).attr("id") == "pagadorIndividual" && $(this).is(":checked")) {
        $(".fieldsClientPayer input").removeAttr("disabled");
        $(".fieldsClientPayer select").removeAttr("disabled");
        $(".fieldsClientPayer").show();
        $(".fieldsLegalPayer").hide();
        $("#lbluseSamePayerAddress").show()
        $("#useSamePayerAddress").show();
        $(".fieldsLegalPayer input").attr("disabled", "disabled");
        $(".fieldsLegalPayer select").attr("disabled", "disabled");

    } else if ($(this).attr("id") == "pagadorJuridico" && $(this).is(":checked")) {
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

function changeFieldsPayerType() {
    if ($("#pagadorIndividual").is(":checked")) {
        $(".fieldsClientPayer input").removeAttr("disabled");
        $(".fieldsClientPayer select").removeAttr("disabled");
        $(".fieldsClientPayer").show();
        $(".fieldsLegalPayer").hide();
        $(".fieldsLegalPayer input").attr("disabled", "disabled");
        $(".fieldsLegalPayer select").attr("disabled", "disabled");
    } else if ($("#pagadorJuridico").is(":checked")) {
        $(".fieldsLegalPayer input").removeAttr("disabled");
        $(".fieldsLegalPayer select").removeAttr("disabled");
        $(".fieldsLegalPayer").show();
        $(".fieldsClientPayer").hide();
        $(".fieldsClientPayer input").attr("disabled", "disabled");
        $(".fieldsClientPayer select").attr("disabled", "disabled");
    }
}

function initValidator() {
    var validator = $("#formPayer").validate({
        rules: {
            /*Client Payer*/
            "payer.taxNumber": {
                required: true
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
                    depends: function () {
                        if ($("#pagadorJuridico").prop("checked")) {
                            return true
                        } else {
                            return $("#payer_passport").val().trim() == "";
                        }
                    }
                }
            },
            "payer.passport": {
                maxlength: 50,
                required: {
                    depends: function () {
                        if ($("#pagadorJuridico").prop("checked")) {
                            return false;
                        } else {
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
                minlength: 13,
                maxlength: 13
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                required: {
                    depends: function () {
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
                    depends: function () {
                        return $("#legalRepresentativePayer_passport").val().trim() == "";
                    }
                }
            },
            "legalRepresentativePayer.passport": {
                maxlength: 50,
                required: {
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
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
                    depends: function () {
                        let options = jQuery("#municipality_legal_payer option")
                        var required = true;
                        if (options.size() < 2) {
                            required = false;
                        }
                        return required;
                    }
                }
            },
            "legalRepresentativePayer.phoneNumber1": {
                number: true
            },
            "legalRepresentativePayer.phoneNumber2": {
                number: true,
                required: true,
                isCellphone: true,
                minlength: 8,
                maxlength: 8
            },
            "legalRepresentativePayer.phoneNumber3": {
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
            }
        },
        messages: {
            /*Client Payer PEP*/
            "clientPayerPEP.relationFirtName": {
                required: "Requerido"
            },
            "clientPayerPEP.relationFirstSurname": {
                required: "Requerido"
            },

            /*Client Payer*/
            "payer.taxNumber": {
                required: "Requerido",
                validarNit: "NIT inválido"
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
            "legalRepresentativePayer.taxNumber": {
                required: "Requerido",
                validarNit: "NIT inválido"
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
            "legalRepresentativePayer.phoneNumber1": {
                required: "Requerido",
                number: "Solo debe contener números"
            },
            "legalRepresentativePayer.phoneNumber2": {
                number: "Solo debe contener números",
                required: "Requerido",
                minlength: "Por favor ingrese 8 dígitos",
                maxlength: "Por favor ingrese 8 dígitos"
            },
            "legalRepresentativePayer.phoneNumber3": {
                number: "Solo debe contener números"
            }
        },
        highlight: function (element) {
            $(element).closest(".form-group").removeClass("has-success").addClass("has-error");
        },
        unhighlight: function (element) {
            $(element).closest(".form-group").removeClass("has-error").addClass("has-success");
        },
        errorElement: "span",
        errorClass: "help-block"
    });

    $.validator.addMethod("isCellphone", function (Number, element) {
        if ((Number.startsWith("2") || Number.startsWith("1")))
            return false;
        else
            return true;
    }, "No valido");
}

