JVM实战操作

1. Xxs 与 Xms一般设置成一样的，这样防止弹性抖动

   Java  Xxs200M Xmx200M -XX:+PrintGCDetails

2. Java程序top后内存在不断上涨

   Jinfo pid: 查看当前这个pid对应的相关信息

   jstat -options: 查看有哪些参数

   Jstat gc pid

   Jmap : 查看那个对象在吃内存: jmap -histo pid

   Jmap -histo pid | head 10: 那个对象产生多少个对象并能产生多少自己，并只看前面几个

   Jhat : Java heap 分析工具  

3. arthas

   https://alibaba.github.io/arthas/

   arthas 下载

   curl -O https://alibaba.github.io/arthas/arthas-boot.jar

