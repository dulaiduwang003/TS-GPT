<template>
  <view>
    <view @touchmove.stop.prevent="doNothing" @click="hanDoff">
      <view class="hover_ball_cell"
            :style="{ width: `${diameter}px`, height: `${diameter}px`, top: `${top}px`, left: `${left}px` }"
            @touchmove="touchmove" @touchend="touchend" @touchcancel="touchcancel" @tap="onTap"
            :animation="ballAnimation">
        <view class="wave">
          <image :src="icon" style="width: 150rpx;height: 150rpx"/>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      icon: '/static/assets/drawing.svg',
      counter: 0,
      diameter: 0,
      top: 0,
      left: 0,
      isMove: false,
      ballAnimation: {},
      timeout: null,
      modile: {}
    }
  },
  created() {
    let _this = this;
    _this.modile = uni.getSystemInfoSync();
    _this.top = _this.modile.safeArea.top + 400;
    _this.left = _this.modile.safeArea.right;
    _this.diameter = _this.modile.screenHeight / 15;
  },
  methods: {
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
    hanDoff: function () {
      uni.$emit('changeHanDoff');
    }
  }
}
</script>

<style lang="less" scoped>
.hover_ball_cell {
  position: fixed;
  overflow: hidden;
  border-radius: 50%;
  //background: rgb(16, 20, 40);

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
</style>
