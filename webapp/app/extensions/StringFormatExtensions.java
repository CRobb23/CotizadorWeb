package extensions;
 
import play.Logger;
import play.templates.JavaExtensions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
 
public class StringFormatExtensions extends JavaExtensions {
 
	public static String decimalFormat(String string) {

        try {
            BigDecimal number = new BigDecimal(string);
            return decimalFormat(number);
        } catch (Exception e) {
            Logger.error(e, "Invalid number: %s", string);
        }

		return string;
	}
	
	public static String decimalFormat(Object number) {
		  DecimalFormat formatter = new DecimalFormat("#,###.00");
		  formatter.setMinimumIntegerDigits(1);
		  try {
			  return formatter.format(number);
		  } catch (Exception e) {
			  Logger.error(e, "Error formatting decimal");
		  }
		  
		  if (number==null) {
			  return "";
		  }
		  
		  return number.toString();
	}
	
	public static String decimalFormat(Double number) {
		  DecimalFormat formatter = new DecimalFormat("#,###.00");
		  formatter.setMinimumIntegerDigits(1);
		  try {
			  return formatter.format(number);
		  } catch (Exception e) {
			  Logger.error(e, "Error formatting decimal");
		  }
		  
		  if (number==null) {
			  return "";
		  }
		  
		  return number.toString();
	}
	
	public static String numberFormat(Double number) {
		  DecimalFormat formatter = new DecimalFormat("#,###.##");
		  formatter.setMinimumIntegerDigits(1);
		  try {
			  return formatter.format(number);
		  } catch (Exception e) {
			  Logger.error(e, "Error formatting number");
		  }
		  
		  if (number==null) {
			  return "";
		  }
		  
		  return number.toString();
	}
	
	public static String dateFormat(String string, String currentFormatString, String newFormatString) {
		try {
			SimpleDateFormat currentFormat = new SimpleDateFormat(currentFormatString);
			SimpleDateFormat newFormat = new SimpleDateFormat(newFormatString);
			
			Date currentDate = currentFormat.parse(string);
			return newFormat.format(currentDate);
		} catch (Exception e) {
			Logger.error(e, "Error formatting date");
		}
		
		return string;
	}
	
	public static String maskCreditCard(String string, Integer firstCharactersToShow, Integer lastCharactersToShow) {
		
		if (string.length() > firstCharactersToShow + lastCharactersToShow) {
			String prefix = string.substring(0, firstCharactersToShow);
			String encoded = string.substring(firstCharactersToShow, string.length()-lastCharactersToShow).replaceAll("(?s).", "X");			
			String suffix = string.substring(string.length()-lastCharactersToShow, string.length());
			
			return prefix + encoded + suffix;
		}
		
		return string;
	}
	
	public static Double toDouble(String string) {
		try {
			return Double.parseDouble(string);
		} catch (Exception e) {
			Logger.error(e, "Error coverting to double");
		}
		
		return null;
	}

    public static String formatDeductibleIncrease(Number number) {

        if (number.intValue() == 100) {
            return "DOBLE";
        }

        if (number.intValue() == 200) {
            return "TRIPLE";
        }

        if (number.intValue() == 300) {
            return "CUADRUPLE";
        }

        if (number.intValue() == 400) {
            return "QUINTUPLE";
        }

        if (number.intValue() == 500) {
            return "SEXTUPLE";
        }

        if (number.intValue() == 600) {
            return "SEPTUPLE";
        }

        if (number.intValue() == 700) {
            return "OCTUPLE";
        }

        if (number.intValue() == 800) {
            return "NONUPLO";
        }

        if (number.intValue() == 900) {
            return "DECUPLO";
        }

        return "";
    }
}