import env from '../utils/env';

function service(options = {}) {
    options.url = `${env.baseUrl}${options.url}`;
    options.timeout = 100000;
    return new Promise((resolve, reject) => {
        // 发送 HTTP 请求
        uni.request({
            ...options,
            success: function (res) {
                if (res.statusCode !== 200) {
                    reject("调用Ai接口时出现错误,请重试");
                } else {
                    if (res.data.code !== 20000) {
						uni.$emit('removeErrMsg');
                        // reject(res.data.msg);
						reject("ChatGPT接口异常 请尝试切换模型或 检查Key是否有效");
                    } else {
                        resolve(res.data);
                    }

                }
            },
            fail: function (err) {
				uni.$emit('removeErrMsg');
                reject("调用服务端接口错误 可能服务器正在维护");
            }
        });
    });
}

export default service;
