# K6 POC

## Overview

This project is a proof of concept for K6, a load testing tool.

# (AAA) Automation Testing Framework


<!-- TOC -->
- [K6 POC](#k6-poc)
  - [Overview](#overview)
- [ Automation Testing Framework](#automation-testing-framework)
  - [What Can I do with K6 Performance test frameworks](#what-can-i-do-with-k6-performance-test-frameworks)
  - [How does it work?](#how-does-it-work)
    - [Directory Structure](#directory-structure)
  - [How to use it for (CCC) Performance  testing](#how-to-use-it-for-performance--testing)
    - [Quick examples:](#quick-examples)
      - [Create test  files](#create-test--files)
      - [Running test cases](#running-test-cases)
      - [push the new case into API Repo](#push-the-new-case-into-api-repo)
<!--TOC -->

 


K6 Performance test is a framework for automated Perfoamcne testing of  APIs. It follows   running in local or remote servers.


## What Can I do with K6 Performance test frameworks

With the new framework, you can automatically create java restAssured class file , execute the test cases and generate allure reports.

## How does it work?
The figure below shows how Performance Test works:


### Directory Structure

- `certs/`: This folder contains...
- `config/`: This folder is for...
- `data`: This folder is responsible for...
- `endpoints`: API  request endpoints for each Tests
- `payload`: the payload used by endpoints
- `reports`: reports generated after test finishes
- `tests` : all k6 tests 
- `utils`: all utilities used in the projects

## How to use it for (CCC) Performance  testing

### Quick examples:
1. create test  files
2. execute test cases locally
3. Push the new cases to <API Repo>

#### Create test  files
Steps:
  1) clone the whole project to local via "git clone" 
  2) in endpoints folder, create your endpoint js files.
  3) If you need to extract variables outside your endpoints, in payload folder, define your payload json file when exectue endpoints
  4) put the data file into data folder.


#### Running test cases

The following  provides an example of how we can obtain this configuration file.

use below command in the project folder to execute test cases.

1)for one endpoint in one js file
```javascript
ENV=sit_ch2.0 k6 run PT_One_Endpoint_test.js  
```
2) run multiple endpoints in one js file
   
```javascript
ENV=sit_ch2.0 k6 run PT_catalog-api_v1.js  
```



Please Note:
   
 
 #### push the new case into API Repo
After running successfully in local env, please upload the cases into your <API> Repo.

The paths should be here(examples for ):
  ```
  <api repo>/tests/performance_test
 ```


