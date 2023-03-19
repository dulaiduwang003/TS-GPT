<template>
  <view>
    <canvas v-if="displayCanvas" id="myCanvas" ref="canvas" canvas-id="myCanvas"
            style="position: absolute;top: 100rpx"/>
    <scroll-view class="scroll-view" scroll-y="true" :scroll-top="scrollTop">
      <view id="scroll-view-content">
        <index v-show="handoff===0" ref="index"/>
        <painting v-show="handoff===1" ref="painting"/>
        <bing v-show="handoff===2" ref="bing"/>
      </view>
    </scroll-view>
    <view :animation="animationData ? animationData : ''"
          class="input-box ">
      <view class="pro-wx">
        <input placeholder="有问题尽管问我" maxlength="300" class="input" v-model="input" confirm-type="发送"
               @confirm="send()"/>

        <image class="c-img" src="/static/assets/time.svg" @click="cleaner"/>
        <image class="c-img" src="/static/assets/panel.svg" @click="openCtrl"/>
        <image class="c-img" src="/static/assets/delete.svg" @click="delData"/>
      </view>
      <image class="send" src="/static/assets/send.svg" @click="send"/>
    </view>
    <touch ref="touch" @click="hanDoff" @longpress="onLongPress" longpress-duration="3000"></touch>
    <uni-popup ref="panel">
      <view class="wx-panel">
        <image src="/static/assets/flower.svg" class="wx-panel-img"/>
        <view>请作者喝一杯可乐</view>
        <view>
          <image show-menu-by-longpress="true" src="/static/images/wx_code.jpg" style="width: 300rpx;height: 300rpx"/>
        </view>
        <view style="font-size: 25rpx;color: #3a3a3a">长按图片保存到手机扫一扫</view>
        <view style="margin-top: 30rpx">
          <button class="wx-panel-introduce" @click="donation">放烟花~</button>
        </view>
      </view>
    </uni-popup>
    <uni-popup ref="control">
      <view class="wx-ctl">
        <view class="ctl-title">个性设置</view>
        <view class="ctl-lottie">
          <canvas id="lottie" type="2d" class="canvas"/>
        </view>
        <button open-type="contact" class="bdth">
        </button>
        <view class="ctl-word">UP正在遨游时间海...</view>
        <view class="ctl-up">点击小汽车骚扰作者</view>
        <view class="ctl-box">
          <view>
            历史记录
            <switch color="#72c79c" class="ctl-t" :checked="history" @change="enableHistory"/>
          </view>
          <view>
            黑夜模式
            <switch color="#72c79c" class="ctl-t" @click="dev" disabled="true"/>
          </view>
          <view>
            动画控制
            <switch color="#72c79c" class="ctl-t" checked="true" disabled="true" @click="dev"/>
          </view>
          <view>
            流式数据
            <switch color="#72c79c" class="ctl-t" checked="true" disabled="true" @click="dev"/>
          </view>
        </view>
      </view>
    </uni-popup>
    <uni-popup ref="console">
      <view class="wx-console">
        <view class="ctl-title">数据面板</view>
        <view class="console-box">
          <view class="console-title">
            就绪(官网)
          </view>
          <view class="console-input-box">
            <input maxlength="300" value="" style="width: 100%;" disabled="true" v-model="appData.mainKey"
                   @click="copyHandle(appData.mainKey)"/>
          </view>
          <view class="console-title">
            就绪(第三方)
          </view>
          <view class="console-input-box">
            <input maxlength="300" value="" style="width: 100%;" disabled="true" v-model="appData.thirdPartyKey"
                   @click="copyHandle(appData.thirdPartyKey)"/>
          </view>
          <view style="margin-top: 15rpx">
            有效缓存池
          </view>
          <view class="console-view-box">
            <view class="console-view-content">
              <scroll-view class="console-view-scroll" scroll-y="true">
                <view class="console-text" v-for="(item,index) in appData.availableKeys" :key="index">
                  <view @click="copyHandle(item)">{{ item }}</view>
                </view>
              </scroll-view>
            </view>
          </view>
          <view style="margin-top: 15rpx">
            无效缓存池
          </view>
          <view class="console-view-box">
            <view class="console-view-content">
              <scroll-view class="console-view-scroll" scroll-y="true">
                <view class="console-view-text-box">
                  <view class="console-text" v-for="(item,index) in appData.lapseKeys" :key="index">
                    <view @click="copyHandle(item)">{{ item }}</view>
                  </view>
                </view>
              </scroll-view>
            </view>
          </view>
        </view>
      </view>
    </uni-popup>
    <uni-popup ref="authorization">
      <view class="auth-box">
        <view class="auth-title">微信授权</view>
        <view class="auth-title-b"></view>
        <image src="/static/icon/icon.png" class="auth-icon"/>
        <view class="auth-content">小程序申请获得以下权限</view>
        <view class="auth-content-button"></view>
        <view class="auth-prompt">获取你的公开信息(用户唯一标识)</view>
        <view class="auth-button">
          <view style="color: #424242;" class="que-button" @click="this.$refs.authorization.close()">
            拒绝
          </view>
          <view style="color: #75c4ab;" class="que-button" @click="auth">
            允许
          </view>
        </view>
      </view>
    </uni-popup>
    <lyy-pass-input ref="passInput" title="控制台密码" @confirm="confirm" @close="close"></lyy-pass-input>
  </view>
