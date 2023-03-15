-----------------


**前言** 小程序基于主要基于uniapp SpringBoot node(可选) 实现 2023/3/16本地仓库仍是旧版 新版将会在 3/24号进行覆盖分支
以下功能介绍 基于第二版本实现 效果图在最后面
## 快速体验
![gh_02bcd3e715fe_258](https://user-images.githubusercontent.com/87460202/225406059-b97b5bf4-2844-4261-a8dc-0893394a847a.jpg)

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
* **支持黑夜模式(部分平台不适用)**
* **支持Stream流数据控制(部分平台不适用)**
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
  可正常编译至字节小程序平台 (部分功能不支持 详细请查看控制面板)

### 项目环境(必需)

* [**JDK>=17**](golang_install_guide)
* [**CentOS>=7**](golang_install_guide)
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

### 有问题可以找我 WX: SeatimeIsland



----------------------
![微信截图_20230316013005](https://user-images.githubusercontent.com/87460202/225406312-2e0c4080-ef2c-42ba-94f0-3467de6550de.png)
![微信截图_20230316013005](https://user-images.githubusercontent.com/87460202/225406292-2221d842-1edb-4493-88ad-fff838b67266.png)
![微信截图_20230316013057](https://user-images.githubusercontent.com/87460202/225406349-fb75d60a-4395-4503-9595-ed6b125327b5.png)
![微信截图_20230316013020](https://user-images.githubusercontent.com/87460202/225406370-6a2f01b9-bdd8-4f17-a9b0-a7515428a86a.png)
![微信截图_20230316013040](https://user-images.githubusercontent.com/87460202/225406573-5509048c-0aef-4a38-9d62-83659734cb1b.png)

