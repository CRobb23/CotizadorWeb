package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import helpers.ERConstants;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.Logger;
import play.data.validation.*;

import javax.persistence.*;

import play.db.jpa.Model;
import play.libs.Crypto;

@Entity
public class ER_Incident_Parameter extends Model {

	//@Required
	@MaxSize(100)
	@Column(length = 100)
	@CheckWith(IncidentParameterCheck.class)
	public String value;

	@Required
	@ManyToOne
	public ER_Parameter parameter;

	@Required
	@ManyToOne
	public ER_Incident incident;
	
	static boolean arrayContainsValue(String array, Object value) {
		if (value==null || array==null) {
			return false;
		}
		
		try {
			String val = String.valueOf(value);
			if (val.isEmpty()) {
				return false;
			}
			String[] components = array.split(", ");
			for (String component : components) {
				if (val.equals(component)) {
					return true;
				}
			}
		} catch (Exception e) {
			Logger.info(e, "Value is not valid");
		}
		
		return false;
	}
	
	public boolean containsValue(Object value) {
		if (value==null || this.value==null) {
			return false;
		}
		
		try {
			String val = String.valueOf(value);
			if (val.isEmpty()) {
				return false;
			}
			String[] components = this.value.split(", ");
			for (String component : components) {
				if (val.equals(component)) {
					return true;
				}
			}
		} catch (Exception e) {
			Logger.info(e, "Value is not valid");
		}
		
		return false;
	}

	public String decryptedValue() {
	    if (this.value != null) {
            try {
                return new Crypto().decryptAES(this.value);
            } catch (Exception e) {
                Logger.info(e, "Could not decrypt value: %s", this.value);
            }
        }
		return null;
	}
	
	public Object getValueString() {
		
		if (parameter.type.code == ERConstants.PARAMETER_TYPE_OPTIONS) {
			try {
				String valueString = "";
				String[] components = this.value.split(",");
				for (int i=0; i<components.length;i++) {
					Long optionId = Long.parseLong(components[i]);
					ER_Parameter_Option option = ER_Parameter_Option.findById(optionId);
					if (valueString.length()>0) {
						valueString+=",";
					}
					
					valueString += option.value;
				}
				
				return valueString;
			} catch (Exception e) {
				Logger.info(e, "Could not get option values");
			}
		}
		
		if (parameter.type.code == ERConstants.PARAMETER_TYPE_ACCOUNT) {
			return decryptedValue();
		}
		
		return this.value;
	}
	
	static class IncidentParameterCheck extends Check {

		public boolean isSatisfied(Object incidentParameter, Object value) {
			ER_Incident_Parameter incidentParameterConverted = (ER_Incident_Parameter)incidentParameter;
			if(incidentParameterConverted != null)
			{		
				ER_Parameter parameter = ER_Parameter.findById(incidentParameterConverted.parameter.id);
				
				if (parameter.required!=null && parameter.required) {
					if (value==null || String.valueOf(value).isEmpty()) {
						setMessage("validation.required");
						return false;
					}
				}
				
				setMessage("validation.invalid");
				
				switch(parameter.type.code)
				{
				case ERConstants.PARAMETER_TYPE_ALPHANUMERIC:
					return isString(value);
				case ERConstants.PARAMETER_TYPE_BOOLEAN:
					return isBoolean(value);
				case ERConstants.PARAMETER_TYPE_DATE:
					return isDateValid(value);
				case ERConstants.PARAMETER_TYPE_NUMERIC:
					return isNumber(value);
				case ERConstants.PARAMETER_TYPE_ACCOUNT:
					return isString(value);
				case ERConstants.PARAMETER_TYPE_OPTIONS:
					return isString(value);
				default:
					return false;
				}
			}
			else
				return false;
		}

		public boolean isString(Object value) {
			if (value == null)
				return false;
			else {
				try {
					String.valueOf(value);
					return true;
				} catch (Exception e) {
					Logger.info(e, "Invalid string");
					return false;
				}
			}
		}

		public boolean isBoolean(Object value) {
			if (value == null) {
				return false;
			}
			
			try {
				String boolStr = String.valueOf(value);
				
				String[] components = boolStr.split(",");
				if (components[0].equals("on") || components[0].equals("off")) {
					return true;
				}
				
				boolean result = Boolean.parseBoolean(boolStr);
				if(result == false)
				{
					int intVal = Integer.parseInt(boolStr);
					if(intVal == 0 || intVal == 1)
						result = true;
				}
				return result;
			} catch (Exception e) {
				Logger.info(e, "Invalid boolean");
				return false;
			}
		}

		public boolean isNumber(Object value) {
			
			String stringValue = value.toString();
			if (value==null || stringValue.isEmpty()) {
				return true;
			}
			
			try {
				Double.parseDouble(String.valueOf(value));
				return true;
			} catch (NumberFormatException nfe) {
				Logger.info(nfe, "Invalid number: %s", value);
				return false;
			}
		}

		public boolean isDateValid(Object value){

			if(value == null)
				return false;

			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			sdf.setLenient(false);

			try {
				
				String dateToValidate = String.valueOf(value);
				
				if(dateToValidate == null){
					return false;
				}

				//if not valid, it will throw ParseException
				sdf.parse(dateToValidate);

			} catch (ParseException e) {
				Logger.info(e, "Invalid date");
				return false;
			}

			return true;
		}
	}

}
