package base;

/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.JSONManager;
import utils.PropertiesReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TokenHelper {
    RequestSpecBuilder builder = new RequestSpecBuilder();
    String keystore = "src/test/resources/ssl/keystore_latest.jks"; // truststore in jks format
    String baseUrl ;
    JSONObject json;
    static RestAssuredConfig sslConfig;

    // RestAssured.useRelaxedHTTPSValidation();
    SSLConfig config = null;
    String password = "AAA";
     static String staffToken;
    static String fakeStaffToken;
    static String azureIdToken;

    static String fakeUserJWT;
    static String fakeSystemToken;
    static String userJWT;

    //@BeforeTest
    public static String  getAzureIdToken() {
        RestAssured.proxy("localhost", 3128);
        RestAssured.useRelaxedHTTPSValidation();
        String tokenUrl="";
        RestAssuredConfig  sslConfig = RestAssuredConfig.config().sslConfig(
                SSLConfig.sslConfig()
                        .keyStore("src/test/resources/ssl/keystore.p12", "BBB")
                        .keystoreType("PKCS12")
                        .relaxedHTTPSValidation()
        );
        String idTokenData="";
        try{
            JSONTokener tokener = new JSONTokener(new FileInputStream("src/test/resources/tokenSettings/azureIDTokenSettings.json"));
            JSONObject json = new JSONObject(tokener);
            idTokenData="client_id="+json.get("client_id")+"&client_secret="+json.get("client_secret")+"&refresh_token="+json.get("refresh_token")+"&grant_type="+json.get("grant_type");

        } catch(FileNotFoundException ex){
            ex.printStackTrace();

        }


        RestAssured.baseURI = tokenUrl;
        //RestAssured.config(sslConfig);
         String responseBody =  RestAssured.given().config(sslConfig).
                given().
                   header("Content-Type", "application/x-www-form-urlencoded").
                   body(idTokenData).
                when().
                    post(tokenUrl)
              .andReturn()
              .body().jsonPath().get("id_token");
         azureIdToken=responseBody;
         return azureIdToken;
    }

    public static  void requestStaffToken(String ocvId) {
        String baseUrl="/DDD/api/v1/token";
        baseUrl= PropertiesReader.getBaseURL("dev",true)+baseUrl;
        if(azureIdToken==null || azureIdToken.isEmpty()){
            getAzureIdToken();
        }
        String bodyData="scope=aegis-introspect&identity_system_client_id=salesforce&requested_token_typ=staffJWT&ocvId="+ocvId+"&personaType=retail"; // 
        
        String responseBody =  RestAssured.given().config(sslConfig).
                given().
                header("Content-Type", "application/x-www-form-urlencoded").
                header("Authorization","Bearer "+azureIdToken).
                body(bodyData).
                when().
                post(baseUrl)
                .andReturn()
                .body().jsonPath().get("access_token");
        staffToken=responseBody;

    }

    public static String getStaffToken(String ocvId){
        if(staffToken==null || staffToken.isEmpty()){
            requestStaffToken(ocvId);
        }
     return  staffToken;
    };
    public static String getFakeStaffToken(String ocvId){
        if(fakeStaffToken==null || fakeStaffToken.isEmpty()){
            requestFakeStaffToken(ocvId);
        }
        return  fakeStaffToken;
    };

    public static String getFakeUserJWT(String ocvId){
        if(fakeUserJWT==null || fakeUserJWT.isEmpty()){
            requestFakeUserJWT(ocvId);
        }
        return  fakeUserJWT;
    };

    public static String getFakeSystemToken(String forgeClientId){
        if(fakeSystemToken==null || fakeSystemToken.isEmpty()){
            requestFakeSystemJWT(forgeClientId);
        }
        return  fakeSystemToken;
    }

    private   static void requestFakeStaffToken(String ocvId) {
        String fakeTokenData="";
        try{
            JSONTokener tokener = new JSONTokener(new FileInputStream("src/test/resources/tokenSettings/fakeStaffTokenSettings.json"));
            JSONObject json = new JSONObject(tokener);
         //   JSON ob =new JSONObject();
             fakeTokenData =json.toString();
            fakeTokenData=fakeTokenData.replace("<ocvID>",ocvId);
        } catch(FileNotFoundException ex){
            ex.printStackTrace();

        }

        String tokenUrl="";


      
       
        String responseBody =  RestAssured.given().config(sslConfig).
                given().
                header("Content-Type", "application/json").
                header("Authorization","Bearer abc" ).
                body(fakeTokenData).
                when().
                post(tokenUrl)
                .andReturn()
                .body().jsonPath().get("access_token");
          fakeStaffToken=responseBody;

    }





    private   static void requestFakeUserJWT(String ocvId) {
        String fakeTokenData="";
        try{
            JSONTokener tokener = new JSONTokener(new FileInputStream("src/test/resources/tokenSettings/fakeUserJWTSettings.json"));
            JSONObject json = new JSONObject(tokener);
            //   JSON ob =new JSONObject();
            fakeTokenData =json.toString();
            fakeTokenData=fakeTokenData.replace("<ocvID>",ocvId);

        } catch(FileNotFoundException ex){
            ex.printStackTrace();

        }

        String tokenUrl="https://joker.identity-services-sit-fr.apps.x.gcpnp.anz/ig-int/jwt";


        RestAssuredConfig  sslConfig = RestAssuredConfig.config().sslConfig(
                SSLConfig.sslConfig()
                        .keyStore("src/test/resources/ssl/keystore.p12", "")
                        .keystoreType("PKCS12")
                        .relaxedHTTPSValidation()
        );
       ;
        String responseBody =  RestAssured.given().config(sslConfig).
                given().
                header("Content-Type", "application/json").
                header("Authorization","Bearer abc" ).
                body(fakeTokenData).
                when().
                post(tokenUrl)
                .andReturn()
                .body().jsonPath().get("access_token");
         fakeUserJWT=responseBody;
    }


    private   static void requestFakeSystemJWT(String forgeClientId) {
        String fakeTokenData="";
        try{
            JSONTokener tokener = new JSONTokener(new FileInputStream("src/test/resources/tokenSettings/fakeSystemTokenSettings.json"));
            JSONObject json = new JSONObject(tokener);
            //   JSON ob =new JSONObject();
            fakeTokenData =json.toString();
            fakeTokenData=fakeTokenData.replace("<forgeClientId>",forgeClientId);

        } catch(FileNotFoundException ex){
            ex.printStackTrace();

        }

        String tokenUrl="";


        //baseUrl= PropertiesReader.getBaseURL("dev")+baseUrl;

        String responseBody =  RestAssured.given().config(sslConfig).
                given().
                header("Content-Type", "application/json").
                header("Authorization","Bearer abc" ).
                body(fakeTokenData).
                when().
                post(tokenUrl)
                .andReturn()
                .body().jsonPath().get("access_token");
         fakeSystemToken=responseBody.trim();
    }

    public static void main(String[] args) {
        TokenHelper.getFakeUserJWT("");
        TokenHelper.getFakeStaffToken("");
        TokenHelper.getFakeSystemToken(");
    }

}

