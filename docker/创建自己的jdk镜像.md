### 创建dockerFile

```sh
FROM centos:7
MAINTAINER  jameswang@
ADD jdk-8u144-linux-x64.tar.gz /usr/local/
ENV JAVA_HOME /usr/local/jdk1.8.0_144
ENV CLASSPATH $JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
ENV PATH $PATH:$JAVA_HOME/bin
```
### 使用Dockerfile创建镜像

```sh
sudo docker build -t jdk-8u192:latest . -f Dockerfile
```

### 给你要上传的镜像打个tag

```sh
docker tag [source_name] xxx.xxx.xx.xx:8082/[target_name]:latest
```

### push 

```sh
sudo  docker push xxx.xxx.xx.xx:8082/jdk-8u192
``
