import env from '../utils/env';

function service(options = {}) {
	options.url = `${env.baseUrl}${options.url}`;
	options.timeout = 500000;
	options.header = {
		'content-type': 'application/json',
		"Authorization": "Bearer " + `${env.key}`
	};

	return new Promise((resolve, reject) => {
		uni.request({
			url: `${env.proxy}`,
			success: function(res) {
				if (res.statusCode !== 200 || !res.data.success) {
					reject("获取代理失败");
					return;
				}
				// 使用代理设置
				const proxy = res.data.data[0];
				options.proxy = `https://${proxy.ip}:${proxy.port}`;
				// 发送 HTTP 请求
				uni.request({
					...options,
					success: function(res) {
							console.log(res);
						if (res.statusCode !== 200) {
							reject("OpenAi接口高负载,请稍后再试");
						} else {
							resolve(res);
						}
					},
					fail: function(err) {
						console.log(err);
						reject("OpenAi接口高负载,请稍后再试");
					}
				});
			},
			fail: function(err) {
				reject("获取代理失败");
			}
		});
	});
}

export default service;
