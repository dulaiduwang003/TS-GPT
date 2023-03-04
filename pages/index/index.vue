<template>
	<view class="content">
		<scroll-view class="scroll-view" scroll-y="true" :scroll-into-view="scrollInto" v-show="canvas">
			<view class="me-content">
				<view class="me-box" v-for="(item,index) in data" :key="index" :id="'id-m-'+index">
					<view class="me-font" v-show="item.chat[0].content.length>0">
						<view class="text-m" @click="copyInput(index)">
							{{ item.chat[0].content }}
						</view>
					</view>
					<view :id="'id-g-'+index">
						<view class="chat-gpt">
							<view class="chat-gpt-logo">
								<image class="chat-gpt-height" src="/static/logo.jpg" />
							</view>
						</view>
						<view class="text-g" v-show="item.chat[1].content.length>0">
							<mp-html :copy-link="true" :markdown="true" :lazy-load="true" :selectable="true"
								:tagStyle="tagStyle" :content="item.chat[1].content" />
						</view>
						<view class="text-g" v-show="item.chat[1].content.length===0">
							<view class="loading">
								<view class="dot1"></view>
								<view class="dot2"></view>
								<view class="dot3"></view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</scroll-view>
		<view class="canvas-box" v-show="!canvas">
			<image class="canvas-logo" src="/static/logo.jpg" />
			<view class="canvas-title">ChatGPT</view>
			<view class="canvas-msg">示例</view>
			<view class="text-g canvas-text" @click="writeInputText('用字符生成一个可爱的小猫咪')">
				"用字符生成一个可爱的小猫咪"
			</view>
			<view class="text-g canvas-text" @click="writeInputText('用简单的术语解释量子计算')">
				"用简单的术语解释量子计算"
			</view>
			<view class="text-g canvas-text" @click="writeInputText('SpringBoot实现Redis分布式锁示例代码')">
				"SpringBoot实现Redis分布式锁示例代码"
			</view>
	
			<view class="text-g canvas-text" @click="writeInputText('给我来一篇关于Java的实践性论文')">
				"给我来一篇关于Java的实践性论文"
			</view>

			<view
				style="font-size: 20rpx;display: flex;justify-content: space-between;padding: 100rpx;margin-top: 170rpx">
				<view @click="copyGitHubLink">
					<image style="width:73rpx;height:73rpx;" src="/static/ball/github.png"></image>
					<view>GitHub地址</view>
				</view>
				<view @click="copyBiliBili">
					<image style="width:73rpx;height:73rpx;" src="/static/ball/bili.png"></image>
					<view>B站UP</view>
				</view>
				<view @click="console">
					<image style="width:73rpx;height:73rpx;" src="/static/ball/flower.png"></image>
					<view>打赏</view>
				</view>
			</view>
		</view>
		<view class="taskbar">
			<view style="display: flex">
				<view class="pro-wx">
					<textarea placeholder="问你想问的..." maxlength="-1" class="input" v-model="input" />
				</view>
				<view class="input-img" @click="sendMessage">
					<image class="input-img-size" src="/static/send.png" />
				</view>
			</view>
		</view>
		<touch></touch>
		<uni-popup ref="wechat">
			<image style="width:300rpx;height:300rpx;" src="/static/ball/wechat.jpg" :show-menu-by-longpress="true">
			</image>
		</uni-popup>
	</view>
</template>

