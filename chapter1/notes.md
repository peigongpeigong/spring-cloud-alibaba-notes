## 微服务发展史

### 微服务与SOA的区别
- SOA关注的是服务的重用性及 解决信息孤岛问题
- 微服务关注的是解耦，虽然解耦和可重用性从特定的角度来看是一样的，但
  本质上是有区别的，解耦是降低业务之间的耦合度，而重用性关注的是服务
  的复用
- 微服务会更多地关注在DevOps的持续交付上，因为服务粒度细化之后使得
  开发运维变得更加重要，因此为服务与容器化技术的结合更加紧密

### 微服务的优点
- 复杂度可控：通过对共享业务服务更细粒度的拆分，一个服务只需要关注一个特定的
  业务领域，并通过定义良好的接口清晰表述服务便捷。由于体积小、复杂度低，开发、
  维护会更加简单
- 技术选型灵活：每个微服务可有不同的团队来维护，所以可以结合业务特性自行选择技术栈
- 可扩展性强：可根据每个微服务的性能要求和业务特点来对服务进行灵活扩展，比如通过增加单个服务
  的集群规模，提升部署了该服务的节点的硬件配置
- 独立部署：由于每个微服务都是一个独立运行的进程，所以可以实现独立部署。当某个服务发生变更时
  不需要重新编译部署整个系统，并且单个微服务代码量比较少，使得发布更加高效
- 容错性：在微服务架构中，如果某一个服务发生故障，可以使故障隔离在单个服务中。其他服务可以通过
  重试、降级等机制来实现应用层面的容错

### 微服务架构面临的挑战
- 分布式架构的复杂性：微服务本身构建的是一个分布式系统，分布式系统设计服务之间的
  远程通信，而网络通信中网络的延迟和网络故障是无法避免的，从而增加了应用程序的复杂度
- 服务依赖：微服务数量增加之后，个服务之间会存在更多的依赖关系，使得系统整体更为复杂。
- 故障排查：一次请求可能会经历多个不同的微服务的多次交互，交互的链路可能会比较长，每个
  微服务会产生自己的日志，在这种情况下如果出现一个故障，开发人员定位问题的根源会比较
  困难
- 服务监控：在一个单体架构中很容易实现服务的监控，因为所有功能都在一个服务器中。
  在微服务架构中，服务监控开销会非常大
- 运维成本：在微服务中，需要保证几百个微服务的正常运行，对于维护的挑战是巨大的。