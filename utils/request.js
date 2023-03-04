import env from '../utils/env';

function service(options = {}) {
	options.url = `${env.baseUrl}${options.url}`;
	options.timeout = 500000
	options.header = {
		'content-type': 'application/json',
		"Authorization": "Bearer " + `${env.key}`
	}
	return new Promise((resolved, rejected) => {
		options.success = (res) => {
			resolved(res)
		};
		options.fail = (err) => {
			uni.showToast({
				icon: 'none',
				duration: 3000,
				title: 'OpenAi接口高负载,请稍后再试'
			});
			resolved({
				"data":{
					"choices": [{
						"message": {
							"role": "assistant",
							"content": "OpenAi接口高负载,请稍后再试"
						}
					}]
				}
			})
		};
		uni.request(options);
	});
}

export default service;
