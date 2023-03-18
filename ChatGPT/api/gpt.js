import request from './../utils/request';


export function GPT_Turbo(data) {
    return request({
        url: 'v1/chat/turbo',
        method: 'POST',
        data
    })
}

export function GPT_Auth(data) {
    return request({
        url: 'v1/auth/' + data,
        method: 'GET'
    })
}

export function GPT_Alpha(data) {
    return request({
        url: 'v1/chat/official',
        method: 'POST',
        data
    })
}

export function GPT_Bing(data) {
    return request({
        url: 'v1/chat/bing',
        method: 'POST',
        data
    })
}

export function APP_Status(data) {
    return request({
        url: 'v1/chat/status',
        method: 'GET',
        data
    })
}
