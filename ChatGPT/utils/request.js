import env from '../utils/env';

function service(options = {}) {
    options.url = `${env.baseUrl}${options.url}`;
    options.timeout = 100000;
    options.header = {
        'content-type': 'application/json'
    };
    return new Promise((resolve, reject) => {
        // 发送 HTTP 请求
        uni.request({
            ...options,
            success: function (res) {
                if (res.statusCode !== 200) {
                    reject(res.data.msg);
                } else {
                    if (res.data.code !== 20000) {
                        reject(res.data.msg);
                    } else {
                        resolve(res.data.data);
                    }
                }
            },
            fail: function (err) {
                reject('网络繁忙,请稍后再试');
            }
        });
    });
}

export default service;
