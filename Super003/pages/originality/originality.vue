<template>
    <view class="content">
        <view class="dialogue">& 对话示例</view>
        <view class="dialogue-box" :animation="text_ani">
            <view class="template" @click="send('量子计算机在处理特定问题时具有什么能力')">
                <span class="text">量子计算机在处理特定问题时具有什么能力</span>
            </view>
            <view class="template" @click="send('一加一等于多少')">
                <span class="text">一加一等于多少</span>
            </view>
            <view class="template" @click="send('路漫漫其修远兮 吾将上下而求索 是什么意思')">
                <span class="text">路漫漫其修远兮 吾将上下而求索 是什么意思</span>
            </view>
            <view class="dialogue extra">& 更多功能</view>
            <view class="function">
                <scroll-view scroll-y="true" class="feature-set" show-scrollbar="false">
                    <view class="btn-content" @click="toBing">
                        <image src="/static/images/img/bot-2.png" class="ani-img"/>
                        <view class="ani-font">
                            Bing(Beta)
                        </view>
                        <view class="ani-introduce">
                            基于Ai技术的搜索引擎,用于用户进行自然语言交互
                        </view>
                    </view>
                    <view class="btn-content" @click="toErin">
                        <image src="/static/images/img/ernieInfo.png" class="ani-img"/>
                        <view class="ani-font">
                            文心一言(原生)
                        </view>
                        <view class="ani-introduce">
                            百度全新知识增强大语言模型，文心家族的新成员
                        </view>
                    </view>
                    <view class="btn-content" @click="toTranslation">
                        <image src="/static/assets/translation.svg" class="ani-img"/>
                        <view class="ani-font">
                            文本翻译
                        </view>
                        <view class="ani-introduce">
                            Ai将各类难以理解的外语翻译转为汉文
                        </view>
                    </view>
                    <view class="btn-content" @click="toDialogues">
                        <image src="/static/images/img/bot-5.png" class="ani-img"/>
                        <view class="ani-font">
                            SuperNeural
                        </view>
                        <view class="ani-introduce">
                            比以往任何时候都更具创造力和协作性
                        </view>
                    </view>
                    <view class="btn-content" @click="toGenerate">
                        <image src="/static/assets/code.svg" class="ani-img"/>
                        <view class="ani-font">
                            生成代码
                        </view>
                        <view class="ani-introduce">
                            提供想要实现逻辑功能给ai,自动生成code
                        </view>
                    </view>
                    <view class="btn-content" @click="toPortrait">
                        <image src="/static/assets/drawing_.svg" class="ani-img"/>
                        <view class="ani-font">
                            生成图像
                        </view>
                        <view class="ani-introduce">
                            根据用户输入提示信息生成惊艳绝伦的图片
                        </view>
                    </view>
                </scroll-view>
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {
            text_ani: {}
        }
    },
    methods: {
        toTranslation() {
            uni.vibrateShort()
            uni.navigateTo({
                url: '/pages/translation/translation'
            })
            return true
        },
        toGenerate() {
            uni.vibrateShort()
            uni.navigateTo({
                url: '/pages/generate/generate'
            })
            return true
        },
        toDialogues() {
            uni.vibrateShort()
            uni.navigateTo({
                url: '/pages/dialogues/dialogues'
            })
            return true
        },
        toErin() {
            uni.vibrateShort()
            uni.showToast({
                icon: 'none',
                duration: 5000,
                title: '未获得体验权限'
            });
        },
        toPortrait() {
            uni.vibrateShort()
            uni.navigateTo({
                url: '/pages/portrait/portrait'
            })
            return true
        },
        toBing() {
            uni.vibrateShort()
            uni.navigateTo({
                url: '/pages/bing/bing'
            })
            return true
        },
        send: function (data) {
            uni.vibrateShort()
            uni.reLaunch({
                url: '/pages/dialogue/dialogue'
            })
            setTimeout(() => {
                uni.$emit('send', data);
            }, 500)
        }
    },
    onShow() {
        setTimeout(() => {
            const animation = uni.createAnimation({
                duration: 500,
                timingFunction: 'ease',
                delay: 0
            });
            animation.translateX(0).step();
            this.text_ani = animation.export();
            const that = this

        }, 100)

    }
}
</script>

<style lang="scss">

page {
  background-color: #2B2D30;
}

.function {
  width: 690rpx
}

.feature-set {
  height: 740rpx;
  padding-bottom: 20rpx
}

.dialogue {
  font-weight: 500;
  font-size: 45rpx;

}

.dialogue-box {
  font-size: 28rpx;
  position: fixed;
  transform: translateX(-100%);
  transition: transform 0.01s ease-out;
}

.btn-content {
  text-align: center;
  margin: 13rpx;
  width: 260rpx;
  background-color: rgb(60, 62, 74);
  border-radius: 15rpx;
  padding: 20rpx 30rpx;
  display: inline-block
}

.content {
  padding: 4%;
  color: white;
  position: fixed
}

.text {
  border-radius: 15rpx;
  border: 2rpx solid #a2a2a2;
  padding: 10rpx;
  color: #ffffff;
}

.template {
  margin-top: 50rpx;
}

.extra {
  margin-top: 70rpx;
  margin-bottom: 40rpx;
}


.ani-img {
  width: 50rpx;
  height: 50rpx
}

.ani-font {
  font-size: 26rpx
}

.ani-introduce {
  font-size: 23rpx;
  margin-top: 8rpx;
  color: #777777
}
</style>
