<template>
    <view>
        <view class="external-container">
            <view style="text-align: center">
                <image :src="require('static/images/img/bot-4.png')" class="image"/>
            </view>
            <view class="ctl-word">StanAi</view>
            <view class="h-box">
                <view class="h-c-box" @click="addTags">
                    <image src="/static/assets/lable.svg" class="h-c-img"/>
                    <view>创建新的聊天</view>
                </view>
                <view class="h-code">
                    我的片段
                </view>
                <scroll-view scroll-y="true" :scroll-top="scrollTop">
                    <view style="height: 200rpx">
                        <view class="recording" v-for="(item,index) in tabs" :key="index">
                            <image src="/static/assets/selected.svg" class="r-img" v-show="item.default" @click="chooseTag(index)"/>
                            <image src="/static/assets/text.svg" class="r-img" v-show="!item.default" @click="chooseTag(index)"/>
                            <view class="history-button">
                                <view @click="chooseTag(index)" style="width: 300rpx">{{ item.title }}</view>
                                <view class="history-del" @click="deleteTag(index)">删除</view>
                            </view>
                        </view>
                    </view>
                </scroll-view>
                <view class="r-border"></view>
            </view>
        </view>
    </view>
</template>

<script>
import {getData, setData} from "@/utils/data";

export default {
    data() {
        return {
            scrollTop: 0,
            tabs: [],
            current: 0
        }
    },
    created() {
        if (getData() === "") {
            this.tabs.push({
                "title": '新会话',
                "data": [],
                "chat": [],
                "default": true
            })
            this.current = this.tabs.length
            setData(this.tabs)
        } else {
            this.tabs = getData();
            for (let i = 0; i < this.tabs.length; i++) {
                if (this.tabs[i].default) {
                    this.current = i
                    uni.$emit('changeCache', this.tabs[i]);
                    break
                }
            }

        }
    },
    methods: {
        deleteTag: function (index) {
            uni.vibrateShort()
            if (this.tabs[index].default) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: `当前标签页正在使用中~`
                });
                return
            }
            this.tabs.splice(index, 1)
            setData(this.tabs)
        },
        chooseTag: function (index) {
            uni.vibrateShort()
            if (index !== this.current) {
                this.current = index;
                this.tabs.forEach(t => t.default = false)
                this.tabs[index].default = true
                uni.$emit('changeCache', this.tabs[index]);
                setData(this.tabs)
            }
        },
        addTags: function () {
            uni.vibrateShort()
            this.tabs.push({
                "title": '新会话',
                "data": [],
                "chat": [],
                "default": false
            })
            setData(this.tabs)
        },
    }
}
</script>

<style lang="scss">


button::after {
  border: none;
}

.recording {
  height: 50rpx;
  padding: 10rpx;
  border-radius: 15rpx;
  color: #ffffff;
  font-size: 26rpx;
  display: flex;
  align-items: center
}

.r-img {
  width: 50rpx;
  height: 50rpx;
  padding-right: 30rpx
}

.r-border {
  border-bottom: 2rpx solid #ffffff;
  margin-top: 50rpx
}

.r-setting {
  font-size: 23rpx;
  margin-top: 30rpx;
  color: #636363;
  margin-bottom: 20rpx
}

.external-container {
  width: 600rpx;
  background-color: #202123;
  padding: 95rpx 15rpx 15rpx;
  border-radius: 15rpx;
}

.image {
  width: 100rpx;
  height: 100rpx
}

.history-button {
  display: flex;
  justify-content: space-between;
  width: 600rpx;
}

.history-del {
  color: #7c7c7c;
  margin-left: 50rpx
}

.ctl-t {
  transform: scale(0.6)
}


.ctl-word {
  text-align: center;
  font-size: 48rpx;
  font-weight: 600;
  color: #b4b4b4
}

.ctl-up {
  text-align: center;
  font-size: 18rpx;
  color: #b7b7b7
}

.ctl-box {
  font-size: 25rpx;
  margin-top: 20rpx;
  width: 550rpx;
  display: flex;
  justify-content: space-between;
  color: #737373
}


.h-box {
  margin-top: 50rpx;
  padding: 40rpx
}

.h-c-box {
  height: 50rpx;
  border: 2rpx solid #8d8d8d;
  padding: 10rpx;
  border-radius: 15rpx;
  color: #ffffff;
  font-size: 26rpx;
  display: flex;
  align-items: center;
  margin-bottom: 10rpx
}

.h-c-img {
  width: 50rpx;
  height: 50rpx;
  padding-right: 30rpx
}

.h-code {
  font-size: 23rpx;
  margin-top: 30rpx;
  color: #636363;
  margin-bottom: 20rpx
}

.canvas {
  width: 200rpx;
  height: 200rpx
}
</style>
