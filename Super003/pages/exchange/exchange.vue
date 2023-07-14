<template>
    <view class="content">
        <image src="/static/images/icon/icon.png" class="image"/>
        <view class="title">SuperBot</view>
        <view class="input-box">
            <input
                    placeholder="兑换码" v-model="input" maxlength="6" class="input" @confirm="useCode"
                    confirm-type="send"/>
        </view>
        <button class="btn" @click="useCode">
            兑换
        </button>
        <view class="bottom-text">次数兑换码可联系客服获取
        </view>
    </view>
</template>

<script>
import {exchangeCode} from "@/api/auth";

export default {
    data() {
        return {
            input: ''
        }
    },
    methods: {

        async useCode() {
            uni.vibrateShort()
            if (this.input.length !== 6) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `兑换码格式错误`
                });
                return
            }
            try {
                await exchangeCode(this.input);
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `兑换成功`
                });
                this.input = ''
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: e
                });
                this.input = ''
            }
        }

    }
}
</script>

<style lang="scss">
page {
  background-color: #2B2D30;
}

.content {
  margin-top: 300rpx;
  color: white;
  font-weight: 500;
  text-align: center
}

.image {
  width: 150rpx;
  height: 150rpx
}

.title {
  color: #e8e8e8
}

.input-box {
  padding: 50rpx 100rpx;
}

.input {
  border-radius: 15rpx;
  height: 100rpx;
  background: #151515;
  color: #8d8d8d
}

.btn {
  width: 300rpx;
  height: 80rpx;
  background-color: rgb(115, 91, 207);
  color: white;
  font-size: 28rpx
}

.bottom-text {
  margin-top: 50rpx;
  font-size: 25rpx;
  padding: 40rpx;
  color: #7a7a7a
}
</style>
