package utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {
    private static final String PROPERTIES_FILE_LOCATION = System.getProperty("os.name")
            .toLowerCase().contains("windows") ? "src\\main\\resources\\" : "src/main/resources/";
   private static final String configFolder="src/test/resources/configuration/";
   public static String CLIENT_ID="";
   public static String CLIENT_SECRET="";

    public static String getProperty(String fileName, String propertyName) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE_LOCATION + fileName)) {
            prop.load(fis);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return prop.getProperty(propertyName);
    }

    public static  String getSystemProperty(String propertyName){
        return getProperty("env.properties",propertyName);
    }


    public static String getBaseURL(String env,boolean isInternal) {

        //default value for internal dlb: mTLS false
        //default value for external dlb: mTLS true
        return getBaseURL(env,isInternal,!isInternal);
    }


        public static String getBaseURL(String env,boolean isInternal,boolean isJWTNeeded) {
        try {
            if (env == null || env.isBlank() || env.isEmpty()) {
                env = getSystemProperty("test.env");//"dev"; //default env is dev
            }
            FileInputStream in = new FileInputStream(configFolder + env + "_env.json");
            // Read the JSON file and parse it
            // = new FileInputStream("src/test/resources/testData/dev_env.json");
            JSONTokener tokener = new JSONTokener(in);
            JSONObject json = new JSONObject(tokener);
            String baseUrl = "";
            if(isInternal && !isJWTNeeded) {
                baseUrl= json.get("domain-no-mTLS").toString();
            } else if(isInternal && isJWTNeeded) {
                baseUrl= json.get("domain-with-mTLS").toString();
            } else if(!isInternal && isJWTNeeded) {
                baseUrl= json.get("ext-domain-with-mTLS").toString();
            } else if(!isInternal && !isJWTNeeded) {
                baseUrl= json.get("ext-domain-no-mTLS").toString();
            } else {
                System.out.println("wrong definitions with environments,please check files under env folders!");
            }
            System.out.println("The baseUrl used for API testing is: "+baseUrl);
            return baseUrl;
       }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return "https://dev.int.integration.anzx.anz.com";
        }

    }

   //get client id from env files
    public static String getClientId(String env) {
        try {
            if (env == null || env.isBlank() || env.isEmpty()) {
                env = "dev";
            }
            FileInputStream in = new FileInputStream(configFolder + env + "_env.json");
            // Read the JSON file and parse it
            // = new FileInputStream("src/test/resources/testData/dev_env.json");
            JSONTokener tokener = new JSONTokener(in);
            JSONObject json = new JSONObject(tokener);
           // String clientIdSecret = json.get("client_id").toString();
            String client_id_key_in_GCP = json.get("client_id_in_GCP").toString();
            String clientId=GetGCPSecret.getGCPSecretAsString(getSystemProperty("GCP_PROJECT_NP"),client_id_key_in_GCP,"latest");
            System.out.println("get client id from GSM!" );
            return clientId;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error while loading keystore >>>>>>>>>");
            ex.printStackTrace();
            return "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //get client secret from env files
    public static String getClientSecret(String env) {
        try {
            if (env == null || env.isBlank() || env.isEmpty()) {
                env = "dev";
            }
            FileInputStream in = new FileInputStream(configFolder + env + "_env.json");
            // Read the JSON file and parse it
            // = new FileInputStream("src/test/resources/testData/dev_env.json");
            JSONTokener tokener = new JSONTokener(in);
            JSONObject json = new JSONObject(tokener);
            String client_secret_key_in_GCP = json.get("client_secret_in_GCP").toString();
            String clientSecret=GetGCPSecret.getGCPSecretAsString(getSystemProperty("GCP_PROJECT_NP"),client_secret_key_in_GCP,"latest");
            return clientSecret;
        }
        catch (FileNotFoundException ex) {
            System.out.println("Error while loading keystore >>>>>>>>>");
            ex.printStackTrace();return "";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

