function init(args) {

function ValidateMaxSizeMultimedia(uploadField) {
    if(uploadField.files[0].size > 1300000){
        alert("El archivo no debe ser mayor a 1.3 mb, favor comprimir o bajar calidad si es fotografia");
        uploadField.value = "";
    };
}

function ValidateMaxFilesAndSizeMultimedia(uploadField) {
    if($(uploadField)[0].files.length > 4) {
        alert("Solo puedes seleccionar un maximo de 4 archivos");
        uploadField.value = "";
    }
    for (var i = 0; i<$(uploadField)[0].files.length; i++)
    {
        if ($(uploadField)[0].files[i].size > 1300000) {
            alert("El archivo no debe ser mayor a 1.3 mb, favor comprimir o bajar calidad si es fotografia");
            uploadField.value = "";
        }
        ;
    }
}

    function initValidator() {
        var validator = $("#formClient").validate({
            rules: {

                /*Client Multimedia*/
                "dpi": {
                    required: {
                        depends: function () {
                            return $("#urlDPI").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                },
                "receiptServices": {
                    required: {
                        depends: function () {
                            return $("#urlReceiptServices").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                },
                "circulationCard": {
                    required: {
                        depends: function () {
                            return $("#urlCirculationCard").val().trim() == ""
                                && jQuery('#receiptServices')[0].files.length == 0;
                        }
                    }
                },
                "driverLicence": {
                    required: {
                        depends: function () {
                            return $("#urlDriverLicence").val().trim() == "";
                        }
                    }
                },
                "carInvoiceNew": {
                    required: {
                        depends: function () {
                            if ($("#isIndividual").is(":checked") && $("#isNew_true").is(":checked") && $("#urlCarInvoiceNew").val().trim() == "") {
                                return true;
                            } else if (!$("#isIndividual").is(":checked") && $("#isNew_true").is(":checked") && $("#urlCarInvoiceNew_business").val().trim() == "") {
                                return true;
                            }
                            return false;
                        }
                    }
                },
                /*Business Client Multimedia*/
                "scanPatents": {
                    required: {
                        depends: function () {
                            return $("#urlScanPatents").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                },
                "scanLegalRepresentativeAppointment": {
                    required: {
                        depends: function () {
                            return $("#urlScanLegalRepresentativeAppointment").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                },
                "dpiLegalRepresentative": {
                    required: {
                        depends: function () {
                            return $("#urlDPILegalRepresentative").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                },
                "rtu": {
                    required: {
                        depends: function () {
                            return $("#urlRTU").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                },
                "receiptServicesLegal": {
                    required: {
                        depends: function () {
                            return $("#urlReceiptServicesLegal").val().trim() == "" && $("#client_codeClient").val() == "";
                        }
                    }
                }
            },
            messages: {
                /* Client Multimedia */
                "dpi": "Requerido",
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
    }
}