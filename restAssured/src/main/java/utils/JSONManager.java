package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.log4testng.Logger;

public class JSONManager {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(JSONManager.class);

    public static String convertToStringJSON(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        String strConverted=null;
        try {
            strConverted= objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("Error parsing JSON: {}"+obj);
            logger.error("Exception: ", e);
        }
        return strConverted;
    }

    public static String getStringFromJSON(JsonNode node) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException ex) {
            logger.error("Error parsing JSON: {}"+node);
            logger.error("Exception: ", ex);
        }
        return json;
    }

    public static JSONObject getJsonFromString(String str) {
        try {
           return new JSONObject(str);
          //  return jsonObj;
        } catch(JSONException ex){
            logger.error("Error parsing JSON: {}"+str);
            logger.error("Exception: ", ex);
            return null;
        }

    }


    public static JsonNode getJsonNodeFromString(String str) {
        try {
         ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readTree(str);
        } catch(JSONException | JsonProcessingException ex){
            logger.error("Error parsing JSON: {}"+str);
            logger.error("Exception: ", ex);
            return null;

        }
    }

}
