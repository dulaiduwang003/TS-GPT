<template>
    <view>
        <view class="notLoggedIn login-animation" v-show="!isLogin">
            <view class="logo-img">
                <img class="canvas" :src="require('static/images/img/bot-4.png')">
            </view>
            <view class="prompt-information">
                <view class="prompt">
                    SuperBot Ai
                </view>
                <view class="prompt-detail">
                    Welcome to SuperBot
                </view>
                <view class="login" @click="login">
                    微信授权登录
                </view>
            </view>
        </view>
        <button open-type="contact" class="contact"></button>
        <button open-type="share" class="share" @click="shareFriends"></button>
        <view class="loggedIn load-animation" v-show="isLogin">
            <view class="vertical">
                <view class="user-radius">
                    <image src="/static/images/img/user.png" class="original" @click="copyOpenId"/>
                </view>
                <view class="user_information">
                    <view class="user_name">你好 人类</view>
                    <view class="user_times">剩余次数 {{ user.frequency }}次</view>
                </view>
            </view>
            <view class="receive">
                <view class="receive-btn" @click="watchVideo">
                    <image src="/static/images/img/signIn.png" class="receive-img"/>
                    <view class="receive-text">
                        <view class="receive-0">观看视频</view>
                        <view class="receive-1">领取2次使用次数</view>
                    </view>
                </view>
                <view class="receive-btn">
                    <image src="/static/images/img/share.png" class="receive-img"/>
                    <view class="receive-text">
                        <view class="receive-0">分享好友</view>
                        <view class="receive-1">每天1次使用次数</view>
                    </view>
                </view>
            </view>
            <view class="panel">
                <view class="panel-han">
                    <view>联系客服</view>
                    <image src="/static/images/img/keyboard.png" class="panel-key"/>
                </view>
                <view class="panel-han" @click="toPrivacy">
                    <view>关于隐私</view>
                    <image src="/static/images/img/keyboard.png" class="panel-key"/>
                </view>
                <view class="panel-han" @click="toExchange">
                    <view>兑换次数</view>
                    <image src="/static/images/img/keyboard.png" class="panel-key"/>
                </view>
                <view class="panel-han" @click="toServer">
                    <view>访问控制台</view>
                    <image src="/static/images/img/keyboard.png" class="panel-key"/>
                </view>
                <view class="panel-han" @click="toConcerning">
                    <view>关于小程序</view>
                    <image src="/static/images/img/keyboard.png" class="panel-key"/>
                </view>
            </view>
            <view class="exit-login" @click="loginOut">
                退出登录
            </view>
        </view>
    </view>
</template>

<script>
import {weChatLogin, weChatLogout, userInfo, reward} from "@/api/auth";
import {getToken, removeToken, setToken} from "@/utils/data";
import env from "@/utils/env";

let videoAd = null;
export default {
    data() {
        return {
            isLogin: false,
            user: {}
        }
    },
    onLoad() {
        this.isLogin = getToken() !== "";
        // #ifdef MP
        this.adLoad();
        // #endif
    },
    onShow() {
        this.userInfo();
    },
    methods: {
        async shareFriends() {
            try {
                const user = await reward(1);
                if (user.frequency !== this.user.frequency) {
                    this.user = user
                    uni.showToast({
                        icon: 'none',
                        duration: 3000,
                        title: `已获得奖励`
                    });
                }
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `奖励发放失败`
                });
            }

        },
        toPrivacy: function () {
            uni.navigateTo({
                url: '/pages/privacy/privacy'
            })
            return true
        },
        toExchange: function () {
            uni.navigateTo({
                url: '/pages/exchange/exchange'
            })
            return true
        },
        toServer: function () {
            uni.navigateTo({
                url: '/pages/server/server'
            })
            return true
        },
        toConcerning: function () {
            uni.navigateTo({
                url: '/pages/concerning/concerning'
            })
            return true
        },
        adLoad: function () {
            const that = this
            const advertisement = env.advertisement
            if (wx.createRewardedVideoAd) {
                videoAd = wx.createRewardedVideoAd({
                    adUnitId: advertisement
                });
                videoAd.onError(err => {
                    console.log(err);
                });
                videoAd.onClose((status) => {
                    if (status && status.isEnded || status === undefined) {
                        that.rewardHandout(0)
                    } else {
                        uni.showToast({
                            icon: 'none',
                            duration: 3000,
                            title: `请重新观看视频获得奖励`
                        });
                    }
                });
            }
        },
        async rewardHandout(data) {
            try {
                this.user = await reward(data);
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `奖励已发放`
                });
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `奖励发放失败`
                });
            }

        },
        watchVideo: function () {
            if (videoAd) {
                videoAd.show().catch(() => {
                    // 广告拉取失败，重试
                    videoAd.load().then(() => {
                        videoAd.show();
                    });
                })
            }
        },
        if(videoAd) {
            videoAd.show().catch(() => {
                // 失败重试
                videoAd.load()
                    .then(() => videoAd.show())
                    .catch(err => {
                        console.log('激励视频 广告显示失败')
                    })
            })

        },
        copyOpenId() {
            uni.vibrateShort()
            uni.setClipboardData({
                data: this.user.openId,
                success: function success(res) {
                    uni.showToast({
                        icon: 'none',
                        duration: 1000,
                        title: '用户标识复制到粘贴板'
                    });
                }
            });
        },
        async userInfo() {
            if (getToken() !== "") {
                try {
                    this.user = await userInfo();
                } catch (e) {
                    uni.showToast({
                        icon: 'none',
                        duration: 1000,
                        title: '获取用户数据失败'
                    });
                }
            }
        },
        async loginOut() {
            const that = this
            uni.vibrateShort()
            try {
                uni.showLoading({
                    title: "正在退出登录",
                    mask: true,
                });
                await weChatLogout();
                removeToken()
                that.isLogin = false
            } finally {
                uni.hideLoading()
            }
        },
        login: function () {
            uni.vibrateShort()
            const that = this
            uni.login({
                async success(res) {
                    uni.showLoading({
                        title: "正在登陆",
                        mask: true,
                    });
                    try {
						console.log(res.code);
                        let token = await weChatLogin({
                            code: res.code
                        });
					
                        setToken(token)
                        await that.userInfo();
                        setTimeout(function () {
                            that.isLogin = true
                            uni.hideLoading()
                        }, 1000);
                    } catch (e) {
                        uni.hideLoading()
                        uni.showToast({
                            icon: 'none',
                            duration: 1000,
                            title: e
                        });
                    }

                }
            })


        }
    }
}
</script>

