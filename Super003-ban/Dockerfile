# Docker image for springboot file run
# VERSION 0.0.1
# Author: OuJianfeng
 # 基础镜像使用java
FROM openjdk:17
 # 作者
MAINTAINER OUJIANFENG
 # 指定了临时文件目录为/tmp
# VOLUME /tmp
 # 设置工作目录
WORKDIR /apps/gpt
 # 将jar包添加到容器中并更名为gpt.jar
ADD Super003-ban-0.0.1-SNAPSHOT.jar gpt.jar
 # 设置时区
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && \
    echo "Asia/Shanghai" > /etc/timezone
 # 创建一个空文件
RUN touch /gpt
 # 运行jar包
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/apps/gpt/gpt.jar"]
