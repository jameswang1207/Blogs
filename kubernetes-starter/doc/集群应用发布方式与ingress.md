# 集群应用发布
### ClusterIp
- 分配一个内部集群ip地址，只能再集群内部访问（同一namespace内的pod）
- 分配一个内部集群ip地址，并在节点上暴露端口，外部通过节点ip进行访问
- LoadBalance（如aws上的负载均衡器）

#  ingress
- nginx (实现负载到后端应用pod的集合)
- ingress controller (通过集群api获取对应的ip添加到nginx中，并重载)
- ingress (自动创建虚拟主机)

- 自动读取service相关的路由信息，并将其添加nginx的配置文件中
- 如果没有使用ingress，那么应用访问如下所示
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

- [ingress部署配置文件](https://github.com/kubernetes/ingress-nginx/tree/nginx-0.20.0/deploy)

- 部署内容
```shell
# Default Backend
# ingress Controller
# ingress
# ingress TLS
```












