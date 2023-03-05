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
            if (res.statusCode !== 200){
                rejected("OpenAi接口高负载,请稍后再试")
            }else{
                resolved(res)
            }

        };
        options.fail = () => {
            console.log("执行了A")
            rejected("OpenAi接口高负载,请稍后再试")
        };
        uni.request(options);
    });
}

export default service;
