 

import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;
import utils.PropertiesReader;
import base.BaseTest;
import org.testng.log4testng.Logger;
import static org.hamcrest.Matchers.equalTo;


public class TestGetAllActivePackages extends BaseTest  {

	private static final Logger logger= Logger.getLogger(TestGetAllActivePackages.class);

	@BeforeClass
	public  void setUp() {
		super.setUp();
	}

	public static ResponseSpecification PT_Test_Get_All_Active_Packages_Endpoint_Response_200_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(200)
				.expectResponseTime(Matchers.lessThan(3000l))
				.expectBody("dataType", equalTo("a(AAA)nz.eventresults.GetAllPackageDetailsResult"))
				.expectBody("dataCount", equalTo(1))
				.build();
	 }
	@Test(enabled=true)
	public void PT_Test_Get_All_Active_Packages_Endpoint_Response_200() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id",getClientId())
				.header("client_secret",getClientSecret())
			.when()
				.get(endPoint)
			.then()
				.spec(PT_Test_Get_All_Active_Packages_Endpoint_Response_200_spec())
				.extract().response().asString();

		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public static ResponseSpecification PT_Test_Get_All_Active_Packages_Endpoint_Response_204_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(204)
				.expectResponseTime(Matchers.lessThan(3000l))
				.build();
	 }
	@Test(enabled=true)
	public void PT_Test_Get_All_Active_Packages_Endpoint_Response_204() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active/{productCode}";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id",getClientId())
				.header("client_secret",getClientSecret())
				.pathParam("productCode", "PKG_TEST_NO_FEE")
			.when()
				.get(endPoint)
			.then()
				.spec(PT_Test_Get_All_Active_Packages_Endpoint_Response_204_spec())
				.extract().response().asString();

		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public static ResponseSpecification PT_Test_Get_All_Active_Packages_Endpoint_Response_401_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(401)
				.expectResponseTime(Matchers.lessThan(3000l))
				.build();
	 }
	@Test(enabled=true)
	public void PT_Test_Get_All_Active_Packages_Endpoint_Response_401() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active";
		String SWAGGER_JSON_URL ="src/test/resources/openAPI/(AAA)-cpb-product-catalog-api.yaml";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id", "aa")
				.header("client_secret", "")
				.queryParam("include", "standardRates")
			.when()
				.get(endPoint)
			.then()
				.spec(PT_Test_Get_All_Active_Packages_Endpoint_Response_401_spec())
				.extract().response().asString();

			   Assert.assertTrue(utils.JsonSchemaValidator.validateSchemaFromAPISpec(SWAGGER_JSON_URL,responseBody,"/packages/active","GET","401","null")) ;
;
		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public static ResponseSpecification NT_Test_Get_All_Active_Packages_Endpoint_Response_405_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(405)
				.expectResponseTime(Matchers.lessThan(3000l))
				.build();
	 }
	@Test(enabled=true)
	public void NT_Test_Get_All_Active_Packages_Endpoint_Response_405() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id",getClientId())
				.header("client_secret",getClientSecret())
				.queryParam("include", "multiUseProducts")
			.when()
				.post(endPoint)
			.then()
				.spec(NT_Test_Get_All_Active_Packages_Endpoint_Response_405_spec())
				.extract().response().asString();

		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public static ResponseSpecification NT_Test_Get_All_Active_Packages_Endpoint_Response_406_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(406)
				.expectResponseTime(Matchers.lessThan(3000l))
				.build();
	 }
	@Test(enabled=true)
	public void NT_Test_Get_All_Active_Packages_Endpoint_Response_406() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id",getClientId())
				.header("client_secret",getClientSecret())
				.header("Accept", "application/xml")
			.when()
				.get(endPoint)
			.then()
				.spec(NT_Test_Get_All_Active_Packages_Endpoint_Response_406_spec())
				.extract().response().asString();

		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public static ResponseSpecification NG_Test_Get_All_Active_Packages_Endpoint_Response_400_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(400)
				.expectResponseTime(Matchers.lessThan(3000l))
				.build();
	 }
	@Test(enabled=true)
	public void NG_Test_Get_All_Active_Packages_Endpoint_Response_400() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active";
		String SWAGGER_JSON_URL ="src/test/resources/openAPI/(AAA)-cpb-product-catalog-api.ya(AAA)l";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id",getClientId())
				.header("client_secret",getClientSecret())
				.queryParam("maxDepth", "15")
			.when()
				.get(endPoint)
			.then()
				.spec(NG_Test_Get_All_Active_Packages_Endpoint_Response_400_spec())
				.extract().response().asString();

				//Assert.assertTrue(utils.JsonSchemaValidator.validateSchemaFromAPISpec(SWAGGER_JSON_URL,responseBody,"/packages/active","GET","400","application/json")) ;
;
		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

	public static ResponseSpecification NT_Test_Get_All_Active_Packages_Endpoint_Response_415_spec() {
	return new ResponseSpecBuilder()
				.expectStatusCode(415)
				.expectResponseTime(Matchers.lessThan(3000l))
				.build();
	 }
	@Test(enabled=true)
	public void NT_Test_Get_All_Active_Packages_Endpoint_Response_415() {
		String testResultId = "null";

		String endPoint=PropertiesReader.getBaseURL(testEnv,true)+"/product-catalog/v2/packages/active";
		try {
			 String responseBody = RestAssured
			.given().config(sslConfig)
				.header("client_id",getClientId())
				.header("client_secret",getClientSecret())
				.header("Content-Type", "application")
			.when()
				.get(endPoint)
			.then()
				.spec(NT_Test_Get_All_Active_Packages_Endpoint_Response_415_spec())
				.extract().response().asString();

		} catch (RuntimeException ex) {
			System.err.println(ex.getMessage());
			logger.error(ex.getMessage());
		}
	}

}
