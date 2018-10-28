# 集群应用发布
### ClusterIp
- 分配一个内部集群ip地址，只能再集群内部访问（同一namespace内的pod）
- 分配一个内部集群ip地址，并在节点上暴露端口，外部通过节点ip进行访问
- LoadBalance（如aws上的负载均衡器）

#  ingress
```shell
            internet
               |
-------------------------------
[           services           ]
```

- 使用ingress访问时：

```shell
            internet
               |
[           Ingress            ]             
-----------|--------|----------
[           services           ]
```

- [网关部署使用traefik](https://jimmysong.io/kubernetes-handbook/practice/traefik-ingress-installation.html)
- [部署脚本](https://github.com/jameswangAugmentum/Blogs/tree/master/kubernetes-starter/ingress/traefik-ingress)
- 在部署过程中使用DaemonSet部署(daemonSet为每个node部署一个)

# 因为在每个节点上都部署了ingress，因此访问看ingress是否部署成功
```shell
http://172.17.8.84:8580/dashboard/
或
http://172.17.8.85:8580/dashboard/
```

## 测试ingress
```shell
# 创建ingress规则
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: traefik-ingress
  namespace: default
spec:
  rules:
  - host: traefik.nginx.io
    http:
      paths:
      - path: /
        backend:
          serviceName: my-nginx
          servicePort: 80
  - host: traefik.frontend.io
    http:
      paths:
      - path: /
        backend:
          serviceName: frontend
          servicePort: 80
```

- 创建my-nginx和nginx对应的pod

```shell
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
---
apiVersion: v1
kind: Service
metadata:
  name: my-nginx
spec:
  selector:
    service: my-nginx
  ports:
  - port: 80
    targetPort: 80
```
#  在任意节点上访问
#  172.17.8.85 node节点上访问：
```shell
[root@localhost node01]# curl -H Host:traefik.nginx.io http://172.17.8.85/
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
```
# 172.17.8.84 node上访问
```shell
[root@localhost node01]# curl -H Host:traefik.nginx.io http://172.17.8.84/
<!DOCTYPE html>
<html>
<head>
<title>Welcome to nginx!</title>
<style>
    body {
        width: 35em;
        margin: 0 auto;
        font-family: Tahoma, Verdana, Arial, sans-serif;
    }
</style>
</head>
<body>
<h1>Welcome to nginx!</h1>
<p>If you see this page, the nginx web server is successfully installed and
working. Further configuration is required.</p>

<p>For online documentation and support please refer to
<a href="http://nginx.org/">nginx.org</a>.<br/>
Commercial support is available at
<a href="http://nginx.com/">nginx.com</a>.</p>

<p><em>Thank you for using nginx.</em></p>
</body>
</html>
```

# 在浏览器中访问
### 使用浏览器访问：traefik.nginx.io

```shell
  172.17.8.76     console.shyfzx.com
  172.17.8.85     traefik.nginx.io
  172.17.8.84     traefik.nginx.io
```

### 浏览器出现nginx页面
![我是图片](https://github.com/jameswangAugmentum/Blogs/blob/master/kubernetes-starter/images/ingress.png)








