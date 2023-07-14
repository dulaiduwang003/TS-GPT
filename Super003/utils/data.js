const tokenKey = "TOKEN_KEY"
const dataKey = "DATA_KEY"
const serverKey ="SERVER_KEY"

export function setServerToken(data) {
    uni.setStorageSync(serverKey, data)
}

export function getServerToken() {
    return uni.getStorageSync(serverKey)
}

export function removeServerToken() {
    return uni.removeStorageSync(serverKey)
}
export function setToken(data) {
    uni.setStorageSync(tokenKey, data)
}

export function getToken() {
    return uni.getStorageSync(tokenKey)
}

export function removeToken() {
    return uni.removeStorageSync(tokenKey)
}

export function setData(data) {
    uni.setStorageSync(dataKey, data)
}

export function getData() {
    return uni.getStorageSync(dataKey)
}

export function removeData() {
    return uni.getStorageSync(dataKey)
}
