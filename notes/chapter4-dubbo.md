### Dubbo概念
Dubbo是一个分布式服务框架，主要实现多个系统之间的高性能、透明化的调用，简单来说他就是一个RPC框架，
但是和普通的RPC框架不同的是，他提供了服务治理功能，比如服务注册、监控、路由、容错等。

#### 促使Dubbo框架产生的原因有两个

- 在大规模服务化之后，服务越来越多，服务消费者在调用服务提供者的服务时，需要在配置文件中维护服务提供者的URL地址，
  当服务提供者出现故障或者动态扩容时，所有相关的服务消费者都需要更新本地配置的URL地址，这种维护成本非常高。这个时候，
  实现服务的上下线动态感知及服务地址的动态维护就显得非常重要了
- 随着用户访问量的增大，后端服务为了支撑更大的访问量，会通过增加服务器来扩容。但是，哪些服务要扩容，哪些服务要缩容，
  需要一个判断依据，也就是说需要知道每个服务的调用量及响应时间，这个时候，就需要有一种监控手段，使用监控的数组作为容
  量规划的参考值，从而实现根据不同服务的访问情况来合理的调控服务器资源，提高机器的利用率。
  

Dubbo更像一个生态，它提供了很多比较主流框架的集成：
- 支持多种协议的服务发布，默认是dubbo://，还可以支持rest://、webservice://、thrift://等
- 支持多种不同的注册中心，如Nacos、ZooKeeper、Redis，未来还将会支持Consul、Eureka
- 支持多种序列化技术，如avro、fst、fastjson、hessian2、kryo等

除此之外，Dubbo在服务治理方面的功能非常完善，比如集群容错、服务路由、负载均衡、服务降级、
服务限流、服务监控、安全验证等。

#### 集群容错
Dubbo提供了6种容错模式，默认为Failover Cluster。如果这6种容错模式不能满足实际需求，还可以
自行扩展。
- Failover Cluster：失败自动切换。当服务调用失败后，会切换到集群中的其他机器进行重试，默认
  重试次数为2，通过属性retries=2可以修改次数。这种容错通常用与读操作，因为事务型操作会带来
  数据重复问题。
- Failfast Cluster：快速失败。当服务调用失败之后，立即报错，也就是只发起一次调用。通常
  用于一些幂等的写操作，比如新增数据，因为当服务调用失败时，很可能这个请求已经在服务器端处理成功，
  只是因为网络延迟导致响应失败，为了避免在结果不确定的情况下导致数据重复插入的问题，可以使用这种容错机制
- Failsafe Cluster：失败安全。也就是出现异常时，直接忽略异常。
- Failback Cluster：失败后自动回复。服务调用出现异常时，在后台记录这条失败的请求定时重发。
  这种模式适合用于消息通知操作，保证这个请求一定发送成功
- Forking Cluster：并行调用及群众的多个服务，只要其中一个成功就返回。可以通过forks=2来
  设置最大并行数
- Broadcast Cluster：广播调用所有的服务提供者，任意一个服务报错则表示服务调用失败。
  这种机制通常用于通知所有的服务提供者更新缓存或者本地资源信息
  
```java
//使用方式
@Service(cluster="failfast")
public class HelloServiceImpl implements HelloService {

    @Value("${spring.application.name}")
    private String serviceName;

    public String hello(String name) {
        return String.format("[%s]: Hello, %s",serviceName,name);
    }
}
```
在实际应用中，查询语句容错策略建议使用默认的Failover Cluster，而增删改操作建议使用Failfast Cluster
或者使用Failover Cluster(retries=0)策略，防止出现数据重复添加等其他问题。

#### 负载均衡
在访问量较大的情况下，我们会通过水平扩容的方式增加多个节点拉平衡请求的流量，从而提升服务的整体
性能。

在Dubbo中提供了4中负载均衡策略，默认负载均衡策略是random。同样，如果这4种策略不能满足实际需求，
我们可依据与Dubbo中的SPI机制来扩展。
- Random LoadBalance：随机算法，可以针对性能较好的服务器设置较大的权重值，权重值越大，随机
  的概率也会越大
- RoundRobin LoadBalance：轮询。按照公约后的权重设置轮询比例
- LeastActive LoadBalance：最少活跃调用书。处理及傲慢的节点将会收到更少的请求
- ConsistentHash LoadBalance：一致性Hash。相同参数的请求总是发送到同一个服务提供者。

```java
//配置方式，在@Service注解上增加loadbalance参数
@Service(cluster="failfast", loadbalance="roundrobin")
```

#### 服务降级
服务降级是一种系统保护策略，当服务器访问压力较大时，可以根据当前业务情况对不重要的服务进行降级，
以保证核心服务的正常运行。所谓的降级，就是把一些非必要的功能载流量较大的时间段暂时关闭。

降级有多个层面的分类
- 按照是否自动化可以分为自动降级和人工降级
- 按照功能可以分为读服务降级和写服务降级

人工降级一般具有一定的前置性，比如在电商大促之前，暂时关闭某些非核心服务，如评价、推荐等。
而自动降级更多来自于系统出现某些异常的时候自动触发"兜底的流畅"，如：
- 故障降级：调用的远程服务挂了，网络故障或者RPC服务返回异常。这类情况在业务允许的情况下
  可以通过设置兜底数据响应给客户端
- 限流降级：不管是什么类型的系统，他所支撑的流量是有限的，为了保护系统不被压垮，在系统中会
  针对可新业务进行限流。当请求达到阈值时，后续的请求会被拦截，这类请求可以进入排队系统，比如12306.
  也可以直接返回降级页面，如返回"活动太火爆，请稍后再来"
  
Dubbo提供了一种Mock配置来实现服务降级，也就是说当服务提供方出现网络异常无法访问时，客户端
不抛出异常，而是通过降级配置返回兜底数据，操作步骤如下：
```java
//在Consumer项目中创建一个MockService类，实现需要自动降级的接口即可
public class MockHelloService implements HelloService{
    public String hello(String name){
        return "服务无法访问，返回降级数据";    
    }
}
//在引用是增加mock参数
@RestController
public class HelloController{

    @Reference(mock=xxx.xxx.xx.MockHelloService, cluater="failfast")
    private HelloService helloService;
    
    @GetMapping("hello")
    public String hello(){
        return helloService.hello("p");
    }

}
```