</template>

<script>
import index from '@/pages/index/index.vue'
import painting from '@//pages/painting/painting.vue'
import bing from '@/pages/bing/bing.vue'
import touch from '@/pages/touch/touch.vue'
import lottie from 'lottie-miniprogram';
import {CanvasRender, ConfettiEjector} from 'confetti-ts-canvas'
import {APP_Status, GPT_Auth} from "@/api/gpt";
import {getHistoryEnable, getOpen, removeHistory, setHistoryEnable, setOpen} from "@/utils/data";
import env from "@/utils/env";
import mpHtml from '@/components/mp-html/mp-html.vue'
import md from '@/static/css/md';

let ani = null;
const canvasRender = new CanvasRender();
export default {
  computed: {
    md() {
      return md
    }
  },
  components: {
    painting,
    index,
    touch,
    mpHtml,
    bing
  },
  data() {
    return {
      handoff: 0,
      input: '',
      animationData: {},
      displayCanvas: true,
      canvasSize: {
        width: 0,
        height: 120,
      },
      history: false,
      scrollTop: 0,
      appData: null,
      texts: [
        ['切换到绘画模式', '请等待GPT回复或者点击垃圾箱中断回复后方可切换功能', '/static/assets/bing.svg', 1],
        ['切换到必应搜索', '请等待GPT回复或者点击垃圾箱中断回复后方可切换功能,', '/static/assets/ai.svg', 2],
        ['切换到问答模式', '请等待Bing回复或者点击垃圾箱中断回复后方可切换功能', '/static/assets/drawing.svg', 0],
      ]
    }
  },
  methods: {
    cleaner: function () {
      uni.showToast({
        icon: 'none',
        duration: 1000,
        title: '清理会话数据'
      });
      this.$refs.index.delChat()
    },
    close: function () {

    },
    confirm: function (e) {
      if (e.pass === env.password) {
        this.initConsole()
        this.$refs.console.open("center");
      } else {
        uni.showToast({
          icon: 'none',
          duration: 1000,
          title: '控制台密码错误'
        });

      }
      this.$refs.passInput.close()
    },
    // 滚动到指定位置
    scrollToPosition() {
      uni.createSelectorQuery().select('#scroll-view-content').boundingClientRect((dom) => {
        if (dom.height > 0) this.$set(this, 'scrollTop', dom.height)
      }).exec()
    },
    enableHistory: function (e) {
      if (!e.detail.value) {
        setHistoryEnable("0")
        removeHistory()
        e.detail.value = false
      } else {
        setHistoryEnable("1")
        e.detail.value = true
      }
    },
    dev: function () {
      uni.showToast({
        icon: 'none',
        duration: 1000,
        title: '仅支持抖音小程序,暂不兼容微信小程序'
      });
    },
    auth: function () {
      try {
        uni.login({
          async success(res) {
            uni.showLoading({
              title: "正在获取权限",
              mask: true,
            });
            let openId = await GPT_Auth(res.code);
            setOpen(openId)
            uni.showToast({
              icon: 'none',
              duration: 1000,
              title: '授权成功'
            });
          }
        })
      } catch (e) {
        uni.showToast({
          icon: 'none',
          duration: 1000,
          title: '授权失败'
        });
      } finally {
        this.$refs.authorization.close()

      }
    },
    copyHandle(data) {
      uni.setClipboardData({
        data: data, // data的值为你要复制的内容
        success: res => {
          uni.showToast({
            icon: 'none',
            duration: 1000,
            title: '已复制到粘贴板'
          });
        }
      });
    },
    openCtrl: function () {
      this.$refs.control.open("center");
    },
    openConsole: function () {
      this.$refs.passInput.open()

    },
    async initConsole() {
      uni.showLoading({
        title: "正在获取数据",
        mask: true,
      });
      try {
        this.appData = await APP_Status();
      } catch (e) {
        uni.showLoading({
          title: "获取数据失败",
          mask: true,
        });
      } finally {
        uni.hideLoading()
      }
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
              animationData: require("@/static/json/" + path),
              rendererSettings: {
                context
              },
            });
          }).exec();
    },
    async donation() {
      const pao = new ConfettiEjector(canvasRender, {
        limitAngle: [525, 1100],//喷发角度区间[-∞,+∞]
        count: 220,//喷发纸片数量
      });
      const boom = pao.create({
        x: Math.random() * (this.canvasSize.width * .5),
        y: Math.random() * (this.canvasSize.height * .5),//喷发位置
        clampforce: [40, 30],//喷发力度
        radius: 6,//纸片大小
      });
      await pao.fire(boom);
    },
    onLongPress: function () {
      this.$refs.panel.open('center')
    },
    toggleHandoff(component, succeedMsg, errorMsg) {
      if (!component.succeed) {
        uni.showToast({
          icon: 'none',
          duration: 3000,
          title: errorMsg
        });
        return false;
      }
      uni.showToast({
        icon: 'none',
        duration: 3000,
        title: succeedMsg
      });
      return true;
    },
    hanDoff() {
      uni.vibrateShort();
      const refs = [this.$refs.index, this.$refs.painting, this.$refs.bing];
      const currRef = refs[this.handoff];
      const [title, message, icon, counter] = this.texts[this.handoff];
      if (this.toggleHandoff(currRef, title, message)) {
        this.$refs.touch.icon = icon;
        this.$refs.touch.counter = counter;
        this.handoff = counter;
      }
    },
    delData() {
      //模拟手机短频震动
      uni.vibrateShort();
      const refs = [this.$refs.index, this.$refs.painting, this.$refs.bing];
      refs[this.handoff].delData();
      uni.showToast({
        icon: 'none',
        duration: 3000,
        title: '页面干净一新~'
      });
      this.input = '';
    },
    sendRequest(component, input) {
      if (component.succeed) {
        component.send(input);
      } else {
        uni.showToast({
          icon: 'none',
          duration: 3000,
          title: '请先等待GPT回应'
        });
      }
    },
    send() {
      if (getOpen() === "") {
        this.$refs.authorization.open("center")
        return;
      }
      uni.vibrateShort()
      if (this.input === "console") {
        this.openConsole()
        this.input = ""
        return
      }
      if (this.handoff === 0) {
        this.$refs.index.send(this.input)
      } else {
        switch (this.handoff) {
          case 1:
            this.sendRequest(this.$refs.painting, this.input);
            break;
          case 2:
            this.sendRequest(this.$refs.bing, this.input);
            break;
        }
      }
      this.input = '';
    }, async initRenders() {
      const rect = await this.getCanvasSize();
      const g = uni.createCanvasContext("myCanvas");
      const {width, height} = rect;
      this.canvasSize.width = width;
      this.canvasSize.height = height;
      canvasRender.init(
          g, {
            width: rect.width,
            height: rect.height,
          },
          {
            onFinished() {

            },
            displayFps: false,
            grivaty: .5,
          }
      );
    }, getCanvasSize() {
      return new Promise((r, e) => {
        uni.createSelectorQuery().select("#myCanvas").boundingClientRect((_) => {
          r(_);
        }).exec();
      });
    },
  }
  ,
  onLoad() {
    const that = this
    uni.$on('changeHanDoff', function () {
      that.hanDoff()
    });
    uni.$on('positioning', function () {
      that.scrollToPosition()
    });
    that.initRenders()
    that.renderLottie("#lottie", "SanDay.json")
    that.history = getHistoryEnable() === "1";
  }
  ,
  mounted() {
    // 计算输入框需要滑动的距离
    const translateY = 30

    // 创建动画对象
    const animation = uni.createAnimation({
      duration: 500,
      timingFunction: 'ease-in-out'
    })

    // 定义动画效果
    animation.translate(0, -translateY).step()

    // 将动画效果保存到 data 中
    this.animationData = animation.export()
  }


}
</script>

