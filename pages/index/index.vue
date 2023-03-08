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
			<view class="text-g canvas-text" @click="writeInputText('SpringBoot实现Redis分布式锁示例代码')">
				"SpringBoot实现Redis分布式锁示例代码"
			</view>
			<view class="text-g canvas-text" @click="writeInputText('给我来一篇关于Java的实践性论文')">
				"给我来一篇关于Java的实践性论文"
			</view>
			<view class="canvas-msg">提示</view>
			<view class="text-g canvas-text">
				"可在悬浮窗切换模型如GPT-003或GPT-3.5 Turbo"
			</view>
			<view
				style="font-size: 20rpx;display: flex;justify-content: space-between;padding: 100rpx;margin-top: 30rpx">
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
					<textarea placeholder="问你想问的..." maxlength="500" class="input" v-model="input" />
				</view>
				<view class="input-img" @click="sendMessage">
					<image class="input-img-size" src="/static/send.png" />
				</view>
			</view>
		</view>
		<touch></touch>
		<uni-popup ref="wechat">
			<image class="tt" src="/static/ball/wechat.jpg" :show-menu-by-longpress="true">
			</image>
		</uni-popup>
		<uni-popup ref="open">
			<view class="open-box">
				<input placeholder="填写新Key" maxlength="-1" class="input" v-model="key" style="padding: 5rpx" />
			</view>
			<button class="open-btn" @click="setKey">提交</button>
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
		setPainting,
		getModel,
		setModel,
		setOpenKey,
		getOpenKey
	} from "@/utils/data";
	import env from "@/utils/env";

	export default {
		components: {
			mpHtml,
			touch
		},
		data() {
			return {
				key: '',
				canvas: false,
				input: "",
				scrollInto: "",
				chat: [],
				data: [],
				first: true,
				succeed: true,
				model: "0",
				tagStyle: {
					p: 'font-size: 26rpx;padding-top: 8px;padding-bottom: 8px;margin: 0;line-height: 26px;color: white',
					h1: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 34rpx;color: white',
					h2: 'margin-top: 30px ;margin-bottom: 15px;font-weight: bold;font-size: 32rpx;color: white',
					h3: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 30rpx;color: white',
					h4: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 28rpx;color: white',
					h5: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 26rpx;color: white',
					h6: 'margin-top: 30px;margin-bottom: 15px;font-weight: bold;font-size: 26rpx;color: white',
					ol: 'margin-top: 8px;margin-bottom: 8px;padding-left: 25px;color: black;color: white',
					ul: 'margin-top: 8px;margin-bottom: 8px;padding-left: 25px;color: black;color: white',
					li: 'margin-top: 5px;margin - bottom: 5 px;line - height: 26 px;color: white',
				},
				currentSize: 0
			}
		},
		onLoad() {
			const _this = this
			this.init()
			uni.$on('openHistory', function() {
				_this.openHistory()
			});
			uni.$on('openModel', function() {
				setModel("1")
				_this.changeModel("1")
			});
			uni.$on('closeModel', function() {
				setModel("0")
				_this.changeModel("0")
				_this.removePainting()
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
			uni.$on('closePage', function() {
				_this.removeData()
			});
			uni.$on('removeErrMsg', function() {
				_this.removeErrMsg()
			});
		},
		methods: {
			init() {
				//初始化Key
				if (getOpenKey() === "") {
					setOpenKey(env.key)
				}
				this.enableCache()
				if (getModel() !== "") {
					this.model = getModel()
				}
			},
			removeErrMsg() {
				this.chat.splice(this.currentSize, 1)
			},
			setKey() {
				if (this.key.length <= 0) {
					uni.showToast({
						icon: 'none',
						duration: 3000,
						title: `OpenKey不能为空`
					});
					return
				}
				setOpenKey(this.key)
				uni.showToast({
					icon: 'none',
					duration: 5000,
					title: `替换成功 如要重置 请输入 remove-key`
				});
				this.$refs.open.close()
			},

			removeData() {
				this.removePainting()
				this.data = []
				this.chat = []
				this.canvas = false
			},
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
					data: "https://www.bilibili.com/video/BV1kD4y1M7C4/?spm_id_from=333.999.0.0&vd_source=247eccf88822f409670040957c2f29a9",
					success: res => {
						uni.showToast({
							icon: 'none',
							duration: 3000,
							title: `已复制链接到粘贴板`
						});
					}
				})
			},
			changeModel(p) {
				this.model = p
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

					this.data.push({
						chat: [{
							role: "user",
							content: this.input,
						}, {
							role: "assistant",
							content: "",
						}]
					})

					for (let i = 0; i < this.data.length; i++) {
						if (this.data[i].chat[1].content.length === 0) {
							this.data.splice(i, 1)
							this.chat.splice(i, 1)
						}
					}
				}
			},
			//发送
			sendMessage() {
				if (this.input === "open-key") {
					this.input = ''
					this.$refs.open.open('open')
					return
				}
				if (this.input === "remove-key") {
					setOpenKey(env.key)
					uni.showToast({
						icon: 'none',
						duration: 3000,
						title: `已重置为默认KEY~`
					});
					this.input = ''
					return
				}
				if (this.succeed) {
					this.canvas = true
					this.succeed = false
					//构建数据模型
					this.createModel()
					//获取渲染内容下标
					const size = this.data.length - 1
					this.currentSize = size
					//设置消息定位
					this.scrollInto = 'id-g-' + size;
					switch (this.model) {
						case "0":
							this.gpt003(size)
							break
						case "1":
							this.gptTurbo(size)
							break
					}
					//重置会话消息
					if (this.chat.length >= 10) {
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
			createModel() {
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
				this.input = ""
			},
			gpt003(size) {
				let parameter = ""
				//处理字符
				for (let i = 0; i < this.data.length; i++) {
					const a = this.data[i].chat[0];
					const b = this.data[i].chat[1];
					if (this.data.length === 1) {
						parameter = "User:" + a.content + "."
						break
					} else {
						if (this.data.length - 1 === i) {
							parameter += "\n" + "User:" + a.content + "."
						} else {
							parameter += "\n" + "User:" + a.content + "." + "\n" + "Bot:" + b.content.trim().replace(
								/^[\n]{3}/, "");
						}
					}
				}

				this.$store.dispatch('gpt/gpt003', parameter).then(res => {
					//输出到聊天框
					this.outPutFont(res.data['choices'][0]['text'].replace("Bot:", ""), size)
				}).catch((error) => {
					this.data[size].chat[1].content = error
					//删除聊天缓存
					this.chat.splice(size, 1)
					
				}).finally(() => {
					console.log(this.chat)
					//等待写入完毕后更新数据
					if (getHistoryEnable() === "1") {
						this.updateHistory(this.data)
					}
					this.input = ""
					this.succeed = true
				})
			},
			outPutFont(text, size) {
				const _this = this
				//模拟打印字效果
				let i = 0;

				let timer = setInterval(function() {
					try {
						if (i <= text.length) {
							_this.data[size].chat[1].content = text.slice(0, i++) + '_|'
						} else {
							_this.data[size].chat[1].content = text
							//关闭
							clearInterval(timer)
						}
						//模拟手机短频震动
						uni.vibrateShort()
					} catch (e) {
						clearInterval(timer)
					}
				}, 10);
			},
			gptTurbo(size) {
				this.$store.dispatch('gpt/gptTurbo', this.chat).then(res => {
					const gptText = res.data['choices'][0]['message']['content'];
					this.outPutFont(gptText, size)
				}).catch((res) => {
					this.data[size].chat[1].content = res
					//删除聊天缓存
					this.chat.splice(size, 1)
				}).finally(() => {
					console.log(this.chat)
					this.input = ""
					this.succeed = true
				})
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
				removePainting()
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

	.tt {
		width: 300rpx;
		height: 300rpx;
	}

	.open-box {

		background-color: #3a3a3a;
		padding: 20rpx;
		border-radius: 20rpx;
		text-align: center
	}

	.open-btn {
		margin-top: 20rpx;
		background-color: #333233;
		font-size: 28rpx;
		width: 200rpx;
		color: white
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
