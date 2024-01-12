export function validateEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}


export function conversionTime(time) {
    const minute = 1000 * 60;
    const hour = minute * 60;
    const day = hour * 24;
    const week = day * 7;
    const month = day * 30;
    const now = new Date().getTime();
    const diffValue = now - time;
    if (diffValue < 0) {
        return;
    }
    let result;
    if (diffValue <= minute) {
        result = "刚刚";
    } else if (diffValue <= hour) {
        result = Math.floor(diffValue / minute) + "分钟前";
    } else if (diffValue <= day) {
        result = Math.floor(diffValue / hour) + "小时前";
    } else if (diffValue <= week) {
        result = Math.floor(diffValue / day) + "天前";
    } else if (diffValue <= month) {
        result = Math.floor(diffValue / week) + "周前";
    } else if (diffValue <= month * 12) {
        result = Math.floor(diffValue / month) + "月前";
    } else {
        result = Math.floor(diffValue / (month * 12)) + "年前";
    }
    return result;
}


export function setChatCache(data) {
    localStorage.setItem("chat-cache", JSON.stringify(data))
}


export function setWritingConfig(data) {
    localStorage.setItem("writing-config-cache",JSON.stringify(data))
}

export function getWritingConfig() {
    return JSON.parse(localStorage.getItem("writing-config-cache"))
}

export function setWritingContent(data) {
    localStorage.setItem("writing-content-cache",data)
}

export function getWritingContent() {
    return JSON.parse(localStorage.getItem("writing-content-cache"))
}


export function getChatCache() {
    return JSON.parse(localStorage.getItem("chat-cache"))
}

export function getOssDoMain() {
    return process.env.VUE_APP_DOMAIN
}

export function getServiceDoMain() {
    return process.env.VUE_APP_BASE_API
}


export function getCurrentFormattedTime() {
    const currentTime = new Date();
    const year = currentTime.getFullYear();
    const month = String(currentTime.getMonth() + 1).padStart(2, '0');
    const day = String(currentTime.getDate()).padStart(2, '0');
    const hours = String(currentTime.getHours()).padStart(2, '0');
    const minutes = String(currentTime.getMinutes()).padStart(2, '0');

    return `${year}/${month}/${day} ${hours}:${minutes}`;
}


export function getCurrentZhCnDate(data) {
    // 获取当前时间戳
    // 将时间戳转换为 Date 对象
    const date = new Date(data);
    // 获取年、月、日、时、分、秒
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    let hour = date.getHours();
    let minute = date.getMinutes();
    let second = date.getSeconds();
    if (second <= 9) {
        second = '0' + second
    }
    if (minute <= 9) {
        minute = '0' + minute
    }
    if (hour <= 9) {
        hour = '0' + hour
    }
    // 拼接日期字符串
    // 返回日期字符串
    return `${year}年${month}月${day}日 ${hour}:${minute}:${second}`;
}

export function removalTime(data) {
    let date = new Date(data);
    let day = date.getDate();
    let month = date.getMonth() + 1;
    let year = date.getFullYear();

    return `${year}-${month}-${day}`;

}

export const scaleImage = (src, width, height) => {
    return new Promise((resolve) => {
        const image = new Image();
        image.src = src;
        image.onload = () => {
            const canvas = document.createElement('canvas');
            canvas.width = width;
            canvas.height = height;
            const ctx = canvas.getContext('2d');
            ctx.drawImage(image, 0, 0, width, height);
            resolve(canvas.toDataURL());
        };
    })
};


export const getImageOriginSize = (src) => {
    return new Promise((resolve) => {
        const image = new Image();
        image.src = src;
        image.onload = () => {
            resolve({
                width: image.naturalWidth,
                height: image.naturalHeight
            });
        };
    });
}


export const convertUrlToBase64 = (src) => {
    return new Promise(resolve => {
        const img = new Image()
        img.crossOrigin = ''
        img.src = src
        img.onload = function () {
            const canvas = document.createElement('canvas');
            canvas.width = img.width;
            canvas.height = img.height;
            const ctx = canvas.getContext('2d');
            ctx.drawImage(img, 0, 0, img.width, img.height);
            const ext = img.src.substring(img.src.lastIndexOf('.') + 1).toLowerCase();
            const dataURL = canvas.toDataURL('image/' + ext);
            resolve(dataURL);
        }
    });

}
