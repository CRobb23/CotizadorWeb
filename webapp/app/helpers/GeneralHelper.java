package helpers;

public class GeneralHelper {

	public static String addDashToNit(String nit) {
		String formatNit = nit;
		if(nit != null && !nit.contains("-")){
			formatNit = nit.substring(0, nit.length()-1);
			formatNit += "-";
			formatNit += nit.substring(nit.length()-1,nit.length());
		}
    	return formatNit;
	}
	
}
