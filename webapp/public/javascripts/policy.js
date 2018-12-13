function init(){
    $("#policy-message").dialog({
        resizable: false,
        height: "auto",
        width: 500,
        modal: true,
        buttons: {
            OK: function() {
                $("#Alert-message").dialog({
                    resizable: false,
                    height: "auto",
                    width: 500,
                    modal: true,
                    buttons: {
                        OK: function () {
                            $(this).dialog("close");
                        }
                    }
                }).prev(".ui-dialog-titlebar").css("color", "white");
                $("#Alert-message" ).dialog();
                $( this ).dialog( "close" );
            }
        }
    }).prev(".ui-dialog-titlebar").css("color", "white");
}