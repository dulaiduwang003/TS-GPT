<template>
    <view class="content">
        <view v-show="userShow">
            <view class="title">提供您的代码需求</view>
            <view class="input-box">
                <textarea confirm-type="send" @confirm="translation" placeholder="如何在SpringBoot中实现Redis锁"
                          class="input" v-model="input" maxlength="-1"/>
            </view>
            <view class="prompt">代码生成结果由SuperBot Ai提供支持</view>
            <view class="t-btn-box">
                <button class="t-btn" @click="translation">生成</button>
            </view>
        </view>
        <view v-show="resultShow" class="ai-animation">
            <view class="content-format">
                <scroll-view scroll-y="true" class="input-box">
                    <view>
                        <mp-html :copy-link="true" :tagStyle="md.tagStyle" :markdown="true" :lazy-load="true"
                                 :selectable="true"
                                 :content="result"/>
                    </view>
                </scroll-view>
            </view>
            <view class="prompt">生成结果由SuperBot Ai提供支持</view>
            <view class="button-style">
                <button @click="goOn"
                        class="c-btn">继续
                </button>
                <button @click="copy"
                        class="c-btn">复制
                </button>
            </view>
        </view>
        <view class="loading-style" v-show="loading">
            <canvas id="loading" type="2d" class="canvas"/>
        </view>
    </view>
</template>

<script>
import {code, subscribeChatData} from "@/api/chat";
import lottie from 'lottie-miniprogram';
import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';
import {getToken} from "@/utils/data";

let ani = null; // 必须
export default {
    computed: {
        md() {
            return md
        }
    },
    components: {
        mpHtml,
    },
    data() {
        return {
            input: '',
            resultShow: false,
            userShow: true,
            loading: false,
            result: '',
            chatData: [],
            reader: null
        }
    },
    created() {
        this.renderLottie("#loading", "Loading6.json")
    },
    methods: {

        goOn: function () {
            this.input = ''
            this.result = ''
            this.loading = false
            this.resultShow = false
            this.userShow = true
        },
        copy: function () {
            uni.vibrateShort()
            uni.setClipboardData({
                data: this.result,
                success: res => {
                    uni.showToast({
                        icon: 'none',
                        duration: 1000,
                        title: '已复制到粘贴板'
                    });
                }
            });
        },
        renderLottie(id, path) {
            this.createSelectorQuery()
                .select(id)
                .node(res => {
                    const canvas = res.node;
                    const context = canvas.getContext('2d');
                    canvas.width = 300;
                    canvas.height = 300;
                    lottie.setup(canvas);
                    const paths = path
                    ani = lottie.loadAnimation({
                        loop: true,
                        autoplay: true,
                        animationData: require("../../static/json/" + path),
                        rendererSettings: {
                            context
                        },
                    });
                }).exec();
        },
        async translation() {
            uni.vibrateShort()
            if (!(this.input && !/^\d+$/.test(this.input) && !/^[^\da-zA-Z\u4e00-\u9fa5]+$/u.test(this.input))) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `生成内容不能为空且不能为纯数字~`
                });
                return
            }
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
            this.userShow = false
            this.resultShow = false
            this.loading = true
            try {
                const text = await code({
                    "messages": [{
                        role: "user",
                        content: this.input,
                    }]
                });
                this.result = text.choices[0].message.content;
                this.loading = false
                this.resultShow = true
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: e
                });
                setTimeout(function () {
                    uni.navigateBack()
                }, 2000);
            }


        },
        _langBtn: function (e) {
            this.index_ = e.detail.value
        },
        langBtn: function (e) {
            this.index = e.detail.value
        },
    }
}
</script>

<style lang="scss">
.c-btn {
  width: 200rpx;
  color: white;
  border-radius: 30rpx;
  background-color: #af82e6;
  font-size: 26rpx
}

.lang-btn {
  display: flex;
  justify-content: space-between
}

.user-font {
  font-size: 50rpx;
  font-weight: 600;
  color: white;
  overflow: hidden;
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
}

.lang-box {
  width: 275rpx;
  background-color: #181818;
  text-align: center;
  color: #ffffff;
  font-size: 40rpx;
  padding: 30rpx
}

.input-box {
  height: 300rpx;
  margin-top: 20rpx;
  background-color: #181818;
  padding: 2%;
  border-radius: 15rpx
}

.input {
  color: white;
  width: 100%
}

.prompt {
  text-align: right;
  color: #a1a1a1;
  margin-top: 15rpx;
  font-size: 23rpx
}

.content {
  padding: 4%
}

.title {
  font-size: 40rpx;
  color: white;
  font-weight: 500
}

.ai-animation {
  animation: fadeIn 0.5s ease-in-out forwards;
}

.content-format {
  padding-right: 20rpx
}

.button-style {
  margin-top: 60rpx;
  display: flex;
  text-align: center;
}

.loading-style {
  height: 100%;
  text-align: center;
  color: white;
  font-size: 35rpx
}

.t-btn {
  width: 400rpx;
  background-color: #aa82e6;
  color: white;
  border-radius: 30rpx;
  font-size: 30rpx
}

.t-btn-box {
  text-align: center;
  margin-top: 60rpx
}

page {
  background-color: #2B2D30;
}

.canvas {
  margin-top: 380rpx;
  margin-left: 150rpx;
  height: 400rpx;
  width: 400rpx;
}
</style>
