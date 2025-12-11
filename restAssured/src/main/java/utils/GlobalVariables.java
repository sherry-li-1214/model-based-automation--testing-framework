package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.HashMap;
import java.util.Map;



public class GlobalVariables {
    public  static final HashMap<String ,Object > globalVariables=new HashMap<>();

    private static final String configFolder="src/test/resources/configuration/";
    public static JSONArray jsonarray=new JSONArray();
    public static void setGlobalVariables(){

    }
    public static void setup() {
            try {

                FileInputStream in = new FileInputStream(configFolder +  "globals.json");
                JSONTokener tokener = new JSONTokener(in);
                jsonarray = new JSONArray(tokener);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String key = jsonobject.getString("key");
                    String value = jsonobject.getString("value");
                    globalVariables.put(key,value);
                }

            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                //return "";
            }
    }
    public static Object getGlobalVariables(String key){
        if(globalVariables.isEmpty()){
            setup();
        }
        return  globalVariables.get(key);
    }

    public static HashMap<String, Object> getAllGlobalVariables() {
        return globalVariables;
    }

    public static void removeGlobalVariables(String name) {
        globalVariables.remove(name);
    }

    public static void addGlobalVariables(String name, String value) {
        if(globalVariables.isEmpty()){
            setup();
        }

        if(!name.isEmpty() && !value.isEmpty())
          globalVariables.put(name, value);
    }

    //write all current global values to files
    public static void flush()  {

        if(globalVariables.isEmpty()){
            setup();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            for(Map.Entry<String, Object> entry : globalVariables.entrySet()) {
                 boolean isNew = true;
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    String key = jsonobject.getString("key");
                    String value = jsonobject.getString("value");
                    if (key.equals(entry.getKey())) { //if the key is existed , update the value
                        isNew = false;
                        jsonobject.put("value", entry.getValue());
                        break;
                    }
                }
                if(isNew) {
                    //if a new value, add the value to json array
                    JSONObject jsonobject = new JSONObject();
                    jsonobject.put("key", entry.getKey());
                    jsonobject.put("value", entry.getValue());
                    jsonobject.put("type", "any");
                    jsonobject.put("enabled", true);
                    jsonarray.put(jsonobject);
                }
             }
            //write the global values map to global.json
            FileUtils.writeStringToFile(new File(configFolder + "globals.json"), jsonarray.toString(2),"UTF-8");

        } catch(Exception e){
            e.printStackTrace();

    }
}
    public static void clearAll(){
        globalVariables.clear();
    }
}
