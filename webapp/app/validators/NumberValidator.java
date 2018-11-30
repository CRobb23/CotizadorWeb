package validators;

import utils.StringUtil;

public class NumberValidator {

    public static boolean isSatisfied(String number) {
    	if (StringUtil.isNullOrBlank(number)) {
    	    return true;
        }
    	return StringUtil.isAllDigits((String) number);
    }
}
