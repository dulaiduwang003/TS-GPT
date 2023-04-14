<template>
    <view style="padding: 4%">
        <view v-show="userShow">
            <view style="font-size: 40rpx;color: white;font-weight: 500">描述您的图像生成效果</view>
            <view class="input-box">
                <textarea placeholder="特朗普在工地工作" class="input" v-model="input" maxlength="30"
                          confirm-type="send" @confirm="translation"/>
            </view>
            <view class="prompt">生成结果由SuperBot Ai提供支持</view>
            <view class="t-btn-box">
                <button class="t-btn" @click="translation">生成</button>
            </view>
        </view>
        <view v-show="resultShow" class="super-animation">
            <view class="user-font">{{ input }}
            </view>
            <image style="width: 100%;border-radius: 15rpx" :src="result" mode="widthFix"
                   :show-menu-by-longpress="true"/>
            <view class="prompt">生成结果由SuperBot Ai提供支持</view>
            <view style="margin-top: 60rpx;display: flex;text-align: center;">
                <button @click="goOn"
                        class="c-btn">继续
                </button>
            </view>
        </view>
        <view style="height: 100%;text-align: center;color: white;font-size: 35rpx" v-show="loading">
            <canvas id="loading" type="2d" class="canvas"/>
        </view>
    </view>
</template>

<script>
import {face, chat} from "@/api/chat";
import lottie from 'lottie-miniprogram';
import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';

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
        renderLottie(id, path) {
            this.createSelectorQuery()
                .select(id)
                .node(res => {
                    const canvas = res.node;
                    const context = canvas.getContext('2d');
                    canvas.width = 300;
                    canvas.height = 300;
                    lottie.setup(canvas);
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
                    title: `内容不能为空不能为纯数字~`
                });
                return
            }
            this.userShow = false
            this.resultShow = false
            this.loading = true
            try {
                const text = await face({
                    "prompt": this.input
                });
                this.result = "data:image/png;base64," + text.images[0];
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


        }
    }
}
</script>

<style lang="scss">
.c-btn {
  width: 200rpx;
  color: white;
  border-radius: 30rpx;
  background-color: #aa82e6;
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
  height: 200rpx;
  margin-top: 20rpx;
  background-color: #181818;
  padding: 2%;
  border-radius: 15rpx
}

.super-animation {
  animation: fadeIn 0.5s ease-in-out forwards;
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

.canvas {
  margin-top: 380rpx;
  margin-left: 150rpx;
  height: 400rpx;
  width: 400rpx;
}
</style>
