import env from '../utils/env';
import {getServerToken} from "@/utils/data";

function service(options = {}) {
    options.url = `${env.baseUrl}${options.url}`;
    options.timeout = 100000;
    if (getServerToken()) {
        options.header = {
            'content-type': 'application/json',
            'token': `${getServerToken()}`
        };
    }
    return new Promise((resolve, reject) => {
        // 发送 HTTP 请求
        uni.request({
            ...options,
            success: function (res) {
                if (res.statusCode === 200) {
                    if (res.data.code === 200) {
                        resolve(res.data.data);
                    }else{
                        reject(res.data.msg);
                    }
                } else {
                    reject('与服务器建立连接失败');
                }
            },
            fail: function () {
                reject('请检查您的网络环境是否正常');
            }
        });
    });
}

export default service;
