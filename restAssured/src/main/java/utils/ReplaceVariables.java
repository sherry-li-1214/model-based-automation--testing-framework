package utils;

import org.testng.log4testng.Logger;

import java.util.HashMap;

import java.util.Map;
import java.util.UUID;


public class ReplaceVariables {

    private static Logger logger = Logger.getLogger(ReplaceVariables.class);
    // processing the variables in definition files

    public static HashMap<String, String> fillReplaceMap() {
        HashMap<String, String> map = new HashMap<String, String>();
        GlobalVariables.setup();
        map.put("\"{{guid}}\"", "\"\"+ReplaceVariables.getMappingValues(\"guid\")");
        map.put("\"{{uuid}}\"", "\"\"+ReplaceVariables.getMappingValues( \"uuid\")");
        map.put("\"{{today}}\"", "\"\"+ReplaceVariables.getMappingValues(\"today\")");
        map.put("\"{{futureDate}}\"","\"\"+ReplaceVariables.getMappingValues(\"futureDate\")");

        //for special case if the value will be put into body as a string,need different ways of replacement
        map.put("{{today}}\\", "\"+ReplaceVariables.getMappingValues(\"today\")+\"\\");
        map.put("{{guid}}\\", "\"+ReplaceVariables.getMappingValues(\"guid\")+\"\\");
        map.put("{{futureDate}}\\", "\"+ReplaceVariables.getMappingValues(\"futureDate\")+\"\\");
        map.put("{{uuid}}\\", "\"+ReplaceVariables.getMappingValues(\"uuid\")+\"\\");


        //put all global variable into the mapping so they could be used for incoming response.
        for(Map.Entry<String, Object> entry : GlobalVariables.getAllGlobalVariables().entrySet()) {
            map.put("\"{{GlobalVariables."+entry.getKey()+"}}\"", "\"\"+GlobalVariables.getGlobalVariables(\""+entry.getKey()+"\")");

            // For replace values in body
            map.put("{{GlobalVariables."+entry.getKey()+"}}\\", "\"+GlobalVariables.getGlobalVariables(\""+entry.getKey()+"\")+\"\\");

        }
        return map;
    }
    public static String replaceTag(String str, Map<String,String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (str.contains(entry.getKey())) {
                str = str.replace(entry.getKey(), entry.getValue());
            }
        }
        return str;
    }

    public static String getMappingValues(String type) {
        String convertedString = ""; //default value if cannot be converted.
        switch (type.toLowerCase()) {
            case "guid":
            case "uuid":
                UUID uuid = UUID.randomUUID();
                convertedString = uuid.toString();
                break;
            case "today":
                long millis = System.currentTimeMillis();
                java.sql.Date todayDate = new java.sql.Date(millis);
                convertedString = todayDate.toString();
                break;
            case "futuredate":
                long futuremillis = System.currentTimeMillis() + 20 * 24 * 60 * 60 * 1000;
                java.sql.Date futureDate = new java.sql.Date(futuremillis);
                convertedString = futureDate.toString();
                break;
            default:
                break;
        }

        return convertedString;
    }
}
