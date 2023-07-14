<template>
    <view class="content">
        <view>
            <view class="title">
                后端服务器代理配置
            </view>
            <view class="straddle">
                <input placeholder="代理ip" maxlength="-1" class="input" v-model="data.ip"/>
                <input placeholder="代理端口" maxlength="-1" class="input" v-model="data.port"/>
            </view>
            <view class="title">
                Linear Regression 算法密钥
            </view>
            <view class="straddle">
                <input placeholder="密钥" maxlength="-1" class="input" v-model="data.openKey"/>
            </view>
            <view class="title">
                Standard 配置
            </view>
            <view class="straddle">
                <input placeholder="接口链接" maxlength="-1" class="input"
                       v-model="data.thirdPartyUrl"/>
                <input placeholder="认证密钥" maxlength="-1" class="input"
                       v-model="data.thirdPartyKey"/>
            </view>
            <view class="title">
                Nerve Algorithm 配置
            </view>
            <view class="straddle">
                <input placeholder="接口链接" maxlength="-1" class="input"
                       v-model="data.gpt4Url"/>
                <input placeholder="认证密钥" maxlength="-1" class="input"
                       v-model="data.gpt4Key"/>
            </view>
            <view class="title">
                Bing Algorithm 配置
            </view>
            <view class="straddle">
                <textarea placeholder="认证参数" maxlength="-1" class="input bing"
                          style="" v-model="data.bingCookie"/>
            </view>
            <view class="title">
                Face Recognition 配置
            </view>
            <view class="straddle">
                <input placeholder="接口连接" maxlength="-1" class="input"
                       v-model="data.sdUrl"/>
            </view>
        </view>
        <button class="btn" @click="apple">
            刷新
        </button>
    </view>
</template>

<script>
import {getConfig, putConfig} from "@/api/server";

export default {
    data() {
        return {
            data: {
                ip: '',
                port: '',
                openKey: '',
                gpt4Url: '',
                gpt4Key: '',
                bingCookie: '',
                sdUrl: '',
                thirdPartyUrl: '',
                thirdPartyKey: ''
            }
        }
    },
    onShow() {
        this.init()
    },
    methods: {
        async apple() {
            uni.vibrateShort()
            try {
                try {
                    await putConfig({
                        "choose": 1,
                        "proxy": {
                            "ip": this.data.ip,
                            "port": this.data.port
                        },
                        "thirdParty": {
                            "baseUrl": this.data.thirdPartyUrl,
                            "token": this.data.thirdPartyKey
                        },
                        "chatGpt4": {
                            "baseUrl": this.data.gpt4Url,
                            "key": this.data.gpt4Key
                        },
                        "sdUrl": this.data.sdUrl,
                        "cookie": this.data.bingCookie,
                        "key": this.data.openKey
                    })
                } catch (e) {
                    console.log("未配置参数")
                }
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: '应用成功 服务器策略已重置为直连模式'
                });
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: e
                });
            }
        },
        async init() {
            try {
                let data = await getConfig();
                this.data.ip = data.proxy.ip
                this.data.port = data.proxy.port
                this.data.openKey = data.key
                this.data.gpt4Url = data.chatGpt4.baseUrl
                this.data.gpt4Key = data.chatGpt4.key
                this.data.bingCookie = data.cookie
                this.data.sdUrl = data.sdUrl
                this.data.thirdPartyUrl = data.thirdParty.baseUrl
                this.data.thirdPartyKey = data.thirdParty.token
            } catch (e) {
                console.log('未配置参数')
            }
        }
    }
}
</script>

<style lang="scss">
page {
  background-color: #2B2D30;
}

.btn {
  width: 580rpx;
  height: 80rpx;
  background-color: rgb(115, 91, 207);
  color: white;
  font-size: 28rpx;
  margin-top: 70rpx
}

.title {
  color: #b2b2b2;
  font-size: 28rpx;
  font-weight: 500;
}

.content {
  padding: 4%
}

.straddle {
  margin-bottom: 50rpx
}

.bing {
  padding: 10rpx;
  height: 100rpx;
  width: 97%
}

.input {
  font-size: 25rpx;
  margin-top: 30rpx;
  height: 50rpx;
  padding: 10rpx;
  color: #a1a1a1;
  background-color: #383838;
  border-radius: 15rpx
}
</style>
