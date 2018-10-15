#  安装环境配置

| 系统类型 | IP地址 | 节点角色 | CPU | Memory | Hostname |
| :-----: | :---------: | :----: | :----: | :-: | :----: |
| centos7 | 172.17.8.77 | master |   4    | 16G | master |
| centos7 | 172.17.8.78 | worker |   4    | 16G | node01 |
| centos7 | 172.17.8.79 | worker |   4    | 16G | node02 |

# 安装必要的软件
```shell
# epel-release软件包，这个软件包会自动配置yum的软件仓库
yum -y install epel-release
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld
# 关闭Linux防火墙
setenforce 0 
```

#  安装docker

```shell
# 安装docker
yum -y  install docker
# 启动docker
systemctl daemon-reload
service docker start
```
