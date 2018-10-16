#  安装环境配置

| 系统类型 | IP地址 | 节点角色 | CPU | Memory | Hostname |
| :-----: | :---------: | :----: | :----: | :-: | :----: |
| centos7 | 172.17.8.77 | master |   4    | 16G | master |
| centos7 | 172.17.8.78 | worker |   4    | 16G | node01 |
| centos7 | 172.17.8.79 | worker |   4    | 16G | node02 |

# 安装必要的软件
```shell
# epel-release软件包，这个软件包会自动配置yum的软件仓库
yum -y install epel-release
```

#  安装docker

```shell
# 安装docker
yum -y  install docker
# 启动docker
systemctl daemon-reload
service docker start
```

# 系统设置（所有节点）
## 防火墙配置
```shell
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
# 关闭Linux防火墙
setenforce 0 
```
## 安装go环境变量


##  配置host文件
```shell
#配置hostname
vi /ect/hostname
# 172.17.8.77配置master
# 172.17.8.78配置node01
# 172.17.8.79配置node02

#配置host，使每个Node都可以通过名字解析到ip地址
vi /etc/hosts
#加入如下片段(ip地址和servername替换成自己的)
172.17.8.77 master
172.17.8.78 node01
172.17.8.79 node02
```
## 下载二进制文件

- [下载版本1.11](https://v1-11.docs.kubernetes.io/docs/imported/release/notes/) 
- 解压到home目录下并配置环境变量

```shell
# master节点，其余节点一致，将k8s_home目录进行修改即可
export K8S_HOME=/home/master/k8s/kubernetes/server
export PATH=.:${K8S_HOME}/bin:$PATH
```

# 下载etc
- [v3.3.10](https://github.com/etcd-io/etcd/releases)
- 注意下载过程中文件大小(etcd-v3.3.10-linux-amd64.tar.gz)
- 将etcd解压并将ectd、etcdctl拷贝到k8sbin目录中

# 生成k8s必要的配置文件
- [生成配置文件参考](https://github.com/liuyi01/kubernetes-starter)

# 部署ectd
### 主节点中配置etcd

```sh
#参考后配置文件生成在/home/master/k8s/k8s-config/kubernetes-starter目录中
#把服务配置文件copy到系统服务目录
cd /home/master/k8s/k8s-config/kubernetes-starter/target/master-node
# 拷贝文件到指定目录
cp  ./etcd.service /lib/systemd/system/
#enable服务
$ systemctl enable etcd.service
#创建工作目录(保存数据的地方,和配置文件中应该保持一致)
$ mkdir -p /var/lib/etcd
# 启动服务
$ service etcd start
# 查看服务日志，看是否有错误信息，确保服务正常
$ journalctl -f -u etcd.service
```












