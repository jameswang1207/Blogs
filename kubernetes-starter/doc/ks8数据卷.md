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
          
          
    
    
    
    




```




