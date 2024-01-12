import request from "@/utils/Request";

/**
 *   分页获取用户信息
 */
export function reqGetUserPage(pageNum, prompt) {
    return request({
        url: '/auth-api/user-management/get/user/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}

/**
 *   修改用户信息
 */
export function reqUpdateUserInfo(data) {
    return request({
        url: 'auth-api/user-management/update/user', method: 'POST', data
    })
}
