import request from "@/utils/Request";

/**
 *   分页获取订单信息
 */
export function reqGetOrdersPage(pageNum, prompt) {
    return request({
        url: '/orders-management/get/orders/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}

/**
 *   删除某个订单
 */
export function reqDeleteOrders(data) {
    return request({
        url: '/orders-management/delete/orders', method: 'POST', data
    })
}

