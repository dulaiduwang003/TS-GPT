小程序体验版暂时关闭

![222880426-f4807bce-c073-49a9-a29a-e37a50b64bb4.png](https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/cd9dc8e8557d4030a394c54aaf44c02d~tplv-k3u1fbpfcp-watermark.image?)

**演示视频**-> [做一个开源chatgpt微信小程序chatgpt3.5turbo_哔哩哔哩_bilibili](https://www.bilibili.com/video/BV1kM411x7Ca/?spm_id_from=333.999.0.0)



**说一下大概有的功能**
-   模型新增GPT3.5增强版( 响应速度先比之前的模型快很多很多 ) 旧的模型也有保留 lottie悬浮窗开启
-   Lottie动画悬浮球控制台 可开启历史回溯 清除Ai会话 主/次页面返回
-   Ai回复内容时模拟文字打印机+手机短频震动效果 如不喜欢 两者可在配置文件中选择性开启
-   支持Ai回复的内容包含代码高亮 java .net c++ css html c python php sql go 如不够可自行添加高亮文件
-   支持ai渲染内容选择性复制 如果Ai回复内容为 代码 则长按复制代码
-   支持Ai保留会话功能 (能记住你的上一次对话,最多5条 可配置更改, 比如 用户: 中国历史有多长? Ai: 5000年 用户:那和美国相比呢? Ai: 中国更长!) 支持手动清除 或自动监测清除
-   支持开启历史回溯 ( 下一次打开保留对话记录和会话信息)
-   点击自己的回答时会直接填入文本框中
-   
**支持发布的平台**
只测试了 
-   微信小程序
-   支付宝小程序
-   抖音小程序 
-   **其他的未测试**
 
**如何使用呢?**

GitHub地址: [dulaiduwang003/ChatGPT_wechat (github.com)](https://github.com/dulaiduwang003/ChatGPT_wechat)   感觉可以或者喜欢的话可以点个star😊

1.使用HB工具导入项目
2.uitls包下找到 env.js文件 其中中有三个参数
**appid** = 填你的微信AppID 
**baseUrl** = 这个是openai域名 不需要改动
**key**= 填你的OpenAi开发者Key
**proxy**= 你的代理地址 具体数据格式请看 utils包下的 request.js,没有这个玩不了~ 因为Openai所有接口最近被墙了 无法直接被访问

如图
ps: 我用的是IDEA工具搞得~ 下了支持微信小程序开发和uniapp开发的插件 所以我这里是直接用IDEA运行, 大家用HB运行也可以的, 运行到微信开发者工具模拟器就有效果了~

![微信截图_20230304181727.png](https://p9-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/0110a254828948519a35617be173fed4~tplv-k3u1fbpfcp-watermark.image?)
