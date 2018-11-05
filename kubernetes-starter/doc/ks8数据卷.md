# k8s 数据卷(volume)
### 空数据卷(emptyDir)---一般用来存放零时文件
```shell
apiVersion: v1
kind: Pod
metadata:
  name: redis-pod
spec:
  containers:
  - image: redis
    name: redis
    volumeMounts:
    - mountPath: /cache
      name: cache-volume
 volumes:
 - name: cache-volume
   emptyDir: {}
```
- 使用空文件的好处：多个pod之间共享
- 但是在会随着pod的消失而清除

### hostPath(将宿主机的目录挂载到pod中)
- 宿主机上必须有该目录
- 查看pod创建过程中的详细信息
```shell
   kubectl --server 172.17.8.82:8080 get describe pod xxx[podName]
```

- nfs 配置文件 
```shell
apiVersion: v1
kind: Pod
metadata:
  name: nginx-pod
spec:
  containers:
  - image: nginx
    name: nginx
    volumeMounts:
    - mountPath: /nginx-dir
      name: nginx-volume
 volumes:
 - name : nginx-volume
   hostPath:　
     path: /data
     type:  
```

### 挂载nfs
```shell
apiVersion: extensions/v1betal
kind: Deployment
metadata:
  name: nginx-deployment
  spec:
    replicas: 2
    template:
      metadata:
        labels:
          app: nginx
      spec:
        containers:
        - name: nginx
          image: nginx
          volumeMounts:
          - mountPath: /usr/share/nginx/html
            name: wwwroot
          ports:
          - containerPort: 80
        volumes:
        - name: wwwroot
          nfs: 
            server:192.168.54.xxx
            # 表示nfs机器上的存在的目录
            path:/opt/wwwroot
```
#　分部署存储的部署：gluster部署
- [gluster部署文档](https://docs.gluster.org/en/latest/)
- 注意事项：在创建数据卷时，我们对每个项目单独创建数据卷
- 记住配置host文件集群中是通过名字来相互访问的
- 在每个节点上安装他对应的客户端
```shell
  yum install glusterfs-fuse
  yum install nfs-utils
```
# k8s支持的存储
- [k8s支持的存储](https://kubernetes.io/docs/concepts/storage/volumes/)

# 持久化数据卷
- PersistenVolum(PV,持久卷)：对存储的抽象实现，使得存储作为集群中的资源。
- PersistenVolumClaim(PVC:持久卷申请)：pvc消费Pv的资源

- Pod申请pvc作为卷来使用，集群通过pvc查找绑定的PV，并mount给pod.
   
```shell
# 无论后面是什么存储，我都不管
apiVersion: v1
kind: Persistenvolume
metadata:
  name: ngs-pv
spec:
  capacity: 
    storage: 5Gi
    accessModes:
      # ReadWriteOnce: 谁用谁挂载
      # ReadOnlyMany: 只读挂载全部|读写挂载全部
      - ReadWriteMany
    # 回收策略：Retain:保留，需要管理员清楚 Recycle：自动回收 Delete
    persistentVolumeReclaimPolicy: Recycle
    nfs:
      path: /opt/nfs/data
      server:172.17.8.xxx
---
apiVersion: v1
kind: Persistenvolume
metadata:
  name: gluster-pv
spec:
  capacity: 
    storage: 10Gi
    accessModes:
      # ReadWriteOnce: 谁用谁挂载
      # ReadOnlyMany: 只读挂载全部|读写挂载全部
      - ReadWriteMany
    glusterfs:
      endpoints: "glusterfs-cluster"
      path: "gv0"
      readOnly: false

---
# 通过容量匹配
# 访问模式
apiVersion: v1
kind: PersistenvolumeClaim
metadata:
  name: pvc001
spec:
  accessModes:
  # ReadWriteOnce: 谁用谁挂载
  # ReadOnlyMany: 只读挂载全部|读写挂载全部
  - ReadWriteMany
  resource:
    requests:
      storage: 5Gi
---
#创建应用来使用pvc
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: my-nginx
spec:
  replicas: 1
  template:
    metadata:
      labels:
        service: my-nginx
    spec:
      containers:
      - name: my-nginx
        image: nginx:1.9
        ports:
        - containerPort: 80
        volumeMounts:
        - mountPath: "/usr/share/nginx/html"
        name: wwwroot
      volumes:
        - name: wwroot
          persistentVolumeClaim:
            claimName: pvc001
```
- 存储卷的好处：
- 开发运维分工
- 运维创建并维护后端存储(pv)
- 开发者只需使用(pvc)

### 安装glusterFS服务器
- [glusterf安装文档](https://jimmysong.io/kubernetes-handbook/practice/using-glusterfs-for-persistent-storage.html)
- 配置两台nfs存储集群

```shell
yum install centos-release-gluster -y
yum install glusterfs-server  glusterfs glusterfs-fuse -y
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
# 启动glusterFS服务：
service glusterd start
# 查看glusterFS服务：
service glusterd status
# 配置集群关联的集群ip
```
### Configure the trusted pool
```shell
# glusterFS-01：172.17.86配置集群 
gluster peer probe 172.17.8.87
# glusterFS-01：172.17.87配置集群 
gluster peer probe 172.17.8.86 
```
### Check the peer status on server1
```shell
# node 172.17.8.86
[root@localhost james]# gluster peer status
Number of Peers: 1
Hostname: 172.17.8.87
Uuid: 8cc1636b-f950-4dc1-9246-fed0a59f2a4f
State: Peer in Cluster (Connected)

# node 172.17.8.87
[root@localhost james]# gluster peer status
Number of Peers: 1
Hostname: 172.17.8.86
Uuid: 8cc1636b-f950-4dc1-9246-fed0a59f2a4f
State: Peer in Cluster (Connected)
```
###  Set up a GlusterFS volume at : all node
```shell
mkdir -p /data/brick1/gv0
gluster volume create gv0 replica 2 172.17.8.86:/data/brick1/gv0 172.17.8.87:/data/brick1/gv0 force
[root@localhost data]# gluster volume start gv0
volume start: gv0: success

# 验证集群信息
[root@localhost data]# gluster volume info
Volume Name: gv0
Type: Replicate
Volume ID: 2a478988-f53f-401a-871f-3529cab65e48
Status: Started
Snapshot Count: 0
Number of Bricks: 1 x 2 = 2
Transport-type: tcp
Bricks:
Brick1: 172.17.8.86:/data/brick1/gv0
Brick2: 172.17.8.87:/data/brick1/gv0
Options Reconfigured:
transport.address-family: inet
nfs.disable: on
performance.client-io-threads: off
```

# 在clinet上安装客户端软件：
```shell
# (每个node上都应安装响应的客户端)
yum -y install glusterfs glusterfs-fuse
# 节点挂载
mkdir /mnt/glusterfs
mount -t glusterfs 172.17.8.86:/gv0 /mnt/glusterfs
# 查看挂载情况
[root@node01 mnt]# mount -t fuse.glusterfs
172.17.8.86:/gv0 on /mnt/glusterfs type fuse.glusterfs (rw,relatime,user_id=0,group_id=0,default_permissions,allow_other,max_read=131072)

# 在k8s节点上
[root@node01 glusterfs]# cd /mnt/glusterfs/
[root@node01 glusterfs]# touch file1 file2 file3

# 在nfs集群中常看文件node01:172.17.8.86
drwxr-xr-x. 3 root root 17 Nov  2 03:12 brick1
[root@localhost data]# cd /data/brick1/
[root@localhost brick1]# ll
total 0
drwxr-xr-x. 3 root root 63 Nov  2 03:40 gv0
[root@localhost brick1]# cd gv0/
[root@localhost gv0]# ll
total 0
-rw-r--r--. 2 root root 0 Nov  2 03:40 file1
-rw-r--r--. 2 root root 0 Nov  2 03:40 file2
-rw-r--r--. 2 root root 0 Nov  2 03:40 file3

# 在nfs集群中常看文件node01:172.17.8.87
[root@localhost gv0]# ll
total 0
-rw-r--r--. 2 root root 0 Nov  2 03:40 file1
-rw-r--r--. 2 root root 0 Nov  2 03:40 file2
-rw-r--r--. 2 root root 0 Nov  2 03:40 file3
```



















