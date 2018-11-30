package validators;

import play.Logger;

public class NitValidator {
	 
	private static boolean validateNit(String nit) {
		
		if (nit!=null && nit.length()==0) {
			return true;
		}
		
		int length = nit.length();
		String verifier = nit.substring(nit.length() - 1, nit.length());
		int sum = 0;
		int value = 0;

		int posCurrent = 0;
		try {
			for (int x = length; x > 1; x--) {
				value = Integer.parseInt(nit.substring(posCurrent, posCurrent + 1));
				sum = sum + (value * x);
				posCurrent++;
			}
		} catch (Exception e) {
			Logger.info(e, "Invalid nit");
			return false;
		}

		int xMOd11 = 0;
		xMOd11 = (11 - (sum % 11)) % 11;
		String s = String.valueOf(xMOd11);
		if ((xMOd11 == 10 & ("k").equalsIgnoreCase(verifier)) || (s.equals(verifier))) {
			return true;
		}
		
		return false;
	}
	
    public static boolean isSatisfied(Object nit) {
	    if (nit == null) {
	        return false;
        }
    	return validateNit((String)nit);
    }
}
