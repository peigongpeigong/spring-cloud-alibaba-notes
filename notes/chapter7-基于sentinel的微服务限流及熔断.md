## 基于Sentinel的微服务限流及熔断

在没有任何保护机制的情况下，如果所有的流量都进入服务器，很可能造成服务器宕机导致真个系统
不可用，从而造成巨大的损失。为了不保证系统在这些场景(双11、秒杀等)中仍然能稳定运行，就需要
草去一定的系统保护策略，常见的策略有服务降级、限流和熔断等

### 服务限流的作用及实现
限流的主要目的是通过限制并发访问数或者限制一个时间窗口内允许处理的请求数量来保护系统，
一旦达到限制数量则对当前请求进行处理采取对应的拒绝策略，比如跳转到错误页面拒绝请求、
进入排队系统、降级等。从本质上来说，限流的主要作用是损失一部分用户的可用性，为大部分
用户提供稳定可靠的服务。比如系统当前能够处理的并发数是10万，如果此时来了12万用户，
那么限流机制会保证为10万用户提供正常的服务

在实际开发过程中，限流几乎无处不在：
- 在Nginx层添加限流模块限制平均访问速度
- 通过设置数据库连接池、线程池的大小限制总的并发数
- 通过Guava提供的Ratelimiter限制接口的访问速度
- TCP通信协议中的流量整形

#### 计数器算法
计数器算法是一种比较简单的限流实现算法，在指定周期内累加访问次数，当访问次数达到设定的阈值
时，处发现刘策略，当进入下一个时间周期时进行访问次数的清零。

这种算法存在一个临界问题。在第一分钟的58秒和第二分钟的02秒这个时间段内，分别出现了
100个请求，整体看来就会出现4秒内总请求量达到200，超出了设置的阈值。

#### 滑动窗口算法
为了解决计数器算法带来的临界问题，引入了滑动窗口算法。滑动窗口算法是一种流量控制
技术，在TCP网络通信协议中，就采用了滑动窗口算法来解决网络拥塞的情况。

简单来说，滑动窗口算法的原理是在固定窗口中分割出多个小时间窗口，分别在每个小时间窗口
中记录访问次数，然后根据时间将窗口往前滑动并删除过期的小时间窗口。最终只需要统计滑动窗口
范围内的所有小时间窗口总的计数即可。

Sentinel就是采用滑动窗口算法来实现限流的。

#### 令牌桶限流算法
令牌桶是网络流量整形(Traffic Shaping)和速率限制(Rate Limiting)中最常使用的一种算法。
对于每一个请求，都需要从令牌桶中获得一个令牌，如果没有货的令牌，则需要出发限流策略。

系统会以一个恒定的速度(r tokens/sec)往固定容量的令牌桶中放入令牌，如果此时有客户端请求，
则需要先从令牌桶中拿到令牌以获得访问资格。

令牌的生成和获取有三种情况：
- 请求速度大于令牌生成速度：那么令牌很快会被取完，后续的请求会被限流
- 请求速度等于令牌生成速度：此时流量处于平稳状态
- 请求速度小于令牌生成速度：说明此时系统的并发数并不高，请求可被正常处理

#### 漏桶限流算法
漏桶限流算法的主要作用是控制数据注入网络的速度，平滑网络上的突发流量。

在漏桶算法内同样维护一个容器，这个容器会以恒定速度出水，不管上面的水流速度有多快，漏桶
水滴的流出速度始终保持不变。实际上消息中间件就使用了漏桶限流的思想，不管生产者的请求量
有多大，消息的处理能力取决于消费者。

漏桶算法可能出现的情况：
- 请求速度大于漏桶流出的速度，也就是请求书超出当前服务所能处理的极限，将会触发限流策略
- 请求速度小于或等于漏桶流出速度，也就是服务端的处理能力刚好满足客户端的请求量

漏桶限流算法和令牌桶限流算法的实现原理相差并不大，最大的区别是漏桶无法处理短时间内的突发流量，
漏桶限流算法是一种恒定速度的限流算法


### 服务熔断与降级
在微服务架构中，由于服务拆分粒度较细，会出现请求链路较长的情况，用户发起一个请求操作，需要
调用多个微服务才能完成。在高并发场景中，这些依赖服务的稳定性对系统的影响非常大，比如某个
服务因为网络延迟或者请求超时等原因不可用时，就会导致当前请求阻塞。所以服务熔断就是用来解决这个
问题的方案。

服务熔断指当某个服务提供者无法正常为服务调用者提供服务时，比如请求超时、服务异常等，为了防止
整个系统出现雪崩效应，暂时将出现故障的接口隔离出来，断绝与外部接口的联系，当触发熔断之后，
后续一段时间内该服务的调用者的请求都会直接失败，直到目标服务恢复正常。

### 分布式限流框架Sentinel
Sentinel是面向分布式服务架构的轻量级流量控制组件，主要以流量为切入点，从限流、流量整形、
服务降级、系统负载保护等多个维度来帮助我们保障微服务的稳定性。

