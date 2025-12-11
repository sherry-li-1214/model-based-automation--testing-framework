import { group, sleep } from 'k6'
import getAllProducts from '../endpoints/.-cpb-product-catalog-api/02_getAllProducts_Get.js'
import getSpecificProducts from '../endpoints/.-cpb-product-catalog-api/03_getSpecificProducts_Get.js'

import { getFakeTokenstaffJWTFromForge } from '../utils/getToken.js';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { textSummary } from "https://jslib.k6.io/k6-summary/0.0.1/index.js";
import  getTLSAuth   from '../utils/tlsAuth.js'

const configFile = JSON.parse(open('../config/env.json'))
const config = JSON.parse(open('../config/configK6.json'))
var env = `${__ENV.ENV}`
let configJson = configFile[env]
let baseURL = configJson.url
let client_id=configJson.client_id
let client_secret=configJson.client_secret
let thinkTime = config.thinkTime
let thinkTime1 = config.thinkTime1

var LoggerTag = config.log

var TranStatus
var JWTToken = null

const (CCC)_HOST = __ENV.host;
const (CCC)_CLIENT_ID = __ENV.client_id;
const (CCC)_CLIENT_SECRET = __ENV.client_secret;
  
export function setup() {
  // Get the forge token to access the API
  
 const ocv_id="";
 let accessTokenResp=getFakeTokenstaffJWTFromForge(ocv_id);
 console.log('accessTokenResp:',accessTokenResp)
  return accessTokenResp;

}

export let options = {
	discardResponseBodies: false,
	/**tlsAuth: [
		{
			cert: open('../certs/cert.pem'),
			key: open('../certs/private.key.pem'),
		 },
	],
	*/
	tlsAuth:getTLSAuth.getTLSAuth(),
	scenarios: {
		GetProduct: {
			executor: 'constant-arrival-rate',
			rate: 1,
			timeUnit: '1s',
			duration: '1m',
			preAllocatedVUs: 100,
			maxVUs: 500,
		},
	},
	summaryTrendStats: ["avg", "med", "max", "p(95)", "p(99)", "p(99.99)"], //set the summary trends you want to see
	thresholds: {
		http_req_failed: ['rate<0.01'],   // http errors should be less than 1%
		http_req_duration: ['p(95)<400'], //the avg should be less than 400ms
	  },
};

export default function () {

	//group('01_getJWT', getToken.getJWT), sleep(thinkTime)

	group('02_getAllProducts', getAllProducts.getAllProducts), sleep(thinkTime)
	//group('03_getSpecificProducts',getSpecificProducts.getSpecificProducts),sleep(thinkTime)
}

export function handleSummary(data) {

	return {
	  stdout: textSummary(data, { indent: '→', enableColors: true }),// Show the text summary to stdout...
	  '../reports/summary30.json': JSON.stringify(data), //the default data object
	  '../reports/summary30.html': htmlReport(data)
  
	};
  }
