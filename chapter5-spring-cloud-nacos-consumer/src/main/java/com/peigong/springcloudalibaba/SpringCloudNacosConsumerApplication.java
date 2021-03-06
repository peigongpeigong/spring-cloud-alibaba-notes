package com.peigong.springcloudalibaba;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: lilei
 * @create: 2020-07-21 15:01
 **/
@SpringBootApplication
@DubboComponentScan
public class SpringCloudNacosConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNacosConsumerApplication.class, args);
    }

}
