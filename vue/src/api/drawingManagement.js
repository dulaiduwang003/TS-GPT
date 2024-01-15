import request from "@/utils/Request";


/**
 *   获取绘图提示词分页
 */
export function reqGetDrawingPromptPage(pageNum, prompt) {
    return request({
        url: '/drawing-management/get/drawing-prompt/page?pageNum=' + pageNum + '&prompt=' + prompt, method: 'GET'
    })
}



/**
 *   修改绘图提示词
 */
export function reqUpdateDrawingPrompt(data) {
    return request({
        url: '/drawing-management/update/drawing-prompt', method: 'POST', data
    })
}

/**
 *   删除绘图提示词
 */
export function reqDeleteDrawingPrompt(data) {
    return request({
        url: '/drawing-management/delete/drawing-prompt', method: 'POST', data
    })
}

/**
 *   新增绘图提示词
 */
export function reqAddDrawingPrompt(data) {
    return request({
        url: '/drawing-management/add/drawing-prompt', method: 'POST', data
    })
}
