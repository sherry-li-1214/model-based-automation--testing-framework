
import { check } from 'k6'
import { Counter, Rate, Trend } from 'k6/metrics'
import logger from '../../utils/log.js'
import http from 'k6/http';

let T01pass_Rate = new Rate('04_getEligibleProducts_Pass_Rate')
let T01failed_Count = new Counter('04_getEligibleProducts_Fail_Count')
let T01pass_Count = new Counter('04_getEligibleProducts_Pass_Count')
let getProductTrend = new Trend('04_getEligibleProducts')
let errorRate = new Rate('errors')

const configFile = JSON.parse(open('./../config/env.json'))
var env = `${__ENV.ENV}`
let configJson = configFile[env]



const getProduct = JSON.parse(open("./../data/product.json"))
const config = JSON.parse(open('./../config.json'))


let getSpecificProducts = function () {

	var params = {
		headers: {
		  'client_id': configJson.client_id, //environment variable
		  'client_secret' : configJson.client_secret, //environment variable
		  'Content-Type': 'application/json',
		  'Accept':"*/*"
		},
		timeout: 180000, //Maximum time to wait for the request to complete. Default timeout is 60 seconds ("60s").
	  };

	  let data = { aa: 'SOLE', bb: 24, cc: " ", dd: "AU_CITIZEN" };

	 
	  var url = 'https://' + __ENV.host + '/product-catalog/v2/products/search';
	  
	  var params1 = {
		headers: {
		  'client_id': __ENV.client_id, //environment variable
		  'client_secret' : __ENV.client_secret, //environment variable
		  'Content-Type': 'application/json'
		},
		timeout: 180000, //Maximum time to wait for the request to complete. Default timeout is 60 seconds ("60s").
	  };
	
	  //let resp = http.get(url, params);
	  let res = http.post(url, JSON.stringify(data),params);
	
	//let baseURL = configJson.url


	//logger.Logger('Starting 03_getSpecificProducts for Iteration ' + (__ITER + 1))
	logger.Logger("client id is:"+configJson.client_id);
	//let res = http.get("https://"+baseURL + '/product-catalog/v2/products', params)
	//logger.Logger('03_getSpecificProducts status code is :: ' + res.status)
	//logger.Logger('03_getSpecificProducts Body is :: ' + res.body)

	let content = JSON.parse(res.body)
	//orderNumber = content.data.submitApplication.orders[0].orderID.orderNumber

	//versionAddress = content.data.submitApplication.orders[0].orderID.version
	
	check(res, {
		'status was 200': (res) => res.status == 200,
		'transaction time OK': (res) => res.timings.duration < config.SLA.getProducts,
		'content OK for 03_getSpecificProducts': (res) => {
			if (res.status == 200) {
				if (Object.keys(JSON.parse(res.body)) == 'data') {
				//	TranStatus = 'PASS'
					getOrder.add(res.timings.duration)
					logger.Logger('response time for 03_getSpecificProducts:' + (res.timings.duration))
					T01pass_Rate.add(1)
					T01pass_Count.add(1)
					return true
				}
				else {
				//	TranStatus = 'FAIL'
					T01pass_Rate.add(0)
					T01failed_Count.add(1)
					return false
				}
			}
			else {
			//	TranStatus = 'FAIL'
				T01pass_Rate.add(0)
				T01failed_Count.add(1)
				return false
			}
		}
	}) || errorRate.add(1)
}

export default Object.freeze({
	getSpecificProducts
})
