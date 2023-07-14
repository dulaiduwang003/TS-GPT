import request from './../utils/request';

export function userInfo() {
    return request({
        url: 'user/get/info',
        method: 'GET'
    })
}

export function reward(data) {
    return request({
        url: 'user/get/reward/' + data,
        method: 'GET'
    })
}


export function weChatLogin(data) {
    return request({
        url: 'auth/wx/login',
        method: 'POST',
        data
    })
}

export function weChatLogout() {
    return request({
        url: 'auth/wx/logout',
        method: 'POST'
    })
}

export function exchangeCode(data) {
    return request({
        url: 'user/get/exchange/' + data,
        method: 'GET'
    })
}

