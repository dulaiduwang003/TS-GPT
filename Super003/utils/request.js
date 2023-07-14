import env from '../utils/env';
import {getToken, removeServerToken, removeToken} from "@/utils/data";

function service(options = {}) {
    options.url = `${env.baseUrl}${options.url}`;
    options.timeout = 100000;
    if (getToken()) {
        options.header = {
            'content-type': 'application/json',
            'token': `${getToken()}`
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
                        if (res.data.code===401){
                            removeToken()
                            removeServerToken()
                            uni.reLaunch({
                                url: '/pages/member/member'
                            })
                        }else{
                            reject(res.data.msg);
                        }
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
