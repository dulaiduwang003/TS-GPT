
const historyKey = "history"

const openKey = "open"

const enableHistoryKey = "enableHistory"
export function setHistory(data) {
    uni.setStorageSync(historyKey, data)
}

export function getHistory() {
    return uni.getStorageSync(historyKey)
}

export function setOpen(data) {
    uni.setStorageSync(openKey, data)
}

export function getOpen() {
    return uni.getStorageSync(openKey)
}

export function removeHistory() {
    return uni.removeStorageSync(historyKey)
}

export function setHistoryEnable(data){
    uni.setStorageSync(enableHistoryKey, data)
}

export function getHistoryEnable(){
    return uni.getStorageSync(enableHistoryKey)
}

