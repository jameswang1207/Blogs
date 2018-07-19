# docker 网络连接

### Docker 容器的网络基础

* docker0 osi 七层模型中的网桥 网桥是在数据链路层设备

| 网络七层 |
|------|
| 应用层 |
| 表示层 |
| 会话层 |
| 传输层 |
| 网络层 |
| 数据连路程 |
| 物理层 |

* Linux虚拟网桥的特点
- 可以设置ip地址
- 相当于拥有一个隐形的虚拟网卡

* docker0 的地址划分：
- inet addr:172.17.0.1  Bcast:172.17.255.255  Mask:255.255.0.0
- Mac：02:42:ac:11:00:00 到 02:42:ac:11:ff:ff
- 总共提供的65534个地址

* linux 中的网桥管理工具
```sh
  sudo apt-get install bridge-utils
  # 查看网桥
  sudo brctl show
  # 给docker0设置新的ip地址
  sudo  ifconfig  docker0 192.168.54.132 netmask 255.255.255.0
  # 自定义新的虚拟网桥
  # 添加虚拟网桥
  sudo brctl addbr br0
  sudo ifconfig br0 192.168.54.132 netmask 255.255.255.0
  # 更改docker守护进程的启动配置
  /etc/default/docker 中添加DOCKER_OPS值
  -b=br0
```

### Docker 容器的互联
* Docker 在默认情况下是应许所有容器相互连接的

```sh
  # 实验的Dockerfile
  FROM ubuntu:16.04
  RUN apt-get install -y ping
  RUN apt-get install -y nginx
  RUN apt-get install -y curl
  EXPOSE 80
  CMD /bin/bash
  # 构建镜像
  docker build -t jamesimages .
  # Docker 默认情况下，同一台机器上的容器间是通过网桥互相连接的
  # 默认参数：--icc=true docker应许容器间的连接
  # docker 在重新启动时每次的ip都会变化
  # --link通过容器代号来访问容器，这样避免了通过ip地址访问容器ip发生变化带来的不便。--link后面的为需要访问的容器
  docker run --link=[container_name]:[alias] [commond]
  docker run -it --name ct2 --link=ct1:webct1 jamesimages
  # 这样做完后，在环境变量和/etc/hosts文件会发生改变
```

* 拒绝容器间互联
```sh
  --icc=false docker应许容器间的连接
  # 修改docker配置文件/etc/default/docker
  DOKCER_OPTS="--icc=false"
```
* 应许特定容器间互联
```sh
  # 在docker启动配置文件进项设置
  --icc=false --iptables=true
  # 在容器启动时使用--link命令来进行访问
  --link
  # 修改docker配置文件/etc/default/docker
  DOKCER_OPTS="--icc=false --iptables=true"
  # 清空iptables
  sudo iptables -F
  sudo iptables -L -n
```

### Docker 容器与外部网络的连接
* ip_forward
- --ip_forward=true，是否允许转发流量，设置为true，在容器启动时允许流量转发
```sh
  ps -ef | grep  nginx
  sysctl net.ipv4.conf.all.forwarding
```
* iptables
- iptables 是与Linux内核集成的包过滤防火墙系统，几乎所有的Linux发行版本都会包含iptables的功能。

- 表(table)
- 链(chain)：input、output、forward
- 规则(rule)：accept、reject、drop

```sh
  # 查看iptable
  sudo iptables -L
  sudo iptables -h
  # 查看对应参数
```
* 允许端口映射访问
* 限制容器访问
