import request from "@/utils/Request";

/**
 *   获取商品
 */
export function getAllProduct() {
    return request({
        url: '/auth-api/pay/get/all/product', method: 'GET'
    })
}


/**
 *   创建支付宝订单
 */
export function createAliOrders(data) {
    return request({
        url: '/auth-api/pay/created/alipay', method: 'POST', data
    })
}


/**
 *   获取支付宝支付状态
 */
export function getAliOrderStatus(data) {
    return request({
        url: '/auth-api/pay/orders/status/' + data, method: 'GET'
    })
}

