# k8s 中部署应用需要使用docker私服镜像
```shell
# 修改私服地址
cd /etc/docker
[root@localhost etc]# cd docker/
[root@localhost docker]# ll
total 20
drwxr-xr-x. 5 root root    75 Oct 20 08:59 certs.d
-rw-r--r--  1 root root    49 Nov  8 01:39 daemon.json
-rw-------  1 root root   244 Oct 21 02:29 key.json
-rw-r--r--. 1 root root 10837 Sep 28 15:07 seccomp.json
[root@localhost docker]# cat daemon.json 
{
"insecure-registries":["192.168.54.25:8082"]
}
# 生成securit,这里必须加上--namespace
kubectl --server 172.17.8.82:8080 create secret docker-registry  regsecret --docker-server=192.168.54.xx：8082 --docker-username=admin --docker-password=123456 --docker-email=xxxx@qq.com --namespace=xxx
# 在对应的pod上添加相应的key
```
# k8s中注意的问题
- k8s中一个pv只能给一个pvc使用
- pv pvc endpoint 在不同的namespace中他们是不能共享的，因此，在不同的namespace中申明自己的pv pvc endpoint
- 在远程存储上需要将自己的每个存储有都必须开启
```shell
gluster volume start gv0
gluster volume start gv1
gluster volume start gv2
```

