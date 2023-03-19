<template>
  <view>
    <view class="introduce" v-show="canvas">
      <view>
        <view>
          <img class="logo" src="/static/assets/ai.svg">
        </view>
        <view class="p-box">
          <view class="p1">欢迎使用ChatGPT对话能力</view>
          <view class="p2">点击悬浮球切换至GPT绘画能力</view>
          <view class="p5">我可以回答问题 你可以问我</view>
          <view class="p4">帮我检查一下代码</view>
          <view class="p4">帮我写一篇关于Java的实践论文</view>
          <view class="p4">中国历史有多长</view>
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
              <image src="/static/images/gpt.jpg" class="icon"/>
            </view>
          </view>
          <view class="md">
            <view style="height: 60rpx" v-show="!item[1].isShow">
              <view v-show="!item[1].reply">
                <view :class="{ 'blink': item[1].reply }" class="md-text">{{ text }}</view>
              </view>
            </view>
            <view v-show="item[1].isShow">
              <mp-html :copy-link="true" :tagStyle="md.tagStyle" :markdown="true" :lazy-load="true"
                       :selectable="true"
                       :content="item[1].content"/>
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
import {GPT_Turbo} from '@/api/gpt'
import {getHistory, getHistoryEnable, getOpen, setHistory} from "@/utils/data";

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
      chat: [],
      canvas: true,
      text: '|',
      replied: false,
      succeed: true,
    }
  },
  created() {
    if (getHistoryEnable() !== "0" && getHistory().length > 0) {
      this.data = getHistory()
      this.canvas = false
    }
  },
  methods: {
    checkHistory: function (data) {
      let historyEnable = getHistoryEnable();
      if (historyEnable !== "0" && historyEnable.length > 0) {
        setHistory(data);
      }
    },
    async send(input) {
      if (this.succeed) {
        //校验
        if (input && !/^\d+$/.test(input) && !/^[^\da-zA-Z\u4e00-\u9fa5]+$/u.test(input)) {
          this.succeed = false
          this.canvas = false
          await this.packetStructure(input);
          uni.$emit('positioning');
          let size = this.data.length - 1;
          let datumElement = this.data[size][1];
          this.timer = setInterval(() => {
            datumElement.reply = !this.data[size][1].reply
          }, 300)
          try {

            const text = await GPT_Turbo({
              "messages": this.chat,
              "openId": getOpen()
            });
            let message = text.choices[0].message;
            uni.$emit('positioning');

            this.chat.push(message)
            this.outPutFont(message, size)
          } catch (e) {
            //当前坐标
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
          title: `请等待GPT的回应~`
        });
      }


    },
    delData: function () {
      this.canvas = true
      this.data = []
      this.chat = []
      this.succeed = true
    }, delChat: function () {
      this.chat = []
    },
    outPutFont: function (message, size) {
      const _this = this
      const intervals = [10];
      let text = message.content;
      let i = 0;

      function printNextCharacter() {
        try {
          if (i < text.length) {
            const interval = intervals[Math.floor(Math.random() * intervals.length)];
            _this.data[size][1].content = text.slice(0, i++) + '_|';
            uni.$emit('positioning');
            setTimeout(() => {
              printNextCharacter();
            }, interval);
          } else {
            _this.data[size][1].content = text;
            _this.checkHistory(_this.data)
            _this.succeed = true;
          }
        } catch (e) {
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
          uni.$emit('positioning');
        } else {
          datumElement.content = text
          uni.$emit('positioning');
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

.p5 {
  font-size: 30rpx;
  margin-top: 50rpx;
  margin-bottom: 30rpx;
}


.p3 {
  text-align: center;
  font-size: 23rpx;
  margin-top: 270rpx;
  color: #6a6a6a
}

.p4 {
  font-size: 25rpx;
  margin-top: 20rpx;
  color: #313131
}


.p-box {
  position: relative;
  top: -150rpx
}

@keyframes blinker {
  0% {
    opacity: 0;
  }
}
</style>





