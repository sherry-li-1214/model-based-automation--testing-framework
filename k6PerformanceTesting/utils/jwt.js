import http from 'k6/http'

function PostWithValidJwt(endpoint, body, env) {

	let applyReqHeaders = {
		'content-Type': 'application/json'
	}
	applyReqHeaders['origin'] = env.origin
	applyReqHeaders['Authorization'] = 'Bearer ' + JWTToken
	// Default Timeout is 1 min for K6, its increased to 3 mins to wait until response comes back
	let res = http.post(endpoint, JSON.stringify(body), { headers: applyReqHeaders, timeout: 180000 })
	return res
}

function GetWithValidJwt(endpoint, body, env) {
	let jwtApplyReqHeaders = {
		'.-Application-Version': '1.0',
		'Content-Type': 'application/json;charset=UTF-8',
		'origApp': 'SOLA',
		'Accept': 'application/json',
		'.-APPLICATION-ID': 'au-hub',
		'x-application-context': '.-AU-SOLA/1.0',
		'.-channel-function': 'ewewew',
		'.-channel-id': 'unauth-idsvc'
	}
	jwtApplyReqHeaders['.-Request-ID'] = create_UUID()
	jwtApplyReqHeaders['x-b3-traceid'] = create_UUID()
	jwtApplyReqHeaders['RequestID'] = create_UUID()

	// Default Timeout is 1 min for K6, its increased to 3 mins to wait until response comes back
	let res = http.post(endpoint, JSON.stringify(body), { headers: jwtApplyReqHeaders, timeout: 180000 })
	return res
}


function create_UUID() {
	var dt = new Date().getTime();
	var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
		var r = (dt + Math.random() * 16) % 16 | 0;
		dt = Math.floor(dt / 16);
		return (c == 'x' ? r : (r & 0x3 | 0x8)).toString(16);
	});
	return uuid;
}

export default Object.freeze({
	PostWithValidJwt,
	GetWithValidJwt,
	create_UUID
})
