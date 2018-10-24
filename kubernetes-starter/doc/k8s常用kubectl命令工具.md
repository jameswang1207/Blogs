## 配置kubectl通过https访问集群
```shell
# 在客户端装与apiserver相同的kubernetes的安装包
# 将apiserver中的admin相关的证书传出到客户端的机器上:
admin-key.pem  admin.pem  ca.pem
# 客户端机器：配置集群中名为kubernetes的apiserver的地址和根证书:创建配置文件：
kubectl --server 172.17.8.82:8080 config set-cluster kubernetes --server=https://172.17.8.82:6443  --certificate-authority=ca.pem
# 测试完后，会在：./kube/目录下生成相应的配置文件,配置文件的名字：config
#设置文件中的用户相的用户认证字段:将工作空间切换到对应的目录下
kubectl --server 172.17.8.82:8080 config set-credentials cluster-admin  --certificate-authority=ca.pem --client-key=admin-key.pem 
--client-cretificate=admin.pem
# 设置环境项中名为default的默认集群和用户
kubectl --server 172.17.8.82:8080 config set-context default --cluster=kubernetes --user=cluster-admin
# 设置默认环境变量为default
kubectl --server 172.17.8.82:8080 config user-context default  
```

## kubectl 命令详细说明
- 使用help命令查看。


