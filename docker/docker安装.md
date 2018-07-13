# docker 安装

### Install docker for ubuntu
* 安装前的准备
* 内核版本
```sh
  uname -a
```

* 检查Device Mapper 映射驱动
```sh
  ls -l /sys/class/misc/device-mapper
```

* 安装方式
- 使用apt-get install ubunut
 
```sh
  # 由于apt官方库里的docker版本可能比较旧，所以先卸载可能存在的旧版本：
  sudo apt-get remove docker docker-engine docker-ce docker.io
  sudo  apt-get update
  # 安装以下包以使apt可以通过HTTPS使用存储库（repository）
  sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common
  # 添加Docker官方的GPG密钥
  curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
  # 使用下面的命令来设置stable存储库：
  sudo add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable"
  sudo apt-get update
  # 列出可用的版本
  apt-cache madison docker-ce
  # 安装指定版本
  sudo apt-get install docker-ce=<VERSION>
  # 验证docker版本
  docker version
  # 经典的hello world
  sudo service  docker status
```

## Docker 基本操作
### 下面这些命令是在命令结束后容器就停止了
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
  # 不加参数 返回的是真在运行的容器
  docker inspect [容器的ID或是容器的名字]
```

* docker自定义容器的名字

```sh
  docker run --name='自定义名' -i -t images名字 /bin/bash
```

* 重新启动已经停止的容器
```sh
  docker start -i 容器名
```

* docker rm 删除停止的容器不能删除在运行的容器

```sh
  docker rm 容器id或是容器名
```

### 守护式容器

* 什么是守护式容器
- 能长期运行
- 没有交互式会话
- 适合运行应用或者服务

* 以守护进程形式运行容器
```sh
  docker run -i -t IMAGE /bin/bash
  # 在结束时使用 退出不结束(连续按Ctrl+q和Ctrl+p)

  docker attach 【容器名、可以是ID】
  # 怎样进入已经退出了的容器

  docker run --name 容器的名字 -d 镜像名 【commond】【args】
  docker run --name doc1 -d ubuntu /bin/bash -c "while true ; do echo hello world ; sleep 1; done"
  # -d 使用后台进程进行运行
```

* 查看容器的日志
```sh
  docker logs [-f][-t][--tail] 容器名
  # -f  --follows = true | false 默认为false 跟踪日志变化返回结构
  # -t  --timestamps=true | false 默认为false 返回日志时间搓
  -- tail = 'all' 返回全部日志 或是返回几条
```

* 容器内进程的运行情况
```sh
  docker top "容器名"
```

* 在运行的容器中启动新的进程
```sh
  docker exec [-d][-i][-t] 容器名 [commond][arg]
```

* 停止运行中的容器
```sh
  docker stop 容器名 发送命令给容器等待容器停止后返回相应的结果
  docker kill 容器名 直接将进程杀死
```

* docker自带使用手册

```sh
  man docker-run
  man docker-logs 
  man docker-exec
  man docker-top
  .....
```

* docker 部署静态网站

* 设置容器的端口映射
```sh
  # containerPort 指定容器的端口
  docker run -p 80 -i -t ubuntu /bin/bash
  # hostPort:containerPort 指定ip和容器端口
  docker run -p 8080:80 -it ubuntu /bin/bash
  # ip:containerPort
  docker run -p 0.0.0.0:80 ubuntu /bin/bash
  # ip:hostPort:containerPort
  docker run -p 0.0.0.0:8080:80 ubuntu /bin/bash
```

* nginx 在容器中的部署
- 创建映射80端口的交互式容器
- 安装nginx在容器中
- 安装文本编辑器vim
- 创建今天页面
- 修改nginx配置
- 运行nginx
- 验证网站的访问

```sh
  # 启动一个容器并开启在容器的80端口
  sudo docker run -p 88 --name web -i -t ubuntu /bin/bash
  # 查看docker的端口
  sudo docker port web
  # 查看docker进程的运行情况
  sudo docker top web
```
