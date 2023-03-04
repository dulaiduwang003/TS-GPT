# ChatGPT小程序

部署平台可选 微信小程序 支付宝小程序 抖音小程序 其他平台暂未测试
右上角点一下star 支持一下😊

### 快速体验
* 微信搜索 SuperBot 或 微信扫一扫
* ![b55644d9e813d21b8411c0401f737f5c](https://user-images.githubusercontent.com/87460202/222879382-b80c419b-ddab-4f88-9bf5-e5f38c028a32.jpg)

### 更新日志 有问题可反馈在 B站-> https://space.bilibili.com/1876478054?spm_id_from=333.1007.0.0
* 不在提供SpringBoot版本 有点多余 已去除
* 新增GPT3.5 tubro文本生成能力接口 旧的模型接口已保留 可在api包下切换
* 新增悬浮球控制台 去除原有文字控制台
* 修复悬浮球中 lottie动画在ios机型拖动掉帧问题
* 修复历史记录数据最后一条始终未null
* 优化模拟文字打印机效果
* 代码高亮新增 java .net c++ css html c python php sql 
* 支持ai渲染内容选择性复制 如果回复内容为 代码 则长按复制代码
* 新增返回主页按钮 主/次
* 支持Ai保留会话功能 (能记住你的上一次对话,最多5条 可配置更改) 支持手动清除 或监测清除
* 支持开启历史回溯 ( 下一次打开保留对话记录和会话信息)


### 如何使用呢?
1. 在utils包下 env.js文件中有三个参数 appid = 填你的微信AppID baseUrl不用改动这个是openai域名 key= 填你的OpenAi开发者Key
2. 在HB中模拟运行试试效果 没问题发布即可

### 打赏 支持一下🤠
![aa3439de05ac9b9a9c442904c73f057](https://user-images.githubusercontent.com/87460202/222879844-6f8f35ab-4310-48ff-9461-599ee6edbe98.jpg)

### 运行效果 
![微信截图_20230304101812](https://user-images.githubusercontent.com/87460202/222880426-f4807bce-c073-49a9-a29a-e37a50b64bb4.png)
![微信截图_20230304144024](https://user-images.githubusercontent.com/87460202/222880430-2a78232e-2560-4d8f-ad7a-e798009e47d1.png)
![微信截图_20230304144033](https://user-images.githubusercontent.com/87460202/222880436-eba3f9b9-18c1-4756-af98-6fd80d413a5f.png)
