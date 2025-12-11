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
    let url = `https://joker.identity-services-sit-fr.apps.x.gcpnp../ig-int/jwt`;
    // Request body
    const requestBody=JSON.stringify({
      "cts": "OAUTH2_STATELESS_GRANT",
      "auditTrackingId": "17f492ac-b3b6-43b2-a0bd-078223fe5e2c-613999",
      "subname": "salesforce",
      "iss": "https://identity-services-sit-int-gw.apps-int.x.gcpnp../am/oauth2/system",
      "tokenName": "access_token",
      "token_type": "Bearer",
      "aud": "salesforce",
      "grant_type": "client_credentials",
      "realm": "/system",
      "jti": "GslMKFIk_FptcHd6Q2fJq7iF-is",
      "role": "",
      "rqf": {
        "sub": "f5e54c6b-f363-4d83-a366-a1826625c445",
        "persona": {
          "_id": "c691cddc-49a8-4abb-9fd3-fda0ab9e4784",
          "type": "retail"
        },
        "ocvid": "1000552160"
      },
      "acr": "IAL4.AAL2.FAL2",
      "act": {
        "sub": "salesforce"
      },
      "amr": [
        "pwd"
      ],
      "._token_type": "staffRqfCustomer",
      "scope": [
        "au.(BBB).crm",
        "aegis-introspect",
        "au.(BBB).fraud"
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

