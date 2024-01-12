import request from "@/utils/Request";


/**
 *   发布SD任务
 */
export function pushSdGenerateTask(data) {
    return request({
        url: '/drawing-api/sd/push/generate',
        method: 'POST',
        data
    })
}

/**
 *   发布对话(DALL2)任务
 */
export function pushDallGenerateTask(data) {
    return request({
        url: '/drawing-api/dall/push/generate',
        method: 'POST',
        data
    })
}


/**
 *   发布对话(DALL3)任务
 */
export function pushDialogueImageTask(data) {
    return request({
        url: '/drawing-api/dall/dialogue/image',
        method: 'POST',
        data
    })
}

/**
 *   获取任务执行状态
 */
export function getDrawingTask(data) {
    return request({
        url: '/drawing-api/drawing/get/task/' + data,
        method: 'GET'
    })
}


/**
 * 获取SD参数
 * @returns {*}
 */
export function getSdParam() {
    return request({
        url: '/drawing-api/sd/get/param',
        method: 'GET'
    })
}

/**
 * 获取我的作品
 */
export function getDrawingOpus() {
    return request({
        url: '/drawing-api/drawing/get/opus',
        method: 'GET'
    })
}

/**
 * 删除作品
 * @returns {*}
 */
export function deleteDrawingOpus(data) {
    return request({
        url: '/drawing-api/drawing/delete/opus',
        method: 'POST',
        data
    })
}

/**
 * 随机获取提示词
 * @returns {*}
 */
export function getPromptWordsRandomly(data) {
    return request({
        url: '/drawing-api/drawing/get/prompt/' + data,
        method: 'GET'
    })
}
