<template>
  <view>
    <view @touchmove.stop.prevent="doNothing">
      <view class="hover_ball_cell"
            :style="{ width: `${diameter}px`, height: `${diameter}px`, top: `${top}px`, left: `${left}px` }"
            @touchmove="touchmove" @touchend="touchend" @touchcancel="touchcancel" @tap="onTap"
            :animation="ballAnimation" @click="console()">
        <view class="wave">
          <canvas id="lottie" type="2d" class="canvas"/>
        </view>
      </view>
    </view>
    <uni-popup ref="controlPanel" background-color="rgb(17,17,17)">
      <view class="c-text">
        <view class="uni-title">历史回溯</view>
        <switch :checked="checked" @change="cache" color=" rgb(37, 50, 100)" style="transform:scale(0.7)"/>
      </view>
      <view class="c-text">
        <view class="uni-title">清除Ai会话</view>
        <switch :checked="r_checked" @change="restart" color=" rgb(37, 50, 100)" style="transform:scale(0.7)"/>
      </view>
      <view class="c-text">
        <view class="uni-title">显示主页</view>
        <switch :checked="b_checked" @change="runIndex" color=" rgb(37, 50, 100)" style="transform:scale(0.7)"/>
      </view>
      <view class="c-text">
        <view class="uni-title">切换至Turbo</view>
        <switch :checked="q_checked" @change="model" color=" rgb(37, 50, 100)" style="transform:scale(0.7)"/>
      </view>
      <view class="c-text">
        <view class="uni-title">清除页面数据</view>
        <switch :checked="c_checked" @change="closeData" color=" rgb(37, 50, 100)" style="transform:scale(0.7)"/>
      </view>
    </uni-popup>
  </view>
</template>

<script>
import lottie from 'lottie-miniprogram';
import {getHistoryEnable, getModel} from "@/utils/data";

let ani = null;
export default {
  data() {
    return {
      diameter: 0,
      top: 0,
      left: 0,
      isMove: false,
      ballAnimation: {},
      timeout: null,
      modile: {},
      q_checked: false,
      checked: false,
      r_checked: false,
      b_checked: false,
      c_checked: false,
    }
  },
  created() {
    let _this = this;
    _this.renderLottie("#lottie", "Windy.json")
    _this.modile = uni.getSystemInfoSync();
    _this.top = _this.modile.safeArea.top + 400;
    _this.left = _this.modile.safeArea.right;
    _this.diameter = _this.modile.screenHeight / 15;
    this.checked = getHistoryEnable() === "1";
    this.q_checked = getModel() === "1";
  },
  methods: {
    closeData() {
      uni.showLoading({
        title: "清除页面数据及会话",
        mask: true,
      });
      this.c_checked = true
      setTimeout(() => {
        uni.$emit('closePage');
        this.c_checked = false
        uni.hideLoading()
      }, 600)
    },
    runIndex() {
      uni.showLoading({
        title: "返回主页",
        mask: true,
      });
      this.b_checked = true
      setTimeout(() => {
        uni.$emit('canvasVisual');
        this.b_checked = false
        uni.hideLoading()
      }, 600)
    },
    cache(e) {
      if (!e.detail.value) {
        uni.$emit('closeHistory');
        e.detail.value = false
      } else {
        e.detail.value = true
        uni.$emit('openHistory');
      }
    },
    model(e) {
      if (e.detail.value) {
        uni.$emit('openModel');
        uni.showToast({
          icon: 'none',
          duration: 2000,
          title: `已切换 GPT-3.5 Turbo`
        });
        e.detail.value = false
      } else {
        uni.$emit('closeModel');
        e.detail.value = true
        uni.showToast({
          icon: 'none',
          duration: 2000,
          title: `已切换 GPT-003`
        });
      }
    },
    restart() {
      uni.showLoading({
        title: "正在清除会话",
        mask: true,
      });
      this.r_checked = true
      uni.$emit('removePainting');
      setTimeout(() => {
        this.r_checked = false
        uni.hideLoading()
      }, 600)
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
              animationData: require("../static/json/" + path),
              rendererSettings: {
                context
              },
            });
          }).exec();
    },
    console() {
      this.$refs.controlPanel.open('left')
      uni.vibrateShort();

    },
    onTap() {
      let _this = this;
      let x = '0';
      if (2 * _this.left >= _this.modile.safeArea.width) {
        x = '-100%';
      }
      let create = uni.createAnimation({
        duration: 0
      });
      create.translate(x).step();
      _this.ballAnimation = create.export();
      _this.overBall();
    },
    touchmove(e) {

      let _this = this;

      _this.isMove = true;
      if (_this.timeout != null) {
        clearTimeout(_this.timeout);
        _this.timeout = null;
      }
      var touch = e.touches[0] || e.changedTouches[0];
      _this.left = touch.clientX;
      _this.top = touch.clientY;
    },
    touchend(e) {
      let _this = this;
      if (!_this.isMove) {
        return;
      }
      _this.finish(e);
    },
    touchcancel(e) {
      let _this = this;
      if (!_this.isMove) {
        return;
      }
      _this.finish(e);
    },
    finish(e) {
      let _this = this;
      _this.isMove = false;
      var touch = e.touches[0] || e.changedTouches[0];
      _this.left = touch.clientX;
      _this.top = touch.clientY;
      let x = '0';
      if (2 * _this.left + _this.diameter >= _this.modile.safeArea.width) {
        _this.left = _this.modile.safeArea.width;
        x = '-100%';
      } else {
        _this.left = _this.modile.safeArea.left;
      }
      if (_this.top > _this.modile.safeArea.height + _this.modile.safeAreaInsets.bottom) {
        _this.top = _this.modile.safeArea.height + _this.modile.safeAreaInsets.bottom;
      } else if (_this.top < -_this.diameter / 2) {
        _this.top = -_this.diameter / 2;
      }
      let create = uni.createAnimation({
        duration: 0
      });
      create.translate(x).step();
      _this.ballAnimation = create.export();
      _this.overBall();
    },
    overBall() {
      let _this = this;
      _this.timeout = setTimeout(() => {
        _this.timeout = null;
        let create = uni.createAnimation({
          duration: 400,
          timingFunction: 'ease-in'
        });
        create.translate('-50%').step();
        _this.ballAnimation = create.export();
      }, 50);
    },
    doNothing: function () {
    },
  }
}
</script>

<style lang="less" scoped>
.hover_ball_cell {
  position: fixed;
  overflow: hidden;
  border-radius: 50%;
  background: rgb(16, 20, 40);

  transform: translate(-50%, 0);
  padding: 17rpx;
  box-shadow: 0rpx 4rpx 8rpx 0rpx rgba(0, 0, 0, 0.08);

  .wave {
    position: relative;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
  }
}

.c-box {
  display: flex;
  align-items: center;
  justify-items: center;
  padding: 10rpx;
  color: white;
  font-size: 26rpx;
  font-weight: 600;
  justify-content: space-between
}

.c-text {
  color: white;
  display: flex;
  align-items: center;
  justify-items: center;
  justify-content: space-between;
  font-size: 26rpx;
  padding: 30rpx;
}

.canvas {
  width: 100rpx;
  height: 100rpx
}


</style>
