import jwtUtil from './jwt.js'
import { check } from 'k6'
import { Counter, Rate, Trend } from 'k6/metrics'
import logger from './util.js'

let T01pass_Rate = new Rate('01_getJWT_Pass_Rate')
let T01failed_Count = new Counter('01_getJWT_Fail_Count')
let T01pass_Count = new Counter('01_getJWT_Pass_Count')
let getVDPToken = new Trend('01_getJWT')
let errorRate = new Rate('errors')

const getJWTQuery = JSON.parse(open('./../payload/01_getJWT.json'))

let getJWT = function () {

	logger.Logger('Starting 01_getJWT for Iteration ' + (__ITER + 1))
	let res = jwtUtil.GetWithValidJwt(vdpURL, getJWTQuery, configJson)
	logger.Logger('01_getJWT status code is :: ' + res.status)
	logger.Logger('Post 01_getJWT Body is :: ' + res.body)

	let content = JSON.parse(res.body)
	JWTToken = content['token']

	check(res, {
		'status was 200': (res) => res.status == 200,
		'transaction time OK': (res) => res.timings.duration < config.SLA.getJWT,
		'content OK for 01_getJWT': (res) => {
			if (res.status == 200) {
				if (JSON.parse(res.body).hasOwnProperty('token')) {
					TranStatus = 'PASS'
					getVDPToken.add(res.timings.duration)
					logger.Logger('response time for 01_getJWT:' + (res.timings.duration))
					T01pass_Rate.add(1)
					T01pass_Count.add(1)
					return true
				}
				else {
					TranStatus = 'FAIL'
					T01pass_Rate.add(0)
					T01failed_Count.add(1)
					return false
				}
			}
			else {
				TranStatus = 'FAIL'
				T01pass_Rate.add(0)
				T01failed_Count.add(1)
				return false
			}
		}
	}) || errorRate.add(1)
}

export default Object.freeze({
	getJWT
})
