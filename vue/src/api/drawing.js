import request from "@/utils/Request";


/**
 *   发布SD任务
 */
export function reqPushSdGenerateTask(data) {
    return request({
        url: '/sd/push/generate',
        method: 'POST',
        data
    })
}

/**
 *   发布对话(DALL2)任务
 */
export function reqPushDallGenerateTask(data) {
    return request({
        url: '/dall/push/generate',
        method: 'POST',
        data
    })
}


/**
 *   发布对话(DALL3)任务
 */
export function reqPushDialogueImageTask(data) {
    return request({
        url: '/dall/dialogue/image',
        method: 'POST',
        data
    })
}

/**
 *   获取任务执行状态
 */
export function reqGetDrawingTask(data) {
    return request({
        url: '/drawing/get/task/' + data,
        method: 'GET'
    })
}


/**
 * 获取SD参数
 * @returns {*}
 */
export function reqGetSdParam() {
    return request({
        url: '/sd/get/param',
        method: 'GET'
    })
}

/**
 * 获取我的作品
 */
export function reqGetDrawingOpus() {
    return request({
        url: '/drawing/get/opus',
        method: 'GET'
    })
}

/**
 * 删除作品
 * @returns {*}
 */
export function reqDeleteDrawingOpus(data) {
    return request({
        url: '/drawing/delete/opus',
        method: 'POST',
        data
    })
}

/**
 * 随机获取提示词
 * @returns {*}
 */
export function reqGetPromptWordsRandomly(data) {
    return request({
        url: '/drawing/get/prompt/' + data,
        method: 'GET'
    })
}