<style>

.wx-panel {

  width: 600rpx;
  background-color: white;
  border-radius: 15rpx;
  text-align: center;
  padding: 20rpx
}

.wx-console {
  width: 600rpx;
  background-color: white;
  border-radius: 15rpx;
  padding: 20rpx
}

.wx-ctl {
  width: 600rpx;
  background-color: white;
  border-radius: 15rpx;
  padding: 20rpx
}

.wx-panel-img {
  width: 100rpx;
  height: 100rpx

}

.console-box {
  margin-top: 50rpx;
  font-size: 23rpx;
  color: #6a6a6a
}

.console-title {
  margin-top: 15rpx
}


.console-input-box {
  padding: 6rpx;
  background: #e1f8e5;
  border-radius: 15rpx;
  text-align: center

}

.console-view-box {
  margin-top: 15rpx;
  padding: 5rpx;
  align-items: center;
  display: flex;
  justify-content: space-between
}

.bdth {
  left: 270rpx;
  top: 110rpx;
  width: 100rpx;
  height: 90rpx;
  /*  background-color: unset; */
  color: white;
  z-index: 2;
  position: absolute;
  background-color: transparent;
  border: none;

}

button::after {
  border: none;
}

.console-view-content {
  background: #e1f8e5;
  border-radius: 15rpx
}

