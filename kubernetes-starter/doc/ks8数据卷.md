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
            path:/opt/wwwroot
```
            
          
          
    
    
    
    




```




