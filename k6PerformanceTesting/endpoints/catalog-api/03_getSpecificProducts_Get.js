
import { check } from 'k6'
import { Counter, Rate, Trend } from 'k6/metrics'
import logger from '../../utils/log.js'
import http from 'k6/http';

let T01pass_Rate = new Rate('03_getSpecificProducts_Pass_Rate')
let T01failed_Count = new Counter('03_getSpecificProducts_Fail_Count')
let T01pass_Count = new Counter('03_getSpecificProducts_Pass_Count')
let getProductTrend = new Trend('03_getSpecificProducts')
let errorRate = new Rate('errors')

const configFile = JSON.parse(open('../../config/env.json'))
var env = `${__ENV.ENV}`
let configJson = configFile[env]

const getProduct = JSON.parse(open("../../data/product.json"))
const config = JSON.parse(open('../../config/configK6.json'))


let getSpecificProducts=function () {
	//function getSpecificProducts(configJson){

	var params = {
		headers: {
		  'client_id': configJson.client_id, //environment variable
		  'client_secret' : configJson.client_secret, //environment variable
		  'Content-Type': 'application/json',
		  'Accept':"*/*"
		},
		timeout: 180000, //Maximum time to wait for the request to complete. Default timeout is 60 seconds ("60s").
	  };

	   var url = 'https://' + configJson.url + '/product-catalog/v2/products/SAVING01?include=standardRates&include=ratePlans&effectiveDate=2024-05-20';
		
	  //let resp = http.get(url, params);
	let res = http.get(url, params);
	logger.Logger('Starting 03_getSpecificProducts for Iteration ' + (__ITER + 1))
	logger.Logger('03_getSpecificProducts status code is :: ' + res.status)
	//logger.Logger('03_getSpecificProducts Body is :: ' + res.body)

	let content = JSON.parse(res.body)
	
	check(res, {
		'status was 200': (res) => res.status == 200,
		'transaction time OK': (res) => res.timings.duration < config.SLA.getProducts,
		'content OK for 03_getSpecificProducts': (res) => {
			if (res.status == 200) {
				//if (Object.keys(JSON.parse(res.body)) == 'data') {
				if(content.data[0].productDetails[0].productCode=='SAVING01'){	
				//	TranStatus = 'PASS'
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

 
export default  Object.freeze({
	getSpecificProducts
})
 
 


