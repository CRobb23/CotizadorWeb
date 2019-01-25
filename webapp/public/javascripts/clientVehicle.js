function init(args) {
    initValidator();
    $("#armor").change(disabledArmorValue);
    $("#armor").trigger("change");
    $("#warranty").change(disabledBeneficiaries);
    $("#warranty").trigger("change");

    $(".datePicker").datetimepicker({
        locale: "es",
        format: "DD/MM/YYYY",
        dayViewHeaderFormat: "MMMM YYYY"
    });

    $("#brand_line").change(function () {
        var selectelement = $(this);
        selectelement.prop("disabled", true);
        $("#line").html("<option value=''>" + args.formSelectLoading + "</option>");
        var id = $(this).val();
        $.ajax({
            type: "POST",
            url: args.urlGetLinesForBrand,
            data: {id: id},
            success: function (response) {
                $("#line").html('<option value=">' + args.formSelect + '"</option>');
                if (response.success) {
                    var html = "";
                    $.each(response.lines, function (index, line) {
                        html += "<option value='" + line.id + "'>" + line.name + "</option>";
                    });
                    $("#line").append(html);
                }
            },
            error: function () {
                $("#line").html("<option value=''>" + args.formSelectError + "</option>");
            },
            complete: function () {
                selectelement.prop("disabled", false);
            }
        });
    });

    function disabledArmorValue() {
        if ($("#armor").val() == "S") {
            $("#vehicle_armorValue").removeAttr("disabled", "");
        } else {
            $("#vehicle_armorValue").attr("disabled", "disabled");
        }
    }

    function disabledBeneficiaries() {
        if ($("#warranty").val() == "S") {
            $("#beneficiaries").removeAttr("disabled", "");
        } else {
            $("#beneficiaries").attr("disabled", "disabled");
        }
    }
}

    function initValidator(){
        var validator = $("#formVehicle").validate({
        rules: {
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
                    depends: function () {
                        return $("#plateType").val().trim() !== "9";
                    }
                }
            },
               /* validatePlate: {
                    depends: function(){
                        return $("#plateType").val().trim() != "9";
                    }
                }
            },*/
            "vehicle.invoiceDate": {
                required: true
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
            }
            },
            messages: {
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
                    required: "Requerido"
                  //  validatePlate: "Ingrese mayúsculas y no debe contener vocales."
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
                    "vehicle.invoiceDate": "Requerido",
                    "vehicle.mileage": {
                    required: "Requerido",
                        number: "Solo debe contener números"
                },
                "vehicle.loanNumber": "Requerido",
                    "vehicle.warranty": "Requerido",
                    "vehicle.reminderType.id": "Requerido",
                    "vehicle.numberDoor": "Requerido",
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
        }
