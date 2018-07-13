# docker 简介

### 什么是容器？

* 依托于Linux内核的虚拟化技术
* 操作系统级别的虚拟化
* 只能运行在相同或相似内核的操作系统上
* 依赖于linux的内核技术：Namespace 与Cgroup

### 什么是docker？

* 将应用程序部署到容器中
* Go 语言编写
* 2013 年有dotcloud 公司创建
* 基于apache 2.0 开源协议发行

### docker的目标？

* 提供简单轻量的建模方式(容易上手、启动速度快、同一台多个容器)
* 职责的逻辑分离（dev开发程序、运维注意维护容器）
* 快速高效开发生命周期（dev、staging、pro一样的开发环境）
* 面前服务的架构（一个容器部署一个应用程序、避免同一服务器部署不同服务带来的问题）

### docker使用场景

* 使用docker容器开发、测试、部署服务
* 创建隔离的运行环境
* 搭建测试环境（在本地测试线上环境问题）
* 提供平台及服务的基础设施（pass）
* 提供软件及服务应用程序（saas）
* 高性能、超大容量的宿主机部署

###  docker 的基本组成

* docker client 客户端
> docker c/s架构，我们通过docker客户端执行各种命令，docker client发送请求给docker守护进程，守护进程将命令执行完后传回给客户端，我们则你可以看到命令执行的结果 

* docker daemon 守护进程

* docker image 镜像 -- 类似于构建打包
> - 容器的基石:容器基于镜像来启动（镜像可以理解为容器的源代码保存了用于启动文件的各种条件）. 
> - 层叠的只读的文件系统:最底层是bootfs（引导启动文件）,当容器启动后，他将被移动到内存中，引导文件系统就被卸载. rootfs文件系统(Ubuntu、centos)
> - 总结：是一种使用联合加载技术实现的层叠的只读文件系统。

* dokcer container 容器 -- 启动执行
> 通过镜像启动容器，Docker容器是docker的执行单元，容器中可以运行客户的一个或多个进程。

* docker registry 仓库
> 用来保存用户生成的镜像。

* Docker 各个部件件的关系
> docker-client通过命令访问docker-daemon，docker-daemon将命令发送给docker容器，docker 容器执行完命令后将结果返回给客户端，docker-container是通过docker-image生成的，而镜像是通过docker仓库来进行管理的。 

###  容器相关技术简介

* Linux namespaces 命名空间
> - 代码级别： 代码隔离
> - 操作系统：系统资源的隔离（进程、网络、文件系统）

* docker namespaces 命名空间
> - pid（process ID） 进程隔离
> - net（network） 管理网络接口
> - ipc (InterProcess communication) 管理跨进程通讯的访问
> - mut (Mount) 管理挂载点
> - uts（unix timesharing system）隔离内核和版本标识

* Control groups 控制组
> - 用来分配资源
> - 来源google
> - Linux 整合
> - 资源限制
> - 优先级设定
> - 资源计量
> - 资源控制（进程挂起和启动）

* 给Docker 带来了哪些能力
> - 文件系统的隔离：每个容器都有自己的root文件系统
> - 进程隔离：每个容器都运行在自己的进程环境中
> - 网络隔离：容器间的虚拟网络接口和IP地址的分开
> - 资源隔离和分组：使用cgroup将cpu和内存之类的资源独立分配给Docker容器