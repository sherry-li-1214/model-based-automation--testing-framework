import http from 'k6/http';
 
/**
 * Authenticate using OAuth against Forge
 * @function
 * @param  {string} token_api_hostname - token_api_hostname.
 * @param  {string} id_token - id_token from Azure. This token is valid for 1 hour.
 * @param  {string} ocvId - ocv id of the customer
 */

export function getFakeTokenstaffJWTFromForge(ocv_id) {
    // Token API URL  
    let url = `https://joker.identity-services/ig-int/jwt`;
    // Request body
    const requestBody=JSON.stringify({
      "cts": "OAUTH2_STATELESS_GRANT",
      "auditTrackingId": "",
      "subname": "salesforce",
      "iss": "https://identity-services-sit/am/oauth2/system",
      "tokenName": "access_token",
      "token_type": "Bearer",
      "aud": "salesforce",
      "grant_type": "client_credentials",
      "realm": "/system",
      "jti": "",
      "role": "",
      "rqf": {
        "sub": "",
        "persona": {
          "_id": "",
          "type": "retail"
        },
        "ocvid": ""
      },
      "acr": "IAL4.AAL2.FAL2",
      "act": {
        "sub": "salesforce"
      },
      "amr": [
        "pwd"
      ],
      "._token_type": "",
      "scope": [
       
      ],
      "expires_in": 31536000
    });
  
    let response;
    //Headers
      let params = {
        headers: {
          'authorization': 'Bearer abc',
          'Content-Type': 'application/json'
         },
      };
      //Invoking the API
      response = http.post(url, requestBody, params);
      return response.json();
  }

