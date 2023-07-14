<template>
    <view>
        <view class="introduce" v-show="canvas">
            <view>
                <img class="logo" :src="require('static/images/img/bot-5.png')" alt="">
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
                    <view v-for="(item,index) in data" :key="index" class="ai-animation">
                        <view class="a-content" @click="copy(item[0].content)">
                            <view class="image-content">
                                <view class="image-box">
                                    <image src="/static/images/img/user.png" class="icon"/>
                                </view>
                            </view>
                            <view class="text" v-show="item[0].isImage">
                                <image :src="item[0].content" class="ai-images" :show-menu-by-longpress="true"/>
                            </view>
                            <view class="text" v-show="!(item[0].isImage)">
                                {{ item[0].content }}
                            </view>
                        </view>
                        <view class="a-content" @click="copy(item[1].content)">
                            <view class="image-content">
                                <view class="image-box">
                                    <image src="/static/images/img/super.png" class="icon"/>
                                </view>
                            </view>
                            <view class="md">
                                <view class="dialogue-height" v-show="!item[1].isShow">
                                    <view v-show="!item[1].reply">
                                        <view :class="{ 'blink': item[1].reply }" class="md-text">{{ text }}</view>
                                    </view>
                                </view>
                                <view v-show="item[1].isShow">
                                    <mp-html :copy-link="true" :tagStyle="md.tagStyle" :markdown="true"
                                             :lazy-load="true"
                                             :selectable="true"
                                             :content="item[1].content"/>
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
                <input placeholder="有问题尽管问我" v-model="input" maxlength="500" class="input" confirm-type="send"
                       @confirm="send"/>
                <image src="/static/assets/image.svg" class="upload" @click="imageRecognition"/>
            </view>
            <view @click="send" class="send-img">
                <image class="send" src="/static/assets/send.svg"/>
            </view>
        </view>

    </view>
</template>

<script>

