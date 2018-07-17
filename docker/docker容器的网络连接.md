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
### Docker 容器与外部网络的连接
