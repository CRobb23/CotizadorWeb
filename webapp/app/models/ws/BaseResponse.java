package models.ws;

public abstract class BaseResponse {

	public abstract String getMessage();
	
	public Boolean isSuccessful(){
		if(getMessage() != null){
			if(getMessage().toLowerCase().indexOf("satisfactorio") > -1){
				return true;
			}
			String[] messages = getMessage().split("-");
			if(messages.length > 0 && messages[0].trim().equals("0000")){
				return true;
			}
		}
		return false;
	}
}
