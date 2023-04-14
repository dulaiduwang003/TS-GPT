import request from './../utils/request';


export function chat(data) {
    return request({
        url: 'v1/chat/completions',
        method: 'POST',
        data
    })
}

export function intensifier(data) {
    return request({
        url: 'v1/chat/intensifier',
        method: 'POST',
        data
    })
}

export function code(data) {
    return request({
        url: 'v1/chat/code',
        method: 'POST',
        data
    })
}

export function translation(data) {
    return request({
        url: 'v1/chat/translation',
        method: 'POST',
        data
    })
}

export function generations(data) {
    return request({
        url: 'v1/images/generations',
        method: 'POST',
        data
    })
}

export function bing(data) {
    return request({
        url: 'v1/chat/bing',
        method: 'POST',
        data
    })
}

export function face(data) {
    return request({
        url: 'v1/images/face',
        method: 'POST',
        data
    })
}
