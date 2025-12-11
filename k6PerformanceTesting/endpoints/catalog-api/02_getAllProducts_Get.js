import jwtUtil from '../../utils/jwt.js'
import { check } from 'k6'
import { Counter, Rate, Trend } from 'k6/metrics'
import logger from '../../utils/log.js'

import http from 'k6/http';
import { getFakeTokenstaffJWTFromForge } from '../../utils/getToken.js';
  

let T01pass_Rate = new Rate('02_getAllProducts_Pass_Rate')
let T01failed_Count = new Counter('02_getAllProducts_Fail_Count')
let T01pass_Count = new Counter('02_getAllProducts_Pass_Count')
let getProductTrend = new Trend('02_getAllProducts')
//let errorRate = new Rate('errors')

const configFile = JSON.parse(open('../../config/env.json'))
var env = `${__ENV.ENV}`
let configJson = configFile[env]

const getProduct = JSON.parse(open("../../data/product.json"))
const config = JSON.parse(open('../../config/configK6.json'))


//export default function getAllProducts () {
let getAllProducts=function () {
	var params = {
		headers: {
		  'client_id': configJson.client_id, //environment variable
		  'client_secret' : configJson.client_secret, //environment variable
		  'Content-Type': 'application/json',
		  'Accept':"*/*"
		},
		timeout: 180000, //Maximum time to wait for the request to complete. Default timeout is 60 seconds ("60s").
	  };
	
	var baseURL = 'https://'+configJson.url+ '/product-catalog/v2/products';


	logger.Logger('Starting 02_getAllProducts for Iteration ' + (__ITER + 1))
	let res = http.get(baseURL, params)
	let content = JSON.parse(res.body)
		
	check(res, {
		'status was 200': (res) => res.status == 200,
		'transaction time OK': (res) => res.timings.duration < config.SLA.getProducts,
		'content OK for 02_getAllProducts': (res) => {
			if (res.status == 200) {
				if(content.data.length>=0) {
				//	TranStatus = 'PASS'
					logger.Logger('response time for 02_getAllProducts:' + (res.timings.duration))
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
	getAllProducts
})




