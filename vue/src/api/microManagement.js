import request from "@/utils/Request";

/**
 *   分页获取预设词
 */
export function reqGetMicroAppPage(pageNum, prompt) {
    return request({
        url: '/micro-management/get/micro-app/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}

/**
 * 新增预设词
 * @param data
 * @returns {*}
 */
export function reqAddMicroApp(data) {
    return request({
        url: '/micro-management/add/micro-app', method: 'POST', data
    })
}


/**
 *   获取类别list
 */
export function reqGetMicroCategoryList() {
    return request({
        url: '/micro-management/get/micro-category/list', method: 'GET'
    })
}

/**
 * 删除
 * @returns {Promise<AxiosResponse<any>> | *}
 */
export function reqDeleteMicroApp(data) {
    return request({
        url: '/micro-management/delete/micro-app', method: 'POST', data
    })
}


/**
 *   获取类别list
 */
export function reqUpdateMicroApp(data) {
    return request({
        url: '/micro-management/update/micro-app', method: 'POST', data
    })
}


/**
 *   新增类别
 */
export function reqAddMicroCategory(data) {
    return request({
        url: '/micro-management/add/micro-category', method: 'POST', data
    })
}


/**
 *   删除类别
 */
export function reqDeleteMicroCategory(data) {
    return request({
        url: '/micro-management/delete/micro-category', method: 'POST', data
    })
}


/**
 *   修改类别
 */
export function reqUpdateMicroCategory(data) {
    return request({
        url: '/micro-management/update/micro-category', method: 'POST', data
    })
}


/**
 *   修改类别
 */
export function reqGetMicroCategoryPage(pageNum, prompt) {
    return request({
        url: '/micro-management/get/micro-category/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}
