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
                    reject("接口异常或OpenKey已失效");
                } else {
                    if (res.data.code !== 20000) {
                        reject("接口异常或OpenKey已失效");
                    } else {
                        resolve(res.data);
                    }

                }
            },
            fail: function (err) {
                reject("接口异常或OpenKey已失效");
            }
        });
    });
}

export default service;
