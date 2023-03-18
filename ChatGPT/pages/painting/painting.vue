<template>
  <view style="height: 1200rpx">
    <view class="introduce" v-show="canvas">
      <view>
        <img class="logo" src="/static/assets/drawing.svg">
      </view>
      <view class="p-box">
        <view class="p1">欢迎使用ChatGPT绘画能力</view>
        <view class="p2">你可以点击悬浮球切换至必应对话能力</view>
        <view class="p5">我可以帮你绘画 你可以问我</view>
        <view class="p4">一个小男孩在村里跑步</view>
        <view class="p4">一只猫咪正在飞行</view>
        <view class="p4">走廊灯关上 书包放</view>
        <view class="p3">
          <view style="padding-bottom:5rpx;font-size:20rpx">长按悬浮球支持作者</view>
          <text>请注意不支持违法、违规等不当信息内容</text>
        </view>
      </view>
    </view>
    <view class="content" v-show="!canvas">
      <view v-for="(item,index) in data" :key="index">
        <view class="a-content">
          <view class="image-content">
            <view class="image-box">
              <image src="/static/images/user.jpg" class="icon"/>
            </view>
          </view>
          <view class="text">
            {{ item[0].content }}
          </view>
        </view>
        <view class="a-content">
          <view class="image-content">
            <view class="image-box">
              <image src="/static/images/painting.jpg" class="icon"/>
            </view>
          </view>
          <view class="md">
            <view style="height: 60rpx" v-show="!item[1].isShow">
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
                <image :src="item[1].content" style="height: 100%;width: 100%" :show-menu-by-longpress="true"
                       @click="previewImage(item[1].content)"/>
              </view>
              <view v-else style="padding-top: 20rpx">
                <view>{{ item[1].message }}</view>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';
import {GPT_Alpha} from '@/api/gpt'
import {getOpen} from "@/utils/data";

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
      data: [],
      canvas: true,
      replied: false,
      succeed: true
    }
  },
  methods: {
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
            uni.$emit('positioning');
          } else {
            datumElement.message = text
            uni.$emit('positioning');
            //关闭
            clearInterval(timer)
          }
        } catch (e) {
          clearInterval(timer)
          _this.succeed = true
        }
      }, 60);
    },
    async send(input) {
      if (input && !/^\d+$/.test(input) && !/^[^\da-zA-Z\u4e00-\u9fa5]+$/u.test(input)) {
        this.succeed = false
        this.canvas = false
        this.packetStructure(input)
        uni.$emit('positioning');
        //创建模型机构
        let size = this.data.length - 1;

        let datumElement = this.data[size][1];

        datumElement.reply = false
        uni.$emit('positioning');
        try {
          //发送请求
          const text = await GPT_Alpha({
            "prompt": input,
            "openId":getOpen()
          });
          uni.$emit('positioning');
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

    }, delData: function () {
      this.canvas = true
      this.data = []
      this.succeed = true
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

.icon {
  width: 100%;
  height: 100%;
}


.logo {
  width: 100rpx;
  margin-bottom: 0;
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
  color: #3a3a3a;
  word-break: break-all;
  padding-left: 20rpx;
  width: 600rpx;
  font-size: 30rpx;
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

page {
  background-color: white;
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
  font-size: 35rpx;
  margin-top: 10rpx
}

.p2 {
  font-size: 26rpx;
  margin-top: 30rpx
}

.p3 {
  text-align: center;
  font-size: 23rpx;
  margin-top: 270rpx;
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
    background-color: #72c79c;
  }
}

.dot0,
.dot1,
.dot2,
.dot3 {
  background: #417e5c;
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

</style>





