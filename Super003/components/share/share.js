export default {
    data() {
        return {
            // 默认的全局分享内容
            share: {
                title: '欢迎体验SuperBot',
                path: '/pages/dialogue/dialogue',
                imageUrl: '/static/images/icon/icon.png',

            }
        }
    },
    // 定义全局分享
    // 1.发送给朋友
    onShareAppMessage(res) {
        return {
            title: this.share.title,
            path: this.share.path,
            imageUrl: this.share.imageUrl,
        }
    },
    //2.分享到朋友圈
    onShareTimeline(res) {
        return {
            title: this.share.title,
            path: this.share.path,
            imageUrl: this.share.imageUrl,
        }
    },
}
