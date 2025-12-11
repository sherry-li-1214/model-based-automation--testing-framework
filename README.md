# model-based-automation--testing-framework
# *** Automation Testing Framework

- [AAA- Automation Testing Framework](#aaa--automation-testing-framework)
  - [What Can I do with aaa-RestAssured](#what-can-i-do-with-aaa-restassured)
  - [How does it work?](#how-does-it-work)
  - [How to use it for aaa- regression testing](#how-to-use-it-for-aaa--regression-testing)
    - [Step by steps](#step-by-steps)
      - [Create test definition files](#create-test-definition-files)
      - [Special /complex test scenarios](#special-complex-test-scenarios)
      - [Generating test cases](#generating-test-cases)
      - [running test cases](#running-test-cases)
      - [push the new case into API Repo](#push-the-new-case-into-api-repo)
      - [trigger  the test in  API Repo](#trigger--the-test-in--api-repo)
  - [Planned enhancement](#planned-enhancement)
  - [Enhancement of  the new framework from 1/12/2023](#enhancement-of--the-new-framework-from-1122023)

aaa-RestAssured is a framework for automated black-box testing of ***  APIs. It follows a model-based approach, where test cases are automatically derived from the test cases definition  of the API under test. No access to the source code is required, which makes it possible to test APIs written in any programming language, running in local or remote servers.

## What Can I do with aaa-RestAssured

With the new framework, you can automatically create java restAssured class file , execute the test cases and generate allure reports.

## How does it work?

The figure below shows how aaa- RestAssured Test works:

1. **Test model generation**: Framework takes as input the test definition of the API under test, considered the *system model*. The default test model template has been put under src/test/resources/testdefinitions/TestCaseTemplate.json. Engineer need to create the test def files based on own API Requirements.

2. **Test case generation**: Framework provides a utility class(src/main/java/testgenerator/TestCaseGenerator.java) to generate test cases class based on test model above.  The test cases will be put to the api package folder and with all the test cases inside.  

3. **Test case execution**: The test cases are executed by RestAssure framework and a set of reports and stats are generated. Stats are machine-readable, and the test reports can be graphically visualized thanks to [Allure](http://allure.qatools.ru/).

4. **Test Artifacts**: All test artifacts will be generated to /target folders, with html bases reports and logs files.

## How to use it for aaa- regression testing

### Step by steps

1. create test definition files
2. generate test cases
3. execute test cases locally
4. Push the new cases to API Repo under <API Repo>/tests/regression_test_java>

#### Create test definition files

Steps:

  1) clone the whole project to local via "git clone", and run "mvn clean compile" to install all packages and dependencies.
  2) copy the template definition files from src/test/resources/testdefinitions/TestCaseTemplate.json and put to your API def folders,normally it should be normally it shoulbe be put under src/test/resources/testdefinitions/<**API Name**>
  3) modify  the necessary key and values inside the files
      include:
      - testName  ---- Test case Name
      - enabled   -----whether you will test this test case
      - baseUri   -----baseUri for your api in spec ,e.g. /product-catalog/v2
      - path      ----- endpoint path inside your api . e.g. /products
      - method    -----  http method, e.g. "POST"
      - statusCode ----- response code .e.g. "200"
      - inputFormat ---- input content type  e.g. "applicaiton/json"
      - outputFormat ---- output content type  e.g. "applicaiton/json"
      - queryParameters  ---- parameters in your query
      - pathParameters   ---- parameters in the path
      - headerParameters ----- paratmeters in header
      - authParameters ----- paratmeters in authentication. Only use this one if your API is external DLB and need mTLS.
      - bodyParameters   ----- body parameters
      - bodyJSONArray -----  use this one only when you body is JSONArray
      - formParameters   ----- form parameters
      - responseCheck       ---- check point of your test cases, e.g.  response Code is 200
      - schemaCheckEnabled": --- if need to check the response schema aligned with API Spec,false or true
      - responseSchemaValidate  --- API Spec file location.  Currently only yaml file and OpenAPI3.0 json are supported
      - responseToGlobalVariables --- put the json response path value to global variables for future use

  4) Detailed Examples are as below:

```json
 [
  {
    "testName": "PT_Test_Get_All_Products_Endpoint_Response_200",
    "enabled": true
    "baseUri": "/product-catalog/v2",
    "path": "/products",
    "method": "GET",
    "statusCode": "200",
    "inputFormat": "application/json",
    "outputFormat": "application/json",
    "queryParameters":
    {
      "include": "multiUseProducts",
      "bb": "dd"
    },
    "pathParameters":
    {

    },
    "headerParameters":
    {
      "header1": "aa",
      "header2": "bb"
    },
    "authParameters":
    {
      "type": "staffToken",
      "ocvId": "1000552160"
    },

    "bodyParameters":
    {
      "body1": "str1",
      "body2": "str2"
    },
    "formParameters": {

    },

    "responseCheck":
    {
      "statusCode": "200",
      "responseTime":3000,
      "body.dataType":"anz.eventresults.GetAllProductDetailsResult",
      "body.dataCount": "1"
    },
    "schemaCheckEnabled": false,

    "responseSchemaValidate": "openAPI/anz-cpb-product-catalog-api.yml",
   },
  {
    "testName": "NG_Test_Get_All_Products_Endpoint_Response_200",
    "enabled": true,
    "path": "/product-catalog/v2/products",
    "method": "POST",
    "statusCode": "400",
    "inputFormat": "application/json",
    "outputFormat": "application/json",
    "queryParameters":
    {
      "include": "multiUseProducts",
      "bb": "dd"
    },
    "pathParameters":
    {

    },
    "bodyParameters":
    {
      "body1": "str1",
      "body2": "str2"
    },
    "formParameters": {

    },
    "headerParameters":
    {
      "header1": "aa",
      "header2": "bb"
    },
    "authParameters":
    {
      "type": "userJWT",
      "ocvId": "1001051903"

    },
    "responseCheck":
    {
      "statusCode": "200",
      "responseTime":3000,
      "body.dataType":"anz.eventresults.GetAllProductDetailsResult",
      "body.dataCount": 1
    },
    "schemaCheckEnabled": false,
    "responseSchemaValidate": "openAPI/anz-cpb-product-catalog-api.yml",
 
  }
]
```

#### Special /complex test scenarios

1. For getting dynammic value for testing, such as guid,date,future Date(now +20 days) use {{varName}} for dynamci value.

      ```json
        "headerParameters": {
          "x-span-id": "{{guid}}"
       },

     ```

2. For setting path value in testing, such as use {varName} for pathVariables

      ```json

         "path": "/products/{productCode}",


          "pathParameters": {
          "productCode": "SAVING01"
        }
        
      ```

3. use global values from previous request/response
   Set the response to global in the 1st request:

   ```json

    "responseToGlobalVariables": {
       "cards[0].tokenized_card_number":"cardNumber"
     }
   ```

   Then refer it in the 2nd request:

   ```json
   "bodyParameters":
     {
       "status": "Block CNP",
       "cardNumber": "{{GlobalVariables.cardNumber}}",
       "reason": ""
     },
   ```

#### Generating test cases

  a) ~~[not needed anymore] create specific API Package folder  under src/test/java folder.  For example, if API_NAME is anz-cpb-cfaas-api, then the package name would be anz.cpb.cfaas.api~~

  b) use below command in the project folder to create test cases.

```java
java src/main/java/testgenerator/TestCaseGenerator.java <API_name> <test def file path> <outputClassName> <isInternalDLB> <isJWTNeeded>

Please note:  you should change above input args  bases on your API .

    API_NAME                         --- your API name,for example anz-cpb-cfaas-api
    testdefinition file/folder path  --- your test case definition json file/folder path, you can use folder path to generate lots of test java files at the same time.
    outputClassName                  --- generated java test file name .This only valid when the    
                                          above path is file, not folders.  Otherwise, predefined test java file names will be created based on the input def file name.
    isInternalDLB                    ---true or false ,default is true
    isJWTNeeded                       ---true or false, default is false
```

After command finishes,A new file src/test/java/anz/cpb/cfaas/api/{outputClassName}.java will be created.

#### running test cases

The following  provides an example of how we can obtain this configuration file.

use below command in the project folder to create test cases.

```java
 mvn -Dtest="$PACKAGE_NAME.**" -Dtest.env=${TEST_ENVIRONMENT} test   -s settings.xml
```

E.g.  mvn -Dtest="anz.cpb.product.catalog.api.**" -Dtest.env=dev test   -s settings.xml

Please Note:

  1. $PACKAGE_NAME is your api package name based on java naming convention.

     For example, if API_NAME is anz-cpb-cfaas-api, then the package name would be anz.cpb.cfaas.api

  2. TEST_ENVIRONMENT(Default is Dev): 
      CH1.0 --sit or dev
      CH2.0 --sit_ch2.0 or dev_ch2.0

#### push the new case into API Repo

After running successfully in local env, please upload the cases into your <API> Repo.

The paths should be here(examples for anz-cpb-cfaas-api):

  ```java
  <api repo>/tests/regression_test_java/src/test/java/anz/cpb/cfaas/api
  ```

#### trigger  the test in  API Repo

After get code promoted to API repo, below GHA will be automatically triggered and run all tests for your API.
<https://github.com/anzx/anz-cpb-cfaas-api/actions/workflows/aaa--regression-testing-v2.yml>

## Planned enhancement

1. load test data from data files
2. setup pre-script in test definition files


## Enhancement of  the new framework from 1/12/2023

1. support creating java test files from defintion folders,rather than individual file.
2. Recurrsivly load test def files and generate java files
enhance the frameworks via automatically generate test java package , folders and put generated java files into the right path.
3. set response value output to global vaialbes. Thus the variables can be used in related requests.Eg. to set card status, need to get card numbers first.
To fix the tests execution dependency issues,set execution order by the case order in test definition json files.
4. automatically generate uuid/date variables for some scenarios, such as setting card status, set expire date....
5. GHA has been updated to upload the test reports into honeyCombe for results analysis.https://ui.honeycomb.io/anzx/environments/non-production/board/pT2uGdjsdKA/***-testing
6. Can fetch client id and secret from GSM,rather than hard-coded them in configuration files.
7. CH2.0 environment support
