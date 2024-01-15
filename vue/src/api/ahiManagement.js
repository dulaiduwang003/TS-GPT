import request from "@/utils/Request";

/**
 *   获取站点数据
 */
export function reqGetSiteData() {
    return request({
        url: '/ahi-management/get/site-data', method: 'GET'
    })
}

/**
 *   获取服务器异常信息分页
 */
export function reqGetExceptionPage(pageNum) {
    return request({
        url: '/ahi-management/get/exception/page?pageNum=' + pageNum, method: 'GET'
    })
}


/**
 *   删除异常
 */
export function reqDeleteException(data) {
    return request({
        url: '/ahi-management/delete/exception', method: 'POST', data
    })
}
