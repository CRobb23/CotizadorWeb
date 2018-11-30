function init(args){
	
	initValidator();
	$("#back").click(validatorDestroy);

	$("#brand_line").change(function(){
		alert("entra");
    	var selectelement = $(this);
    	selectelement.prop("disabled", true);
    	$("#line").html("<option value=''>" + args.formSelectLoading + "</option>");
		
    	var id = $(this).val(); 
    	$.ajax({
			type: 	"POST",
			url: 	args.urlGetLinesForBrand, 
			data:  	{id: id},
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
	
	$("#vehicle_isNew").click(function(){
		if($(this).prop("checked")){
			$("#inspectionRow").hide();
		}else{
			$("#inspectionRow").show();
		}
	});
	
	if($("#vehicle_isNew").prop("checked")){
		$("#inspectionRow").hide();
	}else{
		$("#inspectionRow").show();
	}
	
	$("#erYear").change(function(){
		let brand = $("#brand_line option:selected").text().trim();
		let line = $("#line option:selected").text().trim();
		let year = $("#erYear option:selected").text().trim();
		
		$.ajax({
			url:"/incident/getByInfo",
			type:"post",
			data:{information:brand+"|"+line+"|"+year},
			complete:function(result){
				console.log(result.responseJSON);
				if(result.responseJSON.success){
					$("#averageValueAmount").html(result.responseJSON.averageValue);
					$("#averageValue").val(result.responseJSON.averageValue);
				}else{
					if(brand!="Seleccionar"&&line!="Seleccionar"&&year!="Seleccionar"){
						$("#averageValueAmount").html("No existe valor promedio para este vehículo.");
					}
				}
			}
		});
		
	});
	
	getAverageValue();
}

function getAverageValue(){
	let brand = $("#brand_line").val();
	let line = $("#line").val();
	let year = $("#erYear").val();
	
	if(brand != "" && line != "" && year != ""){
		$("#erYear").trigger("change");
	}
}

var validator;
function validatorDestroy(){
	validator.destroy();
}
function initValidator(){
	validator = $("#formVehicle").validate({
		rules: {
			"vehicle.chassis": {
	        	required: true
	      	},
	      	"vehicle.engine": {
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
            "brand_line2": {
                required: true
            },
            "line2": {
                required: true
            },
            "erYear2": {
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
	        	required: true
	      	},
	      	"vehicle.loanNumber": {
	        	required: true
	      	},
	      	"vehicle.warranty": {
	        	required: true
	      	},
	      	"vehicle.beneficiaries.id": {
	        	required: true
	      	},
	      	"vehicle.inspectionNumber": {
	        	required: true
	      	},
	      	"vehicle.isNew": {
	        	required: true
	      	},
	      	"vehicle.alreadyInsured": {
	        	required: true
	      	},
            "task.description": {
                required: true
            },
	      	"inspection.type": {
	      		required : {
	      			depends : function(){
	      				return !$("#vehicle_isNew").val();
	      			}
	      		}
	      	},
	      	"inspection.inspectionAddress": {
	      		required : {
	      			depends : function(){
	      				return $("input[name='inspection.type']:checked").val()==1;
	      			}
	      		}
	      	},
	      	"inspection.appointmentDate": {
	      		required : {
	      			depends : function(){
	      				return $("input[name='inspection.type']:checked").val()==1;
	      			}
	      		}
	      	},
	      	"inspection.inspectionNumber": {
	      		required : {
	      			depends : function(){
	      				return $("input[name='inspection.type']:checked").val()==3;
	      			}
	      		}
	      	}
	    },
	    
	    messages: {
	    	"vehicle.taxNumber": 			"Requerido",
	    	"vehicle.chassis": 				"Requerido",
	      	"vehicle.engine": 				"Requerido",
			"vehicle.plate": {
	      		required: 					"Requerido",
	      		validatePlate: 				"Formato de placa inválido. Ingrese en mayusculas y sin vocales."
	      	},
            "brand_line2":		 			"Requerido",
            "line2":		 				"Requerido",
            "erYear2":		 				"Requerido",
	      	"vehicle.brand.id": 			"Requerido",
	      	"vehicle.line.id": 				"Requerido",
	      	"vehicle.type.id": 				"Requerido",
	      	"vehicle.rate.id": 				"Requerido",
	      	"vehicle.erYear.id": 			"Requerido",
	      	"vehicle.armor": 				"Requerido",
	      	"vehicle.alarmCode": 			"Requerido",
	      	"vehicle.numberOfPassengers": 	"Requerido",
	      	"vehicle.color": 				"Requerido",
	      	"vehicle.mileage": 				"Requerido",
	      	"vehicle.loanNumber": 			"Requerido",
	      	"vehicle.warranty": 			"Requerido",
	      	"vehicle.beneficiaries.id": 		"Requerido",
	      	"vehicle.inspectionNumber": 	"Requerido",
	      	"vehicle.isNew":				"Requerido",
	      	"vehicle.alreadyInsured":		"Requerido",
	      	"inspection.type":				"Requerido",
	      	"inspection.inspectionAddress":	"Requerido",
	      	"inspection.appointmentDate":	"Requerido",
	      	"inspection.inspectionNumber":	"Requerido",
            "task.description": "Requerido"
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
	
	$.validator.addMethod("validatePlate", function(value, element){
		let regexpr = /[0-9]{3}[B-DF-HJ-NP-TV-Z]{3}/;
		return regexpr.test(value);
	}, "Formato de placa inválido.");
}
