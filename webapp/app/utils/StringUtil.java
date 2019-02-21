package utils;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {


    public static boolean isNullOrBlank(Object value) {
        if (value instanceof String)
            return value == null || ((String)value).trim().isEmpty() || "NULL".equalsIgnoreCase((String) value);
        return value == null;
    }

    public static boolean isAllDigits(String str) {
        boolean isAllDigits = true;
        for (Character chars : str.toCharArray()) {
            if (!Character.isDigit(chars)) {
                isAllDigits = false;
                break;
            }
        }
        return isAllDigits;
    }

    public static String prepareXmlResponse(String response) {
        return escapeXmlResponse(response);
    }

    public static String prepareXmlRequest(String request) {
        return escapeXml(request);
    }

    /**
     * Escape and replace special Characters in XML String
     * @param str
     * @return
     */
    public static String escapeXml(String str) {
        // Espace characters
        str = str.replaceAll("&(?!amp;)","&amp;").replaceAll("\\'", "&apos;").replaceAll("\\\"", "&quot;").replaceAll("\\~", "");
        str = str.replaceAll("Á","+01").replaceAll("á","+02").replaceAll("É","+03").replaceAll("é","+04").replaceAll("Í","+05");
        str = str.replaceAll("í","+06").replaceAll("Ó","+07").replaceAll("ó","+08").replaceAll("Ú","+09").replaceAll("ú","+10");
        str = str.replaceAll("Ñ","+11").replaceAll("ñ","+12").replaceAll("\\p{Pd}","+13").replaceAll("Ü","+14").replaceAll("ü","+15");
        // Remove special characters
        // str = StringUtils.stripAccents(str);–––––––––––
        return str;
    }
    public static String escapeXmlResponse(String str) {
        // Espace characters
        str = str.replaceAll("&(?!amp;)","&amp;").replaceAll("\\'", "&apos;").replaceAll("\\\"", "&quot;").replaceAll("\\~", "");
        str = str.replace("+01","Á").replace("+02","á").replace("+03","É").replace("+04","é").replace("+05","Í");
        str = str.replace("+06","í").replace("+07","Ó").replace("+08","ó").replace("+09","Ú").replace("+10","ú");
        str = str.replace("+11","Ñ").replace("+12","ñ").replace("+14","Ü").replace("+15","ü");
        // Remove special characters
        // str = StringUtils.stripAccents(str);\\p{Pd}
        return str;
    }

    public static String correctString(String str) {
        return str != null && str != "N/A" ? str : "";
    }

    public static String generateBlankString(int length) {
        if (length < 0) return "";
        @SuppressWarnings("StringBufferMayBeStringBuilder") StringBuilder outputBuffer = new StringBuilder(length);
        for (int i = 0; i < length; i++){
            outputBuffer.append(" ");
        }
        return outputBuffer.toString();
    }
}
