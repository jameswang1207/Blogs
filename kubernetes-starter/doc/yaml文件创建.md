# Yaml相关配置说明
- 版本
- 类型
- 元数据
- 控制器
  - 副本数
  - 标签
  - 模板
    - 模板
    - 容器相关配置
 
# pod 管理
- 创建|查询|跟新|删除
- 资源限制（request使用，最大最小值，limist使用的是容器的限制）
- 调度约束
- 重启策略
- 健康检查
  - k8s当业务系统中的业务出现异常，k8s是不会认为你的应用出现异常，如果需要当业务出现异常时，认为容器是异常的，那么我们需要使用prode来进行控制
  - porbe有两种机制(查看文档)
  - 支持的检测方式
    - 支持httpGet
    - exec shell脚本
    - tcpsocket
  
# service网络代理
- [service三种服务代理模式](https://blog.csdn.net/sinat_35930259/article/details/80080778)

# service服务发现
- 实现多个应用间的通讯
### 使用环境变量
```shell
# 当一个pod运行起来时，k8s会为每个容器添加一组环境变量，pod容器就可以使用这个环境变量发现service
# 环境变量如下：
{SVCNAME}_SERVICE_HOST
{SVCNAME}_SERVICE_PORT
# 其中服务名和端口名为大写，连字符转化为下划线
```
- 限制
  - pod与service的创建是有顺序的，service必须在pod创建之前创建，否则环境变量不会设置到pod中
  - pod只能回去同一个namespace中的service环境变量
  
### 使用dns来实现
- DNS服务监视kubernetes api，为每个service创建DNS记录用于域名解析，这种pod中就可以通过DNS域名获取Service的访问地址
- kube-dns原理：

### 部署dns
- dns 部署方法
- [dns](https://jimmysong.io/kubernetes-handbook/practice/kubedns-addon-installation.html)
- 在容器中查看dns相关信息
```shell
kubectl --server 172.17.8.8.:8080 exec -ti busybox -- nslookup  kubernetes.default
```

### 检测kubedns功能
```shell
apiVersion: v1
kind: Pod
metadata: 
    name: busybox
    namespace: default
spec:
    containers:
      - image: busybox
        command:
          - sleep
          - "3600"
        imagePullPolicy: IfNotPresent
        name: busybox
    restartPolicy: Always
```






