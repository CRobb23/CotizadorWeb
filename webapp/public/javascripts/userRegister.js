function init(args){
	initValidator(args);
}

function initValidator(args){
	var validator = $("#formUser").validate({
		rules: {
			"user.email": {
	        	required: true,
	        	email: true,
	        	maxlength: 35,
	        	remote: {
	        		url: args.urlMailExists,
	                type: "POST",
	                dataType: "json",
	                data: { 
	                	"email": function(){ return jQuery("#user_email").val(); }
	                }
	        	}
	      	},
	      	"user.password": {
	        	required: true,
	        	maxlength: 30
	      	},
			"user.confirmPassword": {
	        	required: true,
	        	equalTo: "#user_password",
	        	maxlength: 30
	      	},
	      	"user.firstName": {
	        	required: true,
	        	maxlength: 35
	      	},
	      	"user.lastName": {
	        	required: true
	      	}
	    },
	    
	    messages: {
	    	"user.email": {
	    		required: "Requerido",
	    		email: "Correo Electrónico Inválido",
	    		remote: "Correo Electrónico ya registrado."
	    	},
	      	"user.password": "Requerido",
	      	"user.confirmPassword":  {
	    		required: "Requerido",
	    		equalTo: "Contraseñas no coinciden."
	    	},
	    	"user.firstName": "Requerido",
    		"user.lastName": "Requerido"
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

