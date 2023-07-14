<template>
    <view>
        <view class="content" v-show="isShow">
            <view style="margin-top: 30rpx;">
                <img class="logo" src="/static/images/img/bot-2.png" alt="logo">
                <view class="title" style="font-size: 40rpx;color: white;margin-top: 30rpx;margin-bottom: 30rpx;font-weight: 500;">Welcome to  console</view>
                <input placeholder="令牌" maxlength="10" v-model="account" class="input"/>
                <input placeholder="密钥" maxlength="10" password v-model="password" class="input" confirm-type="send"
                       @confirm="login"/>
                <button class="btn" @click="login">
                    验证
                </button>
            </view>
        </view>
        <view class="content" v-show="!isShow" style="animation: fadeIn 0.5s ease-in-out forwards;">
            <view style="text-align: left">
                <view class="title">
                    服务器策略
                </view>
                <view class="wx-tactics">
                    <view class="wx-choose">
                        <picker @change="bindPickerChange" :value="index" :range="array">
                            <view class="wx-choose-text">{{ array[index] }}</view>
                        </picker>
                    </view>
                    <button class="apple" @click="toSdConfig">
                        配置策略
                    </button>
                </view>
                <view class="title">
                    生成兑换码
                </view>
                <view class="wx-tactics">
                    <view class="wx-choose">
                        <input style="font-size: 24rpx;text-align: center" placeholder="生成数量"
                               v-model="quantity"> </input>
                    </view>
                    <view class="wx-choose" style="margin-left: 30rpx">
                        <input style="font-size: 24rpx;text-align: center" placeholder="兑换码额度"
                               v-model="frequency"> </input>
                    </view>
                    <button class="apple" @click="generateCode">
                        生成
                    </button>
                </view>
                <view class="title">
                    可用次数兑换码
                </view>

                <view class="redemption_code">
                    <view class="redemption_title">
                        <view>
                            兑换码
                        </view>
                        <view>
                            兑换次数
                        </view>
                    </view>
                    <scroll-view scroll-y="true" style="height: 700rpx;">
                        <view class="code" v-for="(item,index) in codes" :key="index" v-show="codes.length>0"
                              @click="copy(item.code)">
                            <view>
                                {{ item.code }}
                            </view>
                            <view>
                                {{ item.frequency }}
                            </view>
                        </view>
                        <view v-show="codes.length===0" style="text-align: center;color: #a4a3a3;padding-top: 250rpx">
                            暂无数据
                        </view>
                    </scroll-view>

                </view>
            </view>
        </view>
    </view>
</template>

<script>
import {generateCode, getCode, getConfig, serverLogin, tactics} from "@/api/server";
import {getServerToken, setServerToken} from "@/utils/data";

export default {
    data() {
        return {
            account: '',
            password: '',
            isShow: true,
            index: 0,
            array: ['直连模式', '代理模式', '第三方模式'],
            codes: [],
            frequency: '',
            quantity: ''
        }
    },
    onShow() {
        if (getServerToken() !== '') {
            this.isShow = false
            this.getData()
        } else {
            this.isShow = true
        }
    },
    methods: {
        copy(data) {
            uni.vibrateShort()
            uni.setClipboardData({
                data: data,
                success: function success(res) {
                    uni.showToast({
                        icon: 'none',
                        duration: 1000,
                        title: '内容已复制到粘贴板'
                    });
                }
            });
        },
        async generateCode() {
            uni.vibrateShort()
            try {
                if (this.frequency.length === 0 && this.frequency !== "0") {
                    uni.showToast({
                        icon: 'none',
                        duration: 3000,
                        title: '兑换码额度格式错误'
                    });
                    return
                }
                if (this.quantity.length === 0 && this.quantity !== "0") {
                    uni.showToast({
                        icon: 'none',
                        duration: 3000,
                        title: '对话码生成数量格式错误'
                    });
                    return
                }

                await generateCode({
                    "frequency": this.frequency,
                    "quantity": this.quantity,
                })
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: '生成完毕'
                });
                await this.getData()
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: e
                });
            }
        },
        toSdConfig: function () {
            uni.vibrateShort()
            uni.navigateTo({
                url: '/pages/config/config'
            })
            return true
        },
        async apple() {
            uni.vibrateShort()
            try {
                let number = Number(this.index) + 1;
                await tactics({
                    "choose": number
                });
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: '应用成功'
                });
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: e
                });
            }
        },
        async getData() {
			try{
				this.codes = await getCode();
				let data = await getConfig();
				  this.index = data.choose - 1;
			}catch(e){
				console.log('未配置');
			}
          
        },
        bindPickerChange: function (e) {
            uni.vibrateShort()
            this.index = e.detail.value
            this.apple()
        },
        async login() {
            uni.vibrateShort()
            if (this.account.length <= 0) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: '认证令牌不能为空'
                });
                return
            }
            if (this.password.length <= 0) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: '认证密钥不能为空'
                });
                return
            }
            try {
                uni.showLoading({
                    title: "正在登陆",
                    mask: true,
                });
                let token = await serverLogin({
                    account: this.account,
                    password: this.password
                });
				console.log(token);
                setServerToken(token)
                await this.getData()
                this.isShow = false
            } catch (e) {
                uni.showToast({
                    icon: 'none',
                    duration: 3000,
                    title: e
                });
            } finally {
                uni.hideLoading()
            }
        }
    }
}
</script>

<style lang="scss">
.content {
  text-align: center;
  padding: 4%;
}

.btn {
  width: 580rpx;
  height: 80rpx;
  background-color: rgb(115, 91, 207);
  color: white;
  font-size: 28rpx;
  margin-top: 70rpx
}

.title {
  color: #b2b2b2;
  font-size: 28rpx;
  font-weight: 500;
}

page {
  background-color: #2B2D30;
  --grid-item-content-background-color: #2B2D30;
  --grid-item-content-active-color: rgb(123, 97, 220);
}

@keyframes jump {
  0% {
    transform: translateY(0);
  }
  100% {
    transform: translateY(-10px);
  }
}

.logo {
  animation: jump 0.7s infinite alternate;
  margin-top: 200rpx;
  width: 170rpx;
  height: 170rpx;

}

.apple {
  margin-left: 30rpx;
  border-radius: 15rpx;
  width: 200rpx;
  height: 70rpx;
  background-color: rgb(115, 91, 207);
  color: white;
  font-size: 26rpx;
}

.wx-apple {
  margin-top: 40rpx
}

.wx-tactics {
  margin-top: 40rpx;
  margin-bottom: 40rpx;
  color: white;
  display: flex;
  justify-content: space-between
}

.wx-choose {
  width: 500rpx;
  background-color: #383838;
  padding: 15rpx;
  border-radius: 15rpx
}

.code {
  display: flex;
  justify-content: space-between;
  margin-top: 30rpx
}

.wx-choose-text {
  text-align: center;
  font-size: 26rpx;
  color: #999999
}

.redemption_code {
  color: #b2b2b2;
  font-size: 28rpx;
  height: 770rpx;
  margin-top: 40rpx;
  padding: 40rpx 50rpx;
  text-align: center;
  background-color: #383838;
  margin-bottom: 30rpx;
  border-radius: 15rpx;
}

.redemption_title {
  display: flex;
  justify-content: space-between;
  margin-bottom: 40rpx;
  font-size: 30rpx;
}

.input {
  margin-left: 50rpx;
  width: 580rpx;
  margin-top: 30rpx;
  height: 80rpx;
  padding: 4rpx;
  color: #a1a1a1;
  background-color: #181818;
  border-radius: 15rpx
}
</style>
