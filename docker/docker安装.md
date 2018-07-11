# docker 安装

### Install docker for ubuntu
* 安装前的准备
> - 内核版本
> ```sh
>   uname -a
> ```
> - 检查Device Mapper 映射驱动
> ```sh
>   ls -l /sys/class/misc/device-mapper
> ```

* 安装方式
> - 使用apt-get install ubunut
> 
> ```sh
>   # 由于apt官方库里的docker版本可能比较旧，所以先卸载可能存在的旧版本：
>   sudo apt-get remove docker docker-engine docker-ce docker.io
>   sudo  apt-get update
>   # 安装以下包以使apt可以通过HTTPS使用存储库（repository）
>   sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
>   # 添加Docker官方的GPG密钥
>   curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
>   # 使用下面的命令来设置stable存储库：
>   sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
>   sudo apt-get update
>   # 列出可用的版本
>   apt-cache madison docker-ce
>   # 安装指定版本
>   sudo apt-get install docker-ce=<VERSION>
>   # 验证docker版本
>   docker version
>   # 经典的hello world
>   sudo service  docker status
> ```

## Docker 基本操作
* 启动容器
```sh 
    # 一次交互命令
    docker run IMAGE[COMMADN][ARGS]
    # 自动交互容器-- 
    docker run -i -t IMAGE /bin/bash
    # -i --interactive=true | fase 默认是false 始终打开交互式终端
    # -t --tty=true | fase 默认是false 告诉docker给终端打开一个tty终端
```

* 查看容器
```sh
   docker ps [-a][-l]
   # -a 列出全部容器
   # -l 最近新建的容器
   # 不加参数
```