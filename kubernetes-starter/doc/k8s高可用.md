# k8s高可用
- 部署k8s高可用架构
- 使用双master部署，在节点上使用nginx来做负载
- 主节点上部署{api-server|kube-controller-manager|kube-skube-scheduler}
- 必须将该节点添加到可信任证书里面
```shell
[root@localhost ssl]# cat kubernetes-csr.json 
{
    "CN": "kubernetes",
    "hosts": [
      "127.0.0.1",
      "172.17.8.82",
      # 必须另一个几点添加到证书中
      "172.17.8.83",
      "172.17.8.84",
      "172.17.8.85",
      "10.254.0.1",
      "kubernetes",
      "kubernetes.default",
      "kubernetes.default.svc",
      "kubernetes.default.svc.cluster",
      "kubernetes.default.svc.cluster.local"
    ],
    "key": {
        "algo": "rsa",
        "size": 2048
    },
    "names": [
        {
            "C": "CN",
            "ST": "BeiJing",
            "L": "BeiJing",
            "O": "k8s",
            "OU": "System"
        }
    ]
}
```
 # 修改后重启api-server
 #  将对应的三个组件文件拷贝到对应的目录上
 ```shell
 cp  ./kube-scheduler.service /usr/lib/systemd/system/
 cp  ./kube-apiserver.service /usr/lib/systemd/system/
 cp  ./controller-manager.service /usr/lib/systemd/system/
 ```
 #修改api-server中的文件
```shell
## The address on the local server to listen to.
KUBE_API_ADDRESS="--advertise-address=172.17.8.83 --bind-address=172.17.8.83 --insecure-bind-address=172.17.8.83"
```
# 启动相应的进程
```shell
systemctl start kube-apiserver.service
systemctl start kube-controller-manager.service
systemctl start kube-scheduler.service
```
# 验证是否成功
```shell
[root@localhost kube-scheduler]# kubectl --server 172.17.8.83:8080  get nodes
NAME          STATUS    ROLES     AGE       VERSION
172.17.8.84   Ready     <none>    7d        v1.10.0
172.17.8.85   Ready     <none>    7d        v1.10.0
[root@localhost kube-scheduler]# kubectl --server 172.17.8.83:8080  get pod -n kube-system
NAME                                    READY     STATUS    RESTARTS   AGE
kube-dns-7b5ff7c455-l4j5t               3/3       Running   0          4d
kubernetes-dashboard-5b56666549-kr9m5   1/1       Running   3          7d
traefik-ingress-lb-4f89q                1/1       Running   0          2d
traefik-ingress-lb-4zrjp                1/1       Running   0          2d
```

# 现在使用的是两个master,那么node01与node02应该只能连接一个master，因此我们在node上安装nginx,用来做反向代理
```shell
sudo rpm -Uvh http://nginx.org/packages/centos/7/noarch/RPMS/nginx-release-centos-7-0.el7.ngx.noarch.rpm
sudo yum  install  nginx
```
# 修改nginx配置文件
- nginx高版本支持四层转发：通过ip加端口进行转发
- 七层，分析应用层数据，咱们api-server证书是在每个api-server上指定的，不用做解析，只需使用ip加端口就转发

# 修改nginx配置文件
```shell
[root@localhost nginx]# cat nginx.conf 

user  nginx;
worker_processes  1;

error_log  /var/log/nginx/error.log warn;
pid        /var/run/nginx.pid;


events {
    worker_connections  1024;
}


stream {
    log_format  main  '$remote_addr $upstream_addr - [$time_local] $status $upstream_bytes_sent';

    access_log  /var/log/nginx/access.log  main;

    upstream k8s-apiserver {
        server 172.17.8.82:6443;
        server 172.17.8.83:6443;
    }

    server {
        listen 127.0.0.1:6443;
        proxy_pass k8s-apiserver;
    }
}
```

# 启动nginx

```
service nginx start
```

# 修改三个文件
```shell
-rw------- 1 root root 2163 Oct 30 12:00 bootstrap.kubeconfig
-rw------- 1 root root 2276 Oct 30 12:00 kubelet.kubeconfig
-rw------- 1 root root 6269 Oct 30 12:00 kube-proxy.kubeconfig
ls *config | xargs -i sed -i 's/172.17.8.82/127.0.0.1/' {}
```

# 重启kubelet与kube-porxy
# 产看日志
# node1：
```shell
[root@localhost kube-proxy]# cat /var/log/nginx/access.log 
127.0.0.1 172.17.8.82:6443 - [30/Oct/2018:12:17:10 -0400] 200 1112
127.0.0.1 172.17.8.82:6443 - [30/Oct/2018:12:17:10 -0400] 200 1112
127.0.0.1 172.17.8.83:6443 - [30/Oct/2018:12:22:13 -0400] 200 37199
127.0.0.1 172.17.8.83:6443 - [30/Oct/2018:12:22:14 -0400] 200 1113
127.0.0.1 172.17.8.82:6443 - [30/Oct/2018:12:22:14 -0400] 200 1114
------------------------------------------------------------------
127.0.0.1 172.17.8.82:6443 - [30/Oct/2018:12:15:09 -0400] 200 1112
127.0.0.1 172.17.8.83:6443 - [30/Oct/2018:12:15:09 -0400] 200 1114
127.0.0.1 172.17.8.82:6443 - [30/Oct/2018:12:16:28 -0400] 200 15227
127.0.0.1 172.17.8.82:6443 - [30/Oct/2018:12:16:29 -0400] 200 1113
127.0.0.1 172.17.8.83:6443 - [30/Oct/2018:12:16:29 -0400] 200 1112
------------------------------------------------------------------
查询对应的日志/var/log/message,发现其中没有报错信息，那么你的k8s master是可用的
```

# k8s高可用部署成功







 
 
 
 
 
 
 
 
 
 
 
 


