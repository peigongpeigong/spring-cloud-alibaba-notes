### Alibaba Nacos
Nacos致力于解决微服务中的统一配置、服务注册与发现的问题。他提供了一组简单易用的特性集，
帮助开发者快速实现动态服务发现、服务配置、服务元数据及流量管理

Nacos关键特性有：
- 服务发现和服务健康监测：Nacos支持基于DNS和基于RPC的服务发现。服务提供者使用原生SDK
  、OpenAPI或一个独立的Agent TODO注册Service后，服务消费者可以使用DNS或HTTP&API
  查找和发现服务。Nacos提供对服务的实时的健康检查，阻止向不健康的主机或服务实例发送请求。
  Nacos支持传输层(PING或TCP)和应用层(如HTTP、MySQL、用户自定义)的健康检查。对于
  复杂的云环境和网络拓扑环境中(如VPC、边缘网络等)服务的健康检查，Nacos提供了agent上报
  和服务端主动监测两种健康检查模式。Nacos还提供了统一的健康检查仪表盘，帮助用户根据健康
  状态管理服务的可用性及流量
- 动态配置服务：动态配置服务可以以中心化、外部化、动态化的方式管理所有环境的应用配置和
  服务配置，可以使配置管理变得更加高效和敏捷。
- 动态DNS服务：动态DNS服务支持权重路由，让开发者更容易地实现中间层负载均衡、更灵活的
  路由策略、流量控制，以及数据中心内网的简单DNS解析服务
- 服务及元数据管理：Nacos可以使开发者从微服务平台建设的视角管理数据中心的所有服务及
  元数据，包括管理服务的描述、生命周期、服务的静态以来分析、服务的健康状态、服务的流量
  管理、路由及安全策略、服务的SLA及最重要的metrics统计数据
  
  
#### 在Spring Boot中使用Nacos作为Dubbo的注册中心

##### Provider方
需要引入的依赖如下
```xml
<dependency>
    <groupId>com.peigong.springcloudalibaba</groupId>
    <artifactId>nacos-sample-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-discovery-spring-boot-starter</artifactId>
    <version>0.2.4</version>
</dependency>
<dependency>
    <groupId>org.apache.dubbo</groupId>
    <artifactId>dubbo-spring-boot-starter</artifactId>
    <version>2.7.7</version>
</dependency>
```

创建Service
```java
@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }
}
```
配置文件
```properties
spring.application.name=spring-dubbo-nacos-sample
dubbo.registry.address=nacos://127.0.0.1:8848

dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
```
启动项目就可以在Nacos的服务列表里看到HelloService了


#### Spring Cloud 中使用Nacos作为Dubbo的注册中心

##### Provider端
引入依赖
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-dubbo</artifactId>
</dependency>
<dependency>
    <groupId>com.peigong.springcloudalibaba</groupId>
    <artifactId>spring-cloud-nacos-sample-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-nacos-discovery</artifactId>
</dependency>
```

Service实现与正常的Dubbo Service无区别

在Spring Cloud中使用Nacos的配置如下
```properties
spring.application.name=spring-cloud-nacos-sample

# 与@DubboComponentScan效果相同
dubbo.scan.base-packages=com.peigong.springcloudalibaba.service
dubbo.protocol.name=dubbo
dubbo.protocol.port=20880
# 表示dubbo的注册中心挂载到spring-cloud注册中心
dubbo.registry.address=spring-cloud://localhost

spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```

##### Consumer端
依赖如下
```xml
<dependency>
    <groupId>com.peigong.springcloudalibaba</groupId>
    <artifactId>spring-cloud-nacos-sample-api</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-dubbo</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-alibaba-nacos-discovery</artifactId>
    <version>2.2.0.RELEASE</version>
</dependency>
```

配置文件如下
```properties
dubbo.cloud.subscribed-services=spring-cloud-nacos-sample
spring.application.name=spring-cloud-nacos-consumer
spring.cloud.nacos.discovery.server-addr=127.0.0.1:8848
```

创建一个Controller进行测试
```java
@RestController
public class HelloController {

    @Reference
    private HelloService helloService;

    @GetMapping("hello")
    public Object hello(String name) {
        return helloService.hello(name);
    }

}
```

#### Nacos实现原理
对于Nacos客户端感知服务端配置变化，Nacos采用的是Pull模式，但并不是简单的Pull模式，而是一种
长轮询机制，他结合Push和Pull两者的优势。客户端采用长轮询的方式定时发起Pull请求，去检查服务端
配置信息是否发生了变更，如果发生了变更，则客户端会根据变更的数据获取最新的配置。

如果客户端发起Pull请求之后，发现服务端的配置和客户端的配置是保持一致的，那么服务端会先Hold主这个
请求，也就是服务端拿到这个连接之后在指定的时间段内不返回结果，直到这段时间内配置发生变化，
服务端会把原来Hold住的请求进行返回。

有两种方式触发该连接返回结果：
- 在等待29.5秒之后触发自动监测机制，这时候不管配置是否发生变化，都会把结果返回给客户端。
- 在29.5秒内任意时刻，配置发生了变动，会触发一个事件机制，监听到该事件的人物会遍历allSubs
  队列，找到发生变更的配置项对应的ClientLongPolling任务，将变更的数据通过该任务中的连接
  进行返回