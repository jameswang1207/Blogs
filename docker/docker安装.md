# docker 安装

### Install docker for ubuntu
* 安装前的准备
> - 内核版本
> ```sh
>   uname -a
> ```
> - 检查Device Mapper 映射驱动
> ```sh
>   ls -l /sys/class/misc/device-mapper
> ```

* 安装方式
> - 使用apt-get安装
> ```sh
>   sudo apt-get install -y docker.io
>   sudo docker version 
> ```
> - Docker 维护的版本来安装
> ```sh
>   sudo apt-get install -y  curl
>   curl -sSl https://get.docker.com/ubuntu/ | sudo sh
> ```
> 
> 