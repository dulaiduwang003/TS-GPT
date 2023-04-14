<template>
    <view>
        <uni-popup ref="panel">
            <panel ref="console"></panel>
        </uni-popup>
        <view class="introduce" v-show="canvas">
            <view>
                <img class="logo" :src="require('static/images/img/bot-4.png')">
                <view class="p-box">
                    <view class="p1">{{ title }}</view>
                    <view class="p3">
                        <text>违反微信社区规定发言将会被限制对话</text>
                    </view>
                </view>
            </view>
        </view>
        <view class="content" v-show="!canvas">
            <scroll-view scroll-y="true" :scroll-top="scrollTop" style="height: 1100rpx;">
                <view id="scroll-view-content" style="padding-bottom: 50rpx;">
                    <view v-for="(item,index) in data" :key="index" class="ai-animation">
                        <view class="a-content" @click="copy(item[0].content)">
                            <view class="image-content">
                                <view class="image-box">
                                    <image src="/static/images/img/user.png" class="icon"/>
                                </view>
                            </view>
                            <view class="text">
                                {{ item[0].content }}
                            </view>
                        </view>
                        <view class="a-content" @click="copy(item[1].content)">
                            <view class="image-content">
                                <view class="image-box">
                                    <image src="/static/images/img/gpt.png" class="icon"/>
                                </view>
                            </view>
                            <view class="md">
                                <view style="height: 60rpx" v-show="!item[1].isShow">
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
                       v-show="isLogin" @confirm="send"/>
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
        <touch ref="touch"/>
    </view>
</template>

<script>
import touch from '@/pages/touch/touch.vue'
import panel from "@/pages/panel/panel.vue";
import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';
import {chat} from "@/api/chat";
import {getData, getToken, setData} from "@/utils/data";

let tabTitle = ''
export default {
    computed: {
        md() {
            return md
        }
    },
    components: {
        available: 0,
        mpHtml,
        touch,
        panel
    },
    data() {
        return {
            scrollTop: 0,
            input: "",
            data: [],
            animationData: {},
            chat: [],
            canvas: true,
            text: '|',
            replied: false,
            succeed: true,
            isLogin: false,
            title: ''
        }
    },
    onShow() {
        if (getToken() !== "") {
            this.tabs = getData();
            for (let i = 0; i < this.tabs.length; i++) {
                if (this.tabs[i].default) {
                    this.canvas = !(this.tabs[i].data.length > 0)
                    this.chat = this.tabs[i].chat
                    this.data = this.tabs[i].data
                    break
                }
            }
            this.isLogin = true
        } else {
            this.isLogin = false
        }
    },
    onLoad() {
        const that = this


        const animation = uni.createAnimation({
            duration: 1000,
            timingFunction: 'ease-in-out'
        })
        animation.translate(0, -95).step()
        that.animationData = animation.export()
        uni.$on('openPanel', function () {
            that.openPanel()
        });

        uni.$on('changeCache', function (data) {
            if (data !== undefined) {
                that.changeCache(data.chat, data.data)
            } else {
                that.changeCache([], [])
            }
        });
        uni.$on('send', function (data) {
            that.input = data
            that.send()
        });
        setTimeout(function () {
            that.loadTitle()
        }, 1000);
    },
    methods: {
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
        changeCache: function (chat, data) {
            this.chat = chat;
            this.data = data;
            this.canvas = data.length <= 0;
        },
        toMember: function () {
            uni.vibrateShort()
            uni.reLaunch({
                url: '/pages/member/member'
            })
        },
        openPanel: function () {
            uni.vibrateShort()
            this.$refs.panel.open("center")
        },
        async send() {
            uni.vibrateShort()
            if (this.succeed) {
                //校验
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
                    this.$refs.touch.succeed = false
                    this.canvas = false
                    await this.packetStructure(this.input);
                    this.scrollToBottom()
                    tabTitle = this.input.substring(0, 7) + "..."
                    this.input = ''
                    let size = this.data.length - 1;
                    let datumElement = this.data[size][1];
                    this.timer = setInterval(() => {
                        datumElement.reply = !this.data[size][1].reply
                    }, 300)
                    try {
                        let text = await chat({
                            messages: this.chat
                        })
                        let message = text.choices[0].message;
                        this.chat.push(message)
                        this.outPutFont(message, size)
                    } catch (e) {
                        this.chat.splice(this.chat.length - 1, 1)
                        this.out(e, size)
                        this.succeed = true
                        this.$refs.touch.succeed = true
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
                    title: `请等待SuperBot回应~`
                });
            }
        },
        outPutFont: function (message, size) {
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
                        _this.data[size][1].content = text;
                        _this.scrollToBottom()
                        let tabs = _this.$refs.console.tabs;
                        let current = _this.$refs.console.current;
                        tabs[current].chat = _this.chat
                        tabs[current].title = tabTitle
                        tabs[current].data = _this.data
                        setData(tabs)
                        _this.succeed = true;
                        _this.$refs.touch.succeed = true
                    }
                } catch (e) {
                    _this.scrollToBottom()
                    _this.$refs.touch.succeed = true
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
        }, loadTitle() {
            const _this = this
            const text = '你好 我是StanAi 我可以回答你的任何问题'
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

.ai-animation {
  animation: fadeIn 0.5s ease-in-out forwards;
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
  animation: jump 0.7s infinite alternate;
  margin-top: 200rpx;
  width: 140rpx;
  height: 140rpx;

}

@keyframes jump {
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
  background-color: rgb(128, 101, 231);
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

.image-content {
  width: 100rpx;
  font-size: 30rpx
}

.image-box {
  background-color: rgb(212, 194, 245);
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
  background-color: white;
  color: #696969;
  font-size: 28rpx;
  height: 80rpx;
  width: 500rpx;
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





