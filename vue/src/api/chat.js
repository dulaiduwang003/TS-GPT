import request from "@/utils/Request";


/**
 *   获取模型列表
 */
export function getAiModelList() {
    return request({
        url: '/chat-api/model/get/all', method: 'GET'
    })
}


/**
 * 获取微应用
 */
export function getMicroAppPage(pageNum) {
    return request({
        url: '/chat-api/public/get/micro/page?pageNum=' + pageNum,
        method: 'GET'

    })
}


/**
 * 搜索微应用
 */
export function searchMicroApp(pageNum, prompt) {
    return request({
        url: '/chat-api/public/search/micro/page?pageNum=' + pageNum + '&prompt=' + prompt,
        method: 'GET'
    })
}

/**
 * 应用模板
 * @returns {Promise<AxiosResponse<any>> | *}
 * @param data
 */
export function useMicroApp(data) {
    return request({
        url: '/chat-api/micro/use/template',
        method: 'POST',
        data
    })
}
