# Redis-Cluster Ubuntu14.04 实验记录



## 一.实验目录脚本结构图

> $project/dist/redis_test.tar.gz 解压后目录结构

```
redis_test

├── bin
│   ├── redis-benchmark
│   ├── redis-check-aof
│   ├── redis-check-rdb
│   ├── redis-cli
│   ├── redis-sentinel
│   ├── redis-server
│   └── redis-trib.rb
├── redis_conf
│   └── redis_model.conf
├── redis_log
├── redis_node
├── redis_rdb
├── clientCuster.sh
├── config.properties
├── custerMeet.sh
├── custerTest.sh
├── deleteAll.sh 
├── dist_config.sh
├── insert.jar
├── redis-3.2.0.tar.gz
├── sshSql.sh
├── startAllRedis.sh
├── startCuster.sh
└── stopAllRedis.sh
```

- redis_test/redis-3.2.0.tar.gz 为官方源码包
- redis_test/bin目录为 3.2.0 编译完成的redis 二进制文件可以直接使用.
- redis_test/*.sh 为编写的自动化部署脚本. 可以直接拿来使用或修改 
- redis_conf/redis_model.conf 为redis 模板文件


##  二. ruby换源地址&并安装redis模块


```
apt-get install ruby
gem sources --remove http://ruby.taobao.org/
gem sources -a https://ruby.taobao.org/
gem sources -l
sudo gem install cocoapods
gem install redis

```

## 三.配置修改&启动 cluster

```
vim config.properties  # 修改配置文件
./dist_config.sh # 生成新的配置文件
./startAllRedis.sh # 启动所有 redis 服务
./startCuster.sh #启动cluster

```

## 四.指令说明

- cluster slots ## 查看 slots分布
- cluster nodes ## 查看节点分布


## 五.注意事项

- redis-cli客户端默认不支持集群模式的需要使用-c 参数[启用 cluster模式  ]

## 六.集群扩容/缩容
  ........未完成
  `中文翻译组cn-Redis 集群教程 可以参考`
## 七.压力测试
  ........未完成
  `感觉遇到了坑,内网压测和本机测试差别比较大,可能是网络问题也可能是配置问题.还在试验中.`
  
  

#### 官方地址

[redis-官方地址](http://www.redis.io/)

[cn-Redis 集群教程](http://www.redis.cn/topics/cluster-tutorial.html)
[cn-Redis 集群规范](http://www.redis.cn/topics/cluster-spec.html)

[java-jedis地址](https://github.com/xetorthio/jedis)

#### 实验参考地址


[部署-资料一](http://debugo.com/redis-cluster-note1/)

[部署-资料二](http://xiaorui.cc/2015/05/16/ubuntu%E5%AE%89%E8%A3%85%E6%B5%8B%E8%AF%95redis3-0%E7%9A%84cluster%E9%9B%86%E7%BE%A4%E6%A8%A1%E5%BC%8F/)

[扩容资料-1](http://blog.bitfoc.us/?p=524)

[错误处理-1](http://www.cnblogs.com/weixiaole/p/4344783.html)