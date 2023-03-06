const historyKey = 'history';

const historyEnableKey = 'historyEnable';

const paintingKey = "painting"

const modelKey = "model"

const openKey = "open"

export function setOpenKey(data) {
    uni.setStorageSync(openKey, data)
}

export function getOpenKey() {
    return uni.getStorageSync(openKey)
}

export function removeOpenKey() {
    return uni.removeStorageSync(openKey)
}


export function setModel(data) {
    uni.setStorageSync(modelKey, data)
}

export function getModel() {
    return uni.getStorageSync(modelKey)
}


export function setHistory(data) {
    uni.setStorageSync(historyKey, data)
}

export function getHistory() {
    return uni.getStorageSync(historyKey)
}

export function removeHistory() {
    return uni.removeStorageSync(historyKey)
}

export function setHistoryEnable(data) {
    uni.setStorageSync(historyEnableKey, data)
}

export function getHistoryEnable() {
    return uni.getStorageSync(historyEnableKey)
}

export function removeHistoryEnable() {
    return uni.removeStorageSync(historyEnableKey)
}

export function getPainting() {
    return uni.getStorageSync(paintingKey)
}

export function removePainting() {
    return uni.removeStorageSync(paintingKey)
}

export function setPainting(data) {
    return uni.setStorageSync(paintingKey, data)
}
