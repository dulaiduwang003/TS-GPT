import request from './../utils/request';


export function gptTurbo(data) {
    return request({
        url: 'v1/chat/completions',
        method: 'POST',
        data
    })
}

export function gpt003(data) {
    return request({
        url: 'v1/completions',
        method: 'POST',
        data
    })
}



