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
- 在etcd中创建网络配置
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

















