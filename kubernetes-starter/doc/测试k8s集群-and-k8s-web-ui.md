## 创建一个测试实例
```shell
    # 创建一个nginx实例
    kubectl --server 172.17.8.82:8080 run nginx --image=nginx --replicas=3 
    # 查看pod运行情况
    kubectl --server 172.17.8.82:8080 get pod
    # 查询在哪个节点上运行
    kubectl --server 172.17.8.82:8080 get pod -o wide
    NAME                     READY     STATUS    RESTARTS   AGE       IP            NODE
    nginx-65899c769f-4twtv   1/1       Running   0          5m        172.30.45.3   172.17.8.85
    nginx-65899c769f-7qqxp   1/1       Running   0          5m        172.30.45.2   172.17.8.85
    nginx-65899c769f-q6mpl   1/1       Running   0          5m        172.30.68.2   172.17.8.84
    # 发布一个service
    kubectl --server 172.17.8.82:8080 expose deployment nginx --port=88 --target-port=80 --type=NodePort
    [root@localhost kube-apiserver]# kubectl --server 172.17.8.82:8080 get svc nginx
    NAME      TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
    nginx     NodePort   10.254.132.110   <none>        88:30487/TCP   3m
    # 注意其中生成的ip是在api-service中指定的，属性是：--service-cluster-ip-range=10.254.0.0/16
    # 从node节点上访问：在node上访问，那么必须部署flannel才能访问
    curl 10.254.132.110：88
    # 从浏览器上访问
    172.17.8.85：30487
    # 查看全部资源运行状态
    kubectl --server 172.17.8.82:8080 get all
    # 查看所有标签
    kubectl --server 172.17.8.82:8080 get pods --show-labels
    # 应用排查故障
    # 查看某个pod的状态
    kubectl --server 172.17.8.82:8080 describe [pod | deploy] nginx-65899c769f-7qqxp
    # 产看运行时的日志
    kubectl --server 172.17.8.82:8080 logs nginx-65899c769f-7qqxp
    # 进入容器
    kubectl --server 172.17.8.82:8080 exec -it nginx-65899c769f-7qqxp /bin/bash
    # 跟新你的资源限制
    kubectl --server 172.17.8.82:8080 set image deployment/nginx nginx=nginx:1.13 --record
    # 修改
    kubectl --server 172.17.8.82:8080 edit deployment/nginx
    
    # 资源的发布管理
    kubectl --server 172.17.8.82:8080 rollout status deploy/nginx
    kubectl --server 172.17.8.82:8080 rollout history deploy/nginx
    # 版本回滚
    kubectl --server 172.17.8.82:8080 rollout undo deploy/nginx
    # 增大pod的数量
    kubectl --server 172.17.8.82:8080 scale deploy/nginx --replicas=5
 ```

## 集群部署-WEB-UI(dashboard)
- [阿里镜像库](https://dev.aliyun.com/search.html)
- [相关脚本](https://github.com/jameswangAugmentum/Blogs/tree/master/kubernetes-starter/dashboard)
```shell
   # 创建dashboard
   kubectl --server 172.17.8.82:8080 create -f dashboard-rbac.yaml
   kubectl --server 172.17.8.82:8080 create -f dashboard-controller.yaml
   kubectl --server 172.17.8.82:8080 create -f dashboard-service.yaml
   # 查看部署情况
   kubectl --server 172.17.8.82:8080 get all -n kube-system
   # 在查询时需要指定命名空间，默认查询到的是default下的组件部署情况
   kubectl --server 172.17.8.82:8080 get pod -n kube-system svc
```

## 通过浏览器查询部署情况
http://172.17.8.84:30302/#!/overview?namespace=default

 
