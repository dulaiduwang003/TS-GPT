import request from "@/utils/Request";

/**
 *   邮箱登录
 */
export function emailLogin(data) {
    return request({
        url: '/auth-api/auth/email/login', method: 'POST', data
    })
}


/**
 *   获取邮箱验证码
 */
export function sendEmailCode(data) {
    return request({
        url: '/auth-api/email/send/code', method: 'POST', data
    })
}
/**
 *   获取当前登录用户信息
 */
export function getCurrentUserInfo() {
    return request({
        url: '/auth-api/user/get/userinfo', method: 'GET'
    })
}


/**
 * 更新头像
 */
export function logout() {
    return request({
        url: '/auth-api/auth/sign/logout',
        method: 'POST'
    })
}


/**
 *   更新昵称
 */
export function uploadNickName(data) {
    return request({
        url: '/auth-api/user/upload/nickname',
        method: 'POST',
        data
    })
}

/**
 * 更新头像
 * @returns {string}
 */
export function uploadAvatar() {
    return '/auth-api/user/upload/avatar'
}

