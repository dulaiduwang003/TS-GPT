import request from '../utils/server';

export function serverLogin(data) {
    return request({
        url: 'auth/server/login',
        method: 'POST',
        data
    })
}

export function generateCode(data) {
    return request({
        url: 'server/generate/code',
        method: 'POST',
        data
    })
}
export function getCode() {
    return request({
        url: 'server/get/code',
        method: 'GET'
    })
}
export function tactics(data) {
    return request({
        url: 'server/choose/tactics',
        method: 'POST',
        data
    })
}
export function getConfig() {
    return request({
        url: 'server/get/config',
        method: 'GET'
    })
}

export function putConfig(data) {
    return request({
        url: 'server/put/config',
        method: 'POST',
        data
    })
}
