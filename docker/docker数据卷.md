# docker 数据管理

### docker容器的数据卷

* 什么是数据卷（data volume）
- 数据卷是通过特殊设计的目录，可以绕过联合文件系统，为一个或多个容器提供访问
- 数据卷设计的目的，在于数据的永久化，他完全独立于数据的生命周期，因此，docker不会在容器删除时删除启挂载的数据卷，也不存在垃圾收集机制，对容器引用的数据卷进行处理。
- docker数据卷是存在于宿主机上的，与docker的生命周期是分离的
- docker数据卷可以是目录，也可以是文件
- 通过数据卷可以与宿主机进行文件共享
- 同一个目录或文件可以支持多个文件的访问，实现容器将的数据共享

* 数据卷的特点
- 数据卷在容器启动时进行初始化，如果容器使用的镜像在挂载点包含了数据，这些数据会拷贝到新的初始化数据卷中。
- 数据卷可以在容器间共享和重用
- 可以对数据卷里的内容直接进项修改
- 数据卷的变化不会影响镜像的更新
- 卷会一直存在，即便挂载数据卷的容器已经被删除

* 数据卷的使用
```sh
  # 为容器添加数据卷，如果没有文件夹，会自动创建
  sudo docker run -v ~/[宿主机的目录]:/容器中的目录 -it ubuntu /bin/bash
  sudo docker run -v ~/container_data:/data -it ubuntu /bin/bash
  # 通过docker inspect 来查看容器是否挂载数据卷
  sudo docker inspect [容器id]
  # 为数据卷添加访问权限
  sudo docker run -v ~/data:/data:ro -it ubuntu /bin/bash
  # 使用Dockerfile构建包含数据卷的镜像,中括号中可以是多个，并逗号隔开
  VOLUME ['~/data','~/data1']
```

* docker 数据卷容器
- 命名的容器挂载数据卷，其他容器通过挂载这个容器实现数据共享，挂载数据卷的容器叫做数据卷容器。
```sh
  # 挂载数据卷容器的方法
  docker run --volumes-from [container name]
  # 当删除数据卷容器时，容器任然能访问数据卷容器的目录，因此，挂载了数据卷容器的容器只是将数据卷信息传递给挂载数据卷容器的容器
```

* 数据卷的备份和还原
- 数据备份方式
```sh 
  # 数据备份
  docker run --volumes-from [container name] -v ${pwd}:/backup ubuntu [执行备份的操作]

  docker run --volumes-from [container name] -v ${pwd}:/backup ubuntu tar cvf /backup/backup.tar [container data volume]

  # 数据还原
  docker run --volumes-from [container name] -v ${pwd}:/backup ubuntu tar xvf /backup/backup.tar [container data volume]
```
