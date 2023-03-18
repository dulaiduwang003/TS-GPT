-----------------


**前言** 
小程序基于主要基于uniapp SpringBoot node 实现 
#### ChatGPT -> 小程序源码
#### ChatGPT-BOT -> ChatGPT源码(java)
#### NewBing-BOT -> 必应源码(node)

## 2023-3-18
参照第一版的影响对第二版某些功能进行了删减 至于原因你们应该都懂(完整功能源码可私信找我 至少能让我知道你在sc~)~ 
主要删减功能为 流数据传输  黑夜模式 可控Key 
此项目主要提供给大家学习参考 
#### 不过这一版我还是会继续维护的 比如 添加 可控标签功能 可轮换保存 , 另外接入GPTPS也有在搞 会在26号左右push(功能会额外集成至悬浮球上) 
#### 另外IDEA插件版也有做 可找我拿(提供jar 不提供源码)
####  此版本适用于VPN 如果需要海外服务器部署需要另一版( 懒得上传了 直接找我拿吧,只是参考群友大部分都没得海外服务器的原因所以第二版直接使用代理,这样对某些小白也很方便能玩上 )

##### 额外的信息
ps: 居然在咸鱼能看到第一版源码售卖(main) 我擦 ~


## 功能演示
[哔哩哔哩视频演示](https://www.bilibili.com/video/BV19L411y7nZ/?spm_id_from=333.999.0.0)

以下功能介绍 基于第二版本实现 效果图在最后面
## 快速体验
![224472294-45bd1dd2-95ee-4a50-8789-d3005e4fdd3c](https://user-images.githubusercontent.com/87460202/226098142-18dff969-171b-4ea0-a4ce-065af8c34e79.jpg)

## 主要功能

### ChatGPT对话能力

支持连续读写(Ai能记住你的上一次对话) Stream流数据渲染 支持保留历史对话记录 支持实时渲染高亮code代码块部分内容
支持长按代码块复制代码内容

### ChatGPT绘画能力

基于输入内容生成图片 支持图片预览以及保存

### NewBing必应对话能力

支持实时渲染高亮code代码块部分内容 支持长按代码块复制代码内容

#### 其他功能以及补充

* **支持清理页面数据或会话数据**
* **输入console可打开数据面板**
* **接入微信客服(可定义为自己)**
* **移除了003模型支持**
* **新增Key池 可设置单个或多个Key**
* **支持第三方Key 可在yml中切换或控制面板中选择使用哪种加载方式**
* **长按悬浮球3秒打开支持面板**
* **引入NewBing对话能力**
* **单击悬浮球可轮换功能**
* **~~支持黑夜模式~~**
* **~~支持Stream流数据控制~~**
* **支持小程序分享功能**
* **新增一些过程动画**
* ps: 输入框最左为清理会话 中间为 设置面板 最右边表示清理当前页面所有数据(包括会话)

### 支持的平台

* [**ios**](https://github.com/tensorflow/tensorflow/tree/master/tensorflow/tools/tf_sig_build_dockerfiles):
  可正常编译至ios平台
* [**android**](tensorflow_runtime_dockerfiles):
  可正常编译至android平台
* [**微信小程序**](manylinux2014_docker_images):
  可正常编译至微信小程序平台 (部分功能不支持 详细请查看控制面板)
* [**字节小程序**](https://github.com/uvarc/rivanna-docker):
  可正常编译至字节小程序平台
* [**支付宝小程序**](devinfra_windows_rbe):
  可正常编译至支付宝小程序平台 (部分功能不支持 详细请查看控制面板)

### 项目环境(必需)

* [**JDK>=17**](golang_install_guide)
* [**CentOS>=8**](golang_install_guide)
* [**NodeJS>=18**](golang_install_guide)
* [**海外服务器**](golang_install_guide)
* [**国内服务器(小程序平台接入需备案)**](golang_install_guide)
* [**如果没有海外服务器则可在国内服务器中使用VPN(Clash)**](golang_install_guide)

### 部署

1.拉取项目后执行命令
<code >
npm install
</code>

2.使用Hb工具打开项目 并中找到util包下的env.js修改相应配置

3.点击根目录下的 manifest.json 找到要部署平台的appid 修改为自己的appid

4.HB导航栏选择运行到XXX模拟器(具体看你部署平台)

5.拉取后端项目 找到application.yml 可根据自己需求调整相应配置

6.无误后maven打包上传jar 开始跑........

7.newBing 工件需要额外 node环境部署 请确保你的服务器有node且版本大于 18  拉下来运行
<code >
npm install
</code>

<code>
npm run start
</code>
### 有问题可以找我 WX: SeatimeIsland
