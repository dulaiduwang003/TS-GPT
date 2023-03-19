<template>
  <view style="height: 1200rpx">
    <view class="introduce" v-show="canvas">
      <view>
        <view>
          <img class="logo" src="/static/assets/bing.svg">
        </view>
        <view class="p-box">
          <view class="p1">欢迎使用微软新必应对话能力</view>
          <view class="p2">你可以点击悬浮球切换至ChatGPT对话能力</view>
          <view class="p5">有什么不懂尽管问我</view>
          <view class="p4">你和ChatGPT有什么区别</view>
          <view class="p4">美国现任总统是谁</view>
          <view class="p4">古巴比利王是谁</view>
          <view class="p3">
            <view style="padding-bottom:5rpx;font-size:20rpx">长按悬浮球支持作者</view>
            <text>请注意不支持违法、违规等不当信息内容</text>
          </view>
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
              <image src="/static/images/bing.jpg" class="icon"/>
            </view>
          </view>
          <view class="md">
            <view style="height: 60rpx" v-show="!item[1].isShow">
              <view v-show="!item[1].reply">
                <view :class="{ 'blink': item[1].reply }" class="md-text">{{ text }}</view>
              </view>
            </view>
            <view v-show="item[1].isShow" style="padding-top: 20rpx">
              <text>{{ item[1].content }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>

import {GPT_Bing} from '@/api/gpt'
import {getOpen} from "@/utils/data";


export default {


  data() {
    return {
      data: [],
      canvas: true,
      text: '|',
      replied: false,
      succeed: true,
    }
  },
  methods: {
    async send(input) {
      if (this.succeed) {
        //校验
        if (input && !/^\d+$/.test(input) && !/^[^\da-zA-Z\u4e00-\u9fa5]+$/u.test(input)) {
          this.succeed = false
          this.canvas = false
          this.packetStructure(input);
          let size = this.data.length - 1;
          let datumElement = this.data[size][1];
          this.timer = setInterval(() => {
            datumElement.reply = !this.data[size][1].reply
          }, 300)
          try {
            let message = await GPT_Bing({"param": input, "openId": getOpen()});
            let s = String(message);
            s = s.slice(1, -1);

            this.outPutFont(s, size)
          } catch (e) {
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
          title: `请等待Bing的回应~`
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
              isShow: false
            }
          ]
      )
    },
    delData: function () {
      this.canvas = true
      this.data = []
      this.succeed = true
    },
    outPutFont: function (message, size) {
      const _this = this
      try {
        const intervals = [100, 13, 10, 12, 22, 13, 12, 200, 10, 32, 6, 471, 2, 11];
        let text = message;
        let i = 0;

        function printNextCharacter() {
          if (i < text.length) {
            const interval = intervals[Math.floor(Math.random() * intervals.length)];
            _this.data[size][1].content = text.slice(0, i++) + '_|';
            uni.$emit('positioning');
            setTimeout(() => {
              printNextCharacter();
            }, interval);
          } else {
            _this.data[size][1].content = text;
            _this.succeed = true;
          }
        }

        printNextCharacter();
      } catch (e) {
        _this.succeed = true;
      }

    },
    beforeDestroy: function () {
      clearInterval(this.timer)
    }
    , out(text, size) {
      let timer;
      try {
        const _this = this
        //模拟打印字效果
        let i = 0;
        timer = setInterval(function () {
          let datumElement = _this.data[size][1];
          if (i <= text.length) {
            datumElement.content = text.slice(0, i++) + '_|'
            uni.$emit('positioning');
          } else {
            datumElement.content = text
            uni.$emit('positioning');
            //关闭
            clearInterval(timer)
          }
        }, 60);
      } catch (e) {
        clearInterval(timer)
      }
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


.text {
  color: #3a3a3a;
  word-break: break-all;
  padding-left: 17rpx;
  width: 600rpx;
  font-size: 30rpx;
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
  overflow: hidden;
  border-radius: 100%;
  width: 70rpx;
  height: 70rpx;
}

page {
  background-color: white;
}

.blink {
  animation: blinker 1s step-end infinite;
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


@keyframes blinker {
  0% {
    opacity: 0;
  }
}
</style>