import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';
import {intensifier} from "@/api/chat";
import env from "@/utils/env";
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
    data() {
        return {
            title: '',
            scrollTop: 0,
            input: "",
            data: [],
            animationData: {},
            chat: [],
            canvas: true,
            text: '|',
            replied: false,
            succeed: true,
            image: ''
        }
    },
    onLoad() {
        const that = this
        const animation = uni.createAnimation({
            duration: 1000,
            timingFunction: 'ease-in-out'
        })
        animation.translate(0, -110).step()
        that.animationData = animation.export()
        setTimeout(function () {
            that.loadTitle()
        }, 1000);
    },
    methods: {
        async imageRecognition() {
            uni.vibrateShort()
            if (getToken() !== "") {
                if (this.succeed) {
                    const that = this
                    uni.chooseImage({
                        count: 1,
                        sizeType: ['compressed'],
                        sourceType: ['album', 'camera'],
                        success: (res) => {
                            this.packetStructure('帮我识别这张图片');
                            this.scrollToBottom()
                            this.succeed = false
                            this.canvas = false
                            let size = this.data.length - 1;
                            let datumElement = this.data[size][1];
                            this.timer = setInterval(() => {
                                datumElement.reply = !this.data[size][1].reply
                            }, 300)
                            const tempFilePaths = res.tempFilePaths;
                            that.logo_list = tempFilePaths[0]
                            uni.uploadFile({
                                url: `${env.baseUrl}` + 'v1/chat/recognition',
                                filePath: tempFilePaths[0],
                                name: 'file',
                                header: {
                                    "token": getToken()
                                },
                                success: (res) => {
                                    console.log(res)
                                    let data = JSON.parse(res.data)
                                    if (data.code !== 200) {
                                        this.chat.splice(this.chat.length - 1, 1)
                                        this.out(data.msg, size)
                                        this.succeed = true
                                        datumElement.isShow = true
                                        datumElement.reply = true
                                        this.beforeDestroy();
                                        return
                                    }
                                    try {
                                        let message = data.data.choices[0].message;
                                        this.chat.push(message)
                                        this.outPutFont(message, size, true, data.data.image)
                                    } catch (e) {
                                        this.chat.splice(this.chat.length - 1, 1)
                                        this.out(e, size)
                                        this.succeed = true
                                    } finally {
                                        datumElement.isShow = true
                                        datumElement.reply = true
                                        this.beforeDestroy();
                                    }
                                }, fail: (res) => {
                                    uni.showToast({
                                        icon: 'none',
                                        duration: 3000,
                                        title: '上传图片失败,请稍后再试'
                                    });
                                    setTimeout(function () {
                                        uni.navigateBack()
                                    }, 1000);

                                }
                            });
                        }
                    });
                } else {
                    uni.showToast({
                        icon: 'none',
                        duration: 3000,
                        title: `请等待SuperBot-4回应~`
                    });
                }
            } else {
                uni.reLaunch({
                    url: '/pages/member/member'
                })
            }
        },
        loadTitle() {
            const _this = this
            const text = '你好 我是SuperNeural 我可以回答你的问题'
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
        copy(data) {
            uni.vibrateShort()
            uni.setClipboardData({
                data: data,
                success: function success(res) {
                    uni.showToast({
                        icon: 'none',
                        duration: 1000,
                        title: '内容已复制到粘贴板'
                    });
                }
            });
        },
        scrollToBottom() {
            uni.createSelectorQuery().select('#scroll-view-content').boundingClientRect((dom) => {
                if (dom.height > 0) this.$set(this, 'scrollTop', dom.height)
            }).exec()
        },
        async send() {
            uni.vibrateShort()
            if (this.succeed) {
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
                    await this.packetStructure(this.input);
                    this.scrollToBottom()
                    this.input = ''
                    let size = this.data.length - 1;
                    let datumElement = this.data[size][1];
                    this.timer = setInterval(() => {
                        datumElement.reply = !this.data[size][1].reply
                    }, 300)
                    try {
                        let text = await intensifier({
                            messages: this.chat
                        })
                        let message = text.choices[0].message;
                        this.chat.push(message)
                        this.outPutFont(message, size, false, '')
                    } catch (e) {
                        this.chat.splice(this.chat.length - 1, 1)
                        this.out(e, size)
                        this.succeed = true
                    } finally {
                        datumElement.isShow = true
                        datumElement.reply = true
                        this.beforeDestroy();
                    }
                } else {
                    uni.showToast({
                        icon: 'none',
                        duration: 3000,
                        title: `回答内容不能为 (空) (纯数字) (纯字符)~`
                    });
                }

            } else {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `请等待SuperBot-4回应~`
                });
            }
        },
        outPutFont: function (message, size, isImage, imageUrl) {
            const _this = this
            const intervals = [20];
            let text = message.content;
            let i = 0;

            function printNextCharacter() {
                try {
                    if (i < text.length) {
                        const interval = intervals[Math.floor(Math.random() * intervals.length)];
                        _this.data[size][1].content = text.slice(0, i++) + '_|';
                        _this.scrollToBottom()
                        setTimeout(() => {
                            printNextCharacter();
                        }, interval);
                    } else {
                        if (isImage) {

                            _this.data[size][0].content = imageUrl
                            _this.data[size][0].isImage = true
                        }
                        _this.data[size][1].content = text;
                        _this.scrollToBottom()
                        _this.succeed = true;
                    }
                } catch (e) {
                    _this.scrollToBottom()
                    _this.succeed = true;
                }
            }

            printNextCharacter();
        },
        beforeDestroy: function () {
            clearInterval(this.timer)
        },
        packetStructure: function (input) {
            this.data.push(
                [
                    {
                        role: "user",
                        content: input,
                        isImage: false
                    },
                    {
                        role: "assistant",
                        content: "",
                        reply: false,
                        isShow: false
                    }
                ]
            )
            this.chat.push({
                role: "user",
                content: input,
            })
        }, out(text, size) {
            const _this = this
            let i = 0;
            let timer = setInterval(function () {
                let datumElement = _this.data[size][1];
                if (i <= text.length) {
                    datumElement.content = text.slice(0, i++) + '_|'
                    _this.scrollToBottom()
                } else {
                    datumElement.content = text
                    _this.scrollToBottom()
                    //关闭
                    clearInterval(timer)
                }
            }, 60);
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

.sc {
  height: 1200rpx;
}

.ai-animation {
  animation: fadeIn 0.5s ease-in-out forwards;
}

.dialogue-height {
  height: 60rpx
}

.upload {
  width: 50rpx;
  height: 50rpx;
  margin-left: 20rpx
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
  background-color: rgb(241, 151, 205);
  padding: 20rpx 30rpx 13rpx;
  border-radius: 15rpx
}

.text {
  color: #f6f6f6;
  margin-left: 10rpx;
  margin-right: 15rpx;
  word-break: break-all;
  padding-left: 17rpx;
  width: 600rpx;
  font-size: 26rpx;
  padding-top: 27rpx;
  border-bottom: 1px solid #e8e8e8;
  padding-bottom: 30rpx
}


.content {
  padding: 30rpx;
  //border: #f1f1f1 solid 2rpx;
  margin: 10rpx 10rpx 50rpx;
  //border-radius: 15rpx
}

.ai-images {
  height: 300rpx;
  width: 100%;
  border-radius: 15rpx
}

.image-content {
  width: 100rpx;
  font-size: 30rpx
}

.image-box {
  background-color: rgb(243, 187, 221);
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

.input {
  background-color: #ffffff;
  color: #696969;
  font-size: 28rpx;
  height: 80rpx;
  width: 450rpx;
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





