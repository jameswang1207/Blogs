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
### 部署etcd

- 关闭所有etcd节点的防火墙
```shell
service docker start
systemctl stop firewalld
systemctl disable firewalld
```
- 部署etcd请参见
- [etcd集群部署](https://jimmysong.io/kubernetes-handbook/practice/etcd-cluster-installation.html)

