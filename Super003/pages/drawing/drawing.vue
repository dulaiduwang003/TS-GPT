<template>
    <view>
        <view class="introduce" v-show="canvas">
            <view>
                <img class="logo" :src="require('static/images/img/bot-3.png')">
                <view class="p-box">
                    <view class="p1">{{ title }}</view>
                    <view class="p3">
                        <text>违反微信社区规定发言将会被限制对话</text>
                    </view>
                </view>
            </view>
        </view>
        <view class="content" v-show="!canvas">
            <scroll-view scroll-y="true" :scroll-top="scrollTop" class="sc">
                <view id="scroll-view-content">
                    <view v-for="(item,index) in data" :key="index"
                          style="  animation: fadeIn 0.5s ease-in-out forwards;">
                        <view class="a-content">
                            <view class="image-content">
                                <view class="image-box">
                                    <image src="/static/images/img/user.png" class="icon"/>
                                </view>
                            </view>
                            <view class="text">
                                {{ item[0].content }}
                            </view>
                        </view>
                        <view class="a-content">
                            <view class="image-content">
                                <view class="image-box">
                                    <image src="/static/images/img/painting.png" class="icon"/>
                                </view>
                            </view>
                            <view class="md">
                                <view class="md-html" v-show="!item[1].isShow">
                                    <view v-show="!item[1].reply">
                                        <view class="loading">
                                            <view class="dot0"></view>
                                            <view class="dot1"></view>
                                            <view class="dot2"></view>
                                            <view class="dot3"></view>
                                            <view class="dot4"></view>
                                        </view>
                                    </view>
                                </view>
                                <view v-show="item[1].isShow">
                                    <view class="img-cent" v-if="!(item[1].abnormal)">
                                        <view class="ai-reply">为您生成
                                            {{ prop }}
                                        </view>
                                        <image :src="item[1].content" class="ai-images" :show-menu-by-longpress="true"
                                               @click="previewImage(item[1].content)"/>
                                    </view>
                                    <view v-else class="reply-data">
                                        <view>{{ item[1].message }}</view>
                                    </view>
                                </view>
                            </view>
                        </view>
                    </view>
                </view>
            </scroll-view>
        </view>
        <view :animation="animationData ? animationData : ''"
              class="input-box ">
            <view class="pro-wx">
                <input placeholder="请告诉我创意词" v-model="input" maxlength="50" class="input" confirm-type="send"
                       @confirm="send"
                       v-show="isLogin"/>
                <input placeholder="请先登录" maxlength="300" class="input" disabled
                       v-show="!isLogin" @click="toMember"/>
            </view>
            <view @click="send" class="send-img" v-show="isLogin">
                <image class="send" src="/static/assets/send.svg"/>
            </view>
            <view @click="toMember" class="send-img" v-show="!isLogin">
                <image class="send" src="/static/assets/send.svg"/>
            </view>
        </view>
    </view>
</template>

<script>
import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';
import {generations} from "@/api/chat";
import {getToken} from "@/utils/data";

