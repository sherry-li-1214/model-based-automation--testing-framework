import http from 'k6/http';
import { Rate, Counter } from 'k6/metrics';
import { check, sleep } from "k6";
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { textSummary } from "https://jslib.k6.io/k6-summary/0.0.1/index.js";
// Custom metrics
// We instantiate them before our main function

export let CounterErrors = new Counter('Errors');

var myRate = new Rate("my_rate");

export let options = {
  discardResponseBodies: true, //specify if response bodies should be discarded
  tlsAuth: [
    {
      domains: ['qa.int.(BBB)com'],
      cert: open(''),
      key: open('/'),
    },
  ],
  scenarios: {
    /*contacts: {
      executor: 'ramping-vus',
      startVUs: 1,
      stages: [
        { duration: "20m", target: 50 }, // Linearly ramp up from 1 to 50 VUs during first 20 minute
        { duration: "20m", target: 50 }, // Hold at 50 VUs for the next 20 minutes
        { duration: "20m", target: 1 },    // Linearly ramp down from 50 to 1 VUs over the last 20 minutes
      ],
      gracefulRampDown: '30s', //time to wait for an already started iteration to finish before stopping it during a ramp down.
      gracefulStop: '30s', //a duration to wait before forcefully interrupting the iterations. Default value of this property is 30s
    },*/
    /*constant_request_rate_10_per_sec: {
      executor: 'constant-arrival-rate',
      rate: 10,
      timeUnit: '1s',
      duration: '30m',
      preAllocatedVUs: 10,
      maxVUs: 500,
    },
    constant_request_rate_20_per_sec: {
      executor: 'constant-arrival-rate',
      rate: 20,
      timeUnit: '1s',
      duration: '60m',
      preAllocatedVUs: 20,
      maxVUs: 500,
    },
      constant_request_rate_50_per_sec: {
      executor: 'constant-arrival-rate',
      rate: 50,
      timeUnit: '1s',
      duration: '30m',
      preAllocatedVUs: 50,
      maxVUs: 500,
    }
    constant_request_rate_100_per_sec: {
      executor: 'constant-arrival-rate',
      rate: 100,
      timeUnit: '1s',
      duration: '30m',
      preAllocatedVUs: 100,
      maxVUs: 500,
    },*/
    constant_request_rate_1_per_sec: {
      executor: 'constant-arrival-rate',
      rate: 2,
      timeUnit: '1s',
      duration: '8m',
      preAllocatedVUs: 100,
      maxVUs: 500,
    },
  },
 summaryTrendStats: ["avg", "min", "med", "max", "p(90)", "p(95)", "p(99)", "p(99.99)"], //set the summary trends you want to see
 thresholds: {
    http_req_failed: ['rate<0.01'],   // http errors should be less than 1%
    http_req_duration: ['p(95)<400'], //the avg should be less than 400ms
  },
};

/* Main function
The main function is what the virtual users will loop over during test execution.
*/
export default function () {
  let data = { relationshipType: 'SOLE', age: 24, customerSegment: "RETAIL", residencyStatus: "AU_CITIZEN" };
var url = 'https://' + __ENV.host + '/product-catalog/v2/packages/search';
var params = {
    headers: {
      'client_id': __ENV.client_id, //environment variable
      'client_secret' : __ENV.client_secret, //environment variable
      'Content-Type': 'application/json'
    },
    timeout: 180000, //Maximum time to wait for the request to complete. Default timeout is 60 seconds ("60s").
  };

  //let resp = http.get(url, params);
  let resp = http.post(url, JSON.stringify(data), params);

  if (resp.status != 200) {
      console.error('Could not send summary, got status ' + resp.status);
      console.log(JSON.stringify(resp));
      console.log(JSON.stringify(__ENV.client_id));
      CounterErrors.add(1); //add to error counter
      myRate.add(1);
      sleep(1);
    }

}
export function handleSummary(data) {
  return {
    stdout: textSummary(data, { indent: '→', enableColors: true }),// Show the text summary to stdout...
    'summary.json': JSON.stringify(data), //the default data object
    'summary.html': htmlReport(data)

  };
}

// k6 run script-qa.js -e client_id=623073228170450a83367096df679fb0 -e client_secret=f9e241FF9A5142e3A0A70a00ee20e11d --iterations=1 -e host=qa.int.integration.(BBB)...com --http-debug=full --logformat=raw
// k6 run script-qa.js -e client_id=623073228170450a83367096df679fb0 -e client_secret=f9e241FF9A5142e3A0A70a00ee20e11d -e host=qa.int.integration.(BBB)...com
// k6 run script-qa.js -e client_id=623073228170450a83367096df679fb0 -e client_secret=f9e241FF9A5142e3A0A70a00ee20e11d --iterations=10 -e host=qa.int.integration.(BBB)...com
//k6 run script-qa.js -e client_id=623073228170450a83367096df679fb0 -e client_secret=f9e241FF9A5142e3A0A70a00ee20e11d --iterations=20 -e host=qa.int.integration.(BBB)...com
//k6 run script-qa.js -e client_id=623073228170450a83367096df679fb0 -e client_secret=f9e241FF9A5142e3A0A70a00ee20e11d --iterations=50 -e host=qa.int.integration.(BBB)...com
// k6 run script-qa.js -e client_id=623073228170450a83367096df679fb0 -e client_secret=f9e241FF9A5142e3A0A70a00ee20e11d --iterations=100 -e host=qa.int.integration.(BBB)...com
