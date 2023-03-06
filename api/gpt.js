import request from './../utils/request';


export function gptTurbo(data) {
    return request({
        url: 'gpt/local/gpt',
        method: 'POST',
        data
    })
}

export function gpt003(data) {
    return request({
        url: 'gpt/local/003',
        method: 'POST',
        data
    })
}