export default {
    computed: {
        md() {
            return md
        }
    },
    components: {
        mpHtml
    },
    onShow() {
        this.isLogin = getToken() !== "";
    },
    data() {
        return {
            isLogin: false,
            scrollTop: 0,
            input: '',
            data: [],
            animationData: {},
            canvas: true,
            text: '|',
            replied: false,
            succeed: true,
            prop: '',
            title: '',
        }
    },
    created() {
        const that = this
        const animation = uni.createAnimation({
            duration: 1000,
            timingFunction: 'ease-in-out'
        })
        animation.translate(0, -95).step()
        that.animationData = animation.export()
        setTimeout(function () {
            that.loadTitle()
        }, 1000);
    },
    methods: {
        loadTitle() {
            const _this = this
            const text = '你好 我是ConcatAi 我可以帮你生成创意'
            let i = 0;
            let timer = setInterval(function () {
                try {
                    if (i <= _this.title.length) {
                        _this.title = text.slice(0, i++) + '|'
                    } else {
                        _this.title = text
                        //关闭
                        clearInterval(timer)
                    }
                } catch (e) {
                    clearInterval(timer)
                }
            }, 60);
        },
        scrollToBottom() {
            uni.createSelectorQuery().select('#scroll-view-content').boundingClientRect((dom) => {
                if (dom.height > 0) this.$set(this, 'scrollTop', dom.height)
            }).exec()
        },
        toMember: function () {
            uni.vibrateShort()
            uni.reLaunch({
                url: '/pages/member/member'
            })
        },
        previewImage(url) {
            uni.previewImage({
                urls: [url]
            })
        },
        outPutFont(text, size) {
            const _this = this
            //模拟打印字效果
            let i = 0;
            let timer = setInterval(function () {
                let datumElement = _this.data[size][1];
                try {
                    if (i <= text.length) {
                        datumElement.message = text.slice(0, i++) + '_|'
                        _this.scrollToBottom()
                    } else {
                        datumElement.message = text
                        _this.scrollToBottom()
                        //关闭
                        clearInterval(timer)
                    }
                } catch (e) {
                    _this.scrollToBottom()
                    clearInterval(timer)
                    _this.succeed = true
                }
            }, 60);
        },
        async send() {
            uni.vibrateShort()
            if (this.input && !/^\d+$/.test(this.input) && !/^[^\da-zA-Z\u4e00-\u9fa5]+$/u.test(this.input)) {
                const filteredStr = this.input.replace(/[\s!@#$%^&*()_+\-=\[\]{};':"\\|,.<>\/?]/g, '');
                const regex = /(openai|gpt)/i;
                if (regex.test(filteredStr)) {
                    uni.showToast({
                        icon: 'none',
                        duration: 3000,
                        title: `请勿发布违反微信社区的发言`
                    });
                    return
                }
                this.succeed = false
                this.canvas = false
                await this.packetStructure(this.input)
                this.scrollToBottom()
                const param = this.input
                this.input = ''
                //创建模型机构
                let size = this.data.length - 1;

                let datumElement = this.data[size][1];

                datumElement.reply = false
                try {
                    //发送请求
                    const text = await generations({
                        "prompt": param
                    });
                    this.prop = param
                    this.data[size][1].content = text.data[0].url
                } catch (e) {
                    //当前坐标
                    this.data[size][1].abnormal = true
                    this.outPutFont(e, size)
                    datumElement.isShow = true
                    datumElement.reply = true
                } finally {
                    const that = this
                    setTimeout(() => {
                        that.succeed = true
                        datumElement.isShow = true
                        datumElement.reply = true
                    }, 1000);

                }
            } else {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `回答内容不能为 (空) (纯数字) (纯字符)~`
                });
            }

        },
        packetStructure: function (input) {
            this.data.push(
                [
                    {
                        role: "user",
                        content: input,
                    },
                    {
                        role: "assistant",
                        content: "",
                        reply: false,
                        isShow: false,
                        abnormal: false,
                        message: "",
                    }
                ]
            )
        }
    }
}
</script>

<style lang="scss">
.loading {
  display: flex;
  padding-top: 25rpx;
  padding-bottom: 25rpx
}

.send {
  width: 40rpx;
  height: 40rpx;
}

.icon {
  width: 100%;
  height: 100%;
}

.pro-wx {
  width: 540rpx;
  display: flex;
  justify-items: center;
  align-items: center;
  justify-content: space-between;
  margin-left: 30rpx;
  margin-right: 5rpx;
  background-color: white;
  border-radius: 15rpx;
  padding-left: 40rpx;
  padding-right: 40rpx;
}

.ai-images {
  height: 100%;
  width: 100%;
  border-radius: 15rpx
}

.logo {
  animation: beating 0.7s infinite alternate;
  margin-top: 200rpx;
  width: 140rpx;
  height: 140rpx;
}

@keyframes beating {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-10px);
  }
}

.md {
  padding-left: 10rpx;
  width: 540rpx;
  border-bottom: 1px solid #efefef;
  padding-bottom: 30rpx;
}

.a-content {
  margin-top: 30rpx;
  margin-bottom: 30px;
  display: flex;
}

.send-img {
  margin-left: 15rpx;
  background-color: rgb(81, 126, 231);
  padding: 20rpx 30rpx 13rpx;
  border-radius: 15rpx
}


