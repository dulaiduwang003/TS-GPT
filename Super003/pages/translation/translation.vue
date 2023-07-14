<template>
  <view style="padding: 4%">
    <view v-show="userShow">
      <view class="lang-btn">
        <view class="lang-box">
          <picker @change="langBtn" :value="index" :range="lang">
            <view style="font-size: 30rpx">{{ lang[index] }}</view>
          </picker>
        </view>
        <view class="lang-box">
          <picker @change="_langBtn" :value="index_" :range="lang_">
            <view style="font-size: 30rpx">{{ lang_[index_] }}</view>
          </picker>
        </view>
      </view>
      <view class="input-box">
        <textarea placeholder="请输入...." class="input" v-model="input" maxlength="-1" confirm-type="send"  @confirm="translation"/>
      </view>
      <view class="prompt">翻译结果由SuperBot Ai提供支持</view>
      <view class="t-btn-box">
        <button class="t-btn" @click="translation">翻译</button>
      </view>
    </view>
    <view v-show="resultShow" style="animation: fadeIn 0.5s ease-in-out forwards;">
      <view class="user-font">{{ input }}
      </view>
      <scroll-view scroll-y="true">
        <view class="input-box" style="color: white;font-size: 40rpx;height: 600rpx;border-radius: 20rpx">
          {{ result }}
        </view>
      </scroll-view>
      <view class="prompt">翻译结果由SuperBot Ai提供支持</view>
      <view style="margin-top: 60rpx;display: flex;text-align: center;">
        <button @click="goOn"
                class="c-btn">继续
        </button>
        <button @click="copy"
                class="c-btn">复制
        </button>
      </view>
    </view>
    <view style="height: 100%;text-align: center;color: white;font-size: 35rpx" v-show="loading">
      <canvas id="loading" type="2d" class="canvas"/>
    </view>
  </view>
</template>

<script>
import {translation} from "@/api/chat";
import lottie from 'lottie-miniprogram';

let ani = null; // 必须
export default {
  data() {
    return {
      lang: ['中文', '英语', '意大利语', '德语', '韩语', '日语', '越南语', '拉丁语', '法语', '俄语', '乌克兰语'],
      lang_: ['英语', '中文', '意大利语', '德语', '韩语', '日语', '越南语', '拉丁语', '法语', '俄语', '乌克兰语'],
      index: 0,
      index_: 0,
      input: '',
      resultShow: false,
      userShow: true,
      loading: false,
      result: ''
    }
  },
  created() {
    this.renderLottie("#loading", "Loading6.json")
  },
  methods: {
    goOn: function () {
      this.input=''
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
          title: `翻译内容不能为空 纯数字~`
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
      const p = '将这句话翻译为' + this.lang_[this.index_] + ":" + this.input
      try {
        const text = await translation({
          "messages": [{
            role: "user",
            content: p,
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