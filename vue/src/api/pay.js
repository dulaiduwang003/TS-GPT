import request from "@/utils/Request";

/**
 *   获取商品
 */
export function reqGetAllProduct() {
    return request({
        url: '/pay/get/all/product', method: 'GET'
    })
}


/**
 *   创建支付宝订单
 */
export function reqCreateAliOrders(data) {
    return request({
        url: '/pay/created/alipay', method: 'POST', data
    })
}


/**
 *   获取支付宝支付状态
 */
export function reqGetAliOrderStatus(data) {
    return request({
        url: '/pay/orders/status/' + data, method: 'GET'
    })
}

