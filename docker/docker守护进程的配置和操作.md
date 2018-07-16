# docker 守护进程的配置和操作

* 查看docker的运行状态

```sh
  ps -ef | grep docker
```

* 启动、停止、重启
```sh
  sudo service docker start
  sudo service docker stop
  sudo service docker restart

  # docker 启动选项
  # 于守护进程方式启动
  sudo docker -d [options]
  # options参数
  -D, --debug=false
  -e, exec-driver="native"
  -g, --graph="/var/lib/docker"
  -l, --log-level="info"
  --label=[]
  -p, --pidfile="/var/run/docker.pid"
  # 网络设置相关
```

* docker启动配置文件
- 文件在/etc/default/docker可在其中配置启动文件

* docker远程访问
- 安装docker的服务器（两台）
- 修改docker守护进程启动选项，区分两台服务器
- 保证client api 与server api版本保持一致

* Dockerfile指令格式：docker官方文档
- 注释：# commont
- 指令: 指令加参数
- 例子
```sh
  # FROM指令
  # 格式：
  FROM <image>
  FROM <image>:<tag>
  # 要求：
  # 必须是已经存在的镜像
  # 基础镜像
  # 必须是Dockerfile中的第一行
  # 例子：
  FROM ubuntu:14.04

  # MAINTAINER指令
  # 格式：
  MAINTAINER <name>
  # 要求：
  # 指定镜像的作者信息，包含镜像的所有者和联系信息
  # 例子：
  MAINTAINER jameswang "wangyongxiang@ruijie.com.cn"

  # RUN指令：指定当前镜像构建时运行的命令
  # 格式：
  # shell 模式 在这种模式中是使用/bin/sh -c command
  RUN <command>
  # exec 模式 Run ['/bin/bash','-c','echo jame']
  RUN ['execuable','param1','param2'] 
  # 例子:
  RUN apt-get update
  # expose <port> {port} 用来指定容器使用的端口，使用一个或多个端口
  # 例子
  EXPOSE 80
  # 但是在启动时还是需要指定端口
  docker run  -p 80 -d [容器名字、容器id、容器repository、容器repository:tag] nginx -g "daemon off;"

  # 容器运行时运行的指令
  # 与RUN的区别：run在镜像构建是使用的指令，CMD在容器运行时执行的命令
  # CMD: 如果在运行docker run时指定容器运行的指令，那么CMD中的指令会被覆盖
  # CMD
  # 格式：CMD ["executable","parm1","param2"] (exec模式)
  CMD ['/bin/bash/nginx','-g','daemon off;']
  # 格式：CMD command param1 param2 (shell模式)
  # 格式：CMD ["parm1","param2"] (作为entrypoint的指令的默认参数)

  # ENTRYPOINT : 不会被docker run命令中指定的命令覆盖 和 CMD命令相似
  # 格式：ENTRYPOINT ["executable","parm1","param2"] (exec模式)
  # 格式：ENTRYPOINT command param1 param2 (shell模式)

  # 使用cmd命令与entrypoint联合使用
  ENTRYPOINT ["/bin/bash/nginx"] 指定命令
  CMD ['-g','daemon off;'] 指定参数
  # cmd 中的东西作为entrypoint的参数

  # 将文件和目录复制到dockerfile指定的文件中，设置镜像的目录和文件
  # ADD
  # 格式：
  # ADD <src> .... <dest>
  # ADD ["<src>" ..... "<dest>"] 使用于文件路径中有空格的情况
  # add 包含类似于tar的解压功能，如果单纯复制文件docker推荐使用copy
  # 例子：
  # 使用本地的一个网页覆盖nginx中的一个网页,注意index.html与dockerfile文件在同一个目录
  COPY index.html /usr/share/nginx/html
  # COPY

  # VOLUME ？？？ 共享文件

  # 镜像在构建和容器运行时的环境设置
  # WORKDIR /home/log用来在容器运行时容器内设置路径   使用绝对路径

  # ENV 用来设置环境变量
  # 格式：
  ENV <key> <value> 
  ENV <key>=<value> ....

  # USER : 进项对什么样的用户运行
  # USER nginx容器启动时使用nginx用户进行启动容器，如果不指定用户，默认使用root用户，USER user:group


  # 类似触发器一样的命令
  # OBBUILD 
  # 当一个镜像被其他镜像作为基础镜像时执行
  # 在构建过程中插入指令
```

* DockerFile 构建过程
- 从基础镜像运行一个容器
- 执行一条指令，对容器做出修改
- 执行类似dockercommit的操作，提交新的镜像层
- 在基于提交的镜像运行一个新的容器
- 执行dockerfile的下一条指令，知道所有的指令执行完毕、
- 可以通过dockerbuild来查看执行过程

- dockerfile在构建过程中，会生成中间层进项，并生成容器，但是在这个过程中中间层容器会被删除，中间层镜像不会被删除

- 中间层镜像给我们带来的好处
- 使用中间层镜像进行调试
- 查找错误

- 构建缓存
- docker在构建进项的过程中会将前面的一步镜像缓存起来，那么在构建的过程中会使用缓存，在dockerbuild执行过程中可以看到。（using cache的字样）
- 但是在有些情况下我们不需要使用缓存
```sh
  docker build --no-cache
```

* 一个小例子
```sh
  FROM ubuntu:14.04
  MAINTAINER jameswang "wangyongxiang@ruijie.com.cn"
  ENV REFRESH_DATE 2018-04-02 #修改时间可以避免使用缓存构建镜像
  RUN apt-get update
  RUN apt-get install -y nginx
  COPY index.html /usr/share/nginx/html
  EXPOSE 80
```

* 查看进行构建的过程
```sh
  # 他可以完整的显示镜像的构建过程
  docker history [image]
```