.ai-reply {
  color: white;
  font-size: 26rpx;
  padding-bottom: 20rpx;
  padding-top: 20rpx
}

.sc {
  height: 1100rpx;
}

.md-html {
  height: 60rpx
}

.reply-data {
  padding-top: 20rpx;
  color: white;
  font-size: 26rpx;
}

.content {
  padding: 30rpx;
  //border: #f1f1f1 solid 2rpx;
  margin: 10rpx 10rpx 50rpx;
  //border-radius: 15rpx
}

.image-content {
  width: 100rpx;
  font-size: 30rpx
}

.image-box {
  background-color: rgb(141, 172, 243);
  overflow: hidden;
  border-radius: 100%;
  width: 70rpx;
  height: 70rpx;
}

page {
  background-color: #2B2D30;
}

.blink {
  animation: blinker 1s step-end infinite;
}

.introduce {
  transform: scale(0.1);
  animation: zoomIn 0.5s ease-in-out forwards;
  text-align: center;
  margin-top: 80rpx;
}

.md-text {
  color: white;
  padding-top: 15rpx;
  font-weight: 1500;
}

.input-box {
  position: fixed;
  align-items: center;
  bottom: -150rpx;
  display: flex;
  justify-content: space-between;
  width: 725rpx;

}

.loading {
  display: flex;
  padding-top: 25rpx;
  padding-bottom: 25rpx
}

.icon {
  width: 100%;
  height: 100%;
}


.md {
  width: 540rpx;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 30rpx;
}

.a-content {
  margin-top: 30rpx;
  margin-bottom: 30px;
  display: flex;
}


.text {
  color: #f6f6f6;
  word-break: break-all;
  padding-left: 20rpx;
  width: 600rpx;
  font-size: 26rpx;
  padding-top: 27rpx;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 30rpx;
}


.content {
  //height: 900rpx;
  padding: 30rpx;
  //border: #f1f1f1 solid 2rpx;
  margin: 10rpx;
  //border-radius: 15rpx
}

.image-content {
  width: 100rpx;
  font-size: 30rpx
}

.image-box {
  overflow: hidden;
  border-radius: 100%;
  width: 70rpx;
  height: 70rpx;
}


.introduce {
  text-align: center;
  margin-top: 80rpx;
}

.md-text {
  padding-top: 15rpx;
  font-weight: 1500;
}


.p1 {
  padding-left: 160rpx;
  line-height: 80rpx;
  padding-right: 160rpx;
  text-align: center;
  font-size: 35rpx;
  margin-top: 80rpx
}


.p3 {
  text-align: center;
  font-size: 23rpx;
  margin-top: 380rpx;
  color: #6a6a6a
}

.img-cent {
  height: 560rpx;
  width: 560rpx;
  border-radius: 10rpx;
  overflow: hidden

}


@keyframes jump {
  0%,
  80%,
  100% {
    transform: scale(0);
    background-color: #F9F9F9;
  }
  40% {
    transform: scale(1.0);
    background-color: rgb(81, 126, 231);
  }
}

.dot0,
.dot1,
.dot2,
.dot3 {
  background: rgb(66, 107, 204);
  width: 30rpx;
  height: 30rpx;
  border-color: #464646;
  border-radius: 50%;
}

.dot0 {
  animation: jump 1.3s -0.64s linear infinite;
}

.dot1 {
  animation: jump 1.3s -0.32s linear infinite;
}

.dot2 {
  animation: jump 1.3s -0.16s linear infinite;
}

.dot3 {
  animation: jump 1.3s linear infinite;
}

.input {
  background-color: white;
  color: #696969;
  font-size: 28rpx;
  height: 80rpx;
  width: 500rpx;
}



.p3 {
  text-align: center;
  font-size: 23rpx;
  margin-top: 380rpx;
  color: #6a6a6a
}


.p-box {
  text-align: center;
  color: #ffffff;
  margin-top: 30rpx;
}


@keyframes zoomIn {
  0% {
    transform: scale(0.1);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes blinker {
  0% {
    opacity: 0;
  }
}
</style>