<style lang="scss">
.loggedIn {
  padding: 30rpx;
}

.receive-img {
  width: 60rpx;
  height: 60rpx;
  margin-top: 5rpx
}

page {
  background-color: #2B2D30;
}

.notLoggedIn {
  margin-top: 200rpx
}

.panel {
  background-color: rgb(60, 62, 74);
  border-radius: 15rpx;
  height: 340rpx;
  margin-top: 30rpx;
  padding: 35rpx
}

.login-animation {
  animation: fadeIn 0.5s ease-in-out forwards;
}

.logo-img {
  text-align: center
}

.prompt-information {
  margin-left: 140rpx;
  margin-top: 100rpx
}

.load-animation {
  animation: fadeIn 0.5s ease-in-out forwards;
}

.original {
  width: 100%;
  height: 100%
}

.vertical {
  display: flex;
  align-items: center
}

.panel-ew {
  background-color: rgb(60, 62, 74);
  border-radius: 15rpx;
  margin-top: 30rpx;
  padding: 35rpx;
  height: 120rpx;
}

.panel-han {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: white;
  font-size: 30rpx;
  margin-bottom: 30rpx
}

.panel-key {
  width: 20rpx;
  height: 20rpx
}

.user-radius {
  background-color: rgb(204, 173, 241);
  width: 130rpx;
  height: 130rpx;
  overflow-x: hidden;
  border-radius: 100%
}

.contact {
  left: 40rpx;
  top: 380rpx;
  width: 680rpx;
  height: 80rpx;
  z-index: 2;
  position: absolute;
  background-color: transparent;
  border: none;
}

.share {
  left: 385rpx;
  top: 225rpx;
  width: 330rpx;
  height: 120rpx;
  z-index: 2;
  position: absolute;
  background-color: transparent;
  border: none;
}

.receive {
  width: 690rpx;
  color: white;
  display: flex;
  justify-content: space-between;
  margin-top: 50rpx;
  text-align: center
}

.receive-btn {
  background-color: rgb(60, 62, 74);
  display: flex;
  padding: 30rpx 50rpx;
  border-radius: 15rpx
}

.receive-text {
  text-align: left;
  margin-left: 20rpx
}

.receive-0 {
  font-size: 28rpx;
  font-weight: 600
}

.receive-1 {
  font-size: 20rpx;
  margin-top: 10rpx
}

.user_information {
  color: white;
  margin-left: 30rpx
}

.user_name {
  font-size: 36rpx;
  font-weight: 600
}

.user_times {
  font-size: 24rpx;
  margin-top: 10rpx
}

.prompt {
  color: white;
  font-size: 50rpx;
  font-weight: 600
}

.prompt-detail {
  font-size: 25rpx;
  text-decoration: underline;
  color: white
}

button::after {
  border: none;
}

.exit-login {
  font-size: 28rpx;
  border-radius: 14rpx;
  margin-top: 80rpx;
  background-color: #af82e6;
  text-align: center;
  padding: 20rpx;
  color: white
}

.login {
  font-size: 28rpx;
  border-radius: 14rpx;
  margin-top: 80rpx;
  width: 450rpx;
  background-color: #aa82e6;
  text-align: center;
  padding: 20rpx;
  color: white
}

.canvas {
  width: 250rpx;
  height: 250rpx;
}
</style>
