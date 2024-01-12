import request from "@/utils/Request";

/**
 *   分页获取产品
 */
export function reqGetProductPage(pageNum, prompt) {
    return request({
        url: '/auth-api/product-management/get/product/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}


/**
 *   新增产品
 */
export function reqAddProduct(data) {
    return request({
        url: '/auth-api/product-management/add/product', method: 'POST', data
    })
}

/**
 *   修改产品
 */
export function reqUpdateProduct(data) {
    return request({
        url: '/auth-api/product-management/update/product', method: 'POST', data
    })
}


/**
 * 删除头像
 */
export function reqDeleteProduct(data) {
    return request({
        url: '/auth-api/product-management/delete/product', method: 'POST', data
    })
}

