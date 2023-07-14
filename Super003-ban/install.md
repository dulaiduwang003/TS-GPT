# 部署教程

> 此教程依次从后端部署开始 请严格参照, 此教程后端为IDEA源码打包方式
> 关于后端用其他语言实现请查看API.md 请严格遵照入参出参格式
> 此版本禁止外传 除非 后续版本发布 才允许外出 君子协定

[GitHub作者主页](https://github.com/dulaiduwang003/ChatGPT_wechat)

## 准备环境
- centOS 8 或更高
- JDK 17
- IDEA
- maven 3.6.0 以上
- MySQL 8
- Redis 7
- SSL证书

## 后端源码以及部署方式
1. 开始在服务器安装Docker 进入 服务器终端后开始执行如下命令
> 安装需要的软件包
> 
> yum install -y yum-utils

> 添加镜像信息
>
> yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

> 安装DOCKER CE
>
> yum -y install docker-ce docker-ce-cli containerd.io

> 查看版本号验证
>
> docker -v

> 启动docker
>
> systemctl start docker

> 设置docker开机自启动
>
> systemctl enable docker

> 重启docker
>
> systemctl restart docker

2. 经过上面的步骤 已经安装好Docker 下面开始 安装 MySQL Redis

> 安装MySQL镜像
>
> docker pull mysql

> 安装好后 检查一下是否存在 如果 控制台有mysql 那就是拉取成功
>
> docker images

> 让我们开始创建一个mysql容器 --name 表示 容器名字 -p 表示 容器端口  -e 表示 设置 mysql密码 = bdthznb666  最后面的mysql 表示具体是使用哪个镜像 我们这里是创建 MySQL 所以写 mysql
>
> docker run -tid --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=bdthznb666 mysql

3. 执行上面命令后应该会打印出一串英文 表示你的MySQL已经启动完成! 让我们来测一下能否连接 ( 请在腾讯云安全组中开放3306端口 )

> 注意 如果使用Navicat 工具测试连接需要 额外做一些操作 
> 因为 mysql8 之前的版本中加密规则是mysql_native_password,
> 而在mysql8之后,加密规则是caching_sha2_password，
> 导致navicate连接不上mysql容器 我这里使用DataGrip(默认规则为caching_sha2_password)进行测试 
> 如图所示 测试成功! 如果不成功请联系群主 当然 如果你对自己很自信 那么可以略过测试 ~ 不影响
> 
> ![测试](/static/img.png)

4. OK! 让我们来安装 Redis

> 欸 和MySQL类似 我们先拉取镜像
>
> docker pull redis

5. 不写废话 和MySQL类似 让我们来检查一遍镜像是否安装成功

> 检查镜像 , 不出意外 应该会显示 mysql 和 redis
>
> docker images

6. 让我们开始创建redis容器

> 创建redis容器并设置密码 --name 表示容器名字 -p表示端口  --requirepass 表示设置 redis密码 为 bdthznb666
>
>  docker run -tid --name redis -p 6379:6379 redis --requirepass bdthznb666

7. 是的 是的 走到这里不容易 你已经创建成功了 让我们测试一下 我这里使用 RedisInsight-v2 工具进行测试

> 填入ip地址以及 port 显示 Connection is successful 表示测试连接成功! 记得在腾讯云安全组 开放 6379端口再进行测试 其他平台也一样
>
> ![测试](/static/img_1.png)

8. 最后我们需要记录一些 docker的mysql redis容器ip

> 输入如下命令 应该会 显示出 当前运行 容器 的 ip地址 比如 172.XXX.X.X 我们先记录一下这些内容
> 
> docker inspect -f '{{.Name}} => {{.NetworkSettings.IPAddress }}' $(docker ps -aq)

### 让我们正式开始部署项目

1. 创建MySQL数据库 取名为 super_bot
2. 将后端源码中的 .sql目录下的所有sql文件执行到该库中 也就是导入表数据
3. 找到application-prod.yml 将SSL证书放置在同目录下 , 如果使用其他方式配置请忽略 另外we-chat参数为必填 另外注意file配置 该项为GPT4.0图片功能
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
4. 找到application-prod.yml 配置好mysql以及redis中间件 以及控制台用于服务器策略配置以及小程序运营 另外注意!!! 还记得 我们之前查容器ip吗? 请把mysql的ip换为我们记录的ip
```yaml
## application-prod.yml
spring:
  data:
    redis:
      database: 0
      ## 这里选  docker inspect -f '{{.Name}} => {{.NetworkSettings.IPAddress }}' $(docker ps -aq) 命令打印出来的 redis容器ip
      host: 容器内部IP
      port: 6379
      password: 'redis密码'
  datasource:
   ##  同理 
    url: jdbc:mysql://容器内部IP:3306/super_bot?useUnicode=true&serverTimezone=Asia/Shanghai&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&autoReconnect=true&allowMultiQueries=true&useSSL=true
    username: mysql账号
    password: 'mysql密码'

# 管理员账号
console:
  account: '你要配置的账户名'
  password: '你要配置的密码'
  
file:
 # 缓存路径 这个表示线上环境服务器 图片存储地址 如果出现上传失败 请注意检查服务器是否拥有该文件夹的访问权限 并且在服务器创建该目录
 path: /apps/chat/resource/
 # 服务器域名 这里选你的域名!!!! 记住是域名 而不是ip
 domain: https://XXX:8624


```
5. 配置完毕后 找到maven 配置好环境开始打包 如图所示
![打包](/static/config.png)

6. 恭喜! 不出意外 可以得到 target目录 其中就包含了已经打包好的
![打包](/static/jar.png)


7. 之后打开 请把 Dockerfile文件和 Super003-ban-0.0.1-SNAPSHOT.jar 一同上传到服务器 两个文件需要在同一个目录

> 这两个文件上传到服务器目录 如  /apps/chat/ 目录下
> 
> ![img_2.png](static%2Fimg_2.png)

8. 让我们开始部署吧!

> cd 进入  /apps/chat/ 执行下面这个命令 进行打包镜像 后面的 .不要少了
>
> docker build -t gpt .

9. 该命令会自动安装JDK环境 之后等待完成后 开始运行镜像

> 开始运行容器 
> 
> docker run -tid --name gpt -p 8624:8624 gpt

10. 让我们检查一下项目是否启动成功

> 查看工件日志 之后如果工件日志 没有报错 恭喜你 你已经成功启动了
> 
> docker logs --tail=2000 gpt
> 
> 让我们看一下现在docker中的容器运行 运行下面命令后应该会有 3个工件 redis mysql gpt!!!!
> 
> docker ps 

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
- 请在微信公众平台开放白名单 request配置为你的域名 updateFile同样需要配置为你的域名

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
