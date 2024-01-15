import request from "@/utils/Request";


/**
 *   获取对话密钥分页
 */
export function reqGetGptKeyPage(pageNum, prompt) {
    return request({
        url: '/gpt-management/get/gpt-key/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}


/**
 *   获取绘图密钥分页
 */
export function reqGetDallKeyPage(pageNum, prompt) {
    return request({
        url: '/dall-management/get/dall-key/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}


/**
 *   获取模型分页
 */
export function reqGetGptModelPage(pageNum, prompt) {
    return request({
        url: '/gpt-management/get/gpt-model/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}

/**
 *   修改密钥信息
 */
export function reqUpdateGptKey(data) {
    return request({
        url: '/gpt-management/update/gpt-key', method: 'POST', data
    })
}

/**
 *   修改模型信息
 */
export function reqUpdateGptModel(data) {
    return request({
        url: '/gpt-management/update/gpt-model', method: 'POST', data
    })
}

/**
 *   修改密钥信息
 */
export function reqUpdateDallKey(data) {
    return request({
        url: '/dall-management/update/dall-key', method: 'POST', data
    })
}

/**
 *   新增GPT密钥
 */
export function reqAddDallKey(data) {
    return request({
        url: '/dall-management/add/dall-key', method: 'POST', data
    })
}

/**
 *   获取dall额外参数
 */
export function reqGetDallExtraConfig() {
    return request({
        url: '/dall-management/get/dall-extra', method: 'GET'
    })
}

/**
 *   设置dall额外参数
 */
export function reqSetDallExtraConfig(data) {
    return request({
        url: '/dall-management/set/dall-extra', method: 'POST', data
    })
}


/**
 *   新增GPT密钥
 */
export function reqAddGptKey(data) {
    return request({
        url: '/gpt-management/add/gpt-key', method: 'POST', data
    })
}


/**
 *   新增模型
 */
export function reqAddGptModel(data) {
    return request({
        url: '/gpt-management/add/gpt-model', method: 'POST', data
    })
}


/**
 *   删除密钥
 */
export function reqDeleteGptKey(data) {
    return request({
        url: '/gpt-management/delete/gpt-key', method: 'POST', data
    })
}

/**
 *   保存额外配置
 */
export function reqSetGptExtra(data) {
    return request({
        url: '/gpt-management/set/gpt/extra', method: 'POST', data
    })
}

/**
 *   保存额外配置
 */
export function reqGetGptExtra() {
    return request({
        url: '/gpt-management/get/gpt/extra', method: 'GET'
    })
}

/**
 *   删除密钥
 */
export function reqDeleteGptModel(data) {
    return request({
        url: '/gpt-management/delete/gpt-model', method: 'POST', data
    })
}


/**
 *   删除密钥
 */
export function reqDeleteDallKey(data) {
    return request({
        url: '/dall-management/delete/dall-key', method: 'POST', data
    })
}


/**
 *   载入GPT配置
 */
export function reqLoadGptStructure() {
    return request({
        url: '/gpt-management/load/load-gpt-structure', method: 'POST'
    })
}


/**
 *   载入Dall配置
 */
export function reqLoadDallStructure() {
    return request({
        url: '/dall-management/load/load-dall-structure', method: 'POST'
    })
}
