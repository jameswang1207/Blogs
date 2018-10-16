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

## 接受所有ip的数据包转发
```shell
vi /lib/systemd/system/docker.service
   
#找到ExecStart=xxx，在这行上面加入一行，内容如下：(k8s的网络需要)
ExecStartPost=/sbin/iptables -I FORWARD -s 0.0.0.0/0 -j ACCEPT
```
## 设置系统参数 - 允许路由转发，不对bridge的数据进行处理
```shell
#写入配置文件
$ cat <<EOF > /etc/sysctl.d/k8s.conf
net.ipv4.ip_forward = 1
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
#生效配置文件
$ sysctl -p /etc/sysctl.d/k8s.conf
```shell

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
### 主节点中配置etcd（在生产环境中etcd必须部署集群模式）

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

# 部署APIServer（主节点）
## 配置为启动服务
```shell
# 参考后配置文件生成在/home/master/k8s/k8s-config/kubernetes-starter目录中
#把服务配置文件copy到系统服务目录
cd /home/master/k8s/k8s-config/kubernetes-starter/target/master-node
# 拷贝文件到指定目录
cp  ./kube-apiserver.service /lib/systemd/system/
systemctl enable kube-apiserver.service
service kube-apiserver start
journalctl -f -u kube-apiserver
```

## 文件配置说明
```shell
Description=Kubernetes API Server
Documentation=https://github.com/GoogleCloudPlatform/kubernetes
After=network.target
[Service]
ExecStart=/home/master/k8s/kubernetes/server/bin/kube-apiserver \
  --admission-control=NamespaceLifecycle,LimitRanger,DefaultStorageClass,ResourceQuota,NodeRestriction \
  --insecure-bind-address=0.0.0.0 \
  --kubelet-https=false \
  --service-cluster-ip-range=10.68.0.0/16 \
  --service-node-port-range=20000-40000 \
  --etcd-servers=http://172.17.8.77:2379 \
  --enable-swagger-ui=true \
  --allow-privileged=true \
  --audit-log-maxage=30 \
  --audit-log-maxbackup=3 \
  --audit-log-maxsize=100 \
  --audit-log-path=/var/lib/audit.log \
  --event-ttl=1h \
  --v=2
Restart=on-failure
RestartSec=5
Type=notify
LimitNOFILE=65536
[Install]
WantedBy=multi-user.target
```

# 部署ControllerManager（主节点）
## 配置为启动服务
```shell
# 参考后配置文件生成在/home/master/k8s/k8s-config/kubernetes-starter目录中
#把服务配置文件copy到系统服务目录
cd /home/master/k8s/k8s-config/kubernetes-starter/target/master-node
# 拷贝文件到指定目录
cp ./kube-controller-manager.service /lib/systemd/system/
systemctl enable kube-controller-manager.service
service kube-controller-manager start
journalctl -f -u kube-controller-manager
```
## 配置文件说明
```shell
Description=Kubernetes Controller Manager
Documentation=https://github.com/GoogleCloudPlatform/kubernetes
[Service]
ExecStart=/home/master/k8s/kubernetes/server/bin/kube-controller-manager \
  --address=127.0.0.1 \
  --master=http://127.0.0.1:8080 \
  --allocate-node-cidrs=true \
  --service-cluster-ip-range=10.68.0.0/16 \
  --cluster-cidr=172.20.0.0/16 \
  --cluster-name=kubernetes \
  --leader-elect=true \
  --cluster-signing-cert-file= \
  --cluster-signing-key-file= \
  --v=2
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target
```

# 部署Scheduler（主节点）
## 配置为启动服务
```shell
# 参考后配置文件生成在/home/master/k8s/k8s-config/kubernetes-starter目录中
#把服务配置文件copy到系统服务目录
cd /home/master/k8s/k8s-config/kubernetes-starter/target/master-node
# 拷贝文件到指定目录
cp ./kube-scheduler.service /lib/systemd/system/
systemctl enable kube-scheduler.service
service kube-scheduler start
journalctl -f -u kube-scheduler
```
## 配置文件说明
```shell
Description=Kubernetes Scheduler
Documentation=https://github.com/GoogleCloudPlatform/kubernetes
[Service]
ExecStart=/home/master/k8s/kubernetes/server/bin/kube-scheduler \
  --address=127.0.0.1 \
  --master=http://127.0.0.1:8080 \
  --leader-elect=true \
  --v=2
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target
```

# 部署CalicoNode（所有节点）
# Calicos使用的是IPtable因此效率更加高效写
## 配置为启动服务(calico是通过系统服务+docker方式完成的)
```shell
# 参考后配置文件生成在/home/master/k8s/k8s-config/kubernetes-starter目录中
#把服务配置文件copy到系统服务目录
cd /home/master/k8s/k8s-config/kubernetes-starter/target/master-node
# 拷贝文件到指定目录
cp ./kube-calico.service /lib/systemd/system
systemctl enable kube-calico.service
service kube-calico start
journalctl -f -u kube-calico
```

## 配置文件说明
```shell
[Unit]
Description=calico node
After=docker.service
Requires=docker.service

[Service]
User=root
PermissionsStartOnly=true
ExecStart=/usr/bin/docker run --net=host --privileged --name=calico-node \
  -e ETCD_ENDPOINTS=http://172.17.8.77:2379 \
  -e CALICO_LIBNETWORK_ENABLED=true \
  -e CALICO_NETWORKING_BACKEND=bird \
  -e CALICO_DISABLE_FILE_LOGGING=true \
  -e CALICO_IPV4POOL_CIDR=172.20.0.0/16 \
  -e CALICO_IPV4POOL_IPIP=off \
  -e FELIX_DEFAULTENDPOINTTOHOSTACTION=ACCEPT \
  -e FELIX_IPV6SUPPORT=false \
  -e FELIX_LOGSEVERITYSCREEN=info \
  -e FELIX_IPINIPMTU=1440 \
  -e FELIX_HEALTHENABLED=true \
  -e IP= \
  -v /var/run/calico:/var/run/calico \
  -v /lib/modules:/lib/modules \
  -v /run/docker/plugins:/run/docker/plugins \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v /var/log/calico:/var/log/calico \
  registry.cn-hangzhou.aliyuncs.com/imooc/calico-node:v2.6.2
ExecStop=/usr/bin/docker rm -f calico-node
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```
###  下载calicoctl
- [下载路径：calicoctl](https://github.com/projectcalico/calicoctl/releases/tag/v3.2.3)
- [下载插件：calico、calico-ipam](https://github.com/projectcalico/cni-plugin/releases)
- 并将其放在k8sbin目录下
- 将其权限修改为755











