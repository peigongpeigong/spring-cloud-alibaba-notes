### ZooKeeper
ZooKeeper是一个高性能的分布式协调中间件，所谓的分布式协调中间件的作用类似于多线程
环境中通过并发工具包来协调线程的访问控制，只是分布式协调中间件主要解决分布式环境中
各个服务进程的访问控制问题，比如访问顺序控制。所以，在这里需要强调的是，ZooKeeper
并不是注册中心，只是基于ZooKeeper本身特性可以实现注册中心这个场景而已。

#### ZooKeeper数据结构
ZooKeeper的数据模型和分布式文件系统类似，是一种层次化的属性结构。和文件系统不同
的是，ZooKeeper的数据是结构化存储的，并没有在物理上体现出文件和目录。<br>
ZooKeeper树中的每个节点被称为Znode，Znode维护了一个stat状态信息，其中包含数据
变化的时间和版本等。并且每个Znode可以设置一个value值，ZooKeeper并不用于通用的
数据库或者大容量的对象存储，它只是管理和协调有关的数据，所以value的数据大小不建议
设置的非常大，较大的数据会带来更大的网络开销

ZooKeeper上的每个节点的数据都是允许读写的。另外，节点的创建规则和文件系统中文件的
创建规则类似，必须要按照层级创建。

#### ZooKeeper特性
ZooKeeper的Znode在被创建的时候，需要指定节点的类型，节点类型分为：
- 持久化节点：节点的数据会持久化到磁盘
- 临时节点：节点的生命周期和创建该节点的客户端的生命周期保持一致，一旦该客户端的
  回话结束，则该客户端所创建的临时节点会被自动删除
- 有序节点：在创建的节点后边会增加一个递增的序列，该序列在同一级父节点之下是唯一的，
  需要注意的是，持久化节点或者临时节点也是可以设置为有序节点的
  
3.5.3版本之后又增加了两种节点类型，分别为：
- 容器节点：当容器节点下的最后一个子节点被删除时，容器节点就会被自动删除
- TTL节点：针对持久化节点或者持久化有序节点，我们可以设置一个存活时间，如果存活时间
  内该节点没有任何修改并且没有任何子节点，他就会被自动删除。
  
#### Watcher机制
ZooKeeper提供了一种针对Znode的订阅/通知机制，也就是当Znode节点状态发生变化时或者
ZooKeeper客户端连接状态发生变化时，会触发事件通知。这个机制在服务注册与发现中，针对
服务调用者及时感知到服务提供者的变化提供了非常好的解决方案。

在ZooKeeper提供的Java API中，提供了三种机制来针对Znode进行注册监听，分别是：
- getData()：用于获取指定节点的value值，并且可以注册监听，党建听的节点进行创建、
  修改、删除操作时，会触发相应的事件通知。
- getChildren()：用于获取指定节点的所有子节点，并且允许注册监听，当监听节点的
  子节点进行创建、修改、删除操作时，触发相应的事件通知
- exists()：用于判断指定的节点是否存在，同样可以注册针对指定节点的监听，监听的时间
  类型和getData()相同
  
Watcher事件的触发是一次性的，比如客户端通过getData('/node',true)注册监听，
如果/node节点发生数据修改，那么该客户端会收到一个修改时间通知，但是/node再次发生变化时，
客户端无法收到Watcher事件，为了解决这个问题，客户端必须在收到事件回调中，再次
注册事件。

#### 常见的应用场景

##### 分布式锁
利用ZooKeeper的节点特性：临时节点以及同级节点的唯一性。
- 获得锁的过程：再获得排它锁时，左右客户端可以起ZooKeeper服务器上exclusive_locks节点下
  创建一个临时节点/lock。ZooKeeper基于同级节点的唯一性，会保证所有客户端中只有一个客户端
  能创建成功，创建成功的客户端获得了排它锁，没有获得锁的客户端就需要通过Watcher机制监听
  /exclusive_locks节点下子节点的变更事件，用于实时监听/lock节点的变化情况以做出反应
- 释放锁的过程：获得锁的时候定义锁节点/lock为临时节点，那么两种情况会触发锁的释放
    - 获得锁的客户端因为异常断开了和服务器的连接，基于临时节点的特性，/lock节点会被自动删除
    - 获得锁的客户端执行完业务逻辑后，主动删除了创建/lock节点
  当lock节点被删除后，ZooKeeper服务器会通知所有监听了/exclusive_locks子节点变化的
  客户端。这些客户端受到通知后，再次发起创建lock节点的操作来获得排它锁

##### Master选举
ZooKeeper中有两种方式来实现Master选举这一场景：
- 同一级节点不能重复创建一个已经存在的节点，有点类似于分布式锁的实现场景，其实Master选举的场景
  也是如此。假设集群中有三个节点，需要选举出Master，那么这三个节点同时去ZooKeeper服务器上创建
  一个临时节点/master-election，由于节点的特性，只会有一个客户端创建成功，创建成功的客户端所在
  的机器就成了Master。同时其他没有创建成功的客户端，针对该节点注册Watcher事件，用于监控当前
  Master机器是否存活，一旦发现Master挂了，也就是/master-election节点被删除了，那么其他的客户端
  将会重新发起Master的选举
- 利用临时有序节点的特性来实现。所有参与选举的客户端在ZooKeeper服务器的/master节点下创建
  一个临时有序节点，编号最小的节点表示Master，后续的节点可以监听前一个节点的删除事件，用于触发
  重新选举。
  
 