.console-view-scroll {
  height: 60rpx;
  width: 600rpx;
}

.console-view-text-box {
  text-align: center;
  padding: 15rpx;
  font-size: 20rpx
}

.console-text {
  overflow: hidden;
  word-break: break-all;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
  white-space: nowrap;
  margin-top: 15rpx;
  margin-bottom: 20rpx
}

.auth-box {
  width: 600rpx;
  background-color: white;
  text-align: center;
  border-radius: 15rpx
}

.auth-title {
  font-size: 30rpx;
  color: #383838;
  padding-top: 30rpx
}

.auth-title-b {
  border-bottom: 1rpx solid #f1f1f1;
  margin-top: 20rpx;
  margin-left: 30rpx;
  margin-right: 30rpx
}

.auth-icon {
  width: 100rpx;
  height: 100rpx;
  margin-top: 20rpx
}

.auth-content {
  font-size: 27rpx;
  color: #3e3e3e;
  margin-top: 20rpx
}

.auth-content-button {
  border-bottom: 1rpx solid #d5d5d5;
  margin-top: 20rpx;
  margin-left: 60rpx;
  margin-right: 60rpx
}

.auth-prompt {
  font-size: 26rpx;
  color: #8d8d8d;
  margin-top: 20rpx
}

.auth-button {
  display: flex;
  justify-content: space-between;
  margin-top: 25rpx
}

.que-button {
  width: 50%;
  text-align: center;
  padding: 20rpx;
  font-weight: 600;
}

.wx-panel-introduce {

  background-color: #a4667f;
  font-size: 28rpx;
  width: 300rpx;
  color: white
}

.input {
  background-color: #d4f3e5;
  color: #696969;
  font-size: 30rpx;
  height: 80rpx;
  width: 420rpx;
}

.scroll-view {
  height: 1250rpx;
}

.send {
  width: 110rpx;
  height: 110rpx;
  padding-top: 40rpx;
  margin-left: 10rpx
}


.ctl-t {
  transform: scale(0.6)
}

.input-box {
  position: fixed;
  align-items: center;
  bottom: 0;
  display: flex;
  justify-content: space-between;
  width: 730rpx
}

.pro-wx {
  width: 530rpx;
  display: flex;
  justify-items: center;
  align-items: center;
  justify-content: space-between;
  margin-left: 30rpx;
  margin-right: 5rpx;
  background-color: #d4f3e5;
  border-radius: 50rpx;
  margin-top: 40rpx;
  padding: 20rpx 40rpx 20rpx 30rpx;
}

.ctl-title {
  font-size: 27rpx;
  color: #3a3a3a
}

.ctl-lottie {
  text-align: center;
  padding-left: 230rpx
}

.ctl-word {
  text-align: center;
  font-size: 25rpx;
  color: #3a3a3a
}

.ctl-up {
  text-align: center;
  font-size: 18rpx;
  color: #696969
}

.ctl-box {
  font-size: 25rpx;
  padding: 20rpx 20rpx 20rpx 45rpx;
  margin-top: 20rpx;
  display: flex;
  justify-content: space-between;
  color: #737373
}

.c-img {
  margin-left: 30rpx;
  width: 50rpx;
  height: 50rpx
}

.canvas {
  width: 150rpx;
  height: 150rpx
}
</style>