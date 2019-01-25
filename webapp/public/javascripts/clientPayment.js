function init(args) {
    initValidator();
    showFieldsCreditCard();
    $("#chargeType").change(showFieldsCreditCard);
    $("#chargeType").trigger("change");

    $("#cardClass").change(ChangedCardClass);
    $("#cardClass").trigger("change");

    function isCreditCardValid(val) {
        var TipoTarjeta = $("#cardClass").val();
        if ($("#chargeType").val() == 3) {
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

    function ChangedCardClass() {
        isCreditCardValid($("#payment_numberAccountCard").val());
    }

    function showFieldsCreditCard() {

        //Cuenta Bancaria
        if ($("#chargeType").val() == 2) {
            $(".bank").show();
            $(".creditCard").hide();
            $(".field").show();
            $("#chkAgree").show();
            $("#formClient").validate();
            $("#payment_numberAccountCard").rules("remove");
            $("#payment_numberAccountCard").off()
            $("#payment_numberAccountCard").prop('maxlength', 10);
            $("#payment_numberAccountCard").rules("add", {
                required: true,
                messages: {
                    required: "Requerido",
                    minlength: "Por favor ingrese 10 dígitos",
                    maxlength: "Por favor ingrese 10 dígitos"
                }
            });
            $("#payment_isAgree").rules("add", {
                required: true,
                messages: {
                    required: "Requerido"
                }
            });
            //Tarjeta Credito
        } else if ($("#chargeType").val() == 3) {
            $(".creditCard").show();
            $("#chkAgree").show();
            $(".bank").hide();
            $(".field").show();
            $("#payment_isAgree").rules("add", {
                required: true,
                messages: {
                    required: "Requerido"
                }
            });
            var TipoTarjeta = $("#cardClass").val();
            if (TipoTarjeta == "3") {
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
            else if (TipoTarjeta == "1" || TipoTarjeta == "2") {
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
            //Check Credit card
            $("#payment_numberAccountCard").keyup(function () {
                isCreditCardValid($("#payment_numberAccountCard").val());
            });
            $("#payment_numberAccountCard").change(
                function () {
                    isCreditCardValid($("#payment_numberAccountCard").val());
                });

        } else {
            $(".creditCard").hide();
            $(".bank").hide();
            $(".fields").hide();
            $("#chkAgree").hide();
            $("#payment_isAgree").rules("remove");

        }
    }
    $(".datePicker").datetimepicker({
        locale: "es",
        format: "DD/MM/YYYY",
        dayViewHeaderFormat: "MMMM YYYY"
    });
}

    function initValidator(){
        var validator = $("#formPayment").validate({
        rules: {
            /* Payment */
            "payment.chargeType.id": {
                required: true
            },
            "payment.numberAccountCard": {
                required: true
            },
            "payment.bankAccountType.id": {
                required: true
            },
            "payment.bank.id": {
                required: true
            },
            "payment.cardType.id": {
                required: true
            },
            "payment.cardClass.id": {
                required: true
            },
            "payment.expirationDate": {
                required: true
            },
            "payment.codeAccountCard": {
                required: true
            }
        },
        messages: {
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
    $.validator.addMethod("creditCardStartingWith345", function(cardNumber, element) {
        if  ((!cardNumber.startsWith("4") || !cardNumber.startsWith("5") || !cardNumber.startsWith("3")) && ($("#cardClass").val() != "1" || $("#cardClass").val() != "3"))
            return false;
        else
            return true;
    }, "Tarjeta Invalida");
    }
