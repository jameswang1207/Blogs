#!/bin/bash

# Y显示4位年份，如：2018；y显示2位年份，如：18。m表示月份；M表示分钟。d表示天，而D则表示当前日期，如：1/18/18(也就是2018.1.18)。H表示小时，而h显示月份(有点懵逼)。s显示当前秒钟，单位为毫秒；S显示当前秒钟，单位为秒。
for (( i = 0 ; i < 10; i++ ))
do
ips=(172.17.8.23 172.17.8.24 172.17.8.25)
num=${#ips[@]}
for (( j = 0 ; j < $num; j++ ))
do
set timeout 30
set password shyfzx 
spawn ssh -l root inspection.ruijie.com.cn -p 11196 
expect "password:"
send_user "in put password!"
send "$password\n"
#expect "]*"
interact
ssh root@${ips[$j]} "echo $(date "+%Y-%m-%d %H:%M:%S")"
done
done