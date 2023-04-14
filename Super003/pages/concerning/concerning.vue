<template>
    <view>
        <view class="content">
            <image src="/static/images/icon/icon.png" class="image"/>
            <view class="title">SuperBot</view>
            <view class="introduce">小程序主要基于百度产品文心一言API实现
                额外功能通过预订模型参数实现
            </view>
        </view>
    </view>
</template>

<script>
export default {
    data() {
        return {}
    },
    methods: {}
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

.introduce {
  margin-top: 50rpx;
  font-size: 30rpx;
  padding: 60rpx 150rpx;
}
</style>