<script>
	import touch from '@/pages/touch.vue'
	import mpHtml from '@/components/mp-html/mp-html.vue'
	import {
		getHistory,
		getPainting,
		setHistory,
		removePainting,
		removeHistory,
		getHistoryEnable,
		setHistoryEnable,
		setPainting
	} from "@/utils/data";

	export default {
		components: {
			mpHtml,
			touch
		},
		data() {
			return {
				canvas: false,
				input: "",
				scrollInto: "",
				chat: [],
				data: [],
				first: true,
				succeed: true,
				tagStyle: {
					p: 'font-size: 26rpx;padding-top: 8px;padding-bottom: 8px;margin: 0;line-height: 26px;color: white',
					h1: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 34rpx;color: white',
					h2: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 32rpx;color: white',
					h3: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 30rpx;color: white',
					h4: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 28rpx;color: white',
					h5: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 26rpx;color: white',
					h6: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 26rpx;color: white',
					ol: 'margin-top: 8px;margin-bottom: 8px;padding-left: 25px;color: black;color: white',
					ul: 'margin-top: 8px;margin-bottom: 8px;padding-left: 25px;color: black;color: white',
					li: 'margin-top: 5px;margin - bottom: 5 px;line - height: 26 px;color: white',
				},
			}
		},
		onLoad() {
			const _this = this
			this.enableCache()
			uni.$on('openHistory', function() {
				_this.openHistory()
			});
			uni.$on('closeHistory', function() {
				_this.closeHistory()
			});
			uni.$on('removePainting', function() {
				_this.removePainting()
			});
			uni.$on('canvasVisual', function() {
				_this.canvas = false
			});
		},
		methods: {
			console() {
				this.$refs.wechat.open('center')
				uni.vibrateShort();
			},
			copyGitHubLink() {
				uni.setClipboardData({
					data: "https://github.com/dulaiduwang003/ChatGPT_wechat",
					success: res => {
						uni.showToast({
							icon: 'none',
							duration: 3000,
							title: `已复制链接到粘贴板`
						});
					}
				})
			},
			copyBiliBili() {
				uni.setClipboardData({
					data: "https://space.bilibili.com/1876478054?spm_id_from=333.337.search-card.all.click",
					success: res => {
						uni.showToast({
							icon: 'none',
							duration: 3000,
							title: `已复制链接到粘贴板`
						});
					}
				})
			},
			//是否开启历史缓存?
			enableCache() {
				if (getHistoryEnable() === "1") {
					if (getHistory().length > 0) {
						this.canvas = true
					}
					//回滚之前对话以及数据
					this.chat = getPainting()
					this.data = getHistory()
				}
			},
			//发送
			sendMessage() {
				if (this.succeed) {
					this.canvas = true
					this.succeed = false
					//构建数据模型
					this.chat.push({
						role: "user",
						content: this.input,
					}, {
						role: "assistant",
						content: "",
					})
					//渲染内容
					this.data.push({
						chat: [{
							role: "user",
							content: this.input,
						}, {
							role: "assistant",
							content: "",
						}]
					})
					//获取渲染内容下标
					const size = this.data.length - 1
					// 执行请求
					this.$store.dispatch('gpt/gptTurbo', this.chat).then(res => {
						let i = 0;
						const _this = this
						//机器人回复内容
						const gptText = res.data['choices'][0]['message']['content'];
						//模拟打印字效果
						let timer = setInterval(function() {
							if (i <= gptText.length) {
								_this.data[size].chat[1].content = gptText.slice(0, i++) + '_|'
							} else {
								_this.data[size].chat[1].content = gptText
								//等待写入完毕后更新数据
								if (getHistoryEnable() === "1") {
									_this.updateHistory(_this.data)
								}
								//关闭
								clearInterval(timer)
							}
							//模拟手机短频震动
							uni.vibrateShort()
						}, 10);
						//设置消息定位
						this.scrollInto = 'id-g-' + size;
					}).catch((res) => {
						this.data[size].chat[1].content = res
						if (getHistoryEnable() === "1") {
							_this.updateHistory(_this.data)
						}
					}).finally(() => {
						this.input = ""
						this.succeed = true
					})
					//大于5时重置会话消息
					if (this.chat.length >= 5) {
						this.removePainting()
					}
				} else {
					uni.showToast({
						icon: 'none',
						duration: 3000,
						title: `请等待GPT的回应~`
					});
				}
			},
			openHistory() {
				uni.showToast({
					icon: 'none',
					duration: 3000,
					title: `开启历史数据回显`
				});
				setHistoryEnable("1")
				//获取缓存数据
				setHistory(this.data)
				setPainting(this.chat)
			},
			updateHistory() {
				setHistory(this.data)
				setPainting(this.chat)
			},
			removePainting() {
				uni.showToast({
					icon: 'none',
					duration: 3000,
					title: `重置GPT会话成功`
				});
				this.chat = []
			},
			closeHistory() {
				uni.showToast({
					icon: 'none',
					duration: 3000,
					title: `关闭历史数据回显`
				});
				setHistoryEnable("0")
				//清除缓存
				removeHistory()
				removePainting()
			},
			copyInput(index) {
				this.input = this.data[index].chat[0].content
			},
			writeInputText(str) {
				this.input = str
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

	.canvas-text {
		padding-top: 20rpx;
		padding-bottom: 20rpx;
		margin-bottom: 15rpx;
	}

	.taskbar {
		position: fixed;
		bottom: 0;
		height: 200rpx;
		width: 100%;
		background-color: rgb(16, 16, 16);

	}

	.canvas-box {
		color: white;
		padding-top: 50rpx;
		text-align: center
	}

	.canvas-logo {
		height: 150rpx;
		width: 150rpx;
	}

	.canvas-title {
		font-weight: 600;
		font-size: 55rpx;
		padding-bottom: 80rpx
	}

	.canvas-msg {
		padding-top: 20rpx;
		padding-bottom: 15rpx
	}

	.pro-wx {
		background-color: #171717;
		border-radius: 10rpx;
		padding: 10rpx;
		margin-top: 40rpx;
		margin-left: 40rpx
	}

	.scroll-view {
		height: 1200rpx;
	}

	.me-font {
		text-align: right;
		color: white;
		margin-top: 30rpx;
		margin-bottom: 20rpx;
	}

	.me-box {
		padding: 20rpx;
	}

	.me-content {
		margin-bottom: 50rpx
	}

	.input {
		background: #171717;
		color: white;
		font-size: 26rpx;
		font-weight: 600;
		width: 550rpx;
		height: 80rpx;
	}

	.input-img {
		padding-top: 65rpx;
		padding-left: 45rpx
	}

	.input-img-size {
		height: 50rpx;
		width: 50rpx
	}

	.text-m {
		word-break: break-all;
		background-color: rgb(53, 116, 240);
		border-radius: 25rpx;
		display: inline-block;
		padding: 20rpx;
		margin-top: 10rpx;
		color: white;
		font-size: 25rpx
	}

	.chat-gpt {
		display: flex;
		justify-items: center;
		align-items: center;
	}

	.text-g {
		max-width: 645rpx;
		word-break: break-all;
		background-color: #2d2d2d;
		border-radius: 25rpx;
		display: inline-block;
		padding-left: 35rpx;
		padding-right: 35rpx;

		margin-top: 10rpx;
		color: white;
		font-size: 25rpx
	}

	.dot1,
	.dot2,
	.dot3 {
		background: #fff;
		width: 20rpx;
		height: 20rpx;
		border-color: black;
		border-radius: 50%;
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

	@keyframes jump {

		0%,
		80%,
		100% {
			transform: scale(0);
			background-color: #F9F9F9;
		}

		40% {
			transform: scale(1.0);
			background-color: #a2a2a2;
		}
	}

	.chat-gpt-logo {
		border-radius: 100%;
		overflow: hidden;
		width: 70rpx;
		height: 70rpx;
	}

	.chat-gpt-height {
		width: 70rpx;
		height: 70rpx;
	}

	.content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
	}

	page {
		background-color: #000000;
	}
</style>