#### Sentinel的特性
- 丰富的应用场景：几乎涵盖所有的应用场景，例如秒杀(即突发流量控制在系统容量可以承受的范围)、
  消息削峰填谷、集群流量控制
- 实时监控：Sentinel提供了实时监控功能。开发者可以在控制台中看到接入应用的单台机器秒级数据，
  甚至500台以下规模的集群汇总运行情况
- 开源生态支持：Sentinel提供开箱即用的与其他开源框架、库的整合，例如与Spring Cloud、
  Dubbo、gRPC的整合。开发者只需要引入相应的依赖并进行简单的配置即可快速接入Sentinel
- SPI扩展点支持：Sentinel提供了SPI扩展点支持，开发者可以通过扩展点来定制化限流规则，
  动态数据源适配等需求
  
#### Sentinel资源保护规则
Sentinel支持多种资源保护规则：流量控制规则、熔断降级规则、系统保护规则、来源访问控制规则、
热点参数规则。

##### 基于并发数和QPS的流量控制
Sentinel流量控制统计有两种类型，通过grade属性来控制：
- 并发线程数(FLOW_GRADE_THREAD)
- QPS(FLOW_GRADE_QPS)

**并发线程数**

并发线程数限流用来保护业务线程不被耗尽。比如：A服务调用B服务，而B服务因为某种原因导致
服务不稳定或者响应延迟，那么对于A服务来说，它的吞吐量会下降，也意味着占用更多的
线程(线程阻塞后一直未释放)，极端情况下会造成线程池耗尽。

针对这种问题，一个常见的解决方案是通过不同业务逻辑使用不同的线程池来隔离业务自身的
资源争抢问题，但是这个方案同样会造成线程数量过多带来的上下文切换问题。

Sentinel并发线程数限流就是统计当前请求的上下文线程数，如果超出阈值，新的请求就会被拒绝。

**QPS**

QPS(Queries Per Second)表示每秒查询数，也就是一台服务器每秒能够响应的查询次数。
当QPS达到限流的阈值时，就会触发限流策略。

##### QPS流量控制行为
当QPS超过阈值时，就会触发流量控制行为，这种行为是通过controlBehavior来设置的，
包含：
- 直接拒绝(RuleConstant.CONTROL_BEHAVIOR_DEFAULT)
- Warm Up(RuleConstant.CONTROL_BEHAVIOR_WARM_UP)，冷启动(预热)
- 匀速排队(RuleConstant.CONTROL_BEHAVIOR_RATE_LIMITER)
- 冷启动+匀速排队(RuleConstant.CONTROL_BEHAVIOR_WARM_UP_RATE_LIMITER)

**直接拒绝**

直接拒绝是默认的流量控制方式，也就是请求流量超出阈值时，直接抛出一个FlowException。

**Warm Up**

Warm Up是一种冷启动(预热)方式。当流量突然增大时，也就意味着系统从空闲状态吐艳切换到
繁忙状态，有可能会瞬间把系统压垮。当我们希望请求处理的数量逐步递增，并在一个逾期时间后
达到允许处理请求的最大值时，Warm Up就可以达到这个目的。

**匀速排队**

匀速排队的方式会严格控制请求通过的时间间隔，也就是让请求以均匀的速度通过，其实相当于前面讲
的漏桶限流算法。

##### 调用关系流量策略
调用关系包括调用方和被调用方，一个方法又可能会调用其他方法，形成一个调用链。所谓的调用关系
流量策略，就是根据不同的调用维度来触发流量控制。
- 根据调用方限流
- 根据调用链路入口限流
- 据有关系的资源流量控制(关联流量控制)

**调用方限流**

所谓调用方限流，就是根据请求来源进行流量控制，我们需要设置limitApp属性来设置来源信息，
他有三个选项：
- default：表示不区分调用者，也就是在任何访问调用者的请求都会进行限流统计。
- {some_origin_name}：表示特性的调用者，只有来自这个调用者的请求才会进行流量统计和控制
- other：表示针对{some_origin_name}外的其他调用者进行流量控制。

由于同一个资源可以配置多条规则，如果多个规则设置的limitApp不一样，那么规则的生效
顺序为：{some_origin_name} -> other -> default

**根据调用链路入口限流**

一个被限流保护的方法，可能来自不用的调用链路。比如针对资源nodeA，入口Entrance1和Entrance2
都调用了资源nodeA，那么Sentinel允许只根据某个入口来进行流量统计。比如我们对于nodeA资源，设置
针对Entrance1入口的调用才会统计请求次数。它在一定程度上有点类似于调用方限流。

**关联流量控制**

当两个资源之间存在依赖或者资源争抢时，我们就说这两个资源存在关联。这两个存在感依赖关系的
资源在执行时可能会因为某一个资源执行操作过于频繁而影响另一个资源的执行效率，所以关联流量
控制(流控)就是限制其中一个资源的执行流量。

#### Sentinel实现服务熔断
Sentinel实现服务熔断操作的配置和限流类似，不同之处在于限流采用的是FlowRule，而熔断
中采用的是DegradeRule

