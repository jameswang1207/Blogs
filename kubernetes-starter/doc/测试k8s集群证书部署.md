## k8s 配置证书认证、etcd集群、master高可用

### 前提准备

- 开放外网安装最新版本的k8s

```shell
# 免密登录
ssh-keygen
ssh-copy-id  root@172.17.8.85
```

```shell
# 安装防火墙
yum install firewalld
```

```shell
# 安装网络工具
yum install net-tools
```

### 准备前的工作

```shell
# 操作节点
master01：172.17.8.82 
master02：172.17.8.83
node：172.17.8.84、172.17.8.85
```
### 开始部署
```shell
# 安装docker
 yum install yum-utils device-mapper-persistent-data lvm2
 yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
 yum update && yum install docker-ce-18.06.2.ce

#关闭所有节点的SELinux
#永久方法 – 需要重启服务器
#修改/etc/selinux/config文件中设置SELINUX=disabled ，然后重启服务器。
```
### 创建TLS证书和秘钥

- [创建TLS证书和秘钥](https://jimmysong.io/kubernetes-handbook/practice/create-tls-and-secret-key.html)

### 部署etcd

- 关闭所有etcd节点的防火墙
- ectd下载地址 https://github.com/etcd-io/etcd/releases 

```shell
service docker start
systemctl stop firewalld
systemctl disable firewalld
```
- 部署etcd请参见
- [etcd集群部署](https://jimmysong.io/kubernetes-handbook/practice/etcd-cluster-installation.html)

- 查看etcd服务是否正常
```shell
journalctl -f -u etcd.service
```

### 部署fannel
- [flannel原理](https://blog.csdn.net/weixin_29115985/article/details/78963125)
- [部署脚本](https://jimmysong.io/kubernetes-handbook/practice/flannel-installation.html)
- 在etcd中创建网络配置：必须配置这一个
```shell
etcdctl --endpoints=https://172.17.8.82:2379,https://172.17.8.84:2379,https://172.17.8.85:2379 \
  --ca-file=/etc/kubernetes/ssl/ca.pem \
  --cert-file=/etc/kubernetes/ssl/kubernetes.pem \
  --key-file=/etc/kubernetes/ssl/kubernetes-key.pem \
  mkdir /kube-centos/network
etcdctl --endpoints=https://172.17.8.82:2379,https://172.17.8.84:2379,https://172.17.8.85:2379 \
  --ca-file=/etc/kubernetes/ssl/ca.pem \
  --cert-file=/etc/kubernetes/ssl/kubernetes.pem \
  --key-file=/etc/kubernetes/ssl/kubernetes-key.pem \
  mk /kube-centos/network/config '{"Network":"172.30.0.0/16","SubnetLen":24,"Backend":{"Type":"vxlan"}}'
  
  cp ./flannel.service /usr/lib/systemd/system/
  systemctl daemon-reload
  systemctl enable flannel.service
  systemctl start flannel.service
  systemctl status flannel.servie
  # 查看日志方法
  journalctl -f -u flannel.servie
```

### docker配置文件，使得docker使用的网段与flannel在一个网段
```shell
vi /usr/lib/systemd/system/docker.service


[Unit]
Description=Docker Application Container Engine
Documentation=http://docs.docker.com
After=network.target
Wants=docker-storage-setup.service
Requires=docker-cleanup.timer

[Service]
Type=notify
NotifyAccess=main
EnvironmentFile=-/run/containers/registries.conf
EnvironmentFile=-/etc/sysconfig/docker
EnvironmentFile=-/etc/sysconfig/docker-storage
EnvironmentFile=-/etc/sysconfig/docker-network
# 必须追加在最后面
EnvironmentFile=-/run/flannel/docker
EnvironmentFile=-/run/flannel/subnet.env
Environment=GOTRACEBACK=crash
Environment=DOCKER_HTTP_HOST_COMPAT=1
Environment=PATH=/usr/libexec/docker:/usr/bin:/usr/sbin
ExecStart=/usr/bin/dockerd-current \
          --add-runtime docker-runc=/usr/libexec/docker/docker-runc-current \
          --default-runtime=docker-runc \
          --exec-opt native.cgroupdriver=systemd \
          --userland-proxy-path=/usr/libexec/docker/docker-proxy-current \
          --init-path=/usr/libexec/docker/docker-init-current \
          --seccomp-profile=/etc/docker/seccomp.json \
          $OPTIONS \
          $DOCKER_STORAGE_OPTIONS \
          $DOCKER_NETWORK_OPTIONS \
          $ADD_REGISTRY \
          $BLOCK_REGISTRY \
          $INSECURE_REGISTRY \
          $REGISTRIES
ExecReload=/bin/kill -s HUP $MAINPID
LimitNOFILE=1048576
LimitNPROC=1048576
LimitCORE=infinity

systemctl daemon-reload
systemctl restart docker
# 查看docker网络是否和flannel网络在一个网段
ipaddr
# 检测集群中节点之间flannel网络是否通讯：ping 对方docker0网关，如果能ping通，说明正常
ping 172.30.68.1
ping 172.30.15.1
ping 172.30.45.1
```
### kubeconfig 的创建
- 创建 TLS Bootstrapping Token为每个kubelet分发证书而不是自己生成
- [创建kubeconfig文件参考文档](https://jimmysong.io/kubernetes-handbook/practice/create-kubeconfig.html)

### 在master节点安装的组件
- kube-apiserver
- kube-scheduler
- kube-controller-manager
- [安装详细解释](https://jimmysong.io/kubernetes-handbook/practice/master-installation.html)

### 安装完后验证集群状态
- 注意在配置api-server时，配置如下的--insecure-bind-address，如果不配置，默认是在127.0.0.1中监听,在证书下发过程中需要使用该条件
```shell
## The address on the local server to listen to.
KUBE_API_ADDRESS="--advertise-address=172.17.8.82 --bind-address=172.17.8.82 --insecure-bind-address=172.17.8.82"
```
- 如上配置了这个参数，因此在查看集群装填时填写： kubectl --server 172.17.8.82:8080 get cs
```shell
NAME                 STATUS    MESSAGE              ERROR
scheduler            Healthy   ok                   
controller-manager   Healthy   ok                   
etcd-0               Healthy   {"health": "true"}   
etcd-2               Healthy   {"health": "true"}   
etcd-1               Healthy   {"health": "true"}
```
### 注意在k8s中scheduler、controller-manager组件的高可用k8s已经做了：参数leader-elect，但是api-server的高可用我们应该自己做

### 部署node节点
- Flannel：二进制安装
- Docker：docker 使用yum安装 
- kubelet：直接用二进制文件安装
- kube-proxy：直接用二进制文件安装
- [node部署详细解析](https://jimmysong.io/kubernetes-handbook/practice/node-installation.html)

- 在中必须进行角色的配置
```shell
cd /etc/kubernetes
kubectl create clusterrolebinding kubelet-bootstrap \
  --clusterrole=system:node-bootstrapper \
  --user=kubelet-bootstrap
  # --user=kubelet-bootstrap 是在 /etc/kubernetes/token.csv 文件中指定的用户名，同时也写入了 /etc/kubernetes/bootstrap.kubeconfig 文件；
  kubectl create clusterrolebinding kubelet-node-clusterbinding --clusterrole=system:node --group=system:nodes
```

- 关闭swap配置
```shell
# 在启动参数中添加
--fail-swap-on=false
```

- 记录部署node过程中的血泪：
- 在生成证书完后，需要拷贝到对应的api-server的目录下，拷贝完后需要重新启动api-server服务
- 记录几条拍错有用的命令
```shell
   # 查看需要签发证书的node节点
   kubectl --server 172.17.8.82:8080 get csr
   # 删除需要签发的证书节点
   kubectl --server 172.17.8.82:8080 delete csr --all
   # 通过签发证书
   kubectl --server 172.17.8.82:8080 certificate approve 【csr的名字】
```

### 部署成功结果
```shell
[root@localhost kube-apiserver]# kubectl --server 172.17.8.82:8080 get nodes
NAME          STATUS    ROLES     AGE       VERSION
172.17.8.84   Ready     <none>    15m       v1.10.0
172.17.8.85   Ready     <none>    6m        v1.10.0
```













