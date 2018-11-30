$(document).ready(function () {
  //called when key is pressed in textbox
  $(".nit").keypress(function (e) {	
  	
  	//Allow k, K and enter
  	if (e.which == 107 || e.which == 75 || e.which==13) {
  		return true;
  	}
  	
     //if the letter is not digit then display error and don't type anything
     if (e.which != 8 && e.which != 0 && (e.which < 48 || e.which > 57)) {
     	return false;
    }
   });
});