几个重要属性说明：
- grade：熔断策略，支持秒级RT、秒级异常比例、分钟级异常数。默认是秒级RT(Response Time)
- timeWindow：熔断降级的时间窗口，单位为s。也就是触发熔断降级之后多长时间内自动熔断
- rtSlowRequestAmount：在RT模式下，1s内持续多少个请求的平均RT超出阈值后出发通断，
  默认是5s
- minRequestAmount：出发的异常熔断最小请求数，请求数小于该值时即使异常比例超出阈值
  也不会触发熔断，默认值为5
  
Sentinel提供三种熔断策略，对于不同策略，参数的含义也不相同
- 平均响应时间 (DEGRADE_GRADE_RT)：当 1s 内持续进入 N 个请求，对应时刻的平均响应
  时间（秒级）均超过阈值（count，以 ms 为单位），那么在接下的时间窗口（DegradeRule 
  中的 timeWindow，以 s 为单位）之内，对这个方法的调用都会自动地熔断（抛出 
  DegradeException）。注意 Sentinel 默认统计的 RT 上限是 4900 ms，
  超出此阈值的都会算作 4900 ms，若需要变更此上限可以通过启动配置项 
  -Dcsp.sentinel.statistic.max.rt=xxx 来配置。
- 异常比例 (DEGRADE_GRADE_EXCEPTION_RATIO)：
  当资源的每秒请求量 >= N（可配置），并且每秒异常总数占通过量的比值超过阈值
  （DegradeRule 中的 count）之后，资源进入降级状态，即在接下的时间窗口
  （DegradeRule 中的 timeWindow，以 s 为单位）之内，对这个方法的调用
  都会自动地返回。异常比率的阈值范围是 [0.0, 1.0]，代表 0% - 100%。
- 异常数 (DEGRADE_GRADE_EXCEPTION_COUNT)：当资源近 1 分钟的异常数
  目超过阈值之后会进行熔断。注意由于统计时间窗口是分钟级别的，若 timeWindow 
  小于 60s，则结束熔断状态后仍可能再进入熔断状态。


#### Spring Cloud Sentinel的使用

1. 首先引入Spring Cloud Sentinel的依赖
    ```xml
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.alibaba.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-sentinel</artifactId>
        <version>2.1.1.RELEASE</version>
    </dependency>
    ```
2. 创建一个Controller
    ```java
    @RestController
    public class HelloController {
    
        @GetMapping("hello")
        public String hello(){
            return "hello";
        }
    
    }
    ```
3. 在application配置文件中设置sentinel相关配置
    ```yaml
    spring:
      application:
        name: spring-cloud-sentinel-dashboard-sample
      cloud:
        sentinel:
          transport:
            # sentinel控制台
            dashboard: 127.0.0.1:7777
            # 在应用对应的机器上启动一个 Http Server，该 Server 会与 Sentinel 控制台做交互
            port: 8791
            # 应用机器的IP，如在同一内网，则可以不配置
            client-ip: 127.0.0.1
    ```
4. 访问127.0.0.1:8080/hello之后，会在sentinel控制台对应的应用名下的簇点监控中看到hello这个资源，
   之后可以对资源进行限流设置

##### 自定义URL限流异常
默认情况下限流之后会返回 Blocked by Sentinel (flow limiting)

但在实际应用中，可能多数采用JSON格式数据，可以自定义限流异常来处理，实现UrlBlockHandler
```java
@Component
public class JsonResponseBlockHandler implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        //language=JSON
        String msg = "{\"code\":999,\"msg\": \"访问人数过多，请稍后再试\"}";
        httpServletResponse.getWriter().write(msg);
    }
}
```
另外，当触发限流之后，可以跳转到一个降级页面，可以通过下边的配置来实现
```properties
spring.cloud.sentinel.servlet.block-page={url}
```

##### URL资源清洗
Sentinel中HTTP服务的限流默认由Sentinel-Web-Servlet包中的CommonFilter来实现，
这个Filter会把每个不同的URL都作为不同的资源来处理。

如以下代码，提供了一个携带{id}参数的REST风格API，对于每个不同的{id}，URL也不一样，
默认情况下Sentinel会把所有的URL当做资源来进行流控
```java
@RestController
public class XController{
     
    @GetMapping("/clean/{id}")
    public String clean(@PathVariable("id") String id){
        return "clean"
    }
}
```

但在实际应用中，我们的实际需求是对/clean/*做流量控制

针对这个问题，可以通过UrlCleaner接口来实现资源清洗
```java
@Component
public class CustomUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        if (s.startsWith("/clean/")) {
            return "/clean/*";
        }
        return s;
    }
}
```

#### Sentinel集成Nacos实现动态流控规则
在使用Sentinel Dashboard设置流控规则，会有一个问题，基于Sentinel Dashboard设置的流控规则
都是保存在内存中的，一旦应用重启，这些规则都会被清除。为了解决这个问题，Sentinel提供了
动态数据源支持。

目前，Sentinel支持Consul、ZooKeeper、Redis、Nacos、Apollo、etcd等数据源的扩展。

