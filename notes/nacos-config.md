### Nacos Config

#### 基于SpringBoot 使用Nacos Config

spring-boot-nacos的依赖为
```xml
<dependency>
    <groupId>com.alibaba.boot</groupId>
    <artifactId>nacos-config-spring-boot-starter</artifactId>
    <version>0.2.4</version>
</dependency>
```
在配置文件中加入nacos的地址
```properties
nacos.config.server-addr=127.0.01:8848
```
在业务代码中需要使用配置的地方通过注解生命
```java
@Component
//指定加载dataId为example的数据源
@NacosPropertySource(dataId = "example",autoRefreshed = true)
public class Configuration {
    //设置属性的值，例子中，info为属性的key，Default Props为默认值
    @NacosValue(value = "${info:Default Props}",autoRefreshed = true)
    private String info;

    public String getInfo() {
        return info;
    }
}
```

#### Spring Cloud Nacos的使用
首先引入依赖
```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    <version>2.2.1.RELEASE</version>
</dependency>
```
接下来创建boorstrap.aapplication
```properties
# 指定应用名
spring.application.name=spring-cloud-nacos-config

# 指定nacos-server地址
spring.cloud.nacos.config.server-addr=127.0.0.1:8848

# 表示dataId的前缀
#spring.cloud.nacos.config.prefix=example

#在不指定spring.cloud.nacos.config.prefix的情况下，
# nacos会默认使用${spring.application.name}.${file-extensions:properties}作为默认的dataId
# 也就是 spring-cloud-nacos-config.properties
# 下面的方式是指定使用${spring.application.name}.${file-extensions:properties}
# 也就是spring-cloud-nacos-config.yaml作为dataId
spring.cloud.nacos.config.file-extension=yaml

# 指定namespace
spring.cloud.nacos.config.namespace=43cf82d9-f90e-4789-851b-346e785d09b7
# 指定组
spring.cloud.nacos.config.group=TEST_GROUP

# 指定使用哪个环境的配置
# 如：
# 在不指定prefix的情况下，会使用${spring.application.name}-${spring.profiles.active}.${file-extension}作为dataId
spring.profiles.active=test
```