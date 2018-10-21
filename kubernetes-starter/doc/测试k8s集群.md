## k8s 配置证书认证、etcd集群、master高可用

### linxu配置免密登录

```shell
ssh-keygen
 ssh-copy-id  root@172.17.8.85
```

### 准备前的工作

```shell
# 操作节点
master01：172.17.8.82 
master02：172.17.8.83
node：172.17.8.84、172.17.8.84
```
### 开始部署
```shell
yum install docker
#关闭所有节点的SELinux
#永久方法 – 需要重启服务器
#修改/etc/selinux/config文件中设置SELINUX=disabled ，然后重启服务器。
```
### 创建TLS证书和秘钥

- [创建TLS证书和秘钥](https://jimmysong.io/kubernetes-handbook/practice/create-tls-and-secret-key.html)

### 部署etcd

- 关闭所有etcd节点的防火墙
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

















