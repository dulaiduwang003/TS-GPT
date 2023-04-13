# 部署教程

> 此教程依次从后端部署开始 请严格参照, 此教程后端为IDEA源码打包方式
> 关于后端用其他语言实现请查看API.md 请严格遵照入参出参格式
> 可以共同维护此项目

[GitHub作者主页](https://github.com/dulaiduwang003/ChatGPT_wechat)

## 准备环境
- centOS 8 或更高
- JDK 17
- IDEA
- maven 3.6.0 以上
- MySQL 8
- Redis 7

## 后端源码
1. 创建MySQL数据库 取名为 super_bot
2. 将后端源码中的 .sql目录下的所有sql文件执行到该库中 也就是导入表数据
3. 找到application-prod.yml 将SSL证书放置在同目录下 , 如果使用其他方式配置请忽略
4. 另外we-chat参数为必填
```yaml
## application-prod.yml
server:
 # SSL证书
 ssl:
  key-store: classpath:XXX.pfx
  key-store-password: 证书密码
  key-store-type: PKCS12

# 微信APPID
we-chat:
 appId: '微信应用ID'
 secret: '微信密钥'

```
4. 找到application-prod.yml 配置好mysql以及redis中间件 以及控制台用于服务器策略配置以及小程序运营
```yaml
## application-prod.yml
spring:
  data:
    redis:
      database: 0
      host: 服务器IP
      port: 6379
      password: 'redis密码'
  datasource:
    url: jdbc:mysql://服务器IP:3306/super_bot?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowMultiQueries=true&useSSL=true
    username: mysql账号
    password: 'mysql密码'

# 管理员账号
console:
  account: '你要配置的账户名'
  password: '你要配置的密码'

```
5. 配置完毕后 找到maven 配置好环境开始打包 如图所示
![打包](/static/config.png)
6. 恭喜! 不出意外 可以得到 target目录 其中就包含了已经打包好的
![打包](/static/jar.png)
7. 之后部署到自己服务器即可 不会部署Jar 请自行百度, 太简单了 不想出教程

## 小程序端部署

1. 首先在小程序源码目录下执行命令
> #### 如果执行报错 请检查本机是否安装了node环境
> npm install 

2. 找到 Super003/utils下的 env.js文件 将以下配置修改
```JavaScript
"use strict";

export default {
	appid: '配置为你的微信APPID',
	baseUrl: 'https://配置为你的域名:9892/',
    advertisement: '微信广告ID'
}
```
3. 找到 Super003下的 manifest.json文件 找到微信相关配置 配置好APPID
```json
    "mp-weixin" : {
        "appid" : "你的APPID",
        "setting" : {
            "urlCheck" : false
        },
        "lazyCodeLoading" : "requiredComponents",
        "usingComponents" : true
    }
```

4. 之后请将你的域名配置到你的微信白名单中(也可以放在后面步骤) 后使用HBuild开发工具运行到微信开发者工具即可 
5. 运行后选择微信授权登录 (因为目前服务器配置都还未配置 是无法使用的) 登录之后可以看到 访问控制台选项

![配置](/static/mine.jpg)
 之后回想后端部署步骤时在yml配置的管理账号和密码 用于登录的令牌及密钥 (后端步骤4)

6. 登录成功后应该可以看到如下界面
![管理](/static/server.png)
7. 之后点击配置策略应该能看到如下界面
![策略](/static/config.jpg)

-  适用于国内服务器 配置 VPN 访问方式 (代理模式)
> 以clash举例 一般配置好后 ip为 127.0.0.1 port为 7890

- Linear Regression配置 (直连模式)
> 算法密钥 为你的官网 OpenAi Key

- Standard配置 (第三方API模式) 
> 链接格式为 https://XXX/ (第三方API后缀必须满足官网格式)
> 认证参数填 第三方提供的 bearer auth认证TOKEN就行 

- Nerve Algorithm 配置 (额外的GPT-4配置 支持任意 如官网 或第三方)
> 链接格式为https://XXXX/v1/chat/completions (请填写全URL)
> 认证参数填 官网openKey或第三方提供的 bearer auth认证TOKEN就行 

- Bing Alorithm配置
> 填写bing cookie即可

- Face Recognition配置
> 填写SD匹配路径即可

- 文心一言 配置
> 接口链接配置全URL
> 认证参数 配置认证Token即可

### 这里是额外的介绍

- 紫色表示 GPT3.5  默认调用消耗次数为1
- 绿色表示 文心一言  默认调用消耗次数为1
- 蓝色表示 GPT绘图  默认调用消耗次书为5
- 粉色表示 GPT4.0  默认调用消耗次数为4 上传图片时消耗提升至6
- 人像生成 SD      默认调用次数消耗为10
- 其他功能均消耗次数为1次

>流量主广告ID 请在小程序源码中自行修改配置 另外以上调用次数均可以在手动调整 如下面newBing接口实例
>请注意这个  BlockKeywords 自定义注解 limit = 1表示 调用该接口会消耗用户1次使用次数 如果用户次数不够则会提示用户次数不足
> check参数 表示 为true时表示 是否对用户输入内容以及AI回复内容做过滤处理 当然也可以直接不要参数如: @BlockKeywords
> 因为 @BlockKeywords 默认参数为 消耗1次 且 过滤处理默认开启
```java
/**
     * New bing result.
     *
     * @param dto the dto
     * @return the result
     */
    @BlockKeywords(limit = 1, check = false)
    @PostMapping(value = "/chat/bing", name = "new-bing", produces = MediaType.APPLICATION_JSON_VALUE)
    public Result newBing(@Validated @RequestBody final NewBingDto dto) {
        try {
            return Result.data(chatService.bing(dto.getParameter()));
        } catch (CustomException e) {
            log.error("Failed to fetch bing model API");
            return Result.error(e.getMessage(), e.getCode());
        }
    }

```
### 此外默认流是关闭的 请参照自行修改 因为微信小程序审核过于严格 不得不放弃流(过滤敏感词需要)
![策略](/static/stream.png)
