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
import org.json.JSONObject;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import utils.PropertiesReader;

import org.testng.log4testng.Logger;

public class BaseTest {
    RequestSpecBuilder builder = new RequestSpecBuilder();
    private static final Logger LOGGER = Logger.getLogger(BaseTest.class);

     String baseUrl;
    JSONObject json;
    public static RestAssuredConfig sslConfig;

    // RestAssured.useRelaxedHTTPSValidation();
    //public SSLConfig config = null;

    static String azureIDToken;
    static String staffToken;
    static String fakeStaffToken;
    static String fakeUserJWT;
    static String fakeSystemToken;
    //static String azureIdToken;
    public static String testEnv=System.getProperty("test.env");

    public  static String clientId;
    public static String clientSecret;
    public static String getClientId(){
        clientId=PropertiesReader.getClientId(testEnv);
        return clientId;

    }
    public static String getClientSecret(){
        clientSecret=PropertiesReader.getClientSecret(testEnv);
        return clientSecret;

    }

    public static String getAzureIDToken(){
        if(azureIDToken==null || azureIDToken.isEmpty()){
            azureIDToken=TokenHelper.getAzureIdToken();
        }
        return  azureIDToken;
    };

    public static String getStaffToken(String ocvId){
        if(staffToken==null || staffToken.isEmpty()){
            staffToken=TokenHelper.getStaffToken(ocvId);
        }
        return  staffToken;
    };

    public static String getFakeStaffToken(String ocvId){
        if(fakeStaffToken==null || fakeStaffToken.isEmpty()){
            fakeStaffToken=TokenHelper.getFakeStaffToken(ocvId);
        }
        return  fakeStaffToken;
    };
    public static String getFakeUserJWT(String ocvId){
        if(fakeUserJWT==null || fakeUserJWT.isEmpty()){
            fakeUserJWT=TokenHelper.getFakeUserJWT(ocvId);
        }
        return  fakeUserJWT;
    };

    public static String getFakeSystemToken(String forgeClientId){
        if(fakeSystemToken==null || fakeSystemToken.isEmpty()){
            fakeSystemToken=TokenHelper.getFakeSystemToken(forgeClientId);
        }
        return  fakeSystemToken;
    };
    //@BeforeTest
    @BeforeSuite
    public void setUp() {
       // getStaffToken(ocvId);
        LOGGER.info("before test suite....");
        // RestAssured.port = port;
        RestAssured.proxy("localhost", 3128);
        RestAssured.useRelaxedHTTPSValidation();
        // RestAssured.useRelaxedHTTPSValidation();
       // SSLConfig config = null;
        String password = "";

        sslConfig = RestAssuredConfig.config().sslConfig(
                SSLConfig.sslConfig()
                       .keyStore("src/test/resources/ssl/keystore.p12", "")
                        .keystoreType("PKCS12")
                        .allowAllHostnames()
                        .relaxedHTTPSValidation("TLSv1.2")
        );
        System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");

    }
    @AfterSuite
    public void TearDown(){

        LOGGER.info("After suite finished.....");
         RestAssured.reset();
    }
}